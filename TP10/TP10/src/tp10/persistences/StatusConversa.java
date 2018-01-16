package tp10.persistences;


public class StatusConversa {
	String classeA;
	String classeB;
	int tipoDependencia;
	String pacoteA;
	String pacoteB;
	
	public StatusConversa(String pacoteA, String pacoteB, int tipoDependencia) {
		this.pacoteA = pacoteA;
		this.pacoteB = pacoteB;
		this.tipoDependencia = tipoDependencia;
	}

	public StatusConversa(String pacoteA, String pacoteB, String classeA, String classeB, int tipoDependencia) {
		this.classeA = classeA;
		this.classeB = classeB;
		this.pacoteA = pacoteA;
		this.pacoteB = pacoteB;
		this.tipoDependencia = tipoDependencia;
	}
	
	public String getPacoteA() {
		return pacoteA;
	}

	public String getPacoteB() {
		return pacoteB;
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
