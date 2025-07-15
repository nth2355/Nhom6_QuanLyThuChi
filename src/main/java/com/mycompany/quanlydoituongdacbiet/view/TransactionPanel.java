package com.mycompany.quanlydoituongdacbiet.view;

import com.mycompany.quanlydoituongdacbiet.entity.Income;
import com.mycompany.quanlydoituongdacbiet.entity.Expense;
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

            if (index == 0) {
                incomeTabPanel.loadTableData();
            } else if (index == 1) {
                expenseTabPanel.loadTableData();
            } else if (index == 2) {
                searchPanel.loadAllTransactions();
            } else if (index == 3) {
                budgetPanel.loadBudgetsToTable();
            }
        });
    }

    public void loadTableData(List<Transaction> list) {
        List<Income> incomeList = list.stream()
                .filter(t -> t instanceof Income)
                .map(t -> (Income) t)
                .collect(Collectors.toList());

        List<Expense> expenseList = list.stream()
                .filter(t -> t instanceof Expense)
                .map(t -> (Expense) t)
                .collect(Collectors.toList());

        incomeTabPanel.loadTableData(incomeList);
        expenseTabPanel.loadTableData(expenseList);
    }


    public void reloadAllTabs() {
        incomeTabPanel.loadTableData();
        expenseTabPanel.loadTableData();
        searchPanel.loadAllTransactions();
        budgetPanel.loadBudgetsToTable();
    }
}
