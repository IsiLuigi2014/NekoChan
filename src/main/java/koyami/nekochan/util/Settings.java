package koyami.nekochan.util;

import org.json.JSONObject;

import java.io.IOException;

public class Settings {
    //TODO: Everything put in a DataBase UwU
    public static String prefix = "-";
    //public static String animenewschannel = "anime-news";
    public static String lastnewsanime = null;
    //public static String manganewschannel = "anime-news";
    public static String lastnewsmanga = null;

    private static String filepath = "./settings.json";

    public static String getToken() {
        return (String) getValue("token");
    }

    public static boolean setToken(Object input, boolean force) {
        return putValue("token",input,force);
    }

    public static String getAnimeNewsChannel() {
        return (String) getValue("anime-news-channel");
    }

    public static boolean setAnimeNewsChannel(Object input, boolean force) {
        return putValue("anime-news-channel",input,force);
    }

    public static String getMangaNewsChannel() {
        return (String) getValue("manga-news-channel");
    }

    public static boolean setMangaNewsChannel(Object input, boolean force) {
        return putValue("manga-news-channel",input,force);
    }

    private static Object getValue(String key) {
        try {
            JSONObject object = new JSONObject(Util.readFile(filepath));
            if (object.keySet().contains(key)) {
                return object.get(key);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private static boolean putValue(String key, Object value, boolean force) {
        try {
            JSONObject object = new JSONObject(Util.readFile(filepath));
            if (object.keySet().contains(key) || force) {
                object.put(key,value);
                Util.writeFile(object.toString(),filepath);
                return true;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
//TODO: Create file with argument change bottoken + automatic bottoken Prompt
    public static void createFile(String bottoken, String animenewschannel, String manganewschannel) {
        try {
            Util.writeFile(String.format("{\"token\":\"%s\"," +
                    "\"anime-news-channel\":\"%s\"," +
                    "\"manga-news-channel\":\"%s\"}",bottoken, animenewschannel, manganewschannel)
                    ,filepath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void start(String bottoken) {
        if (!bottoken.isEmpty()) {
            Logger.logInfo("Resived new Bottoken - Would you like to replace it? (y/n)");
            String s = Util.getConsoleInput();
            switch (s) {
                case "n":
                case "no":
                case "nein":
                    bottoken = "";
                    break;

                case "y":
                case "yes":
                case "ja":
                    setToken(bottoken,true);
                    break;
            }
        }

        Logger.logInfo("Checking settings.json");
        if (!Util.fileExist(filepath)) createSettings(bottoken);
        Logger.logInfo("Settings.json Check done!");
    }

    private static void createSettings(String bottoken) {
        Logger.logWarning("Settings.json not existing, Creating Settings file...");
        if (bottoken.isEmpty()) {
            Logger.logInfo("Please enter below a valid bottoken so Nekochan can live uwu:");
            bottoken = Util.getConsoleInput();
        }
        createFile(bottoken,"anime-news","anime-news");
        Logger.logInfo("Created Settings.json:");
    }
}
