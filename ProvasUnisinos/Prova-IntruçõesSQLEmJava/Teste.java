
public class Teste {
	public static void main(String[] args) {
		SQLServer ss = new SQLServer();
		ss.connect("cadastro");
		
		// para testar o carregamento do arquivo
//		System.out.println(ss.query("select * from cidades"));
//		System.out.println(ss.query("select ano,nome from filmes"));
		
		ss.execute("create table cidades (nome text,UF text, cod int)");
		ss.execute("insert into cidades values('poa','RS',100)");
		ss.execute("insert into cidades values('sp', 'SP', 200)");
		ss.execute("insert into cidades values('canoas','RS',300)");
		ss.execute("insert into cidades values('viamao', 'RS', 400)");
		ss.execute("insert into cidades values('florianopolis', 'SC', 500)");
		ss.execute("insert into cidades values('curitiba', 'PR', 600)");
		
		// a ordem das colunas passadas sera a ordem na qual serao mostradas.
		System.out.println(ss.query("select uf,nome from cidades"));
		// pode-se usar >, >=, =, <=, < ou !=
		ss.execute("delete from cidades where cod >= 300");
		System.out.println(ss.query("select nome,cod,uf from cidades"));
		
		ss.execute("create table filmes (Nome text, Ano int, Genero text)");
		ss.execute("insert into filmes values('O Senhor dos Aneis',2001,'Aventura')");
		ss.execute("insert into filmes values('Pulp Fiction',1994,'Nao Sei')");
		ss.execute("insert into filmes values('Star Wars',999999,'Aventura')");
		System.out.println(ss.query("select * from filmes"));
//		ss.execute("delete from filmes");
		
		// as linhas abaixo geram mensagens de erro.
		// (testes para o tratamento de excecoes)
//		System.out.println("teste1:");
//		ss.execute("create table cidades2 nome text,UF text, cod int)");
//		System.out.println("teste2:");
//		ss.execute("create table cidades2 (nome text,UF txt, cod int)");
//		System.out.println("teste3:");
//		ss.execute("insert into cidades ('poa', 100, 'rs')");
//		System.out.println("teste4:");
//		ss.execute("insert into cidades (poa, RS, 100)");
//		System.out.println("teste5:");
//		ss.execute("insert into naoexiste ('poa', 'RS', 100)");
//		System.out.println("teste6:");
//		ss.execute("delete from naoexiste where cod >= 200");
//		System.out.println("teste7:");
//		ss.execute("delete from cidades where naoexiste = 300");
//		System.out.println("teste8:");
//		ss.execute("delete from cidades where nome > 300");
//		System.out.println("teste9:");
//		System.out.println(ss.query("select * from naoexiste"));
//		System.out.println("teste10:");
//		System.out.println(ss.query("select nome, naoexiste from cidades"));
		
		ss.close();
	}
}
