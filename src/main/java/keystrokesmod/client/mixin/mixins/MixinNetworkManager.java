package keystrokesmod.client.mixin.mixins;

import org.spongepowered.asm.mixin.*;
import net.minecraft.network.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import keystrokesmod.client.utils.*;
import keystrokesmod.client.event.*;
import keystrokesmod.client.event.impl.*;
import keystrokesmod.client.main.*;
import org.spongepowered.asm.mixin.injection.*;
import io.netty.channel.*;

@Mixin(priority = 995, value = { NetworkManager.class })
public class MixinNetworkManager
{
    @Inject(method = { "sendPacket(Lnet/minecraft/network/Packet;)V" }, at = { @At("HEAD") }, cancellable = true)
    public void sendPacket(Packet p_sendPacket_1_, final CallbackInfo ci) {
        if (!PacketUtils.silentPackets.contains(p_sendPacket_1_)) {
            final PacketEvent e = new PacketEvent((Packet<?>)p_sendPacket_1_, EventDirection.OUTGOING);
            Raven.eventBus.post((Object)e);
            p_sendPacket_1_ = e.getPacket();
            if (e.isCancelled()) {
                ci.cancel();
            }
        }
    }
    
    @Inject(method = { "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/Packet;)V" }, at = { @At("HEAD") }, cancellable = true)
    public void receivePacket(final ChannelHandlerContext p_channelRead0_1_, Packet p_channelRead0_2_, final CallbackInfo ci) {
        final PacketEvent e = new PacketEvent((Packet<?>)p_channelRead0_2_, EventDirection.INCOMING);
        Raven.eventBus.post((Object)e);
        p_channelRead0_2_ = e.getPacket();
        if (e.isCancelled()) {
            ci.cancel();
        }
    }
}
