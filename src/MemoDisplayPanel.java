/*
 * Name:        Quang Ho
 * Course:      CS 26000-02 Data Structures, Fall 2026
 * Project:     Project 2
 * File:        MemoDisplayPanel.java
 * Date:        10/13/2026
 *
 * Description: The message board. Draws every memo in the linked list, from
 *              head to tail, as a colored box that holds 4 rows by 20 columns
 *              of text.
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

public class MemoDisplayPanel extends JPanel {

    public static final int MEMO_WIDTH = 160;
    public static final int MEMO_HEIGHT = 160;

    private static final Color BOARD_COLOR = new Color(244, 244, 244);
    private static final Font TITLE_FONT = new Font("SansSerif", Font.PLAIN, 14);
    private static final Font NOTE_FONT = new Font("SansSerif", Font.PLAIN, 12);

    private MemoLinkedList memoList;

    /**
     * Creates a message board that displays the memos in the given list.
     *
     * @param memoList the list of memos to display
     */
    public MemoDisplayPanel(MemoLinkedList memoList) {
        this.memoList = memoList;
        setBackground(BOARD_COLOR);
    }

    /**
     * Paints the board and then every memo from the beginning of the list to
     * the end, so later memos appear on top of earlier ones.
     *
     * @param g the graphics context
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        for (MemoNode node = memoList.getHead(); node != null; node = node.getLink()) {
            drawMemo(g, node.getData());
        }
    }

    /**
     * Draws one memo box at the memo's (x, y) location.
     *
     * @param g    the graphics context
     * @param memo the memo to draw
     */
    private void drawMemo(Graphics g, Memo memo) {
        int x = memo.getX();
        int y = memo.getY();

        g.setColor(memo.getBgColor());
        g.fillRect(x, y, MEMO_WIDTH, MEMO_HEIGHT);

        g.setColor(memo.getFgColor());
        g.setFont(TITLE_FONT);
        g.drawString("Memo " + memo.getMemoID(), x + 5, y + 18);

        g.setFont(NOTE_FONT);
        String[] lines = memo.getNote().split("\n");
        for (int i = 0; i < lines.length; i++) {
            g.drawString(lines[i], x + 5, y + 36 + i * 15);
        }
    }
}
