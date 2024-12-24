package org.example.filter;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class NumberDocFilter extends DocumentFilter  {

    private final boolean allowDecimal;

    public NumberDocFilter(boolean allowDecimal) {
        this.allowDecimal = allowDecimal;
    }

    @Override
    public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        if (isValidInput(fb.getDocument().getText(0, fb.getDocument().getLength()) + string)) {
            super.insertString(fb, offset, string, attr);
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        if (isValidInput(fb.getDocument().getText(0, fb.getDocument().getLength()).substring(0, offset) + text +
                fb.getDocument().getText(0, fb.getDocument().getLength()).substring(offset + length))) {
            super.replace(fb, offset, length, text, attrs);
        }
    }

    @Override
    public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
        super.remove(fb, offset, length);
    }

    private boolean isValidInput(String text) {
        if (text.isEmpty()) return true;
        String regex = allowDecimal ? "\\d*(\\.\\d*)?" : "\\d*";
        return text.matches(regex);
    }
}
