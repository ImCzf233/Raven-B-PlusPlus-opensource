package keystrokesmod.client.module.modules.combat;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import java.lang.reflect.*;
import keystrokesmod.client.module.setting.*;
import net.minecraft.client.*;
import net.minecraftforge.fml.relauncher.*;
import keystrokesmod.client.event.impl.*;
import keystrokesmod.client.utils.*;
import com.google.common.eventbus.*;

public class DelayRemover extends Module
{
    public static DescriptionSetting desc;
    private final Field leftClickCounterField;
    
    public DelayRemover() {
        super("Delay Remover", ModuleCategory.combat);
        this.withEnabled(true);
        this.registerSetting(DelayRemover.desc = new DescriptionSetting("Gives you 1.7 hitreg."));
        this.leftClickCounterField = ReflectionHelper.findField((Class)Minecraft.class, new String[] { "field_71429_W", "leftClickCounter" });
        if (this.leftClickCounterField != null) {
            this.leftClickCounterField.setAccessible(true);
        }
    }
    
    @Override
    public boolean canBeEnabled() {
        return this.leftClickCounterField != null;
    }
    
    @Subscribe
    public void onGameLoop(final GameLoopEvent event) {
        if (Utils.Player.isPlayerInGame() && this.leftClickCounterField != null) {
            if (!DelayRemover.mc.field_71415_G || DelayRemover.mc.field_71439_g.field_71075_bZ.field_75098_d) {
                return;
            }
            try {
                this.leftClickCounterField.set(DelayRemover.mc, 0);
            }
            catch (IllegalAccessException | IndexOutOfBoundsException ex3) {
                final Exception ex2;
                final Exception ex = ex2;
                ex.printStackTrace();
                this.disable();
            }
        }
    }
}
