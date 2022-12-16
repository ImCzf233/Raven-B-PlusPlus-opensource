package keystrokesmod.client.event.forge;

import net.minecraftforge.fml.common.gameevent.*;
import keystrokesmod.client.utils.*;
import keystrokesmod.client.main.*;
import keystrokesmod.client.module.*;
import keystrokesmod.client.clickgui.raven.*;
import net.minecraft.client.*;
import java.util.*;
import keystrokesmod.client.event.impl.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.*;
import net.minecraftforge.client.event.*;

public class ForgeEventListener
{
    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent e) {
        if (e.phase == TickEvent.Phase.END && Utils.Player.isPlayerInGame()) {
            for (final Module module : Raven.moduleManager.getModules()) {
                if (Minecraft.func_71410_x().field_71462_r instanceof ClickGui) {
                    module.guiUpdate();
                }
            }
        }
    }
    
    @SubscribeEvent
    public void onRenderTick(final TickEvent.RenderTickEvent e) {
        if (e.phase == TickEvent.Phase.END && Utils.Player.isPlayerInGame()) {
            for (final Module module : Raven.moduleManager.getModules()) {
                if (Minecraft.func_71410_x().field_71462_r == null) {
                    module.keybind();
                }
            }
        }
        Raven.eventBus.post((Object)new ForgeEvent((Event)e));
    }
    
    @SubscribeEvent
    public void onHit(final AttackEntityEvent e) {
        Raven.eventBus.post((Object)new ForgeEvent((Event)e));
    }
    
    @SubscribeEvent
    public void onMouseUpdate(final MouseEvent e) {
        Raven.eventBus.post((Object)new ForgeEvent((Event)e));
    }
    
    @SubscribeEvent
    public void onRenderWorldLast(final RenderWorldLastEvent e) {
        Raven.eventBus.post((Object)new ForgeEvent((Event)e));
    }
    
    @SubscribeEvent
    public void onLivingUpdate(final LivingEvent.LivingUpdateEvent e) {
        Raven.eventBus.post((Object)new ForgeEvent((Event)e));
    }
    
    @SubscribeEvent
    public void onEntityJoinWorld(final EntityJoinWorldEvent e) {
        Raven.eventBus.post((Object)new ForgeEvent((Event)e));
    }
    
    @SubscribeEvent
    public void onClientChatReceived(final ClientChatReceivedEvent e) {
        Raven.eventBus.post((Object)new ForgeEvent((Event)e));
    }
    
    @SubscribeEvent
    public void onRenderPlayerPost(final RenderPlayerEvent.Post e) {
        Raven.eventBus.post((Object)new ForgeEvent((Event)e));
    }
    
    @SubscribeEvent
    public void onRenderPlayerPre(final RenderPlayerEvent.Pre e) {
        Raven.eventBus.post((Object)new ForgeEvent((Event)e));
    }
    
    @SubscribeEvent
    public void onRenderLivingSpecialsPre(final RenderLivingEvent.Specials.Pre e) {
        Raven.eventBus.post((Object)new ForgeEvent((Event)e));
    }
    
    @SubscribeEvent
    public void onRenderPlayerEventPre(final RenderLivingEvent e) {
        Raven.eventBus.post((Object)new ForgeEvent((Event)e));
    }
    
    @SubscribeEvent
    public void onDrawBlockHighlight(final DrawBlockHighlightEvent e) {
        Raven.eventBus.post((Object)new ForgeEvent((Event)e));
    }
}
