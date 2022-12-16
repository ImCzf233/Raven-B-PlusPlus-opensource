package keystrokesmod.client.mixin.mixins;

import org.spongepowered.asm.mixin.*;
import net.minecraft.block.*;
import net.minecraft.block.material.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import net.minecraft.util.*;
import keystrokesmod.client.module.modules.render.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(priority = 1005, value = { BlockGrass.class })
public class MixinBlockGrass extends Block
{
    public MixinBlockGrass(final Material p_i46399_1_, final MapColor p_i46399_2_) {
        super(p_i46399_1_, p_i46399_2_);
    }
    
    @Inject(method = { "getBlockLayer" }, at = { @At("HEAD") }, cancellable = true)
    public void getBlockLayer(final CallbackInfoReturnable<EnumWorldBlockLayer> cir) {
        if (XRay.instance.isEnabled()) {
            cir.setReturnValue(super.func_180664_k());
        }
    }
}
