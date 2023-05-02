package koyami.nekochan.commands.control;

import koyami.nekochan.util.Settings;
import koyami.nekochan.util.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.lang.reflect.InvocationTargetException;

public class CommandListener extends ListenerAdapter {

    CommandParser parser;
    public CommandListener() {
        this.parser = new CommandParser();
    }


    public void onMessageReceived(MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        if (message.startsWith(Settings.prefix)) {
            String[] args = splitMessage(message);
            if (parser.cmd.containsKey(args[0])) {
                try {
                    //April Fools
                    int day = event.getMessage().getTimeCreated().getDayOfMonth();
                    int month = event.getMessage().getTimeCreated().getMonthValue();
                    if (day == 1 && month == 4) {
                        int random = Util.random(10);
                        if (random >= 7) {
                            fbi(event, args[0]);
                            return;
                        }
                    }
                    //PermHandler
                    boolean modaccess = false;
                    for (Role role : event.getMember().getRoles()) {
                        if (role.getName().equals("Tiem")) {
                            modaccess = true;
                        }
                    }
                    if (parser.categories.get(args[0]).equals(CommandHandle.MOD_CATEGORY)) {
                        if (!modaccess) return;
                    }
                    ((Command)parser.cmd.get(args[0]).getDeclaredConstructor().newInstance()).action(args,new MessageData(event,args[0]), event);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                         NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }

            }
        }
    }

    //Example: -help command
    private String[] splitMessage(String input) {
        input = input.replaceFirst("-", "");
        String[] output = input.split(" ");
        output[0] = output[0].toLowerCase();
        return output;
    }

    private void fbi(MessageReceivedEvent event, String cmd) {
        event.getChannel().asTextChannel().sendMessageEmbeds(new EmbedBuilder()
                        .setAuthor("FBI is coming...")
                .setDescription("You wanna to do '" + cmd + "' \nNope not today!")
                .setImage("https://media.tenor.com/qEmU0G67ve4AAAAd/fbi-meme-fbi-open-up-memes.gif")
                        .setColor(Util.randomColor())
                .build()).queue();
    }
}
