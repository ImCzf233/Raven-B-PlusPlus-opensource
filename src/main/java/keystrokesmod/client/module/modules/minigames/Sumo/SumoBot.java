package keystrokesmod.client.module.modules.minigames.Sumo;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.modules.combat.*;
import keystrokesmod.client.module.setting.impl.*;
import keystrokesmod.client.module.setting.*;
import keystrokesmod.client.main.*;
import net.minecraft.client.settings.*;
import keystrokesmod.client.utils.*;
import java.util.*;
import com.google.common.eventbus.*;
import keystrokesmod.client.event.impl.*;
import net.minecraftforge.client.event.*;
import keystrokesmod.client.module.modules.render.*;

public class SumoBot extends Module
{
    private State state;
    private STap sTap;
    private SumoClicker leftClicker;
    private AimAssist aimAssist;
    private final CoolDown timer;
    private final CoolDown rcTimer;
    private final CoolDown slotTimer;
    private final SliderSetting autoClickerTrigger;
    
    public SumoBot() {
        super("Sumo Bot", ModuleCategory.sumo);
        this.state = State.HUB;
        this.timer = new CoolDown(0L);
        this.rcTimer = new CoolDown(0L);
        this.slotTimer = new CoolDown(0L);
        this.registerSetting(this.autoClickerTrigger = new SliderSetting("Distance for Autoclicker", 4.5, 1.0, 5.0, 0.1));
    }
    
    @Override
    public void onEnable() {
        this.state = State.HUB;
        this.sTap = (STap)Raven.moduleManager.getModuleByName("STap");
        this.leftClicker = (SumoClicker)Raven.moduleManager.getModuleByName("Sumo Clicker");
        this.aimAssist = (AimAssist)Raven.moduleManager.getModuleByName("AimAssist");
        SumoBot.mc.field_71474_y.field_82881_y = false;
    }
    
    private void matchStart() {
        this.state = State.INGAME;
        this.sTap.setToggled(true);
        this.aimAssist.setToggled(true);
        SumoBot.mc.field_71439_g.field_71071_by.field_70461_c = 4;
        this.slotTimer.setCooldown(2300L);
        this.slotTimer.start();
        KeyBinding.func_74510_a(SumoBot.mc.field_71474_y.field_74351_w.func_151463_i(), true);
    }
    
    private void matchEnd() {
        this.timer.setCooldown(2300L);
        this.timer.start();
        this.state = State.GAMEEND;
        this.sTap.setToggled(false);
        this.leftClicker.setToggled(false);
        this.aimAssist.setToggled(false);
        SumoBot.mc.field_71439_g.field_71071_by.field_70461_c = 4;
        KeyBinding.func_74507_a(SumoBot.mc.field_71474_y.field_74313_G.func_151463_i());
        KeyBinding.func_74510_a(SumoBot.mc.field_71474_y.field_74351_w.func_151463_i(), false);
    }
    
    @Subscribe
    public void onRender2D(final Render2DEvent e) {
        if (!Utils.Player.isPlayerInGame()) {
            return;
        }
        if (this.slotTimer.firstFinish()) {
            SumoBot.mc.field_71439_g.field_71071_by.field_70461_c = 4;
        }
        if (this.rcTimer.firstFinish() && this.state == State.GAMEEND) {
            KeyBinding.func_74507_a(SumoBot.mc.field_71474_y.field_74313_G.func_151463_i());
            this.rcTimer.setCooldown(1900L);
            this.rcTimer.start();
        }
        if (this.state == State.QUEUE) {
            for (final String s : Utils.Client.getPlayersFromScoreboard()) {
                if (s.contains("Opponent")) {
                    this.matchStart();
                }
            }
        }
        else if (this.state == State.GAMEEND) {
            if (this.timer.hasFinished()) {
                this.state = State.QUEUE;
                this.rcTimer.setCooldown(1900L);
                this.rcTimer.start();
                KeyBinding.func_74507_a(SumoBot.mc.field_71474_y.field_74313_G.func_151463_i());
            }
        }
        else if (this.state == State.HUB && this.timer.hasFinished()) {
            if (this.timer.firstFinish()) {
                SumoBot.mc.field_71439_g.func_71165_d("/play duels_sumo_duel");
                this.state = State.QUEUE;
            }
            this.timer.setCooldown(1500L);
            this.timer.start();
        }
        if (this.state == State.INGAME) {
            if (this.leftClicker.isEnabled()) {
                if (Utils.Player.getClosestPlayer(this.autoClickerTrigger.getInput()) == null) {
                    this.leftClicker.disable();
                }
            }
            else if (!this.leftClicker.isEnabled() && Utils.Player.getClosestPlayer(this.autoClickerTrigger.getInput()) != null) {
                this.leftClicker.enable();
            }
        }
    }
    
    @Subscribe
    public void onForgeEvent(final ForgeEvent fe) {
        if (fe.getEvent() instanceof ClientChatReceivedEvent && (AntiShuffle.getUnformattedTextForChat(((ClientChatReceivedEvent)fe.getEvent()).message.func_150254_d()).contains("WINNER") || AntiShuffle.getUnformattedTextForChat(((ClientChatReceivedEvent)fe.getEvent()).message.func_150254_d()).contains("DRAW"))) {
            this.matchEnd();
        }
    }
    
    public void reQueue() {
        SumoBot.mc.field_71439_g.func_71165_d("/hub");
        this.state = State.HUB;
    }
    
    public enum State
    {
        INGAME, 
        HUB, 
        QUEUE, 
        GAMEEND;
    }
}
