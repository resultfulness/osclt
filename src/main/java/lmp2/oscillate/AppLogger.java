package lmp2.oscillate;

import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class AppLogger {
    public static Logger getLogger() {
        Logger logger = Logger.getAnonymousLogger();
        logger.setUseParentHandlers(false);
        ConsoleHandler handler = new ConsoleHandler();
        Formatter formatter = new LogFormatter();
        handler.setFormatter(formatter);
        logger.addHandler(handler);
        return logger;
    }

    private static class LogFormatter extends Formatter {
        public static final String ANSI_RESET = "\u001B[0m";
        public static final String ANSI_BOLD = "\u001B[1m";
        public static final String ANSI_BLACK = "\u001B[30m";
        public static final String ANSI_RED = "\u001B[31m";
        public static final String ANSI_GREEN = "\u001B[32m";
        public static final String ANSI_YELLOW = "\u001B[33m";
        public static final String ANSI_BLUE = "\u001B[34m";
        public static final String ANSI_PURPLE = "\u001B[35m";
        public static final String ANSI_CYAN = "\u001B[36m";
        public static final String ANSI_WHITE = "\u001B[37m";

        @Override
        public String format(LogRecord record) {
            String l = record.getLevel().getName();
            return String.format(
                """
                [%s] (at %s) %s %s
                """,
                (
                    l == "INFO"
                    ? ANSI_BLUE
                    : l == "WARNING"
                    ? ANSI_YELLOW
                    : l == "SEVERE"
                    ? ANSI_RED
                    : ANSI_WHITE
                ) + ANSI_BOLD + l + ANSI_RESET,
                record.getSourceClassName(),
                record.getMessage(),
                record.getParameters() == null ? "" : record.getParameters()
            );
        }
    }
}
