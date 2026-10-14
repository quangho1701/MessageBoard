/*
 * Name:        Quang Ho
 * Course:      CS 26000-02 Data Structures, Fall 2026
 * Project:     Project 2
 * File:        FileIO.java
 * Date:        10/13/2026
 *
 * Description: Reads memos from and writes memos to text files. Also keeps
 *              the list of predefined Java colors supported by the program.
 *
 * File format (per memo):
 *   memoID
 *   x y
 *   note (one to four lines)
 *   foreground color (e.g. Color.black)
 *   background color (e.g. Color.yellow)
 */

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.NoSuchElementException;
import java.util.Scanner;
import javafx.scene.paint.Color;

public class FileIO {

    public static final String COLOR_PREFIX = "Color.";

    public static final String[] COLOR_NAMES = {
        "Black", "Blue", "Cyan", "Gray", "Green", "Magenta",
        "Orange", "Pink", "Red", "White", "Yellow"
    };

    public static final Color[] COLORS = {
        Color.BLACK, Color.BLUE, Color.CYAN, Color.GRAY, Color.GREEN, Color.MAGENTA,
        Color.ORANGE, Color.PINK, Color.RED, Color.WHITE, Color.YELLOW
    };

    /**
     * Reads every memo in the file and returns them in a new list, in the
     * same order as they appear in the file.
     *
     * @param file the input file
     * @return a list containing the memos read from the file
     * @throws IOException if the file cannot be opened or is not formatted
     *                     correctly
     */
    public static MemoLinkedList readMemos(File file) throws IOException {
        MemoLinkedList list = new MemoLinkedList();

        try (Scanner input = new Scanner(file)) {
            while (input.hasNextLine()) {
                String idLine = input.nextLine().trim();
                if (idLine.isEmpty()) {
                    continue;   // skip blank lines between memos
                }
                int memoID = Integer.parseInt(idLine);

                String[] location = input.nextLine().trim().split("\\s+");
                int x = Integer.parseInt(location[0]);
                int y = Integer.parseInt(location[1]);

                // Every line up to the foreground color belongs to the note.
                String note = "";
                int lineCount = 0;
                String line = input.nextLine();
                while (!line.trim().startsWith(COLOR_PREFIX)) {
                    if (lineCount > 0) {
                        note += "\n";
                    }
                    note += line;
                    lineCount++;
                    line = input.nextLine();
                }

                Color fgColor = toColor(line);
                Color bgColor = toColor(input.nextLine());
                list.insertAfter(new Memo(memoID, x, y, note, fgColor, bgColor));
            }
        } catch (NoSuchElementException | IllegalArgumentException e) {
            throw new IOException("Invalid memo file format: " + file.getName());
        }
        return list;
    }

    /**
     * Writes every memo in the list (head to tail) to the file.
     *
     * @param file the output file
     * @param list the list of memos to write
     * @throws IOException if the file cannot be created
     */
    public static void writeMemos(File file, MemoLinkedList list) throws IOException {
        try (PrintWriter output = new PrintWriter(new FileWriter(file))) {
            for (MemoNode node = list.getHead(); node != null; node = node.getLink()) {
                Memo memo = node.getData();
                output.println(memo.getMemoID());
                output.println(memo.getX() + " " + memo.getY());
                for (String line : memo.getNote().split("\n")) {
                    output.println(line);
                }
                output.println(toName(memo.getFgColor()));
                output.println(toName(memo.getBgColor()));
            }
        }
    }

    /**
     * Converts a color name such as "Color.black" or "Black" to a Color.
     *
     * @param name the color name (case-insensitive)
     * @return the matching predefined Java color
     * @throws IllegalArgumentException if the color is not supported
     */
    public static Color toColor(String name) {
        String colorName = name.trim();
        if (colorName.startsWith(COLOR_PREFIX)) {
            colorName = colorName.substring(COLOR_PREFIX.length());
        }
        for (int i = 0; i < COLOR_NAMES.length; i++) {
            if (COLOR_NAMES[i].equalsIgnoreCase(colorName)) {
                return COLORS[i];
            }
        }
        throw new IllegalArgumentException("Unknown color: " + name);
    }

    /**
     * Converts a Color to the name used in memo files, such as "Color.black".
     *
     * @param color one of the supported colors
     * @return the file name of the color ("Color.black" if not supported)
     */
    public static String toName(Color color) {
        for (int i = 0; i < COLORS.length; i++) {
            if (COLORS[i].equals(color)) {
                return COLOR_PREFIX + COLOR_NAMES[i].toLowerCase();
            }
        }
        return COLOR_PREFIX + "black";
    }
}
