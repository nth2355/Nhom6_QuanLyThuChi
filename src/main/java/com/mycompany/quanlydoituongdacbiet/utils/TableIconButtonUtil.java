package com.mycompany.quanlydoituongdacbiet.utils;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;
import java.util.function.Consumer;
import javax.swing.table.DefaultTableCellRenderer;

public class TableIconButtonUtil {

    /**
     * Thêm cột cuối có header là "menu.png" chứa 2 nút edit và delete.
     *
     * @param table JTable muốn thêm
     * @param onEdit callback khi bấm edit, truyền id (cột 0)
     * @param onDelete callback khi bấm delete, truyền id (cột 0)
     */
    public static void addEditDeleteButtons(JTable table, Consumer<Integer> onEdit, Consumer<Integer> onDelete) {
        TableColumn column = table.getColumnModel().getColumn(table.getColumnCount() - 1);
        column.setHeaderValue(new ImageIcon(TableIconButtonUtil.class.getResource("/icons/menu.png")));
        column.setCellRenderer(new ButtonsRenderer());
        column.setCellEditor(new ButtonsEditor(new JCheckBox(), table, onEdit, onDelete));
    }


    /** Cell Renderer cho 2 nút */
    private static class ButtonsRenderer extends JPanel implements TableCellRenderer {
        private final JButton editButton = new JButton(new ImageIcon(TableIconButtonUtil.class.getResource("/icons/edit.png")));
        private final JButton deleteButton = new JButton(new ImageIcon(TableIconButtonUtil.class.getResource("/icons/delete.png")));

        public ButtonsRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            editButton.setContentAreaFilled(false);
            editButton.setBorderPainted(false);
            deleteButton.setContentAreaFilled(false);
            deleteButton.setBorderPainted(false);
            add(editButton);
            add(deleteButton);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    /** Cell Editor cho 2 nút */
    private static class ButtonsEditor extends AbstractCellEditor implements TableCellEditor {
        private final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        private final JButton editButton;
        private final JButton deleteButton;

        private final JTable table;
        private final Consumer<Integer> onEdit;
        private final Consumer<Integer> onDelete;

        public ButtonsEditor(JCheckBox checkBox, JTable table, Consumer<Integer> onEdit, Consumer<Integer> onDelete) {
            this.table = table;
            this.onEdit = onEdit;
            this.onDelete = onDelete;

            editButton = new JButton(new ImageIcon(TableIconButtonUtil.class.getResource("/icons/edit.png")));
            deleteButton = new JButton(new ImageIcon(TableIconButtonUtil.class.getResource("/icons/delete.png")));

            styleButton(editButton);
            styleButton(deleteButton);

            editButton.addActionListener(e -> handleEdit());
            deleteButton.addActionListener(e -> handleDelete());

            panel.add(editButton);
            panel.add(deleteButton);
        }

        private void styleButton(JButton button) {
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);
            button.setFocusPainted(false);
        }

        private void handleEdit() {
            int row = table.getEditingRow();
            int id = Integer.parseInt(table.getValueAt(row, 0).toString());
            onEdit.accept(id);
            fireEditingStopped();
        }

        private void handleDelete() {
            int row = table.getEditingRow();
            int id = Integer.parseInt(table.getValueAt(row, 0).toString());
            onDelete.accept(id);
            fireEditingStopped();
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return null;
        }

        @Override
        public boolean isCellEditable(EventObject e) {
            return true;
        }
    }
    
    public static void centerTableCells(JTable table) {
    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
    centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
    for (int i = 0; i < table.getColumnCount(); i++) {
        table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
    }
}

}
