package keystrokesmod.client.module.modules.world;

import keystrokesmod.client.module.*;
import java.util.*;
import net.minecraft.entity.player.*;
import keystrokesmod.client.module.setting.impl.*;
import keystrokesmod.client.module.setting.*;
import net.minecraftforge.event.entity.*;
import keystrokesmod.client.utils.*;
import com.google.common.eventbus.*;
import keystrokesmod.client.event.impl.*;
import net.minecraft.entity.*;
import keystrokesmod.client.module.modules.player.*;
import keystrokesmod.client.main.*;

public class AntiBot extends Module
{
    private static final HashMap<EntityPlayer, Long> newEnt;
    private final long ms = 4000L;
    public static TickSetting a;
    
    public AntiBot() {
        super("AntiBot", ModuleCategory.world);
        this.withEnabled(true);
        this.registerSetting(AntiBot.a = new TickSetting("Wait 80 ticks", false));
    }
    
    @Override
    public void onDisable() {
        AntiBot.newEnt.clear();
    }
    
    @Subscribe
    public void onEntityJoinWorld(final ForgeEvent fe) {
        if (fe.getEvent() instanceof EntityJoinWorldEvent) {
            final EntityJoinWorldEvent event = (EntityJoinWorldEvent)fe.getEvent();
            if (!Utils.Player.isPlayerInGame()) {
                return;
            }
            if (AntiBot.a.isToggled() && event.entity instanceof EntityPlayer && event.entity != AntiBot.mc.field_71439_g) {
                AntiBot.newEnt.put((EntityPlayer)event.entity, System.currentTimeMillis());
            }
        }
    }
    
    @Subscribe
    public void onTick(final TickEvent ev) {
        if (AntiBot.a.isToggled() && !AntiBot.newEnt.isEmpty()) {
            final long now = System.currentTimeMillis();
            AntiBot.newEnt.values().removeIf(e -> e < now - 4000L);
        }
    }
    
    public static boolean bot(final Entity en) {
        if (!Utils.Player.isPlayerInGame() || AntiBot.mc.field_71462_r != null) {
            return false;
        }
        if (Freecam.en != null && Freecam.en == en) {
            return true;
        }
        final Module antiBot = Raven.moduleManager.getModuleByClazz(AntiBot.class);
        if (antiBot != null && !antiBot.isEnabled()) {
            return false;
        }
        if (!Utils.Client.isHyp()) {
            return false;
        }
        if (AntiBot.a.isToggled() && !AntiBot.newEnt.isEmpty() && AntiBot.newEnt.containsKey(en)) {
            return true;
        }
        if (en.func_70005_c_().startsWith("¡ìc")) {
            return true;
        }
        final String n = en.func_145748_c_().func_150260_c();
        if (n.contains("¡ì")) {
            return n.contains("[NPC] ");
        }
        if (n.isEmpty() && en.func_70005_c_().isEmpty()) {
            return true;
        }
        if (n.length() == 10) {
            int num = 0;
            int let = 0;
            final char[] charArray;
            final char[] var4 = charArray = n.toCharArray();
            for (final char c : charArray) {
                if (Character.isLetter(c)) {
                    if (Character.isUpperCase(c)) {
                        return false;
                    }
                    ++let;
                }
                else {
                    if (!Character.isDigit(c)) {
                        return false;
                    }
                    ++num;
                }
            }
            return num >= 2 && let >= 2;
        }
        return false;
    }
    
    static {
        newEnt = new HashMap<EntityPlayer, Long>();
    }
}
