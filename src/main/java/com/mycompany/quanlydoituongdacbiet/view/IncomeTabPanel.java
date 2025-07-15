package com.mycompany.quanlydoituongdacbiet.view;

import com.mycompany.quanlydoituongdacbiet.controller.TransactionController;
import com.mycompany.quanlydoituongdacbiet.entity.Income;
import com.mycompany.quanlydoituongdacbiet.utils.ButtonModeController;
import com.mycompany.quanlydoituongdacbiet.utils.TableActionColumnUtils.*;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import javax.swing.table.DefaultTableCellRenderer;

public class IncomeTabPanel extends JPanel {

    private JDateChooser dateChooser;
    private JComboBox<String> cbCategory;
    private JTextField tfDescription;
    private JTextField tfAmount;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnSave;
    private JButton btnUpdate;
    private JButton btnCancel;
    private ButtonModeController buttonMode;


    // Danh mục có thể mở rộng
    private List<String> categoryList = new ArrayList<>();

    public IncomeTabPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ---- FORM ----
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Thêm Thu nhập"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 0: Ngày
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.25;
        formPanel.add(new JLabel("Ngày:"), gbc);

        gbc.gridx = 1; gbc.weightx = 0.75;
        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("yyyy-MM-dd");
        dateChooser.setPreferredSize(new Dimension(250, 30));
        formPanel.add(dateChooser, gbc);

        // Row 1: Danh mục
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.25;
        formPanel.add(new JLabel("Danh mục:"), gbc);

        gbc.gridx = 1; gbc.weightx = 0.75;
        cbCategory = new JComboBox<>(new String[]{"Lương", "Thưởng", "Đầu tư"});
        formPanel.add(cbCategory, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        JButton btnAddCategory = new JButton(Theme.getScaledIcon("/icons/add.png", 16, 16));
        btnAddCategory.setToolTipText("Thêm danh mục");
        btnAddCategory.setBackground(Theme.ACTION_BUTTON_COLOR); // xanh đậm
        btnAddCategory.setForeground(Color.WHITE);
        btnAddCategory.setFocusPainted(false);
        btnAddCategory.addActionListener(e -> handleAddCategory());
        formPanel.add(btnAddCategory, gbc);

        // Row 2: Mô tả
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.25;
        formPanel.add(new JLabel("Mô tả:"), gbc);

        gbc.gridx = 1; gbc.gridwidth = 2; gbc.weightx = 0.75;
        tfDescription = new JTextField(20);
        formPanel.add(tfDescription, gbc);
        gbc.gridwidth = 1;

        // Row 3: Số tiền
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.25;
        formPanel.add(new JLabel("Số tiền:"), gbc);

        gbc.gridx = 1; gbc.gridwidth = 2; gbc.weightx = 0.75;
        tfAmount = new JTextField(20);
        formPanel.add(tfAmount, gbc);
        gbc.gridwidth = 1;

        // Row 4: Nút lưu
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton btnSave = new JButton("Lưu giao dịch", Theme.getScaledIcon("/icons/save.png", 16, 16));
        btnSave.setBackground(Theme.ACTION_BUTTON_COLOR);
        btnSave.setForeground(Color.WHITE);
        btnSave.setFocusPainted(false);
        btnSave.addActionListener(this::handleSave);
        formPanel.add(btnSave, gbc);

        add(formPanel, BorderLayout.NORTH);

        // ---- TABLE ----
        String[] columnNames = {"ID", "Ngày", "Danh mục", "Mô tả", "Số tiền", ""};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(35);
        table.getTableHeader().setReorderingAllowed(false);

        // Ẩn cột ID
        TableColumnModel tcm = table.getColumnModel();
        tcm.getColumn(0).setMinWidth(0);
        tcm.getColumn(0).setMaxWidth(0);
        tcm.getColumn(0).setPreferredWidth(0);

        // Center-align các cột thông tin
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 1; i <= 4; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Action Column
        Icon headerIcon = Theme.getScaledIcon("/icons/menu.png", 16, 16);
        tcm.getColumn(5).setHeaderRenderer(new IconHeaderRenderer(headerIcon));
        tcm.getColumn(5).setCellRenderer(new ActionButtonRenderer());
        tcm.getColumn(5).setCellEditor(new ActionButtonEditor(
            e -> handleEdit(table.getEditingRow()),
            e -> handleDelete(table.getEditingRow())
        ));

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        loadTableData();
    }

    private void handleAddCategory() {
        String newCategory = JOptionPane.showInputDialog(this, "Nhập danh mục mới:");
        if (newCategory != null && !newCategory.trim().isEmpty()) {
            cbCategory.addItem(newCategory.trim());
            cbCategory.setSelectedItem(newCategory.trim());
        }
    }

    private void handleSave(ActionEvent e) {
        try {
            Date selectedDate = dateChooser.getDate();
            if (selectedDate == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày!");
                return;
            }
            LocalDate date = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            String category = (String) cbCategory.getSelectedItem();
            String description = tfDescription.getText();
            double amount = Double.parseDouble(tfAmount.getText());

            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Số tiền phải lớn hơn 0!");
                return;
            }

            Income income = new Income(date, category, description, amount);
            TransactionController.saveTransaction(income);

            JOptionPane.showMessageDialog(this, "Lưu giao dịch thành công!");

            clearForm();
            loadTableData();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Số tiền không hợp lệ!");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi lưu giao dịch!");
        }
    }

    private void clearForm() {
        dateChooser.setDate(null);
        cbCategory.setSelectedIndex(0);
        tfDescription.setText("");
        tfAmount.setText("");
    }

    private void handleEdit(int row) {
        dateChooser.setDate(Date.from(
                ((LocalDate) tableModel.getValueAt(row, 1)).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        cbCategory.setSelectedItem(tableModel.getValueAt(row, 2));
        tfDescription.setText((String) tableModel.getValueAt(row, 3));
        String amountStr = tableModel.getValueAt(row, 4).toString().replaceAll("[^\\d]", "");
        tfAmount.setText(amountStr);
    }

    private void handleDelete(int row) {
        int id = (int) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Xóa giao dịch này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            TransactionController.deleteTransaction(id);
            loadTableData();
        }
    }

    public void loadTableData() {
        List<Income> incomes = TransactionController.getAllTransactions().stream()
                .filter(t -> t instanceof Income)
                .map(t -> (Income) t)
                .toList();

        loadTableData(incomes);
    }

    public void loadTableData(List<Income> incomes) {
        tableModel.setRowCount(0);
        for (Income income : incomes) {
            tableModel.addRow(new Object[]{
                    income.getId(),
                    income.getDate(),
                    income.getCategory(),
                    income.getDescription(),
                    "<html><font color='green'>+ " + String.format("%,.0f", income.getAmount()) + "</font></html>",
                    ""
            });
        }
    }
}
