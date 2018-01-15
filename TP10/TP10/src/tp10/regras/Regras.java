package tp10.regras;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;


import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.internal.codeassist.impl.Keywords;
import org.eclipse.ui.internal.registry.KeywordRegistry;

import tp10.persistences.Dependencias;
import tp10.persistences.StatusConversa;

public class Regras {

	private ArrayList<Dependencias> classesDependencias;
	private ArrayList<StatusConversa> status;
	Properties p = new Properties();
	String view = "view", model = "model", controller = "controller",  util = "util" , awt = "awt", swing = "swing"; 

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

	/**
	 * 0 - dp permitida
	 * 1 - dp obrigatoria
	 * 2 - dp nao permitida ou nao explicitada
	 * */
	
	
	public ArrayList<StatusConversa> especificacaoDasDependencias() { 
		
		conversa(view, controller, 0);
		
		
		// Model nao pode depender de Controller e View
		conversa(model, controller, 2);
		conversa(model, view, 2);
	 
	
		// O pacote Util nao pode depender de nenhuma classe especifica do codigo fonte do sistema. 
		conversa(util, model, 2);
		conversa(util, controller, 2);
		conversa(util, view, 2);
		
		// View pode depender apenas do Controller e Util 
		conversa(view, model, 2);
		
		// Somente a View pode depender dos componentes providos pelo AWT/Swing
		// conversa(model, awt);
		// conversa(controller, awt);
		 conversa(view, swing, 0);
		
		return status;
	}

	public void conversa(String a, String b, int tipoDependencia) {
		if(b.equals(util) || b.equals(model) || b.equals(controller) || b.equals(view)){
			for (int i = 0; i < classesDependencias.size(); i++) {
				// System.out.println(classe.getPacote().getElementName());
				if (classesDependencias.get(i).getPacote().getElementName().equals(a)) {
					for (int j = 0; j < classesDependencias.size(); j++) {
						if (classesDependencias.get(j).getPacote().getElementName().contentEquals(b)) {
							if (classesDependencias.get(i).getDependencias()
									.contains(classesDependencias.get(j).getClasse().getElementName())) {
								status.add(new StatusConversa(classesDependencias.get(i).getClasse().getFullyQualifiedName(),
										classesDependencias.get(j).getClasse().getFullyQualifiedName(), tipoDependencia)); 
							}
						}
					}
				}
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
								status.add(new StatusConversa(classesDependencias.get(i).getClasse().getFullyQualifiedName(),
										b, tipoDependencia)); 
							}
					}
				}
			}
		}
	}
}
	