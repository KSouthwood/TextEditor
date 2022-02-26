package editor;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicBorders;
import java.awt.*;

public class TextEditor extends JFrame {

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
        JTextArea jTextArea = new JTextArea();
        jTextArea.setName("TextArea");
        jTextArea.setLineWrap(true);
        jTextArea.setWrapStyleWord(true);
        jTextArea.setBorder(BasicBorders.getTextFieldBorder());
        return jTextArea;
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
        JTextField jTextField = new JTextField();
        jTextField.setName("FilenameField");
        jTextField.setPreferredSize(new Dimension(250, 30));
        jTextField.setBorder(BasicBorders.getTextFieldBorder());
        return jTextField;
    }

    private JButton createLoadButton() {
        JButton loadButton = new JButton("Load");
        loadButton.setName("LoadButton");
        loadButton.setPreferredSize(new Dimension(75, 30));
        return loadButton;
    }

    private JButton createSaveButton() {
        JButton saveButton = new JButton("Save");
        saveButton.setName("SaveButton");
        saveButton.setPreferredSize(new Dimension(75, 30));
        return saveButton;
    }


}
