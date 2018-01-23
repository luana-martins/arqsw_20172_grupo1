package tp11.regras;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

import tp11.handlers.SampleHandler;
import tp11.persistences.Dependencias;

public class Regras {

	private ArrayList<Dependencias> classesDependencias;
	Properties p = new Properties();

	public Regras(ArrayList<Dependencias> classesDependencias) {
		this.classesDependencias = classesDependencias;
		
		try {
			p.load(new FileInputStream("C:\\Users\\Luana\\Downloads\\TP10\\prop.txt"));
		} catch (FileNotFoundException e) {
			System.err.println("Arquivo properties.txt nao encontrado");
		} catch (IOException e) {
			System.err.println("IO");
		}
		SampleHandler.pac.addAll(Arrays.asList(p.getProperty("bibliotecas").split(", ")));
	}

	public int getMatriz(int i, int j, int contador) {
		String array[] = new String[3];
		
		array[0] = SampleHandler.pac.get(i);
		array[1] = SampleHandler.pac.get(j);
		array[2] = String.valueOf(contador);
		
		return especificacaoDasDependencias(array);
	}

	public int especificacaoDasDependencias(String arrayLinha[]) {
		String array[] = new String[3];
		
		try {

			File f = new File("C:\\Users\\Luana\\Downloads\\TP10\\regras.txt");
			BufferedReader b = new BufferedReader(new FileReader(f));
			String readLine = "";

			while ((readLine = b.readLine()) != null) {
				array = readLine.split(" ");

				// encontra o [x,y] da matriz de dependencias no arquivo txt
				if (arrayLinha[0].equals(array[0])) {
					if (arrayLinha[1].equals(array[2])) {

						// valores para violacao
						if (Integer.parseInt(arrayLinha[2]) == 0) {
							if (array[1].equals("CAN")) {
								return 3;
							} else if (array[1].equals("MUST")) {
								return 4;
							} else if (array[1].equals("CANNOT")) {
								return 5;
							}
						}

						// valores para nao violacao
						else {
							if (array[1].equals("CAN")) {
								return 0;
							} else if (array[1].equals("MUST")) {
								return 1;
							} else if (array[1].equals("CANNOT")) {
								return 2;
							}
						}
					}
				}
			}
			b.close();
		} catch (IOException e) {
			System.err.println("Arquivo regras.txt nao encontrado");
		}
		return -1;
	}

	public Integer conversa(String a, String b, int tipoDependencia) {
		int cont = 0;

		// depende de classes
		if (!p.containsKey(b)) {
			for (int i = 0; i < classesDependencias.size(); i++) {
				if (classesDependencias.get(i).getPacote().getElementName().equals(a)) {
					for (int j = 0; j < classesDependencias.size(); j++) {
						if (classesDependencias.get(j).getPacote().getElementName().contentEquals(b)) {
							if (classesDependencias.get(i).getDependencias()
									.contains(classesDependencias.get(j).getClasse().getElementName())) {
								cont++;
							}
						}
					}
				}
			}
		}

		// depende de bibliotecas - arquivo properties
		else {

			// adiciona na lista para gerar matriz de dependencias
			if (!SampleHandler.pac.contains(b)) {
				SampleHandler.pac.add(b);
			}

			ArrayList<String> keywordsList = new ArrayList<String>();
			p.getProperty(b);
			keywordsList.addAll(Arrays.asList(p.getProperty(b).split(",")));

			for (int i = 0; i < classesDependencias.size(); i++) {
				if (classesDependencias.get(i).getPacote().getElementName().equals(a)) {
					for (int j = 0; j < keywordsList.size(); j++) {
						if (classesDependencias.get(i).getDependencias().toString().contains(keywordsList.get(j))) {
							cont++;
						}
					}
				}
			}
		}
		return cont;
	}

	public Regras() {
	}
}
