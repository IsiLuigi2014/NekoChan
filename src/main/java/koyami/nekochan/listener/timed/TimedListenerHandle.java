package koyami.nekochan.listener.timed;

import koyami.nekochan.commands.control.Command;
import koyami.nekochan.commands.control.CommandHandle;
import koyami.nekochan.commands.control.MessageData;
import koyami.nekochan.util.CustomWatchingTexts;
import koyami.nekochan.util.Logger;
import koyami.nekochan.util.MyTimer;
import net.dv8tion.jda.api.JDA;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import static org.reflections.scanners.Scanners.SubTypes;
import static org.reflections.scanners.Scanners.TypesAnnotated;

//TODO: Work with reflections
public class TimedListenerHandle {
    public static HashMap<String,MyTimer> tasks = new HashMap();
    public static JDA jda;
    public static void registerListeners(JDA jda, String className) {
        TimedListenerHandle.jda = jda;
        Reflections reflections = new Reflections("koyami.nekochan");
        Set<Class<?>> annotated =
                reflections.get(SubTypes.of(TypesAnnotated.with(TimedListener.class)).asClass());
        for (Class<?> controller : annotated) {
            TimedListener request = controller.getAnnotation(TimedListener.class);
            String name = request.name();
            if (!name.equals(className) && className != null) continue;
            int time = request.time();
            MyTimer timer = new MyTimer(1000 * 5, time * 1000L);

            //TODO: What happened on an Exception?
            timer.runAtScheduleTimeOnInitiation(new TimerTask() {
                @Override
                public void run() {
                    try {
                        ((TimedListenerInterface)controller.getDeclaredConstructor().newInstance()).action(jda);
                    } catch (Exception e) {
                        e.printStackTrace();
                        //throw new RuntimeException(e);
                    }
                }
            });
            tasks.put(name,timer);
        }
    }

    public static void restart() {
        stop();
        Logger.logInfo("Restarting all Timed-Listeners");
        TimedListenerHandle.registerListeners(jda, null);
        Logger.logInfo("Restarted all Timed-Listeners!");
    }

    public static boolean restart(String name) {
        boolean exist = stop(name);
        if (!exist) return false;
        Logger.logInfo(String.format("Restarting %s Timed-Listeners", name));
        TimedListenerHandle.registerListeners(jda, null);
        Logger.logInfo(String.format("Restarted %s Timed-Listeners!", name));
        return true;
    }

    public static boolean start(String name) {
        if (tasks.containsKey(name)) return false;
        Logger.logInfo(String.format("Restarting %s Timed-Listeners", name));
        TimedListenerHandle.registerListeners(jda, null);
        Logger.logInfo(String.format("Restarted %s Timed-Listeners!", name));
        return true;
    }

    public static boolean stop(String name) {
        if (tasks.containsKey(name)) return false;
        Logger.logInfo(String.format("Stopping %s Timed-Listeners", name));
        tasks.get(name).cancel();
        tasks.remove(name);
        return true;
    }

    public static void stop() {
        Logger.logInfo("Stopping all Timed-Listeners");
        tasks.values().forEach(Timer::cancel);
        tasks.clear();
    }
}
