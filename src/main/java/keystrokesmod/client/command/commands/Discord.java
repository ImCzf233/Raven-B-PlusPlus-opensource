package keystrokesmod.client.command.commands;

import keystrokesmod.client.command.*;
import keystrokesmod.client.clickgui.raven.*;
import keystrokesmod.client.utils.*;

public class Discord extends Command
{
    public Discord() {
        super("discord", "Allows you to join the Raven B++ discord", 0, 3, new String[] { "copy", "open", "print" }, new String[] { "dc", "chat" });
    }
    
    @Override
    public void onCall(final String[] args) {
        boolean opened = false;
        boolean copied = false;
        boolean showed = false;
        int argCurrent = 0;
        if (args.length == 0) {
            Terminal.print("¡ì3Opening https://discord.gg/UqJ8ngteud");
            Utils.Client.openWebpage("https://discord.gg/UqJ8ngteud");
            opened = true;
            return;
        }
        for (final String argument : args) {
            if (argument.equalsIgnoreCase("copy")) {
                if (!copied) {
                    Utils.Client.copyToClipboard("https://discord.gg/UqJ8ngteud");
                    copied = true;
                    Terminal.print("Copied https://discord.gg/UqJ8ngteud to clipboard!");
                }
            }
            else if (argument.equalsIgnoreCase("open")) {
                if (!opened) {
                    Utils.Client.openWebpage("https://discord.gg/UqJ8ngteud");
                    opened = true;
                    Terminal.print("Opened invite link!");
                }
            }
            else if (argument.equalsIgnoreCase("print")) {
                if (!showed) {
                    Terminal.print("https://discord.gg/UqJ8ngteud");
                    showed = true;
                }
            }
            else if (argCurrent != 0) {
                this.incorrectArgs();
            }
            ++argCurrent;
        }
    }
}
