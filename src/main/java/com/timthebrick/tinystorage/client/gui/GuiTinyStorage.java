package com.timthebrick.tinystorage.client.gui;

import codechicken.nei.VisiblityData;
import codechicken.nei.api.INEIGuiHandler;
import codechicken.nei.api.TaggedInventoryArea;
import com.timthebrick.tinystorage.TinyStorage;
import com.timthebrick.tinystorage.client.gui.widgets.*;
import com.timthebrick.tinystorage.client.gui.widgets.settings.*;
import com.timthebrick.tinystorage.util.client.Colours;
import com.timthebrick.tinystorage.common.reference.Messages;
import com.timthebrick.tinystorage.common.tileentity.TileEntityTinyStorage;
import com.timthebrick.tinystorage.network.PacketHandler;
import com.timthebrick.tinystorage.network.message.MessageConfigButton;
import com.timthebrick.tinystorage.network.message.MessagePlayerJoinGui;
import com.timthebrick.tinystorage.network.message.MessagePlayerLeaveGui;
import com.timthebrick.tinystorage.util.common.UUIDHelper;
import com.timthebrick.tinystorage.util.client.colour.Colour;
import cpw.mods.fml.common.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Optional.InterfaceList({@Optional.Interface(iface = "codechicken.nei.api.INEIGuiHandler", modid = "NotEnoughItems")})
public class GuiTinyStorage extends GuiContainer implements IContainerWidgetProvider, INEIGuiHandler {

    protected Container container;
    private GuiImageButton accessMode;
    private GuiTabbedPane friendsPanel;
    private TileEntityTinyStorage tileEntity;
    protected List<IGuiWidgetAdvanced> widgets = new ArrayList<IGuiWidgetAdvanced>();
    protected List<IGuiAnimation> animations = new ArrayList<IGuiAnimation>();

    protected GuiTinyStorage(Container container, TileEntityTinyStorage te) {
        super(container);
        this.container = container;
        this.tileEntity = te;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        Colour.resetGLColour();
        boolean hasClicked = Mouse.isButtonDown(0);
        for (IGuiWidgetAdvanced widget : widgets) {
            widget.updateGraphics();
            if (widget instanceof IGuiWidgetBackground) {
                widget.drawWidget(this, xSize, ySize);
            }
            if (widget instanceof GuiTabbedPane) {
                ((GuiTabbedPane) widget).drawScreen(mouseX, mouseY, partialTicks);
            }
        }
        Colour.resetGLColour();
        for (Object c : this.buttonList) {
            if (c instanceof IButtonTooltip) {
                IButtonTooltip tooltip = (IButtonTooltip) c;
                int x = tooltip.xPos(); // ((GuiImgButton) c).xPosition;
                int y = tooltip.yPos(); // ((GuiImgButton) c).yPosition;
                if (x < mouseX && x + tooltip.getWidth() > mouseX && tooltip.isVisible()) {
                    if (y < mouseY && y + tooltip.getHeight() > mouseY) {
                        if (y < 15) {
                            y = 15;
                        }
                        String msg = tooltip.getMessage();
                        if (msg != null) {
                            this.drawTooltip(x + 11, y + 4, 0, msg);
                            Colour.resetGLColour();
                        }
                    }
                }
            }
        }
        Colour.resetGLColour();
        for (Object c : this.widgets) {
            if (c instanceof IWidgetTooltip) {
                IWidgetTooltip tooltip = (IWidgetTooltip) c;
                int x = tooltip.xTriggerPos(); // ((GuiImgButton) c).xPosition;
                int y = tooltip.yTriggerPos(); // ((GuiImgButton) c).yPosition;
                if (x < mouseX && x + tooltip.getTooltipWidth() > mouseX && tooltip.isTooltipVisible()) {
                    if (y < mouseY && y + tooltip.getTooltipHeight() > mouseY) {
                        if (y < 15) {
                            y = 15;
                        }
                        String msg = tooltip.getMessage();
                        if (msg != null) {
                            this.drawTooltip(x + 11, y + 4, 0, msg);
                            Colour.resetGLColour();
                        }
                    }
                }
            }
        }
        Colour.resetGLColour();
    }

    @Override
    public void updateScreen() {
        for (IGuiWidgetAdvanced widget : widgets) {
            widget.updateWidget();
        }
        super.updateScreen();
    }

