package tp7.persistences;

import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;

public class Recomendacao {
	private IType classe;
	private IPackageFragment pacoteDestino;
	private double simPacoteAtual, simPacoteDestino;
	
	public Recomendacao(IType classe, IPackageFragment pacoteDestino, double simPacoteAtual, double simPacoteDestino) {
		this.classe = classe;
		this.pacoteDestino = pacoteDestino;
		this.simPacoteAtual = simPacoteAtual;
		this.simPacoteDestino = simPacoteDestino;
	}

	public IType getClasse() {
		return classe;
	}

	public IPackageFragment getPacoteDestino() {
		return pacoteDestino;
	}

	public double getSimPacoteAtual() {
		return simPacoteAtual;
	}

	public double getSimPacoteDestino() {
		return simPacoteDestino;
	}
	
	

}
