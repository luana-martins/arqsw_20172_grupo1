package tp1.handlers;

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
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.internal.core.JavaProject;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import tp1.visitor.DependencyVisitor;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeSelection;

public class SampleHandler extends AbstractHandler {

	private ArrayList<MethodDeclaration> allMethods;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		IProject iProject = getProjectFromWorkspace(event);

		try {
			allMethods = getClassesMethods(iProject);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int totalTamMetodos = 0;
		for (int i = 0; i < allMethods.size(); i++) {
			int tamanhoMetodo = allMethods.get(i).getLength();
			totalTamMetodos += tamanhoMetodo;
			System.out.println("Metodo: " + allMethods.get(i).resolveBinding().getJavaElement().getElementName());
			System.out.println("Tamanho: " + tamanhoMetodo);
			System.out.println();
		}

		double mediaTamMetodos = totalTamMetodos / allMethods.size();

		for (int i = 0; i < allMethods.size(); i++) {
			int tamanhoMetodo = allMethods.get(i).getLength();
			if (tamanhoMetodo >= mediaTamMetodos * 2) {
				System.out.println("Metodo " + allMethods.get(i).resolveBinding().getJavaElement().getElementName()
						+ " parece ser longo");
				System.out.println();
			}

		}

		allMethods.clear();

		return null;

	}

	private ArrayList<MethodDeclaration> getClassesMethods(final IProject project) throws CoreException {

		ArrayList<MethodDeclaration> map = new ArrayList<MethodDeclaration>();
		project.accept(new IResourceVisitor() {

			@Override
			public boolean visit(IResource resource) throws JavaModelException {
				if (resource instanceof IFile && resource.getName().endsWith(".java")) {
					ICompilationUnit unit = ((ICompilationUnit) JavaCore.create((IFile) resource));

					DependencyVisitor dp = new DependencyVisitor(unit);
					map.addAll(dp.getMapMethods());

				}
				return true;
			}
		});
		return map;
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
}