package tp9.persistences;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;

import tp9.enums.Archs;
import tp9.handlers.SampleHandler;

public class Dependencias {

	private IPackageFragment pacote;
	private IType classe;
	private ArrayList<String> dependencias;
	private int[] mvc;
	private Archs tipoClasse;

	public Dependencias(IType classe, ArrayList<String> dependencias) {
		this.pacote = classe.getPackageFragment();
		this.classe = classe;
		this.dependencias = dependencias;
		this.mvc = countsMVC();

	}

	private int[] countsMVC() {
		try {

			// le o arquivo com as classes pertencentes ao mvc
			Properties p = new Properties();
			p.load(new FileInputStream(System.getProperty("user.dir") + "/mvc.txt"));

			// salva cada classe em uma array de strings
			String[] model = p.getProperty("model").split(",");
			String[] view = p.getProperty("view").split(",");
			String[] controller = p.getProperty("controller").split(",");

			int countModel = 0, countView = 0, countController = 0;

			// verifica para cada dependencia do prejeto se contem classes do
			// mvc
			for (String m : model) {
				if (dependencias.contains(m)) {
					countModel++;
				}
			}

			for (String v : view) {
				if (dependencias.contains(v)) {
					countView++;
				}
			}

			for (String c : controller) {
				if (dependencias.contains(c)) {
					countController++;
				}
			}

			int[] mvc = { countModel, countView, countController };

			// verifica se pertence ao model
			if (mvc[0] > 0 && mvc[1] == 0 && mvc[2] == 0) {
				tipoClasse = Archs.MODEL;
			}

			// verifica se pertence ao view do mvc
			else if (mvc[1] > mvc[0] && mvc[1] > mvc[2] && mvc[0] != 0) {
				tipoClasse = Archs.VIEW_MVC;
				SampleHandler.temUmaViewMVC = true;
			}

			// verifica se pertence ao view do mvp
			else if (mvc[1] > mvc[0] && mvc[1] > mvc[2] && mvc[0] == 0) {
				tipoClasse = Archs.VIEW_MVP;
			}

			// verifica se pertence ao controller/presenter
			else if (mvc[2] > mvc[0] && mvc[2] > mvc[1]) {
				tipoClasse = Archs.CONTROLLER;
			}

			else {
				tipoClasse = Archs.MODEL_OU_NENHUM;
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

	public ArrayList<String> getDependencias() {
		return dependencias;
	}

	public int[] getMVCCounts() {
		return mvc;
	}

	public Archs getTipoClasse() {
		return tipoClasse;
	}

	public void setTipoClasse(Archs tipoClasse) {
		this.tipoClasse = tipoClasse;
	}
}
