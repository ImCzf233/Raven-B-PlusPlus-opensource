package keystrokesmod.client.main;

import net.minecraft.util.*;
import net.minecraft.client.settings.*;
import net.minecraft.client.resources.*;
import net.minecraft.client.renderer.texture.*;
import java.awt.image.*;
import org.apache.commons.io.*;
import java.io.*;
import org.lwjgl.opengl.*;
import com.ibm.icu.text.*;
import net.minecraft.client.renderer.vertex.*;
import net.minecraft.client.renderer.*;
import java.util.*;
import net.minecraft.client.*;

public class FontRenderer extends net.minecraft.client.gui.FontRenderer
{
    private static final ResourceLocation[] unicodePageLocations;
    protected int[] field_78286_d;
    public int field_78288_b;
    public Random field_78289_c;
    protected byte[] field_78287_e;
    private final int[] colorCode;
    protected final ResourceLocation field_111273_g;
    private final TextureManager renderEngine;
    protected float field_78295_j;
    protected float field_78296_k;
    private boolean unicodeFlag;
    private boolean bidiFlag;
    private float red;
    private float blue;
    private float green;
    private float alpha;
    private int textColor;
    private boolean randomStyle;
    private boolean boldStyle;
    private boolean italicStyle;
    private boolean underlineStyle;
    private boolean strikethroughStyle;
    
    public FontRenderer(final GameSettings gameSettingsIn, final ResourceLocation location, final TextureManager textureManagerIn, final boolean unicode) {
        super(gameSettingsIn, location, textureManagerIn, unicode);
        this.field_78286_d = new int[256];
        this.field_78288_b = 9;
        this.field_78289_c = new Random();
        this.field_78287_e = new byte[65536];
        this.colorCode = new int[32];
        this.field_111273_g = location;
        this.renderEngine = textureManagerIn;
        this.unicodeFlag = unicode;
        this.bindTexture(this.field_111273_g);
        for (int i = 0; i < 32; ++i) {
            final int j = (i >> 3 & 0x1) * 85;
            int k = (i >> 2 & 0x1) * 170 + j;
            int l = (i >> 1 & 0x1) * 170 + j;
            int i2 = (i & 0x1) * 170 + j;
            if (i == 6) {
                k += 85;
            }
            if (gameSettingsIn.field_74337_g) {
                final int j2 = (k * 30 + l * 59 + i2 * 11) / 100;
                final int k2 = (k * 30 + l * 70) / 100;
                final int l2 = (k * 30 + i2 * 70) / 100;
                k = j2;
                l = k2;
                i2 = l2;
            }
            if (i >= 16) {
                k /= 4;
                l /= 4;
                i2 /= 4;
            }
            this.colorCode[i] = ((k & 0xFF) << 16 | (l & 0xFF) << 8 | (i2 & 0xFF));
        }
        this.readGlyphSizes();
    }
    
    public void func_110549_a(final IResourceManager resourceManager) {
        this.readFontTexture();
        this.readGlyphSizes();
    }
    
    private void readFontTexture() {
        BufferedImage bufferedimage;
        try {
            bufferedimage = TextureUtil.func_177053_a(this.getResourceInputStream(this.field_111273_g));
        }
        catch (IOException ioexception) {
            throw new RuntimeException(ioexception);
        }
        final int i = bufferedimage.getWidth();
        final int j = bufferedimage.getHeight();
        final int[] aint = new int[i * j];
        bufferedimage.getRGB(0, 0, i, j, aint, 0, i);
        final int k = j / 16;
        final int l = i / 16;
        final int i2 = 1;
        final float f = 8.0f / l;
        for (int j2 = 0; j2 < 256; ++j2) {
            final int k2 = j2 % 16;
            final int l2 = j2 / 16;
            if (j2 == 32) {
                this.field_78286_d[j2] = 3 + i2;
            }
            int i3;
            for (i3 = l - 1; i3 >= 0; --i3) {
                final int j3 = k2 * l + i3;
                boolean flag = true;
                for (int k3 = 0; k3 < k; ++k3) {
                    final int l3 = (l2 * l + k3) * i;
                    if ((aint[j3 + l3] >> 24 & 0xFF) != 0x0) {
                        flag = false;
                        break;
                    }
                }
                if (!flag) {
                    break;
                }
            }
            ++i3;
            this.field_78286_d[j2] = (int)(0.5 + i3 * f) + i2;
        }
    }
    
