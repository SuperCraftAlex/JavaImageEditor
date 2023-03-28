package at.alex_s168.imageeditor.api;

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

    public int[] pix;

    public PixelMap(int widthIn, int heightIn) {
        this.width = widthIn;
        this.height = heightIn;
    }

    public PixelMap(int widthIn, int heightIn, int[] pixIn) {
        this.width = widthIn;
        this.height = heightIn;
        this.pix = pixIn;
    }

    public int get(int x, int y) {
        return pix[x+y*width];
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
        return new AABB(new Vec2d(0,0), new Vec2d(width, height));
    }

    public PixelMap() {
        this.width = 0;
        this.height = 0;
        this.pix = new int[]{0};
    }

}
