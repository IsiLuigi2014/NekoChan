package koyami.nekochan.commands.mod;

import koyami.nekochan.Main;
import koyami.nekochan.commands.control.Command;
import koyami.nekochan.commands.control.CommandHandle;
import koyami.nekochan.commands.control.MessageData;
import koyami.nekochan.listener.timed.TimedListenerHandle;
import koyami.nekochan.util.Util;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import static koyami.nekochan.commands.control.CommandHandle.*;

@CommandHandle(name = "restarttimer", description = "Restart Timed-Listeners", category = MOD_CATEGORY)
public class CmdRestartTimedListeners implements Command {
    @Override
    public void action(String[] args, MessageData data, MessageReceivedEvent event) {
        Util.sendMessage(data.textchannel, "Restarting TimedListeners");
        TimedListenerHandle.restart();
        Util.sendMessage(data.textchannel, "Restarted TimedListeners");
    }
}
