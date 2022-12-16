package keystrokesmod.client.command.commands;

import keystrokesmod.client.command.*;
import keystrokesmod.client.module.modules.other.*;
import keystrokesmod.client.clickgui.raven.*;

public class Cname extends Command
{
    public Cname() {
        super("cname", "Hides your name client-side", 1, 1, new String[] { "New name" }, new String[] { "cn", "changename" });
    }
    
    @Override
    public void onCall(final String[] args) {
        if (args.length == 0) {
            this.incorrectArgs();
            return;
        }
        NameHider.n = args[0];
        Terminal.print("Nick has been set to: " + NameHider.n);
    }
}
