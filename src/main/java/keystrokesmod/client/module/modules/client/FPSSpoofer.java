package keystrokesmod.client.module.modules.client;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import java.lang.reflect.*;
import keystrokesmod.client.module.setting.*;
import net.minecraft.client.*;
import net.minecraftforge.fml.relauncher.*;
import keystrokesmod.client.event.impl.*;
import java.util.concurrent.*;
import keystrokesmod.client.utils.*;
import com.google.common.eventbus.*;

public class FPSSpoofer extends Module
{
    public static DescriptionSetting desc;
    public static DoubleSliderSetting fps;
    public int ticksPassed;
    private final Field fpsField;
    
    public FPSSpoofer() {
        super("FPSSpoof", ModuleCategory.other);
        this.registerSetting(FPSSpoofer.desc = new DescriptionSetting("Spoofs your fps"));
        this.registerSetting(FPSSpoofer.fps = new DoubleSliderSetting("FPS", 99860.0, 100000.0, 0.0, 100000.0, 100.0));
        (this.fpsField = ReflectionHelper.findField((Class)Minecraft.class, new String[] { "field_71420_M", "fpsCounter" })).setAccessible(true);
    }
    
    @Override
    public boolean canBeEnabled() {
        return this.fpsField != null;
    }
    
    @Override
    public void onEnable() {
        this.ticksPassed = 0;
    }
    
    @Subscribe
    public void onGameLoop(final GameLoopEvent e) {
        try {
            final int fpsN = ThreadLocalRandom.current().nextInt((int)FPSSpoofer.fps.getInputMin(), (int)FPSSpoofer.fps.getInputMax() + 1);
            this.fpsField.set(FPSSpoofer.mc, fpsN);
        }
        catch (IllegalAccessException ex) {
            ex.printStackTrace();
            Utils.Java.throwException(new RuntimeException("Could not access FPS field, THIS SHOULD NOT HAPPEN"));
            this.disable();
        }
    }
}
