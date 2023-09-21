package koyami.nekochan.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Logger {
    //ANSI escape codes: https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static boolean writeToFile = true;
    private static final String filepath = "./log.txt";

    public static void logDebug(String message) {
        log("DEBUG", ANSI_WHITE, message);
    }

    public static void logInfo(String message) {
        log("INFO", "", message);
    }

    public static void logWarning(String message) {
        log("WARN", ANSI_YELLOW, message);
    }

    public static void logError(String message) {
        log("ERROR", ANSI_RED, message);
    }

    public static void logSpecial(String message) {
        log("INFO", ANSI_PURPLE, message);
    }

    private static void log(String type, String color, String message) {
        String calledClass = Thread.currentThread().getStackTrace()[3].getClassName();
        String datetime = Util.getTime();
        String out = String.format("%s[%s] %s %s - %s%s%n", color, datetime, calledClass, type, message, ANSI_RESET);
        System.out.print(out);
        if (writeToFile) {
            if (!Util.fileExist(filepath)) {
                try {
                    Util.writeFile("", filepath);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            try {
                Files.write(Paths.get(filepath), out.getBytes(), StandardOpenOption.APPEND);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
