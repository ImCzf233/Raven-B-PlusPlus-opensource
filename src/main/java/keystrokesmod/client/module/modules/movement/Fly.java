package keystrokesmod.client.module.modules.movement;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import keystrokesmod.client.module.setting.*;
import keystrokesmod.client.event.impl.*;
import com.google.common.eventbus.*;
import net.minecraft.client.*;

public class Fly extends Module
{
    private final VanFly vanFly;
    private final GliFly gliFly;
    public static DescriptionSetting dc;
    public static SliderSetting a;
    public static SliderSetting b;
    private static final String c1 = "Vanilla";
    private static final String c2 = "Glide";
    
    public Fly() {
        super("Fly", ModuleCategory.movement);
        this.vanFly = new VanFly();
        this.gliFly = new GliFly();
        this.registerSetting(Fly.a = new SliderSetting("Value", 1.0, 1.0, 2.0, 1.0));
        this.registerSetting(Fly.dc = new DescriptionSetting("Mode: Vanilla"));
        this.registerSetting(Fly.b = new SliderSetting("Speed", 2.0, 1.0, 5.0, 0.1));
    }
    
    @Override
    public void onEnable() {
        switch ((int)Fly.a.getInput()) {
            case 1: {
                this.vanFly.onEnable();
                break;
            }
            case 2: {
                this.gliFly.onEnable();
                break;
            }
        }
    }
    
    @Override
    public void onDisable() {
        switch ((int)Fly.a.getInput()) {
            case 1: {
                this.vanFly.onDisable();
                break;
            }
            case 2: {
                this.gliFly.onDisable();
                break;
            }
        }
    }
    
    @Subscribe
    public void onTick(final TickEvent e) {
        switch ((int)Fly.a.getInput()) {
            case 1: {
                this.vanFly.update();
                break;
            }
            case 2: {
                this.gliFly.update();
                break;
            }
        }
    }
    
    @Override
    public void guiUpdate() {
        switch ((int)Fly.a.getInput()) {
            case 1: {
                Fly.dc.setDesc("Mode: Vanilla");
                break;
            }
            case 2: {
                Fly.dc.setDesc("Mode: Glide");
                break;
            }
        }
    }
    
    class GliFly
    {
        boolean opf;
        
        public void onEnable() {
        }
        
        public void onDisable() {
            this.opf = false;
        }
        
        public void update() {
            if (Fly.mc.field_71439_g.field_71158_b.field_78900_b > 0.0f) {
                if (!this.opf) {
                    this.opf = true;
                    if (Fly.mc.field_71439_g.field_70122_E) {
                        Fly.mc.field_71439_g.func_70664_aZ();
                    }
                }
                else {
                    if (Fly.mc.field_71439_g.field_70122_E || Fly.mc.field_71439_g.field_70123_F) {
                        Fly.this.disable();
                        return;
                    }
                    final double s = 1.94 * Fly.b.getInput();
                    final double r = Math.toRadians(Fly.mc.field_71439_g.field_70177_z + 90.0f);
                    Fly.mc.field_71439_g.field_70159_w = s * Math.cos(r);
                    Fly.mc.field_71439_g.field_70179_y = s * Math.sin(r);
                }
            }
        }
    }
    
    static class VanFly
    {
        private final float dfs = 0.05f;
        
        public void onEnable() {
        }
        
        public void onDisable() {
            if (Minecraft.func_71410_x().field_71439_g == null) {
                return;
            }
            if (Minecraft.func_71410_x().field_71439_g.field_71075_bZ.field_75100_b) {
                Minecraft.func_71410_x().field_71439_g.field_71075_bZ.field_75100_b = false;
            }
            Minecraft.func_71410_x().field_71439_g.field_71075_bZ.func_75092_a(0.05f);
        }
        
        public void update() {
            Fly.mc.field_71439_g.field_70181_x = 0.0;
            Fly.mc.field_71439_g.field_71075_bZ.func_75092_a((float)(0.05000000074505806 * Fly.b.getInput()));
            Fly.mc.field_71439_g.field_71075_bZ.field_75100_b = true;
        }
    }
}
