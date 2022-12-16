package keystrokesmod.client.module.modules.player;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import keystrokesmod.client.module.setting.*;
import keystrokesmod.client.event.impl.*;
import com.google.common.eventbus.*;

public class NoFall extends Module
{
    public static DescriptionSetting warning;
    public static ComboSetting mode;
    int ticks;
    double dist;
    boolean spoofing;
    
    public NoFall() {
        super("NoFall", ModuleCategory.player);
        this.registerSetting(NoFall.warning = new DescriptionSetting("HypixelSpoof silent flags."));
        this.registerSetting(NoFall.mode = new ComboSetting("Mode", (T)Mode.Spoof));
    }
    
    @Subscribe
    public void onTick(final TickEvent e) {
        switch (NoFall.mode.getMode()) {
            case Spoof: {
                if (NoFall.mc.field_71439_g.field_70143_R > 2.5) {
                    NoFall.mc.field_71439_g.field_70122_E = true;
                    break;
                }
                break;
            }
            case HypixelSpoof: {
                if (NoFall.mc.field_71439_g.field_70122_E) {
                    this.ticks = 0;
                    this.dist = 0.0;
                    this.spoofing = false;
                    break;
                }
                if (NoFall.mc.field_71439_g.field_70143_R <= 2.0f) {
                    break;
                }
                if (this.spoofing) {
                    ++this.ticks;
                    NoFall.mc.field_71439_g.field_70122_E = true;
                    if (this.ticks >= 2) {
                        this.spoofing = false;
                        this.ticks = 0;
                        this.dist = NoFall.mc.field_71439_g.field_70143_R;
                        break;
                    }
                    break;
                }
                else {
                    if (NoFall.mc.field_71439_g.field_70143_R - this.dist > 2.0) {
                        this.spoofing = true;
                        break;
                    }
                    break;
                }
                break;
            }
            case Verus: {
                if (NoFall.mc.field_71439_g.field_70122_E) {
                    this.dist = 0.0;
                    this.spoofing = false;
                    break;
                }
                if (NoFall.mc.field_71439_g.field_70143_R <= 2.0f) {
                    break;
                }
                if (this.spoofing) {
                    NoFall.mc.field_71439_g.field_70122_E = true;
                    NoFall.mc.field_71439_g.field_70181_x = 0.0;
                    this.spoofing = false;
                    this.dist = NoFall.mc.field_71439_g.field_70143_R;
                    break;
                }
                if (NoFall.mc.field_71439_g.field_70143_R - this.dist > 2.0) {
                    this.spoofing = true;
                    break;
                }
                break;
            }
        }
    }
    
    public enum Mode
    {
        Spoof, 
        HypixelSpoof, 
        Verus;
    }
}
