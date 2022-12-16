package keystrokesmod.client.module.modules.minigames;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import net.minecraft.block.state.*;
import net.minecraft.init.*;
import keystrokesmod.client.module.setting.*;
import keystrokesmod.client.event.impl.*;
import net.minecraftforge.client.event.*;
import keystrokesmod.client.utils.*;
import net.minecraft.util.*;
import org.lwjgl.input.*;
import com.google.common.eventbus.*;
import java.util.*;
import net.minecraft.client.*;

public class SumoFences extends Module
{
    public static DescriptionSetting a;
    public static DescriptionSetting d;
    public static SliderSetting b;
    public static SliderSetting c;
    private Timer t;
    private final List<String> m;
    private IBlockState f;
    private static final List<BlockPos> f_p;
    private final String c1;
    private final String c2;
    private final String c3;
    private final String c4;
    private final String c5;
    private int ymod;
    
    public SumoFences() {
        super("Sumo Fences", ModuleCategory.minigames);
        this.m = Arrays.asList("Sumo", "Space Mine", "White Crystal");
        this.f = Blocks.field_180407_aO.func_176223_P();
        this.c1 = "Mode: Sumo Duel";
        this.c2 = "Oak fence";
        this.c3 = "Leaves";
        this.c4 = "Glass";
        this.c5 = "Barrier";
        this.registerSetting(SumoFences.a = new DescriptionSetting("Fences for Hypixel sumo."));
        this.registerSetting(SumoFences.b = new SliderSetting("Fence height", 4.0, 1.0, 16.0, 1.0));
        this.registerSetting(SumoFences.c = new SliderSetting("Block type", 1.0, 1.0, 4.0, 1.0));
        this.registerSetting(SumoFences.d = new DescriptionSetting("Mode: " + this.c2));
    }
    
    @Override
    public void onEnable() {
        (this.t = new Timer()).scheduleAtFixedRate(this.t(), 0L, 500L);
    }
    
    @Override
    public void onDisable() {
        if (this.t != null) {
            this.t.cancel();
            this.t.purge();
            this.t = null;
        }
        for (final BlockPos p : SumoFences.f_p) {
            for (int i = 0; i < SumoFences.b.getInput(); ++i) {
                final BlockPos p2 = new BlockPos(p.func_177958_n(), p.func_177956_o() + i, p.func_177952_p());
                if (SumoFences.mc.field_71441_e.func_180495_p(p2).func_177230_c() == this.f) {
                    SumoFences.mc.field_71441_e.func_175656_a(p2, Blocks.field_150350_a.func_176223_P());
                }
            }
        }
    }
    
    @Subscribe
    public void onForgeEvent(final ForgeEvent fe) {
        if (fe.getEvent() instanceof MouseEvent) {
            final MouseEvent e = (MouseEvent)fe.getEvent();
            if (e.buttonstate && (e.button == 0 || e.button == 1) && Utils.Player.isPlayerInGame() && this.is()) {
                final MovingObjectPosition mop = SumoFences.mc.field_71476_x;
                if (mop != null && mop.field_72313_a == MovingObjectPosition.MovingObjectType.BLOCK) {
                    final int x = mop.func_178782_a().func_177958_n();
                    final int z = mop.func_178782_a().func_177952_p();
                    for (final BlockPos pos : SumoFences.f_p) {
                        if (pos.func_177958_n() == x && pos.func_177952_p() == z) {
                            e.setCanceled(true);
                            if (e.button == 0) {
                                Utils.Player.swing();
                            }
                            Mouse.poll();
                            break;
                        }
                    }
                }
            }
        }
    }
    
    public TimerTask t() {
        return new TimerTask() {
            @Override
            public void run() {
                if (SumoFences.this.is()) {
                    for (final BlockPos p : SumoFences.f_p) {
                        for (int i = 0; i < SumoFences.b.getInput(); ++i) {
                            final BlockPos p2 = new BlockPos(p.func_177958_n(), p.func_177956_o() + i + SumoFences.this.ymod, p.func_177952_p());
                            if (SumoFences.mc.field_71441_e.func_180495_p(p2).func_177230_c() == Blocks.field_150350_a) {
                                SumoFences.mc.field_71441_e.func_175656_a(p2, SumoFences.this.f);
                            }
                        }
                    }
                }
            }
        };
    }
    
