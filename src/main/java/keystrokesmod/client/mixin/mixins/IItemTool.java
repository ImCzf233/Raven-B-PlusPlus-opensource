package keystrokesmod.client.mixin.mixins;

import org.spongepowered.asm.mixin.*;
import net.minecraft.item.*;
import org.spongepowered.asm.mixin.gen.*;

@Mixin({ ItemTool.class })
public interface IItemTool
{
    @Accessor
    float getEfficiencyOnProperMaterial();
}
