package org.mm.ui.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import org.mm.core.MappingExpression;
import org.mm.core.MappingExpressionSet;
import org.mm.core.MappingExpressionSetFactory;
import org.mm.ui.dialog.CreateMappingExpressionDialog;
import org.mm.ui.dialog.MMDialogManager;
import org.mm.ui.model.MappingExpressionModel;

public class MappingBrowserView extends JPanel implements MMView
{
	private static final long serialVersionUID = 1L;

	private ApplicationView container;

	private JButton cmdAdd;
	private JButton cmdEdit;
	private JButton cmdDelete;
	private JButton cmdSave;

	private JTextField txtMappingPath;
	private JTable tblMappingExpression;

	private MappingExpressionTableModel tableModel;

	public MappingBrowserView(ApplicationView container)
	{
		this.container = container;
		
		tblMappingExpression = new JTable();
		tblMappingExpression.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblMappingExpression.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tblMappingExpression.setGridColor(Color.LIGHT_GRAY);
		tblMappingExpression.setDefaultRenderer(String.class, new MultiLineCellRenderer());

		JScrollPane scrMappingExpression = new JScrollPane(tblMappingExpression);
		
		setLayout(new BorderLayout());
		
		JPanel pnlTop = new JPanel(new BorderLayout());
		add(pnlTop, BorderLayout.NORTH);
		
		JPanel pnlCommandButton = new JPanel(new BorderLayout());
		pnlTop.add(pnlCommandButton, BorderLayout.EAST);
		
		cmdAdd = new JButton("Add");
		cmdAdd.addActionListener(new AddButtonActionListener());
		pnlCommandButton.add(cmdAdd, BorderLayout.WEST);
		
		cmdEdit = new JButton("Edit");
		cmdEdit.addActionListener(new EditButtonActionListener());
		pnlCommandButton.add(cmdEdit, BorderLayout.CENTER);
		
		cmdDelete = new JButton("Delete");
		cmdDelete.addActionListener(new DeleteButtonActionListener());
		pnlCommandButton.add(cmdDelete, BorderLayout.EAST);
		
		add(scrMappingExpression, BorderLayout.CENTER);
		
		JPanel pnlBottom = new JPanel(new BorderLayout());
		pnlBottom.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Mapping File"));
		add(pnlBottom, BorderLayout.SOUTH);

		txtMappingPath = new JTextField();
		txtMappingPath.setPreferredSize(new Dimension(80, 30));
		txtMappingPath.setEnabled(true);
		pnlBottom.add(txtMappingPath, BorderLayout.CENTER);

		JPanel pnlMappingOpenSave = new JPanel(new GridLayout(1, 4));
		pnlBottom.add(pnlMappingOpenSave, BorderLayout.EAST);

		JButton cmdOpen = new JButton("Open");
		cmdOpen.addActionListener(new OpenMappingAction());
		pnlMappingOpenSave.add(cmdOpen);

		cmdSave = new JButton("Save");
		cmdSave.addActionListener(new SaveMappingAction());
		cmdSave.setEnabled(false);
		pnlMappingOpenSave.add(cmdSave);

		JButton cmdSaveAs = new JButton("Save As...");
		cmdSaveAs.addActionListener(new SaveAsMappingAction());
		pnlMappingOpenSave.add(cmdSaveAs);
		
		validate();
	}

	@Override
	public void update()
	{
		tableModel = new MappingExpressionTableModel(getMappingExpressionsModel());
		tblMappingExpression.setModel(tableModel);
		setPreferredColumnSize();
		resizeColumnWidth();
		resizeColumnHeight();
	}

	public void updateTableModel(int selectedRow, String sheetName, String startColumn, String endColumn, String startRow, String endRow, String expression, String comment)
	{
		Vector<String> row = new Vector<>();
		row.add(0, sheetName);
		row.add(1, startColumn);
		row.add(2, endColumn);
		row.add(3, startRow);
		row.add(4, endRow);
		row.add(5, expression);
		row.add(6, comment);
		
		if (selectedRow != -1) { // there was row selected
			tableModel.removeRow(selectedRow);
		}
		tableModel.addRow(row);
		resizeColumnWidth();
		resizeColumnHeight();
	}

