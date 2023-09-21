package koyami.nekochan.listener.anime;

import koyami.nekochan.listener.timed.TimedListener;
import koyami.nekochan.listener.timed.TimedListenerInterface;
import koyami.nekochan.util.Logger;
import koyami.nekochan.util.Settings;
import koyami.nekochan.util.Util;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

//TODO: 41 why not printing messanges? Not working because no loading uwu
//@TimedListener(name = "mangapassion", time = 5 * 60)
public class MangaPassionListener implements TimedListenerInterface {
    @Override
    public void action(JDA jda) {
        String baseUrl = "https://www.manga-passion.de/articles";
        Document doc = Util.getComplexDocument(baseUrl);
        try {
            Util.writeFile(doc.toString(),"test3.html");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Elements elements = doc.getElementsByClass("article-list_list__shOdT ").get(0).getElementsByClass("article-list_item__Cz7JA");
        if (Settings.lastnewsmanga == null) Settings.lastnewsmanga = elements.get(1).getElementsByTag("h4").text();
        //Logger.logDebug(elements.size() + "");
        for (Element element : elements) {
            String title = element.getElementsByTag("h4").text();
            Logger.logInfo(title);
            String url = element.attr("href");
            String imageUrl = baseUrl + element.getElementsByTag("img").attr("src");
            String description = "";//element.getElementsByTag("p").get(1).text();//element.getElementsByClass("article-list_description__cCOMM").get(0).text();
            String time = "";//element.getElementsByTag("p").get(0).text();//element.getElementsByClass("article-list_date__mtcve").get(0).text();
            if (title.isEmpty()) continue;
            if (title.equals(Settings.lastnewsmanga)) {
                Logger.logDebug("break");
                //break;
            }
            for (Guild guild : jda.getGuilds()) {
                guild.getTextChannelsByName(Settings.getAnimeNewsChannel(), true).get(0).sendMessageEmbeds(
                        Util.constructNewsEmbed(title,description,url,imageUrl,time,"").build()).complete();
            }
        }
    }
}
