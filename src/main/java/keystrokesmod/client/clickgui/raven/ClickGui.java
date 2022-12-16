package keystrokesmod.client.clickgui.raven;

import net.minecraft.client.gui.*;
import keystrokesmod.client.clickgui.raven.components.*;
import keystrokesmod.client.module.*;
import keystrokesmod.client.main.*;
import java.util.concurrent.*;
import keystrokesmod.client.utils.*;
import net.minecraft.client.renderer.*;
import keystrokesmod.client.module.modules.client.*;
import keystrokesmod.client.utils.font.*;
import net.minecraft.client.gui.inventory.*;
import net.minecraft.entity.*;
import keystrokesmod.client.utils.version.*;
import java.util.*;
import java.io.*;
import org.lwjgl.input.*;

public class ClickGui extends GuiScreen
{
    private ScheduledFuture<?> sf;
    private Timer aT;
    private Timer aL;
    private Timer aE;
    private Timer aR;
    private final ArrayList<CategoryComponent> categoryList;
    public final Terminal terminal;
    private int mouseX;
    private int mouseY;
    public static int binding;
    
    public ClickGui() {
        this.terminal = new Terminal();
        this.categoryList = new ArrayList<CategoryComponent>();
        Module.ModuleCategory[] values;
        for (int categoryAmount = (values = Module.ModuleCategory.values()).length, category = 0; category < categoryAmount; ++category) {
            final Module.ModuleCategory moduleCategory = values[category];
            final CategoryComponent currentModuleCategory = new CategoryComponent(moduleCategory);
            currentModuleCategory.setVisable(currentModuleCategory.categoryName.isShownByDefault());
            this.categoryList.add(currentModuleCategory);
        }
        int xOffSet = 5;
        int yOffSet = 5;
        for (final CategoryComponent category2 : this.categoryList) {
            category2.setX(xOffSet);
            category2.setY(yOffSet);
            xOffSet += 100;
            if (xOffSet > 400) {
                xOffSet = 5;
                yOffSet += 120;
            }
        }
        this.terminal.setLocation(380, 0);
        this.terminal.setSize(138, 103);
    }
    
    public void resetSort() {
        int xOffSet = 5;
        int yOffSet = 5;
        for (final CategoryComponent category : this.categoryList) {
            category.setX(xOffSet);
            category.setY(yOffSet);
            xOffSet += 100;
            if (xOffSet > this.field_146294_l - 100) {
                xOffSet = 5;
                yOffSet += this.field_146295_m / 3;
            }
        }
    }
    
    public void initMain() {
        final Timer at = new Timer(500.0f);
        this.aR = at;
        this.aE = at;
        (this.aT = at).start();
        final Timer al;
        this.sf = Raven.getExecutor().schedule(() -> {
            al = new Timer(650.0f);
            (this.aL = al).start();
        }, 650L, TimeUnit.MILLISECONDS);
    }
    
    public void func_73866_w_() {
        super.func_73866_w_();
    }
    
