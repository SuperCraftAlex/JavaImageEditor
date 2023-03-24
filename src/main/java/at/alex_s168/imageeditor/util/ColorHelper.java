package at.alex_s168.imageeditor.util;

import org.eclipse.swt.graphics.Color;

public class ColorHelper {

    public static int colorConvert(float Red, float Green, float Blue){
        int R = Math.round(255 * Red);
        int G = Math.round(255 * Green);
        int B = Math.round(255 * Blue);

        R = (R << 16) & 0x00FF0000;
        G = (G << 8) & 0x0000FF00;
        B = B & 0x000000FF;

        return 0xFF000000 | R | G | B;
    }

    public static int colorConvert(Color c){
        int R = c.getRed();
        int G = c.getGreen();
        int B = c.getBlue();

        R = (R << 16) & 0x00FF0000;
        G = (G << 8) & 0x0000FF00;
        B = B & 0x000000FF;

        return 0xFF000000 | R | G | B;
    }

    public static Color sub(Color c1, Color c2) {
        return new Color(
                Math.min(Math.abs(c1.getRed() - c2.getRed()),255),
                Math.min(Math.abs(c1.getGreen() - c2.getGreen()),255),
                Math.min(Math.abs(c1.getBlue() - c2.getBlue()),255)
        );
    }

    public static Color add(Color c1, Color c2) {
        return new Color(
                Math.min(c1.getRed() + c2.getRed(),255),
                Math.min(c1.getGreen() + c2.getGreen(),255),
                Math.min(c1.getBlue() + c2.getBlue(),255)
        );
    }

    public static Color div(Color c1, double v) {
        return new Color(
                (int) (c1.getRed() / v),
                (int) (c1.getGreen() / v),
                (int) (c1.getBlue() / v)
        );
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

    public static double luminosity(Color c) {
        return 0.5 * (max1(c) + min1(c));
    }

}
