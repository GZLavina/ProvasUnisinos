
public class Teste {
	public static void main(String[] args) {
		
		Agenda a = new Agenda();
		String[] tags = {"consulta", "saude", "limpeza", "longe"};
		a.agendaCompromisso("Dentista", "14/08", "09:45", 45, tags);
		String[] tags1 = {"consulta", "saude", "pneumologista", "perto", "importante"};
		a.agendaCompromisso("Medico", "16/08", "08:30", 20, tags1);
		String[] tags2 = {"faculdade", "revisao", "estudo"};
		a.agendaCompromisso("Aula", "13/08", "10:30", 100, tags2);
		String[] tags3 = {"lazer", "amigos"};		
		a.agendaCompromisso("Futebol", "12/08", "18:30", 90, tags3);
		String[] tags4 = {"trabalho", "importante", "cliente", "problema"};
		a.agendaCompromisso("Reuniao", "15/08", "13:30", 60, tags4);
		String[] tags5 = {"lazer", "restaurante", "longe"};
		a.agendaCompromisso("Jantar", "12/08", "20:30", 90, tags5);
		String[] tags6 = {"treino", "perna"};
		a.agendaCompromisso("Academia", "12/08", "20:30", 10, tags6);
		String[] tags7 = {"lazer", "ingresso", "perto"};
		a.agendaCompromisso("Cinema", "01/09", "21:00", 10, tags7);
		String[] tags8 = {"trabalho", "problema", "urgente"};
		a.agendaCompromisso("Reuniao", "11/08", "13:30", 45, tags8);
		a.agendaCompromisso("teste", "01/01", "00:00", 0, new String[5]);
	
		
		a.visualizarCompromissos("nome, data, duracao, tag");
		
		a.removeCompromissos(8); // remove compromissos do mes 8.
		a.removeCompromissos("tags", "lazer");
		a.removeCompromissos("nome", "reuniao");
		a.removeCompromissos("nome", "naoExiste");
		
		a.visualizarCompromissos("nome data tags");
		
		a.desfaz();
		a.desfaz();
		a.desfaz();
		//a.desfaz();
		//a.desfaz();
		//a.desfaz();
		
		
		a.visualizarCompromissos("nome, data");
		
		a.procuraCompromisso("importante");
		//a.procuraCompromisso("naoExiste");
		
		a.salvaAgenda("agenda.txt");
		a.carregaAgenda("agenda.txt");
		
		a.visualizarCompromissos("data tag");
		
		//a.desfaz();
		
	}
}