    private void readGlyphSizes() {
        InputStream inputstream = null;
        try {
            inputstream = this.getResourceInputStream(new ResourceLocation("font/glyph_sizes.bin"));
            inputstream.read(this.field_78287_e);
        }
        catch (IOException ioexception) {
            throw new RuntimeException(ioexception);
        }
        finally {
            IOUtils.closeQuietly(inputstream);
        }
    }
    
    private float func_181559_a(final char ch, final boolean italic) {
        if (ch == ' ') {
            return 4.0f;
        }
        final int i = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8?\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1??????????\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261¡À\u2265\u2264\u2320\u2321\u00f7\u2248¡ã\u2219¡¤\u221a\u207f?\u25a0\u0000".indexOf(ch);
        return (i != -1 && !this.unicodeFlag) ? this.func_78266_a(i, italic) : this.func_78277_a(ch, italic);
    }
    
    protected float func_78266_a(final int ch, final boolean italic) {
        final int i = ch % 16 * 8;
        final int j = ch / 16 * 8;
        final int k = italic ? 1 : 0;
        this.bindTexture(this.field_111273_g);
        final int l = this.field_78286_d[ch];
        final float f = l - 0.01f;
        GL11.glBegin(5);
        GL11.glTexCoord2f(i / 128.0f, j / 128.0f);
        GL11.glVertex3f(this.field_78295_j + k, this.field_78296_k, 0.0f);
        GL11.glTexCoord2f(i / 128.0f, (j + 7.99f) / 128.0f);
        GL11.glVertex3f(this.field_78295_j - k, this.field_78296_k + 7.99f, 0.0f);
        GL11.glTexCoord2f((i + f - 1.0f) / 128.0f, j / 128.0f);
        GL11.glVertex3f(this.field_78295_j + f - 1.0f + k, this.field_78296_k, 0.0f);
        GL11.glTexCoord2f((i + f - 1.0f) / 128.0f, (j + 7.99f) / 128.0f);
        GL11.glVertex3f(this.field_78295_j + f - 1.0f - k, this.field_78296_k + 7.99f, 0.0f);
        GL11.glEnd();
        return (float)l;
    }
    
    private ResourceLocation getUnicodePageLocation(final int page) {
        if (FontRenderer.unicodePageLocations[page] == null) {
            FontRenderer.unicodePageLocations[page] = new ResourceLocation(String.format("textures/font/unicode_page_%02x.png", page));
        }
        return FontRenderer.unicodePageLocations[page];
    }
    
    private void loadGlyphTexture(final int page) {
        this.bindTexture(this.getUnicodePageLocation(page));
    }
    
    protected float func_78277_a(final char ch, final boolean italic) {
        if (this.field_78287_e[ch] == 0) {
            return 0.0f;
        }
        final int i = ch / '\u0100';
        this.loadGlyphTexture(i);
        final int j = this.field_78287_e[ch] >>> 4;
        final int k = this.field_78287_e[ch] & 0xF;
        final float f = (float)j;
        final float f2 = (float)(k + 1);
        final float f3 = ch % '\u0010' * 16 + f;
        final float f4 = (float)((ch & '\u00ff') / 16 * 16);
        final float f5 = f2 - f - 0.02f;
        final float f6 = italic ? 1.0f : 0.0f;
        GL11.glBegin(5);
        GL11.glTexCoord2f(f3 / 256.0f, f4 / 256.0f);
        GL11.glVertex3f(this.field_78295_j + f6, this.field_78296_k, 0.0f);
        GL11.glTexCoord2f(f3 / 256.0f, (f4 + 15.98f) / 256.0f);
        GL11.glVertex3f(this.field_78295_j - f6, this.field_78296_k + 7.99f, 0.0f);
        GL11.glTexCoord2f((f3 + f5) / 256.0f, f4 / 256.0f);
        GL11.glVertex3f(this.field_78295_j + f5 / 2.0f + f6, this.field_78296_k, 0.0f);
        GL11.glTexCoord2f((f3 + f5) / 256.0f, (f4 + 15.98f) / 256.0f);
        GL11.glVertex3f(this.field_78295_j + f5 / 2.0f - f6, this.field_78296_k + 7.99f, 0.0f);
        GL11.glEnd();
        return (f2 - f) / 2.0f + 1.0f;
    }
    
