package keystrokesmod.client.module.modules.minigames;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import net.minecraft.entity.player.*;
import keystrokesmod.client.module.setting.*;
import keystrokesmod.client.event.impl.*;
import net.minecraftforge.client.event.*;
import keystrokesmod.client.utils.*;
import keystrokesmod.client.module.modules.render.*;
import keystrokesmod.client.main.*;
import keystrokesmod.client.module.modules.world.*;
import net.minecraft.entity.*;
import java.awt.*;
import net.minecraft.item.*;
import com.google.common.eventbus.*;
import java.util.*;

public class MurderMystery extends Module
{
    public static TickSetting alertMurderers;
    public static TickSetting searchDetectives;
    public static TickSetting announceMurder;
    private static final List<EntityPlayer> mur;
    private static final List<EntityPlayer> det;
    
    public MurderMystery() {
        super("Murder Mystery", ModuleCategory.minigames);
        this.registerSetting(MurderMystery.alertMurderers = new TickSetting("Alert", true));
        this.registerSetting(MurderMystery.searchDetectives = new TickSetting("Search detectives", true));
        this.registerSetting(MurderMystery.announceMurder = new TickSetting("Announce murderer", false));
    }
    
    @Subscribe
    public void onForgeEvent(final ForgeEvent fe) {
        if (fe.getEvent() instanceof RenderWorldLastEvent && Utils.Player.isPlayerInGame()) {
            final PlayerESP p = (PlayerESP)Raven.moduleManager.getModuleByName("PlayerESP");
            assert p != null;
            if (p.isEnabled()) {
                p.disable();
            }
            if (this.inMMGame()) {
                for (final EntityPlayer entity : MurderMystery.mc.field_71441_e.field_73010_i) {
                    if (entity != MurderMystery.mc.field_71439_g && !entity.func_82150_aj() && !AntiBot.bot((Entity)entity)) {
                        final String c4 = "&7[&cALERT&7]";
                        if (entity.func_70694_bm() != null && entity.func_70694_bm().func_82837_s()) {
                            final Item i = entity.func_70694_bm().func_77973_b();
                            if (i instanceof ItemSword || i instanceof ItemAxe || i instanceof ItemEnderPearl || i instanceof ItemHoe || i instanceof ItemPickaxe || i instanceof ItemFishingRod || entity.func_70694_bm().func_82833_r().contains("Knife")) {
                                if (!MurderMystery.mur.contains(entity)) {
                                    MurderMystery.mur.add(entity);
                                    final String c5 = "is a murderer!";
                                    if (MurderMystery.alertMurderers.isToggled()) {
                                        final String c6 = "note.pling";
                                        MurderMystery.mc.field_71439_g.func_85030_a(c6, 1.0f, 1.0f);
                                        Utils.Player.sendMessageToSelf(c4 + " &e" + entity.func_70005_c_() + " &3" + c5);
                                    }
                                    if (MurderMystery.announceMurder.isToggled()) {
                                        final String msg = Utils.Java.randomChoice(new String[] { entity.func_70005_c_() + " " + c5, entity.func_70005_c_() });
                                        MurderMystery.mc.field_71439_g.func_71165_d(msg);
                                    }
                                }
                            }
                            else if (i instanceof ItemBow && MurderMystery.searchDetectives.isToggled() && !MurderMystery.det.contains(entity)) {
                                MurderMystery.det.add(entity);
                                final String c7 = "has a bow!";
                                if (MurderMystery.alertMurderers.isToggled()) {
                                    Utils.Player.sendMessageToSelf(c4 + " &e" + entity.func_70005_c_() + " &3" + c7);
                                }
                                if (MurderMystery.announceMurder.isToggled()) {
                                    MurderMystery.mc.field_71439_g.func_71165_d(entity.func_70005_c_() + " " + c7);
                                }
                            }
                        }
                        int rgb = Color.cyan.getRGB();
                        if (MurderMystery.mur.contains(entity)) {
                            rgb = Color.red.getRGB();
                        }
                        else if (MurderMystery.det.contains(entity)) {
                            rgb = Color.green.getRGB();
                        }
                        Utils.HUD.drawBoxAroundEntity((Entity)entity, 2, 0.0, 0.0, rgb, false);
                    }
                }
                return;
            }
            this.c();
        }
    }
    
    private boolean inMMGame() {
        if (Utils.Client.isHyp()) {
            if (MurderMystery.mc.field_71439_g.func_96123_co() == null || MurderMystery.mc.field_71439_g.func_96123_co().func_96539_a(1) == null) {
                return false;
            }
            final String d = MurderMystery.mc.field_71439_g.func_96123_co().func_96539_a(1).func_96678_d();
            final String c2 = "MYSTERY";
            final String c3 = "MURDER";
            if (!d.contains(c3) && !d.contains(c2)) {
                return false;
            }
            for (final String l : Utils.Client.getPlayersFromScoreboard()) {
                final String s = Utils.Java.str(l);
                final String c4 = "Role:";
                if (s.contains(c4)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private void c() {
        MurderMystery.mur.clear();
        MurderMystery.det.clear();
    }
    
    static {
        mur = new ArrayList<EntityPlayer>();
        det = new ArrayList<EntityPlayer>();
    }
}
