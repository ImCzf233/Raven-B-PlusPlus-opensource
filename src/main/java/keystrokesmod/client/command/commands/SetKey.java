package keystrokesmod.client.command.commands;

import keystrokesmod.client.command.*;
import keystrokesmod.client.clickgui.raven.*;
import keystrokesmod.client.main.*;
import keystrokesmod.client.utils.*;

public class SetKey extends Command
{
    public SetKey() {
        super("setkey", "Sets hypixel's API key. To get a new key, run `/api new`", 2, 2, new String[] { "key" }, new String[] { "apikey" });
    }
    
    @Override
    public void onCall(final String[] args) {
        if (args.length == 0) {
            this.incorrectArgs();
            return;
        }
        Terminal.print("Setting...");
        final String n = args[0];
        final String s;
        Raven.getExecutor().execute(() -> {
            if (Utils.URLS.isHypixelKeyValid(s)) {
                Utils.URLS.hypixelApiKey = s;
                Terminal.print("Success!");
                Raven.clientConfig.saveConfig();
            }
            else {
                Terminal.print("Invalid key.");
            }
        });
    }
}
