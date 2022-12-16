package keystrokesmod.client.command.commands;

import keystrokesmod.client.command.*;
import keystrokesmod.client.clickgui.raven.*;

public class Shoutout extends Command
{
    public Shoutout() {
        super("shoutout", "Everyone who helped make b+", 0, 0, new String[0], new String[] { "love", "thanks" });
    }
    
    @Override
    public void onCall(final String[] args) {
        Terminal.print("Everyone who made b++ possible:");
        Terminal.print("- kv! aka KingVoid (current dev)");
        Terminal.print("- kopamed (raven b+ dev)");
        Terminal.print("- hevex/blowsy (weeaboo, b3 dev) (disapproves to b+ as he earned less money because less ppl clicked on his adfly link)");
        Terminal.print("- blowsy (hevex's alt)");
        Terminal.print("- jmraichdev (client dev)");
        Terminal.print("- nighttab (website dev)");
        Terminal.print("- mood (java help)");
        Terminal.print("- jc (b3 b2 betta tester, very good moaner (moans very loudly in discord vcs, giving everyone emotional motivation))");
        Terminal.print("- sigmaclientwastaken (broke tlauncher compatibility, aimassist, hitboxes and many other things)");
    }
}
