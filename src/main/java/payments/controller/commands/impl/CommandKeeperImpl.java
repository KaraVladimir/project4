package payments.controller.commands.impl;

import payments.controller.commands.Command;
import payments.helper.Pages;

import java.util.Map;
import java.util.TreeMap;

/**
 * Class takes URI and give command
 *
 * @author kara.vladimir2@gmail.com.
 */
public class CommandKeeperImpl implements payments.controller.commands.CommandKeeper {
    private final Map<String, Command> commands = mapInit();

    private Map<String, Command> mapInit() {
        Map<String, Command> map = new TreeMap<>();
        map.put(Pages.PATH_HOME, new LoginCommand());
        map.put(Pages.PATH_LOGOUT, new LogoutCommand());
        map.put(Pages.PATH_UNBLOCK, new UnblockCommand());
        map.put(Pages.PATH_FIND_ACCOUNT, new FindAccountCommand());
        map.put(Pages.PATH_PAY, new PayCommand());
        map.put(Pages.PATH_REFILL, new RefillCommand());
        map.put(Pages.PATH_BLOCK, new BlockCommand());
        return map;
    }

    /**
     * Returns command object with the given name.
     *
     * @param commandName Name of the command.
     * @return Command object.
     */
    @Override
    public Command get(String commandName) {
        if (commandName == null || !commands.containsKey(commandName)) {
            return commands.get("noCommand");
        }
        return commands.get(commandName);
    }

}
