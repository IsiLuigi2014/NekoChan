package koyami.nekochan.listener.anime;

import koyami.nekochan.listener.timed.TimedListener;
import koyami.nekochan.listener.timed.TimedListenerInterface;
import koyami.nekochan.util.Logger;
import koyami.nekochan.util.Settings;
import koyami.nekochan.util.Util;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
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
        //Logger.logDebug(String.format("Lastnews before: %s",Settings.lastnewsanime));
        String newestnews = elements.get(0).getElementsByAttribute("href").get(0).attr("title");
        if (Settings.lastnewsanime == null) Settings.lastnewsanime = newestnews;
        //Logger.logDebug(String.format("Lastnews after: %s",Settings.lastnewsanime));
        //Collections.reverse(elements);
        //Logger.logDebug(String.valueOf(elements.size()));
        for (Element element : elements) {
            String title = element.getElementsByAttribute("href").get(0).attr("title");
            String url = element.getElementsByAttribute("href").get(0).attr("href");
            //Logger.logDebug(title);
            //Logger.logDebug(url);
            //Logger.logDebug(String.format("Current News: %s",title));
            if (title.equals(Settings.lastnewsanime)) {
                //Settings.lastnewsanime = title;
                //Logger.logDebug("Breaking loop for sending, the above news was not send");
                break;
            }
            Document doc2 = Util.getDocument(url);
            String description = doc2.getElementsByTag("h6").get(0).text();
            String imageUrl = doc2.getElementsByClass("td-ss-main-content").get(0).getElementsByTag("img").get(1).attr("src");
            String author = doc2.getElementsByClass("td-post-author-name").get(0).getElementsByTag("a").text();
            String time = convertTime(doc2.getElementsByClass("td-post-date").get(0).getElementsByTag("time").get(0).attr("datetime"));
            //Logger.logInfo("Send Animenews: " + title);
            try {
            for (Guild guild : jda.getGuilds()) {
                TextChannel channel = guild.getTextChannelsByName(Settings.getAnimeNewsChannel(), true).get(0);
                Message message = channel.getHistory().getRetrievedHistory().get(0);
                if (message.getEmbeds().size() != 0) {
                    if (Objects.equals(message.getEmbeds().get(0).getAuthor().getName(), title)) continue;
                }
                channel.sendMessageEmbeds(
                        Util.constructNewsEmbed(title,description,url,imageUrl,time,author).build()).complete();
            }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        Settings.lastnewsanime = newestnews;
        //Logger.logDebug("Loop Done - Setting Lastanimenews new");
    }

    private String convertTime(String time) {
        //Dienstag, 8. November 2022 19:02
        //System.out.println(time);
        ZonedDateTime date = ZonedDateTime.parse(time, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        return date.format(DateTimeFormatter.ofPattern("EEEE, dd. LLLL yyyy HH:mm z", Locale.GERMAN));
    }
}
