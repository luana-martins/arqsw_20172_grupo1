package tp5.handlers;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.MethodDeclaration;

public class DadosDoProjeto {

	private ArrayList<MethodDeclaration> metodos;
	private String pacote;
	private String nomeClasse;
	private String nomeMetodo;
	
	
	public String getNomeMetodo() {
		return nomeMetodo;
	}
	public void setNomeMetodo(String nomeMetodo) {
		this.nomeMetodo = nomeMetodo;
	}
	public String getNome() {
		return nomeClasse;
	}
	public void setNome(String nome) {
		this.nomeClasse = nome;
	}
	public DadosDoProjeto(ArrayList<MethodDeclaration> metodos, String pacote, String nomeClasse, String nomeMetodo) {
		this.metodos = metodos;
		this.pacote = pacote;
		this.nomeClasse = nomeClasse;
		this.nomeMetodo = nomeMetodo;
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
