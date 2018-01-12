package tp10.handlers;

public class Violacao {
	
	String classeViolada;
	String classeVioladora;
	String metodoViolador;
	Integer tipo;
	
	public Violacao(Integer tipo, String classeViolada, String classeVioladora, String metodoViolador) {
		this.classeViolada = classeViolada;
		this.classeVioladora = classeVioladora;
		this.metodoViolador = metodoViolador;
		this.tipo = tipo;
	}
	
	
	public String getClasseViolada() {
		return classeViolada;
	}
	public String getClasseVioladora() {
		return classeVioladora;
	}
	public String getMetodoViolador() {
		return metodoViolador;
	}
	
	public String getMessage(){
		if(tipo==2) {
			return "A classe '"+classeVioladora+
					"' não pode declarar atributos de '"+classeViolada+"'";
		}
		if(tipo==1) {
			return "O método '"+metodoViolador+
					"' pertencente a classe '"+classeVioladora+
					"' não pode instanciar um objeto de '"+classeViolada+"'";
		}
		if(tipo==3) {
			return "O método '"+metodoViolador+
					"' pertencente a classe '"+classeVioladora+
					"' recebe como parâmetro atributos do tipo '"+classeViolada+"'";
		}
		return "";
	}
}
