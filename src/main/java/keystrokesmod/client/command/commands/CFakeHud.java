package keystrokesmod.client.command.commands;

import keystrokesmod.client.command.*;
import keystrokesmod.client.module.modules.client.*;
import keystrokesmod.client.clickgui.raven.*;

public class CFakeHud extends Command
{
    public CFakeHud() {
        super("fakehud", "fakehud add <Name>, fakehud remove <Name>", 3, 100, new String[] { "add/remove" }, new String[] { "fh" });
    }
    
    @Override
    public void onCall(final String[] args) {
        final String s = args[0];
        switch (s) {
            case "add": {
                for (int i = 1; i < args.length; ++i) {
                    FakeHud.addModule(args[i]);
                    Terminal.print("added " + args[i] + "!");
                }
                break;
            }
            case "remove": {
                for (int i = 1; i < args.length; ++i) {
                    FakeHud.removeModule(args[i]);
                    Terminal.print("removed " + args[i] + "!");
                }
                break;
            }
            default: {
                Terminal.print("incorrect arguments");
                break;
            }
        }
    }
}
