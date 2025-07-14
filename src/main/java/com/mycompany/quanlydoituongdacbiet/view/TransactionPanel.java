package com.mycompany.quanlydoituongdacbiet.view;

import com.mycompany.quanlydoituongdacbiet.controller.TransactionController;
import com.mycompany.quanlydoituongdacbiet.entity.Transaction;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionPanel extends JPanel {

    private final JTabbedPane tabbedPane;
    private final IncomeTabPanel incomeTabPanel;
    private final ExpenseTabPanel expenseTabPanel;
    private final SearchPanel searchPanel;
    private final BudgetPanel budgetPanel;

    public TransactionPanel() {
        setLayout(new BorderLayout(0, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        tabbedPane = new JTabbedPane();

        incomeTabPanel = new IncomeTabPanel();
        expenseTabPanel = new ExpenseTabPanel();
        searchPanel = new SearchPanel(this);
        budgetPanel = new BudgetPanel();

        tabbedPane.addTab("Quản Lý Thu", Theme.getScaledIcon("/icons/income.png", 16, 16), incomeTabPanel);
        tabbedPane.addTab("Quản Lý Chi", Theme.getScaledIcon("/icons/expense.png", 16, 16), expenseTabPanel);
        tabbedPane.addTab("Tìm kiếm", Theme.getScaledIcon("/icons/search.png", 16, 16), searchPanel);
        tabbedPane.addTab("Ngân Sách", Theme.getScaledIcon("/icons/budget.png", 16, 16), budgetPanel);

        add(tabbedPane, BorderLayout.CENTER);

        addTabChangeListener();
    }

    private void addTabChangeListener() {
        tabbedPane.addChangeListener(e -> {
            int index = tabbedPane.getSelectedIndex();

            List<Transaction> all = TransactionController.getAllTransactions();

            if (index == 0) {
                List<Transaction> incomeList = all.stream()
                        .filter(t -> "Income".equalsIgnoreCase(t.getType()))
                        .collect(Collectors.toList());
                incomeTabPanel.loadTableData(incomeList);
            } else if (index == 1) {
                List<Transaction> expenseList = all.stream()
                        .filter(t -> "Expense".equalsIgnoreCase(t.getType()))
                        .collect(Collectors.toList());
                expenseTabPanel.loadTableData(expenseList);
            } else if (index == 2) {
                searchPanel.loadAllTransactions();
            } else if (index == 3) {
                budgetPanel.loadBudgetsToTable();
            }
        });
    }

    public void loadTableData(List<Transaction> list) {
        List<Transaction> incomeList = list.stream()
                .filter(t -> "Income".equalsIgnoreCase(t.getType()))
                .collect(Collectors.toList());
        List<Transaction> expenseList = list.stream()
                .filter(t -> "Expense".equalsIgnoreCase(t.getType()))
                .collect(Collectors.toList());

        incomeTabPanel.loadTableData(incomeList);
        expenseTabPanel.loadTableData(expenseList);
    }

    public void reloadAllTabs() {
        List<Transaction> all = TransactionController.getAllTransactions();
        loadTableData(all);
        searchPanel.loadAllTransactions();
        budgetPanel.loadBudgetsToTable();
    }
}
