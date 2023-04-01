package at.alex_s168.imageeditor.util;

import at.alex_s168.imageeditor.features.image.ImageMode;
import org.eclipse.swt.graphics.Color;

public class ColorHelper {

    //todo: Color#getRGBA()

    public static int colorConvert(float Red, float Green, float Blue){
        int R = Math.round(255 * Red);
        int G = Math.round(255 * Green);
        int B = Math.round(255 * Blue);
        return colorConvert(R, G, B);
    }

    public static int colorConvert(int R, int G, int B){
        R = (R << 16) & 0x00FF0000;
        G = (G << 8) & 0x0000FF00;
        B = B & 0x000000FF;

        return 0xFF000000 | R | G | B;
    }

    public static int colorConvert(int R, int G, int B, int A){
        A = (A << 24) & 0xFF000000;
        R = (R << 16) & 0x00FF0000;
        G = (G << 8)  & 0x0000FF00;
        B =  B        & 0x000000FF;

        return R | G | B | A;
    }

    public static int colorConvert(Color c){
        return colorConvert( 
            c.getRed(),
            c.getGreen(),
            c.getBlue(),
            c.getAlpha()
        );
    }

    public static Color colorConvert(int val, ImageMode mode) {
        return switch (mode) {
            case RGB, GRAYSCALE -> new Color(
                      (val >> 16) & 0xFF,
                    (val >> 8) & 0xFF,
                     (val) & 0xFF,
                    (val >> 24) & 0xFF
            );
        };
    }

    public static boolean empty(Color c) {
        if(c== null) {
            return true;
        }
        return sum(c) == 0;
    }

    public static Color sub(Color c1, Color c2) {
        return new Color(
                truncate(c1.getRed() - c2.getRed()),
                truncate(c1.getGreen() - c2.getGreen()),
                truncate(c1.getBlue() - c2.getBlue())
        );
    }

    public static Color add(Color c1, Color c2) {
        return new Color(
                truncate(c1.getRed() + c2.getRed()),
                truncate(c1.getGreen() + c2.getGreen()),
                truncate(c1.getBlue() + c2.getBlue())
        );
    }

    public static Color mul(Color c1, Color c2) {
        return new Color(
                truncate(c1.getRed() * c2.getRed()),
                truncate(c1.getGreen() * c2.getGreen()),
                truncate(c1.getBlue() * c2.getBlue())
        );
    }
    public static Color mul(Color c1, int val) {
        return new Color(
                truncate(c1.getRed()   * val ),
                truncate(c1.getGreen() * val ),
                truncate(c1.getBlue()  * val )
        );
    }

    public static Color div(Color c1, Color c2) {
        return new Color(
                truncate(c1.getRed() / c2.getRed()     ),
                truncate(c1.getGreen() / c2.getGreen() ),
                truncate(c1.getBlue() / c2.getBlue()   )
        );
    }

    public static Color div(Color c1, int val) {
        return new Color(
                truncate(c1.getRed() /   val),
                truncate(c1.getGreen() / val),
                truncate(c1.getBlue() /  val)
        );
    }

    public static Color alpha(Color c1, int alpha) {
        return new Color(
                truncate(c1.getRed()  ),
                truncate(c1.getGreen()),
                truncate(c1.getBlue() ),
                truncate(alpha        )
        );
    }

    public static double red1(Color c) {
        return (double)c.getRed() / 255;
    }

    public static double green1(Color c) {
        return (double)c.getGreen() / 255;
    }

    public static double blue1(Color c) {
        return (double)c.getBlue() / 255;
    }

    public static double alpha1(Color c) {
        return (double)c.getAlpha() / 255;
    }

    public static int one(double d) {
        return truncate((int) (d * 255));
    }

    public static int sum(Color c) {
        return c.getRed() + c.getGreen() + c.getBlue();
    }

    public static double max(Color c) {
        return Math.max(Math.max(c.getRed(),c.getBlue()),c.getGreen());
    }

    public static double min(Color c) {
        return Math.min(Math.min(c.getRed(),c.getBlue()),c.getGreen());
    }

    public static double max1(Color c) {
        return Math.max(Math.max((double) c.getRed()/255, (double) c.getBlue()/255), (double) c.getGreen()/255);
    }

    public static double min1(Color c) {
        return Math.min(Math.min((double) c.getRed()/255, (double) c.getBlue()/255), (double) c.getGreen()/255);
    }

    public static Color truncate(Color c) {
        return new Color(
            range(c.getRed(), 0, 255),
            range(c.getGreen(), 0, 255),
            range(c.getBlue(), 0, 255),
            range(c.getAlpha(), 0, 255)
        );
    }

    public static int range(int i, int min, int max) {
        if(i<min) {
            return min;
        }
        return Math.min(i, max);
    }

    public static double range(double i, double min, double max) {
        if(i<min) {
            return min;
        }
        return Math.min(i, max);
    }

    public static int truncate(int i) {
        return range(i, 0, 255);
    }

    public static int truncateABS(int i) {
        return range(Math.abs(i), 0, 255);
    }

    public static int truncate(double i) {
        return range((int) i, 0, 255);
    }

    public static int truncate(float i) {
        return range((int) i, 0, 255);
    }

    public static double truncate1(double i) {
        return range(i , 0, 1);
    }

    public static int calculateIntensityFake(Color c) {
        return (int) (0.299*c.getRed() + 0.587*c.getGreen() + 0.114*c.getBlue());
    }

    public static int calculateIntensity(Color c) {
        return (c.getRed() + c.getGreen() + c.getBlue()) / 3;
    }

}
