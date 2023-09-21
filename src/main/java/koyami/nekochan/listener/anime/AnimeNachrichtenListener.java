package koyami.nekochan.listener.anime;

import koyami.nekochan.listener.timed.TimedListener;
import koyami.nekochan.listener.timed.TimedListenerInterface;
import koyami.nekochan.util.Settings;
import koyami.nekochan.util.Util;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

//@TimedListener(name = "animenachrichten", time = 5 * 60)
public class AnimeNachrichtenListener implements TimedListenerInterface {
    private String lastNews = null;
    @Override
    public void action(JDA jda) {
        Document doc = Util.getDocument("https://www.animenachrichten.de/kategorie/nachrichten");
        if (doc == null) return;
        Elements elements = doc.getElementsByClass("td-category-grid").get(0).getElementsByClass("td-module-thumb");
        String[][] news = new String[2][elements.size()];
        int i = 0;
        for (Element element : elements) {
            String title = element.getElementsByAttribute("href").get(0).attr("title");
            String url = element.getElementsByAttribute("href").get(0).attr("href");
            news[0][i] = title;
            news[1][i] = url;
            i++;
        }

        if (lastNews == null) lastNews = news[0][0];

        for (int it = 0; it < i; i++) {
            if (Objects.equals(news[0][it], lastNews)) {
                break;
            } else {
                doc = Util.getDocument(news[1][it]);
                String description = doc.getElementsByTag("h6").get(0).text();
                String imageUrl = doc.getElementsByClass("td-ss-main-content").get(0).getElementsByTag("img").get(1).attr("src");
                String author = doc.getElementsByClass("td-post-author-name").get(0).getElementsByTag("a").text();
                String time = convertTime(doc.getElementsByClass("td-post-date").get(0).getElementsByTag("time").get(0).attr("datetime"));
                for (Guild guild : jda.getGuilds()) {
                    guild.getTextChannelsByName(Settings.getAnimeNewsChannel(), true).get(0).sendMessageEmbeds(
                            Util.constructNewsEmbed(news[0][it],description,news[1][it],imageUrl,time,author).build()).complete();
                }
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
