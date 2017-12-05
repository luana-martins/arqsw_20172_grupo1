package tp6.persistence;

import org.eclipse.jdt.core.IType;

public class DadosDependencias {
	//pacote, classe, svd.getType().resolveBinding().getQualifiedName(), iType
	private String pacote;
	private String nomeClasse;
	private String dependencia;
	private IType classe;
	public DadosDependencias(String pacote, String nomeClasse, String dependencia, IType classe) {
		super();
		this.pacote = pacote;
		this.nomeClasse = nomeClasse;
		this.dependencia = dependencia;
		this.classe = classe;
	}
	public String getPacote() {
		return pacote;
	}
	public String getNomeClasse() {
		return nomeClasse;
	}
	public String getDependencia() {
		return dependencia;
	}
	public IType getClasse() {
		return classe;
	}
}
