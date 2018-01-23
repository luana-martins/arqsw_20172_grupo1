package tp11.handlers;

import java.util.ArrayList;
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

import tp11.ast.DependencyVisitor;
import tp11.persistences.Dependencias;
import tp11.persistences.DependenciasPacote;
import tp11.regras.Regras;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeSelection;

@SuppressWarnings("restriction")
public class SampleHandler extends AbstractHandler {

	public static ExecutionEvent event;
	public static IJavaProject javaProject;
	public static ArrayList<String> pac;
	public static ArrayList<DependenciasPacote> dependencias;

	private ArrayList<IPackageFragment> todosPacotes;
	private ArrayList<Dependencias> classesDependencias;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		todosPacotes = new ArrayList<IPackageFragment>();
		classesDependencias = new ArrayList<Dependencias>();
		pac = new ArrayList<String>();
		dependencias = new ArrayList<DependenciasPacote>();

		try {
			SampleHandler.event = event;

			hideView();

			IProject iProject = getProjectFromWorkspace(event);
			if (iProject == null) {
				return null;
			}

			getDependencies(iProject);
			Regras regras = new Regras(classesDependencias);

			for (int i = 0; i < pac.size(); i++) {
				ArrayList<Integer> a = new ArrayList<Integer>();
				for (int j = 0; j < pac.size(); j++) {
					a.add(regras.conversa(pac.get(i), pac.get(j), 0));
				}
				dependencias.add(new DependenciasPacote(pac.get(i), a));
			}

			openView();

		} catch (JavaModelException e) {
			e.printStackTrace();
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void getDependencies(final IProject project) throws CoreException {
		project.accept(new IResourceVisitor() {

			@Override
			public boolean visit(IResource resource) throws JavaModelException {
				if (resource instanceof IFile && resource.getName().endsWith(".java")) {
					ICompilationUnit unit = ((ICompilationUnit) JavaCore.create((IFile) resource));
					DependencyVisitor dp = new DependencyVisitor(unit);
					classesDependencias.add(new Dependencias(dp.getClazz(), dp.getDependenciasClasse()));
					if (!todosPacotes.contains(dp.getPacote())) {
						todosPacotes.add(dp.getPacote());
						pac.add(dp.getPacote().getElementName());
					}
				}
				return true;
			}
		});
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
		IViewPart myView = wp.findView("tp1.views.SampleView");
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