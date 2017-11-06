package tp1.handlers;

public class DadosDoProjeto {

	private String nomeDaClasse;
	private String nomeDoMetodo;
	private int qtdeDeLinhas;
	private int porcentagem;
	private int numCaracteres;
	

	public DadosDoProjeto(String nomeDaClasse, String nomeDoMetodo, int qtdeDeLinhas, int porcentagem, int numCaracteres) {
		this.nomeDaClasse = nomeDaClasse;  
		this.nomeDoMetodo = nomeDoMetodo;
		this.qtdeDeLinhas = qtdeDeLinhas;
		this.porcentagem = porcentagem;
		this.numCaracteres = numCaracteres;
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
	
	public int getNumCaracteres() {
		return numCaracteres;
	}

	
}
