package tp6.similaridade;

import java.util.ArrayList;

import tp6.persistence.*;
import tp6.ast.*;

public class Similaridade {
	
	private ArrayList<DadosSimilaridade> mesmoPacote;
	private ArrayList<ArrayList<DadosSimilaridade>> outroPacote;
	private ArrayList<String> classes;
	private ArrayList<String> pacotes;;
	private DependencyVisitor dp;
	
	public Similaridade(DependencyVisitor dp, ArrayList<String> pacotes){
		this.dp = dp;
		this.pacotes = pacotes;
	}
	
	public void classesDoPacote(String j) {
//		for(int i =0; i < dp.getDependencias().size(); i++) {
//			if(dp.getDependencias().get(i).getPacote().equals(j)) {
//				
//				if(this.classes==null) {
//					this.classes = new ArrayList<String>();
//				}
//				if(!this.classes.contains(dp.getDependencias().get(i).getNomeClasse())) {
//					this.classes.add(dp.getDependencias().get(i).getNomeClasse());
//				}
//			}
//		}
	
	}
	
	public void similaridadeMesmoPacote() {
//		for(int j = 0; j < dp.getPacotes().size();j++) {
//		System.out.println(pacotes.size());
//			classesDoPacote(pacotes.get(0));
//			for(int i = 0; i < dp.getDependencias().size(); i++) {
//				if(dp.getPacotes().get(0).equals(dp.getDependencias().get(i).getPacote())) {
//					
//					
//				}
//			}	
		}
//	}
}
