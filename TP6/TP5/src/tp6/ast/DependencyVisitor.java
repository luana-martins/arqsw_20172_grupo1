package tp6.ast;

import java.util.ArrayList;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import tp6.handlers.DadosDeclaracao;
import tp6.handlers.DadosMetodo;
import tp6.handlers.SampleHandler;

public class DependencyVisitor extends ASTVisitor {

	private CompilationUnit fullClass;
	private ArrayList<MethodDeclaration> arrayMethod;
	private String pacote ="";
	DadosMetodo dados = null;
	String  nome = "";
	private ArrayList<DadosDeclaracao> arrayClasse;
	DadosDeclaracao decl;
	private ArrayList<String> parametros = null;
	ArrayList<String> estatico;

	@SuppressWarnings("deprecation")
	public DependencyVisitor(ICompilationUnit unit) throws JavaModelException {

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

	public ArrayList<MethodDeclaration> getArrayMethod() {
		return arrayMethod;
	}

	@Override
	public boolean visit(TypeDeclaration anota){
		nome = anota.getName().toString();
		arrayClasse = new ArrayList<DadosDeclaracao>();
		for(FieldDeclaration model : anota.getFields()) {
			decl = new DadosDeclaracao(model.getType()+ " " , model.fragments().get(0).toString());
			arrayClasse.add(decl);
		}
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
		parametros = new ArrayList<String>();
		arrayMethod.add(node);

		System.out.println("metodo name "+node.getName().toString());
		for(int i =0; i < node.parameters().size();i++) {
			parametros.add(node.parameters().get(i).toString());
			System.out.println(node.parameters().get(i).toString());
		}
		
		dados = new DadosMetodo(arrayMethod, pacote, nome, node.getName().toString(), arrayClasse, parametros);
		SampleHandler.dadosProjeto.add(dados);
		return true;
	}
}
