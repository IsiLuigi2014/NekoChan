package koyami.nekochan.commands.mod;

import koyami.nekochan.Main;
import koyami.nekochan.commands.control.Command;
import koyami.nekochan.commands.control.CommandHandle;
import koyami.nekochan.commands.control.MessageData;
import koyami.nekochan.listener.timed.TimedListenerHandle;
import koyami.nekochan.util.Util;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import static koyami.nekochan.commands.control.CommandHandle.*;

//@CommandHandle(name = "timedlisteners", category = MOD_CATEGORY)
public class CmdTimedListeners implements Command {
    @Override
    public void action(String[] args, MessageData data, MessageReceivedEvent event) {
        try {
            String option;
            if (args.length != 1) option = args[1];
            else option = "list";
            switch (option) {
                case "list":
                    StringBuilder builder = new StringBuilder();
                    builder.append("Registrierte TimedListeners:\n");
                    for (String key : TimedListenerHandle.tasks.keySet()) {
                        builder.append(String.format("%s\n",key));
                    }
                    Util.sendMessage(data.textchannel, builder.toString());
                    break;
                case "restart":
                    boolean exist = TimedListenerHandle.restart(args[2]);
                    if (exist) Util.sendMessage(data.textchannel, String.format("Restarted %s-Listener", args[2]));
                    else Util.sendMessage(data.textchannel, "Listener konnte nicht gefunden werden *-w-*");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
