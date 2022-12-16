package keystrokesmod.client.mixin.mixins;

import net.minecraft.client.renderer.*;
import java.nio.*;
import keystrokesmod.client.module.modules.render.*;
import org.spongepowered.asm.mixin.*;
import net.minecraft.util.*;

@Mixin(value = { WorldRenderer.class }, priority = 999)
public abstract class MixinWorldRenderer
{
    @Shadow
    private boolean field_78939_q;
    @Shadow
    private IntBuffer field_178999_b;
    
    @Shadow
    public abstract int func_78909_a(final int p0);
    
    @Overwrite
    public void func_178978_a(final float p_putColorMultiplier_1_, final float p_putColorMultiplier_2_, final float p_putColorMultiplier_3_, final int p_putColorMultiplier_4_) {
        final int i = this.func_78909_a(p_putColorMultiplier_4_);
        int j = -1;
        if (!this.field_78939_q) {
            j = this.field_178999_b.get(i);
            int k;
            int l;
            int i2;
            if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
                k = (int)((j & 0xFF) * p_putColorMultiplier_1_);
                l = (int)((j >> 8 & 0xFF) * p_putColorMultiplier_2_);
                i2 = (int)((j >> 16 & 0xFF) * p_putColorMultiplier_3_);
                j &= 0xFF000000;
                j = (j | i2 << 16 | l << 8 | k);
            }
            else {
                k = (int)((j >> 24 & 0xFF) * p_putColorMultiplier_1_);
                l = (int)((j >> 16 & 0xFF) * p_putColorMultiplier_2_);
                i2 = (int)((j >> 8 & 0xFF) * p_putColorMultiplier_3_);
                j &= 0xFF;
                j = (j | k << 24 | l << 16 | i2 << 8);
            }
            if (XRay.instance.isEnabled()) {
                j = this.getColor(k, l, i2, (int)XRay.opacity.getInput());
            }
        }
        this.field_178999_b.put(i, j);
    }
    
    public int getColor(final int red, final int green, final int blue, final int alpha) {
        int color = MathHelper.func_76125_a(alpha, 0, 255) << 24;
        color |= MathHelper.func_76125_a(red, 0, 255) << 16;
        color |= MathHelper.func_76125_a(green, 0, 255) << 8;
        color |= MathHelper.func_76125_a(blue, 0, 255);
        return color;
    }
}
