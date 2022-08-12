import java.util.ArrayList;
import java.io.*;

public class SQLServer {
	
	private String nomeArquivo;
	private ArrayList<Tabela> base;
	
	@SuppressWarnings("unchecked")
	public void connect(String nomeBaseDados) {
		String nome = nomeBaseDados + ".db";
		this.nomeArquivo = nome;
		File f = new File(nome);
		if (f.exists()) {
			try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
				
				this.base = (ArrayList<Tabela>) ois.readObject();
				System.out.println("Base de dados carregada do arquivo " + nome + " com sucesso!");
				
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("Erro ao tentar ler " + nome);
			}
		} else {
			System.out.println("Criando nova base de dados.");
			this.base = new ArrayList<>();
		}
	}
	
	public void execute(String sqlStatement) {
		if (this.nomeArquivo == null) {
			System.out.println("Nenhuma base de dados conectada!");
			return;
		}
		
		if (sqlStatement.startsWith("create table"))
			create(sqlStatement);
		else if (sqlStatement.startsWith("insert into"))
			insert(sqlStatement);
		else if (sqlStatement.startsWith("delete from"))
			delete(sqlStatement);
		else
			System.out.println("Statement nao reconhecido.");
	}
	
	public void create(String str) {
		try {
			
			String[] tokens = str.split(" ");
			String nome = tokens[2];
			
			// verifica se ja existe
			for (Tabela tab : this.base) {
				if (tab.getNome().equalsIgnoreCase(nome))
					return;
			}
			
			// verifica se o comando tem colunas validas.
			if (!tokens[3].startsWith("(")) {
				System.out.println("Criacao de tabela formatada incorretamente!");
				return;
			} 
			
			str = str.substring(15 + nome.length(), str.length()-1);
			tokens = str.split(",");			

			// monta as listas para inicializar uma nova tabela
			ArrayList<Character> tipos = new ArrayList<>();
			ArrayList<String> nomes = new ArrayList<>();
			String[] nomeTipo;
			for (int i = 0; i < tokens.length; i++) {
				tokens[i] = tiraEspacos(tokens[i]);
				nomeTipo = tokens[i].split(" ");
				if (nomeTipo[1].equals("text")) {
					tipos.add('T');
					nomes.add(nomeTipo[0]);
				} else if (nomeTipo[1].equals("int")) {
					tipos.add('N');
					nomes.add(nomeTipo[0]);
				} else {
					System.out.println("Coluna inserida incorretamente! Tabela nao foi criada.");
					return;
				}
			}
			
			this.base.add(new Tabela(nome, tipos, nomes));
			
		} catch(IndexOutOfBoundsException e) {
			System.out.println("Statement mal-formatado!");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Erro ao tentar criar nova tabela!");
		}
	}
	
	public void insert(String str) {
		try {
			
			String[] tokens = str.split(" ");
			Tabela tab;
			if ((tab = getTabela(tokens[2])) == null) {
				System.out.println("Nao ha tabela com esse nome na base de dados!");
				return;
			}
			
			// pega substring com os valores.
			String valoresStr = str.substring(20 + tokens[2].length(), str.length()-1);
			String[] valores = valoresStr.split(",");
			
			// compara os valores inseridos com os tipos da tabela e depois insere na linha a ser adicionada.
			ArrayList<Character> tipos = tab.getTipos();
			ArrayList<String> linha = new ArrayList<>();
			char tipo;
			for (int i = 0; i < valores.length; i++) {
				if (valores[i].contains(" "))
					valores[i] = tiraEspacos(valores[i]);
				
				tipo = verificaTipo(valores[i]);
				if (tipo == 'T' && tipo == tipos.get(i)) {
					linha.add(valores[i].substring(1, valores[i].length()-1));
				} else if (tipo == 'N' && tipo == tipos.get(i)) {
					linha.add(valores[i]);
				} else if (tipo == 'X') {
					System.out.println("valor inserido incorretamente!");
					return;
				} else {
					System.out.println("Valores inseridos nao estao de acordo com a estrutura da tabela.");
					return;
				}
			}
			
			tab.insert(linha);
			
		} catch (IndexOutOfBoundsException e) {
			System.out.println("Statement mal-formatado ou com quantidade incorreta de valores!");
		} catch (Exception e) {
			System.out.println("Erro ao tentar inserir novos valores na tabela!");
		}
	}
	
	public Tabela getTabela(String nome) {
		Tabela tab = null;
		for (Tabela t : this.base) {
			if (t.getNome().equals(nome)) {
				tab = t;
				break;
			}
		} 
		
		return tab;
	}
	
	// verifica se o valor eh texto ou int
	public char verificaTipo(String str) {
		if (str.startsWith("'") && str.endsWith("'") && str.length() > 1 && !str.substring(1, str.length()-1).isBlank())
			return 'T';
		
		try {
			
			@SuppressWarnings("unused")
			int i = Integer.parseInt(str);
			return 'N';
			
		} catch (NumberFormatException e) {
			return 'X';
		}
	}
	
	// tira espacos se estiverem em posicoes desnecessarias (inicio ou fim).
	public String tiraEspacos(String str) {
		if (str.startsWith(" "))
			str = str.substring(1, str.length());
		if (str.endsWith(" "))
			str = str.substring(0, str.length()-1);
		
		return str;
	}
	
	public void delete(String str) {
		try {
			String[] tokens = str.split(" ");
			Tabela tab;
			if ((tab = getTabela(tokens[2])) == null) {
				System.out.println("Nao ha tabela com esse nome na base de dados!");
				return;
			} if (tokens.length == 3) {
				deletaTudo(tab);
				return;
			} if (!tokens[3].equals("where") || tokens.length != 7) {
				System.out.println("Delecao formatada incorretamente!");
				return;
			} if (!verificaOperador(tokens[5])) {
				System.out.println("Operador invalido!");
				return;
			}
			
			// procura o indice da coluna escolhida
			ArrayList<String> nomeCols = tab.getLinha(0);
			int i;
			boolean existe = false;
			for (i = 0; i < nomeCols.size(); i++) {
				if (nomeCols.get(i).equalsIgnoreCase(tokens[4])) {
					existe = true;
					break;
				}
			}
			if (!existe) {
				System.out.println("Coluna " + tokens[4] + " nao existe na tabela " + tab.getNome() + "!");
				return;
			}
			
			char tipo = tab.getTipo(i);
			ArrayList<String> linha;
			// se o tipo da coluna eh numero, faz a comparacao com normal
			if (tipo == 'N') {
				for (int j = 1; j <= tab.size(); j++) {
					linha = tab.getLinha(j);
					if (compara(Integer.parseInt(linha.get(i)), tokens[5], Integer.parseInt(tokens[6]))) {
						tab.deleta(j);
						j--; // repete o valor de j para a proxima iteracao, ja que ArrayList e size() se atualizam na remocao.
					}
				}
			} else {
				// se for texto, suporta apenas = .
				if (!tokens[5].equals("=")) {
					System.out.println("Operador invalido para texto");
					return;
				} if (tokens[6].startsWith("'") && tokens[6].endsWith("'"))
					tokens[6] = tokens[6].substring(1, tokens[6].length()-1);
				
				for (int j = 1; j < tab.size(); j++) {
					linha = tab.getLinha(j);
					if (linha.get(i).equalsIgnoreCase(tokens[6])) {
						tab.deleta(j);
						j--; // repete o valor de j para a proxima iteracao.
					}
				}
			}
			
		} catch (NumberFormatException e){
			System.out.println("Numero invalido!");
		} catch (IndexOutOfBoundsException e) {
			System.out.println("Statement mal-formatado ou com quantidade incorreta de valores!");
		} catch (Exception e) {
			System.out.println("Erro ao tentar inserir novos valores na tabela!");
		}
			
	}
	
	public boolean verificaOperador(String op) {
		if (op.equals(">") || op.equals(">=") || op.equals("=") || op.equals("<=") || op.equals("<") || op.equals("!=")) {
			return true;
		}
		
		return false;
	}
	
	public boolean compara(int tab, String op, int user) throws UnsupportedOperationException {
		if (op.equals(">")) {
			if (tab > user)
				return true;
			else
				return false;
		} if (op.equals(">=")) {
			if (tab >= user)
				return true;
			else
				return false;
		} if (op.equals("=")) {
			if (tab == user)
				return true;
			else
				return false;
		} if (op.equals("<=")) {
			if (tab <= user)
				return true;
			else
				return false;
		} if (op.equals("<")) {
			if (tab < user)
				return true;
			else
				return false;
		} if (op.equals("!=")) {
			if (tab != user)
				return true;
			else
				return false;
		}
		
		System.out.println("Erro ao verificar se um elemento da tabela satisfazia a condicao!");
		return false;
	}
	
	public void deletaTudo(Tabela tab) {
		for (int i = tab.size(); i > 0; i--) {
			tab.deleta(i);
		}
	}
	
	public String query(String sqlStr) {
		if (this.nomeArquivo == null) {
			return "Nenhuma base de dados conectada!";
		}
		
		try {
			
			String[] tokens = sqlStr.split(" ");
			if (!tokens[0].equalsIgnoreCase("select") || !tokens[tokens.length-2].equals("from")) {
				return "Query deve comecar com \"select\" e terminar com \"from 'nome_tabela'\"!";
			}
			
			Tabela tab;
			if ((tab = getTabela(tokens[tokens.length - 1])) == null) {
				return "Nao ha tabela com esse nome na base de dados!";
			}
			
			if (tokens[1].equals("*")) {
				int[] colunas = new int[tab.getTipos().size()];
				for (int i = 0; i < colunas.length; i++) {
					colunas[i] = i;
				}
				return montaTabela(tab, colunas);
			}
			
			// pega so as colunas e separa por virgulas
			sqlStr = sqlStr.substring(7, sqlStr.length() - (6 + tokens[tokens.length-1].length()));
			String[] arrCol = sqlStr.split(",");
			
			// monta um array com os indices das colunas que o usuario informou
			ArrayList<String> nomes = tab.getLinha(0);
			int[] colunas = new int[arrCol.length];
			r1:for (int i = 0; i < arrCol.length; i++) {
				arrCol[i] = tiraEspacos(arrCol[i]);
				for (int j = 0; j < nomes.size(); j++) {
					if (nomes.get(j).equalsIgnoreCase(arrCol[i])) {
						colunas[i] = j;
						continue r1;
					}
				}
				return "Coluna nao existente no statement!";
			}
			
			// passa o array montado
			return montaTabela(tab, colunas);
			
		} catch(IndexOutOfBoundsException e) {
			e.printStackTrace();
			return "Erro ao ler statement de query! (IndexOutOfBoundsException)";
		} catch(Exception e) {
			//e.printStackTrace();
			return "Erro desconhecido ao ler statement de query!";
		}
		
	}
	
	public String montaTabela(Tabela tab, int[] colunas) {
		StringBuilder sb = new StringBuilder();
		sb.append("Tabela " + tab.getNome() + ":\n");
		
		int[] countTipos = new int[2]; // conta a quantidade de colunas de texto ou de numero.
		ArrayList<Character> tipos = tab.getTipos();
		ArrayList<String> linha = tab.getLinha(0);
		// monta os nomes das colunas selecionadas.
		for (int i = 0; i < colunas.length; i++) {
			if (tipos.get(colunas[i]) == 'T') {
				sb.append(String.format("%-20s", linha.get(colunas[i]))); 
				countTipos[0]++;
			} else {
				sb.append(String.format("%-10s", linha.get(colunas[i])));
				countTipos[1]++;
			}
			
			sb.append(" | ");		
		} sb.append("\n");
		
		// faz a linha de separacao do nome das colunas e linhas da tabela.
		for (int i = 0; i < countTipos[0]; i++) {
			sb.append("====================");
		} for (int i = 0; i < countTipos[1]; i++) {
			sb.append("==========");
		} for (int i = 0; i < colunas.length; i++) {
			if (i != colunas.length-1)
				sb.append("===");
			else
				sb.append("==");
		} sb.append("\n");
		
		// mostra as linhas.
		for (int i = 1; i <= tab.size(); i++) {
			linha = tab.getLinha(i);
			for (int j = 0; j < colunas.length; j++) {
				if (tipos.get(colunas[j]) == 'T')
					sb.append(String.format("%-20s", linha.get(colunas[j])));
				else
					sb.append(String.format("%10s", linha.get(colunas[j])));
				
				sb.append(" | ");		
			}
			sb.append("\n");
		}
		
		return sb.toString();
	}
	
	public void close() {
		File f = new File(nomeArquivo);
		try (ObjectOutputStream ois = new ObjectOutputStream(new FileOutputStream(f))) {
			
			ois.writeObject(this.base);
			System.out.println("Base de dados salva em " + nomeArquivo + " com sucesso!");
			this.nomeArquivo = null;
			
		} catch (IOException e) {
			System.out.println("Erro ao tentar gravar base de dados em " + nomeArquivo);
		}
	}
	
//	public void teste(String nomeTab) {
//		Tabela tab;
//		if ((tab = getTabela(nomeTab)) == null) {
//			System.out.println("Nao ha tabela com esse nome na base de dados!");
//			return;
//		}
//		
//		ArrayList<ArrayList<String>> tabela = tab.getTabela();
//		
//		for (ArrayList<String> linha : tabela) {
//			System.out.println(linha);
//		}
//	}
}
