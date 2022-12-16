package keystrokesmod.client.mixin.mixins;

import org.spongepowered.asm.mixin.*;
import net.minecraft.client.gui.*;
import net.minecraft.util.*;
import keystrokesmod.client.main.*;
import keystrokesmod.client.module.modules.other.*;
import keystrokesmod.client.module.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(priority = 995, value = { GuiUtilRenderComponents.class })
public class MixinGuiUtilRenderComponents
{
    @Redirect(method = { "func_178908_a" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/IChatComponent;getUnformattedTextForChat()Ljava/lang/String;", ordinal = 0))
    private static String mixin(final IChatComponent instance) {
        final Module nameHider = Raven.moduleManager.getModuleByClazz(NameHider.class);
        if (nameHider != null && nameHider.isEnabled()) {
            return NameHider.format(instance.func_150261_e());
        }
        return instance.func_150261_e();
    }
}
