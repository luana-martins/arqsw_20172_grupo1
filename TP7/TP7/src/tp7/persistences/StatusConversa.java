package tp7.persistences;


public class StatusConversa {
	String classeA;
	String classeB;
	int tipoDependencia;
	
	public StatusConversa(String classeA, String classeB, int tipoDependencia) {
		this.classeA = classeA;
		this.classeB = classeB;
		this.tipoDependencia = tipoDependencia;
	}

	public String getClasseA() {
		return classeA;
	}

	public String getClasseB() {
		return classeB;
	}

	public int getTipoDependencia() {
		return tipoDependencia;
	}
}
