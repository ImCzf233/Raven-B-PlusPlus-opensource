package keystrokesmod.client.mixin.mixins;

import org.spongepowered.asm.mixin.*;
import net.minecraft.client.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import keystrokesmod.client.main.*;
import keystrokesmod.client.event.impl.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(priority = 1005, value = { Minecraft.class })
public class MixinMinecraft
{
    @Inject(method = { "runTick" }, at = { @At("HEAD") })
    public void onTick(final CallbackInfo ci) {
        Raven.eventBus.post((Object)new GameLoopEvent());
    }
}
