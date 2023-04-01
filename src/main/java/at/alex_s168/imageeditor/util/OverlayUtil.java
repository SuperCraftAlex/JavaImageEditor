package at.alex_s168.imageeditor.util;

import at.alex_s168.imageeditor.api.PixelMap;
import de.m_marvin.univec.impl.Vec2i;

public class OverlayUtil {

    public static PixelMap overlay(PixelMap a, PixelMap b) {
        Vec2i max = VecUtil.max(a.getBounds().addM(a.pos), b.getBounds().addM(b.pos));
        PixelMap m = new PixelMap(max.x, max.y);

        //todo: do sometime

        int it = 0;
        for(int pixel : m.pix) {
            it++;
        }

        return m;
    }

}
