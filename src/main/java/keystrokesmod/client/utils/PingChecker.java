package keystrokesmod.client.utils;

import net.minecraftforge.client.event.*;
import net.minecraftforge.fml.common.eventhandler.*;
import keystrokesmod.client.clickgui.raven.*;

public class PingChecker
{
    private static boolean e;
    private static long s;
    
    @SubscribeEvent
    public void onChatMessageReceived(final ClientChatReceivedEvent event) {
        if (PingChecker.e && Utils.Player.isPlayerInGame() && Utils.Java.str(event.message.func_150260_c()).startsWith("Unknown")) {
            event.setCanceled(true);
            PingChecker.e = false;
            this.getPing();
        }
    }
    
    public static void checkPing() {
        Terminal.print("Checking...");
        if (PingChecker.e) {
            Terminal.print("Please wait.");
        }
        else {
            Utils.mc.field_71439_g.func_71165_d("/...");
            PingChecker.e = true;
            PingChecker.s = System.currentTimeMillis();
        }
    }
    
    private void getPing() {
        int ping = (int)(System.currentTimeMillis() - PingChecker.s) - 20;
        if (ping < 0) {
            ping = 0;
        }
        Terminal.print("Your ping: " + ping + "ms");
        reset();
    }
    
    public static void reset() {
        PingChecker.e = false;
        PingChecker.s = 0L;
    }
}
