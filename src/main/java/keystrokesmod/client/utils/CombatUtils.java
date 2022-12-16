package keystrokesmod.client.utils;

import net.minecraft.client.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.item.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.monster.*;

public class CombatUtils
{
    public static boolean canTarget(final Entity entity, final boolean idk) {
        if (entity != null && entity != Minecraft.func_71410_x().field_71439_g) {
            EntityLivingBase entityLivingBase = null;
            if (entity instanceof EntityLivingBase) {
                entityLivingBase = (EntityLivingBase)entity;
            }
            final boolean isTeam = isTeam((EntityPlayer)Minecraft.func_71410_x().field_71439_g, entity);
            final boolean isVisible = !entity.func_82150_aj();
            return !(entity instanceof EntityArmorStand) && isVisible && ((entity instanceof EntityPlayer && !isTeam && !idk) || entity instanceof EntityAnimal || entity instanceof EntityMob || (entity instanceof EntityLivingBase && entityLivingBase.func_70089_S()));
        }
        return false;
    }
    
    public static boolean isTeam(final EntityPlayer player, final Entity entity) {
        if (entity instanceof EntityPlayer && ((EntityPlayer)entity).func_96124_cp() != null && player.func_96124_cp() != null) {
            final Character entity_3 = entity.func_145748_c_().func_150254_d().charAt(3);
            final Character player_3 = player.func_145748_c_().func_150254_d().charAt(3);
            final Character entity_4 = entity.func_145748_c_().func_150254_d().charAt(2);
            final Character player_4 = player.func_145748_c_().func_150254_d().charAt(2);
            boolean isTeam = false;
            if (entity_3.equals(player_3) && entity_4.equals(player_4)) {
                isTeam = true;
            }
            else {
                final Character entity_5 = entity.func_145748_c_().func_150254_d().charAt(1);
                final Character player_5 = player.func_145748_c_().func_150254_d().charAt(1);
                final Character entity_6 = entity.func_145748_c_().func_150254_d().charAt(0);
                final Character player_6 = player.func_145748_c_().func_150254_d().charAt(0);
                if (entity_5.equals(player_5) && Character.isDigit(0) && entity_6.equals(player_6)) {
                    isTeam = true;
                }
            }
            return isTeam;
        }
        return true;
    }
}
