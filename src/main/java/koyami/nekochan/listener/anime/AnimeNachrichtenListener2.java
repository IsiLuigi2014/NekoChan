package koyami.nekochan.listener.anime;

import koyami.nekochan.listener.timed.TimedListener;
import koyami.nekochan.listener.timed.TimedListenerInterface;
import koyami.nekochan.util.Logger;
import koyami.nekochan.util.Settings;
import koyami.nekochan.util.Util;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Objects;

@TimedListener(name = "animenachrichten2", time = 5 * 60)
public class AnimeNachrichtenListener2 implements TimedListenerInterface {
    @Override
    public void action(JDA jda) {
        Document doc = Util.getDocument("https://www.animenachrichten.de/kategorie/nachrichten");
        if (doc == null) return;
        Elements elements = doc.getElementsByClass("td-category-grid").get(0).getElementsByClass("td-module-thumb");
        if (Settings.lastnewsanime == null) Settings.lastnewsanime = elements.get(0).getElementsByAttribute("href").get(0).attr("title");
        //Collections.reverse(elements);
        for (Element element : elements) {
            String title = element.getElementsByAttribute("href").get(0).attr("title");
            String url = element.getElementsByAttribute("href").get(0).attr("href");
            //Logger.logDebug(title);
            //Logger.logDebug(url);
            if (title.equals(Settings.lastnewsanime)) {
                break;
            }
            Document doc2 = Util.getDocument(url);
            String description = doc2.getElementsByTag("h6").get(0).text();
            String imageUrl = doc2.getElementsByClass("td-ss-main-content").get(0).getElementsByTag("img").get(1).attr("src");
            String author = doc2.getElementsByClass("td-post-author-name").get(0).getElementsByTag("a").text();
            String time = convertTime(doc2.getElementsByClass("td-post-date").get(0).getElementsByTag("time").get(0).attr("datetime"));
            Logger.logInfo("Send Animenews: " + title);
            for (Guild guild : jda.getGuilds()) {
                guild.getTextChannelsByName(Settings.animenewschannel, true).get(0).sendMessageEmbeds(
                        Util.constructNewsEmbed(title,description,url,imageUrl,time,author).build()).complete();
            }
        }
    }

    private String convertTime(String time) {
        //Dienstag, 8. November 2022 19:02
        //System.out.println(time);
        ZonedDateTime date = ZonedDateTime.parse(time, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        return date.format(DateTimeFormatter.ofPattern("EEEE, dd. LLLL yyyy HH:mm z", Locale.GERMAN));
    }
}
