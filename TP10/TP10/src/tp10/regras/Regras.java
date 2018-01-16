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


import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.internal.codeassist.impl.Keywords;
import org.eclipse.ui.internal.registry.KeywordRegistry;
import org.omg.IOP.ENCODING_CDR_ENCAPS;

import tp10.handlers.SampleHandler;
import tp10.persistences.Dependencias;
import tp10.persistences.StatusConversa;

public class Regras {

	private ArrayList<Dependencias> classesDependencias;
	private ArrayList<StatusConversa> status;
	Properties p = new Properties();

	public Regras(ArrayList<Dependencias> classesDependencias) {
		this.classesDependencias = classesDependencias;
		status = new ArrayList<StatusConversa>();
		try {
			p.load(new FileInputStream("C:\\Users\\Luana\\Downloads\\arqsw_20172_grupo1\\TP10\\TP10\\prop.txt"));
			
		} catch (FileNotFoundException e) {
			System.err.println("notFound");
		} catch (IOException e) {
			System.err.println("IO");
		}
	}
	
	public ArrayList<StatusConversa> especificacaoDasDependencias() { 
		String array[] = new String[3];
		 try {
	            File f = new File("C:\\Users\\Luana\\Downloads\\arqsw_20172_grupo1\\TP10\\TP10\\regras.txt");
	            BufferedReader b = new BufferedReader(new FileReader(f));
	            String readLine = "";
	            System.out.println("Reading file using Buffered Reader");
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
	            e.printStackTrace();
	        }
		return status;
	}

	public void conversa(String a, String b, int tipoDependencia) {
		int cont = 0;
		if(b.equals("util") || b.equals("model") || b.equals("controller") || b.equals("view")){
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
			if(cont==0) {
				SampleHandler.recomendacoes.add(new StatusConversa(a,
							b, tipoDependencia+3));
				status.add(new StatusConversa(a,
							b, tipoDependencia+3)); 
			} 
			else {
				SampleHandler.recomendacoes.add(new StatusConversa(a,
						b, tipoDependencia));
				status.add(new StatusConversa(a,
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
							}
					}
				}
			}
			if(cont==0) {
				SampleHandler.recomendacoes.add(new StatusConversa(a,
						b, tipoDependencia+3));
				status.add(new StatusConversa(a,
							b, tipoDependencia+3)); 
			} 
			else {
				SampleHandler.recomendacoes.add(new StatusConversa(a,
						b, tipoDependencia));
				status.add(new StatusConversa(a,
						b, tipoDependencia));
			}
		}
	}
}
	