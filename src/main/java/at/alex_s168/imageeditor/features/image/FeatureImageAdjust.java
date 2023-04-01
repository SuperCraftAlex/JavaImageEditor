package at.alex_s168.imageeditor.features.image;

import at.alex_s168.imageeditor.PixelStorage;
import at.alex_s168.imageeditor.util.ColorHelper;
import org.eclipse.swt.graphics.Color;

import static at.alex_s168.imageeditor.util.ColorHelper.colorConvert;
import static at.alex_s168.imageeditor.util.ColorHelper.truncate;

public class FeatureImageAdjust {

    public static void changeContrast(double val) {
        int it = 0;
        for (int pixel : PixelStorage.getSelf().getCurrentPixelMap().pix) {
            Color c = colorConvert(pixel, PixelStorage.getSelf().getCurrentPixelMap().mode);

            float factor = (float) (259 * (val + 255)) / (float) (255 * (259 - val));

            Color c2 = new Color(
                    truncate(factor * (c.getRed() - 128) + 128),
                    truncate(factor * (c.getGreen() - 128) + 128),
                    truncate(factor * (c.getBlue() - 128) + 128)

            );

            PixelStorage.getSelf().getCurrentPixelMap().pix[it] = colorConvert(c2);

            it++;
        }

        PixelStorage.getSelf().update();

    }

    public static void changeGamma(int val) {
        // if bugs check: https://www.dfstudios.co.uk/articles/programming/image-programming-algorithms/image-processing-algorithms-part-6-gamma-correction/

        int it = 0;
        for (int pixel : PixelStorage.getSelf().getCurrentPixelMap().pix) {
            Color c = colorConvert(pixel, PixelStorage.getSelf().getCurrentPixelMap().mode);

            int gammaCorrection = (int) (1 / (float)val);

            Color c2 = new Color(
                255 * (c.getRed()   / 255)  ^ gammaCorrection, 
                255 * (c.getGreen() / 255)  ^ gammaCorrection, 
                255 * (c.getBlue()  / 255)  ^ gammaCorrection
            );

            PixelStorage.getSelf().getCurrentPixelMap().pix[it] = colorConvert(c2);

            it++;
        }

        PixelStorage.getSelf().update();

    }

    public static void colorReplace(Color what, Color to, int tolerance, boolean smart) {

        int it = 0;
        for (int pixel : PixelStorage.getSelf().getCurrentPixelMap().pix) {
            Color c = colorConvert(pixel, PixelStorage.getSelf().getCurrentPixelMap().mode);

            Color diff = ColorHelper.sub(c, what);

            if (Math.abs(ColorHelper.sum(diff)) < tolerance) {
                if(smart) {
                    PixelStorage.getSelf().getCurrentPixelMap().pix[it] = colorConvert( ColorHelper.add(to, diff) );
                }
                else {
                    PixelStorage.getSelf().getCurrentPixelMap().pix[it] = colorConvert(to);
                }
            }
            it++;
        }

        PixelStorage.getSelf().update();

    }

    public static void colorInvert() {

        int it = 0;
        for (int pixel : PixelStorage.getSelf().getCurrentPixelMap().pix) {
            Color c = colorConvert(pixel, PixelStorage.getSelf().getCurrentPixelMap().mode);

            PixelStorage.getSelf().getCurrentPixelMap().pix[it] = colorConvert(new Color(
                    255 - c.getRed(),
                    255 - c.getGreen(),
                    255 - c.getBlue()
            ));

            it++;
        }

        PixelStorage.getSelf().update();

    }

    public static void colorBrightness(double m) {

        int it = 0;
        for (int pixel : PixelStorage.getSelf().getCurrentPixelMap().pix) {
            Color c = colorConvert(pixel, PixelStorage.getSelf().getCurrentPixelMap().mode);

            Color cn = new Color(
                    Math.min((int) (c.getRed() * m),255),
                    Math.min((int) (c.getGreen() * m),255),
                    Math.min((int) (c.getBlue() * m),255)
            );

            PixelStorage.getSelf().getCurrentPixelMap().pix[it] = colorConvert(cn);

            it++;
        }

        PixelStorage.getSelf().update();

    }

}