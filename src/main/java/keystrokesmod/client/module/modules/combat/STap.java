package keystrokesmod.client.module.modules.combat;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import net.minecraft.entity.*;
import java.util.*;
import keystrokesmod.client.module.setting.*;
import com.google.common.eventbus.*;
import keystrokesmod.client.event.impl.*;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.event.entity.living.*;
import net.minecraft.entity.player.*;
import keystrokesmod.client.utils.*;
import net.minecraft.client.settings.*;
import java.util.concurrent.*;

public class STap extends Module
{
    public ComboSetting eventType;
    public SliderSetting range;
    public SliderSetting chance;
    public SliderSetting tapMultiplier;
    public TickSetting onlyPlayers;
    public TickSetting onlySword;
    public TickSetting dynamic;
    public DoubleSliderSetting waitMs;
    public DoubleSliderSetting actionMs;
    public DoubleSliderSetting hitPer;
    public int hits;
    public int rhit;
    public boolean call;
    public boolean p;
    public long s;
    private StapState state;
    private final CoolDown timer;
    private Entity target;
    public Random r;
    
    public STap() {
        super("STap", ModuleCategory.combat);
        this.state = StapState.NONE;
        this.timer = new CoolDown(0L);
        this.r = new Random();
        this.registerSetting(this.eventType = new ComboSetting("Event:", (T)WTap.EventType.Attack));
        this.registerSetting(this.onlyPlayers = new TickSetting("Only combo players", true));
        this.registerSetting(this.onlySword = new TickSetting("Only sword", false));
        this.registerSetting(this.waitMs = new DoubleSliderSetting("Press s for ... ms", 30.0, 40.0, 1.0, 300.0, 1.0));
        this.registerSetting(this.actionMs = new DoubleSliderSetting("STap after ... ms", 20.0, 30.0, 1.0, 300.0, 1.0));
        this.registerSetting(this.chance = new SliderSetting("Chance %", 100.0, 0.0, 100.0, 1.0));
        this.registerSetting(this.hitPer = new DoubleSliderSetting("Once every ... hits", 1.0, 1.0, 1.0, 10.0, 1.0));
        this.registerSetting(this.range = new SliderSetting("Range: ", 3.0, 1.0, 6.0, 0.05));
        this.registerSetting(this.dynamic = new TickSetting("Dynamic tap time", false));
        this.registerSetting(this.tapMultiplier = new SliderSetting("wait time sensitivity", 1.0, 0.0, 5.0, 0.10000000149011612));
    }
    
    @Subscribe
    public void onRender2D(final Render2DEvent e) {
        if (this.state == StapState.NONE) {
            return;
        }
        if (this.state == StapState.WAITINGTOTAP && this.timer.hasFinished()) {
            this.startCombo();
        }
        else if (this.state == StapState.TAPPING && this.timer.hasFinished()) {
            this.finishCombo();
        }
    }
    
    @Subscribe
    public void onForgeEvent(final ForgeEvent fe) {
        if (fe.getEvent() instanceof AttackEntityEvent) {
            final AttackEntityEvent e = (AttackEntityEvent)fe.getEvent();
            this.target = e.target;
            if (this.isSecondCall() && this.eventType.getMode() == WTap.EventType.Attack) {
                this.sTap();
            }
        }
        else if (fe.getEvent() instanceof LivingEvent.LivingUpdateEvent) {
            final LivingEvent.LivingUpdateEvent e2 = (LivingEvent.LivingUpdateEvent)fe.getEvent();
            if (this.eventType.getMode() == WTap.EventType.Hurt && e2.entityLiving.field_70737_aN > 0 && e2.entityLiving.field_70737_aN == e2.entityLiving.field_70738_aO && e2.entity == this.target) {
                this.sTap();
            }
        }
    }
    
    public void sTap() {
        if (this.state != StapState.NONE) {
            return;
        }
        if (Math.random() > this.chance.getInput() / 100.0) {
            ++this.hits;
        }
        if (STap.mc.field_71439_g.func_70032_d(this.target) > this.range.getInput() || (this.onlyPlayers.isToggled() && !(this.target instanceof EntityPlayer)) || (this.onlySword.isToggled() && !Utils.Player.isPlayerHoldingSword()) || this.rhit < this.hits) {
            return;
        }
        this.trystartCombo();
    }
    
    public void finishCombo() {
        this.state = StapState.NONE;
        KeyBinding.func_74510_a(STap.mc.field_71474_y.field_74368_y.func_151463_i(), false);
        this.hits = 0;
        final int easports = (int)(this.hitPer.getInputMax() - this.hitPer.getInputMin() + 1.0);
        this.rhit = ThreadLocalRandom.current().nextInt(easports);
        this.rhit += (int)this.hitPer.getInputMin();
    }
    
    public void startCombo() {
        this.state = StapState.TAPPING;
        KeyBinding.func_74510_a(STap.mc.field_71474_y.field_74368_y.func_151463_i(), true);
        double cd = ThreadLocalRandom.current().nextDouble(this.waitMs.getInputMin(), this.waitMs.getInputMax() + 0.01);
        if (this.dynamic.isToggled() && STap.mc.field_71439_g != null && this.target != null) {
            cd = ((3.0f - STap.mc.field_71439_g.func_70032_d(this.target) < 3.0f) ? (cd + (3.0 - STap.mc.field_71439_g.func_70032_d(this.target) * this.tapMultiplier.getInput() * 10.0)) : cd);
        }
        this.timer.setCooldown((long)cd);
        this.timer.start();
    }
    
    public void trystartCombo() {
        this.state = StapState.WAITINGTOTAP;
        this.timer.setCooldown((long)ThreadLocalRandom.current().nextDouble(this.actionMs.getInputMin(), this.actionMs.getInputMax() + 0.01));
        this.timer.start();
    }
    
    @Override
    public void guiButtonToggled(final TickSetting b) {
    }
    
    private boolean isSecondCall() {
        if (this.call) {
            this.call = false;
            return true;
        }
        this.call = true;
        return false;
    }
    
    public enum StapState
    {
        NONE, 
        WAITINGTOTAP, 
        TAPPING;
    }
}
