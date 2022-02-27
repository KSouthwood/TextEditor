package editor;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicBorders;
import java.awt.*;
import java.io.*;

public class TextEditor extends JFrame {

    private JTextArea  textArea;
    private JTextField filename;

    public TextEditor() {
        setWindowProperties();
        addComponents();
        setVisible(true);
    }

    private void setWindowProperties() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 450);
        setTitle("Text Editor");
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    private void addComponents() {
        add(createPanel(), BorderLayout.NORTH);
        add(createScrollPane(), BorderLayout.CENTER);
    }

    private JScrollPane createScrollPane() {
        JScrollPane jScrollPane = new JScrollPane(createTextArea());
        jScrollPane.setName("ScrollPane");
        jScrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        return jScrollPane;
    }

    private JTextArea createTextArea() {
        textArea = new JTextArea();
        textArea.setName("TextArea");
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBorder(BasicBorders.getTextFieldBorder());
        return textArea;
    }

    private JPanel createPanel() {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout());
        jPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        jPanel.add(createJTextField());
        jPanel.add(createLoadButton());
        jPanel.add(createSaveButton());
        return jPanel;
    }

    private JTextField createJTextField() {
        filename = new JTextField();
        filename.setName("FilenameField");
        filename.setPreferredSize(new Dimension(250, 30));
        filename.setBorder(BasicBorders.getTextFieldBorder());
        return filename;
    }

    private JButton createLoadButton() {
        JButton loadButton = new JButton("Load");
        loadButton.setName("LoadButton");
        loadButton.setPreferredSize(new Dimension(75, 30));

        loadButton.addActionListener(actionEvent -> {
            try (BufferedReader file = new BufferedReader(new FileReader(filename.getText()))) {
                textArea.read(file, null);
            } catch (IOException e) {
                System.out.println("Error: "  + e);
                textArea.setText("");
            }
        });

        return loadButton;
    }

    private JButton createSaveButton() {
        JButton saveButton = new JButton("Save");
        saveButton.setName("SaveButton");
        saveButton.setPreferredSize(new Dimension(75, 30));

        saveButton.addActionListener(actionEvent -> {
            try (BufferedWriter file = new BufferedWriter(new FileWriter(filename.getText()))) {
                textArea.write(file);
            } catch (IOException e) {
                System.out.println("Error: " + e);
            }
        });

        return saveButton;
    }
}