    private void drawTooltip(int x, int y, int forceWidth, String Msg) {
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        String[] var4 = Msg.split("\n");
        if (var4.length > 0) {
            int var5 = 0;
            int var6;
            int var7;
            for (var6 = 0; var6 < var4.length; ++var6) {
                var7 = this.fontRendererObj.getStringWidth(var4[var6]);
                if (var7 > var5) {
                    var5 = var7;
                }
            }
            var6 = x + 5;
            var7 = y - 5;
            int var9 = 8;
            if (var4.length > 1) {
                var9 += 2 + (var4.length - 1) * 10;
            }
            if (this.guiTop + var7 + var9 + 6 > this.height) {
                var7 = this.height - var9 - this.guiTop - 6;
            }
            if (forceWidth > 0) {
                var5 = forceWidth;
            }
            this.zLevel = 300.0F;
            itemRender.zLevel = 300.0F;
            int var10 = -267386864;
            this.drawGradientRect(var6 - 3, var7 - 4, var6 + var5 + 3, var7 - 3, var10, var10);
            this.drawGradientRect(var6 - 3, var7 + var9 + 3, var6 + var5 + 3, var7 + var9 + 4, var10, var10);
            this.drawGradientRect(var6 - 3, var7 - 3, var6 + var5 + 3, var7 + var9 + 3, var10, var10);
            this.drawGradientRect(var6 - 4, var7 - 3, var6 - 3, var7 + var9 + 3, var10, var10);
            this.drawGradientRect(var6 + var5 + 3, var7 - 3, var6 + var5 + 4, var7 + var9 + 3, var10, var10);
            int var11 = 1347420415;
            int var12 = (var11 & 16711422) >> 1 | var11 & -16777216;
            this.drawGradientRect(var6 - 3, var7 - 3 + 1, var6 - 3 + 1, var7 + var9 + 3 - 1, var11, var12);
            this.drawGradientRect(var6 + var5 + 2, var7 - 3 + 1, var6 + var5 + 3, var7 + var9 + 3 - 1, var11, var12);
            this.drawGradientRect(var6 - 3, var7 - 3, var6 + var5 + 3, var7 - 3 + 1, var11, var11);
            this.drawGradientRect(var6 - 3, var7 + var9 + 2, var6 + var5 + 3, var7 + var9 + 3, var12, var12);
            for (int var13 = 0; var13 < var4.length; ++var13) {
                String var14 = var4[var13];
                if (var13 == 0) {
                    var14 = '\u00a7' + Integer.toHexString(15) + var14;
                } else {
                    var14 = "\u00a77" + var14;
                }
                this.fontRendererObj.drawStringWithShadow(var14, var6, var7, -1);
                if (var13 == 0) {
                    var7 += 2;
                }
                var7 += 10;
            }
            this.zLevel = 0.0F;
            itemRender.zLevel = 0.0F;
        }
        GL11.glPopAttrib();
        RenderHelper.enableStandardItemLighting();
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        Colour.resetGLColour();
    }

    @Override
    public void onGuiClosed() {
        PacketHandler.INSTANCE.sendToServer(new MessagePlayerLeaveGui(this.getMinecraft().thePlayer, tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord));
        super.onGuiClosed();
    }

    protected void handleButtonVisibility() {
        if (this.accessMode != null) {
            this.accessMode.setVisibility(true);
        }
    }

    @Override
    public void handleWidgetVisibility() {
        if (this.friendsPanel != null) {
            this.friendsPanel.setVisibility(true);
        }
    }

    @Override
    public void initGui() {
        super.initGui();
        PacketHandler.INSTANCE.sendToServer(new MessagePlayerJoinGui(this.getMinecraft().thePlayer, tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord));
        this.addButtons();
        this.addWidgets();
    }

    @Override
    public void addWidgets() {
        if (this.friendsPanel == null) {
            if (tileEntity.hasUniqueOwner()) {
                if (tileEntity.getUniqueOwner().equals(Minecraft.getMinecraft().thePlayer.getGameProfile().getId().toString() + Minecraft.getMinecraft().thePlayer.getDisplayName())) {
                    this.friendsPanel = new GuiTabbedPane(this, new TabHandlerFriendsList(), getXSize() + 2, 8, 112, 170, 12, 12, 0, 0, 24, 0);
                    this.addWidget(friendsPanel);
                    GuiLabel searchLabel = new GuiLabel.GuiLabelTabbed(this, friendsPanel, fontRendererObj, 2, friendsPanel.getButtonHeight() + 1, friendsPanel.getWidth() - 3, fontRendererObj.FONT_HEIGHT + 1, Messages.GuiLabels.FRIENDS_LIST, new Colour(Colours.INV_GRAY));
                    friendsPanel.addContainedWidget(searchLabel);
                    GuiTextInput search = new GuiTextInput.GuiTextInputTabbed(this, friendsPanel, this.fontRendererObj, 3, friendsPanel.getButtonHeight() + searchLabel.getHeight() + 1, friendsPanel.getWidth() - 6, 10, CharFilters.FILTER_ALPHANUMERIC);
                    friendsPanel.addContainedWidget(search);
                    GuiTextList playerList = new GuiFriendsList(this, friendsPanel, this.fontRendererObj, search.getXOrigin(), search.getYOrigin() + search.getHeight() + 4, friendsPanel.getWidth() - 15, 3 + (fontRendererObj.FONT_HEIGHT * 14), UUIDHelper.getStringFromMap(TinyStorage.instance.playerUUIDMap), search);
                    friendsPanel.addContainedWidget(playerList);
                    GuiImageButton prev = new GuiImageButton(playerList.xPos() + playerList.getWidth() + 2, playerList.yPos() - 1, ButtonSettings.UP, EnableMode.ENABLED, true);
                    prev.visible = false;
                    friendsPanel.addContainedButton(prev);
                    GuiImageButton next = new GuiImageButton(playerList.xPos() + playerList.getWidth() + 2, playerList.yPos() + playerList.getHeight() - 7, ButtonSettings.DOWN, EnableMode.ENABLED, true);
                    next.visible = false;
                    friendsPanel.addContainedButton(next);
                }
            }
        } else {
            this.friendsPanel.adjustPosition();
        }
    }

