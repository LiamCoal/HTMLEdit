package com.liamcoalstudio.htmledit;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Vector;

public class MainGUI extends JFrame {
    public class Element {
        public String name, text;

        public Element(String name, String text) {
            this.name = name;
            this.text = text;
        }

        public String toString() {
            return name;
        }
    }

    private JPanel panel1;
    private JEditorPane editorPane1;
    private JButton boldButton;
    private JButton italicsButton;
    private JButton underlineButton;
    private JButton linkButton;
    private JButton saveButton;
    private JButton openButton;
    private JTextField textField1;
    private String currentFile;

    public MainGUI() {
        super("HTMLEdit");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        currentFile = "";
        editorPane1.setText(currentFile);
        editorPane1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                currentFile = editorPane1.getText();
                int bytes = currentFile.length();
                int hashcode = currentFile.hashCode();
                currentFile = currentFile.replace("\t", "    ");
                currentFile = currentFile.replace("\r\n", "\n");
                if(bytes != currentFile.length() || hashcode != currentFile.hashCode()) {
                    int orig = editorPane1.getCaretPosition();
                    editorPane1.setText(currentFile);
                    editorPane1.setCaretPosition(orig+(currentFile.length() - bytes));
                }
            }
        });
        boldButton.addActionListener(e -> insertAtCursor("<b></b>", 3));
        italicsButton.addActionListener(e -> insertAtCursor("<i></i>", 3));
        underlineButton.addActionListener(e -> insertAtCursor("<u></u>", 3));
        linkButton.addActionListener(e -> insertAtCursor("<a href=>Click me!</a>", 8));
        saveButton.addActionListener(e -> {
            try {
                Files.writeString(Path.of(textField1.getText()), currentFile);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        openButton.addActionListener(e -> {
            try {
                currentFile = new String(Files.readAllBytes(Path.of(textField1.getText())));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        JMenuBar menuBar = new JMenuBar();
        JMenu generate = new JMenu("Generate");
        menuBar.add(generate);

        for(Generators.Generator g : Generators.generators) {
            if(g.group) {
                JMenu submenu = new JMenu(g.name);
                for(Generators.Generator g2 : g.array) {
                    JMenuItem item = new JMenuItem(g2.name);
                    item.addActionListener(e -> insertAtCursor(g2.text, g2.offset));
                    item.setToolTipText(g2.text);
                    submenu.add(item);
                }
                generate.add(submenu);
            } else {
                JMenuItem item = new JMenuItem(g.name);
                item.addActionListener(e -> insertAtCursor(g.text, g.offset));
                item.setToolTipText(g.text);
                generate.add(item);
            }
        }

        setJMenuBar(menuBar);

        add(panel1);
    }

    void insertAtCursor(String s, int off) {
        int orig = editorPane1.getCaret().getDot();
        String first = currentFile.substring(0, orig);
        String second = currentFile.substring(orig);
        currentFile = first + s + second;
        editorPane1.setText(currentFile);
        editorPane1.setCaretPosition(orig+off);
        editorPane1.requestFocus();
    }

    String getCurrentFile() { return currentFile; }
}
