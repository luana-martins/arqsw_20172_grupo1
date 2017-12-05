package tp6.ast;

import java.util.ArrayList;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.eclipse.jdt.core.dom.Block;

import tp6.persistence.DadosRemodularizar;

public class DependencyVisitor extends ASTVisitor {

	private CompilationUnit fullClass;
	private String pacote;
	private DadosRemodularizar dados;
	private ICompilationUnit unit;
	private ArrayList<Object[]> dependenciesCP;

	public DependencyVisitor(ICompilationUnit unit) throws JavaModelException {
		this.dependenciesCP = new ArrayList<Object[]>();
		this.unit = unit;
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

	@Override
	public boolean visit(TypeDeclaration node) {

		try {
			IType type = (IType) unit.getTypes()[0];
			ITypeHierarchy typeHierarchy = type.newSupertypeHierarchy(null);

			IType[] typeSuperclasses = typeHierarchy.getAllSuperclasses(type);
			for (IType t : typeSuperclasses) {
				if (node.getSuperclassType() != null && t.getFullyQualifiedName()
						.equals(node.getSuperclassType().resolveBinding().getQualifiedName())) {
					this.dependenciesCP.add(new Object[] { t.getFullyQualifiedName() });
					System.out.println("CLASSE  "+node.getName() + "  DEPENDE DE  " + t.getFullyQualifiedName());
				}
			}

			IType[] typeSuperinter = typeHierarchy.getAllInterfaces();

			for (IType t : typeSuperinter) {
				for (Object it : node.superInterfaceTypes()) {

					SimpleType st = (SimpleType) it;
					if (t.getFullyQualifiedName().equals(st.getName().resolveTypeBinding().getQualifiedName())) {
						this.dependenciesCP.add(new Object[] { t.getFullyQualifiedName() });
						System.out.println("CLASSE  "+node.getName() + "  DEPENDE DE  " + t.getFullyQualifiedName());
					}
				}
			}
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	@Override
	public boolean visit(FieldDeclaration node) {
		Type a = node.getType();
		this.dependenciesCP.add(new Object[] { node.getType().resolveBinding().getQualifiedName() });
		//sem pegar o tipo da lista new Object[] { this.getTargetClassName(node.getType().resolveBinding()}
		System.out.println("ATRIBUTOS   "+ node.getType().resolveBinding().getQualifiedName());
		return true;
	}

	
	@Override
	public boolean visit(MethodDeclaration node) {
		
		for (Object o : node.parameters()) {
			if (o instanceof SingleVariableDeclaration) {
				SingleVariableDeclaration svd = (SingleVariableDeclaration) o;
				System.out.println("PARAMETROS "+svd.getType().resolveBinding().getQualifiedName());
				Type b = svd.getType();
				this.dependenciesCP.add(new Object[] {svd.getType().resolveBinding() });
			}
		}
		return true;
	}
	
	
	@Override
	public boolean visit(ClassInstanceCreation node) {

		switch (node.getNodeType()) {
		case ASTNode.FIELD_DECLARATION:
			// FieldDeclaration fd = (FieldDeclaration) relevantParent;
			this.dependenciesCP.add(new Object[] { node.getType().resolveBinding().getBinaryName() });
		case ASTNode.METHOD_DECLARATION:
			// MethodDeclaration md = (MethodDeclaration) relevantParent;
			this.dependenciesCP.add(new Object[] {  node.getType().resolveBinding().getBinaryName() });
		case ASTNode.INITIALIZER:
			this.dependenciesCP.add(new Object[] { node.getType().resolveBinding().getBinaryName() });
		}
		System.out.println("INSTANCIA   "+node.getType().resolveBinding().getBinaryName());
		return true;
	}
	
	public String getPacote() {
		return pacote;
	}

	public DadosRemodularizar getDados() {
		return dados;
	}

}
