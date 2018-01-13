package tp7.similaridade;

import java.util.ArrayList;

import tp7.persistences.Dependencias;

public class Similaridade {

	
	
	public double similaridadeMesmoPacote(Dependencias classe, ArrayList<Dependencias> classes) {
		double similaridade=0;
		
		for(int i=0;i<classes.size();i++){
			if(classe.getClasse().getElementName().compareTo(classes.get(i).getClasse().getElementName())==0){
				continue;
			}
			similaridade += similaridadePSC(classe.getDependencias(),classes.get(i).getDependencias());
		}
		
		return similaridade/(classes.size()-1);
		
	}
	
	public double similaridadePacotesDiferentes(Dependencias classe, ArrayList<Dependencias> classes) {
		double similaridade=0;
		for(int i=0;i<classes.size();i++){
			similaridade += similaridadePSC(classe.getDependencias(),classes.get(i).getDependencias());
		}
		
		return similaridade/classes.size();
		
	}
	private double similaridadePSC(ArrayList<String> dependencias1, ArrayList<String> dependencias2){
		try{
			int a,b,c;
			
			a = calcularA(dependencias1, dependencias2);
			b = calcularB(dependencias1, a);
			c = calcularC(dependencias2, a);
			
			return (double) (a*a)/((b+a)*(c+a));
			
		}catch(ArithmeticException e){
			return 0;
		}
	}

	private int calcularC(ArrayList<String> dependencias2, int a) {
		return dependencias2.size()-a;
	}

	private int calcularB(ArrayList<String> dependencias1, int a) {
		return dependencias1.size()-a;
	}

	private int calcularA(ArrayList<String> dependencias1, ArrayList<String> dependencias2) {
		int a=0;
		for(int i=0; i<dependencias1.size(); i++){
			for(int j=0; j<dependencias2.size(); j++){
				if(dependencias1.get(i).compareTo(dependencias2.get(j)) == 0){
					a++;
					break;
				}
			}
		}
		return a;
	}
}
