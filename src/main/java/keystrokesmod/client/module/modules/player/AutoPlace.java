package keystrokesmod.client.module.modules.player;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import keystrokesmod.client.module.setting.*;
import keystrokesmod.client.main.*;
import org.lwjgl.input.*;
import net.minecraft.item.*;
import com.google.common.eventbus.*;
import keystrokesmod.client.event.impl.*;
import net.minecraftforge.client.event.*;
import keystrokesmod.client.utils.*;
import net.minecraft.util.*;
import net.minecraft.init.*;
import net.minecraft.block.*;

public class AutoPlace extends Module
{
    public static DescriptionSetting ds;
    public static TickSetting a;
    public static TickSetting b;
    public static TickSetting top;
    public static SliderSetting c;
    private double lfd;
    private final int d = 25;
    private long l;
    private int f;
    private MovingObjectPosition lm;
    private BlockPos lp;
    
    public AutoPlace() {
        super("AutoPlace", ModuleCategory.player);
        this.registerSetting(AutoPlace.ds = new DescriptionSetting("FD: FPS/80"));
        this.registerSetting(AutoPlace.c = new SliderSetting("Frame delay", 8.0, 0.0, 30.0, 1.0));
        this.registerSetting(AutoPlace.a = new TickSetting("Hold right", true));
        this.registerSetting(AutoPlace.top = new TickSetting("Place on top & bottom", false));
    }
    
    @Override
    public void guiUpdate() {
        if (this.lfd != AutoPlace.c.getInput()) {
            this.rv();
        }
        this.lfd = AutoPlace.c.getInput();
    }
    
    @Override
    public void onDisable() {
        if (AutoPlace.a.isToggled()) {
            this.rd(4);
        }
        this.rv();
    }
    
    @Subscribe
    public void onTick(final TickEvent e) {
        final Module fastPlace = Raven.moduleManager.getModuleByClazz(FastPlace.class);
        if (AutoPlace.a.isToggled() && Mouse.isButtonDown(1) && !AutoPlace.mc.field_71439_g.field_71075_bZ.field_75100_b && fastPlace != null && !fastPlace.isEnabled()) {
            final ItemStack i = AutoPlace.mc.field_71439_g.func_70694_bm();
            if (i == null || !(i.func_77973_b() instanceof ItemBlock)) {
                return;
            }
            this.rd((AutoPlace.mc.field_71439_g.field_70181_x > 0.0) ? 1 : 1000);
        }
    }
    
    @Subscribe
    public void onForgeEvent(final ForgeEvent fe) {
        if (fe.getEvent() instanceof DrawBlockHighlightEvent && Utils.Player.isPlayerInGame() && AutoPlace.mc.field_71462_r == null && !AutoPlace.mc.field_71439_g.field_71075_bZ.field_75100_b) {
            final ItemStack i = AutoPlace.mc.field_71439_g.func_70694_bm();
            if (i != null && i.func_77973_b() instanceof ItemBlock) {
                final MovingObjectPosition m = AutoPlace.mc.field_71476_x;
                if (m != null && m.field_72313_a == MovingObjectPosition.MovingObjectType.BLOCK && ((m.field_178784_b != EnumFacing.UP && m.field_178784_b != EnumFacing.DOWN) || AutoPlace.top.isToggled())) {
                    if (this.lm != null && this.f < AutoPlace.c.getInput()) {
                        ++this.f;
                    }
                    else {
                        this.lm = m;
                        final BlockPos pos = m.func_178782_a();
                        if (this.lp == null || pos.func_177958_n() != this.lp.func_177958_n() || pos.func_177956_o() != this.lp.func_177956_o() || pos.func_177952_p() != this.lp.func_177952_p()) {
                            final Block b = AutoPlace.mc.field_71441_e.func_180495_p(pos).func_177230_c();
                            if (b != null && b != Blocks.field_150350_a && !(b instanceof BlockLiquid) && (!AutoPlace.a.isToggled() || Mouse.isButtonDown(1))) {
                                final long n = System.currentTimeMillis();
                                if (n - this.l >= 25L) {
                                    this.l = n;
                                    if (AutoPlace.mc.field_71442_b.func_178890_a(AutoPlace.mc.field_71439_g, AutoPlace.mc.field_71441_e, i, pos, m.field_178784_b, m.field_72307_f)) {
                                        Utils.Client.setMouseButtonState(1, true);
                                        AutoPlace.mc.field_71439_g.func_71038_i();
                                        AutoPlace.mc.func_175597_ag().func_78444_b();
                                        Utils.Client.setMouseButtonState(1, false);
                                        this.lp = pos;
                                        this.f = 0;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    private void rd(final int i) {
        try {
            if (FastPlace.rightClickDelayTimerField != null) {
                FastPlace.rightClickDelayTimerField.set(AutoPlace.mc, i);
            }
        }
        catch (IllegalAccessException ex) {}
        catch (IndexOutOfBoundsException ex2) {}
    }
    
    private void rv() {
        this.lp = null;
        this.lm = null;
        this.f = 0;
    }
}
