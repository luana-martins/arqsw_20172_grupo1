package tp6.handlers;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.internal.resources.Project;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.internal.core.JavaProject;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import tp6.ast.DependencyVisitor;
import tp6.persistence.DadosMetodo;
import tp6.persistence.DadosRemodularizar;
import tp6.persistence.Violacao;
import tp6.refactorings.MoveClass;


import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeSelection;

@SuppressWarnings("restriction")
public class SampleHandler extends AbstractHandler {

	public static ArrayList<Violacao> arrayDados;
	public static ExecutionEvent event;
	public static ArrayList<DadosMetodo> dadosProjeto;
	public ArrayList<String> pacotes;
	public ArrayList<DadosRemodularizar> dadosNovaArq;
	

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		try {
			dadosProjeto = new ArrayList<DadosMetodo>();
			SampleHandler.event = event;
			arrayDados = new ArrayList<Violacao>();
			pacotes = new ArrayList<String>();
			dadosNovaArq = new ArrayList<DadosRemodularizar>();

			hideView();

			IProject iProject = getProjectFromWorkspace(event);
			if(iProject == null){
				return null;
			}
			
			IJavaProject javaProject = JavaCore.create(iProject);

			getClasses(iProject);
			
			if(pacotes.size() > 1){
				MessageDialog.openInformation(HandlerUtil.getActiveShell(event), "Informação", "Projeto possui mais de um pacote");
				return null;
			}

			createArchMVC(javaProject);
			
			redistributeClasses(javaProject);
			
//			for(int i = 0; i < DependencyVisitor.mapaRemodularizacao.size();i++) {
//				if(DependencyVisitor.mapaRemodularizacao.containsKey("view")) {
//					System.out.println(DependencyVisitor.mapaRemodularizacao.values());
//				}
//			}
				
			openView();
			

		} catch (JavaModelException e) {
			e.printStackTrace();
		} catch (CoreException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	private void redistributeClasses(IJavaProject javaProject) throws JavaModelException {
		
		MoveClass mc = new MoveClass();
		IPackageFragment[] packages = javaProject.getPackageFragments();
		for(int i=0; i<packages.length; i++){
			
			if(packages[i].getElementName().compareTo("model") == 0){
				for(int j=0;j<dadosNovaArq.size();j++){
					if(dadosNovaArq.get(j).getTipoPacote().compareTo("model") == 0){
						mc.performMoveClassRefactoring(dadosNovaArq.get(j).getClasse(), packages[i]);
					}
				}
			}
			
			if(packages[i].getElementName().compareTo("view") == 0){
				for(int j=0;j<dadosNovaArq.size();j++){
					if(dadosNovaArq.get(j).getTipoPacote().compareTo("view") == 0){
						mc.performMoveClassRefactoring(dadosNovaArq.get(j).getClasse(), packages[i]);
					}
				}
			}
			
			if(packages[i].getElementName().compareTo("controller") == 0){
				for(int j=0;j<dadosNovaArq.size();j++){
					if(dadosNovaArq.get(j).getTipoPacote().compareTo("controller") == 0){
						mc.performMoveClassRefactoring(dadosNovaArq.get(j).getClasse(), packages[i]);
					}
				}
			}
		}
		
		
	}
	
	private void createArchMVC(IJavaProject javaProject) throws JavaModelException{
		IPackageFragmentRoot packageSrc = null;
		IPackageFragmentRoot[] pfr = javaProject.getAllPackageFragmentRoots();
		for (int i = 0; i < pfr.length; i++) {
			if (pfr[i].getElementName().compareTo("src") == 0) {
				packageSrc = pfr[i];
				break;
			}
		}
		
		packageSrc.createPackageFragment("model", true, new NullProgressMonitor());
		packageSrc.createPackageFragment("view", true, new NullProgressMonitor());
		packageSrc.createPackageFragment("controller", true, new NullProgressMonitor());

	}

	private void getClasses(final IProject project) throws CoreException {
		project.accept(new IResourceVisitor() {

			@Override
			public boolean visit(IResource resource) throws JavaModelException {
				if (resource instanceof IFile && resource.getName().endsWith(".java")) {
					ICompilationUnit unit = ((ICompilationUnit) JavaCore.create((IFile) resource));
					DependencyVisitor dp = new DependencyVisitor(unit);
					if(!pacotes.contains(dp.getPacote())){
						pacotes.add(dp.getPacote());
					}
					
					dadosNovaArq.add(dp.getDados());
					
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