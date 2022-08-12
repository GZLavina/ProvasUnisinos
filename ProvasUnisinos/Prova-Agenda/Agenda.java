import java.io.*;

public class Agenda {
	private Compromisso[] agenda;
	private Operacao[] operacoes;
	
	public Agenda() {
		this.agenda = new Compromisso[10];
		this.operacoes = new Operacao[5];
	}
	
	public void agendaCompromisso(String nome, String data, String horario, int duracao, String[] tags) {
		
		if (!validaData(data)) {
			System.out.println("Data invalida!");
			return;
		} if (!validaHorario(horario)) {
			System.out.println("Horario invalido!");
			return;
		}
		
		for (int i = 0; i < agenda.length; i++) {
			if (agenda[i] != null && agenda[i].getNome().equalsIgnoreCase(nome) && agenda[i].getData().equals(data)) {
				System.out.println("Este compromisso já estava marcado.");
				return;
			}			
		}	
		
		Compromisso c = new Compromisso(nome, data, horario, duracao, tags);
		guardaOperacao(new Operacao(c));
		
		for (int i = 0; i < agenda.length; i++) {
			if (agenda[i] == null) {
				agenda[i] = c;
				return;
			}
		}
				
		agenda = aumentaArray(agenda, 10, c);
	}
	
	public boolean validaData(String data) {
		try {
			
			String[] diaEMes = data.split("/");
			int dia = Integer.parseInt(diaEMes[0]);
			int mes = Integer.parseInt(diaEMes[1]);
			int diaMax = 0;
			
			if (mes == 4 || mes == 6 || mes == 9 || mes == 11)
				diaMax = 30;
			else if (mes == 2)
				diaMax = 28;
			else if (mes > 0 && mes < 13)
				diaMax = 31;
			
			if (dia > 0 && dia <= diaMax)
				return true;
			
			return false;
		
		} catch(Exception e) {
			return false;
		}
	}
	
	public boolean validaHorario(String horario) {
		try {
			
			String[] horaEMin = horario.split(":");
			int hora = Integer.parseInt(horaEMin[0]);
			int min = Integer.parseInt(horaEMin[1]);
			
			if (hora >= 0 && hora <= 23 && min >= 0 && min <= 59)
				return true;
			
			return false;
			
		} catch(Exception e) {
			return false;
		}
	}
	
	public void visualizarCompromissos(String campos) {
		
		organiza();
		
		System.out.println("COMPROMISSOS NA AGENDA: ");
		System.out.println();
		for (int i = 0; i < agenda.length; i++) {
			if (agenda[i] == null) {   
				System.out.println();
				return;
			}
			
			if (campos.toLowerCase().contains("nome")) 
				System.out.println(agenda[i].getNome());
			if (campos.toLowerCase().contains("data")) 
				System.out.println("Compromisso no dia " + agenda[i].getData() + ", às " + agenda[i].getHorario());
			if (campos.toLowerCase().contains("dura")) 
				System.out.println("Duração de " + agenda[i].getDuracao() + " minutos.");
			if (campos.toLowerCase().contains("tag")) {
				String[] tags = agenda[i].getTags();
				String tag;
				StringBuilder sb = new StringBuilder();
				for (int j = 0; j < tags.length; j++) {
					if (tags[j] == null)
						break;
					if (j == 0)
						sb.append("Tags: ");
					else
						sb.append(", ");
					
					tag = tags[j];
					if (sb.length() % 34 == 0 || (sb.length() % 34) + tag.length() >= 34)
						sb.append("\n" + tag);
					else
						sb.append(tag);
				}
				if (sb.toString().contains("Tags"))
					sb.append(".");
				else
					sb.append("Sem tags.");
				
				System.out.println(sb);
			}
			System.out.println("==================================");
		}
		System.out.println();
	}
	
