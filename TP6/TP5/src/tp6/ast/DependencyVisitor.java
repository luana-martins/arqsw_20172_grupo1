package tp6.ast;

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

import tp6.persistence.DadosRemodularizar;

public class DependencyVisitor extends ASTVisitor {

	private CompilationUnit fullClass;
	private String pacote;
	private DadosRemodularizar dados;

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
	public boolean visit(TypeDeclaration anota) {

		IType iType = (IType) anota.resolveBinding().getJavaElement();

		try {

			ITypeHierarchy th = iType.newTypeHierarchy(null);

			if ((th.toString().contains("Window")) || (th.getAllTypes().toString().contains("Frame"))) {
				dados = new DadosRemodularizar("view", iType);
			} else if (((th.toString().contains("ActionListener")) || (th.toString().contains("ChangeListener"))) && (th.toString().contains("EventListener"))) {
				dados = new DadosRemodularizar("controller", iType);
			} else if ((th.toString().contains("EventObject")) || (th.toString().contains("Adapter"))) {
				dados = new DadosRemodularizar("model", iType);
			} else if((th.getAllSupertypes(iType).length==1) && (th.toString().contains("Object"))) {
				dados = new DadosRemodularizar("model", iType);
			}
			else {
				dados = new DadosRemodularizar(iType.getPackageFragment().getElementName() + " (atual)", iType);
			}

		} catch (JavaModelException e) {
			e.printStackTrace();
		}

		pacote = iType.getPackageFragment().getElementName();
		return true;
	}

	public String getPacote() {
		return pacote;
	}

	public DadosRemodularizar getDados() {
		return dados;
	}

}
