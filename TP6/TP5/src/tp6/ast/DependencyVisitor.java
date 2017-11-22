package tp6.ast;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import tp6.persistence.DadosMetodo;
import tp6.persistence.DadosRemodularizar;


public class DependencyVisitor extends ASTVisitor {

	private CompilationUnit fullClass;
	private String pacote;
	private IType clazz;
	private ArrayList<DadosRemodularizar> arrayClasse;


	public DependencyVisitor(ICompilationUnit unit) throws JavaModelException {
		ASTParser parser = ASTParser.newParser(AST.JLS8); 
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(unit);
		parser.setCompilerOptions(JavaCore.getOptions());
		parser.setProject(unit.getJavaProject());
		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);

		this.fullClass = (CompilationUnit) parser.createAST(null);// parse
		this.fullClass.accept(this);
		
	}

	@Override
	public boolean visit(TypeDeclaration anota){
		IType iType = (IType) anota.resolveBinding().getJavaElement();
		arrayClasse= new ArrayList<DadosRemodularizar>();
		try {
			ITypeHierarchy th= iType.newTypeHierarchy(null);
			System.out.println(th);
			
			if(th.toString().contains("ActionListener")) {
				if(th.toString().contains("Window") || th.getAllTypes().toString().contains("Frame")) {
					DadosRemodularizar a = new DadosRemodularizar("view", fullClass);
					arrayClasse.add(a);
				}if(th.toString().contains("EventListener")) {
					DadosRemodularizar a = new DadosRemodularizar("controller", fullClass);
					arrayClasse.add(a);
				}
			}
			for(int i = 0; i < arrayClasse.size();i++) {
			System.out.println(arrayClasse.get(i).getChave()+"   "+arrayClasse.get(i).getClasses());
			}
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pacote = iType.getPackageFragment().getElementName();
		clazz = iType;
		return true;
	}

	@Override
	public boolean visit(ImportDeclaration node){

		//Imports packages
		//System.out.println(node);
		return true;
	}

	public String getPacote(){
		return pacote;
	}

	public IType getClazz() {
		return clazz;
	}

}
