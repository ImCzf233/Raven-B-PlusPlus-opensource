package keystrokesmod.client.module.modules.render;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import net.minecraft.client.entity.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.*;
import net.minecraftforge.event.entity.player.*;
import net.minecraft.entity.player.*;
import com.google.common.eventbus.*;
import keystrokesmod.client.event.impl.*;

public class TargetHUD extends Module
{
    public TickSetting editPosition;
    public int height;
    public int width;
    public FontRenderer fr;
    private AbstractClientPlayer target;
    ScaledResolution sr;
    
    public TargetHUD() {
        super("Target HUD", ModuleCategory.render);
        this.sr = new ScaledResolution(Minecraft.func_71410_x());
        this.height = this.sr.func_78328_b();
        this.width = this.sr.func_78326_a();
        this.fr = TargetHUD.mc.field_71466_p;
    }
    
    @Subscribe
    public void onForgeEvent(final ForgeEvent fe) {
        if (fe.getEvent() instanceof AttackEntityEvent) {
            final AttackEntityEvent e = (AttackEntityEvent)fe.getEvent();
            System.out.println(e.target instanceof AbstractClientPlayer);
            System.out.println(e.target);
            final EntityPlayer entityPlayer = (EntityPlayer)e.target;
        }
    }
    
    @Subscribe
    public void onRender2d(final Render2DEvent e) {
    }
}
