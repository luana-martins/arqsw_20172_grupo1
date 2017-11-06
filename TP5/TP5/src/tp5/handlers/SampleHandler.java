package tp5.handlers;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ITypeBinding;
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
	public static ExecutionEvent event;
	protected ArrayList<Integer> statements;
	private ArrayList<ITypeBinding> fCatchedExceptions;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		SampleHandler.event = event;
		arrayDados = new ArrayList<DadosDoProjeto>();
		hideView();

		IProject iProject = getProjectFromWorkspace(event);

		try {
			getClassesMethods(iProject);
		} catch (CoreException e) {
			e.printStackTrace();
		}

		
		openView();

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
		String a = "";

		for (IType type : allTypes) {
			IMethod[] methods = type.getMethods();

			int cont = 0;
			String var = null;
			for (IMethod method : methods) {
				boolean mudou = false;
				Document doc = new Document(method.getSource());
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
						if (doc.get(l, doc.getLineLength(i)).toString().contains("return")) {
							doc.replace(l, doc.getLineLength(i), "");
						}
						if (!(doc.get(l, doc.getLineLength(i)).toString().contains("int"))
								|| (doc.get(l, doc.getLineLength(i)).toString().contains("float"))
								|| (doc.get(l, doc.getLineLength(i)).toString().contains("double"))
								|| (doc.get(l, doc.getLineLength(i)).toString().contains("char"))
								|| (doc.get(l, doc.getLineLength(i)).toString().contains("String"))) {
							if (doc.get(l, doc.getLineLength(i)).toString().contains(var + "=")) {
								a = doc.get(l, doc.getLineLength(i)).toString().replace(var + "=", "return");
								doc.replace(l, doc.getLineLength(i), a);
								mudou = true;
							}
							if (doc.get(l, doc.getLineLength(i)).toString().contains(var + " =")) {
								a = doc.get(l, doc.getLineLength(i)).toString().replace(var + " =", "return");
								doc.replace(l, doc.getLineLength(i), a);
								mudou = true;
							}
						}
						l += doc.getLineLength(i);
					}

				}
				if (mudou) {
					arrayDados.add(new DadosDoProjeto(type.getFullyQualifiedName(), method.getElementName(),
							doc.get().toString()));
				}

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

}