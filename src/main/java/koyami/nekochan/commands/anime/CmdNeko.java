package koyami.nekochan.commands.anime;

import koyami.nekochan.commands.control.Command;
import koyami.nekochan.commands.control.CommandHandle;
import koyami.nekochan.commands.control.MessageData;
import koyami.nekochan.util.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jsoup.nodes.Document;

@CommandHandle(name = "neko", description = "sends cute Nekos UwU", category = CommandHandle.ANIME_CATEGORY)
public class CmdNeko implements Command {

    @Override
    public void action(String[] args, MessageData data, MessageReceivedEvent event) {
        Document doc = Util.getDocument("https://nekos.life/");
        String imageUrl = doc.getElementsByTag("img").attr("src");
        data.textchannel.sendMessageEmbeds(new EmbedBuilder().setAuthor("Nekos~").setImage(imageUrl).setColor(Util.randomColor()).build()).complete();

    }
}
