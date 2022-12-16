package keystrokesmod.client.utils;

import net.minecraftforge.client.event.*;
import keystrokesmod.client.main.*;
import net.minecraft.client.*;
import keystrokesmod.client.module.modules.world.*;
import net.minecraft.entity.*;
import net.minecraftforge.fml.common.eventhandler.*;
import java.util.*;

public class MouseManager
{
    private static final List<Long> leftClicks;
    private static final List<Long> rightClicks;
    public static long leftClickTimer;
    public static long rightClickTimer;
    
    @SubscribeEvent
    public void onMouseUpdate(final MouseEvent mouse) {
        if (mouse.buttonstate) {
            if (mouse.button == 0) {
                addLeftClick();
                if (Raven.debugger && Minecraft.func_71410_x().field_71476_x != null) {
                    final Entity en = Minecraft.func_71410_x().field_71476_x.field_72308_g;
                    if (en == null) {
                        return;
                    }
                    Utils.Player.sendMessageToSelf("&7&m-------------------------");
                    Utils.Player.sendMessageToSelf("n: " + en.func_70005_c_());
                    Utils.Player.sendMessageToSelf("rn: " + en.func_70005_c_().replace("¡ì", "%"));
                    Utils.Player.sendMessageToSelf("d: " + en.func_145748_c_().func_150260_c());
                    Utils.Player.sendMessageToSelf("rd: " + en.func_145748_c_().func_150260_c().replace("¡ì", "%"));
                    Utils.Player.sendMessageToSelf("b?: " + AntiBot.bot(en));
                }
            }
            else if (mouse.button == 1) {
                addRightClick();
            }
        }
    }
    
    public static void addLeftClick() {
        MouseManager.leftClicks.add(MouseManager.leftClickTimer = System.currentTimeMillis());
    }
    
    public static void addRightClick() {
        MouseManager.rightClicks.add(MouseManager.rightClickTimer = System.currentTimeMillis());
    }
    
    public static int getLeftClickCounter() {
        if (!Utils.Player.isPlayerInGame()) {
            return MouseManager.leftClicks.size();
        }
        for (final Long lon : MouseManager.leftClicks) {
            if (lon < System.currentTimeMillis() - 1000L) {
                MouseManager.leftClicks.remove(lon);
                break;
            }
        }
        return MouseManager.leftClicks.size();
    }
    
    public static int getRightClickCounter() {
        if (!Utils.Player.isPlayerInGame()) {
            return MouseManager.leftClicks.size();
        }
        for (final Long lon : MouseManager.rightClicks) {
            if (lon < System.currentTimeMillis() - 1000L) {
                MouseManager.rightClicks.remove(lon);
                break;
            }
        }
        return MouseManager.rightClicks.size();
    }
    
    static {
        leftClicks = new ArrayList<Long>();
        rightClicks = new ArrayList<Long>();
    }
}
