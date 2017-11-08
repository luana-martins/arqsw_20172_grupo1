package tp5.handlers;

import java.util.ArrayList;

public class PadraoArquitetural {
	private  ArrayList<DadosDoProjeto> dados = SampleHandler.dadosProjeto;
	private ArrayList<String>c;
	private ArrayList<String>v;
	private ArrayList<String>m;
	
	
	public void print(){
		
		for(int i = 0; i < dados.size(); i++){
			System.out.println("pack: "+dados.get(i).getPacote());
			System.out.println("node "+dados.get(i).getMetodos());
			System.out.println("Classe: "+dados.get(i).getNome());
			System.out.println("Metodo: "+dados.get(i).getNomeMetodo());
		}
	}
	
	public void popular(){
		c = new ArrayList<String>();
		v = new ArrayList<String>();
		m = new ArrayList<String>();
		for(int i = 0; i < dados.size(); i++){
			if(dados.get(i).getPacote().endsWith(".controller")) {
				if(!c.contains(dados.get(i).getNome())) {
					c.add(dados.get(i).getNome());
				}
			}
			if(dados.get(i).getPacote().endsWith(".view")) {
				if(!v.contains(dados.get(i).getNome())) {
					v.add(dados.get(i).getNome());
				}
			}
			if(dados.get(i).getPacote().endsWith(".model")) {
				if(!c.contains(dados.get(i).getNome())) {
					m.add(dados.get(i).getNome());
				}
			}
		}
	}
	
	public void conferirConversa() {
		for(int i = 0; i < dados.size();i++) {
			if(dados.get(i).getPacote().endsWith(".model")) {
				for(int j = 0; j < v.size();j++) {
					if(dados.get(i).getMetodos().toString().contains("new "+v.get(j))) {
						System.out.println("A classe "+dados.get(i).getNome()+" instancia um objeto "+v.get(j));
					}
				}
				for(int j = 0; j < c.size();j++) {
					if(dados.get(i).getMetodos().toString().contains("new "+c.get(j))) {
						System.out.println("O método '"+dados.get(i).getNomeMetodo()+
								"' pertencente a classe '"+dados.get(i).getNome()+
								"' não pode instanciar um objeto de '"+c.get(j)+"'");
					}
				}
			}
		}
	}
}
