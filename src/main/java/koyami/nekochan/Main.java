package koyami.nekochan;

import koyami.nekochan.commands.control.CommandListener;
import koyami.nekochan.listener.level.LevelListener;
import koyami.nekochan.listener.timed.TimedListenerHandle;
import koyami.nekochan.util.Logger;
import koyami.nekochan.util.Token;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

//TODO: ON Connection lost stop listeners and restart when online again uwu, Log everything,-myanimelist
public class Main {
    public static TimedListenerHandle timedHandle;

    public static void main(String[] args) {
        Logger.logInfo("Ohayo, Watashi wa Nekochan~");
        JDABuilder builder = JDABuilder.createDefault(Token.token);

        // Disable parts of the cache
        builder.disableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE);
        // Enable the bulk delete event
        builder.setBulkDeleteSplittingEnabled(false);
        // Disable compression (not recommended)
        builder.setCompression(Compression.NONE);

        builder.enableIntents(GatewayIntent.MESSAGE_CONTENT);
        // Set activity (like "playing Something")
        builder.setActivity(Activity.listening("Starting up!"));

        builder.addEventListeners(new CommandListener());
        builder.addEventListeners(new LevelListener());

        JDA jda = builder.build();

        TimedListenerHandle.registerListeners(jda, null);

    }
}