	private void setPreferredColumnSize()
	{
		final TableColumnModel columnModel = tblMappingExpression.getColumnModel();
		columnModel.getColumn(0).setPreferredWidth(80);
		columnModel.getColumn(1).setPreferredWidth(80);
		columnModel.getColumn(2).setPreferredWidth(80);
		columnModel.getColumn(3).setPreferredWidth(80);
		columnModel.getColumn(4).setPreferredWidth(80);
		columnModel.getColumn(5).setPreferredWidth(400);
		columnModel.getColumn(6).setPreferredWidth(280);
	}

	private void resizeColumnWidth()
	{
		final TableColumnModel columnModel = tblMappingExpression.getColumnModel();
		for (int column = 0; column < tblMappingExpression.getColumnCount(); column++) {
			if (column == 1 || column == 2 || column == 3 || column == 4) { // skip for certain columns
				continue;
			}
			int width = columnModel.getColumn(column).getPreferredWidth(); // min width
			for (int row = 0; row < tblMappingExpression.getRowCount(); row++) {
				TableCellRenderer renderer = tblMappingExpression.getCellRenderer(row, column);
				Component comp = tblMappingExpression.prepareRenderer(renderer, row, column);
				width = Math.max(comp.getPreferredSize().width + 5, width);
			}
			columnModel.getColumn(column).setPreferredWidth(width);
		}
	}

	private void resizeColumnHeight()
	{
		for (int column = 0; column < tblMappingExpression.getColumnCount(); column++) {
			if (column == 5) {
				int height = 0; // min height;
				for (int row = 0; row < tblMappingExpression.getRowCount(); row++) {
					Object value = tblMappingExpression.getModel().getValueAt(row, column);
					TableCellRenderer renderer = tblMappingExpression.getDefaultRenderer(String.class);
					Component comp = renderer.getTableCellRendererComponent(tblMappingExpression, value, false, false, row, column);
					height = Math.max(comp.getPreferredSize().height, height);
				}
				tblMappingExpression.setRowHeight(height);
			}
		}
	}

	public List<MappingExpression> getMappingExpressions()
	{
		return tableModel.getMappingExpressions();
	}

	private MappingExpressionModel getMappingExpressionsModel()
	{
		return container.getApplicationModel().getMappingExpressionsModel();
	}

	private MMDialogManager getApplicationDialogManager()
	{
		return container.getApplicationDialogManager();
	}

	class MappingExpressionTableModel extends DefaultTableModel
	{
		private static final long serialVersionUID = 1L;

		private final String[] COLUMN_NAMES = {
			"Sheet Name", "Start Column", "End Column", "Start Row", "End Row",
			"Mapping Expression", "Comment"
		};

		public MappingExpressionTableModel(final MappingExpressionModel model)
		{
			super();
			for (MappingExpression mapping : model.getExpressions()) {
				Vector<Object> row = new Vector<Object>();
				row.add(mapping.getSheetName());
				row.add(mapping.getStartColumn());
				row.add(mapping.getEndColumn());
				row.add(mapping.getStartRow());
				row.add(mapping.getEndRow());
				row.add(mapping.getExpressionString());
				row.add(mapping.getComment());
				addRow(row);
			}
		}

		@Override
		public String getColumnName(int column) // 0-based
		{
			return COLUMN_NAMES[column];
		}

		@Override
		public int getColumnCount()
		{
			return COLUMN_NAMES.length;
		}

		@Override
		public Class<?> getColumnClass(int columnIndex)
		{
			return String.class;
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex)
		{
			return false;
		}

		public List<MappingExpression> getMappingExpressions()
		{
			List<MappingExpression> mappings = new ArrayList<>();
			for (int row = 0; row < getRowCount(); row++) {
				@SuppressWarnings("unchecked")
				Vector<String> rowVector = (Vector<String>) getDataVector().elementAt(row);
				String sheetName = rowVector.get(0);
				String startColumn = rowVector.get(1);
				String endColumn = rowVector.get(2);
				String startRow = rowVector.get(3);
				String endRow = rowVector.get(4);
				String expression = rowVector.get(5);
				String comment = rowVector.get(6);
				mappings.add(new MappingExpression(sheetName, startColumn, endColumn, startRow, endRow, comment, expression));
			}
			return mappings;
		}

		public MappingExpressionSet getMappingExpressionSet()
		{
			MappingExpressionSet mappings = new MappingExpressionSet();
			for (MappingExpression mapping : getMappingExpressions()) {
				mappings.add(mapping);
			}
			return mappings;
		}
	}

