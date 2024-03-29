package tp8.ast;

import java.util.ArrayList;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class DependencyVisitor extends ASTVisitor {

	private CompilationUnit fullClass;
	private IPackageFragment pacote;
	private IType clazz;
	private ICompilationUnit unit;
	private ArrayList<String> dependencias;

	public DependencyVisitor(ICompilationUnit unit) throws JavaModelException {
		dependencias = new ArrayList<String>();
		this.unit = unit;
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(unit);
		parser.setCompilerOptions(JavaCore.getOptions());
		parser.setProject(unit.getJavaProject());
		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);

		this.fullClass = (CompilationUnit) parser.createAST(null);
		this.fullClass.accept(this);
	}
	
	@Override
	public boolean visit(PackageDeclaration pack) {
		pacote = (IPackageFragment) pack.resolveBinding().getJavaElement();
		return true;
	}


	@Override
	public boolean visit(TypeDeclaration node) {

		clazz = (IType) node.resolveBinding().getJavaElement();
		try {

			IType type = (IType) unit.getTypes()[0];
			ITypeHierarchy typeHierarchy = type.newSupertypeHierarchy(null);
			IType[] typeSuperclasses = typeHierarchy.getAllSuperclasses(type);
			for (IType t : typeSuperclasses) {
				if (node.getSuperclassType() != null && t.getFullyQualifiedName()
						.equals(node.getSuperclassType().resolveBinding().getQualifiedName()) && !node.resolveBinding().isPrimitive()) {
					if (!dependencias.contains(t.getElementName())) {
						dependencias.add(t.getElementName());
					}
				}
			}

			IType[] typeSuperinter = typeHierarchy.getAllInterfaces();

			for (IType t : typeSuperinter) {
				for (Object it : node.superInterfaceTypes()) {

					SimpleType st = (SimpleType) it;
					if (t.getFullyQualifiedName().equals(st.getName().resolveTypeBinding().getQualifiedName()) && !node.resolveBinding().isPrimitive()) {
						if (!dependencias.contains(t.getElementName())) {
							dependencias.add(t.getElementName());
						}
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

		if (!dependencias.contains(node.getType().resolveBinding().getName()) && !node.getType().isPrimitiveType()) {
			dependencias.add(node.getType().resolveBinding().getName());
		}

		return true;
	}

	@Override
	public boolean visit(MethodDeclaration node) {

		for (Object o : node.parameters()) {
			if (o instanceof SingleVariableDeclaration) {
				SingleVariableDeclaration svd = (SingleVariableDeclaration) o;
				if (!dependencias.contains(svd.getType().resolveBinding().getName()) && !svd.getType().isPrimitiveType()) {
					dependencias.add(svd.getType().resolveBinding().getName());
				}
			}
		}
		return true;
	}

	@Override
	public boolean visit(ClassInstanceCreation node) {
		if (!dependencias.contains(node.getType().resolveBinding().getName()) && !node.getType().isPrimitiveType()) {
			dependencias.add(node.getType().resolveBinding().getName());
		}
		return true;
	}

	public ArrayList<String> getDependenciasClasse() {
		return dependencias;
	}

	public IType getClazz() {
		return clazz;
	}
	
	public IPackageFragment getPacote(){
		return pacote;
	}

}