    public void func_73863_a(final int x, final int y, final float p) {
        this.mouseX = x;
        this.mouseY = y;
        final Version clientVersion = Raven.versionManager.getClientVersion();
        final Version latestVersion = Raven.versionManager.getLatestVersion();
        func_73734_a(0, 0, this.field_146294_l, this.field_146295_m, (int)(this.aR.getValueFloat(0.0f, 0.7f, 2) * 255.0f) << 24);
        final int quarterScreenHeight = this.field_146295_m / 4;
        final int halfScreenWidth = this.field_146294_l / 2;
        final int w_c = 30 - this.aT.getValueInt(0, 30, 3);
        this.func_73732_a(this.field_146289_q, "r", halfScreenWidth + 1 - w_c, quarterScreenHeight - 25, Utils.Client.rainbowDraw(2L, 1500L));
        this.func_73732_a(this.field_146289_q, "a", halfScreenWidth - w_c, quarterScreenHeight - 15, Utils.Client.rainbowDraw(2L, 1200L));
        this.func_73732_a(this.field_146289_q, "v", halfScreenWidth - w_c, quarterScreenHeight - 5, Utils.Client.rainbowDraw(2L, 900L));
        this.func_73732_a(this.field_146289_q, "e", halfScreenWidth - w_c, quarterScreenHeight + 5, Utils.Client.rainbowDraw(2L, 600L));
        this.func_73732_a(this.field_146289_q, "n", halfScreenWidth - w_c, quarterScreenHeight + 15, Utils.Client.rainbowDraw(2L, 300L));
        this.func_73732_a(this.field_146289_q, "b", halfScreenWidth + 1 + w_c, quarterScreenHeight + 25, Utils.Client.rainbowDraw(2L, 0L));
        this.func_73732_a(this.field_146289_q, "+ +", halfScreenWidth + 1 + w_c, quarterScreenHeight + 30, Utils.Client.rainbowDraw(2L, 0L));
        final float speed = 4890.0f;
        if (latestVersion.isNewerThan(clientVersion)) {
            int margin = 2;
            int rows = 1;
            for (int i = Raven.updateText.length - 1; i >= 0; --i) {
                final String up = Raven.updateText[i];
                GlStateManager.func_179117_G();
                if (GuiModule.useCustomFont()) {
                    FontUtil.normal.drawSmoothString(up, halfScreenWidth - this.field_146289_q.func_78256_a(up) / 2, (float)(this.field_146295_m - this.field_146289_q.field_78288_b * rows - margin), Utils.Client.astolfoColorsDraw(10, 28, speed));
                }
                else {
                    this.field_146297_k.field_71466_p.func_175063_a(up, (float)(halfScreenWidth - this.field_146289_q.func_78256_a(up) / 2), (float)(this.field_146295_m - this.field_146289_q.field_78288_b * rows - margin), Utils.Client.astolfoColorsDraw(10, 28, speed));
                }
                ++rows;
                margin += 2;
            }
        }
        else {
            GlStateManager.func_179117_G();
            if (GuiModule.useCustomFont()) {
                FontUtil.normal.drawSmoothString("Raven B++ v" + clientVersion + " | Config: " + Raven.configManager.getConfig().getName(), 4.0, (float)(this.field_146295_m - 3 - this.field_146297_k.field_71466_p.field_78288_b), Utils.Client.astolfoColorsDraw(10, 14, speed));
            }
            else {
                this.field_146297_k.field_71466_p.func_175063_a("Raven B++ v" + clientVersion + " | Config: " + Raven.configManager.getConfig().getName(), 4.0f, (float)(this.field_146295_m - 3 - this.field_146297_k.field_71466_p.field_78288_b), Utils.Client.astolfoColorsDraw(10, 14, speed));
            }
        }
        this.func_73728_b(halfScreenWidth - 10 - w_c, quarterScreenHeight - 30, quarterScreenHeight + 38, Utils.Client.customDraw(0));
        this.func_73728_b(halfScreenWidth + 10 + w_c, quarterScreenHeight - 30, quarterScreenHeight + 38, Utils.Client.customDraw(0));
        if (this.aL != null) {
            final int animationProggress = this.aL.getValueInt(0, 20, 2);
            this.func_73730_a(halfScreenWidth - 10, halfScreenWidth - 10 + animationProggress, quarterScreenHeight - 29, Utils.Client.customDraw(0));
            this.func_73730_a(halfScreenWidth + 10, halfScreenWidth + 10 - animationProggress, quarterScreenHeight + 38, Utils.Client.customDraw(0));
        }
        for (final CategoryComponent category : this.categoryList) {
            if (category.isVisable()) {
                category.rf();
                category.up(x, y);
                for (final Component module : category.getModules()) {
                    module.update(x, y);
                }
            }
        }
        GlStateManager.func_179117_G();
        GuiInventory.func_147046_a(this.field_146294_l + 15 - this.aE.getValueInt(0, 40, 2), this.field_146295_m - 19 - this.field_146289_q.field_78288_b, 40, (float)(this.field_146294_l - 25 - x), (float)(this.field_146295_m - 50 - y), (EntityLivingBase)this.field_146297_k.field_71439_g);
        this.terminal.update(x, y);
        this.terminal.draw();
    }
    
