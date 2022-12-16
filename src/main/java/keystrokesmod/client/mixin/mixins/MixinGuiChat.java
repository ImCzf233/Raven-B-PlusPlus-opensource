package keystrokesmod.client.mixin.mixins;

import org.spongepowered.asm.mixin.*;
import net.minecraft.client.gui.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import keystrokesmod.client.module.modules.*;
import org.spongepowered.asm.mixin.injection.*;
import net.minecraft.client.renderer.*;
import keystrokesmod.client.hud.*;

@Mixin(priority = 1005, value = { GuiChat.class })
public class MixinGuiChat
{
    @Inject(method = { "mouseClicked" }, at = { @At("HEAD") })
    public void mouseClicked(final int p_mouseClicked_1_, final int p_mouseClicked_2_, final int p_mouseClicked_3_, final CallbackInfo ci) {
        HUD.getInstance().handleClick(p_mouseClicked_1_, p_mouseClicked_2_, p_mouseClicked_3_);
    }
    
    @Inject(method = { "drawScreen" }, at = { @At("RETURN") })
    public void kms(final int p_drawScreen_1_, final int p_drawScreen_2_, final float p_drawScreen_3_, final CallbackInfo ci) {
        HUD.getInstance().updateDrag(p_drawScreen_1_, p_drawScreen_2_);
        HUD.getInstance().comps.forEach((compC, comp) -> {
            GlStateManager.func_179117_G();
            comp.draw(true);
        });
    }
}
