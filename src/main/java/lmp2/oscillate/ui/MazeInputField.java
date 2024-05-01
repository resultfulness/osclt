package lmp2.oscillate.ui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MazeInputField extends JPanel implements ActionListener {
    private String fileInput = "";
    private JLabel inputFieldLabel;
    private JTextField inputField;

    public MazeInputField() {
        super(new FlowLayout());

        this.inputFieldLabel = new JLabel();
        this.inputFieldLabel.setHorizontalAlignment(JLabel.LEFT);
        this.inputFieldLabel.setText("Enter file input path: ");

        this.inputField = new JTextField();
        this.inputField.setPreferredSize(
            new Dimension(200, this.inputField.getPreferredSize().height));
        this.inputField.addActionListener(this);

        this.add(this.inputFieldLabel);
        this.add(this.inputField);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        this.fileInput = inputField.getText();
    }

    public String getFileInputName() {
        return this.fileInput;
    }
}