package tp8.persistences;

public class Grafo {
	String classe1;
	String classe2;
	double similaridade;
	public String getClasse1() {
		return classe1;
	}

	public String getClasse2() {
		return classe2;
	}

	public double getSimilaridade() {
		return similaridade;
	}
	public Grafo(String classe1, String classe2, double similaridade) {
		super();
		this.classe1 = classe1;
		this.classe2 = classe2;
		this.similaridade = similaridade;
	}
}
