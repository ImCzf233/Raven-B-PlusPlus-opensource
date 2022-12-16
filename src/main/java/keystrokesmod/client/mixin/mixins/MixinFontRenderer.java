package keystrokesmod.client.mixin.mixins;

import org.spongepowered.asm.mixin.*;
import net.minecraft.client.gui.*;
import keystrokesmod.client.main.*;
import keystrokesmod.client.module.modules.other.*;
import keystrokesmod.client.module.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(priority = 1005, value = { FontRenderer.class })
public class MixinFontRenderer
{
    @Inject(method = { "renderStringAtPos" }, at = { @At("HEAD") })
    private void renderStringAtPos(String p_renderStringAtPos_1_, final boolean p_renderStringAtPos_2_, final CallbackInfo ci) {
        final Module nameHider = Raven.moduleManager.getModuleByClazz(NameHider.class);
        if (nameHider != null && nameHider.isEnabled()) {
            p_renderStringAtPos_1_ = NameHider.format(p_renderStringAtPos_1_);
        }
    }
    
    @Inject(method = { "getStringWidth" }, at = { @At("HEAD") })
    private void getStringWidth(String p_getStringWidth_1_, final CallbackInfoReturnable<Integer> cir) {
        final Module nameHider = Raven.moduleManager.getModuleByClazz(NameHider.class);
        if (nameHider != null && nameHider.isEnabled()) {
            p_getStringWidth_1_ = NameHider.format(p_getStringWidth_1_);
        }
    }
}
