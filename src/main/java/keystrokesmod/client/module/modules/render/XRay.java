package keystrokesmod.client.module.modules.render;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import keystrokesmod.client.module.setting.*;
import net.minecraft.block.*;

public class XRay extends Module
{
    public static XRay instance;
    public static TickSetting hypixel;
    public static SliderSetting opacity;
    
    public XRay() {
        super("XRay", ModuleCategory.render);
        this.registerSettings(XRay.opacity = new SliderSetting("Opacity", 120.0, 0.0, 255.0, 1.0), XRay.hypixel = new TickSetting("Hypixel", true));
        XRay.instance = this;
    }
    
    @Override
    public void onEnable() {
        XRay.mc.field_71438_f.func_72712_a();
    }
    
    @Override
    public void onDisable() {
        XRay.mc.field_71438_f.func_72712_a();
    }
    
    public static boolean isOreBlock(final Block block) {
        return block instanceof BlockOre || block instanceof BlockRedstoneOre;
    }
}
