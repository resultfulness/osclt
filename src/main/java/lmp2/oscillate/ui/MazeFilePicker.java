package lmp2.oscillate.ui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

import lmp2.oscillate.App;

public class MazeFilePicker extends JPanel implements ActionListener {
    private String fileInput = "";
    private boolean isInputBinary = false;

    private JButton openFilePickerButton;
    private JCheckBox isInputBinaryCheckbox;
    private JLabel fileNameLabel;

    private AppWindow appWindow;

    public MazeFilePicker(AppWindow appWindow) {
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.appWindow = appWindow;

        initComponent();

        // this.add(createInputFieldArea());
        this.add(this.isInputBinaryCheckbox);
        this.add(createFilePickerArea());
    }

    private void initComponent() { 
        this.isInputBinaryCheckbox = new JCheckBox("Is input binary?");
        this.isInputBinaryCheckbox.setSelected(App.config.getIsInputFileBinary());
        this.isInputBinaryCheckbox.addActionListener(this);
    }
    
    private JPanel createFilePickerArea() {
        JPanel filePickerArea = new JPanel(new FlowLayout());

        this.fileNameLabel = new JLabel(
            "Selected file: `" + App.config.getInputFilePath() + "`"
        );
        filePickerArea.add(this.fileNameLabel);

        this.openFilePickerButton = new JButton("Choose file...");
        this.openFilePickerButton.addActionListener(this);
        filePickerArea.add(this.openFilePickerButton);

        return filePickerArea;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if(event.getSource() == this.isInputBinaryCheckbox) {
            App.config.setInputFileBinary(this.isInputBinaryCheckbox.isSelected());
        }
        if(event.getSource() == this.openFilePickerButton) {
            var fileChooser = new JFileChooser();
            File workingDirectory = new File(System.getProperty("user.dir"));
            fileChooser.setCurrentDirectory(workingDirectory);
            int ret = fileChooser.showOpenDialog(null);

            if (ret == JFileChooser.APPROVE_OPTION) {
                this.fileInput = fileChooser.getSelectedFile().getPath();
                this.fileNameLabel.setText(fileChooser.getSelectedFile().getPath());
                this.appWindow.loadMaze(this.fileInput, this.isInputBinary);
            }
        }
    }
}
