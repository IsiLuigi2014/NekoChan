package koyami.nekochan.commands.level;

import koyami.nekochan.commands.control.Command;
import koyami.nekochan.commands.control.CommandHandle;
import koyami.nekochan.commands.control.MessageData;
import koyami.nekochan.util.LevelUtil;
import koyami.nekochan.util.Util;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import static koyami.nekochan.util.LevelUtil.*;

//TODO: Level Command Write
@CommandHandle(name = "level", description = "Check your level status uwu")
public class CmdLevel implements Command {
    @Override
    public void action(String[] args, MessageData data, MessageReceivedEvent event) {
        Member member = Util.getMemberFromMessage(data,args);
        if (member.getUser().isBot()) return;
        long userID = member.getIdLong();
        String userFile = String.format("%s%s.json",mainPath,userID);
        TextChannel channel = event.getChannel().asTextChannel();
        if (Util.fileExist(userFile)) {
            long[] lvlData = LevelUtil.readUserLvl(userID);
            if (lvlData == null) return;
            Util.sendMessage(channel,String.format(
                    "Statistik von %s:\n" +
                    "Level: %d\n" +
                    "Points: %d\n" +
                    "Points to next Level: %d", member.getUser().getAsMention(),lvlData[0],lvlData[1],lvlData[2]));
        } else {
            Util.sendMessage(channel, "Nekochan hat über diesen Nutzer bis jetzt keine Infos -w-");
        }
    }
}
