package koyami.nekochan.listener.level;

import koyami.nekochan.listener.timed.TimedListener;
import koyami.nekochan.listener.timed.TimedListenerInterface;
import net.dv8tion.jda.api.JDA;

import java.util.HashMap;

@TimedListener(name = "level", time = 1)
public class LevelTimer implements TimedListenerInterface {
    public static HashMap<Long, Integer> waitList;
    public LevelTimer() {
        waitList = new HashMap<>();
    }

    @Override
    public void action(JDA jda) {
        for (Long userID : waitList.keySet()) {
            int time = waitList.get(userID);
            waitList.remove(userID);
            waitList.put(userID,time);
        }
    }

    public static boolean isUserWaiting(long userID) {
        return waitList.containsKey(userID);
    }

    public static void addUserWaiting(long userID) {
        waitList.put(userID,30);
    }
}
