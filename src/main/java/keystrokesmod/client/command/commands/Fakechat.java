package keystrokesmod.client.command.commands;

import keystrokesmod.client.command.*;
import keystrokesmod.client.utils.*;
import keystrokesmod.client.clickgui.raven.*;
import keystrokesmod.client.module.modules.other.*;

public class Fakechat extends Command
{
    public Fakechat() {
        super("fakechat", "Sends a message in chat", 1, 600, new String[] { "text" }, new String[] { "fkct" });
    }
    
    @Override
    public void onCall(final String[] args) {
        if (args.length == 0) {
            this.incorrectArgs();
            return;
        }
        final String c = Utils.Java.joinStringList(args, " ");
        final String n = c.replaceFirst("fakechat", "").substring(1);
        if (n.isEmpty() || n.equals("\\n")) {
            Terminal.print("&cInvalid message.");
            return;
        }
        FakeChat.msg = n;
        Terminal.print("Message set!");
    }
}
