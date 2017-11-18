package tp5.handlers;

import java.util.ArrayList;

public class PadraoArquitetural {
	private  ArrayList<DadosMetodo> dados = SampleHandler.dadosProjeto;
	private ArrayList<String>c;
	private ArrayList<String>v;
	private ArrayList<String>m;

	public void popular(){
		c = new ArrayList<String>();
		v = new ArrayList<String>();
		m = new ArrayList<String>();
		
		for(int i = 0; i < dados.size(); i++){
			if(dados.get(i).getPacote().endsWith(".controller")) {
				if(!c.contains(dados.get(i).getNomeClasse())) {
					c.add(dados.get(i).getNomeClasse());
				}
			}
			if(dados.get(i).getPacote().endsWith(".view")) {
				if(!v.contains(dados.get(i).getNomeClasse())) {
					v.add(dados.get(i).getNomeClasse());
				}
			}
			if(dados.get(i).getPacote().endsWith(".model")) {
				if(!c.contains(dados.get(i).getNomeClasse())) {
					m.add(dados.get(i).getNomeClasse());
				}
			}
		}
	}

	public ArrayList<Violacao> conferirConversa() {
		ArrayList<Violacao> violacoes = null;  
		for(int i = 0; i < dados.size();i++) {
			if(dados.get(i).getPacote().endsWith(".model")) {
				// View
				for(int j = 0; j < v.size();j++) {
					if(violacoes==null) {
						violacoes = new ArrayList<Violacao>();
					}

					// Verificar instancia de objetos Controller
					if(dados.get(i).getMetodos().toString().contains("new "+v.get(j))) {
						violacoes.add(new Violacao(1,v.get(j), dados.get(i).getNomeClasse(), dados.get(i).getNomeMetodo()));
					}

					// Verificar declaração e instanciacao de atributos
					for(int k = 0; k < dados.get(i).getClasse().size(); k++) {
						if(dados.get(i).getClasse().get(k).getTipo().contains(v.get(j))) {
							violacoes.add(new Violacao(2, v.get(j), dados.get(i).getNomeClasse(), null));

							// Verificar se foi instanciado
							if(dados.get(i).getClasse().get(k).getNome().contains("=new")) {
								violacoes.add(new Violacao(1, v.get(j), dados.get(i).getNomeClasse(), null));
							}
						}
					}

					// Verificar parametros 
					if(dados.get(i).getParametros().toString().contains(v.get(j))) {
						violacoes.add(new Violacao(3, v.get(j), dados.get(i).getNomeClasse(), dados.get(i).getNomeMetodo()));
					}
				}

				// Controller
				for(int j = 0; j < c.size();j++) {
					if(violacoes==null) {
						violacoes = new ArrayList<Violacao>();
					}

					// Verificar instancia de objetos Controller
					if(dados.get(i).getMetodos().toString().contains("new "+c.get(j))) {
						violacoes.add(new Violacao(1,c.get(j), dados.get(i).getNomeClasse(), dados.get(i).getNomeMetodo()));
					}

					// Verificar declaração e instanciacao de atributos
					for(int k = 0; k < dados.get(i).getClasse().size(); k++) {
						if(dados.get(i).getClasse().get(k).getTipo().contains(c.get(j))) {
							violacoes.add(new Violacao(2, c.get(j), dados.get(i).getNomeClasse(), null));

							// Verificar se foi instanciado
							if(dados.get(i).getClasse().get(k).getNome().contains("=new")) {
								violacoes.add(new Violacao(1, c.get(j), dados.get(i).getNomeClasse(), null));
							}
						}
					}

					// Verificar parametros 
					if(dados.get(i).getParametros().toString().contains(c.get(j))) {
						violacoes.add(new Violacao(3, c.get(j), dados.get(i).getNomeClasse(), dados.get(i).getNomeMetodo()));
					}

				}
			}
		}
		return violacoes;
	}
}
