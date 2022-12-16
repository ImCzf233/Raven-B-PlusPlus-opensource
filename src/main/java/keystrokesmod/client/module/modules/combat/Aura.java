package keystrokesmod.client.module.modules.combat;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import net.minecraft.entity.*;
import keystrokesmod.client.module.setting.*;
import com.google.common.eventbus.*;
import keystrokesmod.client.event.impl.*;
import java.util.*;
import net.minecraft.entity.player.*;
import org.apache.commons.lang3.*;
import keystrokesmod.client.utils.*;
import net.minecraft.network.*;
import keystrokesmod.client.main.*;
import keystrokesmod.client.module.modules.movement.*;
import net.minecraft.item.*;
import net.minecraft.client.entity.*;
import keystrokesmod.client.module.modules.world.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.world.*;
import net.minecraft.network.play.client.*;
import net.minecraft.util.*;
import net.minecraft.client.*;

public class Aura extends Module
{
    public static DescriptionSetting atDesc;
    public static DoubleSliderSetting aps;
    public static ComboSetting<SortingMethod> sortingMode;
    public static ComboSetting<AutoBlockMode> abMode;
    public static ComboSetting<AttackTiming> attackTimingSetting;
    public static SliderSetting fov;
    public static SliderSetting attackReach;
    public static SliderSetting targetRange;
    public static SliderSetting turnSpeed;
    public static TickSetting fovCheck;
    public static TickSetting raytrace;
    public static TickSetting lockView;
    public static TickSetting rotCheck;
    public static TickSetting targetInvisibles;
    public static TickSetting targetPlayers;
    public static TickSetting targetTeammates;
    public static TickSetting targetMobs;
    public static TickSetting targetAnimals;
    int waitTicks;
    MillisTimer attackTimer;
    private EntityLivingBase target;
    private boolean entityInBlockRange;
    private boolean blocking;
    private float lastYaw;
    private float lastPitch;
    private static Aura instance;
    private final Map<Entity, EntityData> entityDataCache;
    private final DataSupplier dataSupplier;
    
    public Aura() {
        super("KillAura", ModuleCategory.combat);
        this.attackTimer = new MillisTimer();
        this.entityDataCache = new HashMap<Entity, EntityData>();
        final EntityData data;
        this.dataSupplier = (entity -> {
            data = new EntityData(Utils.Player.getRotationsToEntity(entity), getDistToEntity(entity.field_70165_t, entity.field_70163_u, entity.field_70161_v));
            this.entityDataCache.put(entity, data);
            return data;
        });
        this.registerSettings(Aura.aps = new DoubleSliderSetting("APS", 6.0, 8.0, 1.0, 20.0, 0.1), Aura.attackReach = new SliderSetting("Reach", 3.0, 3.0, 8.0, 0.1), Aura.targetRange = new SliderSetting("Target Range", 3.3, 3.0, 8.0, 0.1), Aura.fovCheck = new TickSetting("FOV Check", false), Aura.fov = new SliderSetting("FOV", 90.0, 0.0, 180.0, 1.0), Aura.abMode = new ComboSetting<AutoBlockMode>("AutoBlock", AutoBlockMode.None), Aura.atDesc = new DescriptionSetting("Pre is recommended."), Aura.attackTimingSetting = new ComboSetting<AttackTiming>("Attack Timing", AttackTiming.Pre), Aura.raytrace = new TickSetting("Raytrace Check", false), Aura.rotCheck = new TickSetting("Rotation Check", true), Aura.turnSpeed = new SliderSetting("Rotation Speed", 45.0, 10.0, 180.0, 5.0), Aura.lockView = new TickSetting("Lock View", false), new DescriptionSetting("   "), new DescriptionSetting("Targets:"), Aura.targetInvisibles = new TickSetting("Invisibles", false), Aura.targetTeammates = new TickSetting("Teammates", false), Aura.targetPlayers = new TickSetting("Players", true), Aura.targetMobs = new TickSetting("Mobs", false), Aura.targetAnimals = new TickSetting("Animals", false), Aura.sortingMode = new ComboSetting<SortingMethod>("Sorting", SortingMethod.Combined));
        Aura.instance = this;
    }

