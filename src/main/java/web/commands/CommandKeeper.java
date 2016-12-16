package web.commands;

/**
 * @author kara.vladimir2@gmail.com.
 */
public interface CommandKeeper {
    Command get(String commandName);
}
