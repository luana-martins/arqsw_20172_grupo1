package tp11.views;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;

import tp11.handlers.SampleHandler;
import tp11.persistences.DependenciasPacote;
import tp11.regras.Regras;

public class SampleView extends ViewPart {

	public static final String ID = "tp1.views.SampleView";
	private TableViewer viewer;
	private Action applyRemodularizationAction;

	public void createPartControl(Composite parent) {
		GridLayout layout = new GridLayout(4, false);
		parent.setLayout(layout);
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		String[] titles = SampleHandler.pac.toArray(new String[SampleHandler.pac.size()]);

		TableViewerColumn col = createTableViewerColumn("", 150, 0);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				DependenciasPacote r = (DependenciasPacote) element;
				return r.getNome();
			}
		});

		for (int i = 0; i < titles.length; i++) {
			TableViewerColumn col2 = createTableViewerColumn(titles[i], 100, i);
			col2.setLabelProvider(new ColumnLabelProvider() {
				int i = 0;
				@Override
				public String getText(Object element) {
					DependenciasPacote r = (DependenciasPacote) element;
					if(r.getFromArray()==-1) {
						return "-";
					}
					return Integer.toString(r.getFromArray());

				}
				
				@Override
				public Color getForeground(Object element) {
					DependenciasPacote r = (DependenciasPacote) element;
					Regras regras = new Regras();
					int a = regras.ciclo(i, r.getContador());
					if (a == 1) {
						return Display.getCurrent().getSystemColor(SWT.COLOR_MAGENTA);
					} else {
						return Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
					}
				}

				@Override
				public Color getBackground(Object element) {
					DependenciasPacote r = (DependenciasPacote) element;
					Regras regras = new Regras();
					int violacao = regras.getMatriz(i, r.getContador(), r.getFromArray());
					i++;

					if (violacao == 4) {
						Integer.toString(r.getNextFromArray());
						return Display.getCurrent().getSystemColor(SWT.COLOR_RED);
					} else if (violacao == 2) {
						Integer.toString(r.getNextFromArray());
						return Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW);
					} else if (violacao == 3) {
						Integer.toString(r.getNextFromArray());
						return Display.getCurrent().getSystemColor(SWT.COLOR_GRAY);
					} else {
						Integer.toString(r.getNextFromArray());
						return Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
					}
				}
			});

		}

		viewer.refresh();
		final Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		viewer.setContentProvider(ArrayContentProvider.getInstance());

		viewer.setInput(SampleHandler.dependencias);
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