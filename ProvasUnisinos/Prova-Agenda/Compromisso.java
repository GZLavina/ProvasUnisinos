import java.io.Serializable;

@SuppressWarnings("serial")
public class Compromisso implements Serializable {
	private String nome;
	private String data;       // Entendi pelo enunciado que esses atributos devem ser String
	private String horario;    // Se eu entendi errado, o metodo que organiza a agenda seria mais simples.
	private int duracao;
	private String[] tags;
	
	public Compromisso(String nome, String data, String horario, int duracao, String[] tags) {
		this.nome = nome;
		this.data = data;
		this.horario = horario;
		this.duracao = duracao;
		this.tags = tags;
	}

	public String getNome() {
		return nome;
	}

	public String getData() {
		return data;
	}

	public String getHorario() {
		return horario;
	}

	public int getDuracao() {
		return duracao;
	}

	public String[] getTags() {
		return tags;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(nome + "\n");
		sb.append("Compromisso no dia " + data + ", às " + horario + "\n");
		sb.append("Duração de " + duracao + " minutos." + "\n");
		StringBuilder sbTags = new StringBuilder();
		String tag;
		for (int j = 0; j < tags.length; j++) {
			if (tags[j] == null)
				break;
			if (j == 0)
				sbTags.append("Tags: ");
			else
				sbTags.append(", ");
			
			tag = tags[j];
			if (sbTags.length() % 34 == 0 || (sbTags.length() % 34) + tag.length() >= 34)
				sbTags.append("\n" + tag);
			else
				sbTags.append(tag);
		}
		if (sbTags.toString().contains("Tags"))
			sbTags.append(".");
		else
			sbTags.append("Sem tags.");
		
		sbTags.append("\n");
		
		return sb.toString() + sbTags.toString();
	}
	
}