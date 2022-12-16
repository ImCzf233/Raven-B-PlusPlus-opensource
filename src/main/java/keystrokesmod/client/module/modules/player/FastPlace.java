package keystrokesmod.client.module.modules.player;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import java.lang.reflect.*;
import keystrokesmod.client.module.setting.*;
import keystrokesmod.client.event.impl.*;
import keystrokesmod.client.utils.*;
import net.minecraft.item.*;
import com.google.common.eventbus.*;
import net.minecraft.client.*;
import net.minecraftforge.fml.relauncher.*;

public class FastPlace extends Module
{
    public static SliderSetting delaySlider;
    public static SliderSetting projSlider;
    public static TickSetting blockOnly;
    public static TickSetting projSeparate;
    public static final Field rightClickDelayTimerField;
    
    public FastPlace() {
        super("FastPlace", ModuleCategory.player);
        this.registerSetting(FastPlace.delaySlider = new SliderSetting("Delay", 0.0, 0.0, 4.0, 1.0));
        this.registerSetting(FastPlace.blockOnly = new TickSetting("Blocks only", true));
        this.registerSetting(FastPlace.projSeparate = new TickSetting("Separate Projectile Delay", true));
        this.registerSetting(FastPlace.projSlider = new SliderSetting("Projectile Delay", 2.0, 0.0, 4.0, 1.0));
    }
    
    @Override
    public boolean canBeEnabled() {
        return FastPlace.rightClickDelayTimerField != null;
    }
    
    @Subscribe
    public void onTick(final TickEvent event) {
        if (Utils.Player.isPlayerInGame() && FastPlace.mc.field_71415_G && FastPlace.rightClickDelayTimerField != null) {
            if (FastPlace.blockOnly.isToggled()) {
                final ItemStack item = FastPlace.mc.field_71439_g.func_70694_bm();
                if (item != null && item.func_77973_b() instanceof ItemBlock) {
                    try {
                        final int c = (int)FastPlace.delaySlider.getInput();
                        if (c == 0) {
                            FastPlace.rightClickDelayTimerField.set(FastPlace.mc, 0);
                        }
                        else {
                            if (c == 4) {
                                return;
                            }
                            final int d = FastPlace.rightClickDelayTimerField.getInt(FastPlace.mc);
                            if (d == 4) {
                                FastPlace.rightClickDelayTimerField.set(FastPlace.mc, c);
                            }
                        }
                    }
                    catch (IllegalAccessException | IndexOutOfBoundsException ex) {}
                }
                else if (item != null && (item.func_77973_b() instanceof ItemSnowball || item.func_77973_b() instanceof ItemEgg) && FastPlace.projSeparate.isToggled()) {
                    try {
                        final int c = (int)FastPlace.projSlider.getInput();
                        if (c == 0) {
                            FastPlace.rightClickDelayTimerField.set(FastPlace.mc, 0);
                        }
                        else {
                            if (c == 4) {
                                return;
                            }
                            final int d = FastPlace.rightClickDelayTimerField.getInt(FastPlace.mc);
                            if (d == 4) {
                                FastPlace.rightClickDelayTimerField.set(FastPlace.mc, c);
                            }
                        }
                    }
                    catch (IllegalAccessException ex2) {}
                    catch (IndexOutOfBoundsException ex3) {}
                }
            }
            else {
                try {
                    final int c2 = (int)FastPlace.delaySlider.getInput();
                    if (c2 == 0) {
                        FastPlace.rightClickDelayTimerField.set(FastPlace.mc, 0);
                    }
                    else {
                        if (c2 == 4) {
                            return;
                        }
                        final int d2 = FastPlace.rightClickDelayTimerField.getInt(FastPlace.mc);
                        if (d2 == 4) {
                            FastPlace.rightClickDelayTimerField.set(FastPlace.mc, c2);
                        }
                    }
                }
                catch (IllegalAccessException ex4) {}
                catch (IndexOutOfBoundsException ex5) {}
            }
        }
    }
    
    static {
        rightClickDelayTimerField = ReflectionHelper.findField((Class)Minecraft.class, new String[] { "field_71467_ac", "rightClickDelayTimer" });
        if (FastPlace.rightClickDelayTimerField != null) {
            FastPlace.rightClickDelayTimerField.setAccessible(true);
        }
    }
}
