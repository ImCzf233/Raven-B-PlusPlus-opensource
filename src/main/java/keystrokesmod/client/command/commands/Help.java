package keystrokesmod.client.command.commands;

import keystrokesmod.client.command.*;
import keystrokesmod.client.main.*;
import keystrokesmod.client.clickgui.raven.*;
import java.util.*;

public class Help extends Command
{
    public Help() {
        super("help", "Shows you different command usages", 0, 1, new String[] { "name of module" }, new String[] { "?", "wtf", "what" });
    }
    
    @Override
    public void onCall(final String[] args) {
        if (args.length == 0) {
            Raven.commandManager.sort();
            Terminal.print("Available commands:");
            int index = 1;
            for (final Command command : Raven.commandManager.getCommandList()) {
                if (command.getName().equalsIgnoreCase("help")) {
                    continue;
                }
                Terminal.print(index + ") " + command.getName());
                ++index;
            }
            Terminal.print("Run \"help commandname\" for more information about the command");
        }
        else if (args.length == 1) {
            final Command command2 = Raven.commandManager.getCommandByName(args[0]);
            if (command2 == null) {
                Terminal.print("Unable to find a command with the cname or alias with " + args[0]);
                return;
            }
            Terminal.print(command2.getName() + "'s info:");
            if (command2.getAliases() != null || command2.getAliases().length != 0) {
                Terminal.print(command2.getName() + "'s aliases:");
                for (final String alias : command2.getAliases()) {
                    Terminal.print(alias);
                }
            }
            if (!command2.getHelp().isEmpty()) {
                Terminal.print(command2.getName() + "'s description:");
                Terminal.print(command2.getHelp());
            }
            if (command2.getArgs() != null) {
                Terminal.print(command2.getName() + "'s argument description:");
                Terminal.print("Min args: " + command2.getMinArgs() + ", max args: " + command2.getMaxArgs());
                int argIndex = 1;
                for (final String argText : command2.getArgs()) {
                    Terminal.print("Argument " + argIndex + ": " + argText);
                    ++argIndex;
                }
            }
        }
    }
}