    public int func_175063_a(final String text, final float x, final float y, final int color) {
        return this.func_175065_a(text, x, y, color, true);
    }
    
    public int func_78276_b(final String text, final int x, final int y, final int color) {
        return this.func_175065_a(text, (float)x, (float)y, color, false);
    }
    
    public int func_175065_a(final String text, final float x, final float y, final int color, final boolean dropShadow) {
        this.enableAlpha();
        this.resetStyles();
        int i;
        if (dropShadow) {
            i = this.renderString(text, x + 1.0f, y + 1.0f, color, true);
            i = Math.max(i, this.renderString(text, x, y, color, false));
        }
        else {
            i = this.renderString(text, x, y, color, false);
        }
        return i;
    }
    
    private String bidiReorder(final String text) {
        try {
            final Bidi bidi = new Bidi(new ArabicShaping(8).shape(text), 127);
            bidi.setReorderingMode(0);
            return bidi.writeReordered(2);
        }
        catch (ArabicShapingException var3) {
            return text;
        }
    }
    
    private void resetStyles() {
        this.randomStyle = false;
        this.boldStyle = false;
        this.italicStyle = false;
        this.underlineStyle = false;
        this.strikethroughStyle = false;
    }
    
    private void renderStringAtPos(final String text, final boolean shadow) {
        for (int i = 0; i < text.length(); ++i) {
            char c0 = text.charAt(i);
            if (c0 == '¡ì' && i + 1 < text.length()) {
                int i2 = "0123456789abcdefklmnor".indexOf(text.toLowerCase(Locale.ENGLISH).charAt(i + 1));
                if (i2 < 16) {
                    this.randomStyle = false;
                    this.boldStyle = false;
                    this.strikethroughStyle = false;
                    this.underlineStyle = false;
                    this.italicStyle = false;
                    if (i2 < 0 || i2 > 15) {
                        i2 = 15;
                    }
                    if (shadow) {
                        i2 += 16;
                    }
                    final int j1 = this.colorCode[i2];
                    this.textColor = j1;
                    this.setColor((j1 >> 16) / 255.0f, (j1 >> 8 & 0xFF) / 255.0f, (j1 & 0xFF) / 255.0f, this.alpha);
                }
                else if (i2 == 16) {
                    this.randomStyle = true;
                }
                else if (i2 == 17) {
                    this.boldStyle = true;
                }
                else if (i2 == 18) {
                    this.strikethroughStyle = true;
                }
                else if (i2 == 19) {
                    this.underlineStyle = true;
                }
                else if (i2 == 20) {
                    this.italicStyle = true;
                }
                else if (i2 == 21) {
                    this.randomStyle = false;
                    this.boldStyle = false;
                    this.strikethroughStyle = false;
                    this.underlineStyle = false;
                    this.italicStyle = false;
                    this.setColor(this.red, this.blue, this.green, this.alpha);
                }
                ++i;
            }
            else {
                int k = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8?\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1??????????\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261¡À\u2265\u2264\u2320\u2321\u00f7\u2248¡ã\u2219¡¤\u221a\u207f?\u25a0\u0000".indexOf(c0);
                if (this.randomStyle && k != -1) {
                    final int l = this.func_78263_a(c0);
                    char c2;
                    do {
                        k = this.field_78289_c.nextInt("\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8?\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1??????????\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261¡À\u2265\u2264\u2320\u2321\u00f7\u2248¡ã\u2219¡¤\u221a\u207f?\u25a0\u0000".length());
                        c2 = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8?\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1??????????\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261¡À\u2265\u2264\u2320\u2321\u00f7\u2248¡ã\u2219¡¤\u221a\u207f?\u25a0\u0000".charAt(k);
                    } while (l != this.func_78263_a(c2));
                    c0 = c2;
                }
                final float f1 = (k == -1 || this.unicodeFlag) ? 0.5f : 1.0f;
                final boolean flag = (c0 == '\0' || k == -1 || this.unicodeFlag) && shadow;
                if (flag) {
                    this.field_78295_j -= f1;
                    this.field_78296_k -= f1;
                }
                float f2 = this.func_181559_a(c0, this.italicStyle);
                if (flag) {
                    this.field_78295_j += f1;
                    this.field_78296_k += f1;
                }
                if (this.boldStyle) {
                    this.field_78295_j += f1;
                    if (flag) {
                        this.field_78295_j -= f1;
                        this.field_78296_k -= f1;
                    }
                    this.func_181559_a(c0, this.italicStyle);
                    this.field_78295_j -= f1;
                    if (flag) {
                        this.field_78295_j += f1;
                        this.field_78296_k += f1;
                    }
                    ++f2;
                }
                this.doDraw(f2);
            }
        }
    }
    
