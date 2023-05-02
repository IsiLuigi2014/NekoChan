package koyami.nekochan.commands.control;

import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Set;

import static org.reflections.scanners.Scanners.SubTypes;
import static org.reflections.scanners.Scanners.TypesAnnotated;

public class CommandParser {
    public static HashMap<String,Class> cmd = new HashMap<>();
    public static HashMap<String,String> help = new HashMap<>();
    public static HashMap<String,String> categories = new HashMap<>();
    public static HashMap<String,String> argslist = new HashMap<>();
    public static HashMap<String,Boolean> hide = new HashMap<>();

    public CommandParser() {
        registerCommands();
    }

    private void registerCommands() {
        Reflections reflections = new Reflections("koyami.nekochan.commands");
        Set<Class<?>> annotated =
                reflections.get(SubTypes.of(TypesAnnotated.with(CommandHandle.class)).asClass());

        for (Class<?> controller : annotated) {
            CommandHandle request = controller.getAnnotation(CommandHandle.class);
            String name = request.name();
            String description = request.description();
            String category = request.category();
            String args = request.args();
            cmd.put(name,controller);
            help.put(name,description);
            categories.put(name,category);
            argslist.put(name, args);
            hide.put(name,request.hidden());
        }
    }
}
