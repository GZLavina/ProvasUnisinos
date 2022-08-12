import java.util.ArrayList;
import java.io.Serializable;


@SuppressWarnings("serial")
public class Tabela implements Serializable{
	
	private String nome;
	private ArrayList<Character> tipos;
	private ArrayList<ArrayList<String>> tabela; // o primeiro ArrayList<String> possui o nome das colunas.
	private int size;
	
	public Tabela(String nome, ArrayList<Character> tipos, ArrayList<String> primLinha) {
		this.nome = nome;
		this.tipos = tipos;
		this.tabela = new ArrayList<>();
		this.tabela.add(primLinha);
		this.size = 0;
	}

	public void insert(ArrayList<String> linha) {
		tabela.add(linha);
		size++;
	}
	
	public String getNome() {
		return nome;
	}

	public ArrayList<Character> getTipos() {
		return tipos;
	}
	
	public char getTipo(int index){
		return tipos.get(index);
	}
	
	public ArrayList<ArrayList<String>> getTabela(){
		return tabela;
	}
	
	public int size() {
		return size;
	}
	
	public ArrayList<String> getLinha(int index) {
		return tabela.get(index);
	}
	
	public void deleta(int index) {
		tabela.remove(index);
		size--;
	}
}
