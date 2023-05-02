package koyami.nekochan.util.jisho;

import koyami.nekochan.util.Util;
import org.json.JSONArray;
import org.json.JSONObject;

//TODO
public class Jisho {
    public static String mainURL = "https://jisho.org/api/v1/search/words?keyword=";

    public JSONArray array;
    public JSONObject object;
    public int size;
    public Jisho(String word) {
        this.object = searchWord(word);
        this.array = object.getJSONArray("data");
        this.size = array.length();

    }

    public JSONObject searchWord(String word) {
        return new JSONObject(Util.readHTML(mainURL + word));
    }

    public JSONObject getOneWord(int index) {
        if (index > size) return new JSONObject();
        return array.getJSONObject(index);
    }

    public JSONArray getJSONArray() {
        return array;
    }
    public JSONObject getJSONObject() {
        return object;
    }

    public int length() {
        return size;
    }


}
