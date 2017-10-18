package tp1.handlers;


import java.util.ArrayList;
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
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
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
		
		
		MessageDialog.openInformation(HandlerUtil.getActiveShell(event), "Informação", "Métrica CPL (Caracteres por Linha) = "+metrica.getCPL() 
																			+"\n Métrica LPM (Linhas por Método) = "+metrica.getLPM()
																			+"\n Métrica TMM (Tamanho Médio dos Métodos) = "+metrica.getTMM());
		ordenarMetodos();
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
		DadosDoProjeto dados = null;
		 	for (IType type : allTypes) {
		 		IMethod[] methods = type.getMethods();
				for(IMethod method : methods){
					Document doc = new Document(method.getSource());
					int numChars = 0;
					for (int i = 0; i < doc.getNumberOfLines(); i++) {
						numChars += doc.getLineLength(i);
					}
		 			dados =  new DadosDoProjeto(unit.getElementName(),method.getElementName(), doc.getNumberOfLines(), 0, numChars);
		 			arrayDados.add(dados);
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
	
	private void ordenarMetodos(){
		Collections.sort (arrayDados, new Comparator() {
            public int compare(Object o1, Object o2) {
                DadosDoProjeto d1 = (DadosDoProjeto) o1;
                DadosDoProjeto d2 = (DadosDoProjeto) o2;
                return d1.getPorcentagem() > d2.getPorcentagem() ? -1 : (d1.getPorcentagem() < d2.getPorcentagem() ? +1 : 0);
            }
        });
	}
}