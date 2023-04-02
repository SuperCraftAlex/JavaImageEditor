package at.alex_s168.imageeditor;

import at.alex_s168.imageeditor.api.AABB;
import at.alex_s168.imageeditor.api.Layer;
import at.alex_s168.imageeditor.api.PixelMap;
import at.alex_s168.imageeditor.util.BlendingUtil;
import at.alex_s168.imageeditor.util.GLHelper;
import de.m_marvin.univec.impl.Vec2i;
import org.lwjgl.opengl.GL33;

import java.util.ArrayList;
import java.util.List;

public class PixelStorage {

    private final List<Layer> layers = new ArrayList<>();

    public final AABB visibleAABB = new AABB(new Vec2i(0,0), new Vec2i(0,0));

    private int currentLayer;

    private int textureID;

    private boolean toUpdate = false;

    private PixelMap mOut = new PixelMap();


    public static PixelStorage getSelf() {
        return ImageEditor.getInstance().getPixelStorage();
    }

    public PixelMap getCurrentPixelMap() {
        return layers.get(currentLayer).pixelMap;
    }

    public void reset(PixelMap m) {
        layers.clear();

        addLayer(m);

        currentLayer = 0;
    }

    public void addLayer(PixelMap m) {
        layers.add( new Layer(m) );
    }

    private void renderMap(int x, int y, int w, int h) {
        GLHelper.renderMap(x,y,w,h,textureID);
    }

    private void updateTexture() {
        mOut = overlayVisible();
        GLHelper.updateTexture(mOut.pix, mOut.getWidth(), mOut.getHeight(), textureID);
    }

    public void update() {
        toUpdate = true;
    }

    public void tick(int x, int y, double scale) {

        //todo: smart overlaying

        if(toUpdate) {
            updateTexture();
            toUpdate = false;
        }

        for(Layer l : layers) {
            if(l.visible) {
                visibleAABB.maxI(l.pixelMap.getBoundsPositional());
            }
        }

        renderMap((int) ((x+mOut.pos.x)*scale), (int) ((y+mOut.pos.y)*scale), (int) (visibleAABB.getWidth()*scale), (int) (visibleAABB.getHeight()*scale));

    }

    public void initGL() {
        textureID = GL33.glGenTextures();

        GL33.glEnable(GL33.GL_TEXTURE_2D);
        GL33.glBindTexture(GL33.GL_TEXTURE_2D, textureID);
        GL33.glTexParameterf(GL33.GL_TEXTURE_2D, GL33.GL_TEXTURE_MIN_FILTER, GL33.GL_NEAREST);
        GL33.glTexParameterf(GL33.GL_TEXTURE_2D, GL33.GL_TEXTURE_MAG_FILTER, GL33.GL_NEAREST);
        GL33.glBindTexture(GL33.GL_TEXTURE_2D, 0);
    }

    public PixelMap overlayVisible() {
        PixelMap m = new PixelMap(visibleAABB.getWidth(), visibleAABB.getHeight());
        for(Layer l : layers) {
            if(l.visible)
                m = BlendingUtil.blend(l.pixelMap, m);
        }
        return m;
    }

}
