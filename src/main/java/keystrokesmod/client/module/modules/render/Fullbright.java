package keystrokesmod.client.module.modules.render;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import keystrokesmod.client.module.setting.*;
import keystrokesmod.client.event.impl.*;
import keystrokesmod.client.utils.*;
import com.google.common.eventbus.*;

public class Fullbright extends Module
{
    private float defaultGamma;
    private final float clientGamma;
    
    public Fullbright() {
        super("Fullbright", ModuleCategory.render);
        final DescriptionSetting description;
        this.registerSetting(description = new DescriptionSetting("No more darkness!"));
        this.clientGamma = 10000.0f;
    }
    
    @Override
    public void onEnable() {
        this.defaultGamma = Fullbright.mc.field_71474_y.field_74333_Y;
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        super.onEnable();
        Fullbright.mc.field_71474_y.field_74333_Y = this.defaultGamma;
    }
    
    @Subscribe
    public void onTick(final TickEvent e) {
        if (!Utils.Player.isPlayerInGame()) {
            this.onDisable();
            return;
        }
        if (Fullbright.mc.field_71474_y.field_74333_Y != this.clientGamma) {
            Fullbright.mc.field_71474_y.field_74333_Y = this.clientGamma;
        }
    }
}
