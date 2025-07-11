package com.mycompany.quanlydoituongdacbiet.view;

import com.mycompany.quanlydoituongdacbiet.controller.TransactionController;
import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import java.awt.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class SearchPanel extends JPanel {
    private final JTextField tfKeyword;
    private final JTextField tfMinAmount;
    private final JTextField tfMaxAmount;
    private final JComboBox<String> cbType;
    private final JDateChooser dcFromDate;
    private final JDateChooser dcToDate;
    private final JButton btnSearch;
    private TransactionPanel mainPanel;

    public SearchPanel(TransactionPanel mainPanel) {
        this.mainPanel = mainPanel;
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridLayout(3, 4, 10, 10));
        tfKeyword = new JTextField();
        tfMinAmount = new JTextField();
        tfMaxAmount = new JTextField();
        cbType = new JComboBox<>(new String[]{"Tất cả", "Thu nhập", "Chi tiêu"});
        dcFromDate = new JDateChooser();
        dcToDate = new JDateChooser();

        formPanel.add(new JLabel("Từ khóa mô tả:"));
        formPanel.add(tfKeyword);
        formPanel.add(new JLabel("Loại giao dịch:"));
        formPanel.add(cbType);
        formPanel.add(new JLabel("Số tiền từ:"));
        formPanel.add(tfMinAmount);
        formPanel.add(new JLabel("Đến:"));
        formPanel.add(tfMaxAmount);
        formPanel.add(new JLabel("Từ ngày:"));
        formPanel.add(dcFromDate);
        formPanel.add(new JLabel("Đến ngày:"));
        formPanel.add(dcToDate);

        btnSearch = new JButton();
        btnSearch.setIcon(Theme.getScaledIcon("/icons/search.png", 16, 16));
        btnSearch.setPreferredSize(new Dimension(50, 50));
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(btnSearch, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);
        btnSearch.addActionListener(e -> doSearch());
    }

    private void doSearch() {
        String keyword = tfKeyword.getText().trim();
        String type = cbType.getSelectedItem().toString();
        double minAmount = 0;
        double maxAmount = Double.MAX_VALUE;

        try {
            if (!tfMinAmount.getText().isEmpty()) {
                minAmount = Double.parseDouble(tfMinAmount.getText());
            }
            if (!tfMaxAmount.getText().isEmpty()) {
                maxAmount = Double.parseDouble(tfMaxAmount.getText());
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đúng định dạng số cho khoảng tiền.");
            return;
        }

        LocalDate fromDate = getFromDate();
        LocalDate toDate = getToDate();

        mainPanel.loadTableData(TransactionController.searchTransactions(keyword, type, minAmount, maxAmount, fromDate, toDate));
    }

    public LocalDate getFromDate() {
        Date date = dcFromDate.getDate();
        return (date != null) ? Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate() : null;
    }

    public LocalDate getToDate() {
        Date date = dcToDate.getDate();
        return (date != null) ? Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate() : null;
    }
}