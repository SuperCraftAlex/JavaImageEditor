package at.alex_s168.imageeditor.features.image;

import at.alex_s168.imageeditor.ImageEditor;
import at.alex_s168.imageeditor.ui.EditorArea;
import static at.alex_s168.imageeditor.util.ColorHelper.*;
import org.eclipse.swt.graphics.Color;

public class FeatureImageMode {

    /**
     * Changes the Image mode to RGB
     */
    public static void changeModeToRGB() {
        ImageMode to;

        switch(EditorArea.getSelf().rOut.mode) {
            case ImageMode.Grayscale:
                to = ImageMode.RGB;

            default:
                return;
        }

        EditorArea.getSelf().rOut.mode = to;
    }

    /**
     * Changes the Image mode to Grayscale
     * @param real if real set to false, the image looks better to human eye
     */
    public static void changeModeToGrayscale(bool real) {
        // if bugs check: https://www.dfstudios.co.uk/articles/programming/image-programming-algorithms/image-processing-algorithms-part-3-greyscale-conversion/

        if(EditorArea.getSelf().rOut.mode == ImageMode.RGB) {

            int it = 0;
            for (int pixel : EditorArea.getSelf().rOut.pix) {
                Color c = new Color(
                        (pixel>>16)&0xFF,
                        (pixel>>8)&0xFF,
                        (pixel)&0xFF
                );

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