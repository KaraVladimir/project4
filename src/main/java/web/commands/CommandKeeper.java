package web.commands;

import web.commands.impl.*;
import web.config.Pages;

import java.util.Map;
import java.util.TreeMap;

/**
 * Class keeps {@link Map<String,Command>}
 * @author kara.vladimir2@gmail.com.
 */
public enum  CommandKeeper {
    INSTANCE;

    private static Map<String, Command> commands = mapInit();

    private static Map<String,Command> mapInit() {
        Map<String, Command> map = new TreeMap<>();
        // common commands
        map.put(Pages.PATH_HOME, new LoginCommand());
        map.put(Pages.PATH_LOGOUT, new LogoutCommand());
        map.put(Pages.PATH_UNBLOCK, new UnblockCommand());
        map.put(Pages.PATH_FIND_ACCOUNT, new FindAccountCommand());
        map.put(Pages.PATH_FIND_ACCOUNT_FOR_PAY, new FindAccountCommand());
        map.put(Pages.PATH_PAY, new FindClientAccountsCommand());
        map.put(Pages.PATH_REFILL, new FindClientAccountsCommand());
        map.put(Pages.PATH_BLOCK, new FindClientAccountsCommand());
        map.put(Pages.COM_BLOCK, new BlockCommand());
        map.put(Pages.COM_PAY, new PayCommand());
        map.put(Pages.COM_REFILL, new RefillCommand());

        return map;
    }

    /**
     * Returns command object with the given name.
     *
     * @param commandName
     *            Name of the command.
     * @return Command object.
     */
    public static Command get(String commandName) {
        if (commandName == null || !commands.containsKey(commandName)) {
            return commands.get("noCommand");
        }
        return commands.get(commandName);
    }

}
