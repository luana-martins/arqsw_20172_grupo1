package tp12.ast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMethod;
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
	private ArrayList<String> extendsAndImplementsTypes;
	private ArrayList<String> attributesTypes;
	private Map<IMethod, ArrayList<String>> methodsAndParametersTypes;
	private ArrayList<String> parametersMethod;
	private ArrayList<String> allOthersInstancesTypes;
	private IType clazz;
	private ICompilationUnit unit;

	public DependencyVisitor(ICompilationUnit unit) throws JavaModelException {
		extendsAndImplementsTypes = new ArrayList<String>();
		attributesTypes = new ArrayList<String>();
		allOthersInstancesTypes = new ArrayList<String>();
		methodsAndParametersTypes = new HashMap<IMethod, ArrayList<String>>();
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
					if (!extendsAndImplementsTypes.contains(t.getElementName())) {
						extendsAndImplementsTypes.add(t.getFullyQualifiedName());
					}
				}
			}

			IType[] typeSuperinter = typeHierarchy.getAllInterfaces();

			for (IType t : typeSuperinter) {
				for (Object it : node.superInterfaceTypes()) {

					SimpleType st = (SimpleType) it;
					if (t.getFullyQualifiedName().equals(st.getName().resolveTypeBinding().getQualifiedName()) && !node.resolveBinding().isPrimitive()) {
						if (!extendsAndImplementsTypes.contains(t.getElementName())) {
							extendsAndImplementsTypes.add(t.getFullyQualifiedName());
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
		if (!attributesTypes.contains(node.getType().resolveBinding().getName()) && !node.getType().isPrimitiveType()) {
				attributesTypes.add(node.getType().resolveBinding().getQualifiedName());
		}

		return true;
	}

	@Override
	public boolean visit(MethodDeclaration node) {
		
		parametersMethod = new ArrayList<String>();
		
		for (Object o : node.parameters()) {
			if (o instanceof SingleVariableDeclaration) {
				SingleVariableDeclaration svd = (SingleVariableDeclaration) o;
				
				if (!parametersMethod.contains(svd.getType().resolveBinding().getName()) && !svd.getType().isPrimitiveType()) {
					parametersMethod.add(svd.getType().resolveBinding().getQualifiedName());
					
				}
			}
		}
		
		methodsAndParametersTypes.put((IMethod) node.resolveBinding().getJavaElement(), parametersMethod);
		
		
		return true;
	}

	@Override
	public boolean visit(ClassInstanceCreation node) {
		if (!allOthersInstancesTypes.contains(node.getType().resolveBinding().getName()) && !node.getType().isPrimitiveType()) {
			allOthersInstancesTypes.add(node.getType().resolveBinding().getQualifiedName());
		}
		return true;
	}

	public IType getClazz() {
		return clazz;
	}
	
	public IPackageFragment getPacote(){
		return pacote;
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
	
	

}
