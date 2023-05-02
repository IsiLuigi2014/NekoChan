package koyami.nekochan.listener.level;

import koyami.nekochan.util.LevelUtil;
import koyami.nekochan.util.Logger;
import koyami.nekochan.util.Util;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.maven.plugin.logging.Log;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

import static koyami.nekochan.util.LevelUtil.*;

public class LevelListener extends ListenerAdapter {
    /*
    How JSON should look:
    level - 0
    points - 0 -> 1 - 5 points per message per 30 seconds
    pointsToNextLvl - 100
    rank?
     */

    private TextChannel channel;
    private Member member;

    public LevelListener() {
        try {
            if (!new File(mainPath).exists())
                Files.createDirectory(Path.of(mainPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onMessageReceived(MessageReceivedEvent event) {
        member = event.getMember();
        if (member.getUser().isBot()) return;
        long userID = member.getIdLong();
        String userFile = String.format("%s%s.json", mainPath, userID);
        channel = event.getChannel().asTextChannel();
        if (!LevelTimer.isUserWaiting(userID)) {
            JSONObject object = new JSONObject();
            if (Util.fileExist(userFile)) {
                long[] data = LevelUtil.readUserLvl(userID);
                if (data != null) object = calculateLevel(data, channel, member);
                else {
                    Util.sendMessage(event.getChannel().asTextChannel(), "Nekochan ist in einen Fehler gelaufen >_> Level-Datei beschädigt *no*").delete().completeAfter(2, TimeUnit.SECONDS);
                    return;
                }
            } else {
                object = putIn(0L, 0L, 35L);
                try {
                    Files.createFile(Path.of(userFile));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                Util.writeFile(object.toString(), userFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            LevelTimer.addUserWaiting(userID);
        }
    }
}
