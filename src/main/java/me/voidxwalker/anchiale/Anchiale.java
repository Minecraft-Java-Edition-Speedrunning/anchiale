package me.voidxwalker.anchiale;

import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.*;

import java.io.IOException;
import java.nio.file.*;

public class Anchiale {
    public static final Logger LOGGER = LogManager.getLogger();
    public static boolean fastReset = false;
    public static ButtonLocation buttonLocation = ButtonLocation.BOTTOM_RIGHT;
    public static Path config = FabricLoader.getInstance().getConfigDir().resolve("anchiale.txt");

    static {
        try {
            if (Files.notExists(config)) {
                Files.createFile(config);
                saveConfig();
            }
            int ordinal = Integer.parseInt(new String(Files.readAllBytes(config)));
            ordinal = Math.max(0, Math.min(ButtonLocation.values().length - 1, ordinal));
            buttonLocation = ButtonLocation.values()[ordinal];
        } catch (Exception e) {
            LOGGER.error("Failed to create/read Anchiale config");
            e.printStackTrace();
        }
    }

    public static void saveConfig() {
        try {
            Files.write(config, ("" + buttonLocation.ordinal()).getBytes());
        } catch (IOException e) {
            LOGGER.error("Failed to write Anchiale config");
            e.printStackTrace();
        }
    }

    public enum ButtonLocation {
        BOTTOM_RIGHT("Bottom Right"),
        CENTER("Center");

        private final String name;

        ButtonLocation(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }
}
