package koyami.nekochan.util;

import org.json.JSONArray;
import org.json.JSONObject;

//TODO: https://testbooru.donmai.us/posts.json?tags=yuri programming
public class Danbooru {
    //https://testbooru.donmai.us/posts/random.json?tags=yuri+rating:e&only=file_url,rating,md5,id&search[order]=custom
    public final static char SAFE = 'g';
    public final static char QUESTIONABLE = 'q';
    public final static char EXPLICIT = 'e';
    public final static char NONE = ' ';
    private static JSONObject startRequest(String tag, char rating) {
        try {
            String baseurl = "https://danbooru.donmai.us/posts/random.json?tags=%s%s";
            String rate = "";
            if (rating != ' ') rate = "&"+rating;
            baseurl = String.format(baseurl,tag, rate,Util.random(4242));
            //Logger.logDebug(baseurl);
            String request = Util.readHTML(baseurl);
            //Logger.logDebug(request);
            if (request.startsWith("{\"success\":false")) return new JSONObject("{}");
            else return new JSONObject(request);
        } catch (Exception ignored) {
            return new JSONObject("{}");
        }
    }

    public static String[] getYuri() {
        return getRandomPicture("yuri+2girls");
    }

    //TODO
    public static String[] tagOptions(String name,int limit) {
        String baseurl = "https://testbooru.donmai.us/tag.json?name=%s&limit=%d";
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

    public static String[] getRandomPicture(String tag) {
        JSONObject object = startRequest(tag,NONE);
        if (object.isEmpty()) return new String[]{};
        return getDataFromJSONObject(object);
    }

    public static JSONObject getRandomSFWPicture(String tag) {
        return startRequest(tag,SAFE);
    }

    public static JSONObject getRandomNSFWPicture(String tag) {
        int ratingchosser = Util.random(1);
        JSONObject object ;
        if (ratingchosser == 0) object = startRequest(tag,EXPLICIT);
        else object = startRequest(tag,QUESTIONABLE);
        return object;
    }

    public static String[] getRandomWithRatingPicture(String tag, char rating) {
        JSONObject object = startRequest(tag,rating);
        if (object.isEmpty()) return new String[]{};
        //.getJSONObject(5).getString("url");
        return getDataFromJSONObject(object);
    }

    private static String[] getDataFromJSONObject(JSONObject object) {
        /*int i = 0;
        while (true) {
            try {
                JSONObject object2 = object.getJSONObject("media_asset").getJSONArray("variants").getJSONObject(i);
                if (object2.getString("type") == "original") {
                    return object2.getString("url");
                }
                i++;
            } catch (Exception ignored) {
                break;
            }
        }*/
        //file url, rating, file name
        try {
        return new String[]{object.getString("file_url"), object.getString("rating") ,object.getString("md5")};
        } catch (Exception e) {
            return new String[]{};
        }
    }
}