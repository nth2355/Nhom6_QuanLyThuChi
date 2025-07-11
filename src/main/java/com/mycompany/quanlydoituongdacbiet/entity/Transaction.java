package com.mycompany.quanlydoituongdacbiet.entity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public abstract class Transaction {

    private int id;
    private LocalDate date; 
    private String category;
    private String description;
    private double amount;



    public Transaction() {
    }

    public Transaction(int id, LocalDate date, String category, String description, double amount) {
        this.id = id;
        this.date = date;
        this.category = category;
        this.description = description;
        this.amount = amount;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    // --- Abstract Method ---

    /**
     * Phương thức trừu tượng, buộc các lớp con phải định nghĩa
     * loại giao dịch của riêng nó (ví dụ: "Thu nhập" hoặc "Chi tiêu").
     * @return Một chuỗi String mô tả loại giao dịch.
     */
    public abstract String getType();

    // --- Overridden Methods ---

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = this.date.format(formatter);

        return  "ID=" + id +
                ", Ngày='" + formattedDate + '\'' +
                ", Danh mục='" + category + '\'' +
                ", Mô tả='" + description + '\'' +
                ", Số tiền=" + String.format("%,.0f", amount); 
    }
}