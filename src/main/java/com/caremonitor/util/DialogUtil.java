// File: src/main/java/com/caremonitor/util/DialogUtil.java
package com.caremonitor.util;

import javax.swing.*;
import java.awt.*;

public final class DialogUtil {
    private DialogUtil() {}

    public static void showMessage(Component parent, String message, String title, int messageType) {
        if (GraphicsEnvironment.isHeadless() || Boolean.getBoolean("caremonitor.suppressDialogs")) {
            System.out.println(title + ": " + message);
            return;
        }
        JOptionPane.showMessageDialog(parent, message, title, messageType);
    }
}