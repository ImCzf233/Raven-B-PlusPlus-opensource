package keystrokesmod.client.module.modules.world;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import keystrokesmod.client.module.setting.*;
import com.google.common.eventbus.*;
import net.minecraft.client.gui.*;
import keystrokesmod.client.event.impl.*;
import net.minecraft.network.play.client.*;
import keystrokesmod.client.utils.*;
import net.minecraft.network.*;
import net.minecraft.client.entity.*;
import net.minecraft.item.*;
import net.minecraft.world.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.client.*;
import net.minecraft.util.*;
import net.minecraft.block.properties.*;
import org.apache.commons.lang3.*;
import net.minecraft.block.state.*;
import net.minecraft.block.*;

public class Scaffold extends Module
{
    private static final BlockPos[] BLOCK_POSITIONS;
    private static final EnumFacing[] FACINGS;
    private static Scaffold instance;
    private int slot;
    private float lastYaw;
    private float lastPitch;
    public static TickSetting tower;
    public static TickSetting sprint;
    public static TickSetting safewalk;
    public static TickSetting swing;
    public static TickSetting keepY;
    public static SliderSetting speed;
    public static SliderSetting blockSlot;
    public static SliderSetting delay;
    private final MillisTimer clickTimer;
    private int blockCount;
    private int originalHotBarSlot;
    private int bestBlockStack;
    private BlockData data;
    private float[] angles;
    private int placeCounter;
    private int ticksSincePlace;
    private int lastPos;
    private boolean towering;
    
    public Scaffold() {
        super("Scaffold", ModuleCategory.world);
        this.clickTimer = new MillisTimer();
        this.registerSettings(Scaffold.tower = new TickSetting("Tower", true), Scaffold.sprint = new TickSetting("Allow Sprint", false), Scaffold.safewalk = new TickSetting("Safewalk", true), Scaffold.swing = new TickSetting("Show Swing", false), Scaffold.keepY = new TickSetting("Keep Y", false), Scaffold.speed = new SliderSetting("Move Speed", 1.0, 0.5, 2.0, 0.05), Scaffold.blockSlot = new SliderSetting("Block Slot", 9.0, 1.0, 9.0, 1.0), Scaffold.delay = new SliderSetting("Delay Ticks", 3.0, 0.0, 15.0, 1.0));
        Scaffold.instance = this;
    }
    
    @Override
    public void onEnable() {
        this.blockCount = 0;
        this.placeCounter = 0;
        this.ticksSincePlace = 0;
        this.lastPos = (int)Scaffold.mc.field_71439_g.field_70163_u;
        this.originalHotBarSlot = Scaffold.mc.field_71439_g.field_71071_by.field_70461_c;
        this.lastYaw = Scaffold.mc.field_71439_g.field_70126_B;
        this.lastPitch = Scaffold.mc.field_71439_g.field_70126_B;
    }
    
    @Override
    public void onDisable() {
        this.angles = null;
        Scaffold.mc.field_71439_g.field_71071_by.field_70461_c = this.originalHotBarSlot;
    }
    
    @Subscribe
    public void onMove(final MoveEvent e) {
        e.setX(e.getX() * Scaffold.speed.getInput());
        e.setZ(e.getZ() * Scaffold.speed.getInput());
    }
    
    @Subscribe
    public void onRender2D(final Render2DEvent e) {
        final ScaledResolution sr = new ScaledResolution(Scaffold.mc);
        RenderUtils.drawOutlinedString(this.blockCount + " blocks", (int)(sr.func_78326_a() / 2.0f + 8.0f), (int)(sr.func_78328_b() / 2.0f - 4.0f), (this.blockCount <= 16) ? 16711680 : -1, 0);
    }
    
