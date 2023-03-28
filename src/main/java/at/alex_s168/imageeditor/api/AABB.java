package at.alex_s168.imageeditor.api;

import de.m_marvin.univec.impl.Vec2d;

public class AABB {

    public Vec2d A;
    public Vec2d B;

    public AABB() {}

    public AABB(Vec2d a, Vec2d b) {
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

    public double getWidth() {
        return Math.abs(A.x - B.x);
    }

    public double getHeight() {
        return Math.abs(A.y - B.y);
    }

    public boolean inBounds(Vec2d vec) {
        return (
                vec.x >= A.x && vec.x <= B.x && vec.y >= A.y && vec.y <= B.y
        );
    }

}
