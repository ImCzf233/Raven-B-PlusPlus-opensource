package keystrokesmod.client.clickgui.raven;

import keystrokesmod.client.utils.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import java.awt.*;
import org.lwjgl.opengl.*;
import java.util.*;
import org.apache.commons.lang3.*;
import keystrokesmod.client.main.*;

public class Terminal implements Component
{
    private int x;
    private int y;
    private int width;
    private int height;
    private final int barHeight;
    private final int border;
    private final int minWidth;
    private final int minHeight;
    private final int resizeButtonSize;
    public boolean opened;
    public boolean hidden;
    private boolean resizing;
    private boolean focused;
    private final CoolDown keyDown;
    private int backCharsCursor;
    public final int[] acceptableKeycodes;
    private final Minecraft mc;
    private final FontRenderer fr;
    private String inputText;
    private static final ArrayList<String> out;
    private final String prefix = "$ ";
    private boolean dragging;
    private double windowStartDragX;
    private double windowStartDragY;
    private double mouseStartDragX;
    private double mouseStartDragY;
    
    public Terminal() {
        this.keyDown = new CoolDown(500L);
        this.acceptableKeycodes = new int[] { 41, 0, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 26, 27, 39, 40, 0, 51, 52, 53, 41, 145, 144, 147, 146, 57, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 30, 31, 32, 33, 34, 35, 36, 37, 38, 44, 45, 46, 47, 48, 49, 50 };
        this.inputText = "";
        this.x = 0;
        this.y = 0;
        this.width = 300;
        this.minWidth = 184;
        this.height = 214;
        this.minHeight = 67;
        this.barHeight = 13;
        this.mc = Minecraft.func_71410_x();
        this.fr = this.mc.field_71466_p;
        this.border = 2;
        this.resizeButtonSize = 10;
    }
    
    public static void clearTerminal() {
        Terminal.out.clear();
    }
    
    public void show() {
        this.hidden = false;
    }
    
    public void hide() {
        this.hidden = true;
    }
    
    @Override
    public void draw() {
        if (this.hidden) {
            return;
        }
        final double desiredTextSize = this.barHeight * 0.65;
        final double scaleFactor = desiredTextSize / this.fr.field_78288_b;
        final double coordFactor = 1.0 / scaleFactor;
        final double margin = (int)((this.barHeight - desiredTextSize) * 0.8);
        final float textY = (float)((this.y + margin) * coordFactor);
        final float textX = (float)((this.x + margin) * coordFactor);
        final float buttonX = (float)((this.x + this.width - margin - this.fr.func_78256_a(this.opened ? "-" : "+")) * coordFactor);
        int cursorX = 0;
        int cursorY = 0;
        float outStartX = (float)(this.x + margin + this.border);
        float outFinishX = (float)(this.x + this.width - (margin + this.border));
        float outStartY = (float)(this.y + this.barHeight + margin);
        float outFinishY = (float)(this.y + this.height - margin - this.border);
        final float maxTextWidth = outFinishX - outStartX;
        final int maxLines = Math.floorDiv((int)(outFinishY - outStartY), (int)(desiredTextSize + margin));
        int linesPrinted = 0;
        cursorX = (int)outStartX;
        outStartX *= (float)coordFactor;
        outFinishX *= (float)coordFactor;
        outStartY *= (float)coordFactor;
        outFinishY *= (float)coordFactor;
        Gui.func_73734_a(this.x, this.y, this.x + this.width, this.y + this.barHeight, -13813950);
        if (this.opened) {
            Gui.func_73734_a(this.x, this.y + this.barHeight, this.x + this.width, this.y + this.height, new Color(51, 51, 51, 210).getRGB());
            Gui.func_73734_a(this.x, this.y + this.barHeight, this.x + this.border, this.y + this.height, -13813950);
            Gui.func_73734_a(this.x + this.width, this.y + this.barHeight, this.x + this.width - this.border, this.y + this.height, -13813950);
            Gui.func_73734_a(this.x, this.y + this.height - this.border, this.x + this.width, this.y + this.height, -13813950);
            Gui.func_73734_a(this.x + this.width - this.resizeButtonSize, this.y + this.height - this.resizeButtonSize, this.x + this.width, this.y + this.height, new Color(79, 104, 158).getRGB());
        }
        GL11.glPushMatrix();
        GL11.glScaled(scaleFactor, scaleFactor, scaleFactor);
        this.fr.func_175065_a("Terminal", textX, textY, 16777215, false);
        this.fr.func_175065_a(this.opened ? "-" : "+", buttonX, textY, 16777215, false);
        if (this.opened) {
            final ArrayList<String> currentOut = new ArrayList<String>(Terminal.out);
            currentOut.add("$ " + this.inputText);
            final ArrayList<String> finalOut = new ArrayList<String>();
            for (int end = (currentOut.size() >= maxLines) ? (currentOut.size() - maxLines) : 0, j = currentOut.size() - 1; j >= end; --j) {
                final String currentLine = currentOut.get(j);
                final String[] splitUpLine = this.splitUpLine(currentLine, maxTextWidth, scaleFactor);
                for (int i = splitUpLine.length - 1; i >= 0 && linesPrinted < maxLines; ++linesPrinted, --i) {
                    finalOut.add(splitUpLine[i]);
                }
            }
            final String[] inputTextLineSplit = this.splitUpLine("$ " + this.inputText.substring(0, this.inputText.length() - this.backCharsCursor), maxTextWidth, scaleFactor);
            final String finalInputLine = inputTextLineSplit[inputTextLineSplit.length - 1];
            cursorX += (int)(this.fr.func_78256_a(finalInputLine) * scaleFactor);
            for (int k = finalOut.size() - 1; k >= 0; --k) {
                final String currentLine = finalOut.get(k);
                final int topMargin = (int)((finalOut.size() - 1 - k) * (desiredTextSize + margin) * coordFactor + outStartY);
                this.fr.func_78276_b(currentLine, (int)outStartX, topMargin, new Color(32, 194, 14).getRGB());
                if (currentLine.startsWith(finalInputLine)) {
                    cursorY = (int)(topMargin / coordFactor);
                }
            }
        }
        GL11.glPopMatrix();
        if (this.opened) {
            Gui.func_73734_a(cursorX, cursorY, cursorX + 1, (int)(cursorY + desiredTextSize), -1);
        }
    }
    
