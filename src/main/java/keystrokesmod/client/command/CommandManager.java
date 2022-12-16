package keystrokesmod.client.command;

import keystrokesmod.client.command.commands.*;
import java.util.*;
import keystrokesmod.client.clickgui.raven.*;
import keystrokesmod.client.main.*;
import keystrokesmod.client.utils.*;

public class CommandManager
{
    public List<Command> commandList;
    public List<Command> sortedCommandList;
    
    public CommandManager() {
        this.commandList = new ArrayList<Command>();
        this.sortedCommandList = new ArrayList<Command>();
        this.addCommand(new Update());
        this.addCommand(new Help());
        this.addCommand(new SetKey());
        this.addCommand(new Discord());
        this.addCommand(new ConfigCommand());
        this.addCommand(new Clear());
        this.addCommand(new Cname());
        this.addCommand(new Debug());
        this.addCommand(new Duels());
        this.addCommand(new Fakechat());
        this.addCommand(new Nick());
        this.addCommand(new Ping());
        this.addCommand(new Shoutout());
        this.addCommand(new Uwu());
        this.addCommand(new Friends());
        this.addCommand(new VersionCommand());
        this.addCommand(new CFakeHud());
        this.addCommand(new CHideModule());
    }
    
    public void addCommand(final Command c) {
        this.commandList.add(c);
    }
    
    public List<Command> getCommandList() {
        return this.commandList;
    }
    
    public Command getCommandByName(final String name) {
        for (final Command command : this.commandList) {
            if (command.getName().equalsIgnoreCase(name)) {
                return command;
            }
            for (final String alias : command.getAliases()) {
                if (alias.equalsIgnoreCase(name)) {
                    return command;
                }
            }
        }
        return null;
    }
    
    public void noSuchCommand(final String name) {
        Terminal.print("Command '" + name + "' not found! Report this on the discord if this is an error!");
    }
    
    public void executeCommand(final String commandName, final String[] args) {
        final Command command = Raven.commandManager.getCommandByName(commandName);
        if (command == null) {
            this.noSuchCommand(commandName);
            return;
        }
        command.onCall(args);
    }
    
    public void sort() {
        this.sortedCommandList.sort((o1, o2) -> Utils.mc.field_71466_p.func_78256_a(o2.getName()) - Utils.mc.field_71466_p.func_78256_a(o1.getName()));
    }
}
