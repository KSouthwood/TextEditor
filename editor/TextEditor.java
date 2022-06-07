package editor;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileSystemView;
import javax.swing.plaf.basic.BasicBorders;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextEditor extends JFrame {

    JTextArea    textArea;
    JTextField   searchField;
    JFileChooser fileChooser;
    private JButton   loadButton;
    private JButton   saveButton;
    private JButton   searchButton;
    private JButton   previousButton;
    private JButton   nextButton;
    private JCheckBox useRegex;

    private final String        filepath                  = "/projects/JetBrains Academy (Java)/Text Editor/Text " +
                                                            "Editor/task/src/editor/images/";
    private final String        openIconFilename          = filepath + "8664907_folder_open_document_icon.png";
    private final String        saveIconFilename          = filepath + "3671850_disk_save_icon.png";
    private final String        searchIconFilename        = filepath + "172546_search_icon.png";
    private final String        previousMatchIconFilename =
            filepath + "476327_arrow_circle_left_prev_previous_icon.png";
    private final String        nextMatchIconFilename     = filepath + "4829872_arrow_next_right_icon.png";
    private final int           ICON_WIDTH                = 25;
    private final int           ICON_HEIGHT               = 25;
    private final int           BUTTON_WIDTH              = 30;
    private final int           BUTTON_HEIGHT             = 30;
    private       PerformSearch search;

    public TextEditor() {
        setWindowProperties();
        addComponents();
        addOpenListener(new LoadAction());
        addSaveListener(new SaveAction());
        this.setVisible(true);
    }

    private void setWindowProperties() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(600, 500);
        this.setTitle("Text Editor");
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
    }

    private void addComponents() {
        this.setJMenuBar(createMenuBar());
        defineFileChooser();
        this.add(fileChooser);
        this.add(createToolBar(), BorderLayout.NORTH);
        this.add(createTextPane(), BorderLayout.CENTER);
    }

    private void defineFileChooser() {
        fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        fileChooser.setName("FileChooser");
    }

    private JMenuBar createMenuBar() {
        JMenuBar jMenuBar = new JMenuBar();
        jMenuBar.setMargin(new Insets(10, 10, 10, 10));
        jMenuBar.add(createFileMenu());
        jMenuBar.add(createSearchMenu());
        return jMenuBar;
    }

    private JMenu createFileMenu() {
        JMenu fileMenu = new JMenu();
        fileMenu.setText("File");
        fileMenu.setName("MenuFile");

        fileMenu.add(createMenuItem("Open", "MenuOpen", e -> loadButton.doClick()));
        fileMenu.add(createMenuItem("Save", "MenuSave", e -> saveButton.doClick()));
        fileMenu.addSeparator();
        fileMenu.add(createMenuItem("Exit", "MenuExit", e -> System.exit(0)));
        return fileMenu;
    }

    private JMenu createSearchMenu() {
        JMenu searchMenu = new JMenu();
        searchMenu.setText("Search");
        searchMenu.setName("MenuSearch");

        searchMenu.add(createMenuItem("Start search", "MenuStartSearch", e -> searchButton.doClick()));
        searchMenu.add(createMenuItem("Previous search", "MenuPreviousMatch",
                                      e -> previousButton.doClick()));
        searchMenu.add(createMenuItem("Next Match", "MenuNextMatch", e -> nextButton.doClick()));
        searchMenu.add(createMenuItem("Use regular expressions", "MenuUseRegExp",
                                      e -> useRegex.doClick()));
        return searchMenu;
    }

    private JMenuItem createMenuItem(final String text, final String name, final ActionListener action) {
        JMenuItem menuItem = new JMenuItem(text);
        menuItem.setName(name);
        menuItem.addActionListener(action);
        return menuItem;
    }

    private JScrollPane createTextPane() {
        JScrollPane textArea = new JScrollPane(createTextArea());
        textArea.setName("ScrollPane");
        textArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        return textArea;
    }

    private JTextArea createTextArea() {
        textArea = new JTextArea();
        textArea.setName("TextArea");
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBorder(BasicBorders.getTextFieldBorder());
        return textArea;
    }

    private JPanel createToolBar() {
        JPanel toolbar = new JPanel();
        toolbar.setLayout(new FlowLayout());
        toolbar.setBorder(new EmptyBorder(10, 10, 10, 10));
        toolbar.add(createLoadButton());
        toolbar.add(createSaveButton());
        toolbar.add(createSearchField());
        toolbar.add(createSearchButton());
        toolbar.add(createPreviousMatchButton());
        toolbar.add(createNextMatchButton());
        toolbar.add(createUseRegexCheckbox());

        return toolbar;
    }

    private JTextField createSearchField() {
        searchField = new JTextField();
        searchField.setName("SearchField");
        searchField.setPreferredSize(new Dimension(250, 30));
        searchField.setBorder(BasicBorders.getTextFieldBorder());
        return searchField;
    }

    private JButton createLoadButton() {

        loadButton = new JButton();
        loadButton.setName("OpenButton");
        loadButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        try {
            var icon = ImageIO.read(new File(openIconFilename))
                              .getScaledInstance(ICON_WIDTH, ICON_HEIGHT, Image.SCALE_DEFAULT);
            loadButton.setIcon(new ImageIcon(icon));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return loadButton;
    }

    private JButton createSaveButton() {
        saveButton = new JButton();
        saveButton.setName("SaveButton");
        saveButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        try {
            var icon = ImageIO.read(new File(saveIconFilename))
                              .getScaledInstance(ICON_WIDTH, ICON_HEIGHT, Image.SCALE_DEFAULT);
            saveButton.setIcon(new ImageIcon(icon));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return saveButton;
    }

    private JButton createSearchButton() {
        searchButton = new JButton();
        searchButton.setName("StartSearchButton");
        searchButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        searchButton.addActionListener(new Search());

        try {
            searchButton.setIcon(new ImageIcon(ImageIO.read(new File(searchIconFilename))
                                                      .getScaledInstance(ICON_WIDTH, ICON_HEIGHT,
                                                                         Image.SCALE_DEFAULT)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return searchButton;
    }

    private JButton createPreviousMatchButton() {
        previousButton = new JButton();
        previousButton.setName("PreviousMatchButton");
        previousButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        previousButton.addActionListener(e -> search.previousSearchResult());

        try {
            previousButton.setIcon(new ImageIcon(ImageIO.read(new File(previousMatchIconFilename))
                                                        .getScaledInstance(ICON_WIDTH, ICON_HEIGHT,
                                                                           Image.SCALE_DEFAULT)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return previousButton;
    }

    private JButton createNextMatchButton() {
        nextButton = new JButton();
        nextButton.setName("NextMatchButton");
        nextButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        nextButton.addActionListener(e -> search.nextSearchResult());

        try {
            nextButton.setIcon(new ImageIcon(ImageIO.read(new File(nextMatchIconFilename))
                                                    .getScaledInstance(ICON_WIDTH, ICON_HEIGHT, Image.SCALE_DEFAULT)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return nextButton;
    }

    private JCheckBox createUseRegexCheckbox() {
        useRegex = new JCheckBox("Use regex", false);
        useRegex.setName("UseRegExCheckbox");
        return useRegex;
    }

    void addOpenListener(final ActionListener actionListener) {
        loadButton.addActionListener(actionListener);
    }

    void addSaveListener(final ActionListener actionListener) {
        saveButton.addActionListener(actionListener);
    }

    String getSearchText() {
        return searchField.getText();
    }

    class LoadAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            var result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                try (BufferedReader file = new BufferedReader(new FileReader(fileChooser.getSelectedFile()))) {
                    textArea.read(file, null);
                } catch (IOException exception) {
                    System.out.println("Error: " + exception);
                    textArea.setText("");
                }
            }
        }
    }

    class SaveAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            var result = fileChooser.showSaveDialog(null);

            if (result == JFileChooser.APPROVE_OPTION) {
                try (BufferedWriter file = new BufferedWriter(new FileWriter(fileChooser.getSelectedFile()))) {
                    textArea.write(file);
                } catch (IOException exception) {
                    System.out.println("Error: " + exception);
                }
            }
        }
    }

    class Search implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            search = new PerformSearch(textArea, getSearchText(), useRegex.isSelected());
            search.execute();
        }
    }

    static class PerformSearch extends SwingWorker<Void, MatchPair> {
        private       int                  matchIndex     = 0;
        private final boolean              useRegex;
        private final String               searchTerm;
        private final String               searchText;
        private final ArrayList<MatchPair> matchesFound   = new ArrayList<>();
        private final JTextArea searchTextArea;

        PerformSearch(final JTextArea textArea, final String searchTerm, final boolean regex) {
            this.searchTextArea = textArea;
            this.searchText = textArea.getText();
            this.searchTerm = searchTerm;
            this.useRegex = regex;
            setProgress(0);
        }

        @Override
        public Void doInBackground() {
            Matcher matcher;
            if (!useRegex) {
                matcher = Pattern.compile(searchTerm, Pattern.LITERAL).matcher(searchText);
            } else {
                matcher = Pattern.compile(searchTerm).matcher(searchText);
            }

            while (matcher.find()) {
                publish(new MatchPair(matcher.start(), matcher.end()));
                setProgress(100 * matcher.start() / searchText.length());
            }

            setProgress(100);

            return null;
        }

        @Override
        protected void process(List<MatchPair> chunks) {
            matchesFound.addAll(chunks);
        }

        @Override
        protected void done() {
            if (matchesFound.size() > 0) {
                highlightMatch();
            }
        }

        void nextSearchResult() {
            if (matchesFound.size() != 0) {
                matchIndex++;
                if (matchIndex == matchesFound.size()) {
                    matchIndex = 0;
                }
                highlightMatch();
            }
        }

        void previousSearchResult() {
            if (matchesFound.size() != 0)
            {
                matchIndex--;
                if (matchIndex < 0) {
                    matchIndex = matchesFound.size() - 1;
                }
                highlightMatch();
            }
        }

        private void highlightMatch() {
            var textToHighlight = matchesFound.get(matchIndex);
            searchTextArea.setCaretPosition(textToHighlight.start);
            searchTextArea.moveCaretPosition(textToHighlight.end);
            searchTextArea.grabFocus();
        }
    }

    static class MatchPair {
        int start;
        int end;

        MatchPair(final int start, final int end) {
            this.start = start;
            this.end = end;
        }
    }

}
