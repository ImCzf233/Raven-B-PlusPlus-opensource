package keystrokesmod.client.mixin.mixins;

import org.spongepowered.asm.mixin.*;
import net.minecraft.network.play.client.*;
import org.spongepowered.asm.mixin.gen.*;

@Mixin({ C0DPacketCloseWindow.class })
public interface IC0DPacketCloseWindow
{
    @Accessor
    int getWindowId();
}
