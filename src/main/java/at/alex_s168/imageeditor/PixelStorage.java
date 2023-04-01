package at.alex_s168.imageeditor;

import at.alex_s168.imageeditor.api.AABB;
import at.alex_s168.imageeditor.api.Layer;
import at.alex_s168.imageeditor.api.PixelMap;
import at.alex_s168.imageeditor.util.BlendingUtil;
import de.m_marvin.univec.impl.Vec2i;
import org.lwjgl.opengl.GL33;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class PixelStorage {

    private final List<Layer> layers = new ArrayList<>();
    private final Queue<Integer> updateQueue = new LinkedList<>();

    public final AABB visibleAABB = new AABB(new Vec2i(0,0), new Vec2i(0,0));

    private int currentLayer;

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
        layers.add( new Layer(m, initGL()) );
        updateLayerTexture( layers.get(layers.size()-1) );
    }

    private void renderLayer(int x, int y, int w, int h, int textureID) {
        GL33.glBindTexture(GL33.GL_TEXTURE_2D, textureID);

        GL33.glColor4f(1, 1, 1, 1f);

        GL33.glBegin(GL33.GL_QUADS);
        GL33.glTexCoord2f(0, 0);
        GL33.glVertex2f(x, y);
        GL33.glTexCoord2f(1, 0);
        GL33.glVertex2f(x + w, y);
        GL33.glTexCoord2f(1, 1);
        GL33.glVertex2f(x + w, y + h);
        GL33.glTexCoord2f(0, 1);
        GL33.glVertex2f(x, y + h);
        GL33.glEnd();
    }

    private void updateTexture(int[] pixels, int width, int height, int txid) {
        GL33.glBindTexture(GL33.GL_TEXTURE_2D, txid);
        GL33.glTexImage2D(GL33.GL_TEXTURE_2D, 0, GL33.GL_RGBA8, width, height, 0,  GL33.GL_BGRA, GL33.GL_UNSIGNED_INT_8_8_8_8_REV, pixels);
        GL33.glBindTexture(GL33.GL_TEXTURE_2D, txid);
    }

    private void updateLayerTexture(Layer l) {
        PixelMap m = l.pixelMap;
        updateTexture(m.pix, m.getWidth(), m.getHeight(), l.textureID);
    }

    public void updateCurrent() {
        updateQueue.add(currentLayer);
    }

    public void tick(int x, int y, double scale) {

        for(int layer : updateQueue) {
            updateLayerTexture(layers.get(layer));
        }
        updateQueue.clear();

        for(Layer l : layers) {
            if(l.visible) {
                PixelMap m = l.pixelMap;
                int w = (int) (m.getWidth() * scale);
                int h = (int) (m.getHeight() * scale);
                renderLayer((int) ((x+m.pos.x)*scale), (int) ((y+m.pos.y)*scale), w, h, l.textureID);

                visibleAABB.maxI(m.getBoundsPositional());
            }
        }

    }

    private int initGL() {
        int t = GL33.glGenTextures();

        GL33.glEnable(GL33.GL_TEXTURE_2D);
        GL33.glBindTexture(GL33.GL_TEXTURE_2D, t);
        GL33.glTexParameterf(GL33.GL_TEXTURE_2D, GL33.GL_TEXTURE_MIN_FILTER, GL33.GL_NEAREST);
        GL33.glTexParameterf(GL33.GL_TEXTURE_2D, GL33.GL_TEXTURE_MAG_FILTER, GL33.GL_NEAREST);
        GL33.glBindTexture(GL33.GL_TEXTURE_2D, t);

        return t;

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
