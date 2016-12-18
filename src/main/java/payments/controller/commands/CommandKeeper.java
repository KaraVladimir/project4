package payments.controller.commands;

/**
 * @author kara.vladimir2@gmail.com.
 */
public interface CommandKeeper {
    Command get(String commandName);
}
