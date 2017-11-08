package tp5.ast;

import java.util.ArrayList;


import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import tp5.handlers.DadosDoProjeto;
import tp5.handlers.SampleHandler;


public class DependencyVisitor extends ASTVisitor {

	private CompilationUnit fullClass;
	private ArrayList<MethodDeclaration> arrayMethod;
	private String pacote ="";
	DadosDoProjeto dados = null;
	String  nome = "";

	@SuppressWarnings("deprecation")
	public DependencyVisitor(ICompilationUnit unit) throws JavaModelException {
		
			ASTParser parser = ASTParser.newParser(AST.JLS4); // It was JSL3, but it
			// is now deprecated
			parser.setKind(ASTParser.K_COMPILATION_UNIT);
			parser.setSource(unit);
			parser.setCompilerOptions(JavaCore.getOptions());
			parser.setProject(unit.getJavaProject());
			parser.setResolveBindings(true);
			parser.setBindingsRecovery(true);
			
			this.fullClass = (CompilationUnit) parser.createAST(null);// parse
			this.fullClass.accept(this);
	}
	
	public ArrayList<MethodDeclaration> getArrayMethod() {
		return arrayMethod;
	}
	
	@Override
	public boolean visit(TypeDeclaration anota){
		nome = anota.getName().toString();
		return true;
	}

	@Override
	public boolean visit(PackageDeclaration pack) {
		pacote = pack.getName().toString();
		return true;
	}
	
	@Override
	public boolean visit(MethodDeclaration node) {
		
		arrayMethod = new ArrayList<MethodDeclaration>();
		arrayMethod.add(node);
		
		dados = new DadosDoProjeto(arrayMethod, pacote, nome, node.getName().toString());
		SampleHandler.dadosProjeto.add(dados);
		
		return true;
	}

	public String getPack(){
		return pacote;
	}

}
