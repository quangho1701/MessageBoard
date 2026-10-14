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

import java.io.File;
import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MemoGUI extends Application implements EventHandler<ActionEvent> {

    public static final int BOARD_WIDTH = 600;
    public static final int BOARD_HEIGHT = 560;
    public static final int CONTROL_WIDTH = 200;

    private MemoLinkedList memoList;
    private MemoDisplayPanel displayPanel;
    private Stage stage;
    private FileChooser fileChooser;

    private Button addButton;
    private Button deleteButton;
    private Button readButton;
    private Button writeButton;
    private Button mergeButton;
    private Button moveButton;
    private Button clearButton;
    private Button quitButton;

    private ComboBox<String> fgColorBox;
    private ComboBox<String> bgColorBox;

    /**
     * Builds and shows the message board window, then prompts the user for
     * a memo note file to display.
     *
     * @param primaryStage the main window
     */
    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        memoList = new MemoLinkedList();
        fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));

        displayPanel = new MemoDisplayPanel(memoList, BOARD_WIDTH, BOARD_HEIGHT);

        BorderPane root = new BorderPane();
        root.setCenter(displayPanel);
        root.setRight(createControlPanel());

        stage.setTitle("Project #2 : Message Board");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();

        // Prompt for the initial memo file, the same as the Read Memos button.
        readButton.fire();
    }

    /**
     * Builds the control panel: two color combo boxes followed by the
     * command buttons.
     *
     * @return the control panel
     */
    private VBox createControlPanel() {
        // Color selection
        Label bgLabel = new Label("Background Color");
        bgLabel.setFont(Font.font(18));
        bgColorBox = new ComboBox<>();
        bgColorBox.getItems().addAll(FileIO.COLOR_NAMES);
        bgColorBox.setValue("Black");
        bgColorBox.setMaxWidth(Double.MAX_VALUE);

        Label fgLabel = new Label("Foreground Color");
        fgLabel.setFont(Font.font(18));
        fgColorBox = new ComboBox<>();
        fgColorBox.getItems().addAll(FileIO.COLOR_NAMES);
        fgColorBox.setValue("White");
        fgColorBox.setMaxWidth(Double.MAX_VALUE);

        // Command buttons
        addButton = new Button("Add Memo");
        deleteButton = new Button("Delete Memo");
        readButton = new Button("Read Memos");
        writeButton = new Button("Write Memos");
        mergeButton = new Button("Merge Memos");
        moveButton = new Button("Move Memo");
        clearButton = new Button("Clear");
        quitButton = new Button("Quit");

        VBox buttonPanel = new VBox(1);
        Button[] buttons = {addButton, deleteButton, readButton, writeButton,
                            mergeButton, moveButton, clearButton, quitButton};
        for (Button button : buttons) {
            button.setMaxWidth(Double.MAX_VALUE);
            button.setPrefHeight(33);
            button.setOnAction(this);
            buttonPanel.getChildren().add(button);
        }

        VBox controlPanel = new VBox(8, bgLabel, bgColorBox, fgLabel, fgColorBox, buttonPanel);
        VBox.setMargin(buttonPanel, new Insets(40, 0, 0, 0));
        controlPanel.setPadding(new Insets(10));
        controlPanel.setPrefWidth(CONTROL_WIDTH);
        controlPanel.setStyle("-fx-background-color: aliceblue;");
        return controlPanel;
    }

    /**
     * Runs the command of the button that was clicked and reports any
     * invalid input or file error to the user.
     *
     * @param e the button click event
     */
    @Override
    public void handle(ActionEvent e) {
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
            new Alert(Alert.AlertType.ERROR,
                    "Please enter whole numbers for the memo ID and location.").showAndWait();
        } catch (IllegalArgumentException | IOException ex) {
            new Alert(Alert.AlertType.ERROR, ex.getMessage()).showAndWait();
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
        TextField idField = new TextField();
        TextField xField = new TextField();
        TextField yField = new TextField();
        TextField[] lineFields = new TextField[Memo.MAX_LINES];
        for (int i = 0; i < lineFields.length; i++) {
            lineFields[i] = new TextField();
            lineFields[i].setPromptText("LINE " + (i + 1));
        }

        boolean confirmed = showDialog("Memo Data Prompt", "Add Memo",
                new Label("Enter Memo ID:"), idField,
                new Label("X-Position:"), xField,
                new Label("Y-Position:"), yField,
                new Label("Message:"), lineFields[0], lineFields[1], lineFields[2], lineFields[3]);
        if (!confirmed) {
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

        Color fgColor = FileIO.toColor(fgColorBox.getValue());
        Color bgColor = FileIO.toColor(bgColorBox.getValue());
        memoList.insertAfter(new Memo(memoID, x, y, note, fgColor, bgColor));
        displayPanel.draw();
    }

    /**
     * Prompts for a memoID and deletes the matching memo, if there is one.
     */
    private void deleteMemo() {
        TextField idField = new TextField();
        if (!showDialog("Delete Memo", "OK", new Label("Enter Memo ID to Delete:"), idField)) {
            return;
        }

        int memoID = Integer.parseInt(idField.getText().trim());
        if (memoList.search(memoID)) {
            memoList.remove(memoID);
            displayPanel.draw();
        } else {
            new Alert(Alert.AlertType.INFORMATION, "Memo " + memoID + " was not found.").showAndWait();
        }
    }

    /**
     * Clears the current list, then reads memos from a chosen file.
     *
     * @throws IOException if the file cannot be read
     */
    private void readMemos() throws IOException {
        File file = fileChooser.showOpenDialog(stage);
        if (file == null) {
            return;
        }

        // Read first so a bad file does not wipe out the current memos.
        MemoLinkedList newList = FileIO.readMemos(file);
        memoList.clear();
        memoList.merge(newList);
        displayPanel.draw();
    }

    /**
     * Writes all memos in the list to a chosen output file.
     *
     * @throws IOException if the file cannot be written
     */
    private void writeMemos() throws IOException {
        File file = fileChooser.showSaveDialog(stage);
        if (file == null) {
            return;
        }

        FileIO.writeMemos(file, memoList);
        new Alert(Alert.AlertType.INFORMATION,
                "Memos were written to " + file.getName() + ".").showAndWait();
    }

    /**
     * Reads memos from a chosen file and merges them into the current list.
     *
     * @throws IOException if the file cannot be read
     */
    private void mergeMemos() throws IOException {
        File file = fileChooser.showOpenDialog(stage);
        if (file == null) {
            return;
        }

        memoList.merge(FileIO.readMemos(file));
        displayPanel.draw();
    }

    /**
     * Prompts for a memoID and a new location, then moves that memo.
     *
     * @throws IllegalArgumentException if the new location does not fit in
     *                                  the window
     */
    private void moveMemo() {
        TextField idField = new TextField();
        idField.setPromptText("Memo ID");
        TextField xField = new TextField();
        xField.setPromptText("New X-Position");
        TextField yField = new TextField();
        yField.setPromptText("New Y-Position");

        if (!showDialog("Move Memo", "OK", new Label("Enter Memo ID to Move:"),
                idField, xField, yField)) {
            return;
        }

        int memoID = Integer.parseInt(idField.getText().trim());
        int x = Integer.parseInt(xField.getText().trim());
        int y = Integer.parseInt(yField.getText().trim());
        checkBounds(x, y);

        if (!memoList.search(memoID)) {
            new Alert(Alert.AlertType.INFORMATION, "Memo " + memoID + " was not found.").showAndWait();
            return;
        }

        // Same memoID, so insertAfter replaces the memo in place.
        Memo memo = memoList.retrieve();
        memo.setX(x);
        memo.setY(y);
        memoList.insertAfter(memo);
        displayPanel.draw();
    }

    /**
     * Deletes all memos in the list.
     */
    private void clearMemos() {
        memoList.clear();
        displayPanel.draw();
    }

    /**
     * Exits the application.
     */
    private void quit() {
        Platform.exit();
    }

    /**
     * Shows a pop-up window containing the given labels and text fields.
     *
     * @param title  the title of the pop-up window
     * @param okText the text of the confirm button
     * @param nodes  the labels and text fields to show, top to bottom
     * @return true if the user clicked the confirm button
     */
    private boolean showDialog(String title, String okText, Node... nodes) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(stage);
        dialog.setTitle(title);

        ButtonType okButton = new ButtonType(okText, ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, ButtonType.CANCEL);
        dialog.getDialogPane().setContent(new VBox(4, nodes));

        return dialog.showAndWait().orElse(ButtonType.CANCEL) == okButton;
    }

    /**
     * Makes sure a memo placed at (x, y) fits within the message board.
     *
     * @param x the x-coordinate of the memo's upper left corner
     * @param y the y-coordinate of the memo's upper left corner
     * @throws IllegalArgumentException if the memo would not fit
     */
    private void checkBounds(int x, int y) {
        int maxX = BOARD_WIDTH - MemoDisplayPanel.MEMO_WIDTH;
        int maxY = BOARD_HEIGHT - MemoDisplayPanel.MEMO_HEIGHT;
        if (x < 0 || y < 0 || x > maxX || y > maxY) {
            throw new IllegalArgumentException("Memo at (" + x + ", " + y
                    + ") does not fit in the window. X must be 0-" + maxX
                    + " and Y must be 0-" + maxY + ".");
        }
    }
}
