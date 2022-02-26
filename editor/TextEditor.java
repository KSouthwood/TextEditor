package editor;

import javax.swing.*;
import java.awt.*;

public class TextEditor extends JFrame {
    public TextEditor() {
        setWindowProperties();
        addComponents();
        setVisible(true);
    }

    private void setWindowProperties() {
         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         setSize(300, 300);
         setTitle("Text Editor");
         setLocationRelativeTo(null);
         setLayout(new BorderLayout());
     }

    private void addComponents() {
        add(createTextArea(), BorderLayout.CENTER);
    }

    private JTextArea createTextArea() {
        JTextArea jTextArea = new JTextArea();
        jTextArea.setName("TextArea");
        jTextArea.setLineWrap(true);
        jTextArea.setWrapStyleWord(true);
        return jTextArea;
    }
}
