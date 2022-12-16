package keystrokesmod.client.module.modules.combat;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import keystrokesmod.client.module.setting.*;
import net.minecraftforge.event.entity.living.*;
import keystrokesmod.client.utils.*;
import org.lwjgl.input.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.client.entity.*;
import com.google.common.eventbus.*;
import keystrokesmod.client.event.impl.*;
import net.minecraft.network.play.server.*;

public class Velocity extends Module
{
    public static SliderSetting a;
    public static SliderSetting b;
    public static SliderSetting c;
    public static SliderSetting ap;
    public static SliderSetting bp;
    public static SliderSetting cp;
    public static SliderSetting dt;
    public static TickSetting d;
    public static TickSetting e;
    public static TickSetting f;
    public static ComboSetting g;
    public static ComboSetting veloMode;
    public Mode mode;
    
    public Velocity() {
        super("Velocity", ModuleCategory.combat);
        this.mode = Mode.Distance;
        this.registerSetting(Velocity.veloMode = new ComboSetting("Mode", (T)VeloMode.Old));
        this.registerSetting(Velocity.a = new SliderSetting("Horizontal", 90.0, -100.0, 100.0, 1.0));
        this.registerSetting(Velocity.b = new SliderSetting("Vertical", 100.0, -100.0, 100.0, 1.0));
        this.registerSetting(Velocity.c = new SliderSetting("Chance", 100.0, 0.0, 100.0, 1.0));
        this.registerSetting(Velocity.d = new TickSetting("Only while targeting", false));
        this.registerSetting(Velocity.e = new TickSetting("Disable while holding S", false));
        this.registerSetting(Velocity.f = new TickSetting("Different velo for projectiles", false));
        this.registerSetting(Velocity.g = new ComboSetting("Projectiles Mode", (T)this.mode));
        this.registerSetting(Velocity.ap = new SliderSetting("Horizontal projectiles", 90.0, -100.0, 100.0, 1.0));
        this.registerSetting(Velocity.bp = new SliderSetting("Vertical projectiles", 100.0, -100.0, 100.0, 1.0));
        this.registerSetting(Velocity.cp = new SliderSetting("Chance projectiles", 100.0, 0.0, 100.0, 1.0));
        this.registerSetting(Velocity.dt = new SliderSetting("Distance projectiles", 3.0, 0.0, 20.0, 0.1));
    }
    
    @Subscribe
    public void onLivingUpdate(final ForgeEvent fe) {
        if (fe.getEvent() instanceof LivingEvent.LivingUpdateEvent) {
            if (Velocity.veloMode.getMode() != VeloMode.Old) {
                return;
            }
            if (Utils.Player.isPlayerInGame() && Velocity.mc.field_71439_g.field_70738_aO > 0 && Velocity.mc.field_71439_g.field_70737_aN == Velocity.mc.field_71439_g.field_70738_aO) {
                if (Velocity.d.isToggled() && (Velocity.mc.field_71476_x == null || Velocity.mc.field_71476_x.field_72308_g == null)) {
                    return;
                }
                if (Velocity.e.isToggled() && Keyboard.isKeyDown(Velocity.mc.field_71474_y.field_74368_y.func_151463_i())) {
                    return;
                }
                if (Velocity.mc.field_71439_g.func_110144_aD() instanceof EntityPlayer) {
                    final EntityPlayer attacker = (EntityPlayer)Velocity.mc.field_71439_g.func_110144_aD();
                    final Item item = (attacker.func_71045_bC() != null) ? attacker.func_71045_bC().func_77973_b() : null;
                    if ((item instanceof ItemEgg || item instanceof ItemBow || item instanceof ItemSnow || item instanceof ItemFishingRod) && this.mode == Mode.ItemHeld) {
                        this.velo();
                        return;
                    }
                    if (attacker.func_70032_d((Entity)Velocity.mc.field_71439_g) > Velocity.dt.getInput()) {
                        this.velo();
                        return;
                    }
                }
                if (Velocity.c.getInput() != 100.0) {
                    final double ch = Math.random();
                    if (ch >= Velocity.c.getInput() / 100.0) {
                        return;
                    }
                }
                if (Velocity.a.getInput() != 100.0) {
                    final EntityPlayerSP field_71439_g = Velocity.mc.field_71439_g;
                    field_71439_g.field_70159_w *= Velocity.a.getInput() / 100.0;
                    final EntityPlayerSP field_71439_g2 = Velocity.mc.field_71439_g;
                    field_71439_g2.field_70179_y *= Velocity.a.getInput() / 100.0;
                }
                if (Velocity.b.getInput() != 100.0) {
                    final EntityPlayerSP field_71439_g3 = Velocity.mc.field_71439_g;
                    field_71439_g3.field_70181_x *= Velocity.b.getInput() / 100.0;
                }
            }
        }
    }
    
    @Subscribe
    public void onPacket(final PacketEvent e) {
        if (Velocity.veloMode.getMode() != VeloMode.Cancel) {
            return;
        }
        if (e.getPacket() instanceof S12PacketEntityVelocity) {
            e.cancel();
        }
    }
    
    public void velo() {
        if (Velocity.cp.getInput() != 100.0) {
            final double ch = Math.random();
            if (ch >= Velocity.cp.getInput() / 100.0) {
                return;
            }
        }
        if (Velocity.ap.getInput() != 100.0) {
            final EntityPlayerSP field_71439_g = Velocity.mc.field_71439_g;
            field_71439_g.field_70159_w *= Velocity.ap.getInput() / 100.0;
            final EntityPlayerSP field_71439_g2 = Velocity.mc.field_71439_g;
            field_71439_g2.field_70179_y *= Velocity.ap.getInput() / 100.0;
        }
        if (Velocity.bp.getInput() != 100.0) {
            final EntityPlayerSP field_71439_g3 = Velocity.mc.field_71439_g;
            field_71439_g3.field_70181_x *= Velocity.bp.getInput() / 100.0;
        }
    }
    
    public enum Mode
    {
        Distance, 
        ItemHeld;
    }
    
    public enum VeloMode
    {
        Old, 
        Cancel;
    }
}