	class MultiLineCellRenderer extends JTextArea implements TableCellRenderer
	{
		private static final long serialVersionUID = 1L;

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		{
			if (isSelected) {
				setForeground(table.getSelectionForeground());
				setBackground(table.getSelectionBackground());
			} else {
				setForeground(table.getForeground());
				setBackground(table.getBackground());
			}
			setFont(table.getFont());
			if (hasFocus) {
				setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
				if (table.isCellEditable(row, column)) {
					setForeground(UIManager.getColor("Table.focusCellForeground"));
					setBackground(UIManager.getColor("Table.focusCellBackground"));
				}
			} else {
				setBorder(new EmptyBorder(1, 2, 1, 2));
			}
			setText((value == null) ? "" : value.toString());
			return this;
		}
	}

	/*
	 * Action listener implementations for command buttons in MappingExpressionView panel
	 */

	class AddButtonActionListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			CreateMappingExpressionDialog dialog = new CreateMappingExpressionDialog(container);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		}
	}

	class EditButtonActionListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			int selectedRow = tblMappingExpression.getSelectedRow();
			if (selectedRow == -1) {
				getApplicationDialogManager().showMessageDialog(container, "No mapping expression was selected");
				return;
			}
			CreateMappingExpressionDialog dialog = new CreateMappingExpressionDialog(container);
			dialog.fillDialogFields(selectedRow,
					getValueAt(selectedRow, 0),
					getValueAt(selectedRow, 1),
					getValueAt(selectedRow, 2),
					getValueAt(selectedRow, 3),
					getValueAt(selectedRow, 4),
					getValueAt(selectedRow, 5),
					getValueAt(selectedRow, 6));
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		}

		private String getValueAt(int row, int column)
		{
			return (String) tableModel.getValueAt(row, column);
		}
	}

	class DeleteButtonActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			int selectedRow = tblMappingExpression.getSelectedRow();
			if (selectedRow == -1) {
				getApplicationDialogManager().showMessageDialog(container, "No mapping expression was selected");
				return;
			}
			int answer = getApplicationDialogManager().showConfirmDialog(container,
					"Delete", "Do you really want to delete the selected expression?");
			if (answer == JOptionPane.YES_OPTION) {
				tableModel.removeRow(selectedRow);
			}
		}
	}

	class OpenMappingAction implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			JFileChooser fileChooser = getApplicationDialogManager().createOpenFileChooser(
					"Open", "json", "MappingMaster DSL Mapping Expression (.json)");
			if (fileChooser.showOpenDialog(container) == JFileChooser.APPROVE_OPTION) {
				try {
					File file = fileChooser.getSelectedFile();
					String filename = file.getAbsolutePath();
					container.loadMappingDocument(filename);
					txtMappingPath.setText(filename);
					cmdSave.setEnabled(true);
				} catch (Exception ex) {
					getApplicationDialogManager().showErrorMessageDialog(container,
							"Error opening file: " + ex.getMessage());
				}
			}
		}
	}

	class SaveMappingAction implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			try {
				MappingExpressionSetFactory.saveMappingExpressionSetToDocument(
						txtMappingPath.getText(),
						tableModel.getMappingExpressionSet());
				container.updateMappingExpressionModel(tableModel.getMappingExpressionSet());
			}
			catch (IOException ex) {
				getApplicationDialogManager().showErrorMessageDialog(container,
						"Error saving file: " + ex.getMessage());
			}
		}
	}

	class SaveAsMappingAction implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			JFileChooser fileChooser = getApplicationDialogManager().createSaveFileChooser(
					"Save As", "json", "MappingMaster DSL Mapping Expression (.json)", true);
			if (fileChooser.showSaveDialog(container) == JFileChooser.APPROVE_OPTION) {
				try {
					File file = fileChooser.getSelectedFile();
					String filename = file.getAbsolutePath();
					MappingExpressionSetFactory.saveMappingExpressionSetToDocument(
							filename,
							tableModel.getMappingExpressionSet());
					container.updateMappingExpressionModel(tableModel.getMappingExpressionSet());
					txtMappingPath.setText(filename);
				} catch (Exception ex) {
					getApplicationDialogManager().showErrorMessageDialog(container,
							"Error saving file: " + ex.getMessage());
				}
			}
		}
	}
}
