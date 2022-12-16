package keystrokesmod.client.module.modules.combat;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import keystrokesmod.client.module.setting.*;
import keystrokesmod.client.event.impl.*;
import keystrokesmod.client.utils.*;
import keystrokesmod.client.mixin.mixins.*;
import net.minecraft.util.*;
import com.google.common.eventbus.*;
import net.minecraft.entity.player.*;
import keystrokesmod.client.module.modules.world.*;
import net.minecraft.entity.*;
import java.util.stream.*;

public class LegitAura extends Module
{
    public static DescriptionSetting dRotSpeed1;
    public static DescriptionSetting dRotSpeed2;
    public static DescriptionSetting dHitChance;
    public static DescriptionSetting dCombo;
    public static DoubleSliderSetting clickJitter;
    public static DoubleSliderSetting aps;
    public static DoubleSliderSetting reach;
    public static DoubleSliderSetting comboRange;
    public static SliderSetting hitChance;
    public static SliderSetting rotationSpeed;
    public static SliderSetting switchHits;
    public static SliderSetting targetRange;
    public static ComboSetting<TargetingMode> targetingMode;
    public static TickSetting combo;
    public static TickSetting tPlayers;
    public static TickSetting tOthers;
    public static TickSetting tInvisible;
    public static TickSetting tDead;
    public static TickSetting tSmartSwitch;
    boolean triedToCombo;
    EntityLivingBase target;
    int hits;
    long lastAttack;
    
    public LegitAura() {
        super("LegitAura", ModuleCategory.combat);
        this.hits = 0;
        this.registerSetting(LegitAura.aps = new DoubleSliderSetting("CPS", 9.5, 11.0, 1.0, 20.0, 0.1));
        this.registerSetting(LegitAura.reach = new DoubleSliderSetting("Reach (Blocks)", 3.0, 3.2, 3.0, 6.0, 0.05));
        this.registerSetting(LegitAura.clickJitter = new DoubleSliderSetting("Attack Jitter", 0.1, 0.2, 0.0, 2.0, 0.05));
        this.registerSetting(new DescriptionSetting(EnumChatFormatting.GRAY + "        "));
        this.registerSetting(LegitAura.dHitChance = new DescriptionSetting(EnumChatFormatting.GRAY + "% chance that an attack will hit."));
        this.registerSetting(LegitAura.hitChance = new SliderSetting("Hit Chance %", 95.0, 10.0, 100.0, 1.0));
        this.registerSetting(new DescriptionSetting(EnumChatFormatting.GRAY + "      "));
        this.registerSetting(LegitAura.dRotSpeed1 = new DescriptionSetting(EnumChatFormatting.GRAY + "Rotation Speed Math:"));
        this.registerSetting(LegitAura.dRotSpeed2 = new DescriptionSetting(EnumChatFormatting.YELLOW + "rotation speed * sensitivity"));
        this.registerSetting(LegitAura.rotationSpeed = new SliderSetting("Rotation Speed", 80.0, 10.0, 200.0, 5.0));
        this.registerSetting(LegitAura.dCombo = new DescriptionSetting(EnumChatFormatting.GRAY + "Stops enemy velocity with projectile."));
        this.registerSetting(new DescriptionSetting(EnumChatFormatting.GRAY + "     "));
        this.registerSetting(LegitAura.combo = new TickSetting("Try to Combo", true));
        this.registerSetting(LegitAura.comboRange = new DoubleSliderSetting("Combo Range", 3.1, 3.3, 3.0, 6.0, 0.05));
        this.registerSetting(new DescriptionSetting(EnumChatFormatting.GRAY + "   "));
        this.registerSetting(LegitAura.dCombo = new DescriptionSetting(EnumChatFormatting.GRAY + "Targets:"));
        this.registerSetting(LegitAura.tPlayers = new TickSetting("Players", true));
        this.registerSetting(LegitAura.tOthers = new TickSetting("Other", false));
        this.registerSetting(LegitAura.tInvisible = new TickSetting("Invisible", false));
        this.registerSetting(LegitAura.tDead = new TickSetting("Dead", false));
        this.registerSetting(new DescriptionSetting(EnumChatFormatting.GRAY + "    "));
        this.registerSetting(LegitAura.targetRange = new SliderSetting("Targeting Range", 3.4, 3.0, 6.0, 0.05));
        this.registerSetting(LegitAura.targetingMode = new ComboSetting<TargetingMode>("Targeting Mode", TargetingMode.SINGLE));
        this.registerSetting(LegitAura.switchHits = new SliderSetting("Switch Hits", 2.0, 1.0, 5.0, 1.0));
        this.registerSetting(LegitAura.tSmartSwitch = new TickSetting("Smart Target Switching", false));
    }
    
