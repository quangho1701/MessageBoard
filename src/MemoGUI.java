/*
 * Name:        Quang Ho
 * Course:      CS 26000-02 Data Structures, Fall 2026
 * Project:     Project 2
 * File:        MemoGUI.java
 * Date:        10/13/2026
 *
 * Description: Main window of the message board. Contains a display panel
 *              that shows the memos and a control panel used to add, delete,
 *              read, write, merge, move, and clear memos.
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MemoGUI extends JFrame implements ActionListener {

    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 600;
    public static final int CONTROL_WIDTH = 190;

    private static final Color CONTROL_COLOR = new Color(240, 248, 255);
    private static final Font LABEL_FONT = new Font("SansSerif", Font.PLAIN, 18);

    private MemoLinkedList memoList;
    private MemoDisplayPanel displayPanel;
    private JFileChooser fileChooser;

    private JButton addButton;
    private JButton deleteButton;
    private JButton readButton;
    private JButton writeButton;
    private JButton mergeButton;
    private JButton moveButton;
    private JButton clearButton;
    private JButton quitButton;

    private JComboBox<String> fgColorBox;
    private JComboBox<String> bgColorBox;

    /**
     * Creates and shows the message board window for the given list.
     *
     * @param memoList the list of memos to display and edit
     */
    public MemoGUI(MemoLinkedList memoList) {
        super("Project #2 : Message Board");
        this.memoList = memoList;
        fileChooser = new JFileChooser(System.getProperty("user.dir"));

        displayPanel = new MemoDisplayPanel(memoList);
        add(displayPanel, BorderLayout.CENTER);
        add(createControlPanel(), BorderLayout.EAST);

        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    /**
     * Builds the control panel: two color combo boxes followed by the
     * command buttons.
     *
     * @return the control panel
     */
    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel(new BorderLayout(0, 50));
        controlPanel.setBackground(CONTROL_COLOR);
        controlPanel.setPreferredSize(new Dimension(CONTROL_WIDTH, WINDOW_HEIGHT));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Color selection
        bgColorBox = new JComboBox<>(FileIO.COLOR_NAMES);
        bgColorBox.setSelectedItem("Black");
        fgColorBox = new JComboBox<>(FileIO.COLOR_NAMES);
        fgColorBox.setSelectedItem("White");

        JLabel bgLabel = new JLabel("Background Color");
        bgLabel.setFont(LABEL_FONT);
        JLabel fgLabel = new JLabel("Foreground Color");
        fgLabel.setFont(LABEL_FONT);

        JPanel colorPanel = new JPanel(new GridLayout(4, 1, 0, 6));
        colorPanel.setOpaque(false);
        colorPanel.add(bgLabel);
        colorPanel.add(bgColorBox);
        colorPanel.add(fgLabel);
        colorPanel.add(fgColorBox);

        // Command buttons
        addButton = new JButton("Add Memo");
        deleteButton = new JButton("Delete Memo");
        readButton = new JButton("Read Memos");
        writeButton = new JButton("Write Memos");
        mergeButton = new JButton("Merge Memos");
        moveButton = new JButton("Move Memo");
        clearButton = new JButton("Clear");
        quitButton = new JButton("Quit");

        JPanel buttonPanel = new JPanel(new GridLayout(8, 1, 0, 2));
        buttonPanel.setOpaque(false);
        JButton[] buttons = {addButton, deleteButton, readButton, writeButton,
                             mergeButton, moveButton, clearButton, quitButton};
        for (JButton button : buttons) {
            button.setPreferredSize(new Dimension(CONTROL_WIDTH, 34));
            button.addActionListener(this);
            buttonPanel.add(button);
        }

        // Keep the buttons at the top instead of stretching to the bottom.
        JPanel buttonHolder = new JPanel(new BorderLayout());
        buttonHolder.setOpaque(false);
        buttonHolder.add(buttonPanel, BorderLayout.NORTH);

        controlPanel.add(colorPanel, BorderLayout.NORTH);
        controlPanel.add(buttonHolder, BorderLayout.CENTER);
        return controlPanel;
    }

    /**
     * Runs the command of the button that was clicked and reports any
     * invalid input or file error to the user.
     *
     * @param e the button click event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        try {
            if (source == addButton) {
                addMemo();
            } else if (source == deleteButton) {
                deleteMemo();
            } else if (source == readButton) {
                readMemos();
            } else if (source == writeButton) {
                writeMemos();
            } else if (source == mergeButton) {
                mergeMemos();
            } else if (source == moveButton) {
                moveMemo();
            } else if (source == clearButton) {
                clearMemos();
            } else if (source == quitButton) {
                quit();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Please enter whole numbers for the memo ID and location.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException | IOException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // ---------------------------------------------------------------
    // Command handlers
    // ---------------------------------------------------------------

    /**
     * Prompts for memoID, content, and location; colors come from the combo
     * boxes. The memo is inserted (or replaced) at the cursor.
     *
     * @throws IllegalArgumentException if the memo does not fit in the window
     */
    private void addMemo() {
        JTextField idField = new JTextField();
        JTextField xField = new JTextField();
        JTextField yField = new JTextField();
        JTextField[] lineFields = new JTextField[Memo.MAX_LINES];

        JPanel prompt = new JPanel(new GridLayout(0, 1));
        prompt.add(new JLabel("Enter Memo ID:"));
        prompt.add(idField);
        prompt.add(new JLabel("X-Position:"));
        prompt.add(xField);
        prompt.add(new JLabel("Y-Position:"));
        prompt.add(yField);
        prompt.add(new JLabel("Message:"));
        for (int i = 0; i < lineFields.length; i++) {
            lineFields[i] = new JTextField(Memo.MAX_CHARS_PER_LINE);
            prompt.add(lineFields[i]);
        }

        int choice = JOptionPane.showConfirmDialog(this, prompt, "Memo Data Prompt",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (choice != JOptionPane.OK_OPTION) {
            return;
        }

        int memoID = Integer.parseInt(idField.getText().trim());
        int x = Integer.parseInt(xField.getText().trim());
        int y = Integer.parseInt(yField.getText().trim());
        checkBounds(x, y);

        String note = "";
        for (int i = 0; i < lineFields.length; i++) {
            if (i > 0) {
                note += "\n";
            }
            note += lineFields[i].getText();
        }

        Color fgColor = FileIO.COLORS[fgColorBox.getSelectedIndex()];
        Color bgColor = FileIO.COLORS[bgColorBox.getSelectedIndex()];
        memoList.insertAfter(new Memo(memoID, x, y, note, fgColor, bgColor));
        displayPanel.repaint();
    }

    /**
     * Prompts for a memoID and deletes the matching memo, if there is one.
     */
    private void deleteMemo() {
        String input = JOptionPane.showInputDialog(this, "Enter Memo ID to Delete:",
                "Delete Memo", JOptionPane.PLAIN_MESSAGE);
        if (input == null) {
            return;
        }

        int memoID = Integer.parseInt(input.trim());
        if (memoList.search(memoID)) {
            memoList.remove(memoID);
            displayPanel.repaint();
        } else {
            JOptionPane.showMessageDialog(this, "Memo " + memoID + " was not found.");
        }
    }

    /**
     * Clears the current list, then reads memos from a chosen file.
     *
     * @throws IOException if the file cannot be read
     */
    private void readMemos() throws IOException {
        if (fileChooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        // Read first so a bad file does not wipe out the current memos.
        MemoLinkedList newList = FileIO.readMemos(fileChooser.getSelectedFile());
        memoList.clear();
        memoList.merge(newList);
        displayPanel.repaint();
    }

    /**
     * Writes all memos in the list to a chosen output file.
     *
     * @throws IOException if the file cannot be written
     */
    private void writeMemos() throws IOException {
        if (fileChooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        FileIO.writeMemos(fileChooser.getSelectedFile(), memoList);
        JOptionPane.showMessageDialog(this, "Memos were written to "
                + fileChooser.getSelectedFile().getName() + ".");
    }

    /**
     * Reads memos from a chosen file and merges them into the current list.
     *
     * @throws IOException if the file cannot be read
     */
    private void mergeMemos() throws IOException {
        if (fileChooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        memoList.merge(FileIO.readMemos(fileChooser.getSelectedFile()));
        displayPanel.repaint();
    }

    /**
     * Prompts for a memoID and a new location, then moves that memo.
     *
     * @throws IllegalArgumentException if the new location does not fit in
     *                                  the window
     */
    private void moveMemo() {
        JTextField idField = new JTextField();
        JTextField xField = new JTextField();
        JTextField yField = new JTextField();

        JPanel prompt = new JPanel(new GridLayout(0, 1));
        prompt.add(new JLabel("Enter Memo ID to Move:"));
        prompt.add(idField);
        prompt.add(new JLabel("New X-Position:"));
        prompt.add(xField);
        prompt.add(new JLabel("New Y-Position:"));
        prompt.add(yField);

        int choice = JOptionPane.showConfirmDialog(this, prompt, "Move Memo",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (choice != JOptionPane.OK_OPTION) {
            return;
        }

        int memoID = Integer.parseInt(idField.getText().trim());
        int x = Integer.parseInt(xField.getText().trim());
        int y = Integer.parseInt(yField.getText().trim());
        checkBounds(x, y);

        if (!memoList.search(memoID)) {
            JOptionPane.showMessageDialog(this, "Memo " + memoID + " was not found.");
            return;
        }

        // Same memoID, so insertAfter replaces the memo in place.
        Memo memo = memoList.retrieve();
        memo.setX(x);
        memo.setY(y);
        memoList.insertAfter(memo);
        displayPanel.repaint();
    }

    /**
     * Deletes all memos in the list.
     */
    private void clearMemos() {
        memoList.clear();
        displayPanel.repaint();
    }

    /**
     * Exits the application.
     */
    private void quit() {
        System.exit(0);
    }

    /**
     * Makes sure a memo placed at (x, y) fits within the message board.
     *
     * @param x the x-coordinate of the memo's upper left corner
     * @param y the y-coordinate of the memo's upper left corner
     * @throws IllegalArgumentException if the memo would not fit
     */
    private void checkBounds(int x, int y) {
        if (x < 0 || y < 0
                || x + MemoDisplayPanel.MEMO_WIDTH > displayPanel.getWidth()
                || y + MemoDisplayPanel.MEMO_HEIGHT > displayPanel.getHeight()) {
            throw new IllegalArgumentException("A memo at (" + x + ", " + y
                    + ") does not fit within the window boundaries.\n"
                    + "X must be 0 to " + (displayPanel.getWidth() - MemoDisplayPanel.MEMO_WIDTH)
                    + " and Y must be 0 to " + (displayPanel.getHeight() - MemoDisplayPanel.MEMO_HEIGHT) + ".");
        }
    }
}