    protected void doDraw(final float f) {
        if (this.strikethroughStyle) {
            final Tessellator tessellator = Tessellator.func_178181_a();
            final WorldRenderer worldrenderer = tessellator.func_178180_c();
            GlStateManager.func_179090_x();
            worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
            worldrenderer.func_181662_b((double)this.field_78295_j, (double)(this.field_78296_k + this.field_78288_b / 2), 0.0).func_181675_d();
            worldrenderer.func_181662_b((double)(this.field_78295_j + f), (double)(this.field_78296_k + this.field_78288_b / 2), 0.0).func_181675_d();
            worldrenderer.func_181662_b((double)(this.field_78295_j + f), (double)(this.field_78296_k + this.field_78288_b / 2 - 1.0f), 0.0).func_181675_d();
            worldrenderer.func_181662_b((double)this.field_78295_j, (double)(this.field_78296_k + this.field_78288_b / 2 - 1.0f), 0.0).func_181675_d();
            tessellator.func_78381_a();
            GlStateManager.func_179098_w();
        }
        if (this.underlineStyle) {
            final Tessellator tessellator2 = Tessellator.func_178181_a();
            final WorldRenderer worldrenderer2 = tessellator2.func_178180_c();
            GlStateManager.func_179090_x();
            worldrenderer2.func_181668_a(7, DefaultVertexFormats.field_181705_e);
            final int l = this.underlineStyle ? -1 : 0;
            worldrenderer2.func_181662_b((double)(this.field_78295_j + l), (double)(this.field_78296_k + this.field_78288_b), 0.0).func_181675_d();
            worldrenderer2.func_181662_b((double)(this.field_78295_j + f), (double)(this.field_78296_k + this.field_78288_b), 0.0).func_181675_d();
            worldrenderer2.func_181662_b((double)(this.field_78295_j + f), (double)(this.field_78296_k + this.field_78288_b - 1.0f), 0.0).func_181675_d();
            worldrenderer2.func_181662_b((double)(this.field_78295_j + l), (double)(this.field_78296_k + this.field_78288_b - 1.0f), 0.0).func_181675_d();
            tessellator2.func_78381_a();
            GlStateManager.func_179098_w();
        }
        this.field_78295_j += (int)f;
    }
    
    private int renderStringAligned(final String text, int x, final int y, final int width, final int color, final boolean dropShadow) {
        if (this.bidiFlag) {
            final int i = this.func_78256_a(this.bidiReorder(text));
            x = x + width - i;
        }
        return this.renderString(text, (float)x, (float)y, color, dropShadow);
    }
    
