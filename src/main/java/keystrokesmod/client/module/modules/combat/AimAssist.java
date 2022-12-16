package keystrokesmod.client.module.modules.combat;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import keystrokesmod.client.module.setting.*;
import keystrokesmod.client.event.impl.*;
import net.minecraftforge.fml.common.gameevent.*;
import keystrokesmod.client.utils.*;
import net.minecraft.init.*;
import keystrokesmod.client.main.*;
import keystrokesmod.client.module.modules.player.*;
import org.lwjgl.input.*;
import java.util.concurrent.*;
import net.minecraft.util.*;
import net.minecraft.block.*;
import net.minecraft.client.entity.*;
import com.google.common.eventbus.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.*;
import keystrokesmod.client.module.modules.world.*;
import net.minecraft.client.network.*;
import java.util.*;

public class AimAssist extends Module
{
    public static SliderSetting speed;
    public static SliderSetting compliment;
    public static SliderSetting fov;
    public static SliderSetting distance;
    public static TickSetting clickAim;
    public static TickSetting weaponOnly;
    public static TickSetting aimInvis;
    public static TickSetting breakBlocks;
    public static TickSetting blatantMode;
    public static TickSetting ignoreFriends;
    public static TickSetting ignoreNaked;
    public static ArrayList<Entity> friends;
    
    public AimAssist() {
        super("AimAssist", ModuleCategory.combat);
        this.registerSetting(AimAssist.speed = new SliderSetting("Speed 1", 45.0, 5.0, 100.0, 1.0));
        this.registerSetting(AimAssist.compliment = new SliderSetting("Speed 2", 15.0, 2.0, 97.0, 1.0));
        this.registerSetting(AimAssist.fov = new SliderSetting("FOV", 90.0, 15.0, 360.0, 1.0));
        this.registerSetting(AimAssist.distance = new SliderSetting("Distance", 4.5, 1.0, 10.0, 0.1));
        this.registerSetting(AimAssist.clickAim = new TickSetting("Click aim", true));
        this.registerSetting(AimAssist.breakBlocks = new TickSetting("Break blocks", true));
        this.registerSetting(AimAssist.ignoreFriends = new TickSetting("Ignore Friends", true));
        this.registerSetting(AimAssist.weaponOnly = new TickSetting("Weapon only", false));
        this.registerSetting(AimAssist.aimInvis = new TickSetting("Aim invis", false));
        this.registerSetting(AimAssist.blatantMode = new TickSetting("Blatant mode", false));
        this.registerSetting(AimAssist.ignoreNaked = new TickSetting("Ignore naked", false));
    }
    
    @Subscribe
    public void onRender(final ForgeEvent fe) {
        if (fe.getEvent() instanceof TickEvent.ClientTickEvent) {
            if (!Utils.Client.currentScreenMinecraft()) {
                return;
            }
            if (!Utils.Player.isPlayerInGame()) {
                return;
            }
            if (AimAssist.breakBlocks.isToggled() && AimAssist.mc.field_71476_x != null) {
                final BlockPos p = AimAssist.mc.field_71476_x.func_178782_a();
                if (p != null) {
                    final Block bl = AimAssist.mc.field_71441_e.func_180495_p(p).func_177230_c();
                    if (bl != Blocks.field_150350_a && !(bl instanceof BlockLiquid) && bl != null) {
                        return;
                    }
                }
            }
            if (!AimAssist.weaponOnly.isToggled() || Utils.Player.isPlayerHoldingWeapon()) {
                final Module autoClicker = Raven.moduleManager.getModuleByClazz(RightClicker.class);
                if ((AimAssist.clickAim.isToggled() && Utils.Client.autoClickerClicking()) || (Mouse.isButtonDown(0) && autoClicker != null && !autoClicker.isEnabled()) || !AimAssist.clickAim.isToggled()) {
                    final Entity en = this.getEnemy();
                    if (en != null) {
                        if (Raven.debugger) {
                            Utils.Player.sendMessageToSelf(this.getName() + " &e" + en.func_70005_c_());
                        }
                        if (AimAssist.blatantMode.isToggled()) {
                            Utils.Player.aim(en, 0.0f, false);
                        }
                        else {
                            final double n = Utils.Player.fovFromEntity(en);
                            if (n > 1.0 || n < -1.0) {
                                final double complimentSpeed = n * (ThreadLocalRandom.current().nextDouble(AimAssist.compliment.getInput() - 1.47328, AimAssist.compliment.getInput() + 2.48293) / 100.0);
                                final float val = (float)(-(complimentSpeed + n / (101.0 - (float)ThreadLocalRandom.current().nextDouble(AimAssist.speed.getInput() - 4.723847, AimAssist.speed.getInput()))));
                                final EntityPlayerSP field_71439_g = AimAssist.mc.field_71439_g;
                                field_71439_g.field_70177_z += val;
                            }
                        }
                    }
                }
            }
        }
    }
    