    @Subscribe
    public void onUpdate(final UpdateEvent e) {
        if (e.isPre()) {
            this.updateBlockCount();
            this.data = null;
            this.bestBlockStack = findBestBlockStack(36, 45);
            if (this.bestBlockStack == -1 && this.clickTimer.hasElapsed(250L)) {
                this.bestBlockStack = findBestBlockStack(9, 36);
                if (this.bestBlockStack == -1) {
                    return;
                }
                boolean override = true;
                for (int i = 44; i >= 36; --i) {
                    final ItemStack stack = InventoryUtils.getStackInSlot(i);
                    if (!InventoryUtils.isValid(stack, true)) {
                        InventoryUtils.windowClick(this.bestBlockStack, i - 36, InventoryUtils.ClickType.SWAP_WITH_HOT_BAR_SLOT);
                        this.bestBlockStack = i;
                        override = false;
                        this.clickTimer.reset();
                        break;
                    }
                }
                if (override) {
                    final int blockSlot = (int)(Scaffold.blockSlot.getInput() - 1.0);
                    InventoryUtils.windowClick(this.bestBlockStack, blockSlot, InventoryUtils.ClickType.SWAP_WITH_HOT_BAR_SLOT);
                    this.bestBlockStack = blockSlot + 36;
                    this.clickTimer.reset();
                }
            }
            if (this.bestBlockStack >= 36) {
                final BlockPos blockUnder = this.getBlockUnder();
                BlockData data = getBlockData(blockUnder);
                if (data == null) {
                    data = getBlockData(blockUnder.func_177982_a(0, -1, 0));
                }
                if (data != null && this.bestBlockStack >= 36) {
                    if (validateReplaceable(data) && data.hitVec != null) {
                        this.angles = getRotations(data);
                    }
                    else {
                        data = null;
                    }
                }
                if (this.angles != null) {
                    this.rotate(e, this.angles, 30.0f, false);
                }
                this.data = data;
            }
        }
        else if (this.data != null && this.bestBlockStack >= 36) {
            final EntityPlayerSP player = Scaffold.mc.field_71439_g;
            if (++this.ticksSincePlace < Scaffold.delay.getInput()) {
                return;
            }
            player.field_71071_by.field_70461_c = this.bestBlockStack - 36;
            if (Scaffold.mc.field_71442_b.func_178890_a(player, Scaffold.mc.field_71441_e, player.func_71045_bC(), this.data.pos, this.data.face, this.data.hitVec)) {
                ++this.placeCounter;
                this.towering = (Scaffold.tower.isToggled() && Scaffold.mc.field_71474_y.field_74314_A.func_151470_d());
                if (this.towering && this.isDistFromGround(0.0626) && this.placeCounter % 4 != 0) {
                    player.field_70181_x = 0.419545647161442;
                }
                if (Scaffold.swing.isToggled()) {
                    player.func_71038_i();
                }
                else {
                    PacketUtils.sendPacket((Packet<?>)new C0APacketAnimation());
                }
                this.ticksSincePlace = 0;
            }
        }
        this.lastYaw = e.getYaw();
        this.lastPitch = e.getPitch();
    }
    
    private static int findBestBlockStack(final int start, final int end) {
        int bestSlot = -1;
        int blockCount = -1;
        for (int i = end - 1; i >= start; --i) {
            final ItemStack stack = InventoryUtils.getStackInSlot(i);
            if (stack != null && stack.func_77973_b() instanceof ItemBlock && InventoryUtils.isGoodBlockStack(stack) && stack.field_77994_a > blockCount) {
                bestSlot = i;
                blockCount = stack.field_77994_a;
            }
        }
        return bestSlot;
    }
    
    private BlockPos getBlockUnder() {
        final EntityPlayerSP player = Scaffold.mc.field_71439_g;
        final boolean useLastPos = Scaffold.keepY.isToggled() && !this.towering;
        final double playerPos = player.field_70163_u - 1.0;
        if (!useLastPos) {
            this.lastPos = (int)player.field_70163_u;
        }
        return new BlockPos(player.field_70165_t, useLastPos ? Math.min(this.lastPos, playerPos) : playerPos, player.field_70161_v);
    }
    
    private static float[] getRotations(final BlockData data) {
        final EntityPlayerSP player = Scaffold.mc.field_71439_g;
        final Vec3 hitVec = data.hitVec;
        final double xDif = hitVec.field_72450_a - player.field_70165_t;
        final double zDif = hitVec.field_72449_c - player.field_70161_v;
        final double yDif = hitVec.field_72448_b - (player.field_70163_u + player.func_70047_e());
        final double xzDist = StrictMath.sqrt(xDif * xDif + zDif * zDif);
        return new float[] { (float)(StrictMath.atan2(zDif, xDif) * 57.29577951308232) - 90.0f, (float)(-(StrictMath.atan2(yDif, xzDist) * 57.29577951308232)) };
    }
    
    private static boolean validateBlockRange(final BlockData data) {
        final Vec3 pos = data.hitVec;
        if (pos == null) {
            return false;
        }
        final EntityPlayerSP player = Scaffold.mc.field_71439_g;
        final double x = pos.field_72450_a - player.field_70165_t;
        final double y = pos.field_72448_b - (player.field_70163_u + player.func_70047_e());
        final double z = pos.field_72449_c - player.field_70161_v;
        return StrictMath.sqrt(x * x + y * y + z * z) <= 5.0;
    }
    
