package at.alex_s168.imageeditor.util;

import de.m_marvin.univec.impl.Vec2d;
import de.m_marvin.univec.impl.Vec2f;
import de.m_marvin.univec.impl.Vec2i;
import de.m_marvin.univec.impl.Vec3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL33;

public class GLHelper {

    public static void drawRectangle(float width, float x, float y, float w, float h) {
        GL11.glLineWidth(width);
        GL11.glBegin(GL11.GL_LINE_STRIP);
        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x + w, y);
        GL11.glVertex2f(x + w, y + h);
        GL11.glVertex2f(x, y + h);
        GL11.glVertex2f(x, y);
        GL11.glEnd();
    }

    public static void drawLine(float width, int x, int y, int x2, int y2) {
        GL11.glLineWidth(width);
        GL11.glBegin(GL11.GL_LINE_STRIP);
        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x2, y2);
        GL11.glEnd();
    }

    public static void drawPoint(float size, int x, int y) {
        GL11.glPointSize(size);
        GL11.glBegin(GL11.GL_POINTS);
        GL11.glVertex2f(x, y);
        GL11.glEnd();
    }

    public static void drawCircle(float width, float x, float y, float r) {
        GL11.glLineWidth(width);
        GL11.glBegin(GL11.GL_LINE_STRIP);
        int segmentCount = 10;
        for (int a = 0; a <= 360; a += 360 / segmentCount) {
            double cx = Math.sin(Math.toRadians(a)) * r;
            double cy = Math.cos(Math.toRadians(a)) * r;
            GL11.glVertex2d(x + cx, y + cy);
        }
        GL11.glEnd();
    }

    // vec as arg

    public static void drawRectangle(float width, Vec2d v, float w, float h) {
        Vec2f vf = new Vec2f((float) v.x, (float) v.y);

        GL11.glLineWidth(width);
        GL11.glBegin(GL11.GL_LINE_STRIP);
        GL11.glVertex2f(vf.x, vf.y);
        GL11.glVertex2f(vf.x + w, vf.y);
        GL11.glVertex2f(vf.x + w, vf.y + h);
        GL11.glVertex2f(vf.x, vf.y + h);
        GL11.glVertex2f(vf.x, vf.y);
        GL11.glEnd();
    }

    public static void drawRectangle(float width, Vec2d v, Vec2d v2) {
        Vec2f vf = new Vec2f((float) v.x, (float) v.y);
        Vec2f vf2 = new Vec2f((float) v2.x, (float) v2.y).sub(vf);

        GL11.glLineWidth(width);
        GL11.glBegin(GL11.GL_LINE_STRIP);
        GL11.glVertex2f(vf.x, vf.y);
        GL11.glVertex2f(vf.x + vf2.x, vf.y);
        GL11.glVertex2f(vf.x + vf2.x, vf.y + vf2.y);
        GL11.glVertex2f(vf.x, vf.y + vf2.y);
        GL11.glVertex2f(vf.x, vf.y);
        GL11.glEnd();
    }

    public static void drawRectangleNew(Vec2d v, Vec2d v2, float r, float g, float b, float a) {
        GL33.glColor4f(r, g, b, a);

        int x = (int) v.x;
        int y = (int) v.y;
        int x2 = (int) v2.x;
        int y2 = (int) v2.y;

        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x2, y);
        GL11.glVertex2d(x2, y2);
        GL11.glVertex2d(x, y2);
        GL11.glEnd();
    }

    public static void drawLine(float width, Vec2d a, Vec2d b) {
        int x = (int) a.x;
        int y = (int) a.y;

        int x2 = (int) b.x;
        int y2 = (int) b.y;

        GL11.glLineWidth(width);
        GL11.glBegin(GL11.GL_LINE_STRIP);
        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x2, y2);
        GL11.glEnd();
    }

    public static void glVertex2(Vec2d v) {
        GL33.glVertex2d(v.x, v.y);
    }

    public static void glVertex2(Vec2f v) {
        GL33.glVertex2f(v.x, v.y);
    }

    public static void glVertex2(Vec2i v) {
        GL33.glVertex2i(v.x, v.y);
    }

}
