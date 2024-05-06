package lmp2.oscillate.ui;

import javax.swing.JOptionPane;

public class LogDialog extends JOptionPane {
    public static enum Level {
        INFO,
        WARN,
        ERROR,
        CRITICAL
    }

    public static void show(String msg, LogDialog.Level level) {
        String message;
        String title;
        int messageType;
        switch (level) {
            case INFO:
                message = msg;
                title = "Info";
                messageType = LogDialog.INFORMATION_MESSAGE;
                break;
            case WARN:
                message = "Warning: " + msg;
                title = "Warning";
                messageType = LogDialog.WARNING_MESSAGE;
                break;
            case ERROR:
                message = "Error: " + msg;
                title = "Error";
                messageType = LogDialog.ERROR_MESSAGE;
                break;
            case CRITICAL:
                message = "A critical error has occured: " +
                    msg + "; The program will now exit";
                title = "Unrecoverable error!";
                messageType = LogDialog.ERROR_MESSAGE;
                break;
            default:
                return;
        }

        LogDialog.showMessageDialog(null, message, title, messageType);
    }
}
