package tp11.persistences;

import java.util.ArrayList;

import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;

public class Dependencias {
	
	private IPackageFragment pacote;
	private IType classe;
	private ArrayList<String> dependencias;
	
	public Dependencias(IType classe, ArrayList<String> dependencias){
		this.pacote = classe.getPackageFragment();
		this.classe = classe;
		this.dependencias = dependencias;
	}

	public IPackageFragment getPacote() {
		return pacote;
	}

	public IType getClasse() {
		return classe;
	}

	public ArrayList<String> getDependencias() {
		return dependencias;
	}
	
	
}
