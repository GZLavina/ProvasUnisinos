
public class Operacao {
	private Compromisso inserido;
	private Compromisso[] removidos;
	
	public Operacao(Compromisso ins) {
		this.inserido = ins;
	}
	
	public Operacao() {
		this.removidos = new Compromisso[10];
	}
	
	public void insere(Compromisso c) {
		for (int i = 0; i < removidos.length; i++) {
			if (removidos[i] == null) {
				removidos[i] = c;
				return;
			}
		}
		
		Compromisso[] novoRemovidos = new Compromisso[removidos.length+10];
		for (int i = 0; i < removidos.length; i++) {
			novoRemovidos[i] = removidos[i];
		}
		novoRemovidos[removidos.length] = c;
		this.removidos = novoRemovidos;
	}

	public Compromisso getInserido() {
		return inserido;
	}

	public Compromisso[] getRemovidos() {
		return removidos;
	}
}
