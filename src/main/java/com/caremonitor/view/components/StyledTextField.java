package com.caremonitor.view.components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

import com.caremonitor.view.theme.UIStyles;

/**
 * Text and password fields with consistent styling and placeholder support.
 */
public class StyledTextField extends JTextField {
    private String placeholder;

    public StyledTextField() {
        this(null);
    }

    public StyledTextField(String placeholder) {
        this.placeholder = placeholder;
        setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(UIStyles.SUBTITLE_GRAY, 1),
                new EmptyBorder(10, 10, 10, 10)));
        setFont(UIStyles.ARIAL_PLAIN_14);
        setOpaque(false);
    }

    public void setPlaceholderText(String placeholder) {
        this.placeholder = placeholder;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (placeholder != null && getText().isEmpty() && !hasFocus()) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(Color.GRAY);
            g2.setFont(getFont().deriveFont(Font.ITALIC));
            FontMetrics fm = g2.getFontMetrics();
            int x = getInsets().left;
            int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(placeholder, x, y);
            g2.dispose();
        }
    }

    /** Password field with the same styling and placeholder behaviour. */
    public static class StyledPasswordField extends JPasswordField {
        private String placeholder;

        public StyledPasswordField() {
            this(null);
        }

        public StyledPasswordField(String placeholder) {
            this.placeholder = placeholder;
            setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(UIStyles.SUBTITLE_GRAY, 1),
                    new EmptyBorder(10, 10, 10, 10)));
            setFont(UIStyles.ARIAL_PLAIN_14);
            setEchoChar('•');
            setOpaque(false);
        }

        public void setPlaceholderText(String placeholder) {
            this.placeholder = placeholder;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (placeholder != null && getPassword().length == 0 && !hasFocus()) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(Color.GRAY);
                g2.setFont(getFont().deriveFont(Font.ITALIC));
                FontMetrics fm = g2.getFontMetrics();
                int x = getInsets().left;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(placeholder, x, y);
                g2.dispose();
            }
        }
    }
}