    private String[] splitUpLine(final String currentLine, final float maxTextWidth, final double scaleSize) {
        if (this.fr.func_78256_a(currentLine) * scaleSize <= maxTextWidth) {
            return new String[] { currentLine };
        }
        for (int i = currentLine.length(); i >= 0; --i) {
            final String newLine = currentLine.substring(0, i);
            if (this.fr.func_78256_a(newLine) * scaleSize <= maxTextWidth) {
                return mergeArray(new String[] { newLine }, this.splitUpLine(currentLine.substring(i), maxTextWidth, scaleSize));
            }
        }
        return new String[] { "" };
    }
    
    public static void print(final String message) {
        Terminal.out.add(message);
    }
    
    public static String[] mergeArray(final String[] arr1, final String[] arr2) {
        return ArrayUtils.addAll(arr1, arr2);
    }
    
    @Override
    public void update(final int x, final int y) {
        if (this.hidden) {
            return;
        }
        if (this.dragging) {
            this.x = (int)(this.windowStartDragX + (x - this.mouseStartDragX));
            this.y = (int)(this.windowStartDragY + (y - this.mouseStartDragY));
        }
        else if (this.resizing) {
            final int newWidth = Math.max(x, this.x + this.minWidth) - this.x;
            final int newHeight = Math.max(y, this.y + this.minHeight) - this.y;
            this.width = newWidth;
            this.height = newHeight;
        }
    }
    
    @Override
    public void mouseDown(final int x, final int y, final int b) {
        this.focused = false;
        if (this.hidden) {
            return;
        }
        if (this.overToggleButton(x, y) && b == 0) {
            this.opened = !this.opened;
        }
        else if (this.overBar(x, y)) {
            if (b == 0) {
                this.dragging = true;
                this.mouseStartDragX = x;
                this.mouseStartDragY = y;
                this.windowStartDragX = this.x;
                this.windowStartDragY = this.y;
            }
            else if (b == 1) {
                this.opened = !this.opened;
            }
        }
        else if (this.overResize(x, y) && b == 0) {
            this.resizing = true;
        }
        else if (this.overWindow(x, y) && b == 0) {
            this.focused = true;
        }
    }
    
