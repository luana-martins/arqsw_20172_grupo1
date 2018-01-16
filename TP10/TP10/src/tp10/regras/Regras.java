package tp10.regras;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

import tp10.handlers.SampleHandler;
import tp10.persistences.Dependencias;
import tp10.persistences.StatusConversa;

public class Regras {

	private ArrayList<Dependencias> classesDependencias;
	Properties p = new Properties();

	public Regras(ArrayList<Dependencias> classesDependencias) {
		this.classesDependencias = classesDependencias;
		try {
			p.load(new FileInputStream("C:\\Users\\Luana\\Downloads\\arqsw_20172_grupo1\\TP10\\TP10\\prop.txt"));
			
		} catch (FileNotFoundException e) {
			System.err.println("notFound");
		} catch (IOException e) {
			System.err.println("IO");
		}
	}
	
	public void especificacaoDasDependencias() { 
		String array[] = new String[3];
		 try {
	            File f = new File("C:\\Users\\Luana\\Downloads\\arqsw_20172_grupo1\\TP10\\TP10\\regras.txt");
	            BufferedReader b = new BufferedReader(new FileReader(f));
	            String readLine = "";
	            while ((readLine = b.readLine()) != null) {
	            	array = readLine.split(" ");
	            	if(array[1].equals("CAN"))
	            	conversa(array[0], array[2], 0);
	            	else if(array[1].equals("MUST"))
		            	conversa(array[0], array[2], 1);
	            	else if(array[1].equals("CANNOT"))
		            	conversa(array[0], array[2], 2);
	            }
	        } catch (IOException e) {
	        	System.err.println("notnotFound");
	        }
		return;
	}

	public void conversa(String a, String b, int tipoDependencia) {
		int cont = 0;
		StatusConversa aux = null;
		if(b.equals("util") || b.equals("model") || b.equals("controller") || b.equals("view")){
			for (int i = 0; i < classesDependencias.size(); i++) {
				if (classesDependencias.get(i).getPacote().getElementName().equals(a)) {
					for (int j = 0; j < classesDependencias.size(); j++) {
						if (classesDependencias.get(j).getPacote().getElementName().contentEquals(b)) {
							if (classesDependencias.get(i).getDependencias()
									.contains(classesDependencias.get(j).getClasse().getElementName())) {
								cont++;
								aux = new StatusConversa(a,b,classesDependencias.get(i).getClasse().getElementName(),
										classesDependencias.get(j).getClasse().getElementName(), tipoDependencia); 
								if(!SampleHandler.status.contains(aux)) {
									SampleHandler.status.add(aux);
								}
							}
							else {
								aux = new StatusConversa(a,b,classesDependencias.get(i).getClasse().getElementName(),
										classesDependencias.get(j).getClasse().getElementName(), tipoDependencia+3);
								if(!SampleHandler.status.contains(aux)) {
									SampleHandler.status.add(aux);
								}
							}
							
						}
					}
				}
				
			}
			if(cont==0) {
				SampleHandler.recomendacoes.add(new StatusConversa(a,
							b, tipoDependencia+3));
			} 
			else {
				SampleHandler.recomendacoes.add(new StatusConversa(a,
						b, tipoDependencia));
			}
		}
	
		else {
			ArrayList<String> keywordsList = new ArrayList<String>();
			p.getProperty(b);
			keywordsList.addAll(Arrays.asList(p.getProperty(b).split(",")));
			
			for (int i = 0; i < classesDependencias.size(); i++) {
				// System.out.println(classe.getPacote().getElementName());
				if (classesDependencias.get(i).getPacote().getElementName().equals(a)) {
					for (int j = 0; j < keywordsList.size(); j++) {
							if (classesDependencias.get(i).getDependencias().toString()
									.contains(keywordsList.get(j))) {
								cont++;
								aux = new StatusConversa(a,b, classesDependencias.get(i).getClasse().getElementName(), "", tipoDependencia);
								if(!SampleHandler.status.contains(aux)) {
									SampleHandler.status.add(aux);
								}
							}
							else {
								aux = new StatusConversa(a,b, classesDependencias.get(i).getClasse().getElementName(), "", tipoDependencia+3);
								if(!SampleHandler.status.contains(aux)) {
									SampleHandler.status.add(aux);
								}
							}
					}
				}
			}
			if(cont==0) {
				SampleHandler.recomendacoes.add(new StatusConversa(a,
						b, tipoDependencia+3));
			} 
			else {
				SampleHandler.recomendacoes.add(new StatusConversa(a,
						b, tipoDependencia));
			}
		}
	}
}
	