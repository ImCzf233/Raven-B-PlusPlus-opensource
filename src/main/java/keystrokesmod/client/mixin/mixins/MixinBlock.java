package keystrokesmod.client.mixin.mixins;

import org.spongepowered.asm.mixin.*;
import net.minecraft.block.*;
import net.minecraft.world.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import keystrokesmod.client.module.modules.render.*;
import org.spongepowered.asm.mixin.injection.*;
import net.minecraft.util.*;

@Mixin(value = { Block.class }, priority = 1005)
public class MixinBlock
{
    @Inject(method = { "shouldSideBeRendered" }, at = { @At("HEAD") }, cancellable = true)
    public void shouldSideBeRendered(final IBlockAccess p_shouldSideBeRendered_1_, final BlockPos p_shouldSideBeRendered_2_, final EnumFacing p_shouldSideBeRendered_3_, final CallbackInfoReturnable<Boolean> cir) {
        if (XRay.instance.isEnabled() && XRay.hypixel.isToggled()) {
            cir.setReturnValue(XRay.isOreBlock((Block)this));
        }
    }
    
    @Inject(method = { "getBlockLayer" }, at = { @At("HEAD") }, cancellable = true)
    public void getBlockLayer(final CallbackInfoReturnable<EnumWorldBlockLayer> cir) {
        if (XRay.instance.isEnabled()) {
            cir.setReturnValue(XRay.isOreBlock((Block)this) ? EnumWorldBlockLayer.SOLID : EnumWorldBlockLayer.TRANSLUCENT);
        }
    }
    
    @Inject(method = { "getAmbientOcclusionLightValue" }, at = { @At("HEAD") }, cancellable = true)
    public void getAmbientOcclusionLightValue(final CallbackInfoReturnable<Float> cir) {
        if (XRay.instance.isEnabled()) {
            cir.setReturnValue(1.0f);
        }
    }
}