    private static boolean validateReplaceable(final BlockData data) {
        final BlockPos pos = data.pos.func_177972_a(data.face);
        final World world = (World)Scaffold.mc.field_71441_e;
        return world.func_180495_p(pos).func_177230_c().func_176200_f(world, pos);
    }
    
    private static BlockData getBlockData(final BlockPos pos) {
        final BlockPos[] blockPositions = Scaffold.BLOCK_POSITIONS;
        final EnumFacing[] facings = Scaffold.FACINGS;
        final WorldClient world = Scaffold.mc.field_71441_e;
        for (int i = 0; i < blockPositions.length; ++i) {
            final BlockPos blockPos = pos.func_177971_a((Vec3i)blockPositions[i]);
            if (InventoryUtils.isValidBlock(world.func_180495_p(blockPos).func_177230_c(), false)) {
                final BlockData data = new BlockData(blockPos, facings[i]);
                if (validateBlockRange(data)) {
                    return data;
                }
            }
        }
        final BlockPos posBelow = pos.func_177982_a(0, -1, 0);
        if (InventoryUtils.isValidBlock(world.func_180495_p(posBelow).func_177230_c(), false)) {
            final BlockData data2 = new BlockData(posBelow, EnumFacing.UP);
            if (validateBlockRange(data2)) {
                return data2;
            }
        }
        for (final BlockPos blockPosition : blockPositions) {
            final BlockPos blockPos2 = pos.func_177971_a((Vec3i)blockPosition);
            for (int j = 0; j < blockPositions.length; ++j) {
                final BlockPos blockPos3 = blockPos2.func_177971_a((Vec3i)blockPositions[j]);
                if (InventoryUtils.isValidBlock(world.func_180495_p(blockPos3).func_177230_c(), false)) {
                    final BlockData data3 = new BlockData(blockPos3, facings[j]);
                    if (validateBlockRange(data3)) {
                        return data3;
                    }
                }
            }
        }
        for (final BlockPos blockPosition : blockPositions) {
            final BlockPos blockPos2 = pos.func_177971_a((Vec3i)blockPosition);
            for (final BlockPos position : blockPositions) {
                final BlockPos blockPos4 = blockPos2.func_177971_a((Vec3i)position);
                for (int k = 0; k < blockPositions.length; ++k) {
                    final BlockPos blockPos5 = blockPos4.func_177971_a((Vec3i)blockPositions[k]);
                    if (InventoryUtils.isValidBlock(world.func_180495_p(blockPos5).func_177230_c(), false)) {
                        final BlockData data4 = new BlockData(blockPos5, facings[k]);
                        if (validateBlockRange(data4)) {
                            return data4;
                        }
                    }
                }
            }
        }
        return null;
    }
    
    public boolean isRotating() {
        return this.angles != null;
    }
    
    private void updateBlockCount() {
        this.blockCount = 0;
        for (int i = 9; i < 45; ++i) {
            final ItemStack stack = InventoryUtils.getStackInSlot(i);
            if (stack != null && stack.func_77973_b() instanceof ItemBlock && InventoryUtils.isGoodBlockStack(stack)) {
                this.blockCount += stack.field_77994_a;
            }
        }
    }
    
    private boolean isDistFromGround(final double dist) {
        return Scaffold.mc.field_71441_e.func_72829_c(Scaffold.mc.field_71439_g.func_174813_aQ().func_72321_a(0.0, -dist, 0.0));
    }
    
    public static boolean safewalk() {
        return Scaffold.instance.isEnabled() && Scaffold.safewalk.isToggled();
    }
    
    private void rotate(final UpdateEvent event, final float[] rotations, final float aimSpeed, final boolean lockview) {
        final float[] prevRotations = { this.lastYaw, this.lastPitch };
        final float[] cappedRotations = { this.maxAngleChange(prevRotations[0], rotations[0], aimSpeed), this.maxAngleChange(prevRotations[1], rotations[1], aimSpeed) };
        final float[] appliedRotations = this.applyGCD(cappedRotations, prevRotations);
        event.setYaw(appliedRotations[0]);
        event.setPitch(appliedRotations[1]);
        if (lockview) {
            Scaffold.mc.field_71439_g.field_70177_z = appliedRotations[0];
            Scaffold.mc.field_71439_g.field_70125_A = appliedRotations[1];
        }
        Scaffold.mc.field_71439_g.func_70034_d(appliedRotations[0]);
        Scaffold.mc.field_71439_g.field_70761_aq = appliedRotations[0];
    }
    
