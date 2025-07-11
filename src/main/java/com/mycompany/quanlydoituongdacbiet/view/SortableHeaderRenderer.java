package com.mycompany.quanlydoituongdacbiet.view;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class SortableHeaderRenderer implements TableCellRenderer {

    private final TableCellRenderer defaultRenderer;

    // Hàm khởi tạo sẽ nhận renderer gốc
    public SortableHeaderRenderer(TableCellRenderer defaultRenderer) {
        this.defaultRenderer = defaultRenderer;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        
        // Luôn dùng renderer gốc để vẽ nền, chữ, đường viền...
        Component c = defaultRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (c instanceof JLabel) {
            JLabel label = (JLabel) c;
            label.setHorizontalTextPosition(JLabel.LEFT);
            label.setHorizontalAlignment(JLabel.CENTER);
            
            // Mặc định không có icon, trừ khi đang được sắp xếp
            label.setIcon(Theme.getScaledIcon("/icons/sort.png", 12, 12)); 

            RowSorter<?> sorter = table.getRowSorter();
            if (sorter != null) {
                java.util.List<? extends RowSorter.SortKey> sortKeys = sorter.getSortKeys();
                if (!sortKeys.isEmpty() && sortKeys.get(0).getColumn() == table.convertColumnIndexToModel(column)) {
                    // Nếu cột này đang được sắp xếp, hiển thị icon tương ứng
                    switch (sortKeys.get(0).getSortOrder()) {
                        case ASCENDING:
                            label.setIcon(Theme.getScaledIcon("/icons/sort-up.png", 12, 12));
                            break;
                        case DESCENDING:
                            label.setIcon(Theme.getScaledIcon("/icons/sort-down.png", 12, 12));
                            break;
                    }
                }
            }
            return label;
        }
        return c;
    }
}