    private int renderString(String text, final float x, final float y, int color, final boolean dropShadow) {
        if (text == null) {
            return 0;
        }
        if (this.bidiFlag) {
            text = this.bidiReorder(text);
        }
        if ((color & 0xFC000000) == 0x0) {
            color |= 0xFF000000;
        }
        if (dropShadow) {
            color = ((color & 0xFCFCFC) >> 2 | (color & 0xFF000000));
        }
        this.red = (color >> 16 & 0xFF) / 255.0f;
        this.blue = (color >> 8 & 0xFF) / 255.0f;
        this.green = (color & 0xFF) / 255.0f;
        this.alpha = (color >> 24 & 0xFF) / 255.0f;
        this.setColor(this.red, this.blue, this.green, this.alpha);
        this.field_78295_j = x;
        this.field_78296_k = y;
        this.renderStringAtPos(text, dropShadow);
        return (int)this.field_78295_j;
    }
    
    public int func_78256_a(final String text) {
        if (text == null) {
            return 0;
        }
        int i = 0;
        boolean flag = false;
        for (int j = 0; j < text.length(); ++j) {
            char c0 = text.charAt(j);
            int k = this.func_78263_a(c0);
            if (k < 0 && j < text.length() - 1) {
                ++j;
                c0 = text.charAt(j);
                if (c0 != 'l' && c0 != 'L') {
                    if (c0 == 'r' || c0 == 'R') {
                        flag = false;
                    }
                }
                else {
                    flag = true;
                }
                k = 0;
            }
            i += k;
            if (flag && k > 0) {
                ++i;
            }
        }
        return i;
    }
    
    public int func_78263_a(final char character) {
        if (character == '¡ì') {
            return -1;
        }
        if (character == ' ') {
            return 4;
        }
        final int i = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8?\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1??????????\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261¡À\u2265\u2264\u2320\u2321\u00f7\u2248¡ã\u2219¡¤\u221a\u207f?\u25a0\u0000".indexOf(character);
        if (character > '\0' && i != -1 && !this.unicodeFlag) {
            return this.field_78286_d[i];
        }
        if (this.field_78287_e[character] != 0) {
            final int j = this.field_78287_e[character] >>> 4;
            int k = this.field_78287_e[character] & 0xF;
            return (++k - j) / 2 + 1;
        }
        return 0;
    }
    
    public String func_78269_a(final String text, final int width) {
        return this.func_78262_a(text, width, false);
    }
    
    public String func_78262_a(final String text, final int width, final boolean reverse) {
        final StringBuilder stringbuilder = new StringBuilder();
        int i = 0;
        final int j = reverse ? (text.length() - 1) : 0;
        final int k = reverse ? -1 : 1;
        boolean flag = false;
        boolean flag2 = false;
        for (int l = j; l >= 0 && l < text.length() && i < width; l += k) {
            final char c0 = text.charAt(l);
            final int i2 = this.func_78263_a(c0);
            if (flag) {
                flag = false;
                if (c0 != 'l' && c0 != 'L') {
                    if (c0 == 'r' || c0 == 'R') {
                        flag2 = false;
                    }
                }
                else {
                    flag2 = true;
                }
            }
            else if (i2 < 0) {
                flag = true;
            }
            else {
                i += i2;
                if (flag2) {
                    ++i;
                }
            }
            if (i > width) {
                break;
            }
            if (reverse) {
                stringbuilder.insert(0, c0);
            }
            else {
                stringbuilder.append(c0);
            }
        }
        return stringbuilder.toString();
    }
    
    private String trimStringNewline(String text) {
        while (text != null && text.endsWith("\n")) {
            text = text.substring(0, text.length() - 1);
        }
        return text;
    }
    
    public void func_78279_b(String str, final int x, final int y, final int wrapWidth, final int textColor) {
        this.resetStyles();
        this.textColor = textColor;
        str = this.trimStringNewline(str);
        this.renderSplitString(str, x, y, wrapWidth, false);
    }
    
    private void renderSplitString(final String str, final int x, int y, final int wrapWidth, final boolean addShadow) {
        for (final String s : this.func_78271_c(str, wrapWidth)) {
            this.renderStringAligned(s, x, y, wrapWidth, this.textColor, addShadow);
            y += this.field_78288_b;
        }
    }
    
    public int func_78267_b(final String p_78267_1_, final int p_78267_2_) {
        return this.field_78288_b * this.func_78271_c(p_78267_1_, p_78267_2_).size();
    }
    
