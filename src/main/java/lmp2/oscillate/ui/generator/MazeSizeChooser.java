package lmp2.oscillate.ui.generator;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class MazeSizeChooser extends JPanel implements DocumentListener {
    private int mazeSizeValue = 0;
    private String labelHint;

    private JTextField valueInputField;

    public MazeSizeChooser(String labelHint) {
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.labelHint = labelHint;
        this.initComponent();
    }

    public void initComponent(){
        JLabel sizeLabel = new JLabel();
        sizeLabel.setText(labelHint);

        this.valueInputField = new JTextField();
        this.valueInputField.setPreferredSize(
            new Dimension(200, this.valueInputField.getPreferredSize().height)
        );
        this.valueInputField.getDocument().addDocumentListener(this);

        this.add(sizeLabel);
        this.add(this.valueInputField);
    }
    
    public int getMazeSizeValue() {
        return this.mazeSizeValue;
    }

    @Override
    public void changedUpdate(DocumentEvent arg0) {
        try{
            this.mazeSizeValue = Integer.parseInt(this.valueInputField.getText());
        } catch(Exception ex) {
        }
    }

    @Override
    public void insertUpdate(DocumentEvent arg0) {
        try{
            this.mazeSizeValue = Integer.parseInt(this.valueInputField.getText());
        } catch(Exception ex) {
        }
    }

    @Override
    public void removeUpdate(DocumentEvent arg0) {
        try{
            this.mazeSizeValue = Integer.parseInt(this.valueInputField.getText());
        } catch(Exception ex) {
        }
    }
}