    @Override
    public void mouseReleased(final int x, final int y, final int m) {
        if (this.hidden) {
            return;
        }
        if (this.dragging) {
            this.dragging = false;
        }
        else if (this.resizing) {
            this.resizing = false;
        }
    }
    
    @Override
    public void keyTyped(final char t, final int k) {
        if (!this.focused) {
            return;
        }
        if (k == 28) {
            Terminal.out.add("$ " + this.inputText);
            this.proccessInput();
            this.inputText = "";
            this.backCharsCursor = 0;
        }
        else if (k == 14) {
            if (this.inputText.substring(0, this.inputText.length() - this.backCharsCursor).length() > 0) {
                if (this.backCharsCursor == 0) {
                    this.inputText = this.inputText.substring(0, this.inputText.length() - 1);
                }
                else {
                    String deletable = this.inputText.substring(0, this.inputText.length() - this.backCharsCursor);
                    final String appendable = this.inputText.substring(this.inputText.length() - this.backCharsCursor);
                    if (deletable.length() > 0) {
                        deletable = deletable.substring(0, deletable.length() - 1);
                    }
                    this.inputText = deletable + appendable;
                }
            }
        }
        else if (k == 15) {
            this.addCharToInput("    ");
        }
        else if (k == 203) {
            if (this.backCharsCursor < this.inputText.length()) {
                ++this.backCharsCursor;
            }
        }
        else if (k == 205) {
            if (this.backCharsCursor > 0) {
                --this.backCharsCursor;
            }
        }
        else {
            if (!this.containsElement(this.acceptableKeycodes, k)) {
                return;
            }
            final String e = String.valueOf(t);
            if (!e.isEmpty()) {
                this.addCharToInput(e);
            }
        }
    }
    
    private boolean containsElement(final int[] acceptableKeycodes, final int k) {
        for (final int i : acceptableKeycodes) {
            if (i == k) {
                return true;
            }
        }
        return false;
    }
    
    private void addCharToInput(final String e) {
        if (this.backCharsCursor == 0) {
            this.inputText += e;
        }
        else {
            final String deletable = this.inputText.substring(0, this.inputText.length() - this.backCharsCursor);
            final String appendable = this.inputText.substring(this.inputText.length() - this.backCharsCursor);
            this.inputText = deletable + e + appendable;
        }
    }
    
    private void proccessInput() {
        if (!this.inputText.isEmpty()) {
            try {
                final String command = this.inputText.split(" ")[0];
                final boolean hasArgs = this.inputText.contains(" ");
                final String[] args = hasArgs ? this.inputText.substring(command.length() + 1).split(" ") : new String[0];
                Raven.commandManager.executeCommand(command, args);
            }
            catch (IndexOutOfBoundsException ex) {}
        }
    }
    
    @Override
    public void setComponentStartAt(final int n) {
    }
    
    @Override
    public int getHeight() {
        return this.height;
    }
    
    public boolean overPosition(final int x, final int y) {
        return !this.hidden && (this.opened ? this.overWindow(x, y) : this.overBar(x, y));
    }
    
    public boolean overBar(final int x, final int y) {
        return x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.barHeight;
    }
    
    public boolean overWindow(final int x, final int y) {
        return this.opened && x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.height;
    }
    
    public boolean overResize(final int x, final int y) {
        return x >= this.x + this.width - this.resizeButtonSize && x <= this.x + this.width && y >= this.y + this.height - this.resizeButtonSize && y <= this.y + this.height;
    }
    
    public boolean overToggleButton(final int x, final int y) {
        return x >= this.x + this.width - this.barHeight && x <= this.x + this.width && y >= this.y && y <= this.y + this.barHeight;
    }
    
    public void setLocation(final int x, final int y) {
        this.x = x;
        this.y = y;
    }
    
    public void setSize(final int width, final int height) {
        this.width = width;
        this.height = height;
    }
    
    public boolean hidden() {
        return this.hidden;
    }
    
    @Override
    public int getY() {
        return this.y;
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    static {
        out = new ArrayList<String>();
    }
}
