package tp6.persistence;

import java.util.ArrayList;

public class DadosSimilaridade {
	ArrayList<Integer> similaridade;
	String classe;
	String pacote;
	
	public DadosSimilaridade(ArrayList<Integer>similaridade, String classe, String pacote) {
		this.similaridade = similaridade;
		this.classe = classe;
		this.pacote = pacote;
	}
	public ArrayList<Integer> getSimilaridade() {
		return similaridade;
	}
	public String getPacote() {
		return pacote;
	}
	
	public String getClasse() {
		return classe;
	}
}
