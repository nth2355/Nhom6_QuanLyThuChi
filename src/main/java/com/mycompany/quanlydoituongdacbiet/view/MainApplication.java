package com.mycompany.quanlydoituongdacbiet.view;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class MainApplication {
    public static void main(String[] args) {
        Theme.apply();

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Trình Quản Lý Thu Chi Cá Nhân");
            frame.setIconImage(Theme.getIcon("/icons/bar-chart.png").getImage());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.add(new TransactionPanel());
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}