    protected void addButtons() {
        this.buttonList.remove(accessMode);
        this.accessMode = new GuiImageButton(this.guiLeft - 18, this.guiTop + 8, ButtonSettings.AUTOMATED_SIDE_ACCESS, AccessMode.DISABLED);
        if (tileEntity.hasUniqueOwner()) {
            if (tileEntity.getUniqueOwner().equals(Minecraft.getMinecraft().thePlayer.getGameProfile().getId().toString() + Minecraft.getMinecraft().thePlayer.getDisplayName())) {
                this.buttonList.add(accessMode);
            }
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y) {
        int ox = this.guiLeft; // (width - xSize) / 2;
        int oy = this.guiTop; // (height - ySize) / 2;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.drawFG(ox, oy, x, y);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float opacity, int x, int y) {
        this.drawBG(0, 0, x, y);
    }

    protected void drawFG(int ox, int oy, int x, int y) {
        if (this.accessMode != null) {
            this.accessMode.set(this.tileEntity.accessMode);
        }
        for (IGuiWidgetAdvanced widget : this.widgets) {
            if (widget instanceof IGuiWidgetBackground) {
                continue;
            }
            if (widget.isVisible()) {
                widget.drawWidget(this, xSize, ySize);
            }
        }
        for (IGuiAnimation animation : this.animations) {
            if (animation.isVisible()) {
                animation.drawAnimationBackground(this, xSize, ySize);
            }
        }
        for (IGuiAnimation animation : this.animations) {
            if (animation.isVisible()) {
                animation.drawAnimationForeground(this, xSize, ySize);
            }
        }
    }

    protected void drawBG(int ox, int oy, int x, int y) {
        this.handleButtonVisibility();
        this.handleWidgetVisibility();
    }

    @Override
    public void handleMouseInput() {
        super.handleMouseInput();
        int x = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int y = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
        int b = Mouse.getEventButton();
        int delta = Mouse.getEventDWheel();
        if (delta != 0) {
            mouseWheelEvent(x, y, delta);
        }
    }

    @Override
    protected void mouseClicked(int xCoord, int yCoord, int btn) {
        if (!widgets.isEmpty()) {
            for (IGuiWidgetAdvanced widget : widgets) {
                widget.onMouseClick(xCoord, yCoord, btn);
            }
        }
        if (btn == 1) {
            for (Object o : this.buttonList) {
                GuiButton guibutton = (GuiButton) o;
                if (guibutton.mousePressed(this.mc, xCoord, yCoord)) {
                    super.mouseClicked(xCoord, yCoord, 0);
                    return;
                }
            }
        }
        super.mouseClicked(xCoord, yCoord, btn);
    }

    @Override
    protected void mouseMovedOrUp(int x, int y, int button) {
        super.mouseMovedOrUp(x, y, button);
    }

    @Override
    protected void mouseClickMove(int x, int y, int button, long time) {
        super.mouseClickMove(x, y, button, time);
    }

    protected void mouseWheelEvent(int x, int y, int delta) {
        if (!widgets.isEmpty()) {
            for (IGuiWidgetAdvanced widget : widgets) {
                widget.mouseWheel(x, y, delta);
            }
        }
    }

    @Override
    protected void keyTyped(char c, int key) {
        boolean keyCaptured = false;
        GuiTextInput guiTextInput = null;
        for (IGuiWidgetAdvanced widget : widgets) {
            widget.keyTyped(c, key);
            if (widget instanceof IGuiWidgetContainer) {
                keyCaptured = ((IGuiWidgetContainer) widget).getKeyCaptured();
                if (keyCaptured) {
                    return;
                }
            }
        }
        for (IGuiWidgetAdvanced widget : widgets) {
            if (widget instanceof GuiTextInput) {
                if (((GuiTextInput) widget).isFocused()) {
                    guiTextInput = (GuiTextInput) widget;
                }
            }
        }
        if (key == 1) {
            if (guiTextInput != null && key == 1) {
                guiTextInput.setFocused(false);
                return;
            }
        }
        if (c == '\t') {
            if (guiTextInput != null && c == '\t') {
                guiTextInput.setFocused(false);
                return;
            }
        }
        if (guiTextInput != null) {
            String old = guiTextInput.getText();
            if (guiTextInput.textboxKeyTyped(c, key)) {
                onTextFieldChanged(guiTextInput, old);
                return;
            }
        }
        if (c == 'f') {
            for (IGuiWidgetAdvanced widget : widgets) {
                if (widget instanceof GuiTextInput) {
                    if (!((GuiTextInput) widget).isFocused()) {
                        guiTextInput = (GuiTextInput) widget;
                        guiTextInput.setFocused(true);
                        keyCaptured = true;
                        break;
                    }
                }
            }
        }
        if (!keyCaptured) {
            super.keyTyped(c, key);
        }
    }

    private void onTextFieldChanged(GuiTextInput guiTextInput, String old) {
    }

    @Override
    public void addWidget(IGuiWidgetSimple widget) {
        if (widget instanceof IGuiAnimation) {
            animations.add((IGuiAnimation) widget);
            widgets.add((IGuiWidgetAdvanced) widget);
            ((IGuiWidgetAdvanced) widget).adjustPosition();
        } else {
            widgets.add((IGuiWidgetAdvanced) widget);
            ((IGuiWidgetAdvanced) widget).adjustPosition();
        }
    }

    @Override
    public void removeWidget(IGuiWidgetSimple widget) {
        if (widget instanceof IGuiAnimation) {
            animations.remove(widget);
        }
        widgets.remove(widget);
    }

    @Override
    public void handleWidgetFunctionality(IGuiWidgetAdvanced widget) {
    }

    @Override
    public Minecraft getMinecraft() {
        return this.mc;
    }

    @Override
    public TileEntity getTileEntity() {
        return this.tileEntity;
    }

    @Override
    public int getGuiLeft() {
        return guiLeft;
    }

    @Override
    public int getGuiTop() {
        return guiTop;
    }

    @Override
    public int getXSize() {
        return xSize;
    }

    @Override
    public int getYSize() {
        return ySize;
    }

    @Override
    public GuiScreen getGuiScreen() {
        return this;
    }

    @Override
    protected void actionPerformed(GuiButton btn) {
        super.actionPerformed(btn);
        boolean backwards = Mouse.isButtonDown(1);
        if (btn == this.accessMode) {
            PacketHandler.INSTANCE.sendToServer(new MessageConfigButton(Minecraft.getMinecraft().thePlayer, this.accessMode.getSetting(), backwards, this.tileEntity.xCoord, this.tileEntity.yCoord, this.tileEntity.zCoord));
        }
    }

    public void bindTexture(String base, String file) {
        ResourceLocation loc = new ResourceLocation(base, "textures/" + file);
        this.mc.getTextureManager().bindTexture(loc);
    }

    @Override
    public int getInvLeft() {
        return 0;
    }

    @Override
    public int getInvTop() {
        return 0;
    }

    @Override
    public int getInvWidth() {
        return 0;
    }

    @Override
    public int getInvHeight() {
        return 0;
    }

    @Override
    @Optional.Method(modid = "NotEnoughItems")
    public VisiblityData modifyVisiblity(GuiContainer gui, VisiblityData currentVisibility) {
        return null;
    }

    @Override
    @Optional.Method(modid = "NotEnoughItems")
    public Iterable<Integer> getItemSpawnSlots(GuiContainer gui, ItemStack item) {
        return null;
    }

    @Override
    @Optional.Method(modid = "NotEnoughItems")
    public List<TaggedInventoryArea> getInventoryAreas(GuiContainer gui) {
        return null;
    }

    @Override
    @Optional.Method(modid = "NotEnoughItems")
    public boolean handleDragNDrop(GuiContainer gui, int mousex, int mousey, ItemStack draggedStack, int button) {
        return false;
    }

    @Override
    @Optional.Method(modid = "NotEnoughItems")
    public boolean hideItemPanelSlot(GuiContainer gui, int x, int y, int w, int h) {
        Rectangle area = new Rectangle(x, y, w, h);
        for (IGuiWidgetAdvanced widget : widgets) {
            if ((area.intersects(widget.getWidgetVisibleAreaAbsolute()) && widget.isVisible())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public FontRenderer getFontRenderer() {
        return this.fontRendererObj;
    }

    @Override
    public RenderItem getItemRenderer() {
        return this.itemRender;
    }
}
