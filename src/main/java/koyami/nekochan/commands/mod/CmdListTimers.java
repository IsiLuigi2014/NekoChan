package koyami.nekochan.commands.mod;

import koyami.nekochan.commands.control.Command;
import koyami.nekochan.commands.control.CommandHandle;
import koyami.nekochan.commands.control.MessageData;
import koyami.nekochan.listener.timed.TimedListenerHandle;
import koyami.nekochan.util.Logger;
import koyami.nekochan.util.Util;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@CommandHandle(name = "listtimers", category = CommandHandle.MOD_CATEGORY, description = "Lists all background tasks")
public class CmdListTimers implements Command {
    @Override
    public void action(String[] args, MessageData data, MessageReceivedEvent event) {
        StringBuilder builder = new StringBuilder();
        for (String task : TimedListenerHandle.tasks.keySet()) {
            builder.append(String.format("`%s`\n",task));
        }
        Util.sendEmbedMessage(data.textchannel, Util.constructClassicEmbed("List all Timer:","","",builder.toString(), Util.randomColor()));
    }
}
