package tp8.similaridade;

import java.util.ArrayList;


public class Similaridade {

	

	public double similaridadePSC(ArrayList<String> dependencias1, ArrayList<String> dependencias2) {
		try {
			int a, b, c;

			a = calcularA(dependencias1, dependencias2);
			b = calcularB(dependencias1, a);
			c = calcularC(dependencias2, a);

			return (double) (a * a) / ((b + a) * (c + a));

		} catch (ArithmeticException e) {
			return 0;
		}
	}

	private int calcularC(ArrayList<String> dependencias2, int a) {
		return dependencias2.size() - a;
	}

	private int calcularB(ArrayList<String> dependencias1, int a) {
		return dependencias1.size() - a;
	}

	private int calcularA(ArrayList<String> dependencias1, ArrayList<String> dependencias2) {
		int a = 0;
		for (int i = 0; i < dependencias1.size(); i++) {
			for (int j = 0; j < dependencias2.size(); j++) {
				if (dependencias1.get(i).compareTo(dependencias2.get(j)) == 0) {
					a++;
					break;
				}
			}
		}
		return a;
	}
}
