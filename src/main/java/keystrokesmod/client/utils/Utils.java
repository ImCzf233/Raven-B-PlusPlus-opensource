package keystrokesmod.client.utils;

import net.minecraft.client.*;
import net.minecraft.inventory.*;
import net.minecraft.client.gui.inventory.*;
import net.minecraft.entity.player.*;
import net.minecraft.enchantment.*;

import java.util.List;
import java.util.function.*;
import net.minecraft.entity.*;
import net.minecraft.client.entity.*;
import com.google.common.base.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import net.minecraft.entity.ai.attributes.*;
import org.lwjgl.input.*;
import net.minecraft.potion.*;
import keystrokesmod.client.module.modules.world.*;
import net.minecraft.item.*;
import net.minecraft.client.network.*;
import net.minecraftforge.client.event.*;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.common.*;
import net.minecraftforge.fml.common.eventhandler.*;
import java.nio.*;
import keystrokesmod.client.module.setting.impl.*;
import keystrokesmod.client.main.*;
import keystrokesmod.client.module.modules.combat.*;
import keystrokesmod.client.module.*;
import java.awt.*;
import java.awt.datatransfer.*;
import com.google.common.collect.*;
import net.minecraft.scoreboard.*;
import org.lwjgl.*;
import net.minecraft.util.*;
import java.time.format.*;
import java.time.*;
import java.time.temporal.*;
import java.util.*;
import com.google.gson.*;
import java.net.*;
import java.nio.charset.*;
import java.io.*;
import java.util.function.Predicate;

import org.lwjgl.opengl.*;
import net.minecraft.client.renderer.vertex.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.gui.*;

public class Utils
{
    private static final Random rand;
    public static final Minecraft mc;
    public static final String md = "Mode: ";
    
    static {
        rand = new Random();
        mc = Minecraft.func_71410_x();
    }
    
    public static class Player
    {
        public static boolean isPlayerInChest() {
            return Utils.mc.field_71462_r != null && Utils.mc.field_71439_g.field_71069_bz instanceof ContainerPlayer && Utils.mc.field_71462_r instanceof GuiChest;
        }
        
        public static boolean isPlayerInInventory() {
            return Utils.mc.field_71462_r != null && Utils.mc.field_71439_g.field_71069_bz instanceof ContainerPlayer && Utils.mc.field_71462_r instanceof GuiInventory;
        }
        
        public static float[] getRotationsToEntity(final Entity entity) {
            final double xDif = entity.field_70165_t - Utils.mc.field_71439_g.field_70165_t;
            final double zDif = entity.field_70161_v - Utils.mc.field_71439_g.field_70161_v;
            final AxisAlignedBB entityBB = entity.func_174813_aQ().func_72314_b(0.10000000149011612, 0.10000000149011612, 0.10000000149011612);
            final double playerEyePos = Utils.mc.field_71439_g.field_70163_u + Utils.mc.field_71439_g.func_70047_e();
            final double yDif = (playerEyePos > entityBB.field_72337_e) ? (entityBB.field_72337_e - playerEyePos) : ((playerEyePos < entityBB.field_72338_b) ? (entityBB.field_72338_b - playerEyePos) : 0.0);
            final double fDist = MathHelper.func_76133_a(xDif * xDif + zDif * zDif);
            return new float[] { (float)(StrictMath.atan2(zDif, xDif) * 57.29577951308232) - 90.0f, (float)(-(StrictMath.atan2(yDif, fDist) * 57.29577951308232)) };
        }
        
        public static double getTotalArmorProtection(final EntityPlayer player) {
            double totalArmor = 0.0;
            for (int i = 0; i < 4; ++i) {
                final ItemStack armorStack = player.func_82169_q(i);
                if (armorStack != null && armorStack.func_77973_b() instanceof ItemArmor) {
                    totalArmor += getDamageReduction(armorStack);
                }
            }
            return totalArmor;
        }
        
        public static double getDamageReduction(final ItemStack stack) {
            double reduction = 0.0;
            final ItemArmor armor = (ItemArmor)stack.func_77973_b();
            reduction += armor.field_77879_b;
            if (stack.func_77948_v()) {
                reduction += EnchantmentHelper.func_77506_a(Enchantment.field_180310_c.field_77352_x, stack) * 0.25;
            }
            return reduction;
        }
        
        public static List<EntityLivingBase> getLivingEntities(final Predicate<EntityLivingBase> validator) {
            final List<EntityLivingBase> entities = new ArrayList<EntityLivingBase>();
            for (final Entity entity : Utils.mc.field_71441_e.field_72996_f) {
                if (entity instanceof EntityLivingBase) {
                    final EntityLivingBase e = (EntityLivingBase)entity;
                    if (!validator.test(e)) {
                        continue;
                    }
                    entities.add(e);
                }
            }
            return entities;
        }
        
        public static float getYawToEntity(final Entity entity) {
            final EntityPlayerSP player = Utils.mc.field_71439_g;
            return getYawBetween(player.field_70177_z, player.field_70165_t, player.field_70161_v, entity.field_70165_t, entity.field_70161_v);
        }
        
        public static float getYawBetween(final float yaw, final double srcX, final double srcZ, final double destX, final double destZ) {
            final double xDist = destX - srcX;
            final double zDist = destZ - srcZ;
            final float var1 = (float)(StrictMath.atan2(zDist, xDist) * 180.0 / 3.141592653589793) - 90.0f;
            return yaw + MathHelper.func_76142_g(var1 - yaw);
        }
        
        public static MovingObjectPosition rayTrace(final double reach, final float partialTicks) {
            final Entity entity = Utils.mc.func_175606_aa();
            if (entity != null && Utils.mc.field_71441_e != null) {
                Entity pointedEntity = null;
                MovingObjectPosition objectMouseOver = entity.func_174822_a(reach, partialTicks);
                double distanceToVec = reach;
                final Vec3 vec3 = entity.func_174824_e(partialTicks);
                if (objectMouseOver != null) {
                    distanceToVec = objectMouseOver.field_72307_f.func_72438_d(vec3);
                }
                final Vec3 vec4 = entity.func_70676_i(partialTicks);
                final Vec3 vec5 = vec3.func_72441_c(vec4.field_72450_a * reach, vec4.field_72448_b * reach, vec4.field_72449_c * reach);
                Vec3 vec6 = null;
                final float f = 1.0f;
                final List<Entity> list = (List<Entity>)Utils.mc.field_71441_e.func_175674_a(entity, entity.func_174813_aQ().func_72321_a(vec4.field_72450_a * reach, vec4.field_72448_b * reach, vec4.field_72449_c * reach).func_72314_b((double)f, (double)f, (double)f), Predicates.and(EntitySelectors.field_180132_d, Entity::func_70067_L));
                double d2 = distanceToVec;
                for (final Entity entity2 : list) {
                    final float f2 = entity2.func_70111_Y();
                    final AxisAlignedBB axisalignedbb = entity2.func_174813_aQ().func_72314_b((double)f2, (double)f2, (double)f2);
                    final MovingObjectPosition movingobjectposition = axisalignedbb.func_72327_a(vec3, vec5);
                    if (axisalignedbb.func_72318_a(vec3)) {
                        if (d2 < 0.0) {
                            continue;
                        }
                        pointedEntity = entity2;
                        vec6 = ((movingobjectposition == null) ? vec3 : movingobjectposition.field_72307_f);
                        d2 = 0.0;
                    }
                    else {
                        if (movingobjectposition == null) {
                            continue;
                        }
                        final double d3 = vec3.func_72438_d(movingobjectposition.field_72307_f);
                        if (d3 >= d2 && d2 != 0.0) {
                            continue;
                        }
                        if (entity2 == entity.field_70154_o && !entity.canRiderInteract()) {
                            if (d2 != 0.0) {
                                continue;
                            }
                            pointedEntity = entity2;
                            vec6 = movingobjectposition.field_72307_f;
                        }
                        else {
                            pointedEntity = entity2;
                            vec6 = movingobjectposition.field_72307_f;
                            d2 = d3;
                        }
                    }
                }
                if (pointedEntity != null) {
                    vec3.func_72438_d(vec6);
                    Reach.getReach();
                }
                if (pointedEntity != null && (d2 < distanceToVec || objectMouseOver == null)) {
                    objectMouseOver = new MovingObjectPosition(pointedEntity, vec6);
                }
                return objectMouseOver;
            }
            return null;
        }
        