    @Subscribe
    public void onPacket(final PacketEvent e) {
        if (e.getPacket() instanceof C0APacketAnimation) {
            this.attackTimer.reset();
        }
    }

    @Subscribe
    public void onUpdate(final UpdateEvent e) {
        if (e.isPre()) {
            this.entityDataCache.clear();
            final boolean lastEIBR = this.entityInBlockRange;
            this.entityInBlockRange = false;
            EntityLivingBase optimalTarget = null;
            final List<EntityLivingBase> entities = Utils.Player.getLivingEntities(this::isValid);
            if (entities.size() > 1) {
                entities.sort(Aura.sortingMode.getMode().getSorter());
            }
            for (final EntityLivingBase entity : entities) {
                final double dist = this.computeData((Entity)entity).dist;
                if (!this.entityInBlockRange && dist < Aura.targetRange.getInput()) {
                    this.entityInBlockRange = true;
                }
                if (dist < Aura.attackReach.getInput()) {
                    optimalTarget = entity;
                    break;
                }
            }
            this.target = optimalTarget;
            if (Aura.abMode.getMode() == AutoBlockMode.Vanilla) {
                if (lastEIBR && !this.entityInBlockRange) {
                    this.unblock();
                    this.blocking = false;
                }
                else if (!lastEIBR && this.entityInBlockRange) {
                    this.block();
                    this.blocking = true;
                }
            }
            if (this.isOccupied() || this.checkWaitTicks()) {
                return;
            }
            if (optimalTarget != null) {
                this.rotate(e, this.computeData((Entity)optimalTarget).rotations, (float)Aura.turnSpeed.getInput(), Aura.lockView.isToggled());
                if (Aura.attackTimingSetting.getMode() == AttackTiming.Pre) {
                    this.tryAttack(e);
                }
            }
            this.lastPitch = e.getPitch();
            this.lastYaw = e.getYaw();
        }
        else {
            if (this.isOccupied()) {
                return;
            }
            if (this.target != null && Aura.attackTimingSetting.getMode() == AttackTiming.Post) {
                this.tryAttack(e);
            }
        }
    }

    private static double getDistToEntity(final double x, final double y, final double z) {
        final double xDif = x - Aura.mc.field_71439_g.field_70165_t;
        final double yDif = y - Aura.mc.field_71439_g.field_70163_u;
        final double zDif = z - Aura.mc.field_71439_g.field_70161_v;
        return Math.sqrt(xDif * xDif + zDif * zDif + yDif * yDif);
    }

    public static boolean isAutoBlocking() {
        final Aura aura = getInstance();
        return aura.isEnabled() && aura.entityInBlockRange && Aura.abMode.getMode() != AutoBlockMode.None;
    }

    public static boolean isBlocking() {
        final Aura aura = getInstance();
        return aura.isEnabled() && aura.blocking;
    }

    public static boolean doSlowdown() {
        final Aura aura = getInstance();
        return aura.isEnabled() && aura.blocking && Aura.abMode.getMode().slow;
    }

    public static Aura getInstance() {
        return Aura.instance;
    }

    public static double getEffectiveHealth(final EntityLivingBase entity) {
        if (entity instanceof EntityPlayer) {
            final EntityPlayer player = (EntityPlayer)entity;
            return player.func_110143_aJ() * (Utils.Player.getTotalArmorProtection(player) / 20.0);
        }
        return 0.0;
    }

