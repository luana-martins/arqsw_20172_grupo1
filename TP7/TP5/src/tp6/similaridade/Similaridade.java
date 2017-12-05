package tp6.similaridade;

import java.util.ArrayList;

import tp6.persistence.*;
import tp6.ast.*;

public class Similaridade {

	private ArrayList<String> classes;
	private ArrayList<String> pacotes;
	private DependencyVisitor dp;
	private ArrayList<String> dp1, dp2;
	public Similaridade(DependencyVisitor dp, ArrayList<String> pacotes){
		this.dp = dp;
		this.pacotes = pacotes;
	}
	
	public void classesDoPacote(String j) {
		System.out.println("confere "+j);
		System.out.println("d "+dp.getDependencias().size());
		for(int i =0; i < dp.getDependencias().size(); i++) {
			if(dp.getDependencias().get(i).getPacote().equals(j)) {
				System.out.println("igual "+dp.getDependencias().get(i).getPacote()+" e "+j);
				if(this.classes==null) {
					this.classes = new ArrayList<String>();
				}
				if(!this.classes.contains(dp.getDependencias().get(i).getNomeClasse())) {
					this.classes.add(dp.getDependencias().get(i).getNomeClasse());
				}
			}
		}
		for(int i = 0; i < this.classes.size(); i++) {
			System.out.println(this.classes);
		}
	
	}
	
	public void similaridadeMesmoPacote() {
		classesDoPacote(pacotes.get(0));
		for(int i = 0; i < classes.size();i++) {
			dp1=new ArrayList<String>();
			for(int m=0; m < dp.getDependencias().size();m++) {
				if(dp.getDependencias().get(m).getNomeClasse().equals(classes.get(i))) {
//					System.out.println("------------------CLASSE FIXA----------------------");
//					System.out.println(dp.getDependencias().get(m).getNomeClasse()+" "+dp.getDependencias().get(m).getDependencia());
					
					dp1.add(dp.getDependencias().get(m).getDependencia());
				}
			}
//			for(int v=0;v<dp1.size();v++) {
//				System.out.println(dp1.get(v));
//			}
			
			for(int j = i+1; j <classes.size(); j++) {
				dp2=new ArrayList<String>();
				for(int m=0; m < dp.getDependencias().size();m++) {
					if(dp.getDependencias().get(m).getNomeClasse().equals(classes.get(j))) {
//						System.out.println("------------------CLASSE ITER----------------------"+j);
//						System.out.println(dp.getDependencias().get(m).getNomeClasse()+" "+dp.getDependencias().get(m).getDependencia());
						
						dp2.add(dp.getDependencias().get(m).getDependencia());
					}
				}
//				for(int v=0;v<dp2.size();v++) {
//					System.out.println(dp2.get(v));
//				}
				
				System.out.println("Classe FIXA "+dp1+" Classe ITER "+dp2);
				int cont=0;
				for(int a=0; a < dp1.size(); a++) {
					for(int b=0; b < dp2.size(); b++) {
					if(dp2.contains(dp1.get(a))&&dp1.contains(dp2.get(b))) {
						cont++;
						System.out.println("Classe FIXA "+cont+" Classe ITER");
					}
					}
				}
		}
	}
		
	}
}
