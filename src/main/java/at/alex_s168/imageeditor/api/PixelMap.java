package at.alex_s168.imageeditor.api;

import at.alex_s168.imageeditor.features.image.ImageMode;
import de.m_marvin.univec.impl.Vec2d;
import de.m_marvin.univec.impl.Vec2i;

public class PixelMap {

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    protected int width;
    protected int height;

    public Vec2i pos = new Vec2i(0,0);

    public ImageMode mode;
    public BlendMode blendMode = BlendMode.NORMAL;

    public int[] pix;
    public int[] alpha;

    public PixelMap(int widthIn, int heightIn) {
        this.width = widthIn;
        this.height = heightIn;

        this.mode  = ImageMode.RGB;
        this.pix = new int[widthIn * heightIn];
    }

    public PixelMap(int widthIn, int heightIn, int[] pixIn) {
        this.width = widthIn;
        this.height = heightIn;

        this.pix = pixIn;
        this.alpha = new int[pixIn.length];

        this.mode  = ImageMode.RGB;
    }

    public PixelMap(int widthIn, int heightIn, int[] pixIn, Vec2i posIn) {
        this.width = widthIn;
        this.height = heightIn;

        this.pix = pixIn;
        this.alpha = new int[pixIn.length];

        this.mode  = ImageMode.RGB;
        this.pos = posIn;
    }

    public PixelMap() {
        this.width = 0;
        this.height = 0;

        this.pix = new int[]{0};
        this.alpha = new int[]{0};

        this.mode  = ImageMode.RGB;
    }

    public int get(int x, int y) {
        return pix[x+y*width];
    }

    public int getPositional(int x, int y) {
        return pix[(x - pos.x)+(y - pos.y)*width];
    }

    public void set(int x, int y, int val) {
        if(x<= width && y <= height && x>0 && y>0)
            pix[x+y*width] = val;
    }

    public void set(double x, double y, int val) {
        if(x<= width && y <= height && x>0 && y>0)
            pix[(int)x+(int)y*width] = val;
    }

    public void set(Vec2d p, int val) {
        if(p.x<= width && p.y <= height && p.x>0 && p.y>0)
            pix[(int)p.x+(int)p.y*width] = val;
    }

    public void set(Vec2i p, int val) {
        if(p.x<= width && p.y <= height && p.x>0 && p.y>0)
            pix[p.x+p.y*width] = val;
    }

    // + AABB
    public int get(int x, int y, AABB aabb) {
        return pix[((int)aabb.A.x+x)+((int)aabb.A.y+y)*width];
    }

    public AABB getBounds() {
        return new AABB(new Vec2i(0,0), new Vec2i(width, height));
    }

    public AABB getBoundsPositional() {
        return new AABB(pos, pos.add(width, height));
    }

}