	public void organiza() {  // organiza usando insertion sort.
		r1:for (int i = 1; i < agenda.length; i++) {
			if (agenda[i] == null)
				continue r1;
			r2:for (int j = i; j > 0; j--) {	
				if (agenda[j-1] == null) {
					agenda[j-1] = agenda[j];
					agenda[j] = null;
					continue r2;
				}
				
				String[] diaEMesA = agenda[j].getData().split("/");
				int diaA = Integer.parseInt(diaEMesA[0]);
				int mesA = Integer.parseInt(diaEMesA[1]);
				
				String[] diaEMesB = agenda[j-1].getData().split("/");
				int diaB = Integer.parseInt(diaEMesB[0]);
				int mesB = Integer.parseInt(diaEMesB[1]);
				
				if (mesA < mesB || (mesA == mesB && diaA < diaB)) {
					Compromisso temp = agenda[j-1];
					agenda[j-1] = agenda[j];
					agenda[j] = temp;
					continue r2;
				} if (mesA > mesB || diaA > diaB) {
					continue r1;
				}
				
				String[] horaEMinA = agenda[j].getHorario().split(":");
				int horaA = Integer.parseInt(horaEMinA[0]);
				int minA = Integer.parseInt(horaEMinA[1]);	
				
				String[] horaEMinB = agenda[j-1].getHorario().split(":");    
				int horaB = Integer.parseInt(horaEMinB[0]);
				int minB = Integer.parseInt(horaEMinB[1]);
				
				if (horaA < horaB || (horaA == horaB && minA < minB) 
						|| (horaA == horaB && minA == minB && agenda[j].getDuracao() < agenda[j-1].getDuracao())) {
					Compromisso temp = agenda[j-1];
					agenda[j-1] = agenda[j];
					agenda[j] = temp;
					continue r2;
				} 
				
				continue r1;
			}
		}
	}
	
	public void removeCompromissos(int mesA) {
		boolean removeu = false;
		Operacao op = new Operacao();
		System.out.println("Removendo compromissos do mes " + mesA + "...");
		for (int i = 0; i < agenda.length; i++) {
			if (agenda[i] == null)
				continue;
			
			String[] diaEMes = agenda[i].getData().split("/");
			int mesB = Integer.parseInt(diaEMes[1]);
			
			if (mesA == mesB) {
				op.insere(agenda[i]);
				agenda[i] = null;
				removeu = true;
			}
		}
		
		if (removeu) {
			System.out.println("Remoção efetuada com sucesso.");

			guardaOperacao(op);
		}
		else
			System.out.println("Nenhum compromisso encontrado no mes inserido.");
		System.out.println();
	}
	
	public void removeCompromissos(String tipo, String criterio) {
		boolean removeu = false;
		Operacao op = new Operacao();
		if (tipo.toLowerCase().contains("tag")) {
			System.out.println("Removendo compromissos com a tag \"" + criterio + "\"...");
			for (int i = 0; i < agenda.length; i++) {
				if (agenda[i] == null)
					continue;
				
				String[] tags = agenda[i].getTags();
				for (int j = 0; j < tags.length; j++) {
					if (tags[j] != null && tags[j].equalsIgnoreCase(criterio)) {
						op.insere(agenda[i]);  
						agenda[i] = null;
						removeu = true;
						break;
					}
				}
			} 
		}
		
		else if (tipo.equalsIgnoreCase("nome")) {
			System.out.println("Removendo compromissos com o nome \"" + criterio + "\"...");
			for (int i = 0; i < agenda.length; i++) {
				if (agenda[i] != null && agenda[i].getNome().equalsIgnoreCase(criterio)) {
					op.insere(agenda[i]); 
					agenda[i] = null;
					removeu = true;
				}
			}
		}
		
		if (removeu) {
			System.out.println("Remoção efetuada com sucesso.");
			guardaOperacao(op);
		}
		else
			System.out.println("Nenhum compromisso com o(s) critério(s) encontrado.");
		System.out.println();
	}
	
	public void procuraCompromisso(String tag) {
		organiza();
		
		System.out.println("Procurando compromissos com a tag \"" + tag + "\"...");
		System.out.println();
		String str = procuraCompromisso(tag, 0, new StringBuilder());
		if (str.isEmpty()) {
			System.out.println("Nenhum compromisso com essa tag foi encontrado.");
			System.out.println();
			return;
		}
		
		System.out.println(str);
		System.out.println("Busca terminada.");
		System.out.println();
	}	
	
