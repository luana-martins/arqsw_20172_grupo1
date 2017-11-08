package tp5.handlers;

import java.util.ArrayList;

import org.eclipse.jface.text.BadLocationException;


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

import tp5.ast.DependencyVisitor;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeSelection;

@SuppressWarnings("restriction")
public class SampleHandler extends AbstractHandler {

	public static ArrayList<Violacao> arrayDados;
	public static ExecutionEvent event;
	protected ArrayList<Integer> statements;
	public static String pacote = "";
	public static Boolean anot;
	public static ArrayList<DadosDoProjeto> dadosProjeto;
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		dadosProjeto = new ArrayList<DadosDoProjeto>();
		SampleHandler.event = event;
		arrayDados= new ArrayList<Violacao>();
		
		hideView();
		
		IProject iProject = getProjectFromWorkspace(event);

		try {
			getMethods(iProject);
		} catch (CoreException e) {
			e.printStackTrace();
		}	
		PadraoArquitetural pa = new PadraoArquitetural();
		//pa.print();
		pa.popular();
		arrayDados = pa.conferirConversa();
		openView();
		return null;
	}

	private void getMethods(final IProject project) throws CoreException {
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
		try{
			DependencyVisitor dp = new DependencyVisitor(unit);
			//arrayDados.addAll(dp.getArrayMethod());
			pacote = dp.getPack();
		}catch(NullPointerException e){
			e.printStackTrace();
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