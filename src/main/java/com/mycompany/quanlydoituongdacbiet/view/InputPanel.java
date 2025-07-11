package com.mycompany.quanlydoituongdacbiet.view;

import com.mycompany.quanlydoituongdacbiet.controller.CategoryController;
import com.mycompany.quanlydoituongdacbiet.entity.Category;
import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Date;


public final class InputPanel extends JPanel {
    private final String transactionType; // "income" hoặc "expense"
    private final JDateChooser dateChooser = new JDateChooser();
    private final JComboBox<String> cboCategory = new JComboBox<>();
    private final JButton btnAddCategory = new JButton("+");
    private final JTextField txtDescription = new JTextField();
    private final JFormattedTextField txtAmount = new JFormattedTextField(new java.text.DecimalFormat("#,##0"));

    public InputPanel(String type) {
        this.transactionType = type;
        setupUI();
        updateCategoryComboBox();
        btnAddCategory.setText(null); 
        btnAddCategory.setIcon(Theme.getScaledIcon("/icons/add.png", 24, 24));
        btnAddCategory.setPreferredSize(new Dimension(28, 28));
    }
    
    private void setupUI() {
        setLayout(new GridLayout(4, 2, 10, 20));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel categoryPanel = new JPanel(new BorderLayout(5, 0));
        categoryPanel.add(cboCategory, BorderLayout.CENTER);
        categoryPanel.add(btnAddCategory, BorderLayout.EAST);
        
        add(new JLabel("Ngày:"));
        dateChooser.setDate(new Date());
        add(dateChooser);

        add(new JLabel("Danh mục:"));
        add(categoryPanel);

        add(new JLabel("Mô tả:"));
        add(txtDescription);

        add(new JLabel("Số tiền:"));
        txtAmount.setValue(0);
        add(txtAmount);
        
        dateChooser.setPreferredSize(new Dimension(200, 30));
        cboCategory.setPreferredSize(new Dimension(200, 30));
        txtDescription.setPreferredSize(new Dimension(200, 30));
        txtAmount.setPreferredSize(new Dimension(200, 30));

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