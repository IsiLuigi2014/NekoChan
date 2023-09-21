package koyami.nekochan.commands.anime;

import koyami.nekochan.commands.control.Command;
import koyami.nekochan.commands.control.CommandHandle;
import koyami.nekochan.commands.control.MessageData;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@CommandHandle(name = "mal",hidden = true)
public class CmdMal implements Command {
    @Override
    public void action(String[] args, MessageData data, MessageReceivedEvent event) {

    }
}
