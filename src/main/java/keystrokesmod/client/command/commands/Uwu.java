package keystrokesmod.client.command.commands;

import keystrokesmod.client.command.*;
import keystrokesmod.client.main.*;
import keystrokesmod.client.clickgui.raven.*;

public class Uwu extends Command
{
    private static boolean u;
    
    public Uwu() {
        super("uwu", "hevex/blowsy added this lol", 0, 0, new String[0], new String[] { "hevex", "blowsy", "weeb", "torture", "noplsno" });
        Uwu.u = false;
    }
    
    @Override
    public void onCall(final String[] args) {
        if (Uwu.u) {
            return;
        }
        int i;
        Raven.getExecutor().execute(() -> {
            Uwu.u = true;
            for (i = 0; i < 4; ++i) {
                if (i == 0) {
                    Terminal.print("nya");
                }
                else if (i == 1) {
                    Terminal.print("ichi ni san");
                }
                else if (i == 2) {
                    Terminal.print("nya");
                }
                else {
                    Terminal.print("arigatou!");
                }
                try {
                    Thread.sleep(500L);
                }
                catch (InterruptedException ex) {}
            }
            Uwu.u = false;
        });
    }
}
