package keystrokesmod.client.utils;

import net.minecraft.client.*;
import net.minecraft.enchantment.*;
import net.minecraft.init.*;
import net.minecraft.potion.*;
import java.util.*;
import net.minecraft.entity.ai.attributes.*;
import com.google.common.collect.*;
import net.minecraft.block.*;
import net.minecraft.block.material.*;
import keystrokesmod.client.mixin.mixins.*;
import net.minecraft.item.*;
import net.minecraft.entity.player.*;

public class InventoryUtils
{
    public static final int INCLUDE_ARMOR_BEGIN = 5;
    public static final int EXCLUDE_ARMOR_BEGIN = 9;
    public static final int ONLY_HOT_BAR_BEGIN = 36;
    public static final int END = 45;
    protected static final Minecraft mc;
    
    public static ItemStack getStackInSlot(final int index) {
        return InventoryUtils.mc.field_71439_g.field_71069_bz.func_75139_a(index).func_75211_c();
    }
    
    public static boolean isValid(final ItemStack stack, final boolean archery) {
        if (stack == null) {
            return false;
        }
        if (stack.func_77973_b() instanceof ItemBlock) {
            return isGoodBlockStack(stack);
        }
        if (stack.func_77973_b() instanceof ItemSword) {
            return isBestSword(stack);
        }
        if (stack.func_77973_b() instanceof ItemTool) {
            return isBestTool(stack);
        }
        if (stack.func_77973_b() instanceof ItemArmor) {
            return isBestArmor(stack);
        }
        if (stack.func_77973_b() instanceof ItemPotion) {
            return isBuffPotion(stack);
        }
        if (stack.func_77973_b() instanceof ItemFood) {
            return isGoodFood(stack);
        }
        if (stack.func_77973_b() instanceof ItemBow && archery) {
            return isBestBow(stack);
        }
        return (archery && stack.func_77973_b().func_77658_a().equals("item.arrow")) || stack.func_77973_b() instanceof ItemEnderPearl || isGoodItem(stack);
    }
    
    public static boolean isBestBow(final ItemStack itemStack) {
        double bestBowDmg = -1.0;
        ItemStack bestBow = null;
        for (int i = 9; i < 45; ++i) {
            final ItemStack stack = getStackInSlot(i);
            if (stack != null && stack.func_77973_b() instanceof ItemBow) {
                final double damage = getBowDamage(stack);
                if (damage > bestBowDmg) {
                    bestBow = stack;
                    bestBowDmg = damage;
                }
            }
        }
        return itemStack == bestBow || getBowDamage(itemStack) > bestBowDmg;
    }
    
    public static double getBowDamage(final ItemStack stack) {
        double damage = 0.0;
        if (stack.func_77973_b() instanceof ItemBow && stack.func_77948_v()) {
            damage += EnchantmentHelper.func_77506_a(Enchantment.field_77345_t.field_77352_x, stack);
        }
        return damage;
    }
    
    public static boolean isGoodItem(final ItemStack stack) {
        final Item item = stack.func_77973_b();
        return (!(item instanceof ItemBucket) || ((IItemBucket)item).getIsFull() == Blocks.field_150358_i) && !(item instanceof ItemExpBottle) && !(item instanceof ItemFishingRod) && !(item instanceof ItemEgg) && !(item instanceof ItemSnowball) && !(item instanceof ItemSkull) && !(item instanceof ItemBucket);
    }
    
