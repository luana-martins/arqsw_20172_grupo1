package tp9.persistences;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;

import tp9.enums.MVC;

public class Dependencias {

	private IPackageFragment pacote;
	private IType classe;
	private ArrayList<String> types;
	private ArrayList<String> imports;
	private int[] mvc;
	private MVC tipoClasse;
	private static boolean temUmaViewMVC = false;

	public Dependencias(IType classe, ArrayList<String> types, ArrayList<String> imports) {
		this.pacote = classe.getPackageFragment();
		this.classe = classe;
		this.types = types;
		this.imports = imports;
		this.mvc = countsMVC();

	}

	private int[] countsMVC() {
		try {
			Properties p = new Properties();

			p.load(new FileInputStream(System.getProperty("user.dir") + "/mvc.txt"));

			String[] model = p.getProperty("model").split(",");
			String[] view = p.getProperty("view").split(",");
			String[] controller = p.getProperty("controller").split(",");

			int countModel = 0, countView = 0, countController = 0;

			for (String m : model) {
				if (types.contains(m) && imports.contains(m)) {
					countModel++;
				}
			}

			for (String v : view) {
				if (types.contains(v) && imports.contains(v)) {
					countView++;
				}
			}

			for (String c : controller) {
				if (types.contains(c) && imports.contains(c)) {
					countController++;
				}
			}

			int[] mvc = { countModel, countView, countController };

			// verifica se pertence ao model
			if (mvc[0] > 0 && mvc[1] == 0 && mvc[2] == 0) {
				tipoClasse = MVC.MODEL;
			}

			// verifica se pertence ao view do mvc
			else if (mvc[1] > mvc[0] && mvc[1] > mvc[2] && mvc[0] != 0) {
				tipoClasse = MVC.VIEW_MVC;
				temUmaViewMVC = true;
			}
			
			// verifica se pertence ao view do mvp
			else if (mvc[1] > mvc[0] && mvc[1] > mvc[2] && mvc[0] ==0) {
				tipoClasse = MVC.VIEW_MVP;
			}

			// verifica se pertence ao controller/presenter
			else if (mvc[2] > mvc[0] && mvc[2] > mvc[1]) {
				tipoClasse = MVC.CONTROLLER;
			}

			else {
				tipoClasse = MVC.NENHUM;
			}

			return mvc;

		} catch (IOException e) {
			e.printStackTrace();
			int[] a = { 0, 0, 0 };
			return a;
		}
	}

	public IPackageFragment getPacote() {
		return pacote;
	}

	public IType getClasse() {
		return classe;
	}

	public ArrayList<String> getTypes() {
		return types;
	}

	public ArrayList<String> getImports() {
		return imports;
	}

	public int[] getMVCCounts() {
		return mvc;
	}

	public MVC getTipoClasse() {
		return tipoClasse;
	}
	
	public void setTipoClasse(MVC tipoClasse){
		this.tipoClasse = tipoClasse;
	}

	public static boolean temUmaViewMVC() {
		return temUmaViewMVC;
	}
	
	

}
