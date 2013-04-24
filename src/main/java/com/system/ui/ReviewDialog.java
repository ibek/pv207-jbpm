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

public class ReviewDialog {

    public static interface CompleteHandler {
        public void approve();

        public void reject();
    }

    private String title;

    public ReviewDialog(String title) {
        this.title = title;
    }

    public void show(final Document doc, final CompleteHandler handler) {
        final JFrame jFrame = new JFrame(title);
        jFrame.setLocationByPlatform(true);
        jFrame.setSize(650, 500);
        jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                handler.reject();
            }
        });
        final JPanel panel = new JPanel(new FlowLayout());
        final JEditorPane textdoc = new JEditorPane();
        textdoc.setText(doc.getContent());
        textdoc.setSize(640, 480);
        textdoc.setEditable(false);
        final JButton confirm = new JButton("Approve");
        confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doc.setContent(textdoc.getText());
                jFrame.dispose();
                handler.approve();
            }
        });
        JButton cancel = new JButton("Reject");
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jFrame.dispose();
                handler.reject();
            }
        });
        panel.add(textdoc);
        panel.add(confirm);
        panel.add(cancel);
        jFrame.add(panel);
        jFrame.setVisible(true);
    }

}
