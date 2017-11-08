package tp4.handlers;

import java.util.List;


public class Enumerado {

	private List<?> nomeEnum;
	private String nomeMetodo;
	
	public Enumerado(List<?> nomeEnum, String nomeMetodo) {
		super();
		this.nomeEnum = nomeEnum;
		this.nomeMetodo = nomeMetodo;
	}
	public String getNomeMetodo() {
		return nomeMetodo;
	}
	public void setNomeMetodo(String nomeMetodo) {
		this.nomeMetodo = nomeMetodo;
	}
	public List<?> getNomeEnum() {
		return nomeEnum;
	}
	public void setNomeEnum(List<?> nomeEnum) {
		this.nomeEnum = nomeEnum;
	}
}
