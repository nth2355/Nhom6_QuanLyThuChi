package com.mycompany.quanlydoituongdacbiet.entity;

import java.time.LocalDate;


public class Expense extends Transaction {


    public Expense(int id, LocalDate date, String category, String description, double amount) {
        super(id, date, category, description, amount);
    }

    @Override
    public String getType() {
        return "Chi tiêu";
    }

    @Override
    public String toString() {
        return "💸 [CHI TIÊU] " + super.toString();
    }
}