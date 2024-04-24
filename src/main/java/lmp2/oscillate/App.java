package lmp2.oscillate;

import java.util.logging.Level;
import java.util.logging.Logger;

public class App {
    private static Config config;

    public static void main(String[] args) {
        Logger logger = AppLogger.getLogger();

        logger.log(Level.INFO, "app initialized");

        try {
            config = new Config(args);
        } catch (IllegalArgumentException e) {
            logger.log(Level.SEVERE, e.getMessage());
            System.exit(1);
        }
        logger.log(Level.INFO, "loaded config:\n" + config);
    }
}
