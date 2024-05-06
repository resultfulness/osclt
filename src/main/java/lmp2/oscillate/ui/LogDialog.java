package lmp2.oscillate.ui;

import javax.swing.JOptionPane;

public class LogDialog extends JOptionPane {
    public static void showMessage(String msg) {
        LogDialog.showMessageDialog(
            null, msg, "Info", LogDialog.INFORMATION_MESSAGE);
    }

    public static void showErrorMessage(String msg) {
        LogDialog.showMessageDialog(
            null,
            "A critical error occured: " +  msg,
            "Error!",
            LogDialog.ERROR_MESSAGE
        );
    }
}