        public static void hotkeyToSlot(final int slot) {
            if (!isPlayerInGame()) {
                return;
            }
            Utils.mc.field_71439_g.field_71071_by.field_70461_c = slot;
        }
        
        public static void sendMessageToSelf(final String txt) {
            if (isPlayerInGame()) {
                final String m = Client.reformat("&7[&6R&7]&r " + txt);
                Utils.mc.field_71439_g.func_145747_a((IChatComponent)new ChatComponentText(m));
            }
        }
        
        public static boolean isPlayerInGame() {
            return Utils.mc.field_71439_g != null && Utils.mc.field_71441_e != null;
        }
        
        public static boolean isMoving() {
            return Utils.mc.field_71439_g.field_70701_bs != 0.0f || Utils.mc.field_71439_g.field_70702_br != 0.0f;
        }
        
        public static void aim(final Entity en, final float ps, final boolean pc) {
            if (en != null) {
                final float[] t = getTargetRotations(en);
                if (t != null) {
                    final float y = t[0];
                    final float p = t[1] + 4.0f + ps;
                    if (pc) {
                        Utils.mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C05PacketPlayerLook(y, p, Utils.mc.field_71439_g.field_70122_E));
                    }
                    else {
                        Utils.mc.field_71439_g.field_70177_z = y;
                        Utils.mc.field_71439_g.field_70125_A = p;
                    }
                }
            }
        }
        
        public static float[] silentAim(final Entity en) {
            if (en != null) {
                final float[] t = getTargetRotations(en);
                if (t != null) {
                    final float y = t[0];
                    final float p = t[1];
                    Utils.mc.field_71439_g.field_70759_as = y;
                    Utils.mc.field_71439_g.func_70034_d(y);
                }
                return t;
            }
            return null;
        }
        
        public static double fovFromEntity(final Entity en) {
            return ((Utils.mc.field_71439_g.field_70177_z - fovToEntity(en)) % 360.0 + 540.0) % 360.0 - 180.0;
        }
        
        public static float fovToEntity(final Entity ent) {
            final double x = ent.field_70165_t - Utils.mc.field_71439_g.field_70165_t;
            final double z = ent.field_70161_v - Utils.mc.field_71439_g.field_70161_v;
            final double yaw = Math.atan2(x, z) * 57.2957795;
            return (float)(yaw * -1.0);
        }
        
        public static boolean fov(final Entity entity, float fov) {
            fov *= 0.5;
            final double v = ((Utils.mc.field_71439_g.field_70177_z - fovToEntity(entity)) % 360.0 + 540.0) % 360.0 - 180.0;
            return (v > 0.0 && v < fov) || (-fov < v && v < 0.0);
        }
        
        public static double getPlayerBPS(final Entity en, final int d) {
            final double x = en.field_70165_t - en.field_70169_q;
            final double z = en.field_70161_v - en.field_70166_s;
            final double sp = Math.sqrt(x * x + z * z) * 20.0;
            return Java.round(sp, d);
        }
        
        public static boolean playerOverAir() {
            final double x = Utils.mc.field_71439_g.field_70165_t;
            final double y = Utils.mc.field_71439_g.field_70163_u - 1.0;
            final double z = Utils.mc.field_71439_g.field_70161_v;
            final BlockPos p = new BlockPos(MathHelper.func_76128_c(x), MathHelper.func_76128_c(y), MathHelper.func_76128_c(z));
            return Utils.mc.field_71441_e.func_175623_d(p);
        }
        
        public static boolean playerUnderBlock() {
            final double x = Utils.mc.field_71439_g.field_70165_t;
            final double y = Utils.mc.field_71439_g.field_70163_u + 2.0;
            final double z = Utils.mc.field_71439_g.field_70161_v;
            final BlockPos p = new BlockPos(MathHelper.func_76128_c(x), MathHelper.func_76128_c(y), MathHelper.func_76128_c(z));
            return Utils.mc.field_71441_e.func_175665_u(p) || Utils.mc.field_71441_e.func_175677_d(p, false);
        }
        
        public static int getCurrentPlayerSlot() {
            return Utils.mc.field_71439_g.field_71071_by.field_70461_c;
        }
        
        public static boolean isPlayerHoldingSword() {
            return Utils.mc.field_71439_g.func_71045_bC() != null && Utils.mc.field_71439_g.func_71045_bC().func_77973_b() instanceof ItemSword;
        }
        
        public static boolean isPlayerHoldingAxe() {
            return Utils.mc.field_71439_g.func_71045_bC() != null && Utils.mc.field_71439_g.func_71045_bC().func_77973_b() instanceof ItemAxe;
        }
        
        public static boolean isPlayerHoldingWeapon() {
            return isPlayerHoldingAxe() || isPlayerHoldingSword();
        }
        
        public static int getMaxDamageSlot() {
            int index = -1;
            double damage = -1.0;
            for (int slot = 0; slot <= 8; ++slot) {
                final ItemStack itemInSlot = Utils.mc.field_71439_g.field_71071_by.func_70301_a(slot);
                if (itemInSlot != null) {
                    for (final AttributeModifier mooommHelp : itemInSlot.func_111283_C().values()) {
                        if (mooommHelp.func_111164_d() > damage) {
                            damage = mooommHelp.func_111164_d();
                            index = slot;
                        }
                    }
                }
            }
            return index;
        }
        
        public static double getSlotDamage(final int slot) {
            final ItemStack itemInSlot = Utils.mc.field_71439_g.field_71071_by.func_70301_a(slot);
            if (itemInSlot == null) {
                return -1.0;
            }
            final Iterator<AttributeModifier> iterator = itemInSlot.func_111283_C().values().iterator();
            if (iterator.hasNext()) {
                final AttributeModifier mooommHelp = iterator.next();
                return mooommHelp.func_111164_d();
            }
            return -1.0;
        }
        
        public static ArrayList<Integer> playerWearingArmor() {
            final ArrayList<Integer> wearingArmor = new ArrayList<Integer>();
            for (int armorPiece = 0; armorPiece < 4; ++armorPiece) {
                if (Utils.mc.field_71439_g.func_82169_q(armorPiece) != null) {
                    if (armorPiece == 0) {
                        wearingArmor.add(3);
                    }
                    else if (armorPiece == 1) {
                        wearingArmor.add(2);
                    }
                    else if (armorPiece == 2) {
                        wearingArmor.add(1);
                    }
                    else if (armorPiece == 3) {
                        wearingArmor.add(0);
                    }
                }
            }
            return wearingArmor;
        }
        
        public static int getBlockAmountInCurrentStack(final int currentItem) {
            if (Utils.mc.field_71439_g.field_71071_by.func_70301_a(currentItem) == null) {
                return 0;
            }
            final ItemStack itemStack = Utils.mc.field_71439_g.field_71071_by.func_70301_a(currentItem);
            if (itemStack.func_77973_b() instanceof ItemBlock) {
                return itemStack.field_77994_a;
            }
            return 0;
        }
        
        public static boolean tryingToCombo() {
            return Mouse.isButtonDown(0) && Mouse.isButtonDown(1);
        }
        
