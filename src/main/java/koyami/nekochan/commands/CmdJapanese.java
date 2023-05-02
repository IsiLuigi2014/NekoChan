package koyami.nekochan.commands;

import koyami.nekochan.commands.control.Command;
import koyami.nekochan.commands.control.CommandHandle;
import koyami.nekochan.commands.control.MessageData;
import koyami.nekochan.util.Util;
import koyami.nekochan.util.jisho.Jisho;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
//TODO:

@CommandHandle(name = "japanese",hidden = true)
public class CmdJapanese implements Command {
    @Override
    public void action(String[] args, MessageData data, MessageReceivedEvent event) {
        Jisho jisho = new Jisho(args[1]);
        /*String s = jisho.getJSONObject().toString();
        for (String s1: splitByNumber(s, 2000)) {
            Util.sendMessage(data.textchannel, s1);
        }*/
        Util.sendMessage(data.textchannel, jisho.getOneWord(0).toString());

    }

    public static String[] splitByNumber(String str, int size) {
        return (size<1 || str==null) ? null : str.split("(?<=\\G.{"+size+"})");
    }
}
