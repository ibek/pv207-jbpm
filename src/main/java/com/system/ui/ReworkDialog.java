package com.system.ui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import com.system.data.Document;

public class ReworkDialog {

    private String title;

    public ReworkDialog(String title) {
        this.title = title;
    }

    public static interface CompleteHandler {
        public void complete();
    }

    public void show(final Document doc, final CompleteHandler handler) {
        final JFrame jFrame = new JFrame(title);
        jFrame.setLocationByPlatform(true);
        jFrame.setSize(650, 500);
        jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                handler.complete();
            }
        });
        final JPanel panel = new JPanel(new FlowLayout());
        final JEditorPane textdoc = new JEditorPane();
        textdoc.setText(doc.getContent());
        textdoc.setSize(640, 480);
        final JButton confirm = new JButton("Confirm");
        confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doc.setContent(textdoc.getText());
                jFrame.dispose();
                handler.complete();
            }
        });
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jFrame.dispose();
                handler.complete();
            }
        });
        panel.add(textdoc);
        panel.add(confirm);
        panel.add(cancel);
        jFrame.add(panel);
        jFrame.setVisible(true);
    }

}
