package tp6.views;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.part.*;

import tp6.handlers.SampleHandler;
import tp6.persistence.DadosRemodularizar;
import tp6.refactorings.MoveClass;

import javax.swing.JOptionPane;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.SWT;

public class SampleView extends ViewPart {

	public static final String ID = "tp1.views.SampleView";

	private TableViewer viewer;
	private Action doubleClickAction;
	private Action applyRemodularizationAction;

	public void createPartControl(Composite parent) {
		GridLayout layout = new GridLayout(1, false);
		parent.setLayout(layout);
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);

		String[] titles = { "Classe", "Pacote" };
		int[] bounds = { 300, 300 };

		TableViewerColumn col = createTableViewerColumn(titles[0], bounds[0], 0);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				DadosRemodularizar dr = (DadosRemodularizar) element;
				return dr.getClasse().getElementName();
			}
		});

		col = createTableViewerColumn(titles[1], bounds[1], 1);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				DadosRemodularizar dr = (DadosRemodularizar) element;
				return dr.getTipoPacote();
			}
		});

		viewer.refresh();

		final Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		viewer.setContentProvider(ArrayContentProvider.getInstance());

		viewer.setInput(SampleHandler.dadosNovaArq);
		getSite().setSelectionProvider(viewer);

		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = 2;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		viewer.getControl().setLayoutData(gridData);

		makeActions();
		hookContextMenu();
		contributeToActionBars();
		hookDoubleClickAction();

	}

	public void setFocus() {
		viewer.getControl().setFocus();
	}

	private TableViewerColumn createTableViewerColumn(String title, int bound, final int colNumber) {
		final TableViewerColumn viewerColumn = new TableViewerColumn(viewer, SWT.NONE);
		final TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(bound);
		column.setResizable(true);
		column.setMoveable(true);
		return viewerColumn;
	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});

		doubleClickAction = new Action() {
			public void run() {
				IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
				DadosRemodularizar dr = (DadosRemodularizar) selection.getFirstElement();
				if (dr.getTipoPacote().compareTo("view") == 0 || dr.getTipoPacote().compareTo("model") == 0
						|| dr.getTipoPacote().compareTo("controller") == 0) {
					MessageDialog.openInformation(HandlerUtil.getActiveShell(SampleHandler.event), "Informação",
							"Recomenda-se a criação do pacote " + dr.getTipoPacote() + " para mover a classe "
									+ dr.getClasse().getElementName() + " para ele.");
				} else {
					MessageDialog.openInformation(HandlerUtil.getActiveShell(SampleHandler.event), "Informação",
							"Recomenda-se que a classe " + dr.getClasse().getElementName()
									+ " permaneça no mesmo lugar");
				}

			}
		};
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				SampleView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(applyRemodularizationAction);
		manager.add(new Separator());
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(applyRemodularizationAction);
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(applyRemodularizationAction);

	}

	private void makeActions() {
		applyRemodularizationAction = new Action() {
			public void run() {

				try {
					createArchMVC(SampleHandler.javaProject);
					redistributeClasses(SampleHandler.javaProject);
				} catch (JavaModelException e) {
					e.printStackTrace();
				}

				JOptionPane.showMessageDialog(null, "Remodularização realizada com sucesso!");

				SampleHandler.dadosNovaArq.clear();

				IWorkbenchPage wp = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();

				// Find desired view :
				IViewPart myView = wp.findView("tp1.views.SampleView");

				// Hide the view :
				wp.hideView(myView);

				try {
					wp.showView("tp1.views.SampleView");
				} catch (PartInitException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		};

		applyRemodularizationAction.setToolTipText("Apply Remodularization");
		applyRemodularizationAction.setImageDescriptor(
				PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_FORWARD));
		applyRemodularizationAction.setEnabled(true);
	}

	private void redistributeClasses(IJavaProject javaProject) throws JavaModelException {

		MoveClass mc = new MoveClass();
		IPackageFragment[] packages = javaProject.getPackageFragments();
		for (int i = 0; i < packages.length; i++) {

			if (packages[i].getElementName().compareTo("model") == 0) {
				for (int j = 0; j < SampleHandler.dadosNovaArq.size(); j++) {
					if (SampleHandler.dadosNovaArq.get(j).getTipoPacote().compareTo("model") == 0) {
						mc.performMoveClassRefactoring(SampleHandler.dadosNovaArq.get(j).getClasse(), packages[i]);
					}
				}
			}

			if (packages[i].getElementName().compareTo("view") == 0) {
				for (int j = 0; j < SampleHandler.dadosNovaArq.size(); j++) {
					if (SampleHandler.dadosNovaArq.get(j).getTipoPacote().compareTo("view") == 0) {
						mc.performMoveClassRefactoring(SampleHandler.dadosNovaArq.get(j).getClasse(), packages[i]);
					}
				}
			}

			if (packages[i].getElementName().compareTo("controller") == 0) {
				for (int j = 0; j < SampleHandler.dadosNovaArq.size(); j++) {
					if (SampleHandler.dadosNovaArq.get(j).getTipoPacote().compareTo("controller") == 0) {
						mc.performMoveClassRefactoring(SampleHandler.dadosNovaArq.get(j).getClasse(), packages[i]);
					}
				}
			}
		}

	}

	private void createArchMVC(IJavaProject javaProject) throws JavaModelException {
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
}