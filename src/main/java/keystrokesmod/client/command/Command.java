package keystrokesmod.client.command;

import keystrokesmod.client.clickgui.raven.*;

public abstract class Command
{
    private final String name;
    private final String help;
    private final int minArgs;
    private final int maxArgs;
    private final String[] alias;
    private String[] args;
    
    public Command(final String name, final String help, final int minArgs, final int maxArgs, final String[] args, final String[] alias) {
        this.name = name;
        this.help = help;
        this.minArgs = minArgs;
        this.maxArgs = maxArgs;
        this.args = args;
        this.alias = alias;
    }
    
    public Command(final String name, final String help, final int minArgs, final int maxArgs, final String[] args) {
        this(name, help, minArgs, maxArgs, args, new String[0]);
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getHelp() {
        return this.help;
    }
    
    public int getMinArgs() {
        return this.minArgs;
    }
    
    public int getMaxArgs() {
        return this.maxArgs;
    }
    
    public String[] getArgs() {
        return this.args;
    }
    
    public void setArgs(final String[] args) {
        this.args = args;
    }
    
    public void onCall(final String[] args) {
    }
    
    public void incorrectArgs() {
        Terminal.print("Incorrect arguments! Run help " + this.getName() + " for usage info");
    }
    
    public String[] getAliases() {
        return this.alias;
    }
}
