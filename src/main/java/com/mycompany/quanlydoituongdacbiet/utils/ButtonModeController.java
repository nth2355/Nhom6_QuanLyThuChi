package com.mycompany.quanlydoituongdacbiet.utils;

import javax.swing.*;
import java.awt.event.ActionListener;

public final class ButtonModeController {
    private final JButton btnSave;
    private final JButton btnUpdate;
    private final JButton btnCancel;

    public ButtonModeController(JButton btnSave, JButton btnUpdate, JButton btnCancel) {
        this.btnSave = btnSave;
        this.btnUpdate = btnUpdate;
        this.btnCancel = btnCancel;

        showSaveMode(); // mặc định
    }

    public void showSaveMode() {
        btnSave.setVisible(true);
        btnUpdate.setVisible(false);
        btnCancel.setVisible(false);
    }

    public void showEditMode() {
        btnSave.setVisible(false);
        btnUpdate.setVisible(true);
        btnCancel.setVisible(true);
    }

    public void onSave(ActionListener listener) {
        btnSave.addActionListener(listener);
    }

    public void onUpdate(ActionListener listener) {
        btnUpdate.addActionListener(listener);
    }

    public void onCancel(ActionListener listener) {
        btnCancel.addActionListener(listener);
    }
}
