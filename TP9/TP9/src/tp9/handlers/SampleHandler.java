package tp9.handlers;

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

import tp9.ast.DependencyVisitor;
import tp9.enums.Archs;
import tp9.persistences.Dependencias;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeSelection;

@SuppressWarnings("restriction")
public class SampleHandler extends AbstractHandler {

	public static ExecutionEvent event;
	public static IJavaProject javaProject;
	public static ArrayList<Dependencias> classesDependencias;
	public static boolean temUmaViewMVC;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		classesDependencias = new ArrayList<Dependencias>();
		temUmaViewMVC = false;

		try {
			SampleHandler.event = event;

			hideView();

			IProject iProject = getProjectFromWorkspace(event);
			if (iProject == null) {
				return null;
			}

			getDependencies(iProject);

			for (Dependencias classe : classesDependencias) {
				System.out.println(
						"----------------------------------------------------------------------------------------");
				System.out.println("Classe: " + classe.getClasse().getFullyQualifiedName());
				System.out.println("Dependencias: " + classe.getDependencias());
				System.out.println("Correnpondencias em model: " + classe.getMVCCounts()[0]);
				System.out.println("Correnpondencias em view: " + classe.getMVCCounts()[1]);
				System.out.println("Correnpondencias em controller: " + classe.getMVCCounts()[2]);

			}

			if (temUmaViewMVC) {
				for (Dependencias classe : classesDependencias) {
					if (classe.getTipoClasse() == Archs.VIEW_MVP || classe.getTipoClasse() == Archs.VIEW_MVC) {
						classe.setTipoClasse(Archs.VIEW);
					}
				}

				MessageDialog.openInformation(HandlerUtil.getActiveShell(event), "Informação", "Arquiteura MVC");
				openView();

			} else {
				for (Dependencias classe : classesDependencias) {
					if (classe.getTipoClasse() == Archs.VIEW_MVP || classe.getTipoClasse() == Archs.VIEW_MVC) {
						classe.setTipoClasse(Archs.VIEW);
					}
					if (classe.getTipoClasse() == Archs.CONTROLLER) {
						classe.setTipoClasse(Archs.PRESENTER);
					}
				}

				MessageDialog.openInformation(HandlerUtil.getActiveShell(event), "Informação", "Arquiteura MVP");
				openView();
			}

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
					classesDependencias.add(new Dependencias(dp.getClazz(), dp.getDependencias()));
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