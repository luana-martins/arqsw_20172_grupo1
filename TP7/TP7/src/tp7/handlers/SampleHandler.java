package tp7.handlers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

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

import tp7.ast.DependencyVisitor;
import tp7.persistences.Dependencias;
import tp7.persistences.StatusConversa;
import tp7.regras.Regras;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeSelection;

@SuppressWarnings("restriction")
public class SampleHandler extends AbstractHandler {

	public static ExecutionEvent event;
	public static IJavaProject javaProject;
	private ArrayList<IPackageFragment> todosPacotes;
	private ArrayList<Dependencias> classesDependencias;
	private Map<IPackageFragment,ArrayList<Dependencias>> classesPacotes;
	public static ArrayList<StatusConversa> recomendacoes;
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException   {
		classesPacotes = new HashMap<IPackageFragment,ArrayList<Dependencias>>();
		todosPacotes = new ArrayList<IPackageFragment>();
		classesDependencias = new ArrayList<Dependencias>();
		recomendacoes = new ArrayList<StatusConversa>();
		
		try {
			SampleHandler.event = event;

			hideView();

			IProject iProject = getProjectFromWorkspace(event);
			if (iProject == null) {
				return null;
			}

			getDependencies(iProject);
			
			//Separa em um map as classes por pacotes
			for(int i=0; i<todosPacotes.size(); i++){
				ArrayList<Dependencias> classesMesmoPacote = new ArrayList<Dependencias>();
				for(Dependencias classe : classesDependencias){
					if(classe.getPacote().getElementName().compareTo(todosPacotes.get(i).getElementName()) == 0){
						classesMesmoPacote.add(classe);
					//	System.out.println(" pacote "+classe.getPacote().getElementName());
					
					}
				}
				
				classesPacotes.put(todosPacotes.get(i), classesMesmoPacote);
			}

			
			
			
				
			
			ArrayList<StatusConversa> statusConversas = new ArrayList<StatusConversa>();
			Regras regras = new Regras( classesDependencias);
			statusConversas = regras.especificacaoDasDependencias();
			
			for(int i = 0; i < statusConversas.size();i++) {
				System.out.println("tipo dep "+ statusConversas.get(i).getTipoDependencia()+" "
						+ statusConversas.get(i).getClasseA()+" depende de "+statusConversas.get(i).getClasseB());
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