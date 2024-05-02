package lmp2.oscillate.ui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

import lmp2.oscillate.App;

public class MazeFilePicker extends JPanel implements ActionListener {
    private JButton openFilePickerButton;
    private JCheckBox isInputBinaryCheckbox;
    private JLabel fileNameLabel;

    private AppWindow appWindow;

    public MazeFilePicker(AppWindow appWindow) {
        super();
        this.setLayout(new GridLayout(3, 1));
        this.appWindow = appWindow;

        this.initComponent();

        this.add(this.isInputBinaryCheckbox);
        this.fileNameLabel = new JLabel();
        this.add(this.fileNameLabel);
        this.updateLabels();

        this.openFilePickerButton = new JButton("Choose file...");
        this.openFilePickerButton.addActionListener(this);
        this.add(this.openFilePickerButton);
    }

    private void initComponent() { 
        this.isInputBinaryCheckbox = new JCheckBox("Is input binary?");
        this.isInputBinaryCheckbox.addActionListener(this);
    }

    private void updateLabels() {
        String fullPath = App.config.getInputFilePath();
        String path = fullPath;
        // so the path length doesn't get out of control
        int maxChars = 40;
        if (fullPath.length() > maxChars) {
            String dots = "...";
            int toShow = maxChars - dots.length();

            path = new StringBuilder()
                .append(fullPath, 0, toShow / 2)
                .append(dots)
                .append(
                    fullPath,
                    fullPath.length() - toShow / 2,
                    fullPath.length())
                .toString();
        }

        this.fileNameLabel.setText(
            "Selected file: `" + path + "`"
        );
        this.isInputBinaryCheckbox.setSelected(
            App.config.getIsInputFileBinary()
        );
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if(event.getSource() == this.isInputBinaryCheckbox) {
            App.config.setInputFileBinary(
                this.isInputBinaryCheckbox.isSelected()
            );
        }
        if(event.getSource() == this.openFilePickerButton) {
            JFileChooser fileChooser = new JFileChooser();
            File workingDirectory = new File(System.getProperty("user.dir"));
            fileChooser.setCurrentDirectory(workingDirectory);
            int ret = fileChooser.showOpenDialog(null);

            if (ret == JFileChooser.APPROVE_OPTION) {
                App.config.setInputFilePath(
                    fileChooser.getSelectedFile().getPath()
                );

                // because users are stupid
                App.config.setInputFileBinary(
                    App.config.getInputFilePath().endsWith(".bin")
                );

                this.updateLabels();
                this.appWindow.loadMaze(
                    App.config.getInputFilePath(),
                    App.config.getIsInputFileBinary()
                );
            }
        }
    }
}
