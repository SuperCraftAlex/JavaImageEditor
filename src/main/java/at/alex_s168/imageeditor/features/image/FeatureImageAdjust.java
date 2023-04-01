package at.alex_s168.imageeditor.features.image;

import at.alex_s168.imageeditor.ui.EditorArea;
import org.eclipse.swt.graphics.Color;

import static at.alex_s168.imageeditor.util.ColorHelper.colorConvert;
import static at.alex_s168.imageeditor.util.ColorHelper.truncate;

public class FeatureImageAdjust {

    public static void changeContrast(double val) {
        int it = 0;
        for (int pixel : EditorArea.getSelf().rOut.pix) {
            Color c = colorConvert(pixel, EditorArea.getSelf().rOut.mode);

            float factor = (float) (259 * (val + 255)) / (float) (255 * (259 - val));

            Color c2 = new Color(
                    truncate(factor * (c.getRed() - 128) + 128),
                    truncate(factor * (c.getGreen() - 128) + 128),
                    truncate(factor * (c.getBlue() - 128) + 128)

            );

            EditorArea.getSelf().rOut.pix[it] = colorConvert(c2);

            it++;
        }

        EditorArea.getSelf().update();

    }

    public static void changeGamma(int val) {
        // if bugs check: https://www.dfstudios.co.uk/articles/programming/image-programming-algorithms/image-processing-algorithms-part-6-gamma-correction/

        int it = 0;
        for (int pixel : EditorArea.getSelf().rOut.pix) {
            Color c = colorConvert(pixel, EditorArea.getSelf().rOut.mode);

            int gammaCorrection = (int) (1 / (float)val);

            Color c2 = new Color(
                255 * (c.getRed()   / 255)  ^ gammaCorrection, 
                255 * (c.getGreen() / 255)  ^ gammaCorrection, 
                255 * (c.getBlue()  / 255)  ^ gammaCorrection
            );

            EditorArea.getSelf().rOut.pix[it] = colorConvert(c2);

            it++;
        }

        EditorArea.getSelf().update();

    }

}