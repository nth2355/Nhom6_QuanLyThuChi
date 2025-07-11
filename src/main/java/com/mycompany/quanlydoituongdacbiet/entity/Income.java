package com.mycompany.quanlydoituongdacbiet.entity;

import java.time.LocalDate;

public class Income extends Transaction {

    public Income(int id, LocalDate date, String category, String description, double amount) {
        // Gọi constructor của lớp cha để khởi tạo các thuộc tính chung
        super(id, date, category, description, amount);
    }
    
    @Override
    public String getType() {
        return "Thu nhập";
    }


    @Override
    public String toString() {
        return "✅ [THU NHẬP] " + super.toString();
    }
}