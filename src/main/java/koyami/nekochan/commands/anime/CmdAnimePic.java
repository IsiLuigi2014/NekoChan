package koyami.nekochan.commands.anime;

import koyami.nekochan.commands.control.Command;
import koyami.nekochan.commands.control.CommandHandle;
import koyami.nekochan.commands.control.MessageData;
import koyami.nekochan.util.Konachan;
import koyami.nekochan.util.Logger;
import koyami.nekochan.util.Settings;
import koyami.nekochan.util.Util;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

//TODO: Konachan class + args1 for rating
@CommandHandle(name = "animepic", args = " <tag, rating, count>", description = "Bekomme ein random AnimePic OwO", category = CommandHandle.ANIME_CATEGORY)
public class CmdAnimePic implements Command {
    @Override
    public void action(String[] args, MessageData data, MessageReceivedEvent event) {
        int i = 1;
        int argscount = args.length - 1;
        if (args[argscount] != null) {
            if (args[argscount].chars().allMatch(x -> Character.isDigit(x))) {
                i = Integer.parseInt(args[argscount]);
            }
        }
        for (int ix = 0; ix < i; ix++) {
            sendAnimePic(args,data);
        }
    }

    private void sendAnimePic(String[] args,MessageData data) {
        char[] possible = {'s','q','e'};
        char rating = 's';
        if (args.length >= 3) {
            for (char rate : possible) {
                if (String.valueOf(rate).equals(args[2].toLowerCase())) rating = args[2].toLowerCase().charAt(0);
            }
        }
        String search = "";
        if (args.length != 1) search = args[1];
        JSONObject object = Konachan.getRandomWithRatingPicture(search,rating);
        if (object.isEmpty()) {
            Util.sendMessage(data.textchannel, "Nekochan kann mit diesem Tag kein Bild finden *sad >w<*");//.delete().completeAfter(5, TimeUnit.SECONDS);
            String[] tags = Konachan.tagOptions(search,10);
            if (tags.length == 0) return;
            StringBuilder builder = new StringBuilder();
            for (String tag : tags) {
                builder.append(String.format("`%s`\n",tag));
            }
            builder.append(String.format("Beispiel: `%s%s %s`", Settings.prefix,data.cmd, tags[Util.random(tags.length - 1)]));
            Util.sendEmbedMessage(data.textchannel,
                    Util.constructClassicEmbed(String.format("Weitere Ergebnisse für %s:", search),"","",
                            builder.toString(),Util.randomColor()));
            return;
        }
        Util.sendEmbedMessage(data.textchannel, Util.constructClassicEmbed("AnimePic~", object.getString("file_url"),"", "",Util.randomColor()));
    }
}
