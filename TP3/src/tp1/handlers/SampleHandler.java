package tp1.handlers;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import java.util.Collections;
import java.util.Comparator;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.internal.resources.Project;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.internal.core.JavaProject;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeSelection;

@SuppressWarnings("restriction")
public class SampleHandler extends AbstractHandler {

	public static ArrayList<DadosDoProjeto> arrayDados;
	protected ArrayList<Integer> statements;
	private ArrayList<ITypeBinding> fCatchedExceptions;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		arrayDados = new ArrayList<DadosDoProjeto>();
		hideView();

		IProject iProject = getProjectFromWorkspace(event);

		try {
			getClassesMethods(iProject);
		} catch (CoreException e) {
			e.printStackTrace();
		}

		Metrica metrica = new Metrica();
		metrica.mediaTamanhoMetodos();
		metrica.tamanhoMetodo();

		ordenarMetodos();
		openView();

		MessageDialog.openInformation(HandlerUtil.getActiveShell(event), "Informação",
				" Métrica MCL (Caracteres por Linha) = " + metrica.getCPL() + "\n Métrica MLM (Linhas por Método) = "
						+ metrica.getLPM() + "\n Métrica TMM (Médio dos Métodos) = " + metrica.getTMM());
		return null;
	}

	private void getClassesMethods(final IProject project) throws CoreException {
		project.accept(new IResourceVisitor() {

			@Override
			public boolean visit(IResource resource) throws JavaModelException {
				if (resource instanceof IFile && resource.getName().endsWith(".java")) {
					ICompilationUnit unit = ((ICompilationUnit) JavaCore.create((IFile) resource));
					try {
						metodoInfo(unit);
					} catch (BadLocationException e) {
						e.printStackTrace();
					}
				}
				return true;
			}
		});
	}

	private void metodoInfo(ICompilationUnit unit) throws JavaModelException, BadLocationException {

		IType[] allTypes = unit.getAllTypes();
		System.out.println("Classe: " + unit.getElementName());
		DadosDoProjeto dados = null;
		String a = "";

		for (IType type : allTypes) {
			IMethod[] methods = type.getMethods();

			int cont = 0;
			String var = null;
			for (IMethod method : methods) {

				System.out.println("Método: " + method.getElementName());
				Document doc =  new Document(method.getSource());
				if (method.getSource().contains("return")) {
					int j = 0;
					String stringReturn = "";
					for (int i = 0; i < doc.getNumberOfLines(); i++) {
						if (doc.get(j, doc.getLineLength(i)).toString().contains("return")) {
							cont++;
							stringReturn = doc.get(j, doc.getLineLength(i)).toString();
						}
						j += doc.getLineLength(i);
					}

					if (cont == 1) {
						String[] separar = stringReturn.split("[;\\s]");
						for (int i = 0; i < separar.length; i++) {
							if (separar[i].equals("return")) {
								var = separar[i + 1];
							}
						}
					}
					int l = 0;
					for (int i = 0; i < doc.getNumberOfLines(); i++) {
						if(doc.get(l, doc.getLineLength(i)).toString().contains("return")){
							doc.replace(l, doc.getLineLength(i), "");
						}
						if(!(doc.get(l, doc.getLineLength(i)).toString().contains("int")) ||
								(doc.get(l, doc.getLineLength(i)).toString().contains("float"))||
								(doc.get(l, doc.getLineLength(i)).toString().contains("double"))||
								(doc.get(l, doc.getLineLength(i)).toString().contains("char"))||
								(doc.get(l, doc.getLineLength(i)).toString().contains("String"))
								){
							if (doc.get(l, doc.getLineLength(i)).toString().contains(var + "=")) {
								a= doc.get(l, doc.getLineLength(i)).toString().replace(var + "=", "return");
								doc.replace(l, doc.getLineLength(i), a);
							}
							if (doc.get(l, doc.getLineLength(i)).toString().contains(var + " =")) {
								a = doc.get(l, doc.getLineLength(i)).toString().replace(var + " =", "return");
								doc.replace(l, doc.getLineLength(i), a);
							}
						}
						l += doc.getLineLength(i);
					}

					int numChars = 0;
					for (int s = 0; s < doc.getNumberOfLines(); s++) {
						numChars += doc.getLineLength(s);
					}
					dados = new DadosDoProjeto(unit.getElementName(), method.getElementName(), doc.getNumberOfLines(),
							0, numChars);
					arrayDados.add(dados);
				}
				System.out.println(doc.get());
			}
		}
	}

	private IProject getProjectFromWorkspace(ExecutionEvent event) {

		TreeSelection selection = (TreeSelection) HandlerUtil.getCurrentSelection(event);

		if (selection == null || selection.getFirstElement() == null) {
			MessageDialog.openInformation(HandlerUtil.getActiveShell(event), "Information", "Please select a project");
			return null;
		}

		JavaProject jp;
		Project p;

		try {
			jp = (JavaProject) selection.getFirstElement();
			return jp.getProject();
		} catch (ClassCastException e) {
			p = (Project) selection.getFirstElement();
			return p.getProject();
		}
	}

	private void hideView() {
		IWorkbenchPage wp = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();

		// Acha a view :
		IViewPart myView = wp.findView("tp1.views.SampleView");

		// Esconde a view :
		wp.hideView(myView);
	}

	private void openView() {
		try {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView("tp1.views.SampleView");
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}

	private void ordenarMetodos() {
		Collections.sort(arrayDados, new Comparator() {
			public int compare(Object o1, Object o2) {
				DadosDoProjeto d1 = (DadosDoProjeto) o1;
				DadosDoProjeto d2 = (DadosDoProjeto) o2;
				return d1.getPorcentagem() > d2.getPorcentagem() ? -1
						: (d1.getPorcentagem() < d2.getPorcentagem() ? +1 : 0);
			}
		});
	}
}