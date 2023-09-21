package koyami.nekochan.commands.anime;

import koyami.nekochan.commands.control.Command;
import koyami.nekochan.commands.control.CommandHandle;
import koyami.nekochan.commands.control.MessageData;
import koyami.nekochan.util.Danbooru;
import koyami.nekochan.util.Konachan;
import koyami.nekochan.util.Util;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.utils.FileUpload;
import org.json.JSONObject;

import java.net.URL;
import java.util.concurrent.TimeUnit;

@CommandHandle(name = "yuri2", args = " <count>",description = "new imporved to sends yuri OwO", category = CommandHandle.ANIME_CATEGORY)
public class CmdNewYuri implements Command {

    @Override
    public void action(String[] args, MessageData data, MessageReceivedEvent event) {
        int i = 1;
        if (args.length >= 2) {
            if (args[1].chars().allMatch(Character::isDigit)) {
                i = Integer.parseInt(args[1]);
                if (i > 50) i = 50;
            }
        }
        Util.sendMessage(data.textchannel, "Starting Yuri download. Might take a while so be patient~").delete().queueAfter(10,TimeUnit.SECONDS);
        for (int ix = 0; ix < i; ix++) {
            sendYuri(data);
        }
        Util.sendMessage(data.textchannel, "Yuri sending done owo").delete().queueAfter(10,TimeUnit.SECONDS);;
    }

    private void sendYuri(MessageData data) {
        String[] picdata = Danbooru.getYuri();
        if (picdata.length == 0) {
            Util.sendMessage(data.textchannel, "Nekochan kann das nächste Yuri-Pic nicht finden *sad >w<*").delete().queueAfter(3, TimeUnit.MINUTES);;
            return;
        }
        if (!data.textchannel.isNSFW()) if (picdata[1].equals("q") || picdata[2].equals("e")) {
            Util.sendMessage(data.textchannel, "Nekochan kann das Yuri-Pic nicht senden da zu lewd *OwO >w<*").delete().queueAfter(3, TimeUnit.MINUTES);
            return;
        }
        //Util.sendEmbedMessage(data.textchannel, Util.constructClassicEmbed("Yuri Pic~", object.getJSONObject("media_asset").getJSONArray("variants").getJSONObject(5).getString("url"),"", "",null));
        byte[] img;
        try {
            img = Util.downloadUrl(new URL(picdata[0]));
        } catch (Exception e) {
            Util.sendMessage(data.textchannel, "Nekochan kann das Yuri Pic leider nicht downloaden *sad >w<*").delete().queueAfter(3, TimeUnit.MINUTES);;
            return;
        }
        data.textchannel.sendMessage("Yuri-Pic~").addFiles(FileUpload.fromData(img,picdata[2] + ".jpg")).complete();
        Util.sleep(1);
    }
}
