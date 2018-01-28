package tp12.views;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
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
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.part.ViewPart;

import tp12.handlers.SampleHandler;
import tp12.persistences.Dependencies;
import tp12.persistences.Violation;
import tp12.refactorings.MoveClass;
import tp12.refactorings.MoveMethod;

public class SampleView extends ViewPart {

	public static final String ID = "tp1.views.SampleView";

	private TableViewer viewer;
	private Action doubleClickAction;
	private Action applyRemodularizationAction;

	public void createPartControl(Composite parent) {
		GridLayout layout = new GridLayout(3, false);
		parent.setLayout(layout);
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);

		String[] titles = { "Regra", "Recomendação", "Pacotes/Classes Envolvidas" };
		int[] bounds = {150, 150, 300};

		TableViewerColumn col = createTableViewerColumn(titles[0], bounds[0], 0);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				Violation v = (Violation) element;
				return v.getRule();
			}

		});

		col = createTableViewerColumn(titles[1], bounds[1], 1);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				Violation v = (Violation) element;
				return v.getRecommendation();
			}
		});
		
		col = createTableViewerColumn(titles[2], bounds[2], 2);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				Violation v = (Violation) element;
				if(v.getRuleType() < 2){
					return v.getPackageA().getElementName()+", "+v.getPackageB().getElementName();
				}
				else{
					return v.getClassA().getFullyQualifiedName()+", "+v.getClassB().getFullyQualifiedName();
				}
				
			}
		});

		viewer.refresh();

		final Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		viewer.setContentProvider(ArrayContentProvider.getInstance());

		viewer.setInput(SampleHandler.violations);
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
				Violation v = (Violation) selection.getFirstElement();
				
				//checar regra CAN
				if(v.getRuleType() == 0){
					String message = "O pacote "+v.getPackageA().getElementName()+" poderia depender do pacote "+v.getPackageB().getElementName()+" mas não depende."
							+ "\nVerifique alguma classe do pacote "+v.getPackageB().getElementName()+" que possa ser utilizada por alguma classe do pacote "+v.getPackageA().getElementName()+".";
					MessageDialog.openInformation(HandlerUtil.getActiveShell(SampleHandler.event),"Informação sobre Violação", message);

				}
				
				//checar regra MUST
				else if(v.getRuleType() == 1){
					String message = "O pacote "+v.getPackageA().getElementName()+" deveria depender do pacote "+v.getPackageB().getElementName()+" mas não depende."
							+ "\nVerifique alguma classe do pacote "+v.getPackageB().getElementName()+" que possa ser utilizada por alguma classe do pacote "+v.getPackageA().getElementName()+".";
					MessageDialog.openInformation(HandlerUtil.getActiveShell(SampleHandler.event),"Informação sobre Violação", message);
				}
				
				//checar regra CANNOT para dependencia de extends ou implements
				else if(v.getRuleType() == 2 && v.getDependencyFound().compareTo(Dependencies.EXTENDS_OR_IMPLEMENTS) == 0){
					String message = "O pacote "+v.getPackageA().getElementName()+" não pode depender do pacote "+v.getPackageB().getElementName()+" mas depende."
							+ "\nHá a opção de mover a classe "+v.getClassA().getFullyQualifiedName()+" para o pacote "+v.getPackageB().getElementName()+"."
									+ "\nDeseja mover?";
					boolean ok = MessageDialog.openConfirm(HandlerUtil.getActiveShell(SampleHandler.event),"Informação sobre Violação", message);
					
					if(ok){
						MoveClass.performMoveClassRefactoring(v.getClassA(), v.getPackageB());
					}
					
				}
				
				//checar regra CANNOT para atributos
				else if(v.getRuleType() == 2 && v.getDependencyFound().compareTo(Dependencies.ATTRIBUTE) == 0){
					String message = "O pacote "+v.getPackageA().getElementName()+" não pode depender do pacote "+v.getPackageB().getElementName()+" mas depende."
							+ "\nHá a opção de mover a classe "+v.getClassA().getFullyQualifiedName()+" para o pacote "+v.getPackageB().getElementName()+"."
									+ "\nDeseja mover?";
					boolean ok = MessageDialog.openConfirm(HandlerUtil.getActiveShell(SampleHandler.event),"Informação sobre Violação", message);
					
					if(ok){
						MoveClass.performMoveClassRefactoring(v.getClassA(), v.getPackageB());
					}	
				}
				
				//checar regra CANNOT para parametros de metodos
				else if(v.getRuleType() == 2 && v.getDependencyFound().compareTo(Dependencies.METHOD_PARAMETER) == 0){
					String message = "O pacote "+v.getPackageA().getElementName()+" não pode depender do pacote "+v.getPackageB().getElementName()+" mas depende."
							+ "\nHá a opção de mover o metodo "+v.getMethod().getElementName()+" para a classe "+v.getClassB().getElementName()+"."
									+ "\nDeseja mover?";
					boolean ok = MessageDialog.openConfirm(HandlerUtil.getActiveShell(SampleHandler.event),"Informação sobre Violação", message);
					
					if(ok){
						MoveMethod.performMoveMethod(v.getMethod(), v.getClassB().getElementName());
					}
				}
				
				//checar regra CANNOT para outras instancias
				else if(v.getRuleType() == 2 && v.getDependencyFound().compareTo(Dependencies.OTHER_INSTANCE) == 0){
					String message = "O pacote "+v.getPackageA().getElementName()+" não pode depender do pacote "+v.getPackageB().getElementName()+" mas depende."
							+ "\nHá a opção de mover a classe "+v.getClassA().getFullyQualifiedName()+" para o pacote "+v.getPackageB().getElementName()+"."
									+ "\nDeseja mover?";
					boolean ok = MessageDialog.openConfirm(HandlerUtil.getActiveShell(SampleHandler.event),"Informação sobre Violação", message);
					
					if(ok){
						MoveMethod.performMoveMethod(v.getMethod(), v.getClassB().getFullyQualifiedName());
					}				
				}
				
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