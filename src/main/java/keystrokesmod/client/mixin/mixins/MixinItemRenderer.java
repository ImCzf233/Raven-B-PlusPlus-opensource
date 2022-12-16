package keystrokesmod.client.mixin.mixins;

import org.spongepowered.asm.mixin.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.entity.*;
import keystrokesmod.client.module.modules.combat.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(value = { ItemRenderer.class }, priority = 1005)
public class MixinItemRenderer
{
    @Redirect(method = { "renderItemInFirstPerson" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/AbstractClientPlayer;getItemInUseCount()I"))
    private int mixin(final AbstractClientPlayer instance) {
        return Aura.isAutoBlocking() ? 1 : instance.func_71052_bv();
    }
}
