/*
 * Name:        Quang Ho
 * Course:      CS 26000-02 Data Structures, Fall 2026
 * Project:     Project 2
 * File:        Project2.java
 * Date:        10/13/2026
 *
 * Description: Driver for the message board application. Prompts the user
 *              for a memo file, loads it, and launches the GUI.
 */

import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class Project2 {

    /**
     * Prompts the user for a memo note file, reads it, and displays the
     * memos on the message board. If the user cancels, the board starts empty.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // keep the default look and feel
        }

        MemoLinkedList memoList = new MemoLinkedList();

        JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
        fileChooser.setDialogTitle("Open a Memo Note File");
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            try {
                memoList = FileIO.readMemos(fileChooser.getSelectedFile());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

        new MemoGUI(memoList);
    }
}
