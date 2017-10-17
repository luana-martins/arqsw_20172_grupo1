package tp1.views;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.*;

import tp1.handlers.DadosDoProjeto;
import tp1.handlers.SampleHandler;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.SWT;

public class SampleView extends ViewPart {

	public static final String ID = "tp1.views.SampleView";

	private TableViewer viewer;

	public void createPartControl(Composite parent) {

		GridLayout layout = new GridLayout(2, false);
		parent.setLayout(layout);
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);

		String[] titles = { "Classe Pertencente", "Metodo", "Num. de Linhas", "Porcentagem" };
		int[] bounds = { 150, 200, 120, 100 };

		TableViewerColumn col = createTableViewerColumn(titles[0], bounds[0], 0);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				DadosDoProjeto m = (DadosDoProjeto) element;
				return m.getNomeDaClasse();
			}
		});

		col = createTableViewerColumn(titles[1], bounds[1], 1);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				DadosDoProjeto m = (DadosDoProjeto) element;
				return m.getNomeDoMetodo();
			}
		});

		col = createTableViewerColumn(titles[2], bounds[2], 2);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				DadosDoProjeto m = (DadosDoProjeto) element;
				return ""+m.getQtdeDeLinhas();
			}
		});

		col = createTableViewerColumn(titles[3], bounds[3], 3);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				DadosDoProjeto m = (DadosDoProjeto) element;
				return ""+m.getPorcentagem()+"%";
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
}