package lmp2.oscillate.ui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AlgorithmSelectionField extends JPanel implements ActionListener {
    protected static enum Algorithm {
        DFS, BFS
    };
    private Algorithm selectedAlg = Algorithm.DFS;

    private JComboBox<Algorithm> algorithmSelectorField;

    public AlgorithmSelectionField() {
        super(new FlowLayout()); 
        this.initComponent();
    }

    private void initComponent() {
        JLabel algorithmSelectorLabel = new JLabel();
        algorithmSelectorLabel.setText("Select algorithm used for solve");
        
        this.algorithmSelectorField = new JComboBox<Algorithm>(Algorithm.values());
        this.algorithmSelectorField.addActionListener(this);

        this.add(algorithmSelectorLabel);
        this.add(algorithmSelectorField);
    }

    public Algorithm getSelectedAlgorithm() {
        return selectedAlg;
    }
    
    @Override
    public void actionPerformed(ActionEvent event) { 
        if (event.getSource() == this.algorithmSelectorField)
            selectedAlg = (Algorithm) this.algorithmSelectorField.getSelectedItem();
    }
}
