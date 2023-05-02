package koyami.nekochan.commands.control;

import koyami.nekochan.util.Test;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface Command {

    void action(String[] args, MessageData data, MessageReceivedEvent event);
}
