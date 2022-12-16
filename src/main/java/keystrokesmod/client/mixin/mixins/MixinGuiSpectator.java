package keystrokesmod.client.mixin.mixins;

import org.spongepowered.asm.mixin.*;
import net.minecraft.client.gui.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import keystrokesmod.client.main.*;
import keystrokesmod.client.event.impl.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(value = { GuiSpectator.class }, priority = 1005)
public class MixinGuiSpectator
{
    @Inject(method = { "renderTooltip" }, at = { @At("HEAD") })
    public void onRender(final ScaledResolution p_renderTooltip_1_, final float p_renderTooltip_2_, final CallbackInfo ci) {
        Raven.eventBus.post((Object)new Render2DEvent());
    }
}
