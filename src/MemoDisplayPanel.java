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

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;

public class MemoDisplayPanel extends Canvas {

    public static final int MEMO_WIDTH = 160;
    public static final int MEMO_HEIGHT = 160;

    private static final Font TITLE_FONT = Font.font(14);
    private static final Font NOTE_FONT = Font.font(12);

    private MemoLinkedList memoList;

    /**
     * Creates a message board of the given size that displays the memos in
     * the given list.
     *
     * @param memoList the list of memos to display
     * @param width    the width of the board
     * @param height   the height of the board
     */
    public MemoDisplayPanel(MemoLinkedList memoList, double width, double height) {
        super(width, height);
        this.memoList = memoList;
    }

    /**
     * Clears the board and draws every memo from the beginning of the list to
     * the end, so later memos appear on top of earlier ones.
     */
    public void draw() {
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());

        for (MemoNode node = memoList.getHead(); node != null; node = node.getLink()) {
            drawMemo(gc, node.getData());
        }
    }

    /**
     * Draws one memo box at the memo's (x, y) location.
     *
     * @param gc   the graphics context of the board
     * @param memo the memo to draw
     */
    private void drawMemo(GraphicsContext gc, Memo memo) {
        int x = memo.getX();
        int y = memo.getY();

        gc.setFill(memo.getBgColor());
        gc.fillRect(x, y, MEMO_WIDTH, MEMO_HEIGHT);

        gc.setFill(memo.getFgColor());
        gc.setFont(TITLE_FONT);
        gc.fillText("Memo " + memo.getMemoID(), x + 5, y + 18);

        gc.setFont(NOTE_FONT);
        String[] lines = memo.getNote().split("\n");
        for (int i = 0; i < lines.length; i++) {
            gc.fillText(lines[i], x + 5, y + 35 + i * 15);
        }
    }
}
