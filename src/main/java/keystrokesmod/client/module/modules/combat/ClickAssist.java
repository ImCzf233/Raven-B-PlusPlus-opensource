package keystrokesmod.client.module.modules.combat;

import keystrokesmod.client.module.*;
import keystrokesmod.client.module.setting.impl.*;
import keystrokesmod.client.module.setting.*;
import java.awt.*;
import keystrokesmod.client.event.impl.*;
import net.minecraftforge.client.event.*;
import keystrokesmod.client.utils.*;
import net.minecraft.item.*;
import com.google.common.eventbus.*;
import org.lwjgl.input.*;

public class ClickAssist extends Module
{
    public static DescriptionSetting desc;
    public static SliderSetting chance;
    public static TickSetting L;
    public static TickSetting R;
    public static TickSetting blocksOnly;
    public static TickSetting weaponOnly;
    public static TickSetting onlyWhileTargeting;
    public static TickSetting above5;
    private Robot bot;
    private boolean engagedLeft;
    private boolean engagedRight;
    
    public ClickAssist() {
        super("ClickAssist", ModuleCategory.combat);
        this.registerSetting(ClickAssist.desc = new DescriptionSetting("Boost your CPS."));
        this.registerSetting(ClickAssist.chance = new SliderSetting("Chance", 80.0, 0.0, 100.0, 1.0));
        this.registerSetting(ClickAssist.L = new TickSetting("Left click", true));
        this.registerSetting(ClickAssist.weaponOnly = new TickSetting("Weapon only", true));
        this.registerSetting(ClickAssist.onlyWhileTargeting = new TickSetting("Only while targeting", false));
        this.registerSetting(ClickAssist.R = new TickSetting("Right click", false));
        this.registerSetting(ClickAssist.blocksOnly = new TickSetting("Blocks only", true));
        this.registerSetting(ClickAssist.above5 = new TickSetting("Above 5 cps", false));
    }
    
    @Override
    public void onEnable() {
        try {
            this.bot = new Robot();
        }
        catch (AWTException var2) {
            this.disable();
        }
    }
    
    @Override
    public void onDisable() {
        this.engagedLeft = false;
        this.engagedRight = false;
        this.bot = null;
    }
    
    @Subscribe
    public void onMouseUpdate(final ForgeEvent fe) {
        if (fe.getEvent() instanceof MouseEvent) {
            final MouseEvent ev = (MouseEvent)fe.getEvent();
            if (ev.button >= 0 && ev.buttonstate && ClickAssist.chance.getInput() != 0.0 && Utils.Player.isPlayerInGame()) {
                if (ClickAssist.mc.field_71462_r == null && !ClickAssist.mc.field_71439_g.func_70113_ah() && !ClickAssist.mc.field_71439_g.func_70632_aY()) {
                    if (ev.button == 0 && ClickAssist.L.isToggled()) {
                        if (this.engagedLeft) {
                            this.engagedLeft = false;
                        }
                        else {
                            if (ClickAssist.weaponOnly.isToggled() && !Utils.Player.isPlayerHoldingWeapon()) {
                                return;
                            }
                            if (ClickAssist.onlyWhileTargeting.isToggled() && (ClickAssist.mc.field_71476_x == null || ClickAssist.mc.field_71476_x.field_72308_g == null)) {
                                return;
                            }
                            if (ClickAssist.chance.getInput() != 100.0) {
                                final double ch = Math.random();
                                if (ch >= ClickAssist.chance.getInput() / 100.0) {
                                    this.fix(0);
                                    return;
                                }
                            }
                            this.bot.mouseRelease(16);
                            this.bot.mousePress(16);
                            this.engagedLeft = true;
                        }
                    }
                    else if (ev.button == 1 && ClickAssist.R.isToggled()) {
                        if (this.engagedRight) {
                            this.engagedRight = false;
                        }
                        else {
                            if (ClickAssist.blocksOnly.isToggled()) {
                                final ItemStack item = ClickAssist.mc.field_71439_g.func_70694_bm();
                                if (item == null || !(item.func_77973_b() instanceof ItemBlock)) {
                                    this.fix(1);
                                    return;
                                }
                            }
                            if (ClickAssist.above5.isToggled() && MouseManager.getRightClickCounter() <= 5) {
                                this.fix(1);
                                return;
                            }
                            if (ClickAssist.chance.getInput() != 100.0) {
                                final double ch = Math.random();
                                if (ch >= ClickAssist.chance.getInput() / 100.0) {
                                    this.fix(1);
                                    return;
                                }
                            }
                            this.bot.mouseRelease(4);
                            this.bot.mousePress(4);
                            this.engagedRight = true;
                        }
                    }
                }
                this.fix(0);
                this.fix(1);
            }
        }
    }
    
    private void fix(final int t) {
        if (t == 0) {
            if (this.engagedLeft && !Mouse.isButtonDown(0)) {
                this.bot.mouseRelease(16);
            }
        }
        else if (t == 1 && this.engagedRight && !Mouse.isButtonDown(1)) {
            this.bot.mouseRelease(4);
        }
    }
}
