package keystrokesmod.client.mixin.mixins;

import net.minecraft.world.*;
import net.minecraft.client.resources.model.*;
import net.minecraft.block.*;
import net.minecraft.client.renderer.*;
import net.minecraft.block.state.*;
import net.minecraft.client.*;
import keystrokesmod.client.module.modules.render.*;
import net.minecraft.crash.*;
import net.minecraft.util.*;
import org.spongepowered.asm.mixin.*;

@Mixin(value = { BlockModelRenderer.class }, priority = 999)
public abstract class MixinBlockModelRenderer
{
    @Shadow
    public abstract boolean func_178265_a(final IBlockAccess p0, final IBakedModel p1, final Block p2, final BlockPos p3, final WorldRenderer p4, final boolean p5);
    
    @Shadow
    public abstract boolean func_178258_b(final IBlockAccess p0, final IBakedModel p1, final Block p2, final BlockPos p3, final WorldRenderer p4, final boolean p5);
    
    @Overwrite
    public boolean func_178267_a(final IBlockAccess p_renderModel_1_, final IBakedModel p_renderModel_2_, final IBlockState p_renderModel_3_, final BlockPos p_renderModel_4_, final WorldRenderer p_renderModel_5_, final boolean p_renderModel_6_) {
        final boolean flag = (Minecraft.func_71379_u() && p_renderModel_3_.func_177230_c().func_149750_m() == 0 && p_renderModel_2_.func_177555_b()) || XRay.instance.isEnabled();
        try {
            final Block block = p_renderModel_3_.func_177230_c();
            return flag ? this.func_178265_a(p_renderModel_1_, p_renderModel_2_, block, p_renderModel_4_, p_renderModel_5_, p_renderModel_6_) : this.func_178258_b(p_renderModel_1_, p_renderModel_2_, block, p_renderModel_4_, p_renderModel_5_, p_renderModel_6_);
        }
        catch (Throwable var11) {
            final CrashReport crashreport = CrashReport.func_85055_a(var11, "Tesselating block model");
            final CrashReportCategory crashreportcategory = crashreport.func_85058_a("Block model being tesselated");
            CrashReportCategory.func_175750_a(crashreportcategory, p_renderModel_4_, p_renderModel_3_);
            crashreportcategory.func_71507_a("Using AO", (Object)flag);
            throw new ReportedException(crashreport);
        }
    }
}
