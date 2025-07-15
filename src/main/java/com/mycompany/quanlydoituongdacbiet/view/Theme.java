package com.mycompany.quanlydoituongdacbiet.view;

import javax.swing.ImageIcon;
import javax.swing.UIManager;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.net.URL;

public class Theme {

 // --- BỘ MÀU MỚI: XÁM NHẠT & XANH PASTEL ---
    public static final Color APP_BACKGROUND = Color.decode("#F8F9FA");
    public static final Color PRIMARY_UI_COLOR = Color.decode("#E9ECEF");
    public static final Color SELECTION_COLOR = Color.decode("#D0EFFF");
    public static final Color TABLE_HEADER_COLOR = Color.decode("#DEE2E6");
    public static final Color TEXT_COLOR = Color.decode("#495057");
    public static final Color EXPENSE_BUTTON_COLOR = Color.decode("#F5A623");

    // --- THÊM MÀU MỚI CHO NÚT HÀNH ĐỘNG CHÍNH ---
    public static final Color ACTION_BUTTON_COLOR = Color.decode("#4CAF50"); // Xanh lá cây

    // Font chữ
    public static final Font MAIN_FONT = new Font("Sans-serif ", Font.PLAIN, 14);
    public static final Font BOLD_FONT = new Font("Sans-serif ", Font.BOLD, 14);
    
    
    public static ImageIcon getIcon(String fullPath) {
        URL iconURL = Theme.class.getResource(fullPath);
        if (iconURL != null) {
            return new ImageIcon(iconURL);
        } else {
            System.err.println("Lỗi: Không tìm thấy tài nguyên tại đường dẫn: " + fullPath);
            return null;
        }
    }
    
    public static ImageIcon getScaledIcon(String fullPath, int width, int height) {
        ImageIcon originalIcon = getIcon(fullPath); 
        if (originalIcon != null) {
            Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        }
        return null;
    }

    /**
     * Áp dụng các cài đặt giao diện chung cho toàn bộ ứng dụng.
     */
    public static void apply() {
        try {
            // Thiết lập chung
            UIManager.put("Panel.background", APP_BACKGROUND);
            UIManager.put("Button.font", BOLD_FONT);
            UIManager.put("Label.font", BOLD_FONT); // Bôi đậm các nhãn
            UIManager.put("TextField.font", MAIN_FONT);
            UIManager.put("ComboBox.font", MAIN_FONT);
            UIManager.put("Table.font", MAIN_FONT); // Font cho nội dung bảng
            
            // 2. Nút bấm và icon nền đồng bộ màu
            UIManager.put("Button.background", PRIMARY_UI_COLOR);
            UIManager.put("TabbedPane.selected", PRIMARY_UI_COLOR);
            UIManager.put("Component.focusColor", PRIMARY_UI_COLOR);
            
            // Màu nền khi chọn/hover
            UIManager.put("TextField.selectionBackground", SELECTION_COLOR);
            UIManager.put("ComboBox.selectionBackground", SELECTION_COLOR); 
            
            // 3. Header bảng có màu nền khác để nổi bật
            UIManager.put("TableHeader.font", BOLD_FONT);
            UIManager.put("TableHeader.background", TABLE_HEADER_COLOR);
            
            // Thiết lập màu chữ
            UIManager.put("Label.foreground", TEXT_COLOR);
            UIManager.put("Button.foreground", TEXT_COLOR);
            UIManager.put("TabbedPane.focus", TEXT_COLOR);
            UIManager.put("TextField.foreground", TEXT_COLOR);
            UIManager.put("ComboBox.foreground", TEXT_COLOR);
            UIManager.put("TableHeader.foreground", TEXT_COLOR);
            UIManager.put("Table.foreground", TEXT_COLOR);
            
            // Nền mặc định cho ComboBox
            UIManager.put("ComboBox.background", Color.WHITE); 
            UIManager.put("ComboBox.buttonBackground", Color.WHITE);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}