    @Subscribe
    public void onUpdate(final UpdateEvent event) {
        if (event.isPre()) {
            Utils.Player.sendMessageToSelf("pre");
            if (this.target != null) {
                if (!this.isCurrentTargetValid(this.target)) {
                    this.target = null;
                    return;
                }
                if (System.currentTimeMillis() - this.lastAttack >= Utils.Client.ranModuleVal(LegitAura.aps, Utils.Java.rand())) {
                    final MovingObjectPosition raytrace = Utils.Player.rayTrace(Utils.Client.ranModuleVal(LegitAura.reach, Utils.Java.rand()), ((IMinecraft)LegitAura.mc).getTimer().field_74281_c);
                    assert raytrace != null;
                    final boolean canHit = raytrace.field_72308_g != null;
                    final boolean isRealTarget = canHit && raytrace.field_72308_g == this.target;
                    if (canHit && (isRealTarget || (raytrace.field_72308_g instanceof EntityLivingBase && this.isCurrentTargetValid((EntityLivingBase)raytrace.field_72308_g)))) {
                        LegitAura.mc.field_71439_g.func_71038_i();
                        if (Math.random() <= LegitAura.hitChance.getInput() / 100.0) {
                            Utils.Player.sendMessageToSelf("attack");
                            LegitAura.mc.field_71439_g.func_71059_n(raytrace.field_72308_g);
                        }
                        if (isRealTarget) {
                            ++this.hits;
                            if (this.hits >= LegitAura.switchHits.getInput()) {
                                this.target = this.getTarget();
                                this.hits = 0;
                            }
                        }
                        else if (LegitAura.tSmartSwitch.isToggled()) {
                            this.target = (EntityLivingBase)raytrace.field_72308_g;
                            this.hits = 0;
                        }
                    }
                    else {
                        LegitAura.mc.field_71439_g.func_71038_i();
                    }
                    this.lastAttack = System.currentTimeMillis();
                }
            }
            else {
                this.target = this.getTarget();
                this.hits = 0;
            }
        }
    }
    
    private boolean isCurrentTargetValid(final EntityLivingBase e) {
        return e != LegitAura.mc.field_71439_g && (LegitAura.tPlayers.isToggled() || !(e instanceof EntityPlayer)) && (LegitAura.tOthers.isToggled() || e instanceof EntityPlayer) && (LegitAura.tDead.isToggled() || (!e.field_70128_L && e.func_110143_aJ() > 0.0f)) && (LegitAura.tInvisible.isToggled() || !e.func_98034_c((EntityPlayer)LegitAura.mc.field_71439_g)) && !AntiBot.bot((Entity)e) && e.func_70032_d((Entity)LegitAura.mc.field_71439_g) <= LegitAura.targetRange.getInput();
    }
    
    private boolean isTargetValid(final EntityLivingBase e) {
        return e != LegitAura.mc.field_71439_g && (LegitAura.tPlayers.isToggled() || !(e instanceof EntityPlayer)) && (LegitAura.tOthers.isToggled() || e instanceof EntityPlayer) && (LegitAura.tDead.isToggled() || (!e.field_70128_L && e.func_110143_aJ() > 0.0f)) && (LegitAura.tInvisible.isToggled() || !e.func_98034_c((EntityPlayer)LegitAura.mc.field_71439_g)) && e != this.target && !AntiBot.bot((Entity)e) && e.func_70032_d((Entity)LegitAura.mc.field_71439_g) <= LegitAura.targetRange.getInput();
    }
    
    private EntityLivingBase getTarget() {
        Stream<Entity> stream = (Stream<Entity>)LegitAura.mc.field_71441_e.field_72996_f.stream().filter(e -> e instanceof EntityLivingBase).filter(e -> e != LegitAura.mc.field_71439_g);
        if (!LegitAura.tPlayers.isToggled()) {
            stream = stream.filter(e -> !(e instanceof EntityPlayer));
        }
        if (!LegitAura.tOthers.isToggled()) {
            stream = stream.filter(e -> e instanceof EntityPlayer);
        }
        if (!LegitAura.tDead.isToggled()) {
            stream = stream.filter(e -> e instanceof EntityLivingBase && !((Entity)e).field_70128_L && e.func_110143_aJ() > 0.0f);
        }
        if (!LegitAura.tInvisible.isToggled()) {
            stream = stream.filter(e -> e instanceof EntityLivingBase && !e.func_98034_c((EntityPlayer)LegitAura.mc.field_71439_g));
        }
        stream = stream.filter(e -> e != this.target).filter(e -> !AntiBot.bot(e)).filter(e -> e.func_70032_d((Entity)LegitAura.mc.field_71439_g) <= LegitAura.targetRange.getInput());
        return (EntityLivingBase)stream.findFirst().orElse(null);
    }
    
    public enum TargetingMode
    {
        SINGLE, 
        SWITCH;
    }
}
