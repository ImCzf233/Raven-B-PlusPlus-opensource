package keystrokesmod.client.mixin.mixins;

import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import net.minecraft.client.gui.*;
import keystrokesmod.client.module.modules.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(priority = 1005, value = { GuiScreen.class })
public class MixinGuiScreen
{
    @Inject(method = { "mouseReleased" }, at = { @At("RETURN") })
    public void kys(final int p_mouseReleased_1_, final int p_mouseReleased_2_, final int p_mouseReleased_3_, final CallbackInfo ci) {
        if (((GuiScreen)this) instanceof GuiChat && p_mouseReleased_3_ == 0) {
            HUD.getInstance().endDrag(p_mouseReleased_1_, p_mouseReleased_2_);
        }
    }
}
