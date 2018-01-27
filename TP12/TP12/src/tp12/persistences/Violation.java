package tp12.persistences;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;

public class Violation{

	private IPackageFragment packageA;
	private IPackageFragment packageB;
	private IType classA;
	private IType classB;
	private int ruleType;
	private IMethod method;
	private Dependencies dependencyFound;
	
	//construtor para violacao do tipo CANNOT
	public Violation(IType classA, IType classB, IMethod method, int ruleType, Dependencies dependencyFound) {
		this.packageA = classA.getPackageFragment();
		this.packageB = classB.getPackageFragment();
		this.classA = classA;
		this.classB = classB;
		this.ruleType = ruleType;
		this.method = method;
		this.dependencyFound = dependencyFound;
		
	}
	
	//construtor para violacao do tipo CAN/MUST
	public Violation(IPackageFragment packageA, IPackageFragment packageB, int ruleType){
		this.packageA = packageA;
		this.packageB = packageB;
		this.ruleType = ruleType;
	}
	
	
	public IPackageFragment getPackageA() {
		return packageA;
	}

	public IPackageFragment getPackageB() {
		return packageB;
	}

	public IType getClassA() {
		return classA;
	}

	public IType getClassB() {
		return classB;
	}

	public int getRuleType() {
		return ruleType;
	}

	public IMethod getMethod() {
		return method;
	}

	public Dependencies getDependencyFound() {
		return dependencyFound;
	}

	public void printAllViolations(){
		if(ruleType == 2){
			System.out.println("---------------------------------");
			System.out.println("A classe "+classA.getElementName()+" depende da classe "+classB.getElementName());
			System.out.println("Tipo dependencia: "+dependencyFound.toString());
			if(dependencyFound.compareTo(Dependencies.METHOD_PARAMETER) == 0){
				System.out.println("Metodo: "+method.getElementName());
			}
		}
		else{
			System.out.println("---------------------------------");
			System.out.println("O pacote "+packageA.getElementName()+" depende do pacote "+packageB.getElementName());
		}
	}
	
}
