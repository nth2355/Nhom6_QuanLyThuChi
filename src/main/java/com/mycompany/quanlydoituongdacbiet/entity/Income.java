package com.mycompany.quanlydoituongdacbiet.entity;

import java.time.LocalDate;

public class Income extends Transaction {
    public Income(int id, LocalDate date, String category, String description, double amount) {
        super(id, date, category, description, amount);
    }

    public Income(LocalDate date, String category, String description, double amount) {
        super(-1, date, category, description, amount);
    }

    @Override
    public String getType() {
        return "Income";
    }
}
