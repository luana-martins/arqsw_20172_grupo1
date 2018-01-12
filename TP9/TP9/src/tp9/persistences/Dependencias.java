package tp9.persistences;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;

public class Dependencias {
	
	private IPackageFragment pacote;
	private IType classe;
	private ArrayList<String> types;
	private ArrayList<String> imports;
	private int[] mvc;
	
	public Dependencias(IType classe, ArrayList<String> types, ArrayList<String> imports){
		this.pacote = classe.getPackageFragment();
		this.classe = classe;
		this.types = types;
		this.imports = imports;
		this.mvc = countsMVC();
		
	}
	
	private int[] countsMVC() {
		try {
			Properties p = new Properties();

			p.load(new FileInputStream(System.getProperty("user.dir")+"/mvc.txt"));

			String[] model = p.getProperty("model").split(",");
			String[] view = p.getProperty("view").split(",");
			String[] controller = p.getProperty("controller").split(",");
			
			int countModel=0, countView=0, countController=0;
			
			for(String m : model){
				if(types.contains(m) && imports.contains(m)){
					countModel++;
				}
			}
			
			for(String v : view){
				if(types.contains(v) && imports.contains(v)){
					countView++;
				}
			}
			
			for(String c : controller){
				if(types.contains(c) && imports.contains(c)){
					countModel++;
				}
			}
			
			int[] mvc = {countModel,countView,countController};
			return mvc;

		} catch (IOException e) {
			e.printStackTrace();
			int[] a = {0,0,0};
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
	
	public int[] getMVCCounts(){
		return mvc;
	}
	
	
}
