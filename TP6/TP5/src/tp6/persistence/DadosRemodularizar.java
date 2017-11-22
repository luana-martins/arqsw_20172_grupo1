package tp6.persistence;

import org.eclipse.jdt.core.IType;

public class DadosRemodularizar {
	private String tipoPacote;
	private IType classe;

	public DadosRemodularizar(String tipoPacote, IType classe) {
		this.tipoPacote = tipoPacote;
		this.classe = classe;
	}

	public String getTipoPacote() {
		return tipoPacote;
	}

	public IType getClasse() {
		return classe;
	}

}
