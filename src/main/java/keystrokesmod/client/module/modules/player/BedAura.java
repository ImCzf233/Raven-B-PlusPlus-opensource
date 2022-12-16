package keystrokesmod.client.module.modules.player;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import keystrokesmod.client.module.setting.*;
import java.util.*;
import keystrokesmod.client.utils.*;
import net.minecraft.init.*;
import net.minecraft.network.play.client.*;
import net.minecraft.util.*;
import net.minecraft.network.*;
import net.minecraft.client.*;

public class BedAura extends Module
{
    public static DescriptionSetting d;
    public static SliderSetting r;
    private Timer t;
    private BlockPos m;
    private final long per = 600L;
    
    public BedAura() {
        super("BedAura", ModuleCategory.player);
        this.registerSetting(BedAura.d = new DescriptionSetting("Might silent flag on Hypixel."));
        this.registerSetting(BedAura.r = new SliderSetting("Range", 5.0, 2.0, 10.0, 1.0));
    }
    
    @Override
    public void onEnable() {
        (this.t = new Timer()).scheduleAtFixedRate(this.t(), 0L, 600L);
    }
    
    @Override
    public void onDisable() {
        if (this.t != null) {
            this.t.cancel();
            this.t.purge();
            this.t = null;
        }
        this.m = null;
    }
    
    public TimerTask t() {
        return new TimerTask() {
            @Override
            public void run() {
                int y;
                for (int ra = y = (int)BedAura.r.getInput(); y >= -ra; --y) {
                    for (int x = -ra; x <= ra; ++x) {
                        for (int z = -ra; z <= ra; ++z) {
                            if (Utils.Player.isPlayerInGame()) {
                                final BlockPos p = new BlockPos(BedAura.mc.field_71439_g.field_70165_t + x, BedAura.mc.field_71439_g.field_70163_u + y, BedAura.mc.field_71439_g.field_70161_v + z);
                                final boolean bed = BedAura.mc.field_71441_e.func_180495_p(p).func_177230_c() == Blocks.field_150324_C;
                                if (BedAura.this.m == p) {
                                    if (!bed) {
                                        BedAura.this.m = null;
                                    }
                                }
                                else if (bed) {
                                    BedAura.this.mi(p);
                                    BedAura.this.m = p;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        };
    }
    
    private void mi(final BlockPos p) {
        BedAura.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, p, EnumFacing.NORTH));
        BedAura.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, p, EnumFacing.NORTH));
    }
}
