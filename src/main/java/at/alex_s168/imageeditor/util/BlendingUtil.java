package at.alex_s168.imageeditor.util;

import at.alex_s168.imageeditor.api.BlendMode;
import at.alex_s168.imageeditor.api.PixelMap;
import de.m_marvin.univec.impl.Vec2i;
import org.eclipse.swt.graphics.Color;

import java.util.function.Function;

import static at.alex_s168.imageeditor.util.ColorHelper.*;

public class BlendingUtil {

    public static PixelMap blend(PixelMap base, PixelMap top) {
        Vec2i max = VecUtil.max(base.getBounds().B.add(base.pos), top.getBounds().B.add(top.pos));
        PixelMap m = new PixelMap(max.x, max.y);

        int it = 0;
        for (int y = 0; y < max.y; y++) {
            for (int x = 0; x < max.x; x++) {
                Color ca = null;
                Color cb = null;

                if(base.getBoundsPositional().inBounds(x,y)) {
                    ca = colorConvert(base.getPositional(x, y), base.mode);
                }
                if(top.getBoundsPositional().inBounds(x,y)) {
                    cb = colorConvert(top.getPositional(x, y), top.mode);
                }

                if(empty(ca) && !empty(cb)) {
                    m.pix[it] = colorConvert(cb);
                }
                else if(empty(cb) && !empty(ca)) {
                    m.pix[it] = colorConvert(ca);
                }
                else if(empty(ca) || empty(cb)) {m.pix[it] = colorConvert(0,0,0,0);}
                else {

                    m.pix[it] = colorConvert(blend(ca, cb, top.blendMode));

                }

                it++;
            }
        }

        return m;
    }

    public static Color blend(Color base, Color top, BlendMode mode) {
        switch(mode) {
            case NORMAL -> { return blendNormal(base, top); }
            case MULTIPLY -> { return blendMultiply(base, top); }
            case SCREEN -> { return blendScreen(base, top); }
            case OVERLAY -> { return blendOverlay(base, top); }
            case SOFT_LIGHT -> { return blendSoftLight(base, top); }

            case DIVIDE -> { return blendDivide(base, top); }
            case ADDITION -> { return blendAddition(base, top); }
            case SUBTRACT -> { return blendSubstract(base, top); }
            case DIFFERENCE -> { return blendDifference(base, top); }

            case DARKEN -> { return blendDarken(base, top); }
            case LIGHTEN -> { return blendLighten(base, top); }
        }
        return blend(base, top, BlendMode.NORMAL);
    }

    public static Color blendNormal(Color c1, Color c2) {
        int r = (c1.getRed() + c2.getRed()) / 2;
        int g = (c1.getGreen() + c2.getGreen()) / 2;
        int b = (c1.getBlue() + c2.getBlue()) / 2;
        int a = (c1.getAlpha() + c2.getAlpha()) / 2;
        return new Color(r, g, b, a);
    }

    public static Color blendMultiply(Color c1, Color c2) {
        int r = truncate( c1.getRed()   * 255 - c2.getRed()   );
        int g = truncate( c1.getGreen() * 255 - c2.getGreen() );
        int b = truncate( c1.getBlue()  * 255 - c2.getBlue()  );
        int a = truncate( c1.getAlpha() * 255 - c2.getAlpha() );
        return new Color(r, g, b, a);
    }

    public static Color blendScreen(Color c1, Color c2) {
        int r = truncate( 255 - (255 - c1.getRed()  ) * (255 - c2.getRed()  ) );
        int g = truncate( 255 - (255 - c1.getGreen()) * (255 - c2.getGreen()) );
        int b = truncate( 255 - (255 - c1.getBlue() ) * (255 - c2.getBlue() ) );
        int a = truncate( 255 - (255 - c1.getAlpha()) * (255 - c2.getAlpha()) );
        return new Color(r, g, b, a);
    }

    public static Color blendOverlay(Color c1, Color c2) {
        int r = blendOverlaySingle(c1.getRed(), c2.getRed(), c1.getAlpha());
        int g = blendOverlaySingle(c1.getGreen(), c2.getGreen(), c1.getAlpha());
        int b = blendOverlaySingle(c1.getBlue(), c2.getBlue(), c1.getAlpha());
        int a = blendOverlaySingle(c1.getAlpha(), c2.getAlpha(), c1.getAlpha());
        return new Color(r, g, b, a);
    }
    private static int blendOverlaySingle(int a, int b, int A) {
        if(A < 128) return truncate(2*a*b);
        return truncate(255 - 2*(255 - a)*(255 - b));
    }

    public static Color blendSoftLight(Color c1, Color c2) {
        int r = one(blendSoftLightSingle(red1(c1), c2.getRed()    ));
        int g = one(blendSoftLightSingle(green1(c1), c2.getGreen()));
        int b = one(blendSoftLightSingle(blue1(c1), c2.getBlue()  ));
        int a = one(blendSoftLightSingle(alpha1(c1), c2.getAlpha()));
        return new Color(r, g, b, a);
    }
    private static double blendSoftLightSingle(double a, double b) {
        if(b < 0.5) return truncate( 2*a*b + Math.pow(a,2)*(1-2*b) );
        return truncate( 2*a*(1-b) + Math.sqrt(a)*(2*b-1) );
    }

    public static Color blendDivide(Color c1, Color c2) {
        int r = truncateABS( c1.getRed()   / c2.getRed()   );
        int g = truncateABS( c1.getGreen() / c2.getGreen() );
        int b = truncateABS( c1.getBlue()  / c2.getBlue()  );
        int a = truncateABS( c1.getAlpha() / c2.getAlpha() );
        return new Color(r, g, b, a);
    }

    public static Color blendAddition(Color c1, Color c2) {
        int r = truncate( c1.getRed()   + c2.getRed()   );
        int g = truncate( c1.getGreen() + c2.getGreen() );
        int b = truncate( c1.getBlue()  + c2.getBlue()  );
        int a = truncate( c1.getAlpha() + c2.getAlpha() );
        return new Color(r, g, b, a);
    }

    public static Color blendSubstract(Color c1, Color c2) {
        int r = truncate( c1.getRed()   - c2.getRed()   );
        int g = truncate( c1.getGreen() - c2.getGreen() );
        int b = truncate( c1.getBlue()  - c2.getBlue()  );
        int a = truncate( c1.getAlpha() - c2.getAlpha() );
        return new Color(r, g, b, a);
    }

    public static Color blendDifference(Color c1, Color c2) {
        int r = truncateABS( c1.getRed()   - c2.getRed()   );
        int g = truncateABS( c1.getGreen() - c2.getGreen() );
        int b = truncateABS( c1.getBlue()  - c2.getBlue()  );
        int a = truncateABS( c1.getAlpha() - c2.getAlpha() );
        return new Color(r, g, b, a);
    }

    public static Color blendDarken(Color c1, Color c2) {
        int r = Math.min( c1.getRed()   , c2.getRed()   );
        int g = Math.min( c1.getGreen() , c2.getGreen() );
        int b = Math.min( c1.getBlue()  , c2.getBlue()  );
        int a = Math.min( c1.getAlpha() , c2.getAlpha() );
        return new Color(r, g, b, a);
    }

    public static Color blendLighten(Color c1, Color c2) {
        int r = Math.max( c1.getRed()   , c2.getRed()   );
        int g = Math.max( c1.getGreen() , c2.getGreen() );
        int b = Math.max( c1.getBlue()  , c2.getBlue()  );
        int a = Math.max( c1.getAlpha() , c2.getAlpha() );
        return new Color(r, g, b, a);
    }

}
