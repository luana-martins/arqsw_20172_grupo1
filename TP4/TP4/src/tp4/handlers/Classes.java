package tp4.handlers;

import java.util.List;

public class Classes {
	private String metodo;
	
	public String getMetodo() {
		return metodo;
	}

	public void setMetodo(String metodo) {
		this.metodo = metodo;
	}

	public String getA() {
		return a;
	}

	public void setA(String a) {
		this.a = a;
	}

	public List<?> getParametros() {
		return parametros;
	}

	public void setParametros(List<?> parametros) {
		this.parametros = parametros;
	}
	private String a;
	private List<?> parametros;
	public Classes(String metodo, String a, List<?> parametros) {
		this.a = a;
		this.metodo = metodo;
		this.parametros = parametros;
		if(parametros.contains(a)) {
			parametros.remove(a);
		}
	}
}