        public static float[] getTargetRotations(final Entity q) {
            if (q == null) {
                return null;
            }
            final double diffX = q.field_70165_t - Utils.mc.field_71439_g.field_70165_t;
            double diffY;
            if (q instanceof EntityLivingBase) {
                final EntityLivingBase en = (EntityLivingBase)q;
                diffY = en.field_70163_u + en.func_70047_e() * 0.9 - (Utils.mc.field_71439_g.field_70163_u + Utils.mc.field_71439_g.func_70047_e());
            }
            else {
                diffY = (q.func_174813_aQ().field_72338_b + q.func_174813_aQ().field_72337_e) / 2.0 - (Utils.mc.field_71439_g.field_70163_u + Utils.mc.field_71439_g.func_70047_e());
            }
            final double diffZ = q.field_70161_v - Utils.mc.field_71439_g.field_70161_v;
            final double dist = MathHelper.func_76133_a(diffX * diffX + diffZ * diffZ);
            final float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
            final float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0 / 3.141592653589793));
            return new float[] { Utils.mc.field_71439_g.field_70177_z + MathHelper.func_76142_g(yaw - Utils.mc.field_71439_g.field_70177_z), Utils.mc.field_71439_g.field_70125_A + MathHelper.func_76142_g(pitch - Utils.mc.field_71439_g.field_70125_A) };
        }
        
        public static void fixMovementSpeed(final double s, final boolean m) {
            if (!m || isMoving()) {
                Utils.mc.field_71439_g.field_70159_w = -Math.sin(correctRotations()) * s;
                Utils.mc.field_71439_g.field_70179_y = Math.cos(correctRotations()) * s;
            }
        }
        
        public static void bop(final double s) {
            double forward = Utils.mc.field_71439_g.field_71158_b.field_78900_b;
            double strafe = Utils.mc.field_71439_g.field_71158_b.field_78902_a;
            float yaw = Utils.mc.field_71439_g.field_70177_z;
            if (forward == 0.0 && strafe == 0.0) {
                Utils.mc.field_71439_g.field_70159_w = 0.0;
                Utils.mc.field_71439_g.field_70179_y = 0.0;
            }
            else {
                if (forward != 0.0) {
                    if (strafe > 0.0) {
                        yaw += ((forward > 0.0) ? -45 : 45);
                    }
                    else if (strafe < 0.0) {
                        yaw += ((forward > 0.0) ? 45 : -45);
                    }
                    strafe = 0.0;
                    if (forward > 0.0) {
                        forward = 1.0;
                    }
                    else if (forward < 0.0) {
                        forward = -1.0;
                    }
                }
                final double rad = Math.toRadians(yaw + 90.0f);
                final double sin = Math.sin(rad);
                final double cos = Math.cos(rad);
                Utils.mc.field_71439_g.field_70159_w = forward * s * cos + strafe * s * sin;
                Utils.mc.field_71439_g.field_70179_y = forward * s * sin - strafe * s * cos;
            }
        }
        
        public static float getStrafeYaw(final float forward, final float strafe) {
            float yaw = Utils.mc.field_71439_g.field_70177_z;
            if (forward == 0.0f && strafe == 0.0f) {
                return yaw;
            }
            final boolean reversed = forward < 0.0f;
            final float strafingYaw = 90.0f * ((forward > 0.0f) ? 0.5f : (reversed ? -0.5f : 1.0f));
            if (reversed) {
                yaw += 180.0f;
            }
            if (strafe > 0.0f) {
                yaw -= strafingYaw;
            }
            else if (strafe < 0.0f) {
                yaw += strafingYaw;
            }
            return yaw;
        }
        
        public static float correctRotations() {
            float yw = Utils.mc.field_71439_g.field_70177_z;
            if (Utils.mc.field_71439_g.field_70701_bs < 0.0f) {
                yw += 180.0f;
            }
            float f;
            if (Utils.mc.field_71439_g.field_70701_bs < 0.0f) {
                f = -0.5f;
            }
            else if (Utils.mc.field_71439_g.field_70701_bs > 0.0f) {
                f = 0.5f;
            }
            else {
                f = 1.0f;
            }
            if (Utils.mc.field_71439_g.field_70702_br > 0.0f) {
                yw -= 90.0f * f;
            }
            if (Utils.mc.field_71439_g.field_70702_br < 0.0f) {
                yw += 90.0f * f;
            }
            yw *= 0.017453292f;
            return yw;
        }
        
        public static double pythagorasMovement() {
            return Math.sqrt(Utils.mc.field_71439_g.field_70159_w * Utils.mc.field_71439_g.field_70159_w + Utils.mc.field_71439_g.field_70179_y * Utils.mc.field_71439_g.field_70179_y);
        }
        
        public static void swing() {
            final EntityPlayerSP p = Utils.mc.field_71439_g;
            final int armSwingEnd = p.func_70644_a(Potion.field_76422_e) ? (6 - (1 + p.func_70660_b(Potion.field_76422_e).func_76458_c())) : (p.func_70644_a(Potion.field_76419_f) ? (6 + (1 + p.func_70660_b(Potion.field_76419_f).func_76458_c()) * 2) : 6);
            if (!p.field_82175_bq || p.field_110158_av >= armSwingEnd / 2 || p.field_110158_av < 0) {
                p.field_110158_av = -1;
                p.field_82175_bq = true;
            }
        }
        
        public static EntityPlayer getClosestPlayer(double dis) {
            if (Utils.mc.field_71441_e == null) {
                return null;
            }
            final Iterator entities = Utils.mc.field_71441_e.field_72996_f.iterator();
            EntityPlayer cplayer = null;
            while (entities.hasNext()) {
                final Entity en = entities.next();
                if (en instanceof EntityPlayer && en != Utils.mc.field_71439_g) {
                    final EntityPlayer pl = (EntityPlayer)en;
                    if (Utils.mc.field_71439_g.func_70032_d((Entity)pl) >= dis || AntiBot.bot((Entity)pl)) {
                        continue;
                    }
                    dis = Utils.mc.field_71439_g.func_70032_d((Entity)pl);
                    cplayer = pl;
                }
            }
            return cplayer;
        }
    }
    
    public static class Client
    {
        public static boolean isThrowableItem(final ItemStack is) {
            final Item i = is.func_77973_b();
            return i instanceof ItemEgg || i instanceof ItemEnderEye || i instanceof ItemEnderPearl || i instanceof ItemSnowball || i instanceof ItemExpBottle || (i instanceof ItemPotion && ItemPotion.func_77831_g(is.func_77960_j()));
        }
        
        public static List<NetworkPlayerInfo> getPlayers() {
            final List<NetworkPlayerInfo> yes = new ArrayList<NetworkPlayerInfo>();
            final List<NetworkPlayerInfo> mmmm = new ArrayList<NetworkPlayerInfo>();
            try {
                yes.addAll(Utils.mc.func_147114_u().func_175106_d());
            }
            catch (NullPointerException r) {
                return yes;
            }
            for (final NetworkPlayerInfo ergy43d : yes) {
                if (!mmmm.contains(ergy43d)) {
                    mmmm.add(ergy43d);
                }
            }
            return mmmm;
        }
        
        public static boolean othersExist() {
            for (final Entity wut : Utils.mc.field_71441_e.func_72910_y()) {
                if (wut instanceof EntityPlayer) {
                    return true;
                }
            }
            return false;
        }
        
        public static void setMouseButtonState(final int mouseButton, final boolean held) {
            final MouseEvent m = new MouseEvent();
            ObfuscationReflectionHelper.setPrivateValue((Class)MouseEvent.class, (Object)m, (Object)mouseButton, new String[] { "button" });
            ObfuscationReflectionHelper.setPrivateValue((Class)MouseEvent.class, (Object)m, (Object)held, new String[] { "buttonstate" });
            MinecraftForge.EVENT_BUS.post((Event)m);
            final ByteBuffer buttons = (ByteBuffer)ObfuscationReflectionHelper.getPrivateValue((Class)Mouse.class, (Object)null, new String[] { "buttons" });
            buttons.put(mouseButton, (byte)(held ? 1 : 0));
            ObfuscationReflectionHelper.setPrivateValue((Class)Mouse.class, (Object)null, (Object)buttons, new String[] { "buttons" });
        }
        
        public static void correctSliders(final SliderSetting c, final SliderSetting d) {
            if (c.getInput() > d.getInput()) {
                final double p = c.getInput();
                c.setValue(d.getInput());
                d.setValue(p);
            }
        }
        
        public static double ranModuleVal(final SliderSetting a, final SliderSetting b, final Random r) {
            return (a.getInput() == b.getInput()) ? a.getInput() : (a.getInput() + r.nextDouble() * (b.getInput() - a.getInput()));
        }
        
        public static double ranModuleVal(final DoubleSliderSetting a, final Random r) {
            return (a.getInputMin() == a.getInputMax()) ? a.getInputMin() : (a.getInputMin() + r.nextDouble() * (a.getInputMax() - a.getInputMin()));
        }
        
        public static boolean isHyp() {
            if (!Player.isPlayerInGame()) {
                return false;
            }
            try {
                return !Utils.mc.func_71356_B() && (Utils.mc.func_147104_D().field_78845_b.toLowerCase().contains("hypixel.net") || Utils.mc.func_147104_D().field_78845_b.toLowerCase().contains("localhost"));
            }
            catch (Exception welpBruh) {
                welpBruh.printStackTrace();
                return false;
            }
        }
        
        public static Timer getTimer() {
            return (Timer)ObfuscationReflectionHelper.getPrivateValue((Class)Minecraft.class, (Object)Minecraft.func_71410_x(), new String[] { "timer", "field_71428_T" });
        }
        
        public static void resetTimer() {
            try {
                getTimer().field_74278_d = 1.0f;
            }
            catch (NullPointerException ex) {}
        }
        
        public static boolean autoClickerClicking() {
            final Module autoClicker = Raven.moduleManager.getModuleByClazz(LeftClicker.class);
            return autoClicker != null && autoClicker.isEnabled() && autoClicker.isEnabled() && Mouse.isButtonDown(0);
        }
        
        public static int rainbowDraw(final long speed, final long... delay) {
            final long time = System.currentTimeMillis() + ((delay.length > 0) ? delay[0] : 0L);
            return Color.getHSBColor(time % (15000L / speed) / (15000.0f / speed), 1.0f, 1.0f).getRGB();
        }
        
        public static int customDraw(final int delay) {
            final int r = getColorBetween(150, 250, delay);
            final int g = getColorBetween(0, 165, delay);
            final int b = getColorBetween(0, 1, delay);
            return new Color(r, g, b).getRGB();
        }
        
        public static int getColorBetween(final int min, final int max, final int delay) {
            final int c = (int)Math.abs((System.currentTimeMillis() / 10L + delay) % (2 * (max - min)) - (max - min)) + min;
            return c;
        }
        
        public static int astolfoColorsDraw(final int yOffset, final int yTotal, final float speed) {
            float hue;
            for (hue = System.currentTimeMillis() % (int)speed + (float)((yTotal - yOffset) * 9); hue > speed; hue -= speed) {}
            hue /= speed;
            if (hue > 0.5) {
                hue = 0.5f - (hue - 0.5f);
            }
            hue += 0.5f;
            return Color.HSBtoRGB(hue, 0.5f, 1.0f);
        }
        
        public static int astolfoColorsDraw(final int yOffset, final int yTotal) {
            return astolfoColorsDraw(yOffset, yTotal, 2900.0f);
        }
        
        public static int kopamedColoursDraw(final int yOffset, final int yTotal) {
            final float speed = 6428.0f;
            float hue;
            try {
                hue = System.currentTimeMillis() % (int)speed + (float)((yTotal - yOffset) / (yOffset / yTotal));
            }
            catch (ArithmeticException divisionByZero) {
                hue = System.currentTimeMillis() % (int)speed + (float)((yTotal - yOffset) / (yOffset / yTotal + 1 + 1));
            }
            while (hue > speed) {
                hue -= speed;
            }
            hue /= speed;
            if (hue > 2.0f) {
                hue = 2.0f - (hue - 2.0f);
            }
            hue += 2.0f;
            float current;
            for (current = System.currentTimeMillis() % speed + (yOffset + yTotal) * 9; current > speed; current -= speed) {}
            current /= speed;
            if (current > 2.0f) {
                current = 2.0f - (current - 2.0f);
            }
            current += 2.0f;
            return Color.HSBtoRGB(current / (current - yTotal) + current, 1.0f, 1.0f);
        }
        
        public static boolean openWebpage(final String url) {
            try {
                URL linkURL = null;
                linkURL = new URL(url);
                return openWebpage(linkURL.toURI());
            }
            catch (URISyntaxException | MalformedURLException ex2) {
                final Exception ex;
                final Exception e = ex;
                e.printStackTrace();
                return false;
            }
        }
        
        public static boolean openWebpage(final URL url) {
            try {
                return openWebpage(url.toURI());
            }
            catch (URISyntaxException e) {
                e.printStackTrace();
                return false;
            }
        }
        
        public static boolean openWebpage(final URI uri) {
            final Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
            if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                try {
                    desktop.browse(uri);
                    return true;
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return false;
        }
        
        public static boolean copyToClipboard(final String content) {
            try {
                final StringSelection selection = new StringSelection(content);
                final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(selection, selection);
                return true;
            }
            catch (Exception fuck) {
                fuck.printStackTrace();
                return false;
            }
        }
        
        public static boolean currentScreenMinecraft() {
            return Utils.mc.field_71462_r == null;
        }
        
        public static int serverResponseTime() {
            return Utils.mc.func_147114_u().func_175102_a(Utils.mc.field_71439_g.func_110124_au()).func_178853_c();
        }
        
        public static List<String> getPlayersFromScoreboard() {
            final List<String> lines = new ArrayList<String>();
            if (Utils.mc.field_71441_e == null) {
                return lines;
            }
            final Scoreboard scoreboard = Utils.mc.field_71441_e.func_96441_U();
            if (scoreboard != null) {
                final ScoreObjective objective = scoreboard.func_96539_a(1);
                if (objective != null) {
                    Collection<Score> scores = (Collection<Score>)scoreboard.func_96534_i(objective);
                    final List<Score> list = new ArrayList<Score>();
                    for (final Score score : scores) {
                        if (score != null && score.func_96653_e() != null && !score.func_96653_e().startsWith("#")) {
                            list.add(score);
                        }
                    }
                    if (list.size() > 15) {
                        scores = (Collection<Score>)Lists.newArrayList(Iterables.skip((Iterable)list, scores.size() - 15));
                    }
                    else {
                        scores = list;
                    }
                    for (final Score score : scores) {
                        final ScorePlayerTeam team = scoreboard.func_96509_i(score.func_96653_e());
                        lines.add(ScorePlayerTeam.func_96667_a((Team)team, score.func_96653_e()));
                    }
                }
            }
            return lines;
        }
        
        public static String reformat(final String txt) {
            return txt.replace("&", "��");
        }
    }
    
    public static class Java
    {
        public static void throwException(final Exception e) {
            try {
                throw e;
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        
        public static int getValue(final JsonObject type, final String member) {
            try {
                return type.get(member).getAsInt();
            }
            catch (NullPointerException er) {
                return 0;
            }
        }
        
        public static int indexOf(final String key, final String[] wut) {
            for (int o = 0; o < wut.length; ++o) {
                if (wut[o].equals(key)) {
                    return o;
                }
            }
            return -1;
        }
        
        public static long getSystemTime() {
            return Sys.getTime() * 1000L / Sys.getTimerResolution();
        }
        
        public static Random rand() {
            return Utils.rand;
        }
        
        public static double round(final double n, final int d) {
            if (d == 0) {
                return (double)Math.round(n);
            }
            final double p = Math.pow(10.0, d);
            return Math.round(n * p) / p;
        }
        
        public static String str(final String s) {
            final char[] n = StringUtils.func_76338_a(s).toCharArray();
            final StringBuilder v = new StringBuilder();
            for (final char c : n) {
                if (c < '\u007f' && c > '\u0014') {
                    v.append(c);
                }
            }
            return v.toString();
        }
        
        public static String capitalizeWord(final String s) {
            return s.substring(0, 1).toUpperCase() + s.substring(1);
        }
        
        public static String getDate() {
            final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            final LocalDateTime now = LocalDateTime.now();
            return dtf.format(now);
        }
        
        public static String joinStringList(final String[] wtf, final String okwaht) {
            if (wtf == null) {
                return "";
            }
            if (wtf.length <= 1) {
                return "";
            }
            final StringBuilder finalString = new StringBuilder(wtf[0]);
            for (int i = 1; i < wtf.length; ++i) {
                finalString.append(okwaht).append(wtf[i]);
            }
            return finalString.toString();
        }
        
        public static ArrayList<String> toArrayList(final String[] fakeList) {
            return new ArrayList<String>(Arrays.asList(fakeList));
        }
        
        public static List<String> StringListToList(final String[] whytho) {
            final List<String> howTohackNasaWorking2021NoScamDotCom = new ArrayList<String>();
            Collections.addAll(howTohackNasaWorking2021NoScamDotCom, whytho);
            return howTohackNasaWorking2021NoScamDotCom;
        }
        
        public static JsonObject getStringAsJson(final String text) {
            return new JsonParser().parse(text).getAsJsonObject();
        }
        
        public static String randomChoice(final String[] strings) {
            return strings[Utils.rand.nextInt(strings.length)];
        }
        
        public static int randomInt(final double inputMin, final double v) {
            return (int)(Math.random() * (v - inputMin) + inputMin);
        }
    }
    
    public static class URLS
    {
        public static final String base_url = "https://api.paste.ee/v1/pastes/";
        public static final String base_paste = "{\"description\":\"Raven B+ Config\",\"expiration\":\"never\",\"sections\":[{\"name\":\"TitleGoesHere\",\"syntax\":\"text\",\"contents\":\"BodyGoesHere\"}]}";
        public static String hypixelApiKey;
        public static String pasteApiKey;
        
        public static boolean isHypixelKeyValid(final String ak) {
            final String c = getTextFromURL("https://api.hypixel.net/key?key=" + ak);
            return !c.isEmpty() && !c.contains("Invalid");
        }
        
        public static String getTextFromURL(final String _url) {
            String r = "";
            HttpURLConnection con = null;
            try {
                final URL url = new URL(_url);
                con = (HttpURLConnection)url.openConnection();
                r = getTextFromConnection(con);
            }
            catch (IOException ex) {}
            finally {
                if (con != null) {
                    con.disconnect();
                }
            }
            return r;
        }
        
        private static String getTextFromConnection(final HttpURLConnection connection) {
            if (connection != null) {
                try {
                    final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String result;
                    try {
                        final StringBuilder stringBuilder = new StringBuilder();
                        String input;
                        while ((input = bufferedReader.readLine()) != null) {
                            stringBuilder.append(input);
                        }
                        final String res = stringBuilder.toString();
                        connection.disconnect();
                        result = res;
                    }
                    finally {
                        bufferedReader.close();
                    }
                    return result;
                }
                catch (Exception ex) {}
            }
            return "";
        }
        
        public static boolean isLink(final String string) {
            return string.startsWith("http") && string.contains(".") && string.contains("://");
        }
        
        public static boolean isPasteeLink(final String link) {
            return isLink(link) && link.contains("paste.ee");
        }
        
        public static String makeRawPasteePaste(final String arg) {
            final StringBuilder rawLink = new StringBuilder();
            rawLink.append("https://api.paste.ee/v1/pastes/");
            rawLink.append(arg.split("/")[arg.split("/").length - 1]);
            return rawLink.toString();
        }
        
        public static String createPaste(final String name, final String content) {
            try {
                final HttpURLConnection request = (HttpURLConnection)new URL("https://api.paste.ee/v1/pastes/").openConnection();
                request.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                request.setRequestProperty("X-Auth-Token", URLS.pasteApiKey);
                request.setRequestMethod("POST");
                request.setDoOutput(true);
                request.connect();
                final OutputStream outputStream = request.getOutputStream();
                Throwable occuredErrors = null;
                final String payload = "{\"description\":\"Raven B+ Config\",\"expiration\":\"never\",\"sections\":[{\"name\":\"TitleGoesHere\",\"syntax\":\"text\",\"contents\":\"BodyGoesHere\"}]}".replace("TitleGoesHere", name).replace("BodyGoesHere", content).replace("\\", "");
                try {
                    outputStream.write(payload.getBytes(StandardCharsets.UTF_8));
                    outputStream.flush();
                }
                catch (Throwable microsoftMoment) {
                    occuredErrors = microsoftMoment;
                    throw microsoftMoment;
                }
                finally {
                    if (outputStream != null) {
                        if (occuredErrors != null) {
                            try {
                                outputStream.close();
                            }
                            catch (Throwable var48) {
                                occuredErrors.addSuppressed(var48);
                            }
                        }
                        else {
                            outputStream.close();
                        }
                    }
                }
                request.disconnect();
                final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(request.getInputStream()));
                final JsonParser parser = new JsonParser();
                final JsonObject json = (JsonObject)parser.parse(bufferedReader.readLine());
                return json.get("link").toString().replace("\"", "");
            }
            catch (Exception ex) {
                return "";
            }
        }
        
        public static List<String> getConfigFromPastee(final String link) {
            try {
                final HttpURLConnection request = (HttpURLConnection)new URL(link).openConnection();
                request.setRequestProperty("X-Auth-Token", URLS.pasteApiKey);
                request.setRequestMethod("GET");
                request.setDoOutput(true);
                request.connect();
                final List<String> finall = new ArrayList<String>();
                final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(request.getInputStream()));
                final JsonParser parser = new JsonParser();
                final JsonObject json = (JsonObject)parser.parse(bufferedReader.readLine());
                final JsonObject json2 = json.getAsJsonObject("paste");
                finall.add("true");
                final JsonObject json3 = (JsonObject)json2.getAsJsonArray("sections").get(0);
                finall.add(json3.get("name") + "");
                finall.add(json3.get("contents") + "");
                request.disconnect();
                return finall;
            }
            catch (Exception var51) {
                var51.printStackTrace();
                final List<String> welp = new ArrayList<String>();
                welp.add("false");
                return welp;
            }
        }
        
        static {
            URLS.hypixelApiKey = "";
            URLS.pasteApiKey = "";
        }
    }
    
    public static class Profiles
    {
        public static String getUUIDFromName(final String n) {
            String u = "";
            final String r = URLS.getTextFromURL("https://api.mojang.com/users/profiles/minecraft/" + n);
            if (!r.isEmpty()) {
                try {
                    u = r.split("d\":\"")[1].split("\"")[0];
                }
                catch (ArrayIndexOutOfBoundsException ex) {}
            }
            return u;
        }
        
        public static int[] getHypixelStats(final String UUID, final DuelsStatsMode dm) {
            final int[] s = { 0, 0, 0 };
            final String u = UUID;
            final String c = URLS.getTextFromURL("https://api.hypixel.net/player?key=" + URLS.hypixelApiKey + "&uuid=" + u);
            if (c.isEmpty()) {
                return null;
            }
            if (c.equals("{\"success\":true,\"player\":null}")) {
                s[0] = -1;
                return s;
            }
            JsonObject d;
            try {
                final JsonObject pr = parseJson(c).getAsJsonObject("player");
                d = pr.getAsJsonObject("stats").getAsJsonObject("Duels");
            }
            catch (NullPointerException var8) {
                return s;
            }
            switch (dm) {
                case OVERALL: {
                    s[0] = getValueAsInt(d, "wins");
                    s[1] = getValueAsInt(d, "losses");
                    s[2] = getValueAsInt(d, "current_winstreak");
                    break;
                }
                case BRIDGE: {
                    s[0] = getValueAsInt(d, "bridge_duel_wins");
                    s[1] = getValueAsInt(d, "bridge_duel_losses");
                    s[2] = getValueAsInt(d, "current_winstreak_mode_bridge_duel");
                    break;
                }
                case UHC: {
                    s[0] = getValueAsInt(d, "uhc_duel_wins");
                    s[1] = getValueAsInt(d, "uhc_duel_losses");
                    s[2] = getValueAsInt(d, "current_winstreak_mode_uhc_duel");
                    break;
                }
                case SKYWARS: {
                    s[0] = getValueAsInt(d, "sw_duel_wins");
                    s[1] = getValueAsInt(d, "sw_duel_losses");
                    s[2] = getValueAsInt(d, "current_winstreak_mode_sw_duel");
                    break;
                }
                case CLASSIC: {
                    s[0] = getValueAsInt(d, "classic_duel_wins");
                    s[1] = getValueAsInt(d, "classic_duel_losses");
                    s[2] = getValueAsInt(d, "current_winstreak_mode_classic_duel");
                    break;
                }
                case SUMO: {
                    s[0] = getValueAsInt(d, "sumo_duel_wins");
                    s[1] = getValueAsInt(d, "sumo_duel_losses");
                    s[2] = getValueAsInt(d, "current_winstreak_mode_sumo_duel");
                    break;
                }
                case OP: {
                    s[0] = getValueAsInt(d, "op_duel_wins");
                    s[1] = getValueAsInt(d, "op_duel_losses");
                    s[2] = getValueAsInt(d, "current_winstreak_mode_op_duel");
                    break;
                }
            }
            return s;
        }
        
        public static JsonObject parseJson(final String json) {
            return new JsonParser().parse(json).getAsJsonObject();
        }
        
        public static int getValueAsInt(final JsonObject jsonObject, final String key) {
            try {
                return jsonObject.get(key).getAsInt();
            }
            catch (NullPointerException var3) {
                return 0;
            }
        }
        
        public enum DuelsStatsMode
        {
            OVERALL, 
            BRIDGE, 
            UHC, 
            SKYWARS, 
            CLASSIC, 
            SUMO, 
            OP;
        }
    }
    
    public static class HUD
    {
        public static final int rc = -1089466352;
        private static final double p2 = 6.283185307179586;
        private static final Minecraft mc;
        public static boolean ring_c;
        
        public static void re(final BlockPos bp, final int color, final boolean shade) {
            if (bp != null) {
                final double x = bp.func_177958_n() - HUD.mc.func_175598_ae().field_78730_l;
                final double y = bp.func_177956_o() - HUD.mc.func_175598_ae().field_78731_m;
                final double z = bp.func_177952_p() - HUD.mc.func_175598_ae().field_78728_n;
                GL11.glBlendFunc(770, 771);
                GL11.glEnable(3042);
                GL11.glLineWidth(2.0f);
                GL11.glDisable(3553);
                GL11.glDisable(2929);
                GL11.glDepthMask(false);
                final float a = (color >> 24 & 0xFF) / 255.0f;
                final float r = (color >> 16 & 0xFF) / 255.0f;
                final float g = (color >> 8 & 0xFF) / 255.0f;
                final float b = (color & 0xFF) / 255.0f;
                GL11.glColor4d((double)r, (double)g, (double)b, (double)a);
                RenderGlobal.func_181561_a(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
                if (shade) {
                    dbb(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0), r, g, b);
                }
                GL11.glEnable(3553);
                GL11.glEnable(2929);
                GL11.glDepthMask(true);
                GL11.glDisable(3042);
            }
        }
        
        public static void drawBoxAroundEntity(final Entity e, final int type, final double expand, final double shift, int color, final boolean damage) {
            if (e instanceof EntityLivingBase) {
                final double x = e.field_70142_S + (e.field_70165_t - e.field_70142_S) * Client.getTimer().field_74281_c - HUD.mc.func_175598_ae().field_78730_l;
                final double y = e.field_70137_T + (e.field_70163_u - e.field_70137_T) * Client.getTimer().field_74281_c - HUD.mc.func_175598_ae().field_78731_m;
                final double z = e.field_70136_U + (e.field_70161_v - e.field_70136_U) * Client.getTimer().field_74281_c - HUD.mc.func_175598_ae().field_78728_n;
                final float d = (float)expand / 40.0f;
                if (e instanceof EntityPlayer && damage && ((EntityPlayer)e).field_70737_aN != 0) {
                    color = Color.RED.getRGB();
                }
                GlStateManager.func_179094_E();
                if (type == 3) {
                    GL11.glTranslated(x, y - 0.2, z);
                    GL11.glRotated((double)(-HUD.mc.func_175598_ae().field_78735_i), 0.0, 1.0, 0.0);
                    GlStateManager.func_179097_i();
                    GL11.glScalef(0.03f + d, 0.03f + d, 0.03f + d);
                    final int outline = Color.black.getRGB();
                    Gui.func_73734_a(-20, -1, -26, 75, outline);
                    Gui.func_73734_a(20, -1, 26, 75, outline);
                    Gui.func_73734_a(-20, -1, 21, 5, outline);
                    Gui.func_73734_a(-20, 70, 21, 75, outline);
                    if (color != 0) {
                        Gui.func_73734_a(-21, 0, -25, 74, color);
                        Gui.func_73734_a(21, 0, 25, 74, color);
                        Gui.func_73734_a(-21, 0, 24, 4, color);
                        Gui.func_73734_a(-21, 71, 25, 74, color);
                    }
                    else {
                        final int st = Client.rainbowDraw(2L, 0L);
                        final int en = Client.rainbowDraw(2L, 1000L);
                        dGR(-21, 0, -25, 74, st, en);
                        dGR(21, 0, 25, 74, st, en);
                        Gui.func_73734_a(-21, 0, 21, 4, en);
                        Gui.func_73734_a(-21, 71, 21, 74, st);
                    }
                    GlStateManager.func_179126_j();
                }
                else if (type == 4) {
                    final EntityLivingBase en2 = (EntityLivingBase)e;
                    final double r = en2.func_110143_aJ() / en2.func_110138_aP();
                    final int b = (int)(74.0 * r);
                    final int hc = (r < 0.3) ? Color.red.getRGB() : ((r < 0.5) ? Color.orange.getRGB() : ((r < 0.7) ? Color.yellow.getRGB() : Color.green.getRGB()));
                    GL11.glTranslated(x, y - 0.2, z);
                    GL11.glRotated((double)(-HUD.mc.func_175598_ae().field_78735_i), 0.0, 1.0, 0.0);
                    GlStateManager.func_179097_i();
                    GL11.glScalef(0.03f + d, 0.03f + d, 0.03f + d);
                    final int i = (int)(21.0 + shift * 2.0);
                    Gui.func_73734_a(i, -1, i + 5, 75, Color.black.getRGB());
                    Gui.func_73734_a(i + 1, b, i + 4, 74, Color.darkGray.getRGB());
                    Gui.func_73734_a(i + 1, 0, i + 4, b, hc);
                    GlStateManager.func_179126_j();
                }
                else if (type == 6) {
                    d3p(x, y, z, 0.699999988079071, 45, 1.5f, color, color == 0);
                }
                else {
                    if (color == 0) {
                        color = Client.rainbowDraw(2L, 0L);
                    }
                    final float a = (color >> 24 & 0xFF) / 255.0f;
                    final float r2 = (color >> 16 & 0xFF) / 255.0f;
                    final float g = (color >> 8 & 0xFF) / 255.0f;
                    final float b2 = (color & 0xFF) / 255.0f;
                    if (type == 5) {
                        GL11.glTranslated(x, y - 0.2, z);
                        GL11.glRotated((double)(-HUD.mc.func_175598_ae().field_78735_i), 0.0, 1.0, 0.0);
                        GlStateManager.func_179097_i();
                        GL11.glScalef(0.03f + d, 0.03f, 0.03f + d);
                        final int base = 1;
                        d2p(0.0, 95.0, 10, 3, Color.black.getRGB());
                        for (int i = 0; i < 6; ++i) {
                            d2p(0.0, 95 + (10 - i), 3, 4, Color.black.getRGB());
                        }
                        for (int i = 0; i < 7; ++i) {
                            d2p(0.0, 95 + (10 - i), 2, 4, color);
                        }
                        d2p(0.0, 95.0, 8, 3, color);
                        GlStateManager.func_179126_j();
                    }
                    else {
                        final AxisAlignedBB bbox = e.func_174813_aQ().func_72314_b(0.1 + expand, 0.1 + expand, 0.1 + expand);
                        final AxisAlignedBB axis = new AxisAlignedBB(bbox.field_72340_a - e.field_70165_t + x, bbox.field_72338_b - e.field_70163_u + y, bbox.field_72339_c - e.field_70161_v + z, bbox.field_72336_d - e.field_70165_t + x, bbox.field_72337_e - e.field_70163_u + y, bbox.field_72334_f - e.field_70161_v + z);
                        GL11.glBlendFunc(770, 771);
                        GL11.glEnable(3042);
                        GL11.glDisable(3553);
                        GL11.glDisable(2929);
                        GL11.glDepthMask(false);
                        GL11.glLineWidth(2.0f);
                        GL11.glColor4f(r2, g, b2, a);
                        if (type == 1) {
                            RenderGlobal.func_181561_a(axis);
                        }
                        else if (type == 2) {
                            dbb(axis, r2, g, b2);
                        }
                        GL11.glEnable(3553);
                        GL11.glEnable(2929);
                        GL11.glDepthMask(true);
                        GL11.glDisable(3042);
                    }
                }
                GlStateManager.func_179121_F();
            }
        }
        
        public static void dbb(final AxisAlignedBB abb, final float r, final float g, final float b) {
            final float a = 0.25f;
            final Tessellator ts = Tessellator.func_178181_a();
            final WorldRenderer vb = ts.func_178180_c();
            vb.func_181668_a(7, DefaultVertexFormats.field_181706_f);
            vb.func_181662_b(abb.field_72340_a, abb.field_72338_b, abb.field_72339_c).func_181666_a(r, g, b, a).func_181675_d();
            vb.func_181662_b(abb.field_72340_a, abb.field_72337_e, abb.field_72339_c).func_181666_a(r, g, b, a).func_181675_d();
            vb.func_181662_b(abb.field_72336_d, abb.field_72338_b, abb.field_72339_c).func_181666_a(r, g, b, a).func_181675_d();
            vb.func_181662_b(abb.field_72336_d, abb.field_72337_e, abb.field_72339_c).func_181666_a(r, g, b, a).func_181675_d();
            vb.func_181662_b(abb.field_72336_d, abb.field_72338_b, abb.field_72334_f).func_181666_a(r, g, b, a).func_181675_d();
            vb.func_181662_b(abb.field_72336_d, abb.field_72337_e, abb.field_72334_f).func_181666_a(r, g, b, a).func_181675_d();
            vb.func_181662_b(abb.field_72340_a, abb.field_72338_b, abb.field_72334_f).func_181666_a(r, g, b, a).func_181675_d();
            vb.func_181662_b(abb.field_72340_a, abb.field_72337_e, abb.field_72334_f).func_181666_a(r, g, b, a).func_181675_d();
            ts.func_78381_a();
            vb.func_181668_a(7, DefaultVertexFormats.field_181706_f);
            vb.func_181662_b(abb.field_72336_d, abb.field_72337_e, abb.field_72339_c).func_181666_a(r, g, b, a).func_181675_d();
            vb.func_181662_b(abb.field_72336_d, abb.field_72338_b, abb.field_72339_c).func_181666_a(r, g, b, a).func_181675_d();
            vb.func_181662_b(abb.field_72340_a, abb.field_72337_e, abb.field_72339_c).func_181666_a(r, g, b, a).func_181675_d();
            vb.func_181662_b(abb.field_72340_a, abb.field_72338_b, abb.field_72339_c).func_181666_a(r, g, b, a).func_181675_d();
            vb.func_181662_b(abb.field_72340_a, abb.field_72337_e, abb.field_72334_f).func_181666_a(r, g, b, a).func_181675_d();
            vb.func_181662_b(abb.field_72340_a, abb.field_72338_b, abb.field_72334_f).func_181666_a(r, g, b, a).func_181675_d();
            vb.func_181662_b(abb.field_72336_d, abb.field_72337_e, abb.field_72334_f).func_181666_a(r, g, b, a).func_181675_d();
            vb.func_181662_b(abb.field_72336_d, abb.field_72338_b, abb.field_72334_f).func_181666_a(r, g, b, a).func_181675_d();
            ts.func_78381_a();
            vb.func_181668_a(7, DefaultVertexFormats.field_181706_f);
            vb.func_181662_b(abb.field_72340_a, abb.field_72337_e, abb.field_72339_c).func_181666_a(r, g, b, a).func_181675_d();
            vb.func_181662_b(abb.field_72336_d, abb.field_72337_e, abb.field_72339_c).func_181666_a(r, g, b, a).func_181675_d();
            vb.func_181662_b(abb.field_72336_d, abb.field_72337_e, abb.field_72334_f).func_181666_a(r, g, b, a).func_181675_d();
            vb.func_181662_b(abb.field_72340_a, abb.field_72337_e, abb.field_72334_f).func_181666_a(r, g, b, a).func_181675_d();
            vb.func_181662_b(abb.field_72340_a, abb.field_72337_e, abb.field_72339_c).func_181666_a(r, g, b, a).func_181675_d();
            vb.func_181662_b(abb.field_72340_a, abb.field_72337_e, abb.field_72334_f).func_181666_a(r, g, b, a).func_181675_d();
            vb.func_181662_b(abb.field_72336_d, abb.field_72337_e, abb.field_72334_f).func_181666_a(r, g, b, a).func_181675_d();
            vb.func_181662_b(abb.field_72336_d, abb.field_72337_e, abb.field_72339_c).func_181666_a(r, g, b, a).func_181675_d();
            ts.func_78381_a();
            vb.func_181668_a(7, DefaultVertexFormats.field_181706_f);
            vb.func_181662_b(abb.field_72340_a, abb.field_72338_b, abb.field_72339_c).func_181666_a(r, g, b, a).func_181675_d();
            vb.func_181662_b(abb.field_72336_d, abb.field_72338_b, abb.field_72339_c).func_181666_a(r, g, b, a).func_181675_d();
            vb.func_181662_b(abb.field_72336_d, abb.field_72338_b, abb.field_72334_f).func_181666_a(r, g, b, a).func_181675_d();
            vb.func_181662_b(abb.field_72340_a, abb.field_72338_b, abb.field_72334_f).func_181666_a(r, g, b, a).func_181675_d();
            vb.func_181662_b(abb.field_72340_a, abb.field_72338_b, abb.field_72339_c).func_181666_a(r, g, b, a).func_181675_d();
            vb.func_181662_b(abb.field_72340_a, abb.field_72338_b, abb.field_72334_f).func_181666_a(r, g, b, a).func_181675_d();
            vb.func_181662_b(abb.field_72336_d, abb.field_72338_b, abb.field_72334_f).func_181666_a(r, g, b, a).func_181675_d();
            vb.func_181662_b(abb.field_72336_d, abb.field_72338_b, abb.field_72339_c).func_181666_a(r, g, b, a).func_181675_d();
            ts.func_78381_a();
            vb.func_181668_a(7, DefaultVertexFormats.field_181706_f);
            vb.func_181662_b(abb.field_72340_a, abb.field_72338_b, abb.field_72339_c).func_181666_a(r, g, b, a).func_181675_d();
            vb.func_181662_b(abb.field_72340_a, abb.field_72337_e, abb.field_72339_c).func_181666_a(r, g, b, a).func_181675_d();
            vb.func_181662_b(abb.field_72340_a, abb.field_72338_b, abb.field_72334_f).func_181666_a(r, g, b, a).func_181675_d();
            vb.func_181662_b(abb.field_72340_a, abb.field_72337_e, abb.field_72334_f).func_181666_a(r, g, b, a).func_181675_d();
            vb.func_181662_b(abb.field_72336_d, abb.field_72338_b, abb.field_72334_f).func_181666_a(r, g, b, a).func_181675_d();
            vb.func_181662_b(abb.field_72336_d, abb.field_72337_e, abb.field_72334_f).func_181666_a(r, g, b, a).func_181675_d();
            vb.func_181662_b(abb.field_72336_d, abb.field_72338_b, abb.field_72339_c).func_181666_a(r, g, b, a).func_181675_d();
            vb.func_181662_b(abb.field_72336_d, abb.field_72337_e, abb.field_72339_c).func_181666_a(r, g, b, a).func_181675_d();
            ts.func_78381_a();
            vb.func_181668_a(7, DefaultVertexFormats.field_181706_f);
            vb.func_181662_b(abb.field_72340_a, abb.field_72337_e, abb.field_72334_f).func_181666_a(r, g, b, a).func_181675_d();
            vb.func_181662_b(abb.field_72340_a, abb.field_72338_b, abb.field_72334_f).func_181666_a(r, g, b, a).func_181675_d();
            vb.func_181662_b(abb.field_72340_a, abb.field_72337_e, abb.field_72339_c).func_181666_a(r, g, b, a).func_181675_d();
            vb.func_181662_b(abb.field_72340_a, abb.field_72338_b, abb.field_72339_c).func_181666_a(r, g, b, a).func_181675_d();
            vb.func_181662_b(abb.field_72336_d, abb.field_72337_e, abb.field_72339_c).func_181666_a(r, g, b, a).func_181675_d();
            vb.func_181662_b(abb.field_72336_d, abb.field_72338_b, abb.field_72339_c).func_181666_a(r, g, b, a).func_181675_d();
            vb.func_181662_b(abb.field_72336_d, abb.field_72337_e, abb.field_72334_f).func_181666_a(r, g, b, a).func_181675_d();
            vb.func_181662_b(abb.field_72336_d, abb.field_72338_b, abb.field_72334_f).func_181666_a(r, g, b, a).func_181675_d();
            ts.func_78381_a();
        }
        
        public static void dtl(final Entity e, final int color, final float lw) {
            if (e != null) {
                final double x = e.field_70142_S + (e.field_70165_t - e.field_70142_S) * Client.getTimer().field_74281_c - HUD.mc.func_175598_ae().field_78730_l;
                final double y = e.func_70047_e() + e.field_70137_T + (e.field_70163_u - e.field_70137_T) * Client.getTimer().field_74281_c - HUD.mc.func_175598_ae().field_78731_m;
                final double z = e.field_70136_U + (e.field_70161_v - e.field_70136_U) * Client.getTimer().field_74281_c - HUD.mc.func_175598_ae().field_78728_n;
                final float a = (color >> 24 & 0xFF) / 255.0f;
                final float r = (color >> 16 & 0xFF) / 255.0f;
                final float g = (color >> 8 & 0xFF) / 255.0f;
                final float b = (color & 0xFF) / 255.0f;
                GL11.glPushMatrix();
                GL11.glEnable(3042);
                GL11.glEnable(2848);
                GL11.glDisable(2929);
                GL11.glDisable(3553);
                GL11.glBlendFunc(770, 771);
                GL11.glEnable(3042);
                GL11.glLineWidth(lw);
                GL11.glColor4f(r, g, b, a);
                GL11.glBegin(2);
                GL11.glVertex3d(0.0, (double)HUD.mc.field_71439_g.func_70047_e(), 0.0);
                GL11.glVertex3d(x, y, z);
                GL11.glEnd();
                GL11.glDisable(3042);
                GL11.glEnable(3553);
                GL11.glEnable(2929);
                GL11.glDisable(2848);
                GL11.glDisable(3042);
                GL11.glPopMatrix();
            }
        }
        
        public static void dGR(int left, int top, int right, int bottom, final int startColor, final int endColor) {
            if (left < right) {
                final int j = left;
                left = right;
                right = j;
            }
            if (top < bottom) {
                final int j = top;
                top = bottom;
                bottom = j;
            }
            final float f = (startColor >> 24 & 0xFF) / 255.0f;
            final float f2 = (startColor >> 16 & 0xFF) / 255.0f;
            final float f3 = (startColor >> 8 & 0xFF) / 255.0f;
            final float f4 = (startColor & 0xFF) / 255.0f;
            final float f5 = (endColor >> 24 & 0xFF) / 255.0f;
            final float f6 = (endColor >> 16 & 0xFF) / 255.0f;
            final float f7 = (endColor >> 8 & 0xFF) / 255.0f;
            final float f8 = (endColor & 0xFF) / 255.0f;
            GlStateManager.func_179090_x();
            GlStateManager.func_179147_l();
            GlStateManager.func_179118_c();
            GlStateManager.func_179120_a(770, 771, 1, 0);
            GlStateManager.func_179103_j(7425);
            final Tessellator tessellator = Tessellator.func_178181_a();
            final WorldRenderer worldrenderer = tessellator.func_178180_c();
            worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181706_f);
            worldrenderer.func_181662_b((double)right, (double)top, 0.0).func_181666_a(f2, f3, f4, f).func_181675_d();
            worldrenderer.func_181662_b((double)left, (double)top, 0.0).func_181666_a(f2, f3, f4, f).func_181675_d();
            worldrenderer.func_181662_b((double)left, (double)bottom, 0.0).func_181666_a(f6, f7, f8, f5).func_181675_d();
            worldrenderer.func_181662_b((double)right, (double)bottom, 0.0).func_181666_a(f6, f7, f8, f5).func_181675_d();
            tessellator.func_78381_a();
            GlStateManager.func_179103_j(7424);
            GlStateManager.func_179084_k();
            GlStateManager.func_179141_d();
            GlStateManager.func_179098_w();
        }
        
        public static void db(final int w, final int h, final int r) {
            final int c = (r == -1) ? -1089466352 : r;
            Gui.func_73734_a(0, 0, w, h, c);
        }
        
        public static void drawColouredText(final String text, final char lineSplit, int leftOffset, int topOffset, final long colourParam1, final long shift, final boolean rect, final FontRenderer fontRenderer) {
            final int bX = leftOffset;
            int l = 0;
            long colourControl = 0L;
            for (int i = 0; i < text.length(); ++i) {
                final char c = text.charAt(i);
                if (c == lineSplit) {
                    ++l;
                    leftOffset = bX;
                    topOffset += fontRenderer.field_78288_b + 5;
                    colourControl = shift * l;
                }
                else {
                    fontRenderer.func_175065_a(String.valueOf(c), (float)leftOffset, (float)topOffset, Client.astolfoColorsDraw((int)colourParam1, (int)colourControl), rect);
                    leftOffset += fontRenderer.func_78263_a(c);
                    if (c != ' ') {
                        colourControl -= 90L;
                    }
                }
            }
        }
        
        public static PositionMode getPostitionMode(final int marginX, final int marginY, final double height, final double width) {
            final int halfHeight = (int)(height / 4.0);
            final int halfWidth = (int)width;
            PositionMode positionMode = null;
            if (marginY < halfHeight) {
                if (marginX < halfWidth) {
                    positionMode = PositionMode.UPLEFT;
                }
                if (marginX > halfWidth) {
                    positionMode = PositionMode.UPRIGHT;
                }
            }
            if (marginY > halfHeight) {
                if (marginX < halfWidth) {
                    positionMode = PositionMode.DOWNLEFT;
                }
                if (marginX > halfWidth) {
                    positionMode = PositionMode.DOWNRIGHT;
                }
            }
            return positionMode;
        }
        
        public static void d2p(final double x, final double y, final int radius, final int sides, final int color) {
            final float a = (color >> 24 & 0xFF) / 255.0f;
            final float r = (color >> 16 & 0xFF) / 255.0f;
            final float g = (color >> 8 & 0xFF) / 255.0f;
            final float b = (color & 0xFF) / 255.0f;
            final Tessellator tessellator = Tessellator.func_178181_a();
            final WorldRenderer worldrenderer = tessellator.func_178180_c();
            GlStateManager.func_179147_l();
            GlStateManager.func_179090_x();
            GlStateManager.func_179120_a(770, 771, 1, 0);
            GlStateManager.func_179131_c(r, g, b, a);
            worldrenderer.func_181668_a(6, DefaultVertexFormats.field_181705_e);
            for (int i = 0; i < sides; ++i) {
                final double angle = 6.283185307179586 * i / sides + Math.toRadians(180.0);
                worldrenderer.func_181662_b(x + Math.sin(angle) * radius, y + Math.cos(angle) * radius, 0.0).func_181675_d();
            }
            tessellator.func_78381_a();
            GlStateManager.func_179098_w();
            GlStateManager.func_179084_k();
        }
        
        public static void d3p(final double x, final double y, final double z, final double radius, final int sides, final float lineWidth, final int color, final boolean chroma) {
            final float a = (color >> 24 & 0xFF) / 255.0f;
            final float r = (color >> 16 & 0xFF) / 255.0f;
            final float g = (color >> 8 & 0xFF) / 255.0f;
            final float b = (color & 0xFF) / 255.0f;
            HUD.mc.field_71460_t.func_175072_h();
            GL11.glDisable(3553);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glDisable(2929);
            GL11.glEnable(2848);
            GL11.glDepthMask(false);
            GL11.glLineWidth(lineWidth);
            if (!chroma) {
                GL11.glColor4f(r, g, b, a);
            }
            GL11.glBegin(1);
            long d = 0L;
            final long ed = 15000L / sides;
            final long hed = ed / 2L;
            for (int i = 0; i < sides * 2; ++i) {
                if (chroma) {
                    if (i % 2 != 0) {
                        if (i == 47) {
                            d = hed;
                        }
                        d += ed;
                    }
                    final int c = Client.rainbowDraw(2L, d);
                    final float r2 = (c >> 16 & 0xFF) / 255.0f;
                    final float g2 = (c >> 8 & 0xFF) / 255.0f;
                    final float b2 = (c & 0xFF) / 255.0f;
                    GL11.glColor3f(r2, g2, b2);
                }
                final double angle = 6.283185307179586 * i / sides + Math.toRadians(180.0);
                GL11.glVertex3d(x + Math.cos(angle) * radius, y, z + Math.sin(angle) * radius);
            }
            GL11.glEnd();
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glDepthMask(true);
            GL11.glDisable(2848);
            GL11.glEnable(2929);
            GL11.glDisable(3042);
            GL11.glEnable(3553);
            HUD.mc.field_71460_t.func_180436_i();
        }
        
        static {
            mc = Minecraft.func_71410_x();
        }
        
        public enum PositionMode
        {
            UPLEFT, 
            UPRIGHT, 
            DOWNLEFT, 
            DOWNRIGHT;
        }
    }
    
    public static class Modes
    {
        public enum ClickEvents
        {
            RENDER, 
            TICK;
        }
        
        public enum BridgeMode
        {
            GODBRIDGE, 
            MOONWALK, 
            BREEZILY, 
            NORMAL;
        }
        
        public enum ClickTimings
        {
            RAVEN, 
            SKID;
        }
        
        public enum SprintResetTimings
        {
            PRE, 
            POST;
        }
    }
}
