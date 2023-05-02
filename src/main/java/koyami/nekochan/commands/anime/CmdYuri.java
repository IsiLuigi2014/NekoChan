package koyami.nekochan.commands.anime;

import koyami.nekochan.commands.control.Command;
import koyami.nekochan.commands.control.CommandHandle;
import koyami.nekochan.commands.control.MessageData;
import koyami.nekochan.util.Konachan;
import koyami.nekochan.util.Logger;
import koyami.nekochan.util.Util;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.json.JSONObject;

//TODO: Konachan class
@CommandHandle(name = "yuri", args = " <count>",description = "sends yuri OwO", category = CommandHandle.ANIME_CATEGORY)
public class CmdYuri implements Command {

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
        char rating = Konachan.SAFE;
        switch (Util.random(2)) {
            case 0:
                break;
            case 1:
                rating = Konachan.QUESTIONABLE;
                break;
            case 2:
                rating = Konachan.EXPLICIT;
                break;
        }
        JSONObject object = Konachan.getRandomWithRatingPicture("yuri", rating);
        if (object.isEmpty()) {
            Util.sendMessage(data.textchannel, "Nekochan kann gerade kein Yuris finden *sad >w<*");
            return;
        }
        Util.sendEmbedMessage(data.textchannel, Util.constructClassicEmbed("Yuri Pic~", object.getString("file_url"),"", "",null));
    }
}
