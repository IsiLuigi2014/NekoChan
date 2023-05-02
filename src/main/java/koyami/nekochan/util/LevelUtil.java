package koyami.nekochan.util;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class LevelUtil {

    public static String mainPath = "./levelstats/";

    public static JSONObject calculateLevel(long[] data, TextChannel channel, Member member) {
        long level = data[0];
        long points = data[1];
        long pointsToNextLvl = data[2];

        points = Math.round(Math.random() * 4) + points;
        if (points >= pointsToNextLvl) {
            points = 0;
            pointsToNextLvl = Math.round(pointsToNextLvl * 1.08);
            level++;
            Util.sendMessage(channel, String.format("Glückwunsch, %s! Du hast so eben **Level %d** erreicht.", member.getUser().getAsMention(), level)).delete().completeAfter(5, TimeUnit.SECONDS);
        }
        return putIn(level, points, pointsToNextLvl);
    }

    public static JSONObject putIn(long level, long points, long pointsToNextLvl) {
        JSONObject object = new JSONObject();
        object.put("level", level);
        object.put("points", points);
        object.put("pointsToNextLvl", pointsToNextLvl);
        return object;
    }

    public static long[] readUserLvl(long userID) {
        String userFile = String.format("%s%s.json", mainPath, userID);
        JSONObject object;
        try {
            object = new JSONObject(Util.readFile(userFile));
            if (object.toString().equals("{}")) return null;
            long level = object.getLong("level");
            long points = object.getLong("points");
            long pointsToNextLvl = object.getLong("pointsToNextLvl");
            return new long[]{level, points, pointsToNextLvl};
        } catch (IOException e) {
        }
        return null;
    }

    public long[][] readAllUserLvl() throws IOException {
        try {
            long[][] allUserdata;
            String[] array = Util.readAllFilenames(mainPath);
            allUserdata = new long[3][array.length];
            int i = 0;
            for (String userFilename : array) {
                long userID = Long.parseLong(userFilename.substring(0,userFilename.lastIndexOf(".")));
                long[] userdata = readUserLvl(userID);
                allUserdata[0][i] = userdata[0];
                allUserdata[1][i] = userdata[1];
                allUserdata[2][i] = userdata[2];
                i++;
            }
            return allUserdata;
        } catch (IOException | NullPointerException e) {
        }
        return null;
    }
}
