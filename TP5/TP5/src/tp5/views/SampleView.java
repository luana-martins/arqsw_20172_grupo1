package tp5.views;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.part.*;

import tp5.handlers.DadosDoProjeto;
import tp5.handlers.SampleHandler;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jface.action.Action;
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

	public void createPartControl(Composite parent) {
		GridLayout layout = new GridLayout(2, false);
		parent.setLayout(layout);
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);

		String[] titles = { "Método", "Pacote/Classe"};
		int[] bounds = { 150, 200};

		TableViewerColumn col = createTableViewerColumn(titles[0], bounds[0], 0);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				MethodDeclaration m = (MethodDeclaration) element;
				return m.getName().toString();
			}
		});

		col = createTableViewerColumn(titles[1], bounds[1], 1);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				MethodDeclaration m = (MethodDeclaration) element;
				return m.getParent().toString();
			}
		});

		viewer.refresh();

		final Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		viewer.setContentProvider(ArrayContentProvider.getInstance());

		viewer.setInput(SampleHandler.arrayDados);
		getSite().setSelectionProvider(viewer);

		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = 2;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		viewer.getControl().setLayoutData(gridData);
		
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
			//	DadosDoProjeto dados = (DadosDoProjeto) selection.getFirstElement();
				
			//	MessageDialog.openInformation(HandlerUtil.getActiveShell(SampleHandler.event), "Informação", dados.getDeclaracaoDoMetodo());

				

			}
		};
	}
}