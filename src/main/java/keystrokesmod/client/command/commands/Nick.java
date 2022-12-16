package keystrokesmod.client.command.commands;

import keystrokesmod.client.command.*;
import keystrokesmod.client.module.modules.other.*;
import keystrokesmod.client.clickgui.raven.*;

public class Nick extends Command
{
    public Nick() {
        super("nick", "Like nickhider mod", 1, 1, new String[] { "the new name" }, new String[] { "nk", "nickhider" });
    }
    
    @Override
    public void onCall(final String[] args) {
        if (args.length == 0) {
            this.incorrectArgs();
            return;
        }
        NameHider.playerNick = args[0];
        Terminal.print("&aNick has been set to: " + NameHider.playerNick);
    }
}
