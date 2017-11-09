package tp5.handlers;

public class Violacao {
	
	String classeViolada;
	String classeVioladora;
	String metodoViolador;
	
	public Violacao(String classeViolada, String classeVioladora, String metodoViolador) {
		this.classeViolada = classeViolada;
		this.classeVioladora = classeVioladora;
		this.metodoViolador = metodoViolador;
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
		return "O método '"+metodoViolador+
				"' pertencente a classe '"+classeVioladora+
				"' não pode instanciar um objeto de '"+classeViolada+"'";
	}
}
