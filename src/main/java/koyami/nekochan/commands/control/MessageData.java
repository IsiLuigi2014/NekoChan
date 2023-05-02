package koyami.nekochan.commands.control;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class MessageData {
    public final String cmd;
    public final MessageReceivedEvent event;
    public final User user;
    public final Member member;
    public final Message message;
    public final Guild guild;
    public final Channel channel;
    public final TextChannel textchannel;
    public final String messageRaw;
    public final User botUser;

    public MessageData(MessageReceivedEvent event, String cmd) {
        this.cmd = cmd;
        this.event = event;
        this.user = event.getAuthor() != null ? event.getAuthor() : null;
        this.member = event.getMember();
        this.message = event.getMessage();
        this.guild = event.getGuild();
        this.channel = event.getChannel();
        this.textchannel = event.getChannel().asTextChannel();
        this.messageRaw = event.getMessage().getContentRaw();
        this.botUser = event.getJDA().getSelfUser();

    }
}
