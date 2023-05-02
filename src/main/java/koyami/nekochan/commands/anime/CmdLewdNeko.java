package koyami.nekochan.commands.anime;

import koyami.nekochan.commands.control.Command;
import koyami.nekochan.commands.control.CommandHandle;
import koyami.nekochan.commands.control.MessageData;
import koyami.nekochan.util.Konachan;
import koyami.nekochan.util.Util;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.json.JSONArray;
import org.json.JSONObject;

//TODO: Konachan class
@CommandHandle(name = "lewdneko", args = " <count>",description = "sends cute lewd nekos OwO", category = CommandHandle.ANIME_CATEGORY)
public class CmdLewdNeko implements Command {

    @Override
    public void action(String[] args, MessageData data, MessageReceivedEvent event) {
        int i = 1;
        if (args.length >= 2) {
            if (args[1].chars().allMatch(x -> Character.isDigit(x))) {
                i = Integer.parseInt(args[1]);
                if (i > 50) i = 50;
            }
        }
        for (int ix = 0; ix < i; ix++) {
            sendNeko(data);
        }
    }

    private void sendNeko(MessageData data) {
        char rating;
        if (Math.random() >= 0.5) rating = Konachan.QUESTIONABLE; else rating = Konachan.EXPLICIT;
        JSONObject object = Konachan.getRandomWithRatingPicture("catgirl", rating);
        if (object.isEmpty()) {
            Util.sendMessage(data.textchannel, "Nekochan kann gerade kein lewd Nekos finden *sad >w<*");
            return;
        }
        Util.sendEmbedMessage(data.textchannel, Util.constructClassicEmbed("Lewd Neko~", object.getString("file_url"),"", "",null));
    }
}
