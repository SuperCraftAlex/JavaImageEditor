package at.alex_s168.imageeditor.features.image;

import at.alex_s168.imageeditor.ui.EditorArea;
import static at.alex_s168.imageeditor.util.ColorHelper.*;
import org.eclipse.swt.graphics.Color;
import at.alex_s168.imageeditor.features.image.ImageMode;

public class FeatureImageMode {

    /**
     * Changes the Image mode to RGB
     */
    public static void changeModeToRGB() {
        switch(EditorArea.getSelf().rOut.mode) {
            case GRAYSCALE:
                EditorArea.getSelf().rOut.mode = ImageMode.RGB;

            default:
                return;
        }
    }

    /**
     * Changes the Image mode to Grayscale
     * @param real if real set to false, the image looks better to human eye
     */
    public static void changeModeToGrayscale(boolean real) {
        // if bugs check: https://www.dfstudios.co.uk/articles/programming/image-programming-algorithms/image-processing-algorithms-part-3-greyscale-conversion/

        changeModeToRGB();

        if(EditorArea.getSelf().rOut.mode == ImageMode.RGB) {

            int it = 0;
            for (int pixel : EditorArea.getSelf().rOut.pix) {
                Color c = colorConvert(pixel, ImageMode.RGB);

                int intensity;

                if(!real) {
                    intensity = calculateIntensityFake(c);
                } else {
                    intensity = calculateIntensity(c);
                }

                Color c2 = new Color(
                        intensity,
                        intensity,
                        intensity
                );

                EditorArea.getSelf().rOut.pix[it] = colorConvert(c2);

                it++;
            }

        }

        EditorArea.getSelf().rOut.mode = ImageMode.GRAYSCALE;

        EditorArea.getSelf().update();

    }

}