package web.commands;

import web.commands.impl.*;

import java.util.Map;
import java.util.TreeMap;

/**
 * Class keeps {@link Map<String,Command>}
 * @author kara.vladimir2@gmail.com.
 */
public class CommandKeeper {
    private static Map<String, Command> commands = mapInit();

    private static Map<String,Command> mapInit() {
        Map<String, Command> map = new TreeMap<>();
        // common commands
        map.put("/page/login", new LoginCommand());
        map.put("/page/unblock", new UnblockCommand());
        map.put("/page/findAccount", new FindAccountCommand());
        map.put("logout", new LogoutCommand());
        map.put("register", new RegisterCommand());
        map.put("/", new FirsPageCommand());



//        // client commands
//        commands.put("listCards", new ListCardsCommand());
//        commands.put("createCard", new CreateCardCommand());
//        commands.put("preparePayment", new PreparePaymentCommand());
//        commands.put("listPayment", new ListPaymentsCommand());
//        commands.put("requestUnblock", new RequestUnblockCardCommand());
//        commands.put("refillCardView", new RefillingTerminalViewCommand());
//        commands.put("refillCard", new RefillCardCommand());
//        commands.put("makePaymentView", new MakePaymentViewCommand());
//        commands.put("makePayment", new MakePaymentCommand());
//        commands.put("blockCard", new BlockCardCommand());
//        commands.put("listCardsSorted", new ListCardsSortedCommand());
//        commands.put("listPaymentsSorted", new ListPaymentsSortedCommand());
//
//        // admin commands
//        commands.put("listAccount", new ListAccountCommand());
//        commands.put("unblockCard", new UnblockCardCommand());
//        commands.put("viewUserCards", new ViewUserCardsCommand());
//        commands.put("viewUserPayments", new ViewUserPaymentsCommand());
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
