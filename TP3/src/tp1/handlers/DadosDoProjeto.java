package tp1.handlers;


public class DadosDoProjeto {

	private String nomeDaClasse;
	private String nomeDoMetodo;
	private String declaracaoDoMetodo;
	

	public DadosDoProjeto(String nomeDaClasse, String nomeDoMetodo, String declaracaoDoMetodo /*int qtdeDeLinhas, int porcentagem, int numCaracteres*/) {
		this.nomeDaClasse = nomeDaClasse;  
		this.nomeDoMetodo = nomeDoMetodo;
		this.declaracaoDoMetodo = declaracaoDoMetodo;
	}

	public String getNomeDaClasse() {
		return nomeDaClasse;
	}

	public String getNomeDoMetodo() {
		return nomeDoMetodo;
	}
	
	public String getDeclaracaoDoMetodo(){
		return declaracaoDoMetodo;
	}
	
}
