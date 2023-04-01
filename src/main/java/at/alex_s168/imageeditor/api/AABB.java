package at.alex_s168.imageeditor.api;

import at.alex_s168.imageeditor.util.VecUtil;
import de.m_marvin.univec.impl.Vec2d;
import de.m_marvin.univec.impl.Vec2i;

public class AABB {

    public Vec2i A;
    public Vec2i B;

    public AABB() {}

    public AABB(Vec2i a, Vec2i b) {
        A = a;
        B = b;
    }

    public boolean empty() {
        return (A == null) || (B == null);
    }

    public double sqArea() {
        if(empty()) {
            return 0;
        }
        return A.sub(B).lengthSqrt();
    }

    public void clear() {
        A = null;
        B = null;
    }

    public int getWidth() {
        return Math.abs(A.x - B.x);
    }

    public int getHeight() {
        return Math.abs(A.y - B.y);
    }

    public boolean inBounds(int x, int y) {
        return (
                x > A.x && x < B.x && y > A.y && y < B.y
        );
    }
    public boolean inBounds(Vec2i vec) {
        return inBounds(vec.x, vec.y);
    }

    /**
     * Only works if A = (0|0);
     */
    public Vec2i addM(Vec2i v) {
       return new Vec2i(B.add(v));
    }

    public void maxI(AABB other) {
        A = VecUtil.min(A, other.A);
        B = VecUtil.max(B, other.B);
    }
}
