package koyami.nekochan.util;

import org.json.JSONArray;
import org.json.JSONObject;

public class Konachan {
    public final static char SAFE = 's';
    public final static char QUESTIONABLE = 'q';
    public final static char EXPLICIT = 'e';
    public final static char NONE = ' ';
    private static JSONArray startRequest(String tag, char rating, int limit) {
        try {
            String baseurl = "https://konachan.com/post.json?tags=%s%%20%s%%20order%%3Arandom&limit=%d";
            baseurl = String.format(baseurl, rating,tag,limit);
            //Logger.logDebug(baseurl);
            String request = Util.readHTML(baseurl);
            if (request.equals("[]")) return new JSONArray("[{}]");
            else return new JSONArray(request);
        } catch (Exception ignored) {
            return new JSONArray("[{}]");
        }
    }

    public static String[] tagOptions(String name,int limit) {
        String baseurl = "https://konachan.com/tag.json?name=%s&limit=%d";
        baseurl = String.format(baseurl,name,limit);
        String response = Util.readHTML(baseurl);
        if (response.equals("[]") || response.startsWith("{") || response.isEmpty()) return new String[]{};
        else {
            JSONArray array = new JSONArray(response);
            int i = 0;
            String[] returner = new String[array.length()];
            do {
                returner[i] = array.getJSONObject(i).getString("name");
                i++;
            } while (i < array.length());
            return returner;
        }
    }

    private static JSONObject getRandomPicture(String tag) {
        return startRequest(tag,NONE,1).getJSONObject(0);
    }

    public static JSONObject getRandomSFWPicture(String tag) {
        JSONObject object = new JSONObject("");
        JSONArray array = startRequest(tag,SAFE,1);
        if (array.isEmpty()) return object;
        object = startRequest(tag,SAFE,1).getJSONObject(0);
        if (object.isEmpty()) return new JSONObject("{}");
        return object;
    }

    public static JSONObject getRandomNSFWPicture(String tag) {
        char rating = 'e';
        int ratingchosser = Util.random(1);
        if (ratingchosser == 1) rating = 'q';
        JSONObject object;
        object = startRequest(tag,EXPLICIT,1).getJSONObject(0);
        if (object.isEmpty()) return new JSONObject("{}");
        return object;
    }

    public static JSONObject getRandomWithRatingPicture(String tag, char rating) {
        JSONObject object;
        object = startRequest(tag,rating,1).getJSONObject(0);
        if (object.isEmpty()) return new JSONObject("{}");
        return object;
    }
}