    private float maxAngleChange(final float prev, final float now, final float maxTurn) {
        float dif = MathHelper.func_76142_g(now - prev);
        if (dif > maxTurn) {
            dif = maxTurn;
        }
        if (dif < -maxTurn) {
            dif = -maxTurn;
        }
        return prev + dif;
    }
    
    private double getMouseGCD() {
        final float sens = Scaffold.mc.field_71474_y.field_74341_c * 0.6f + 0.2f;
        final float pow = sens * sens * sens * 8.0f;
        return pow * 0.15;
    }
    
    private float[] applyGCD(final float[] rotations, final float[] prevRots) {
        final float yawDif = rotations[0] - prevRots[0];
        final float pitchDif = rotations[1] - prevRots[1];
        final double gcd = this.getMouseGCD();
        final int n = 0;
        rotations[n] -= (float)(yawDif % gcd);
        final int n2 = 1;
        rotations[n2] -= (float)(pitchDif % gcd);
        return rotations;
    }
    
    static {
        BLOCK_POSITIONS = new BlockPos[] { new BlockPos(-1, 0, 0), new BlockPos(1, 0, 0), new BlockPos(0, 0, -1), new BlockPos(0, 0, 1) };
        FACINGS = new EnumFacing[] { EnumFacing.EAST, EnumFacing.WEST, EnumFacing.SOUTH, EnumFacing.NORTH };
    }
    
    private static class BlockData
    {
        private final BlockPos pos;
        private final Vec3 hitVec;
        private final EnumFacing face;
        
        public BlockData(final BlockPos pos, final EnumFacing face) {
            this.pos = pos;
            this.face = face;
            this.hitVec = this.getHitVec();
        }
        
        private Vec3 getHitVec() {
            final Vec3i directionVec = this.face.func_176730_m();
            double x = directionVec.func_177958_n() * 0.5;
            double z = directionVec.func_177952_p() * 0.5;
            if (this.face.func_176743_c() == EnumFacing.AxisDirection.NEGATIVE) {
                x = -x;
                z = -z;
            }
            final Vec3 hitVec = new Vec3((Vec3i)this.pos).func_72441_c(x + z, directionVec.func_177956_o() * 0.5, x + z);
            final Vec3 src = Scaffold.mc.field_71439_g.func_174824_e(1.0f);
            final MovingObjectPosition obj = Scaffold.mc.field_71441_e.func_147447_a(src, hitVec, false, false, true);
            if (obj == null || obj.field_72307_f == null || obj.field_72313_a != MovingObjectPosition.MovingObjectType.BLOCK) {
                return null;
            }
            switch (this.face.func_176740_k()) {
                case Z: {
                    obj.field_72307_f = new Vec3(obj.field_72307_f.field_72450_a, obj.field_72307_f.field_72448_b, (double)Math.round(obj.field_72307_f.field_72449_c));
                    break;
                }
                case X: {
                    obj.field_72307_f = new Vec3((double)Math.round(obj.field_72307_f.field_72450_a), obj.field_72307_f.field_72448_b, obj.field_72307_f.field_72449_c);
                    break;
                }
            }
            if (this.face != EnumFacing.DOWN && this.face != EnumFacing.UP) {
                final IBlockState blockState = Scaffold.mc.field_71441_e.func_180495_p(obj.func_178782_a());
                final Block blockAtPos = blockState.func_177230_c();
                double blockFaceOffset;
                if (blockAtPos instanceof BlockSlab && !((BlockSlab)blockAtPos).func_176552_j()) {
                    final BlockSlab.EnumBlockHalf half = (BlockSlab.EnumBlockHalf)blockState.func_177229_b((IProperty)BlockSlab.field_176554_a);
                    blockFaceOffset = RandomUtils.nextDouble(0.1, 0.4);
                    if (half == BlockSlab.EnumBlockHalf.TOP) {
                        blockFaceOffset += 0.5;
                    }
                }
                else {
                    blockFaceOffset = RandomUtils.nextDouble(0.1, 0.9);
                }
                obj.field_72307_f = obj.field_72307_f.func_72441_c(0.0, -blockFaceOffset, 0.0);
            }
            return obj.field_72307_f;
        }
    }
}
