package tp1.handlers;

public class DadosDoProjeto {

	String nomeDaClasse;
	String nomeDoMetodo;
	int qtdeDeLinhas;
	int porcentagem;
	
	public DadosDoProjeto(String nomeDaClasse, String nomeDoMetodo, int qtdeDeLinhas, int porcentagem) {
		this.nomeDaClasse = nomeDaClasse;  
		this.nomeDoMetodo = nomeDoMetodo;
		this.qtdeDeLinhas = qtdeDeLinhas;
		this.porcentagem = porcentagem;
	}

	public int getPorcentagem() {
		return porcentagem;
	}

	public void setPorcentagem(int porcentagem) {
		this.porcentagem = porcentagem;
	}

	public String getNomeDaClasse() {
		return nomeDaClasse;
	}

	public String getNomeDoMetodo() {
		return nomeDoMetodo;
	}

	public int getQtdeDeLinhas() {
		return qtdeDeLinhas;
	}

	
}
