package com.mycompany.quanlydoituongdacbiet.view;

import com.mycompany.quanlydoituongdacbiet.controller.CategoryController;
import com.mycompany.quanlydoituongdacbiet.controller.TransactionController;
import com.mycompany.quanlydoituongdacbiet.entity.Category;
import com.mycompany.quanlydoituongdacbiet.entity.Expense;
import com.mycompany.quanlydoituongdacbiet.entity.Income;
import com.mycompany.quanlydoituongdacbiet.entity.Transaction;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class TransactionPanel extends JPanel {

    private JTabbedPane tabbedPane;
    private InputPanel incomeInputPanel;
    private InputPanel expenseInputPanel;
    private SearchPanel searchPanel;
    private final JButton btnSave = new JButton("Lưu");
    private final JButton btnClear = new JButton("Làm mới");
    private final JTable table = new JTable();
    private DefaultTableModel tableModel;
    private BudgetPanel budgetPanel; 

    private JLabel lblTotalIncome;
    private JLabel lblTotalExpense;
    private JLabel lblBalance;
    
    private JPanel topPanel;
    private JScrollPane tableScrollPane;
    
    //--- Các biến trạng thái ---
    private Transaction currentEditingTransaction = null;
    private List<Transaction> currentTransactions;


public TransactionPanel() {
    // --- BƯỚC 1: Đảm bảo layout chính là BorderLayout ---
    setLayout(new BorderLayout(0, 10));
    setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    // Gọi các hàm helper để khởi tạo component
    setupTopPanel();
    setupTablePanel();

    // KHU VỰC 1: FORM NHẬP LIỆU
    JPanel formWrapperPanel = new JPanel(new BorderLayout());
    formWrapperPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Thao tác", TitledBorder.LEFT, TitledBorder.TOP, Theme.BOLD_FONT, Theme.TEXT_COLOR
    ));
    formWrapperPanel.add(topPanel, BorderLayout.CENTER);

    // KHU VỰC 2: BẢNG DỮ LIỆU
    JPanel tableWrapperPanel = new JPanel(new BorderLayout());
    tableWrapperPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Danh sách giao dịch", TitledBorder.LEFT, TitledBorder.TOP, Theme.BOLD_FONT, Theme.TEXT_COLOR
    ));
    tableWrapperPanel.add(tableScrollPane, BorderLayout.CENTER);

    // Panel chứa các thông tin tổng kết
    JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 5));
    lblTotalIncome = new JLabel("Tổng thu: 0");
    lblTotalExpense = new JLabel("Tổng chi: 0");
    lblBalance = new JLabel("Số dư: 0");

    lblTotalIncome.setFont(Theme.BOLD_FONT);
    lblTotalExpense.setFont(Theme.BOLD_FONT);
    lblBalance.setFont(Theme.BOLD_FONT);
    
    summaryPanel.add(lblTotalIncome);
    summaryPanel.add(lblTotalExpense);
    summaryPanel.add(lblBalance);
    
    tableWrapperPanel.add(summaryPanel, BorderLayout.SOUTH);

    // --- BƯỚC 2: Thêm các khu vực vào đúng vị trí ---
    // Đặt form ở trên (NORTH)
    add(formWrapperPanel, BorderLayout.NORTH);
    // Đặt bảng ở giữa (CENTER), nó sẽ chiếm hết không gian còn lại
    add(tableWrapperPanel, BorderLayout.CENTER);

    // Gán sự kiện và tải dữ liệu
    addListeners();
    loadTableData();
}

    private void setupTopPanel() {
        topPanel = new JPanel(new BorderLayout());

        tabbedPane = new JTabbedPane();
        incomeInputPanel = new InputPanel("income");
        expenseInputPanel = new InputPanel("expense");
        searchPanel = new SearchPanel(this);

        tabbedPane.addTab("Quản Lý Thu", Theme.getScaledIcon("/icons/income.png", 16, 16), incomeInputPanel);
        tabbedPane.addTab("Quản Lý Chi", Theme.getScaledIcon("/icons/expense.png", 16, 16), expenseInputPanel);
        tabbedPane.addTab("Ngân Sách", Theme.getScaledIcon("/icons/budget.png", 16, 16), budgetPanel);
        tabbedPane.addTab("Tìm kiếm", Theme.getScaledIcon("/icons/search.png", 16, 16), searchPanel);

        topPanel.add(tabbedPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        btnClear.setIcon(Theme.getScaledIcon("/icons/clear.png", 16, 16));
        btnClear.setToolTipText("Xóa trắng form và hủy chế độ sửa");
        btnClear.setVisible(false);
        buttonPanel.add(btnClear);

        btnSave.setIcon(Theme.getScaledIcon("/icons/save.png", 16, 16));
        btnSave.setBackground(Theme.ACTION_BUTTON_COLOR);
        btnSave.setForeground(Color.WHITE);
        btnSave.setToolTipText("Lưu hoặc Cập nhật giao dịch");
        buttonPanel.add(btnSave);
        
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void setupTablePanel() {
        String[] columnNames = {"ID", "Ngày", "Loại", "Danh Mục", "Mô Tả", "Số Tiền"};
        
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                switch (columnIndex) {
                    case 0: return Integer.class;
                    case 1: return LocalDate.class;
                    case 5: return Double.class;
                    default: return String.class;
                }
            }
        };
        table.setModel(tableModel);
        table.setAutoCreateRowSorter(true);
        hideColumn(0);
        table.setRowHeight(30);
        table.setShowGrid(true);
        table.setGridColor(new Color(220, 220, 220));

        table.setDefaultRenderer(LocalDate.class, new DefaultTableCellRenderer() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (value instanceof LocalDate) { setText(formatter.format((LocalDate) value)); }
                setHorizontalAlignment(JLabel.CENTER);
                return c;
            }
        });

        table.setDefaultRenderer(Double.class, new AmountRenderer());
        
        tableScrollPane = new JScrollPane(table);
    }

    private void addListeners() {
        btnSave.addActionListener(e -> saveOrUpdateTransaction());
        btnClear.addActionListener(e -> clearFormAndState());

        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem editItem = new JMenuItem("Sửa giao dịch", Theme.getScaledIcon("/icons/edit.png", 16, 16));
        JMenuItem deleteItem = new JMenuItem("Xóa giao dịch", Theme.getScaledIcon("/icons/delete.png", 16, 16));
        popupMenu.add(editItem);
        popupMenu.add(deleteItem);

        editItem.addActionListener(e -> prepareToEditTransaction());
        deleteItem.addActionListener(e -> deleteSelectedTransaction());
        
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { prepareToEditTransaction(); }
            }
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    int row = table.rowAtPoint(e.getPoint());
                    if (row >= 0 && row < table.getRowCount()) {
                        table.setRowSelectionInterval(row, row);
                        popupMenu.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            }
        });

        tabbedPane.addChangeListener(e -> {
            int selectedIndex = tabbedPane.getSelectedIndex();
            boolean isEditTab = selectedIndex == 0 || selectedIndex == 1;
            btnSave.setVisible(isEditTab);
            
            if (isEditTab) {
                clearFormAndState();
                loadTableData();
            } else {
                btnClear.setVisible(false);
            }
        });
        
        incomeInputPanel.getAddCategoryButton().addActionListener(e -> addNewCategory("income"));
        expenseInputPanel.getAddCategoryButton().addActionListener(e -> addNewCategory("expense"));
    }

    private void prepareToEditTransaction() {
        int selectedRowInView = table.getSelectedRow();
        if (selectedRowInView == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một giao dịch để sửa.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int modelRow = table.convertRowIndexToModel(selectedRowInView);
        int transactionId = (int) tableModel.getValueAt(modelRow, 0);

        Optional<Transaction> transactionOpt = currentTransactions.stream()
                .filter(t -> t.getId() == transactionId)
                .findFirst();

        if (transactionOpt.isPresent()) {
            currentEditingTransaction = transactionOpt.get();
            InputPanel targetPanel;
            if (currentEditingTransaction instanceof Income) {
                tabbedPane.setSelectedIndex(0);
                targetPanel = incomeInputPanel;
            } else {
                tabbedPane.setSelectedIndex(1);
                targetPanel = expenseInputPanel;
            }
            targetPanel.setDate(currentEditingTransaction.getDate());
            targetPanel.setCategory(currentEditingTransaction.getCategory());
            targetPanel.setDescription(currentEditingTransaction.getDescription());
            targetPanel.setAmount(currentEditingTransaction.getAmount());
            btnSave.setText("Cập nhật");
            btnClear.setVisible(true);
        }
    }

    private void clearFormAndState() {
        currentEditingTransaction = null;
        incomeInputPanel.resetFields();
        expenseInputPanel.resetFields();
        btnSave.setText("Lưu");
        btnClear.setVisible(false);
    }

    private void saveOrUpdateTransaction() {
        int selectedIndex = tabbedPane.getSelectedIndex();
        InputPanel currentPanel = (selectedIndex == 0) ? incomeInputPanel : expenseInputPanel;
        
        Date selectedDate = currentPanel.getDateChooser().getDate();
        if (selectedDate == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        LocalDate date = Instant.ofEpochMilli(selectedDate.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
        
        if (currentPanel.getCategoryComboBox().getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Không có danh mục nào.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String category = currentPanel.getCategoryComboBox().getSelectedItem().toString();
        String description = currentPanel.getDescriptionField().getText().trim();
        double amount = ((Number) currentPanel.getAmountField().getValue()).doubleValue();
        
        if (description.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mô tả.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (amount <= 0) {
            JOptionPane.showMessageDialog(this, "Số tiền phải là một số dương.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (currentEditingTransaction == null) {
            String type = (selectedIndex == 0) ? "income" : "expense";
            Transaction newTransaction = type.equals("income")
                    ? new Income(0, date, category, description, amount)
                    : new Expense(0, date, category, description, amount);
            TransactionController.saveTransaction(newTransaction);
            JOptionPane.showMessageDialog(this, "Đã lưu giao dịch mới thành công!", "Thành Công", JOptionPane.INFORMATION_MESSAGE);
        } else {
            currentEditingTransaction.setDate(date);
            currentEditingTransaction.setCategory(category);
            currentEditingTransaction.setDescription(description);
            currentEditingTransaction.setAmount(amount);
            
            TransactionController.updateTransaction(currentEditingTransaction);
            JOptionPane.showMessageDialog(this, "Đã cập nhật giao dịch thành công!", "Thành Công", JOptionPane.INFORMATION_MESSAGE);
        }

        clearFormAndState();
        loadTableData();
    }
    
    public void loadTableData() {
        this.currentTransactions = TransactionController.getAllTransactions();
        loadTableData(this.currentTransactions);
    }
    
    public void loadTableData(List<Transaction> transactions) {
        this.currentTransactions = transactions;
        tableModel.setRowCount(0);
        for (Transaction trans : transactions) {
            tableModel.addRow(new Object[]{
                    trans.getId(),
                    trans.getDate(),
                    trans.getType(),
                    trans.getCategory(),
                    trans.getDescription(),
                    trans.getAmount()
            });
        }
        updateSummary();
    }

    private void updateSummary() {
        double totalIncome = 0;
        double totalExpense = 0;
        for (Transaction t : currentTransactions) {
            if (t instanceof Income) {
                totalIncome += t.getAmount();
            } else {
                totalExpense += t.getAmount();
            }
        }
        DecimalFormat moneyFormatter = new DecimalFormat("#,##0");
        double balance = totalIncome - totalExpense;
        lblTotalIncome.setText("Tổng thu: " + moneyFormatter.format(totalIncome));
        lblTotalExpense.setText("Tổng chi: " + moneyFormatter.format(totalExpense));
        lblBalance.setText("Số dư: " + moneyFormatter.format(balance));
        lblTotalIncome.setForeground(new Color(0, 128, 0));
        lblTotalExpense.setForeground(Color.RED);
        lblBalance.setForeground(balance >= 0 ? new Color(0, 128, 0) : Color.RED);
    }

    private void deleteSelectedTransaction() {
        int selectedRowInView = table.getSelectedRow();
        if (selectedRowInView == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một giao dịch để xóa.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int modelRow = table.convertRowIndexToModel(selectedRowInView);
        int transactionId = (int) tableModel.getValueAt(modelRow, 0);

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa giao dịch này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            TransactionController.deleteTransaction(transactionId);
            clearFormAndState();
            loadTableData();
        }
    }

    private void addNewCategory(String type) {
        String newCategoryName = JOptionPane.showInputDialog(this, "Nhập tên danh mục mới:", "Thêm Danh Mục", JOptionPane.PLAIN_MESSAGE);
        if (newCategoryName != null && !newCategoryName.trim().isEmpty()) {
            CategoryController.addCategory(new Category(newCategoryName.trim(), type));
            incomeInputPanel.updateCategoryComboBox();
            expenseInputPanel.updateCategoryComboBox();
            if (type.equals("income")) {
                incomeInputPanel.getCategoryComboBox().setSelectedItem(newCategoryName);
            } else {
                expenseInputPanel.getCategoryComboBox().setSelectedItem(newCategoryName);
            }
        }
    }
    
    private void hideColumn(int columnIndex) {
        TableColumn column = table.getColumnModel().getColumn(columnIndex);
        column.setMinWidth(0);
        column.setMaxWidth(0);
        column.setPreferredWidth(0);
        column.setResizable(false);
    }

    // Lớp nội (inner class) để vẽ màu cho cột số tiền
    private class AmountRenderer extends DefaultTableCellRenderer {
        private final DecimalFormat formatter = new DecimalFormat("#,##0");

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (value instanceof Double) {
                double amount = (double) value;
                int modelRow = table.convertRowIndexToModel(row);
                String transactionType = table.getModel().getValueAt(modelRow, 2).toString();

                if (transactionType.equalsIgnoreCase("Thu nhập")) {
                    c.setForeground(new Color(0, 128, 0));
                    setText("+" + formatter.format(amount));
                } else {
                    c.setForeground(Color.RED);
                    setText("-" + formatter.format(amount));
                }
            }
            setHorizontalAlignment(JLabel.RIGHT);
            return c;
        }
    }
}