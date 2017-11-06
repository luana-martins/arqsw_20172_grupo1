package tp4.ast;

import java.util.ArrayList;


import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;




public class DependencyVisitor extends ASTVisitor {

	private CompilationUnit fullClass;
	private ArrayList<MethodDeclaration> arrayMethod;


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
	public boolean visit(MethodDeclaration node) {
		
		//IMethod imeth = (IMethod) node.resolveBinding().getJavaElement();
		
		if(arrayMethod==null){
			arrayMethod = new ArrayList<MethodDeclaration>();
		}
		arrayMethod.add(node);
		
		return true;
	}
	
	@Override
    public boolean visit(IfStatement ifStatement) {
		System.out.println(ifStatement);
		System.out.println(ifStatement.getExpression());
			
		return true;
	}
}
