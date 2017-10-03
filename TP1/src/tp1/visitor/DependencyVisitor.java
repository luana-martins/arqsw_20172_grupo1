package tp1.visitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jface.text.Document;

public class DependencyVisitor extends ASTVisitor {

	private CompilationUnit fullClass;
	private ArrayList<MethodDeclaration> arrayMethod;

	public DependencyVisitor(ICompilationUnit unit) throws JavaModelException {

		this.arrayMethod = new ArrayList<MethodDeclaration>();

		ASTParser parser = ASTParser.newParser(AST.JLS4);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(unit);
		parser.setCompilerOptions(JavaCore.getOptions());
		parser.setProject(unit.getJavaProject());
		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);

		this.fullClass = (CompilationUnit) parser.createAST(null);// parse
		this.fullClass.accept(this);
		
	}

	public ArrayList<MethodDeclaration> getMapMethods() {
		return arrayMethod;
	}

	@Override
	public boolean visit(MethodDeclaration node) {
		if (arrayMethod == null) {
			arrayMethod = new ArrayList<MethodDeclaration>();
		}
		arrayMethod.add(node);
		return true;
	}
	
}