    public static boolean isBuffPotion(final ItemStack stack) {
        final ItemPotion potion = (ItemPotion)stack.func_77973_b();
        final List<PotionEffect> effects = (List<PotionEffect>)potion.func_77832_l(stack);
        for (final PotionEffect effect : effects) {
            if (Potion.field_76425_a[effect.func_76456_a()].func_76398_f()) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isGoodFood(final ItemStack stack) {
        final ItemFood food = (ItemFood)stack.func_77973_b();
        return food instanceof ItemAppleGold || (food.func_150905_g(stack) >= 4 && food.func_150906_h(stack) >= 0.3f);
    }
    
    public static boolean isBestSword(final ItemStack itemStack) {
        double damage = 0.0;
        ItemStack bestStack = null;
        for (int i = 9; i < 45; ++i) {
            final ItemStack stack = getStackInSlot(i);
            if (stack != null && stack.func_77973_b() instanceof ItemSword) {
                final double newDamage = getItemDamage(stack);
                if (newDamage > damage) {
                    damage = newDamage;
                    bestStack = stack;
                }
            }
        }
        return bestStack == itemStack || getItemDamage(itemStack) >= damage;
    }
    
    public static double getItemDamage(final ItemStack stack) {
        double damage = 0.0;
        final Multimap<String, AttributeModifier> attributeModifierMap = (Multimap<String, AttributeModifier>)stack.func_111283_C();
        for (final String attributeName : attributeModifierMap.keySet()) {
            if (attributeName.equals("generic.attackDamage")) {
                final Iterator<AttributeModifier> attributeModifiers = attributeModifierMap.get((Object)attributeName).iterator();
                if (attributeModifiers.hasNext()) {
                    damage += attributeModifiers.next().func_111164_d();
                    break;
                }
                break;
            }
        }
        if (stack.func_77948_v()) {
            damage += EnchantmentHelper.func_77506_a(Enchantment.field_77334_n.field_77352_x, stack);
            damage += EnchantmentHelper.func_77506_a(Enchantment.field_180314_l.field_77352_x, stack) * 1.25;
        }
        return damage;
    }
    
    public static boolean isBestArmor(final ItemStack itemStack) {
        final ItemArmor itemArmor = (ItemArmor)itemStack.func_77973_b();
        double reduction = 0.0;
        ItemStack bestStack = null;
        for (int i = 5; i < 45; ++i) {
            final ItemStack stack = getStackInSlot(i);
            if (stack != null && stack.func_77973_b() instanceof ItemArmor) {
                final ItemArmor stackArmor = (ItemArmor)stack.func_77973_b();
                if (stackArmor.field_77881_a == itemArmor.field_77881_a) {
                    final double newReduction = getDamageReduction(stack);
                    if (newReduction > reduction) {
                        reduction = newReduction;
                        bestStack = stack;
                    }
                }
            }
        }
        return bestStack == itemStack || getDamageReduction(itemStack) > reduction;
    }
    
    public static boolean isGoodBlockStack(final ItemStack stack) {
        return stack.field_77994_a >= 1 && isValidBlock(Block.func_149634_a(stack.func_77973_b()), true);
    }
    
    public static boolean isValidBlock(final Block block, final boolean toPlace) {
        if (block instanceof BlockContainer) {
            return false;
        }
        if (toPlace) {
            return !(block instanceof BlockFalling) && block.func_149730_j() && block.func_149686_d();
        }
        final Material material = block.func_149688_o();
        return !material.func_76222_j() && !material.func_76224_d();
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
    
    public static boolean isBestTool(final ItemStack itemStack) {
        final int type = getToolType(itemStack);
        Tool bestTool = new Tool(-1, -1.0, null);
        for (int i = 9; i < 45; ++i) {
            final ItemStack stack = getStackInSlot(i);
            if (stack != null && stack.func_77973_b() instanceof ItemTool && type == getToolType(stack)) {
                final double efficiency = getToolEfficiency(stack);
                if (efficiency > bestTool.getEfficiency()) {
                    bestTool = new Tool(i, efficiency, stack);
                }
            }
        }
        return bestTool.getStack() == itemStack || getToolEfficiency(itemStack) > bestTool.getEfficiency();
    }
    
    public static float getToolEfficiency(final ItemStack itemStack) {
        final ItemTool tool = (ItemTool)itemStack.func_77973_b();
        float efficiency = ((IItemTool)tool).getEfficiencyOnProperMaterial();
        final int lvl = EnchantmentHelper.func_77506_a(Enchantment.field_77349_p.field_77352_x, itemStack);
        if (efficiency > 1.0f && lvl > 0) {
            efficiency += lvl * lvl + 1;
        }
        return efficiency;
    }
    
    public static int getToolType(final ItemStack stack) {
        final ItemTool tool = (ItemTool)stack.func_77973_b();
        if (tool instanceof ItemPickaxe) {
            return 0;
        }
        if (tool instanceof ItemAxe) {
            return 1;
        }
        if (tool instanceof ItemSpade) {
            return 2;
        }
        return -1;
    }
    
    public static void windowClick(final int slotId, final int mouseButtonClicked, final ClickType mode) {
        InventoryUtils.mc.field_71442_b.func_78753_a(InventoryUtils.mc.field_71439_g.field_71069_bz.field_75152_c, slotId, mouseButtonClicked, mode.ordinal(), (EntityPlayer)InventoryUtils.mc.field_71439_g);
    }
    
    static {
        mc = Minecraft.func_71410_x();
    }
    
    private static class Tool
    {
        private final int slot;
        private final double efficiency;
        private final ItemStack stack;
        
        public Tool(final int slot, final double efficiency, final ItemStack stack) {
            this.slot = slot;
            this.efficiency = efficiency;
            this.stack = stack;
        }
        
        public int getSlot() {
            return this.slot;
        }
        
        public double getEfficiency() {
            return this.efficiency;
        }
        
        public ItemStack getStack() {
            return this.stack;
        }
    }
    
    public enum ClickType
    {
        CLICK, 
        SHIFT_CLICK, 
        SWAP_WITH_HOT_BAR_SLOT, 
        PLACEHOLDER, 
        DROP_ITEM;
    }
}