    private void tryAttack(final UpdateEvent e) {
        if (this.isUsingItem()) {
            return;
        }
        final EntityPlayer localPlayer = (EntityPlayer)Aura.mc.field_71439_g;
        double min = Aura.aps.getInputMin();
        final double max = Aura.aps.getInputMax();
        if (min > max) {
            min = max;
        }
        double cps;
        if (min == max) {
            cps = min;
        }
        else {
            cps = RandomUtils.nextDouble(min, max);
        }
        if (this.attackTimer.hasElapsed((long)(1000.0 / cps)) && this.isLookingAtEntity(e.getYaw(), e.getPitch())) {
            localPlayer.func_71038_i();
            PacketUtils.sendPacket((Packet<?>)new C02PacketUseEntity((Entity)this.target, C02PacketUseEntity.Action.ATTACK));
            final Module keepSprint = Raven.moduleManager.getModuleByClazz(KeepSprint.class);
            if (keepSprint != null && keepSprint.isEnabled()) {
                KeepSprint.slowdown((Entity)this.target);
            }
            else {
                final EntityPlayerSP field_71439_g = Aura.mc.field_71439_g;
                field_71439_g.field_70159_w *= 0.6;
                final EntityPlayerSP field_71439_g2 = Aura.mc.field_71439_g;
                field_71439_g2.field_70179_y *= 0.6;
                Aura.mc.field_71439_g.func_70031_b(false);
            }
        }
    }

    private boolean isLookingAtEntity(final float yaw, final float pitch) {
        final double range = Aura.attackReach.getInput();
        final Vec3 src = Aura.mc.field_71439_g.func_174824_e(1.0f);
        final Vec3 rotationVec = this.getVectorForRotation(pitch, yaw);
        final Vec3 dest = src.func_72441_c(rotationVec.field_72450_a * range, rotationVec.field_72448_b * range, rotationVec.field_72449_c * range);
        final MovingObjectPosition obj = Aura.mc.field_71441_e.func_147447_a(src, dest, false, false, true);
        if (obj == null) {
            return false;
        }
        if (obj.field_72313_a == MovingObjectPosition.MovingObjectType.BLOCK) {
            if (Aura.raytrace.isToggled()) {
                return false;
            }
            if (this.computeData((Entity)this.target).dist > Aura.attackReach.getInput()) {
                return false;
            }
        }
        return !Aura.rotCheck.isToggled() || this.target.func_174813_aQ().func_72314_b(0.10000000149011612, 0.10000000149011612, 0.10000000149011612).func_72327_a(src, dest) != null;
    }

    private EntityData computeData(final Entity entity) {
        final EntityData data = this.entityDataCache.getOrDefault(entity, null);
        EntityData requiredData;
        if (data == null) {
            requiredData = this.dataSupplier.calculate(entity);
        }
        else {
            requiredData = data;
        }
        return requiredData;
    }

    private boolean fovCheck(final EntityLivingBase entity, final int fov) {
        final float[] rotations = this.computeData((Entity)entity).rotations;
        final EntityPlayer player = (EntityPlayer)Aura.mc.field_71439_g;
        final float yawChange = MathHelper.func_76142_g(player.field_70177_z - rotations[0]);
        final float pitchChange = MathHelper.func_76142_g(player.field_70125_A - rotations[1]);
        return Math.sqrt(yawChange * yawChange + pitchChange * pitchChange) < fov;
    }

    @Override
    public void onEnable() {
        this.entityDataCache.clear();
    }

    @Override
    public void onDisable() {
        this.target = null;
        this.entityInBlockRange = false;
        if (this.blocking) {
            this.unblock();
            this.blocking = false;
        }
    }

    private boolean isInMenu() {
        return Aura.mc.field_71462_r != null;
    }

    private boolean isOccupied() {
        return this.isInMenu();
    }

    public EntityLivingBase getTarget() {
        return this.target;
    }

    private boolean checkWaitTicks() {
        if (this.waitTicks > 0) {
            --this.waitTicks;
            return true;
        }
        return false;
    }

    private boolean isUsingItem() {
        return Aura.mc.field_71439_g.func_71039_bw() && !this.isHoldingSword();
    }