    public static boolean isAFriend(final Entity entity) {
        if (entity == AimAssist.mc.field_71439_g) {
            return true;
        }
        for (final Entity wut : AimAssist.friends) {
            if (wut.equals((Object)entity)) {
                return true;
            }
        }
        try {
            final EntityPlayer bruhentity = (EntityPlayer)entity;
            if (Raven.debugger) {
                Utils.Player.sendMessageToSelf("unformatted / " + bruhentity.func_145748_c_().func_150260_c().replace("¡ì", "%"));
                Utils.Player.sendMessageToSelf("susbstring entity / " + bruhentity.func_145748_c_().func_150260_c().substring(0, 2));
                Utils.Player.sendMessageToSelf("substring player / " + AimAssist.mc.field_71439_g.func_145748_c_().func_150260_c().substring(0, 2));
            }
            if (AimAssist.mc.field_71439_g.func_142014_c((EntityLivingBase)entity) || AimAssist.mc.field_71439_g.func_145748_c_().func_150260_c().startsWith(bruhentity.func_145748_c_().func_150260_c().substring(0, 2))) {
                return true;
            }
        }
        catch (Exception fhwhfhwe) {
            if (Raven.debugger) {
                Utils.Player.sendMessageToSelf(fhwhfhwe.getMessage());
            }
        }
        return false;
    }
    
    public Entity getEnemy() {
        final int fov = (int)AimAssist.fov.getInput();
        final List<EntityPlayer> var2 = (List<EntityPlayer>)AimAssist.mc.field_71441_e.field_73010_i;
        for (final EntityPlayer en : var2) {
            if ((AimAssist.ignoreFriends.isToggled() || !isAFriend((Entity)en)) && en != AimAssist.mc.field_71439_g && (AimAssist.aimInvis.isToggled() || !en.func_82150_aj()) && AimAssist.mc.field_71439_g.func_70032_d((Entity)en) < AimAssist.distance.getInput() && !AntiBot.bot((Entity)en) && Utils.Player.fov((Entity)en, (float)fov) && (!AimAssist.ignoreNaked.isToggled() || (en.func_82169_q(3) == null && en.func_82169_q(2) == null && en.func_82169_q(1) == null && en.func_82169_q(0) == null))) {
                return (Entity)en;
            }
        }
        return null;
    }
    
    public static void addFriend(final Entity entityPlayer) {
        AimAssist.friends.add(entityPlayer);
    }
    
    public static boolean addFriend(final String name) {
        boolean found = false;
        for (final Entity entity : AimAssist.mc.field_71441_e.func_72910_y()) {
            if ((entity.func_70005_c_().equalsIgnoreCase(name) || entity.func_95999_t().equalsIgnoreCase(name)) && !isAFriend(entity)) {
                addFriend(entity);
                found = true;
            }
        }
        return found;
    }
    
    public static boolean removeFriend(final String name) {
        boolean removed = false;
        boolean found = false;
        for (final NetworkPlayerInfo networkPlayerInfo : new ArrayList<NetworkPlayerInfo>(AimAssist.mc.func_147114_u().func_175106_d())) {
            final Entity entity = (Entity)AimAssist.mc.field_71441_e.func_72924_a(networkPlayerInfo.func_178854_k().func_150260_c());
            if (entity.func_70005_c_().equalsIgnoreCase(name) || entity.func_95999_t().equalsIgnoreCase(name)) {
                removed = removeFriend(entity);
                found = true;
            }
        }
        return found && removed;
    }
    
    public static boolean removeFriend(final Entity entityPlayer) {
        try {
            AimAssist.friends.remove(entityPlayer);
        }
        catch (Exception eeeeee) {
            eeeeee.printStackTrace();
            return false;
        }
        return true;
    }
    
    public static ArrayList<Entity> getFriends() {
        return AimAssist.friends;
    }
    
    static {
        AimAssist.friends = new ArrayList<Entity>();
    }
}
