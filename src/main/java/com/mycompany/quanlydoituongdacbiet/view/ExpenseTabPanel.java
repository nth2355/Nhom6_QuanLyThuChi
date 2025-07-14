package com.mycompany.quanlydoituongdacbiet.view;

import com.mycompany.quanlydoituongdacbiet.entity.Expense;
import com.mycompany.quanlydoituongdacbiet.entity.Transaction;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class ExpenseTabPanel extends JPanel {

    private JFormattedTextField tfDate;
    private JComboBox<String> cbCategory;
    private JTextField tfDescription;
    private JTextField tfAmount;
    private JButton btnSave;

    private JTable table;
    private DefaultTableModel tableModel;

    public ExpenseTabPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- FORM ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblDate = new JLabel("Ngày:");
        tfDate = new JFormattedTextField(LocalDate.now());
        tfDate.setColumns(10);

        JLabel lblCategory = new JLabel("Danh mục:");
        cbCategory = new JComboBox<>(new String[]{"Ăn uống", "Di chuyển", "Hóa đơn", "Mua sắm", "Khác"});

        JLabel lblDescription = new JLabel("Mô tả:");
        tfDescription = new JTextField(15);

        JLabel lblAmount = new JLabel("Số tiền:");
        tfAmount = new JTextField(10);

        btnSave = new JButton("Lưu giao dịch");

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(lblDate, gbc);
        gbc.gridx = 1;
        formPanel.add(tfDate, gbc);

        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(lblCategory, gbc);
        gbc.gridx = 1;
        formPanel.add(cbCategory, gbc);

        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(lblDescription, gbc);
        gbc.gridx = 1;
        formPanel.add(tfDescription, gbc);

        gbc.gridx = 0; gbc.gridy++;
        formPanel.add(lblAmount, gbc);
        gbc.gridx = 1;
        formPanel.add(tfAmount, gbc);

        gbc.gridx = 1; gbc.gridy++;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(btnSave, gbc);

        // --- TABLE ---
        tableModel = new DefaultTableModel(new Object[]{"Ngày", "Danh mục", "Mô tả", "Số tiền"}, 0);
        table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);

        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // --- Button Action ---
        btnSave.addActionListener(e -> saveExpense());
    }

    private void saveExpense() {
        try {
            LocalDate date = LocalDate.parse(tfDate.getText().trim());
            String category = cbCategory.getSelectedItem().toString();
            String description = tfDescription.getText().trim();
            double amount = Double.parseDouble(tfAmount.getText().trim());

            Expense expense = new Expense(date, category, description, amount);
            addExpenseToTable(expense);

            clearForm();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đúng định dạng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addExpenseToTable(Expense expense) {
        tableModel.addRow(new Object[]{
                expense.getDate(),
                expense.getCategory(),
                expense.getDescription(),
                String.format("%,.0f", expense.getAmount())
        });
    }

    private void clearForm() {
        tfDate.setValue(LocalDate.now());
        cbCategory.setSelectedIndex(0);
        tfDescription.setText("");
        tfAmount.setText("");
    }

    public void loadTableData(List<Transaction> expenses) {
        tableModel.setRowCount(0);
        for (Transaction t : expenses) {
            tableModel.addRow(new Object[]{
                    t.getDate(),
                    t.getCategory(),
                    t.getDescription(),
                    String.format("%,.0f", t.getAmount())
            });
        }
    }
}
