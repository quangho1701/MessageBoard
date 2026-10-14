/*
 * Name:        Quang Ho
 * Course:      CS 26000-02 Data Structures, Fall 2026
 * Project:     Project 2
 * File:        Memo.java
 * Date:        10/07/2026
 *
 * Description: Represents a single "stick on" memo note. A memo has a unique
 *              id, a location on the message board, up to four lines of text
 *              (each at most 20 characters), and foreground/background colors.
 */

import java.awt.Color;

public class Memo implements Cloneable {

    public static final int MAX_LINES = 4;
    public static final int MAX_CHARS_PER_LINE = 20;

    private int memoID;
    private int x;
    private int y;
    private String note;
    private Color fgColor;
    private Color bgColor;

    /**
     * Default constructor: an empty black-on-yellow memo at (0, 0).
     */
    public Memo() {
        this(0, 0, 0, "", Color.BLACK, Color.YELLOW);
    }

    /**
     * Full constructor.
     */
    public Memo(int memoID, int x, int y, String note, Color fgColor, Color bgColor) {
        this.memoID = memoID;
        this.x = x;
        this.y = y;
        setNote(note);
        this.fgColor = fgColor;
        this.bgColor = bgColor;
    }

    // ---------------------------------------------------------------
    // Accessors
    // ---------------------------------------------------------------

    public int getMemoID() {
        return memoID;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getNote() {
        return note;
    }

    public Color getFgColor() {
        return fgColor;
    }

    public Color getBgColor() {
        return bgColor;
    }

    // ---------------------------------------------------------------
    // Modifiers
    // ---------------------------------------------------------------

    public void setMemoID(int memoID) {
        this.memoID = memoID;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    /**
     * Postcondition: characters beyond MAX_CHARS_PER_LINE on each line and
     * lines beyond MAX_LINES are ignored.
     */
    public void setNote(String note) {
        String[] lines = note.split("\n");
        int lineCount = Math.min(lines.length, MAX_LINES);
        String result = "";

        for (int i = 0; i < lineCount; i++) {
            if (i > 0) {
                result += "\n";
            }
            String line = lines[i];
            if (line.length() > MAX_CHARS_PER_LINE) {
                line = line.substring(0, MAX_CHARS_PER_LINE);
            }
            result += line;
        }
        this.note = result;
    }

    public void setFgColor(Color fgColor) {
        this.fgColor = fgColor;
    }

    public void setBgColor(Color bgColor) {
        this.bgColor = bgColor;
    }

    // ---------------------------------------------------------------
    // equals / clone
    // ---------------------------------------------------------------

    /**
     * Two memos are equal when all of their data fields are equal.
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Memo)) {
            return false;
        }
        Memo other = (Memo) obj;
        return memoID == other.memoID
                && x == other.x
                && y == other.y
                && note.equals(other.note)
                && fgColor.equals(other.fgColor)
                && bgColor.equals(other.bgColor);
    }

    @Override
    public Memo clone() {
        try {
            return (Memo) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
