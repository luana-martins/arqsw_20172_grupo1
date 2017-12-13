package tp8.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

import tp8.ast.DependencyVisitor;
import tp8.clusterings.Cluster;
import tp8.clusterings.KMeans;
import tp8.clusterings.KMeansResultado;
import tp8.clusterings.Punto;
import tp8.persistences.Dependencias;
import tp8.persistences.Grafo;
import tp8.persistences.Recomendacao;
import tp8.similaridade.Similaridade;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeSelection;

@SuppressWarnings("restriction")
public class SampleHandler extends AbstractHandler {

	public static ExecutionEvent event;
	public static IJavaProject javaProject;
	private ArrayList<IPackageFragment> todosPacotes;
	private ArrayList<Dependencias> classesDependencias;
	public static ArrayList<Recomendacao> recomendacoes;
	private ArrayList<Grafo> distancias;
	private HashMap<String, Integer> aux;
	private HashMap<String, String> print;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		todosPacotes = new ArrayList<IPackageFragment>();
		classesDependencias = new ArrayList<Dependencias>();
		recomendacoes = new ArrayList<Recomendacao>();
		distancias = new ArrayList<Grafo>();
		print = new HashMap<String, String>();
		aux = new HashMap<String, Integer>();

		try {
			SampleHandler.event = event;

			hideView();

			IProject iProject = getProjectFromWorkspace(event);
			if (iProject == null) {
				return null;
			}

			getDependencies(iProject);
			Similaridade si = new Similaridade();

			for (Dependencias d : classesDependencias) {
				for (int i = 0; i < classesDependencias.size(); i++) {
					if (d.getClasse().getFullyQualifiedName()
							.compareTo(classesDependencias.get(i).getClasse().getFullyQualifiedName()) == 0) {
						continue;
					}
					distancias.add(new Grafo(d.getClasse().getFullyQualifiedName(),
							classesDependencias.get(i).getClasse().getFullyQualifiedName(),
							si.similaridadePSC(d.getDependencias(), classesDependencias.get(i).getDependencias())));
				}
			}

			aplicaKMeans();

			openView();

		} catch (JavaModelException e) {
			e.printStackTrace();
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void aplicaKMeans() {
		List<Punto> puntos = new ArrayList<Punto>();

		for (Grafo grafo : distancias) {
			Punto p = new Punto(grafo);
			puntos.add(p);
		}

		KMeans kmeans = new KMeans();
		KMeansResultado resultado = kmeans.calcular(puntos, 3);

		int i = 0;
		for (Cluster cluster : resultado.getClusters()) {
			i++;
			for (Punto punto : cluster.getPuntos()) {
				aux.put(punto.toString(), i);
			}
		}

		for (String value : aux.keySet()) {
			for (int j = 0; j < distancias.size(); j++) {
				if (value.equals(distancias.get(j).getSimilaridade() + "")) {
					print.put(distancias.get(j).getClasse2(), aux.get(value) + "");
				}
			}
		}

		System.out.println("------ Cluster 1 ------");
		for (String value : print.keySet()) {
			if (print.get(value).equals("1")) {
				System.out.println("Classes: " + value);
			}
		}

		System.out.println("------ Cluster 2 ------");
		for (String value : print.keySet()) {
			if (print.get(value).equals("2")) {
				System.out.println("Classes: " + value);
			}
		}

		System.out.println("------ Cluster 3 ------");
		for (String value : print.keySet()) {
			if (print.get(value).equals("3")) {
				System.out.println("Classes: " + value);
			}
		}
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