    private boolean isHoldingSword() {
        final ItemStack stack;
        return (stack = Aura.mc.field_71439_g.func_71045_bC()) != null && stack.func_77973_b() instanceof ItemSword;
    }

    private boolean isValid(final EntityLivingBase entity) {
        if (!entity.func_70089_S()) {
            return false;
        }
        if (entity.func_82150_aj() && !Aura.targetInvisibles.isToggled()) {
            return false;
        }
        if (entity == Aura.mc.field_71439_g.field_70154_o) {
            return false;
        }
        if (entity instanceof EntityOtherPlayerMP) {
            final EntityPlayer player = (EntityPlayer)entity;
            if (!Aura.targetPlayers.isToggled()) {
                return false;
            }
            if (AntiBot.bot((Entity)entity)) {
                return false;
            }
            if (!Aura.targetTeammates.isToggled() && this.isTeamMate(player)) {
                return false;
            }
            if (AimAssist.isAFriend((Entity)player)) {
                return false;
            }
        }
        else if (entity instanceof EntityMob) {
            if (!Aura.targetMobs.isToggled()) {
                return false;
            }
        }
        else {
            if (!(entity instanceof EntityAnimal)) {
                return false;
            }
            if (!Aura.targetAnimals.isToggled()) {
                return false;
            }
        }
        return this.computeData((Entity)entity).dist < Math.max(Aura.targetRange.getInput(), Aura.attackReach.getInput()) && (!Aura.fovCheck.isToggled() || this.fovCheck(entity, (int)Aura.fov.getInput()));
    }

    private void block() {
        if (!Aura.mc.field_71439_g.func_70632_aY()) {
            Aura.mc.field_71442_b.func_78769_a((EntityPlayer)Aura.mc.field_71439_g, (World)Aura.mc.field_71441_e, Aura.mc.field_71439_g.func_70694_bm());
            Aura.mc.field_71439_g.func_71008_a(Aura.mc.field_71439_g.func_70694_bm(), Aura.mc.field_71439_g.func_70694_bm().func_77988_m());
            this.blocking = true;
        }
    }

