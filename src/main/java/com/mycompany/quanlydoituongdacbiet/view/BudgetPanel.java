package com.mycompany.quanlydoituongdacbiet.view;

import com.mycompany.quanlydoituongdacbiet.controller.BudgetController;
import com.mycompany.quanlydoituongdacbiet.controller.CategoryController;
import com.mycompany.quanlydoituongdacbiet.entity.Budget;
import com.mycompany.quanlydoituongdacbiet.entity.Category;
import com.toedter.calendar.JMonthChooser;
import com.toedter.calendar.JYearChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.List;

public class BudgetPanel extends JPanel {

    private final JComboBox<String> cboCategory = new JComboBox<>();
    private final JMonthChooser monthChooser = new JMonthChooser();
    private final JYearChooser yearChooser = new JYearChooser();
    private final JSpinner spnAmount = new JSpinner(new SpinnerNumberModel(1000000.0, 0.0, Double.MAX_VALUE, 50000.0));
    private final JButton btnSaveBudget = new JButton("Lưu Ngân Sách");
    private final JTable table = new JTable();
    private DefaultTableModel tableModel;

    public BudgetPanel() {
        setLayout(new BorderLayout(10, 10));
        
        setupFormPanel();
        setupTablePanel();
        
        addListeners();
        loadExpenseCategories();
        loadBudgetsToTable();
    }
    
    private void setupFormPanel() {
        JPanel formWrapper = new JPanel(new BorderLayout(10,10));
        formWrapper.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Hàng 1: Danh mục
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        formPanel.add(new JLabel("Danh mục chi:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(cboCategory, gbc);

        // Hàng 2: Thời gian
        gbc.gridy = 1; gbc.gridx = 0; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Áp dụng cho:"), gbc);
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        datePanel.add(new JLabel("Tháng:"));
        datePanel.add(monthChooser);
        datePanel.add(new JLabel("Năm:"));
        datePanel.add(yearChooser);
        gbc.gridx = 1;
        formPanel.add(datePanel, gbc);

        // Hàng 3: Số tiền
        gbc.gridy = 2; gbc.gridx = 0; gbc.weightx = 0;
        formPanel.add(new JLabel("Số tiền ngân sách:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(spnAmount, gbc);

        // Panel chứa nút Lưu
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnSaveBudget.setIcon(Theme.getScaledIcon("/icons/save.png", 16, 16));
        buttonPanel.add(btnSaveBudget);

        formWrapper.add(formPanel, BorderLayout.CENTER);
        formWrapper.add(buttonPanel, BorderLayout.SOUTH);
        
        add(formWrapper, BorderLayout.NORTH);
    }
    
    private void setupTablePanel() {
        String[] columnNames = {"Tháng/Năm", "Danh mục", "Ngân sách"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table.setModel(tableModel);
        table.setRowHeight(25);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }
    
    private void addListeners() {
        btnSaveBudget.addActionListener(e -> saveBudget());

        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem deleteItem = new JMenuItem("Xóa ngân sách", Theme.getScaledIcon("/icons/delete.png", 16, 16));
        popupMenu.add(deleteItem);
        deleteItem.addActionListener(e -> deleteSelectedBudget());

        table.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    int row = table.rowAtPoint(e.getPoint());
                    if (row >= 0) {
                        table.setRowSelectionInterval(row, row);
                        popupMenu.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            }
        });
    }
    
    private void loadExpenseCategories() {
        cboCategory.removeAllItems();
        List<Category> allCategories = CategoryController.getAllCategories();
        for (Category category : allCategories) {
            if ("expense".equals(category.getType())) {
                cboCategory.addItem(category.getName());
            }
        }
    }
    
    public void loadBudgetsToTable() {
        tableModel.setRowCount(0);
        List<Budget> budgets = BudgetController.getAllBudgets();
        DecimalFormat formatter = new DecimalFormat("#,##0");
        
        budgets.sort((b1, b2) -> {
            int yearCompare = Integer.compare(b2.getYear(), b1.getYear());
            if (yearCompare != 0) return yearCompare;
            return Integer.compare(b2.getMonth(), b1.getMonth());
        });

        for (Budget b : budgets) {
            tableModel.addRow(new Object[]{
                String.format("%02d/%d", b.getMonth(), b.getYear()),
                b.getCategory(),
                formatter.format(b.getAmount())
            });
        }
    }
    
    private void saveBudget() {
        if (cboCategory.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn danh mục chi.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String category = cboCategory.getSelectedItem().toString();
        int month = monthChooser.getMonth() + 1;
        int year = yearChooser.getYear();
        double amount = (double) spnAmount.getValue();

        if (amount <= 0) {
            JOptionPane.showMessageDialog(this, "Số tiền ngân sách phải lớn hơn 0.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Budget newBudget = new Budget(category, amount, month, year);
        BudgetController.saveOrUpdateBudget(newBudget);
        
        JOptionPane.showMessageDialog(this, "Đã lưu ngân sách thành công!", "Thành Công", JOptionPane.INFORMATION_MESSAGE);
        loadBudgetsToTable();
    }
    
    private void deleteSelectedBudget() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) return;

        String monthYear = tableModel.getValueAt(selectedRow, 0).toString();
        String category = tableModel.getValueAt(selectedRow, 1).toString();
        
        String[] parts = monthYear.split("/");
        int month = Integer.parseInt(parts[0]);
        int year = Integer.parseInt(parts[1]);

        int confirm = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc muốn xóa ngân sách cho danh mục '" + category + "' tháng " + monthYear + "?",
            "Xác nhận xóa", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            Budget budgetToDelete = new Budget(category, 0, month, year);
            BudgetController.deleteBudget(budgetToDelete);
            loadBudgetsToTable();
        }
    }
    public void refreshData() {
        loadExpenseCategories();
        loadBudgetsToTable();
    }
}