	public String procuraCompromisso(String tag, int i, StringBuilder sb) {
		if (i == agenda.length || agenda[i] == null) {
			return sb.toString();
		}
		
		if (procuraCompromisso(tag, agenda[i].getTags(), 0)) {
			sb.append(agenda[i]);
			sb.append("==================================\n");
		}
		
		procuraCompromisso(tag, i + 1, sb);
		
		return sb.toString();
	}
	
	public boolean procuraCompromisso(String tag, String[] tags, int i) {
		if (i == tags.length || tags[i] == null)
			return false;
		
		if (tags[i].equalsIgnoreCase(tag))
			return true;
		
		return procuraCompromisso(tag, tags, i + 1);
	}
	
	public void salvaAgenda(String nomeArquivo) {
		File f = new File(nomeArquivo);
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f))) {
			
			organiza();  // deixa os null no final.
			
			for (int i = 0; i < agenda.length; i++) {			
				//if (agenda[i] != null)
					oos.writeObject(agenda[i]);
				//else
					//break;
			}
			
			//oos.writeObject(null); // delimita o final do arquivo.
			
			System.out.println("Agenda salva no arquivo " + nomeArquivo);
			
			oos.close();
			
		} catch(IOException e) {
			System.out.println("Erro ao salvar agenda.");
			e.printStackTrace();
		}
	}
	
	public void carregaAgenda(String nomeArquivo) {
		File f = new File(nomeArquivo);        
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))){
			
			
			Compromisso[] temp = new Compromisso[20]; 
			Compromisso c;
			int i = 0;
			
			while ((c = (Compromisso) ois.readObject()) != null) {
				if (i == temp.length)
					temp = aumentaArray(temp, 10);
				temp[i] = c;
				i++;
			}
				
			
			this.agenda = temp;
			
			this.operacoes = new Operacao[5];  // Nao permite que sejam desfeitas operacoes anteriores ao carregamento.
			
			ois.close();
	
			System.out.println("Agenda carregada do arquivo " + nomeArquivo);
			
		} catch (ClassNotFoundException e) {
			System.out.println("Class not found.");
		} catch (EOFException e) {
			System.out.println("Alcancou o final do arquivo.");
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			System.out.println("Arquivo nao encontrado.");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Excecao de IO.");
			e.printStackTrace();
		}
	}
	
	public void guardaOperacao(Operacao op) {
		Operacao tempA = this.operacoes[0];
		this.operacoes[0] = op; // insere a operacao mais recente no inicio do array, depois vai "empurrando" as outras.
		Operacao tempB;
		for (int i = 1; i < 5; i++) {
			tempB = this.operacoes[i];
			this.operacoes[i] = tempA;
			tempA = tempB;
		}
	}
	
	public void desfaz() {  // desfaz uma operacao.
		if (this.operacoes[0] == null) {
			System.out.println("No momento é impossível desfazer operações.");
			return;
		}
		
		Operacao op = this.operacoes[0];
		
		if (op.getInserido() != null) {
			Compromisso c = op.getInserido();
			for (int i = 0; i < agenda.length; i++) {
				if (agenda[i] != null && agenda[i].equals(c)) {
					agenda[i] = null;
					break;
				}
			}
		} 
		else {
			Compromisso[] arrComp = op.getRemovidos();
			r1:for (int i = 0; i < arrComp.length; i++) {
				for (int j = 0; j < agenda.length; j++) {
					if (agenda[j] == null) {
						agenda[j] = arrComp[i];
						continue r1;
					}
				}
			}
		}
		
		System.out.println("Operação desfeita!");
		
		for (int i = 0; i < 4; i++) {
			operacoes[i] = operacoes[i+1];   // retira a opercao mais recente e coloca as próximas no inicio.
			operacoes[i+1] = null;
		}	
	}
	
	public Compromisso[] aumentaArray(Compromisso[] c, int i) {
		Compromisso[] novoArr = new Compromisso[c.length + i];
		for (int j = 0; j < c.length; j++) {
			novoArr[j] = c[j];
		}
		return novoArr;
	}
	
	public Compromisso[] aumentaArray(Compromisso[] arr, int i, Compromisso c) {
		Compromisso[] novoArr = new Compromisso[arr.length + i];
		for (int j = 0; j < arr.length; j++) {
			novoArr[j] = arr[j];
		}
		novoArr[arr.length] = c;
		return novoArr;
	}
}