    private void unblock() {
        if (Aura.mc.field_71439_g.func_70632_aY()) {
            PacketUtils.sendPacket((Packet<?>)new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.field_177992_a, EnumFacing.UP));
            this.blocking = false;
        }
    }

    protected final Vec3 getVectorForRotation(final float p_getVectorForRotation_1_, final float p_getVectorForRotation_2_) {
        final float f = MathHelper.func_76134_b(-p_getVectorForRotation_2_ * 0.017453292f - 3.1415927f);
        final float f2 = MathHelper.func_76126_a(-p_getVectorForRotation_2_ * 0.017453292f - 3.1415927f);
        final float f3 = -MathHelper.func_76134_b(-p_getVectorForRotation_1_ * 0.017453292f);
        final float f4 = MathHelper.func_76126_a(-p_getVectorForRotation_1_ * 0.017453292f);
        return new Vec3((double)(f2 * f3), (double)f4, (double)(f * f3));
    }

    private boolean isTeamMate(final EntityPlayer entity) {
        final String entName = entity.func_145748_c_().func_150254_d();
        final String playerName = Aura.mc.field_71439_g.func_145748_c_().func_150254_d();
        return entName.length() >= 2 && playerName.length() >= 2 && entName.startsWith("��") && playerName.startsWith("��") && entName.charAt(1) == playerName.charAt(1);
    }

    private void rotate(final UpdateEvent event, final float[] rotations, final float aimSpeed, final boolean lockview) {
        final float[] prevRotations = { this.lastYaw, this.lastPitch };
        final float[] cappedRotations = { this.maxAngleChange(prevRotations[0], rotations[0], aimSpeed), this.maxAngleChange(prevRotations[1], rotations[1], aimSpeed) };
        final float[] appliedRotations = this.applyGCD(cappedRotations, prevRotations);
        event.setYaw(appliedRotations[0]);
        event.setPitch(appliedRotations[1]);
        if (lockview) {
            Aura.mc.field_71439_g.field_70177_z = appliedRotations[0];
            Aura.mc.field_71439_g.field_70125_A = appliedRotations[1];
        }
    }

    private double getMouseGCD() {
        final float sens = Aura.mc.field_71474_y.field_74341_c * 0.6f + 0.2f;
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

    public enum AutoBlockMode
    {
        None(false),
        Fake(false),
        Vanilla(true);

        public final boolean slow;

        private AutoBlockMode(final boolean slow) {
            this.slow = slow;
        }
    }

    public enum AttackTiming
    {
        Pre,
        Post;
    }

    public enum SortingMethod
    {
        Distance((Comparator<EntityLivingBase>)new DistanceSorting()),
        Health((Comparator<EntityLivingBase>)new HealthSorting()),
        HurtTime((Comparator<EntityLivingBase>)new HurtTimeSorting()),
        FOV((Comparator<EntityLivingBase>)new CrosshairSorting()),
        Combined((Comparator<EntityLivingBase>)new CombinedSorting());

        private final Comparator<EntityLivingBase> sorter;

        private SortingMethod(final Comparator<EntityLivingBase> sorter) {
            this.sorter = sorter;
        }

        public Comparator<EntityLivingBase> getSorter() {
            return this.sorter;
        }
    }

    private static class EntityData
    {
        private final float[] rotations;
        private final double dist;

        public EntityData(final float[] rotations, final double dist) {
            this.rotations = rotations;
            this.dist = dist;
        }
    }

    private abstract static class AngleBasedSorting implements Comparator<EntityLivingBase>
    {
        protected abstract float getCurrentAngle();

        @Override
        public int compare(final EntityLivingBase o1, final EntityLivingBase o2) {
            final float yaw = this.getCurrentAngle();
            return Double.compare(Math.abs(Utils.Player.getYawToEntity((Entity)o1) - yaw), Math.abs(Utils.Player.getYawToEntity((Entity)o2) - yaw));
        }
    }

    private static class CrosshairSorting extends AngleBasedSorting
    {
        @Override
        protected float getCurrentAngle() {
            return Aura.mc.field_71439_g.field_70177_z;
        }
    }

    private static class CombinedSorting implements Comparator<EntityLivingBase>
    {
        @Override
        public int compare(final EntityLivingBase o1, final EntityLivingBase o2) {
            int t1 = 0;
            for (final SortingMethod sortingMethod : SortingMethod.values()) {
                final Comparator<EntityLivingBase> sorter = sortingMethod.getSorter();
                if (sorter != this) {
                    t1 += sorter.compare(o1, o2);
                }
            }
            return t1;
        }
    }

    private static class DistanceSorting implements Comparator<EntityLivingBase>
    {
        @Override
        public int compare(final EntityLivingBase o1, final EntityLivingBase o2) {
            return Double.compare(o1.func_70068_e((Entity)Aura.mc.field_71439_g), o1.func_70068_e((Entity)Aura.mc.field_71439_g));
        }
    }
    
    private static class HealthSorting implements Comparator<EntityLivingBase>
    {
        @Override
        public int compare(final EntityLivingBase o1, final EntityLivingBase o2) {
            return Double.compare(Aura.getEffectiveHealth(o1), Aura.getEffectiveHealth(o2));
        }
    }
    
    private static class HurtTimeSorting implements Comparator<EntityLivingBase>
    {
        @Override
        public int compare(final EntityLivingBase o1, final EntityLivingBase o2) {
            return Integer.compare(20 - o2.field_70172_ad, 20 - o1.field_70172_ad);
        }
    }
    
    @FunctionalInterface
    private interface DataSupplier
    {
        EntityData calculate(final Entity p0);
    }
}
