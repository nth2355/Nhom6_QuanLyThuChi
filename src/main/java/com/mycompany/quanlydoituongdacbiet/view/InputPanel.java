package com.mycompany.quanlydoituongdacbiet.view;

import com.mycompany.quanlydoituongdacbiet.controller.CategoryController;
import com.mycompany.quanlydoituongdacbiet.entity.Category;
import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Date;

public final class InputPanel extends JPanel {
    private final String transactionType; 
    private final JDateChooser dateChooser = new JDateChooser();
    private final JComboBox<String> cboCategory = new JComboBox<>();
    private final JButton btnAddCategory = new JButton();
    private final JTextField txtDescription = new JTextField();
    private final JFormattedTextField txtAmount = new JFormattedTextField(new java.text.DecimalFormat("#,##0"));

    public InputPanel(String type) {
        this.transactionType = type;
        setupUI();
        updateCategoryComboBox();
    }
    
    private void setupUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // --- Hàng 1: Ngày ---
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        add(new JLabel("Ngày:"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        dateChooser.setDate(new Date());
        ((JButton) dateChooser.getCalendarButton()).setIcon(Theme.getScaledIcon("/icons/calendar.png", 16, 16));
        add(dateChooser, gbc);

        // --- Hàng 2: Danh mục (SỬA LẠI gridy = 1) ---
        gbc.gridy = 1; 
        gbc.gridx = 0; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        add(new JLabel("Danh mục:"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        JPanel categoryPanel = new JPanel(new BorderLayout(5, 0));
        categoryPanel.add(cboCategory, BorderLayout.CENTER);
        btnAddCategory.setIcon(Theme.getScaledIcon("/icons/add.png", 16, 16));
        btnAddCategory.setToolTipText("Thêm danh mục mới");
        btnAddCategory.setPreferredSize(new Dimension(30, 30));
        categoryPanel.add(btnAddCategory, BorderLayout.EAST);
        add(categoryPanel, gbc);

        // --- Hàng 3: Mô tả (SỬA LẠI gridy = 2) ---
        gbc.gridy = 2; 
        gbc.gridx = 0; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        add(new JLabel("Mô tả:"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        add(txtDescription, gbc);
        
        // --- Hàng 4: Số tiền (SỬA LẠI gridy = 3)---
        gbc.gridy = 3; 
        gbc.gridx = 0; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        add(new JLabel("Số tiền:"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtAmount.setValue(0);
        add(txtAmount, gbc);
    }

    public void updateCategoryComboBox() {
        cboCategory.removeAllItems();
        List<Category> allCategories = CategoryController.getAllCategories();
        for (Category category : allCategories) {
            if (category.getType().equals(this.transactionType)) {
                cboCategory.addItem(category.getName());
            }
        }
    }
    
    public JDateChooser getDateChooser() { return dateChooser; }
    public JComboBox<String> getCategoryComboBox() { return cboCategory; }
    public JTextField getDescriptionField() { return txtDescription; }
    public JFormattedTextField getAmountField() { return txtAmount; }
    public JButton getAddCategoryButton() { return btnAddCategory; }
    
    public void resetFields() {
        dateChooser.setDate(new Date());
        txtDescription.setText("");
        txtAmount.setValue(0);
        if (cboCategory.getItemCount() > 0) {
            cboCategory.setSelectedIndex(0);
        }
    }

    public void setDate(LocalDate date) {
        if (date != null) {
            dateChooser.setDate(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        } else {
            dateChooser.setDate(null);
        }
    }

    public void setCategory(String category) {
        cboCategory.setSelectedItem(category);
    }

    public void setDescription(String description) {
        txtDescription.setText(description);
    }

    public void setAmount(double amount) {
        txtAmount.setValue(amount);
    }
}