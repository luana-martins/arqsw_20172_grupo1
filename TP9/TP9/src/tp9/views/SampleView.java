package tp9.views;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.part.FileEditorInput;

import tp9.handlers.SampleHandler;
import tp9.persistences.Dependencias;

public class SampleView extends ViewPart {

	public static final String ID = "tp1.views.SampleView";

	private TableViewer viewer;
	private Action doubleClickAction;
	private Action applyRemodularizationAction;

	public void createPartControl(Composite parent) {
		GridLayout layout = new GridLayout(5, false);
		parent.setLayout(layout);
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);

		String[] titles = { "Classe", "Pertence a", "Model", "View", "Controller" };
		int[] bounds = { 200, 100, 50, 50, 50 };

		TableViewerColumn col = createTableViewerColumn(titles[0], bounds[0], 0);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				Dependencias d = (Dependencias) element;
				return d.getClasse().getFullyQualifiedName();
			}
		});

		col = createTableViewerColumn(titles[1], bounds[1], 1);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				Dependencias d = (Dependencias) element;
				return d.getTipoClasse().toString();
			}
		});

		col = createTableViewerColumn(titles[2], bounds[2], 2);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				Dependencias d = (Dependencias) element;
				return Integer.toString(d.getMVCCounts()[0]);
			}
		});

		col = createTableViewerColumn(titles[3], bounds[3], 3);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				Dependencias d = (Dependencias) element;
				return Integer.toString(d.getMVCCounts()[1]);
			}
		});

		col = createTableViewerColumn(titles[4], bounds[4], 4);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				Dependencias d = (Dependencias) element;
				return Integer.toString(d.getMVCCounts()[2]);
			}
		});

		viewer.refresh();

		final Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		viewer.setContentProvider(ArrayContentProvider.getInstance());

		viewer.setInput(SampleHandler.classesDependencias);
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
				try {
					IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
					Dependencias m = (Dependencias) selection.getFirstElement();

					IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
					IFile file = (IFile) m.getClasse().getCompilationUnit().getResource();

					IEditorDescriptor desc = PlatformUI.getWorkbench().getEditorRegistry()
							.getDefaultEditor(file.getName());
					page.openEditor(new FileEditorInput(file), desc.getId());

				} catch (PartInitException e) {
					e.printStackTrace();
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

			}

		};

		applyRemodularizationAction.setToolTipText("Apply Remodularization");
		applyRemodularizationAction.setImageDescriptor(
				PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_FORWARD));
		applyRemodularizationAction.setEnabled(true);
	}

}