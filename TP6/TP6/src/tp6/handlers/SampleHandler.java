package tp6.handlers;

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

import tp6.ast.DependencyVisitor;
import tp6.persistence.DadosRemodularizar;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeSelection;

@SuppressWarnings("restriction")
public class SampleHandler extends AbstractHandler {

	public static ExecutionEvent event;
	public ArrayList<String> pacotes;
	public static ArrayList<DadosRemodularizar> dadosNovaArq;
	public static IJavaProject javaProject;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		try {
			SampleHandler.event = event;
			pacotes = new ArrayList<String>();
			dadosNovaArq = new ArrayList<DadosRemodularizar>();

			hideView();

			IProject iProject = getProjectFromWorkspace(event);
			if (iProject == null) {
				return null;
			}

			getClasses(iProject);

			if (pacotes.size() > 1) {
				MessageDialog.openInformation(HandlerUtil.getActiveShell(event), "Informação",
						"Projeto possui mais de um pacote");
				return null;
			}

			javaProject = JavaCore.create(iProject);

			openView();

		} catch (JavaModelException e) {
			e.printStackTrace();
		} catch (CoreException e) {
			e.printStackTrace();
		}

		return null;
	}


	private void getClasses(final IProject project) throws CoreException {
		project.accept(new IResourceVisitor() {

			@Override
			public boolean visit(IResource resource) throws JavaModelException {
				if (resource instanceof IFile && resource.getName().endsWith(".java")) {
					ICompilationUnit unit = ((ICompilationUnit) JavaCore.create((IFile) resource));
					DependencyVisitor dp = new DependencyVisitor(unit);
					if (!pacotes.contains(dp.getPacote())) {
						pacotes.add(dp.getPacote());
					}

					if (dp.getDados() != null) {
						dadosNovaArq.add(dp.getDados());
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