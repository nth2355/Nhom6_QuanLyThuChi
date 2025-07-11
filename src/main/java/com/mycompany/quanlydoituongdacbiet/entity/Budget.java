package com.mycompany.quanlydoituongdacbiet.entity;

public class Budget {
    private String category;
    private double amount;
    private int month;
    private int year;

    public Budget() {
    }

    public Budget(String category, double amount, int month, int year) {
        this.category = category;
        this.amount = amount;
        this.month = month;
        this.year = year;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}