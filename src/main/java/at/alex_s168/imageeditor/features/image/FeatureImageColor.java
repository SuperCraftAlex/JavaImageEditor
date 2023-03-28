package at.alex_s168.imageeditor.features.image;

import at.alex_s168.imageeditor.ImageEditor;
import at.alex_s168.imageeditor.ui.EditorArea;
import at.alex_s168.imageeditor.util.ColorHelper;
import org.eclipse.swt.graphics.Color;

public class FeatureImageColor {

    public static void colorReplace(Color what, Color to, int tolerance, boolean smart) {

        int it = 0;
        for (int pixel : EditorArea.getSelf().rOut.pix) {
            Color c = new Color(
                    (pixel>>16)&0xFF,
                    (pixel>>8)&0xFF,
                    (pixel)&0xFF
            );

            Color diff = ColorHelper.sub(c, what);

            if (Math.abs(ColorHelper.sum(diff)) < tolerance) {
                if(smart) {
                    EditorArea.getSelf().rOut.pix[it] = ColorHelper.colorConvert( ColorHelper.add(to, diff) );
                }
                else {
                    EditorArea.getSelf().rOut.pix[it] = ColorHelper.colorConvert(to);
                }
            }
            it++;
        }

        EditorArea.getSelf().update();

    }

    public static void colorInvert() {

        int it = 0;
        for (int pixel : EditorArea.getSelf().rOut.pix) {
            Color c = new Color(
                    255-(pixel>>16)&0xFF,
                    255-(pixel>>8)&0xFF,
                    255-(pixel)&0xFF
            );

            EditorArea.getSelf().rOut.pix[it] = ColorHelper.colorConvert(c);

            it++;
        }

        EditorArea.getSelf().update();

    }

    public static void colorBrightness(double m) {

        int it = 0;
        for (int pixel : EditorArea.getSelf().rOut.pix) {
            Color c = new Color(
                    Math.min((int) (((pixel>>16)&0xFF) * m),255),
                    Math.min((int) (((pixel>>8)&0xFF) * m),255),
                    Math.min((int) (((pixel)&0xFF) * m),255)
            );

            EditorArea.getSelf().rOut.pix[it] = ColorHelper.colorConvert(c);

            it++;
        }

        EditorArea.getSelf().update();

    }

    public static void channelRemoveR() {

        int it = 0;
        for (int pixel : EditorArea.getSelf().rOut.pix) {
            Color c = new Color(
                    0,
                    (pixel>>8)&0xFF,
                    (pixel)&0xFF
            );

            EditorArea.getSelf().rOut.pix[it] = ColorHelper.colorConvert(c);

            it++;
        }

        EditorArea.getSelf().update();

    }

    public static void channelRemoveG() {

        int it = 0;
        for (int pixel : EditorArea.getSelf().rOut.pix) {
            Color c = new Color(
                    (pixel>>16)&0xFF,
                    0,
                    (pixel)&0xFF
            );

            EditorArea.getSelf().rOut.pix[it] = ColorHelper.colorConvert(c);

            it++;
        }

        EditorArea.getSelf().update();

    }

    public static void channelRemoveB() {

        int it = 0;
        for (int pixel : EditorArea.getSelf().rOut.pix) {
            Color c = new Color(
                    (pixel>>16)&0xFF,
                    (pixel>>8)&0xFF,
                    0
            );

            EditorArea.getSelf().rOut.pix[it] = ColorHelper.colorConvert(c);

            it++;
        }

        EditorArea.getSelf().update();

    }

    public static void channelRotateCCW() {
        int it = 0;
        for (int pixel : EditorArea.getSelf().rOut.pix) {
            Color c = new Color(
                    (pixel)&0xFF,
                    (pixel>>16)&0xFF,
                    (pixel>>8)&0xFF
            );

            EditorArea.getSelf().rOut.pix[it] = ColorHelper.colorConvert(c);

            it++;
        }

        EditorArea.getSelf().update();

    }

    public static void channelRotateCW() {
        int it = 0;
        for (int pixel : EditorArea.getSelf().rOut.pix) {
            Color c = new Color(
                    (pixel>>8)&0xFF,
                    (pixel)&0xFF,
                    (pixel>>16)&0xFF
            );

            EditorArea.getSelf().rOut.pix[it] = ColorHelper.colorConvert(c);

            it++;
        }

        EditorArea.getSelf().update();

    }

}