    public void func_78264_a(final boolean unicodeFlagIn) {
        this.unicodeFlag = unicodeFlagIn;
    }
    
    public boolean func_82883_a() {
        return this.unicodeFlag;
    }
    
    public void func_78275_b(final boolean bidiFlagIn) {
        this.bidiFlag = bidiFlagIn;
    }
    
    public List<String> func_78271_c(final String str, final int wrapWidth) {
        return Arrays.asList(this.func_78280_d(str, wrapWidth).split("\n"));
    }
    
    String func_78280_d(final String str, final int wrapWidth) {
        final int i = this.sizeStringToWidth(str, wrapWidth);
        if (str.length() <= i) {
            return str;
        }
        final String s = str.substring(0, i);
        final char c0 = str.charAt(i);
        final boolean flag = c0 == ' ' || c0 == '\n';
        final String s2 = getFormatFromString(s) + str.substring(i + (flag ? 1 : 0));
        return s + "\n" + this.func_78280_d(s2, wrapWidth);
    }
    
    private int sizeStringToWidth(final String str, final int wrapWidth) {
        final int i = str.length();
        int j = 0;
        int k = 0;
        int l = -1;
        boolean flag = false;
        while (k < i) {
            final char c0 = str.charAt(k);
            Label_0164: {
                switch (c0) {
                    case '\n': {
                        --k;
                        break Label_0164;
                    }
                    case ' ': {
                        l = k;
                        break;
                    }
                    case '¡ì': {
                        if (k >= i - 1) {
                            break Label_0164;
                        }
                        ++k;
                        final char c2 = str.charAt(k);
                        if (c2 == 'l' || c2 == 'L') {
                            flag = true;
                            break Label_0164;
                        }
                        if (c2 == 'r' || c2 == 'R' || isFormatColor(c2)) {
                            flag = false;
                        }
                        break Label_0164;
                    }
                }
                j += this.func_78263_a(c0);
                if (flag) {
                    ++j;
                }
            }
            if (c0 == '\n') {
                l = ++k;
                break;
            }
            if (j > wrapWidth) {
                break;
            }
            ++k;
        }
        return (k != i && l != -1 && l < k) ? l : k;
    }
    
    private static boolean isFormatColor(final char colorChar) {
        return (colorChar >= '0' && colorChar <= '9') || (colorChar >= 'a' && colorChar <= 'f') || (colorChar >= 'A' && colorChar <= 'F');
    }
    
    private static boolean isFormatSpecial(final char formatChar) {
        return (formatChar >= 'k' && formatChar <= 'o') || (formatChar >= 'K' && formatChar <= 'O') || formatChar == 'r' || formatChar == 'R';
    }
    
    public static String getFormatFromString(final String text) {
        StringBuilder s = new StringBuilder();
        int i = -1;
        final int j = text.length();
        while ((i = text.indexOf(167, i + 1)) != -1) {
            if (i < j - 1) {
                final char c0 = text.charAt(i + 1);
                if (isFormatColor(c0)) {
                    s = new StringBuilder("¡ì" + c0);
                }
                else {
                    if (!isFormatSpecial(c0)) {
                        continue;
                    }
                    s.append("¡ì").append(c0);
                }
            }
        }
        return s.toString();
    }
    
    public boolean func_78260_a() {
        return this.bidiFlag;
    }
    
    protected void setColor(final float r, final float g, final float b, final float a) {
        GlStateManager.func_179131_c(r, g, b, a);
    }
    
    protected void enableAlpha() {
        GlStateManager.func_179141_d();
    }
    
    protected void bindTexture(final ResourceLocation location) {
        this.renderEngine.func_110577_a(location);
    }
    
    protected InputStream getResourceInputStream(final ResourceLocation location) throws IOException {
        return Minecraft.func_71410_x().func_110442_L().func_110536_a(location).func_110527_b();
    }
    
    public int func_175064_b(final char character) {
        return this.colorCode["0123456789abcdef".indexOf(character)];
    }
    
    static {
        unicodePageLocations = new ResourceLocation[256];
    }
}
