package at.alex_s168.imageeditor.features.image;

import at.alex_s168.imageeditor.ImageEditor;
import at.alex_s168.imageeditor.ui.EditorArea;
import static at.alex_s168.imageeditor.util.ColorHelper.*;
import org.eclipse.swt.graphics.Color;

public class FeatureImageAdjust {

    /**
     * Changes the Contrast of the image
     * @param val The contrast value. Range: -128 to 128
     */
    public static void changeContrast(int val) {
        // if bugs check: https://www.dfstudios.co.uk/articles/programming/image-programming-algorithms/image-processing-algorithms-part-5-contrast-adjustment/

        int it = 0;
        for (int pixel : EditorArea.getSelf().rOut.pix) {
            Color c = colorConvert(pixel, EditorArea.getSelf().rOut.mode);

            float factor = (259 * (val + 255)) / (255 * (259 - val));

            Color c2 = new Color(
                truncate(factor * (color.getRed() - 128) + 128),
                truncate(factor * (color.getGreen() - 128) + 128),
                truncate(factor * (color.getBlue() - 128) + 128)
            );

            EditorArea.getSelf().rOut.pix[it] = colorConvert(c2);

            it++;
        }

        EditorArea.getSelf().update();

    }

    /**
     * Changes the Gamma of the image
     * @param val The gamma value. Range: 0.01 to 7.99
     */
    public static void changeGamma(int val) {
        // if bugs check: https://www.dfstudios.co.uk/articles/programming/image-programming-algorithms/image-processing-algorithms-part-6-gamma-correction/

        int it = 0;
        for (int pixel : EditorArea.getSelf().rOut.pix) {
            Color c = colorConvert(pixel, EditorArea.getSelf().rOut.mode);

            float gammaCorrection = 1 / val;

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