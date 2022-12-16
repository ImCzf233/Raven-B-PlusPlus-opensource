package keystrokesmod.client.mixin.mixins;

import org.spongepowered.asm.mixin.*;
import net.minecraft.item.*;
import net.minecraft.block.*;
import org.spongepowered.asm.mixin.gen.*;

@Mixin({ ItemBucket.class })
public interface IItemBucket
{
    @Accessor
    Block getIsFull();
}
