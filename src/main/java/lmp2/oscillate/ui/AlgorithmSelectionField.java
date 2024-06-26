package lmp2.oscillate.ui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import lmp2.oscillate.Maze_InputFormat;


public class AlgorithmSelectionField extends JPanel implements ActionListener {
    public static enum Algorithm {
        DFS, BFS, AStarEuclidian, AStarManhattan
    };
    private Algorithm selectedAlg = Algorithm.DFS;
    private Maze_InputFormat maze_inputFormat;
    private AppWindow appWindow;

    private JComboBox<Algorithm> algorithmSelectorField;

    public AlgorithmSelectionField(Maze_InputFormat maze_inputFormat, AppWindow appWindow) {
        super(); 
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.maze_inputFormat = maze_inputFormat;
        this.appWindow = appWindow;
        this.initComponent();
    }

    private void initComponent() {
        JLabel algorithmSelectorLabel = new JLabel();
        algorithmSelectorLabel.setText("Select algorithm used for solve");
        algorithmSelectorLabel.setHorizontalAlignment(JLabel.LEFT);

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
        if (event.getSource() == this.algorithmSelectorField){
            selectedAlg = (Algorithm) this.algorithmSelectorField.getSelectedItem();
            this.maze_inputFormat.clearSolution();
            this.appWindow.stopSolve();
        }
    }
}