    public void func_73864_a(final int x, final int y, final int mouseButton) throws IOException {
        final Iterator<CategoryComponent> btnCat = this.visableCategoryList().iterator();
        this.terminal.mouseDown(x, y, mouseButton);
        if (this.terminal.overPosition(x, y)) {
            return;
        }
        while (btnCat.hasNext()) {
            final CategoryComponent category = btnCat.next();
            if (category.insideArea(x, y) && !category.i(x, y) && !category.mousePressed(x, y) && mouseButton == 0) {
                category.mousePressed(true);
                category.xx = x - category.getX();
                category.yy = y - category.getY();
            }
            else if ((category.mousePressed(x, y) && mouseButton == 0) || (category.insideArea(x, y) && mouseButton == 1)) {
                category.setOpened(!category.isOpened());
                this.field_146297_k.field_71439_g.func_85030_a("gui.button.press", 1.0f, 1.0f);
            }
            else if (category.i(x, y) && mouseButton == 0) {
                category.cv(!category.p());
            }
            else if (!category.isOpened()) {
                continue;
            }
            if (!category.getModules().isEmpty()) {
                try {
                    for (final Component c : category.getModules()) {
                        c.mouseDown(x, y, mouseButton);
                    }
                }
                catch (ConcurrentModificationException ex) {}
            }
        }
    }
    
    public void func_146286_b(final int x, final int y, final int s) {
        this.terminal.mouseReleased(x, y, s);
        if (this.terminal.overPosition(x, y)) {
            return;
        }
        if (s == 0) {
            for (final CategoryComponent c4t : this.visableCategoryList()) {
                c4t.mousePressed(false);
            }
            for (final CategoryComponent c4t : this.visableCategoryList()) {
                if (c4t.isOpened() && !c4t.getModules().isEmpty()) {
                    for (final Component c : c4t.getModules()) {
                        c.mouseReleased(x, y, s);
                    }
                }
            }
            return;
        }
        if (Raven.clientConfig != null) {
            Raven.clientConfig.saveConfig();
        }
    }
    
    public void func_73869_a(final char t, final int k) {
        this.terminal.keyTyped(t, k);
        if (k == 1 && ClickGui.binding <= 0) {
            this.field_146297_k.func_147108_a((GuiScreen)null);
            return;
        }
        for (final CategoryComponent cat : this.visableCategoryList()) {
            if (cat.isOpened() && !cat.getModules().isEmpty()) {
                for (final Component c : cat.getModules()) {
                    c.keyTyped(t, k);
                }
            }
        }
    }
    
    public void func_146274_d() throws IOException {
        super.func_146274_d();
        for (final CategoryComponent c : this.visableCategoryList()) {
            if (c.insideAllArea(this.mouseX, this.mouseY)) {
                int i = Mouse.getEventDWheel();
                i = Integer.compare(i, 0);
                c.scroll(i * 5.0f);
            }
        }
    }
    
    public void func_146281_b() {
        this.aL = null;
        if (this.sf != null) {
            this.sf.cancel(true);
            this.sf = null;
        }
        Raven.configManager.save();
        Raven.clientConfig.saveConfig();
        ClickGui.binding = 0;
    }
    
    public boolean func_73868_f() {
        return false;
    }
    
    public ArrayList<CategoryComponent> getCategoryList() {
        return this.categoryList;
    }
    
    public CategoryComponent getCategoryComponent(final Module.ModuleCategory mCat) {
        for (final CategoryComponent cc : this.categoryList) {
            if (cc.categoryName == mCat) {
                return cc;
            }
        }
        return null;
    }
    
    public ArrayList<CategoryComponent> visableCategoryList() {
        final ArrayList<CategoryComponent> newList = (ArrayList<CategoryComponent>)this.categoryList.clone();
        newList.removeIf(obj -> !obj.isVisable());
        return newList;
    }
}
