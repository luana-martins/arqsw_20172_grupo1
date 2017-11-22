package tp6.persistence;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.CompilationUnit;

public class DadosRemodularizar {
	String chave;
	CompilationUnit classes;
	public String getChave() {
		return chave;
	}
	public void setChave(String chave) {
		this.chave = chave;
	}
	public CompilationUnit getClasses() {
		return classes;
	}
	public void setClasses(CompilationUnit classes) {
		this.classes = classes;
	}
	public DadosRemodularizar(String chave, CompilationUnit classes) {
		super();
		this.chave = chave;
		this.classes = classes;
	}
}
