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
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.MethodInvocation;




public class DependencyVisitor extends ASTVisitor {

	private CompilationUnit fullClass;
	private ArrayList<IfStatement> arrayIf;


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
	
	public ArrayList<IfStatement> getArrayIf() {
		return arrayIf;
	}
	
	@Override
    public boolean visit(IfStatement ifStatement) {
		System.out.println(ifStatement);
		System.out.println(ifStatement.getExpression());
		try{
			InfixExpression e = (InfixExpression) ifStatement.getExpression();
			if(e.getLeftOperand().resolveTypeBinding().isEnum()
					&& e.getRightOperand().resolveTypeBinding().isEnum()){
				if(arrayIf==null){
					arrayIf = new ArrayList<IfStatement>();
				}
				arrayIf.add(ifStatement);
			}
			
		}catch(ClassCastException e){
			MethodInvocation m =  (MethodInvocation) ifStatement.getExpression();
			if(m.getExpression().resolveTypeBinding().isEnum()){
				if(arrayIf==null){
					arrayIf = new ArrayList<IfStatement>();
				}
				arrayIf.add(ifStatement);
			}		
		}
		
		
		return true;
	}
}
