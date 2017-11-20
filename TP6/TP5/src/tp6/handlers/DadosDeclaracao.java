package tp6.handlers;

public class DadosDeclaracao {
	
	private String nome;
	private String tipo;
	
	public DadosDeclaracao(String tipo, String nome) {
		this.nome = nome;
		this.tipo = tipo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

}
