package at.alex_s168.imageeditor.ui;

import at.alex_s168.imageeditor.ImageEditor;
import at.alex_s168.imageeditor.PixelStorage;
import at.alex_s168.imageeditor.api.AABB;
import at.alex_s168.imageeditor.api.PixelMap;
import at.alex_s168.imageeditor.features.keybinds.FeatureKeybinds;
import at.alex_s168.imageeditor.util.GLHelper;
import at.alex_s168.imageeditor.util.VecUtil;
import de.m_marvin.logicsim.ui.TextRenderer;
import de.m_marvin.univec.impl.Vec2i;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL33;
import org.lwjgl.opengl.GLCapabilities;

import java.util.ArrayList;
import java.util.List;

import static at.alex_s168.imageeditor.util.GLHelper.drawRectangleNew;

public class UIPreviewWindow extends Canvas {

    protected GLData glData;
    protected GLCanvas glCanvas;
    protected GLCapabilities glCapabilities;
    protected boolean resized;
    protected boolean initialized = false;

    public double scale = 1;

    private int textureID;

    public PixelMap map = new PixelMap();

    private boolean toUpdate = false;

    public UIPreviewWindow(Composite parent) {
        super(parent, SWT.NONE);
        this.setLayout(new FillLayout());
        this.glData = new GLData();
        this.glData.doubleBuffer = true;
        this.glCanvas = new GLCanvas(this, SWT.None, glData);
        this.glCanvas.addListener(SWT.Resize, (event) -> this.resized = true);
        initOpenGL();
    }

    protected void initOpenGL() {
        this.glCanvas.setCurrent();
        this.glCapabilities = GL.createCapabilities();

        GL33.glMatrixMode(GL33.GL_PROJECTION);
        GL33.glLoadIdentity();
        GL33.glOrtho(0, 10_000, 10_000, 0, 0, 1);
        GL33.glEnable(GL33.GL_BLEND);
        GL33.glBlendFunc(GL33.GL_SRC_ALPHA, GL33.GL_ONE_MINUS_SRC_ALPHA);
        GL33.glDisable(GL33.GL_DEPTH_TEST);
        GL33.glDisable(GL33.GL_CULL_FACE);

        textureID = GL33.glGenTextures();

        GL33.glEnable(GL33.GL_TEXTURE_2D);
        GL33.glBindTexture(GL33.GL_TEXTURE_2D, textureID);
        GL33.glTexParameterf(GL33.GL_TEXTURE_2D, GL33.GL_TEXTURE_MIN_FILTER, GL33.GL_NEAREST);
        GL33.glTexParameterf(GL33.GL_TEXTURE_2D, GL33.GL_TEXTURE_MAG_FILTER, GL33.GL_NEAREST);
        GL33.glBindTexture(GL33.GL_TEXTURE_2D, 0);

        GL33.glEnable(GL33.GL_BLEND);
        GL33.glBlendFunc(GL33.GL_SRC_ALPHA, GL33.GL_ONE_MINUS_SRC_ALPHA);

        this.glCanvas.addDisposeListener((e) -> TextRenderer.cleanUpOpenGL());
        this.resized = true;
        this.initialized = true;
    }

    public void render() {
        if (this.isDisposed() || this.glCanvas == null) return;

        if (!initialized) {
            initOpenGL();
        }

        GL33.glClear(GL33.GL_COLOR_BUFFER_BIT);

        if (resized) {
            GL11.glViewport(0, 0, this.getSize().x, this.getSize().y);
            GL11.glLoadIdentity();
            GL11.glOrtho(0.0, this.getSize().x, this.getSize().y, 0.0, 0.0, 1.0);
            GL11.glClearColor(0, 0, 0, 1);
        }

        int x = 0;
        int y = 0;

        if(toUpdate) {
            GLHelper.updateTexture(map.pix, map.getWidth(), map.getHeight(), textureID);
            toUpdate = false;
        }

        GLHelper.renderMap((int) ((x+map.pos.x)*scale), (int) ((y+map.pos.y)*scale), (int) (map.getWidth()*scale), (int) (map.getHeight()*scale), textureID);

        GL33.glBindTexture(GL33.GL_TEXTURE_2D, 0);

        glCanvas.swapBuffers();
    }

    public void update() {
        toUpdate = true;
    }

    public static EditorArea getSelf() {
        return ImageEditor.getInstance().getEditor().editorArea;
    }

}