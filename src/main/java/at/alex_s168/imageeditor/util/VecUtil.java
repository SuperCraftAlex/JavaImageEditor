package at.alex_s168.imageeditor.util;

import de.m_marvin.univec.impl.Vec2i;

public class VecUtil {

    public static Vec2i max(Vec2i a, Vec2i b) {
        return new Vec2i(
                Math.max(a.x, b.x),
                Math.max(a.y, b.y)
        );
    }

    public static Vec2i min(Vec2i a, Vec2i b) {
        return new Vec2i(
                Math.min(a.x, b.x),
                Math.min(a.y, b.y)
        );
    }

    public static Vec2i div(Vec2i a, double b) {
        a.x /= b;
        a.y /= b;
        return a;
    }

    public static Vec2i mul(Vec2i a, double b) {
        a.x *= b;
        a.y *= b;
        return a;
    }

}
