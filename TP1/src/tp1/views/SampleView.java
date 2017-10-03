package tp1.views;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.*;

import tp1.handlers.SampleHandler;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.SWT;

public class SampleView extends ViewPart {

	public static final String ID = "tp1.views.SampleView";

	private TableViewer viewer;

	public SampleView() {
	}

	public void createPartControl(Composite parent) {

		GridLayout layout = new GridLayout(2, false);
		parent.setLayout(layout);
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);

		String[] titles = { "Método", "Classe Pertencente", "N° de Parâmetros", "N° de Linhas", "N° de Caracteres" };
		int[] bounds = { 200, 150, 120, 100, 100 };

		// Primeira coluna é para o nome do método
		TableViewerColumn col = createTableViewerColumn(titles[0], bounds[0], 0);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				IMethod m = (IMethod) element;
				return m.getElementName();
			}
		});

		// Segunda coluna é para nome da classe a qual o método pertence
		col = createTableViewerColumn(titles[1], bounds[1], 1);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				IMethod m = (IMethod) element;
				return m.getCompilationUnit().getParent().getElementName() + "."
						+ m.getDeclaringType().getElementName();
			}
		});

		// Terceira coluna é o número de parametros do método
		col = createTableViewerColumn(titles[2], bounds[2], 2);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				IMethod m = (IMethod) element;
				return Integer.toString(m.getNumberOfParameters());
			}
		});

		// Quarta coluna é o número de linhas do método
		col = createTableViewerColumn(titles[3], bounds[3], 3);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				IMethod m = null;
				Document doc = null;
				try {
					m = (IMethod) element;
					doc = new Document(m.getSource());
				} catch (JavaModelException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return Integer.toString(doc.getNumberOfLines());
			}
		});

		// Quinta coluna é o número de caracteres do método
		col = createTableViewerColumn(titles[4], bounds[4], 4);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				IMethod m = null;
				Document doc;
				int numChars = 0;
				try {
					m = (IMethod) element;
					doc = new Document(m.getSource());

					for (int i = 0; i < doc.getNumberOfLines(); i++) {
						numChars += doc.getLineLength(i);
					}

				} catch (JavaModelException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				return Integer.toString(numChars);
			}
		});

		viewer.refresh();

		final Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		viewer.setContentProvider(new ArrayContentProvider());

		// get the content for the viewer, setInput will call getElements in the
		// contentProvider
		viewer.setInput(SampleHandler.allMethods);
		// make the selection available to other views
		getSite().setSelectionProvider(viewer);
		// set the sorter for the table

		// define layout for the viewer
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = 2;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		viewer.getControl().setLayoutData(gridData);

	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
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
