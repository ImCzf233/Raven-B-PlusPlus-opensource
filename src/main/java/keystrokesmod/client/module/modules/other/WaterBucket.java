package keystrokesmod.client.module.modules.other;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import keystrokesmod.client.module.setting.*;
import keystrokesmod.client.event.impl.*;
import keystrokesmod.client.utils.*;
import com.google.common.eventbus.*;
import net.minecraft.block.*;
import net.minecraft.init.*;
import net.minecraft.util.*;
import net.minecraft.entity.player.*;
import net.minecraft.world.*;
import net.minecraft.item.*;

public class WaterBucket extends Module
{
    public static DescriptionSetting moduleDesc;
    public static SliderSetting distance;
    private boolean handling;
    
    public WaterBucket() {
        super("Water bucket", ModuleCategory.other);
        this.registerSetting(WaterBucket.moduleDesc = new DescriptionSetting("Credits: aycy"));
        this.registerSetting(WaterBucket.moduleDesc = new DescriptionSetting("Disabled in the Nether"));
        this.registerSetting(WaterBucket.distance = new SliderSetting("Fall Distance", 3.0, 1.0, 10.0, 0.1));
    }
    
    @Override
    public boolean canBeEnabled() {
        return !DimensionHelper.isPlayerInNether();
    }
    
    @Subscribe
    public void onTick(final TickEvent ev) {
        if (Utils.Player.isPlayerInGame() && !WaterBucket.mc.func_147113_T()) {
            if (DimensionHelper.isPlayerInNether()) {
                this.disable();
            }
            if (this.inPosition() && this.holdWaterBucket()) {
                this.handling = true;
            }
            if (this.handling) {
                this.mlg();
                if (WaterBucket.mc.field_71439_g.field_70122_E || WaterBucket.mc.field_71439_g.field_70181_x > 0.0) {
                    this.reset();
                }
            }
        }
    }
    
    private boolean inPosition() {
        if (WaterBucket.mc.field_71439_g.field_70181_x < -0.6 && !WaterBucket.mc.field_71439_g.field_70122_E && !WaterBucket.mc.field_71439_g.field_71075_bZ.field_75100_b && !WaterBucket.mc.field_71439_g.field_71075_bZ.field_75098_d && !this.handling && WaterBucket.mc.field_71439_g.field_70143_R > WaterBucket.distance.getInput()) {
            final BlockPos playerPos = WaterBucket.mc.field_71439_g.func_180425_c();
            for (int i = 1; i < 3; ++i) {
                final BlockPos blockPos = playerPos.func_177979_c(i);
                final Block block = WaterBucket.mc.field_71441_e.func_180495_p(blockPos).func_177230_c();
                if (block.func_176212_b((IBlockAccess)WaterBucket.mc.field_71441_e, blockPos, EnumFacing.UP)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    
    private boolean holdWaterBucket() {
        if (this.containsItem(WaterBucket.mc.field_71439_g.func_70694_bm(), Items.field_151131_as)) {
            return true;
        }
        for (int i = 0; i < InventoryPlayer.func_70451_h(); ++i) {
            if (this.containsItem(WaterBucket.mc.field_71439_g.field_71071_by.field_70462_a[i], Items.field_151131_as)) {
                WaterBucket.mc.field_71439_g.field_71071_by.field_70461_c = i;
                return true;
            }
        }
        return false;
    }
    
    private void mlg() {
        final ItemStack heldItem = WaterBucket.mc.field_71439_g.func_70694_bm();
        if (this.containsItem(heldItem, Items.field_151131_as) && WaterBucket.mc.field_71439_g.field_70125_A >= 70.0f) {
            final MovingObjectPosition object = WaterBucket.mc.field_71476_x;
            if (object.field_72313_a == MovingObjectPosition.MovingObjectType.BLOCK && object.field_178784_b == EnumFacing.UP) {
                WaterBucket.mc.field_71442_b.func_78769_a((EntityPlayer)WaterBucket.mc.field_71439_g, (World)WaterBucket.mc.field_71441_e, heldItem);
            }
        }
    }
    
    private void reset() {
        final ItemStack heldItem = WaterBucket.mc.field_71439_g.func_70694_bm();
        if (this.containsItem(heldItem, Items.field_151133_ar)) {
            WaterBucket.mc.field_71442_b.func_78769_a((EntityPlayer)WaterBucket.mc.field_71439_g, (World)WaterBucket.mc.field_71441_e, heldItem);
        }
        this.handling = false;
    }
    
    private boolean containsItem(final ItemStack itemStack, final Item item) {
        return itemStack != null && itemStack.func_77973_b() == item;
    }
}
