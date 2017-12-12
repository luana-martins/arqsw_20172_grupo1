package tp8.clusterizacao;

import java.util.ArrayList;
import java.util.Random;

import tp8.persistences.Grafo;

public class KMeans {
	private boolean termina = false;
	private ArrayList<Grafo> distancias;
	private int k;
	private ArrayList<Grafo> cluster1 = new ArrayList<Grafo>();
	private ArrayList<Grafo> cluster2 = new ArrayList<Grafo>();
	private ArrayList<Grafo> cluster3 = new ArrayList<Grafo>();

	public void calcular(ArrayList<Grafo> distancias, int k) {
		this.distancias = distancias;
		this.k = k;

		ArrayList<String> centroides = elegerCentroides();
		System.out.println("Centroides");
		for(int i = 0; i < centroides.size();i++) {
			System.out.print(" "+centroides.get(i));
		}
		System.out.println();
		
		distribuirClasses(centroides);

		System.out.println("--------CLUSTER1---------------");
		for (int i = 0; i < cluster1.size(); i++) {
			System.out.println(cluster1.get(i).getClasse1());
		}

		System.out.println("--------CLUSTER2---------------");
		for (int i = 0; i < cluster2.size(); i++) {
			System.out.println(cluster2.get(i).getClasse1());
		}
		System.out.println("--------CLUSTER3---------------");
		for (int i = 0; i < cluster3.size(); i++) {
			System.out.println(cluster3.get(i).getClasse1());
		}
		recalcularCentroides();
		
		while (termina == true) {
			distribuirClasses(centroides);
			recalcularCentroides();
		}
	}

	private void recalcularCentroides() {
		
	}
		
	private void distribuirClasses(ArrayList<String> centroides) {
		cluster1.add(0, new Grafo(centroides.get(0), centroides.get(0), 0.0));
		cluster2.add(0, new Grafo(centroides.get(1), centroides.get(1), 0.0));
		cluster3.add(0, new Grafo(centroides.get(2), centroides.get(2), 0.0));

		for (int i = 0; i < distancias.size(); i++) {
			Double maiorSimilaridade = 0.0;
			for (int j = 0; j < centroides.size(); j++) {
				if (!centroides.contains(distancias.get(i).getClasse1())) {
					if (centroides.get(j).equals(distancias.get(i).getClasse2())) {
						if (maiorSimilaridade < distancias.get(i).getSimilaridade()) {
							maiorSimilaridade = distancias.get(i).getSimilaridade();
							if (j == 0) {
								cluster1.add(distancias.get(i));
							} else if (j == 1) {
								cluster2.add(distancias.get(i));
							} else if (j == 2) {
								cluster3.add(distancias.get(i));
							}
						}
					}
				}
			}
		}
	}
	
	private ArrayList<String> elegerCentroides() {
	
		Random random = new Random();
		 
		int posicao;
		ArrayList<String> centroides = new ArrayList<String>();
		while (centroides.size() < 3) {
			posicao = random.nextInt(distancias.size());
			String novoCentro = distancias.get(posicao).getClasse1();
			if (!centroides.contains(novoCentro)) {
				centroides.add(novoCentro);
			}
		}	
		return centroides;
	}
}
