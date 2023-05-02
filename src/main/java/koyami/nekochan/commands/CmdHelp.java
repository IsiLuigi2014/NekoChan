package koyami.nekochan.commands;

import koyami.nekochan.commands.control.Command;
import koyami.nekochan.commands.control.CommandHandle;
import koyami.nekochan.commands.control.CommandParser;
import koyami.nekochan.commands.control.MessageData;
import koyami.nekochan.util.Settings;
import koyami.nekochan.util.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.HashMap;
import java.util.TreeMap;

//TODO: ADD Categories

@CommandHandle(name = "help", description = "Gibt diese Nachricht aus")
public class CmdHelp implements Command {
    @Override
    public void action(String[] args, MessageData data, MessageReceivedEvent event) {
        StringBuilder mainBuilder = new StringBuilder();
        TreeMap<String, String> builder = new TreeMap<>();
        mainBuilder.append("__**Commands-Übersicht:**__\n");
        for (String cmd : CommandParser.help.keySet()) {
            if (CommandParser.hide.get(cmd)) continue;
            String category = CommandParser.categories.get(cmd);
            String argslist = CommandParser.argslist.get(cmd);
            String cmdDescription = String.format("%s%s%s: %s\n", Settings.prefix, cmd, argslist, CommandParser.help.get(cmd));
            String categoryContent;
            categoryContent = builder.getOrDefault(category, "");
            builder.put(category,categoryContent + cmdDescription);
        }
        for (String category : builder.keySet()) {
            String content = builder.get(category);
            if (!category.equals("#none")) content = String.format("**%s-Commands:**\n%s",category,content);
            mainBuilder.append(content);
        }
        //builder.append("Coded in 8 hours with anime listener uwu");
        data.textchannel.sendMessageEmbeds(new EmbedBuilder()
                .setAuthor("Nekochan Commands",null,data.botUser.getAvatarUrl())
                .setDescription(mainBuilder.toString())
                .setColor(Util.randomColor())
                .build()).queue();

    }
}
