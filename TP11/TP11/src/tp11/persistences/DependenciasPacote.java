package tp11.persistences;

import java.util.ArrayList;

public class DependenciasPacote {
	String nome;
	ArrayList<Integer> array;
	private int contador;

	public String getNome() {
		return nome;
	}

	public int getFromArray() {
		int dep = array.get(contador);

		return dep;
	}

	public int getNextFromArray() {
		int dep = array.get(contador);
		contador++;
		return dep;
	}

	public ArrayList<Integer> getArray() {
		return array;
	}

	public DependenciasPacote(String nome, ArrayList<Integer> array) {
		this.nome = nome;
		this.array = array;
		contador = 0;
	}

	public int getContador() {
		return contador;
	}
}
