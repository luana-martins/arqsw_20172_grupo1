package tp4.ast;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;

import tp4.handlers.Classes;
import tp4.handlers.Enumerado;

public class DependencyVisitor extends ASTVisitor {

	private CompilationUnit fullClass;
	private ArrayList<IfStatement> arrayIf;
	private Enumerado enumerado;
	private Classes classe;
	private List parametros;
	private String a = "";
	private String metodo="";

	@SuppressWarnings("deprecation")
	public DependencyVisitor(ICompilationUnit unit) throws JavaModelException {
		
			ASTParser parser = ASTParser.newParser(AST.JLS4); 
			parser.setKind(ASTParser.K_COMPILATION_UNIT);
			parser.setSource(unit);
			parser.setCompilerOptions(JavaCore.getOptions());
			parser.setProject(unit.getJavaProject());
			parser.setResolveBindings(true);
			parser.setBindingsRecovery(true);
			
			this.fullClass = (CompilationUnit) parser.createAST(null);
			this.fullClass.accept(this);
	}
	
	public ArrayList<IfStatement> getArrayIf() {
		return arrayIf;
	}
	
	public boolean visit(EnumDeclaration anota){
		enumerado = new Enumerado( anota.enumConstants(),anota.getName().toString());
		a=anota.getName().toString();
		return true;
	}
	
	public boolean visit(MethodDeclaration anota){
		if(!anota.parameters().isEmpty()) {
			parametros = anota.parameters();
		}
		metodo = anota.getName().toString();
		return true;
	}
	
	@Override
    public boolean visit(IfStatement ifStatement) {
		String operando = "";
		try{
			InfixExpression e = (InfixExpression) ifStatement.getExpression();
			if(e.getLeftOperand().resolveTypeBinding().isEnum()
					&& e.getRightOperand().resolveTypeBinding().isEnum()){
				if(arrayIf==null){
					arrayIf = new ArrayList<IfStatement>();
				}
				operando = e.getLeftOperand().toString();
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
		classe = new Classes(metodo, a, parametros);
		return true;
	}
	
	public Enumerado getEnumerado() {
		return enumerado;
	}

}