    private boolean is() {
        if (Utils.Client.isHyp()) {
            for (final String l : Utils.Client.getPlayersFromScoreboard()) {
                final String s = Utils.Java.str(l);
                if (s.startsWith("Map:")) {
                    if (this.m.contains(s.substring(5))) {
                        this.ymod = (s.contains("Fort Royale") ? 7 : 0);
                        return true;
                    }
                    continue;
                }
                else {
                    if (s.equals(this.c1)) {
                        return true;
                    }
                    continue;
                }
            }
        }
        return false;
    }
    
    @Override
    public void guiUpdate() {
        switch ((int)SumoFences.c.getInput()) {
            case 1: {
                this.f = Blocks.field_180407_aO.func_176223_P();
                SumoFences.d.setDesc("Mode: " + this.c2);
                break;
            }
            case 2: {
                this.f = Blocks.field_150362_t.func_176223_P();
                SumoFences.d.setDesc("Mode: " + this.c3);
                break;
            }
            case 3: {
                this.f = Blocks.field_150359_w.func_176223_P();
                SumoFences.d.setDesc("Mode: " + this.c4);
                break;
            }
            case 4: {
                this.f = Blocks.field_180401_cv.func_176223_P();
                SumoFences.d.setDesc("Mode: " + this.c5);
                break;
            }
        }
    }
    
    static {
        f_p = Arrays.asList(new BlockPos(9, 65, -2), new BlockPos(9, 65, -1), new BlockPos(9, 65, 0), new BlockPos(9, 65, 1), new BlockPos(9, 65, 2), new BlockPos(9, 65, 3), new BlockPos(8, 65, 3), new BlockPos(8, 65, 4), new BlockPos(8, 65, 5), new BlockPos(7, 65, 5), new BlockPos(7, 65, 6), new BlockPos(7, 65, 7), new BlockPos(6, 65, 7), new BlockPos(5, 65, 7), new BlockPos(5, 65, 8), new BlockPos(4, 65, 8), new BlockPos(3, 65, 8), new BlockPos(3, 65, 9), new BlockPos(2, 65, 9), new BlockPos(1, 65, 9), new BlockPos(0, 65, 9), new BlockPos(-1, 65, 9), new BlockPos(-2, 65, 9), new BlockPos(-3, 65, 9), new BlockPos(-3, 65, 8), new BlockPos(-4, 65, 8), new BlockPos(-5, 65, 8), new BlockPos(-5, 65, 7), new BlockPos(-6, 65, 7), new BlockPos(-7, 65, 7), new BlockPos(-7, 65, 6), new BlockPos(-7, 65, 5), new BlockPos(-8, 65, 5), new BlockPos(-8, 65, 4), new BlockPos(-8, 65, 3), new BlockPos(-9, 65, 3), new BlockPos(-9, 65, 2), new BlockPos(-9, 65, 1), new BlockPos(-9, 65, 0), new BlockPos(-9, 65, -1), new BlockPos(-9, 65, -2), new BlockPos(-9, 65, -3), new BlockPos(-8, 65, -3), new BlockPos(-8, 65, -4), new BlockPos(-8, 65, -5), new BlockPos(-7, 65, -5), new BlockPos(-7, 65, -6), new BlockPos(-7, 65, -7), new BlockPos(-6, 65, -7), new BlockPos(-5, 65, -7), new BlockPos(-5, 65, -8), new BlockPos(-4, 65, -8), new BlockPos(-3, 65, -8), new BlockPos(-3, 65, -9), new BlockPos(-2, 65, -9), new BlockPos(-1, 65, -9), new BlockPos(0, 65, -9), new BlockPos(1, 65, -9), new BlockPos(2, 65, -9), new BlockPos(3, 65, -9), new BlockPos(3, 65, -8), new BlockPos(4, 65, -8), new BlockPos(5, 65, -8), new BlockPos(5, 65, -7), new BlockPos(6, 65, -7), new BlockPos(7, 65, -7), new BlockPos(7, 65, -6), new BlockPos(7, 65, -5), new BlockPos(8, 65, -5), new BlockPos(8, 65, -4), new BlockPos(8, 65, -3), new BlockPos(9, 65, -3));
    }
}
