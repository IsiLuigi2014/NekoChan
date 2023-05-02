package koyami.nekochan.util;

import koyami.nekochan.listener.timed.TimedListener;
import koyami.nekochan.listener.timed.TimedListenerInterface;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;

//TODO: Watching Text like Activity - Anime watching
@TimedListener(name = "customstatus")
public class CustomWatchingTexts implements TimedListenerInterface {
    //0 = playing; 1 = watching; 2 = live; 3 = listening
    private final String[] quotes = {
            "1-Nekos","1-Mangas","1-Anime","1-UwU",
            "1-OwO", "3--help | Call me~", "3-Call me~",
            "2-Anime Time~", "0-Currently Beta", "1-バカ！"};

    @Override
    public void action(JDA jda) {
        int i = (int) Math.round(Math.random() * (quotes.length - 1));
        String status = quotes[i];
        int type = Integer.parseInt(status.substring(0,status.indexOf("-")));
        status = status.substring(status.indexOf("-") + 1);
        switch (type) {
            case 0:
                jda.getPresence().setActivity(Activity.playing(status));
                break;
            case 1:
                jda.getPresence().setActivity(Activity.watching(status));
                break;
            case 2:
                jda.getPresence().setActivity(Activity.streaming(status,""));
                break;
            case 3:
                jda.getPresence().setActivity(Activity.listening(status));
                break;
        }
    }
}
