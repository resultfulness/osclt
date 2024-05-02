package lmp2.oscillate.ui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class MazeInputField extends JPanel implements ActionListener, DocumentListener {
    private String fileInput = "";
    private boolean isInputBinary = false;

    private JLabel inputFieldLabel;
    private JTextField inputField;
    private JButton loadButton;
    private JCheckBox isInputBinaryCheckbox;

    private AppWindow appWindow;

    public MazeInputField(AppWindow appWindow) {
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.appWindow = appWindow;

        initComponent();

        this.add(createInputFieldArea());
        this.add(isInputBinaryCheckbox);
    }

    private void initComponent() { 
        this.isInputBinaryCheckbox = new JCheckBox("Is input binary");
        this.isInputBinaryCheckbox.addActionListener(this);
    }

    private JPanel createInputFieldArea() { 
        JPanel inputFieldArea = new JPanel(new FlowLayout());

        this.inputFieldLabel = new JLabel();
        this.inputFieldLabel.setHorizontalAlignment(JLabel.LEFT);
        this.inputFieldLabel.setText("Enter file input path: ");

        this.inputField = new JTextField();
        this.inputField.setPreferredSize(
            new Dimension(200, this.inputField.getPreferredSize().height));
        this.inputField.getDocument().addDocumentListener(this);
        
        this.loadButton = new JButton("Load from file");
        this.loadButton.addActionListener(this);

        inputFieldArea.add(this.inputFieldLabel);
        inputFieldArea.add(this.inputField);
        inputFieldArea.add(this.loadButton);

        return inputFieldArea;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if(event.getSource() == this.inputField)
            this.fileInput = inputField.getText();
        if(event.getSource() == this.loadButton)
            this.appWindow.loadMaze(this.fileInput, this.isInputBinary);
        if(event.getSource() == this.isInputBinaryCheckbox)
            this.isInputBinary = this.isInputBinaryCheckbox.isSelected();
    }

    @Override
    public void changedUpdate(DocumentEvent arg0) {
        this.fileInput = inputField.getText();
    }

    @Override
    public void insertUpdate(DocumentEvent arg0) {
        this.fileInput = inputField.getText();
    }

    @Override
    public void removeUpdate(DocumentEvent arg0) {
        this.fileInput = inputField.getText();
    }
}