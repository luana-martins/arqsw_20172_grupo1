package tp12.persistences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;

public class Dependency {

	private IPackageFragment pckg;
	private IType clazz;
	private ArrayList<String> extendsAndImplementsTypes;
	private ArrayList<String> attributesTypes;
	private Map<IMethod, ArrayList<String>> methodsAndParametersTypes;
	private ArrayList<String> allOthersInstancesTypes;

	public Dependency(IPackageFragment pckg, IType clazz, ArrayList<String> extendsAndImplementsTypes,
			ArrayList<String> attributesTypes, Map<IMethod, ArrayList<String>> methodsAndParametersTypes,
			ArrayList<String> allOthersInstancesTypes) {
		this.pckg = pckg;
		this.clazz = clazz;
		this.extendsAndImplementsTypes = extendsAndImplementsTypes;
		this.attributesTypes = attributesTypes;
		this.methodsAndParametersTypes = methodsAndParametersTypes;
		this.allOthersInstancesTypes = allOthersInstancesTypes;
	}

	public IPackageFragment getPckg() {
		return pckg;
	}

	public IType getClazz() {
		return clazz;
	}

	public ArrayList<String> getExtendsAndImplementsTypes() {
		return extendsAndImplementsTypes;
	}

	public ArrayList<String> getAttributesTypes() {
		return attributesTypes;
	}

	public Map<IMethod, ArrayList<String>> getMethodsAndParametersTypes() {
		return methodsAndParametersTypes;
	}

	public ArrayList<String> getAllOthersInstancesTypes() {
		return allOthersInstancesTypes;
	}
	
	public void printAllDependencies(){
		System.out.println("------------------------------------------------------");
		System.out.println("Classe: "+clazz.getElementName());
		System.out.println("Pacote: "+pckg.getElementName());
		System.out.println("Dependencias Extends/Implements: "+Arrays.toString(extendsAndImplementsTypes.toArray()));
		System.out.println("Dependencias Atributos: "+Arrays.toString(attributesTypes.toArray()));
		for(IMethod m : methodsAndParametersTypes.keySet()){
			System.out.println("Dependencias Parametros do metodo "+m.getElementName()+": "+Arrays.toString(methodsAndParametersTypes.get(m).toArray()));
		}
		System.out.println("Dependencias de outras instancias: "+Arrays.toString(allOthersInstancesTypes.toArray()));
	}
}
