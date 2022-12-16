package keystrokesmod.client.module.modules.other;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import java.util.*;
import net.minecraft.network.*;
import keystrokesmod.client.module.setting.*;
import net.minecraft.util.*;
import com.google.common.eventbus.*;
import keystrokesmod.client.event.impl.*;
import keystrokesmod.client.mixin.mixins.*;
import net.minecraft.network.play.client.*;
import net.minecraft.entity.*;
import keystrokesmod.client.utils.*;

public class Disabler extends Module
{
    public static DescriptionSetting warning;
    public static DescriptionSetting mmcSafeWarning1;
    public static DescriptionSetting mmcSafeWarning2;
    public static ComboSetting mode;
    public static DoubleSliderSetting mmcSafeDelay;
    public static TickSetting hypSilentInv;
    public static TickSetting hypSprintFix;
    LinkedList<Packet<?>> mmcPackets;
    boolean mmc;
    boolean idfk;
    boolean idfk2;
    
    public Disabler() {
        super("Disabler", ModuleCategory.other);
        this.mmcPackets = new LinkedList<Packet<?>>();
        this.registerSetting(Disabler.warning = new DescriptionSetting("WILL BAN DONT USE ON MAIN"));
        this.registerSetting(Disabler.mode = new ComboSetting("Mode", (T)Mode.MMCSafe));
        this.registerSetting(new DescriptionSetting("   "));
        this.registerSetting(Disabler.mmcSafeWarning1 = new DescriptionSetting(EnumChatFormatting.GRAY + "Difference between min and max"));
        this.registerSetting(Disabler.mmcSafeWarning2 = new DescriptionSetting(EnumChatFormatting.GRAY + "should be less than 5."));
        this.registerSetting(Disabler.mmcSafeDelay = new DoubleSliderSetting("MMCSafe Delay", 77.0, 80.0, 10.0, 200.0, 1.0));
        this.registerSetting(new DescriptionSetting("    "));
        this.registerSettings(new DescriptionSetting("Possible InvMove bypass."), Disabler.hypSilentInv = new TickSetting("Hypixel Silent Inv", false), new DescriptionSetting("Have this on."), Disabler.hypSprintFix = new TickSetting("Hypixel Sprint Fix", true));
        this.registerSetting(new DescriptionSetting(EnumChatFormatting.BOLD.toString() + EnumChatFormatting.AQUA + "GHOST CLIENT WITH DISABLER OP"));
    }
    
    @Override
    public void onEnable() {
        this.mmcPackets.clear();
    }
    
    @Subscribe
    public void onUpdate(final UpdateEvent e) {
        if (Disabler.mc.field_71439_g.field_70173_aa <= 1) {
            this.idfk = false;
            this.idfk2 = false;
        }
    }
    
    @Subscribe
    public void onPacket(final PacketEvent e) {
        switch (Disabler.mode.getMode()) {
            case MMCSafe: {
                if (e.isOutgoing() && !this.mmc) {
                    if (e.getPacket() instanceof C00PacketKeepAlive) {
                        this.mmcPackets.add(e.getPacket());
                        e.cancel();
                    }
                    if (e.getPacket() instanceof C0FPacketConfirmTransaction) {
                        this.mmcPackets.add(e.getPacket());
                        e.cancel();
                    }
                    final int packetsCap = Utils.Java.randomInt(Disabler.mmcSafeDelay.getInputMin(), Disabler.mmcSafeDelay.getMax());
                    while (this.mmcPackets.size() >= packetsCap) {
                        this.mmc = true;
                        Disabler.mc.field_71439_g.field_71174_a.func_147297_a((Packet)this.mmcPackets.poll());
                    }
                    this.mmc = false;
                    break;
                }
                break;
            }
            case Hypixel: {
                if (Disabler.hypSilentInv.isToggled()) {
                    if ((e.getPacket() instanceof C16PacketClientStatus && e.getPacket().func_149435_c() == C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT) || (e.getPacket() instanceof C0DPacketCloseWindow && e.getPacket().getWindowId() == Disabler.mc.field_71439_g.field_71069_bz.field_75152_c)) {
                        e.cancel();
                    }
                    if (e.getPacket() instanceof C0EPacketClickWindow && e.getPacket().func_149548_c() == Disabler.mc.field_71439_g.field_71069_bz.field_75152_c) {
                        e.cancel();
                        if (this.idfk) {
                            PacketUtils.sendPacketSilent((Packet<?>)new C0BPacketEntityAction((Entity)Disabler.mc.field_71439_g, C0BPacketEntityAction.Action.STOP_SPRINTING));
                        }
                        if (this.idfk2) {
                            PacketUtils.sendPacketSilent((Packet<?>)new C0BPacketEntityAction((Entity)Disabler.mc.field_71439_g, C0BPacketEntityAction.Action.STOP_SNEAKING));
                        }
                        PacketUtils.sendPacketSilent((Packet<?>)new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
                        PacketUtils.sendPacketSilent(e.getPacket());
                        PacketUtils.sendPacketSilent((Packet<?>)new C0DPacketCloseWindow(Disabler.mc.field_71439_g.field_71069_bz.field_75152_c));
                        if (this.idfk) {
                            PacketUtils.sendPacketSilent((Packet<?>)new C0BPacketEntityAction((Entity)Disabler.mc.field_71439_g, C0BPacketEntityAction.Action.START_SPRINTING));
                        }
                        if (this.idfk2) {
                            PacketUtils.sendPacketSilent((Packet<?>)new C0BPacketEntityAction((Entity)Disabler.mc.field_71439_g, C0BPacketEntityAction.Action.START_SNEAKING));
                        }
                    }
                }
                if (!(e.getPacket() instanceof C0BPacketEntityAction)) {
                    break;
                }
                boolean die = false;
                switch (e.getPacket().func_180764_b()) {
                    case STOP_SNEAKING: {
                        if (!this.idfk2) {
                            die = true;
                        }
                        this.idfk2 = false;
                        break;
                    }
                    case START_SNEAKING: {
                        if (this.idfk2) {
                            die = true;
                        }
                        this.idfk2 = true;
                        break;
                    }
                    case STOP_SPRINTING: {
                        if (!this.idfk) {
                            die = true;
                        }
                        this.idfk = false;
                        break;
                    }
                    case START_SPRINTING: {
                        if (this.idfk) {
                            die = true;
                        }
                        this.idfk = true;
                        break;
                    }
                }
                if (die && Disabler.hypSprintFix.isToggled()) {
                    e.cancel();
                    break;
                }
                break;
            }
        }
    }
    
    public enum Mode
    {
        MMCSafe, 
        Hypixel;
    }
}
