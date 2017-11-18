package tp5.handlers;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;

public class DadosMetodo {

	private ArrayList<MethodDeclaration> metodos;
	private String pacote;
	private String nomeClasse;
	private String nomeMetodo;
	private ArrayList<DadosDeclaracao> classe;
	private ArrayList<String> parametros;

	public DadosMetodo(ArrayList<MethodDeclaration> metodos, String pacote, String nomeClasse, String nomeMetodo, 
			ArrayList<DadosDeclaracao> arrayClasse, ArrayList<String> parametros) {
		this.metodos = metodos;
		this.pacote = pacote;
		this.nomeClasse = nomeClasse;
		this.nomeMetodo = nomeMetodo;
		this.classe = arrayClasse;
		this.parametros = parametros;
	}

	public ArrayList<String> getParametros() {
		return parametros;
	}

	public void setParametros(ArrayList<String> parametros) {
		this.parametros = parametros;
	}

	public ArrayList<DadosDeclaracao> getClasse() {
		return classe;
	}

	public void setClasse(ArrayList<DadosDeclaracao> classe) {
		this.classe = classe;
	}

	public String getNomeMetodo() {
		return nomeMetodo;
	}

	public void setNomeMetodo(String nomeMetodo) {
		this.nomeMetodo = nomeMetodo;
	}

	public String getNomeClasse() {
		return nomeClasse;
	}

	public void setNomeClasse(String nome) {
		this.nomeClasse = nome;
	}

	public ArrayList<MethodDeclaration> getMetodos() {
		return metodos;
	}

	public void setMetodos(ArrayList<MethodDeclaration> metodos) {
		this.metodos = metodos;
	}

	public String getPacote() {
		return pacote;
	}

	public void setPacote(String pacote) {
		this.pacote = pacote;
	}
}
