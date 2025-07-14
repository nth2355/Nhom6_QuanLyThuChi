package com.mycompany.quanlydoituongdacbiet.entity;

import java.time.LocalDate;

public class Expense extends Transaction {
    public Expense(int id, LocalDate date, String category, String description, double amount) {
        super(id, date, category, description, amount);
    }

    public Expense(LocalDate date, String category, String description, double amount) {
        super(-1, date, category, description, amount);
    }

    @Override
    public String getType() {
        return "Expense";
    }
}
