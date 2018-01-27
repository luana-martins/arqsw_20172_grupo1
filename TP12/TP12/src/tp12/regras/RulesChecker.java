package tp12.regras;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;

import tp12.persistences.Dependencies;
import tp12.persistences.Dependency;
import tp12.persistences.Violation;

public class RulesChecker {

	private ArrayList<Dependency> dependencies;
	private ArrayList<Violation> violations;
	private Properties p = new Properties();

	public RulesChecker(ArrayList<Dependency> dependencies) {
		this.dependencies = dependencies;
		this.violations = new ArrayList<Violation>();
		try {
			p.load(new FileInputStream("prop.txt"));
			
		} catch (FileNotFoundException e) {
			System.err.println("notFound");
		} catch (IOException e) {
			System.err.println("IO");
		}
	}
	
	@SuppressWarnings("resource")
	public void checkRules() { 
		String array[] = new String[3];
		 try {
	            File f = new File("regras.txt");
	            BufferedReader b = new BufferedReader(new FileReader(f));
	            String readLine = "";
	            while ((readLine = b.readLine()) != null) {
	            	array = readLine.split(" ");
	            	if(array[1].equals("CAN"))
	            	checkRule(array[0], array[2], 0);
	            	else if(array[1].equals("MUST"))
		            	checkRule(array[0], array[2], 1);
	            	else if(array[1].equals("CANNOT"))
		            	checkRule(array[0], array[2], 2);
	            }
	        } catch (IOException e) {
	        	System.err.println("notnotFound");
	        }
		return;
	}

	private void checkRule(String a, String b, int tipoDependencia) {
		
		boolean oneDependencyFound = false;
		
		IPackageFragment packageA, packageB;
		
		packageA = packageB = null;
		
		//procurar para cada classe dentro do pacote a se ela depende de b
		for (int i = 0; i < dependencies.size(); i++) {
			
			//achou uma classe dentro do pacote a
			if (dependencies.get(i).getPckg().getElementName().contains(a)) {
				
				packageA = dependencies.get(i).getPckg();
				
				for (int j = 0; j < dependencies.size(); j++) {
					
					//achou uma classe dentro do pacote b
					if (dependencies.get(j).getPckg().getElementName().contains(b)) {
						
						packageB = dependencies.get(j).getPckg();
						
						//verifica, entre os implements e extends da classe a, se tem alguma dependencia da classe b
						if(dependencies.get(i).getExtendsAndImplementsTypes().contains(dependencies.get(j).getClazz().getFullyQualifiedName())){
							
							if(tipoDependencia == 2){
								violations.add(new Violation(dependencies.get(i).getClazz(),dependencies.get(j).getClazz(),null,tipoDependencia,Dependencies.EXTENDS_OR_IMPLEMENTS));
							}
							else{
								oneDependencyFound = true;
							}
							
							
						} 
						
						//verifica, entre os atributos da classe a, se tem alguma dependencia da classe b
						else if(dependencies.get(i).getAttributesTypes().contains(dependencies.get(j).getClazz().getFullyQualifiedName())){
							
							if(tipoDependencia == 2){
								violations.add(new Violation(dependencies.get(i).getClazz(),dependencies.get(j).getClazz(),null,tipoDependencia,Dependencies.ATTRIBUTE)); 
							}
							
							else{
									oneDependencyFound = true;
								}
						}
						
						//verifica, entre as instancias encontradas na classe a, se tem alguma dependencia da classe b
						else if(dependencies.get(i).getAllOthersInstancesTypes().contains(dependencies.get(j).getClazz().getFullyQualifiedName())){
								
							if(tipoDependencia == 2){
							violations.add(new Violation(dependencies.get(i).getClazz(),dependencies.get(j).getClazz(),null,tipoDependencia,Dependencies.OTHER_INSTANCE)); 
							}
							
							else{
								oneDependencyFound = true;
							}
							
						}
						
						//verifica, entre os parametros dos metodos da classe a, se tem alguma dependencia da classe b
						else {
							for(IMethod m : dependencies.get(i).getMethodsAndParametersTypes().keySet()){
								
								if(dependencies.get(i).getMethodsAndParametersTypes().get(m).contains(dependencies.get(j).getClazz().getFullyQualifiedName())){
								
									if(tipoDependencia == 2){
										violations.add(new Violation(dependencies.get(i).getClazz(),dependencies.get(j).getClazz(),m,tipoDependencia,Dependencies.METHOD_PARAMETER)); 
									}
									else{
										oneDependencyFound = true;
									}
									
								}
							}
						}
							
					}
				}
			}
			
		}
		
		if(tipoDependencia < 2 && !oneDependencyFound){
			violations.add(new Violation(packageA, packageB, tipoDependencia));
		}
	}
	
	public ArrayList<Violation> getViolations(){
		return violations;
	}
}
	