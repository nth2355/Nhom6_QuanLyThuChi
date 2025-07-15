package com.mycompany.quanlydoituongdacbiet.utils;

import com.mycompany.quanlydoituongdacbiet.view.Theme;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Utils for adding an action column with edit/delete buttons to JTable
 */
public class TableActionColumnUtils {

    /**
     * Header renderer for an icon header
     */
    public static class IconHeaderRenderer extends DefaultTableCellRenderer {
        private final Icon icon;

        public IconHeaderRenderer(Icon icon) {
            this.icon = icon;
            setHorizontalAlignment(SwingConstants.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setIcon(icon);
            setText("");
            return this;
        }
    }

    /**
     * Cell renderer with edit + delete icons
     */
    public static class ActionButtonRenderer extends JPanel implements TableCellRenderer {
        private final Icon editIcon;
        private final Icon deleteIcon;

        public ActionButtonRenderer() {
            this.editIcon = Theme.getScaledIcon("/icons/edit.png", 16, 16);
            this.deleteIcon = Theme.getScaledIcon("/icons/delete.png", 16, 16);

            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            setOpaque(true);

            JButton editButton = createButton(editIcon);
            JButton deleteButton = createButton(deleteIcon);

            add(editButton);
            add(deleteButton);
        }

        private JButton createButton(Icon icon) {
            JButton btn = new JButton(icon);
            btn.setBorderPainted(false);
            btn.setContentAreaFilled(false);
            btn.setFocusable(false);
            return btn;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setBackground(table.getSelectionBackground());
            } else {
                setBackground(table.getBackground());
            }
            return this;
        }
    }

    /**
     * Cell editor to handle click on edit/delete
     */
    public static class ActionButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private final JPanel panel;
        private final JButton editButton;
        private final JButton deleteButton;

        public ActionButtonEditor(ActionListener onEdit, ActionListener onDelete) {
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            editButton = new JButton(Theme.getScaledIcon("/icons/edit.png", 16, 16));
            deleteButton = new JButton(Theme.getScaledIcon("/icons/delete.png", 16, 16));

            styleButton(editButton);
            styleButton(deleteButton);

            panel.add(editButton);
            panel.add(deleteButton);

            editButton.addActionListener(onEdit);
            deleteButton.addActionListener(onDelete);
        }

        private void styleButton(JButton btn) {
            btn.setBorderPainted(false);
            btn.setContentAreaFilled(false);
            btn.setFocusable(false);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return null;
        }
    }
}
