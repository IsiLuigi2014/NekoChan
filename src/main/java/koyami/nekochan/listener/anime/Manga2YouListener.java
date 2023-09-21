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
import java.util.concurrent.TimeUnit;

//@TimedListener(name = "manga2you", time = 60 * 5)
public class Manga2YouListener implements TimedListenerInterface {
    /*
    news array = news[0][0] => first news, title
    news array = news[1][0] => first news, url
    news array = news[0][1] => second news, title
    news array = news[1][1] => second news, url
    ...
     */
    private String lastNews = null;
    @Override
    public void action(JDA jda) {
        Util.logDebugFileAppend("Starting Manga2YouListener");
        String baseUrl = "https://www.manga2you.de/kategorie/nachrichten/";
        Document doc = Util.getDocument(baseUrl);
        if (doc == null) return;
        Elements elements = doc.getElementsByClass("loop-list").get(0).getElementsByTag("article");
        int i = 0;
        String[][] news = new String[2][elements.size()];
        Util.logDebugFileAppend("Size of elements array: " +elements.size());
        for (Element element : elements) {
            String title = element.getElementsByClass("post-title").text();
            String url = element.getElementsByTag("a").get(0).attr("href");
            //String time = convertTime(element.getElementsByClass("post-date").get(0).attr("datetime"));
            //System.out.println(String.format("%s - %s", title, url));
            news[0][i] = title;
            news[1][i] = url;
            Util.logDebugFileAppend(String.format("news[0][%d]: %s", i, title));
            Util.logDebugFileAppend(String.format("news[1][%d]: %s", i, url));

            //news[2][i] = time;
            i++;
        }
        Util.logDebugFileAppend(String.format("size of i %d", i));
        Util.logDebugFileAppend(String.format("size of news array: %d", news.length * news[0].length));


        if (lastNews == null) lastNews = news[0][0];

        Util.logDebugFileAppend("Last News " + lastNews);
        for (int it = 0; it < i; it++) {
            if (Objects.equals(news[0][it], lastNews)) {
                Util.logDebugFileAppend("News in List on iteration - Stop:" + news[0][it]);
                break;
            } else {
                Util.logDebugFileAppend("News in List on iteration - Send:" + news[0][it]);
                if (news[1][it] == null) continue;
                doc = Util.getDocument(news[1][it]);
                String description = doc.getElementsByClass("post-content-wrap").get(0).getElementsByTag("p").get(0).text();
                String imageUrl = doc.getElementsByClass("post-wrap").get(0).getElementsByTag("img").get(0).attr("data-src");
                String author = doc.getElementsByClass("post-author").get(0).getElementsByTag("a").text();
                String time = convertTime(doc.getElementsByClass("meta-item date").get(0).getElementsByTag("time").get(0).attr("datetime"));
                for (Guild guild : jda.getGuilds()) {
                    guild.getTextChannelsByName(Settings.getMangaNewsChannel(), true).get(0).sendMessageEmbeds(
                            Util.constructNewsEmbed(news[0][it],description,news[1][it],imageUrl,time,author).build()).queueAfter(250, TimeUnit.MILLISECONDS);
                    Util.logDebugFileAppend("Sending news: " + news[0][it]);
                    Util.sleep(2);
                }
            }
        }
        Util.logDebugFileAppend("End of Manga2YouListener\n");
    }
    private String convertTime(String time) {
        ZonedDateTime date = ZonedDateTime.parse(time, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        return date.format(DateTimeFormatter.ofPattern("EEEE, dd. LLLL yyyy HH:mm z", Locale.GERMAN));
    }
}
