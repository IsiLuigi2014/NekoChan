package koyami.nekochan.commands.anime;

import koyami.nekochan.commands.control.Command;
import koyami.nekochan.commands.control.CommandHandle;
import koyami.nekochan.commands.control.MessageData;
import koyami.nekochan.util.Danbooru;
import koyami.nekochan.util.Konachan;
import koyami.nekochan.util.Logger;
import koyami.nekochan.util.Util;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.utils.AttachedFile;
import net.dv8tion.jda.api.utils.FileUpload;
import org.json.JSONObject;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

@CommandHandle(name = "yuri", args = " <count>",description = "sends yuri OwO", category = CommandHandle.ANIME_CATEGORY)
public class CmdYuri implements Command {

    @Override
    public void action(String[] args, MessageData data, MessageReceivedEvent event) {
        int i = 1;
        if (args.length >= 2) {
            if (args[1].chars().allMatch(Character::isDigit)) {
                i = Integer.parseInt(args[1]);
                if (i > 50) i = 50;
            }
        }
        for (int ix = 0; ix < i; ix++) {
            sendYuriold(data);
        }
    }

    private void sendYuriold(MessageData data) {
        JSONObject object = Konachan.getRandomPicture("yuri");
        //Logger.logDebug(object.toString());
        if (object.isEmpty()) {
            Util.sendMessage(data.textchannel, "Nekochan kann gerade kein Yuri-Pics finden *sad >w<*");
            return;
        }
        if (!data.textchannel.isNSFW() && (object.getString("rating").equals("e") || object.getString("rating").equals("q"))) {
            Util.sendMessage(data.textchannel, "Nekochan kann das Bild nicht schicken, da es zu lewd ist *OwO*");
            return;
        }
        Util.sendEmbedMessage(data.textchannel, Util.constructClassicEmbed("Yuri-Pic~", object.getString("file_url"),"", "",null));
    }
    private void sendYuri(MessageData data) {
        String[] picdata = Danbooru.getRandomPicture("yuri");
        if (picdata.length == 0) {
            Util.sendMessage(data.textchannel, "Nekochan kann gerade kein Yuris finden *sad >w<*");
            return;
        }
        if (!data.textchannel.isNSFW()) if (picdata[1].equals("q") || picdata[2].equals("e")) {
            Util.sendMessage(data.textchannel, "Nekochan kann das Yuri-Pic nicht senden da zu lewd *OwO >w<*").delete().completeAfter(3, TimeUnit.MINUTES);
            return;
        }
        //Util.sendEmbedMessage(data.textchannel, Util.constructClassicEmbed("Yuri Pic~", object.getJSONObject("media_asset").getJSONArray("variants").getJSONObject(5).getString("url"),"", "",null));
        byte[] img;
        try {
            img = Util.downloadUrl(new URL(picdata[0]));
        } catch (Exception e) {
            Util.sendMessage(data.textchannel, "Nekochan kann das Yuri Pic leider nicht downloaden *sad >w<*");
            return;
        }
        data.textchannel.sendMessage("Yuri-Pics~").addFiles(FileUpload.fromData(img,picdata[3] + ".jpg")).complete();
        Util.sleep(1);
    }
}
