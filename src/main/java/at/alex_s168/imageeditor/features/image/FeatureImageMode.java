package at.alex_s168.imageeditor.features.image;

import at.alex_s168.imageeditor.PixelStorage;
import static at.alex_s168.imageeditor.util.ColorHelper.*;
import org.eclipse.swt.graphics.Color;
import at.alex_s168.imageeditor.features.image.ImageMode;

public class FeatureImageMode {

    /**
     * Changes the Image mode to RGB
     */
    public static void changeModeToRGB() {
        switch(PixelStorage.getSelf().getCurrentPixelMap().mode) {
            case GRAYSCALE:
                PixelStorage.getSelf().getCurrentPixelMap().mode = ImageMode.RGB;

            default:
        }
    }

    /**
     * Changes the Image mode to Grayscale
     * @param real if real set to false, the image looks better to human eye
     */
    public static void changeModeToGrayscale(boolean real) {
        // if bugs check: https://www.dfstudios.co.uk/articles/programming/image-programming-algorithms/image-processing-algorithms-part-3-greyscale-conversion/

        changeModeToRGB();

        if(PixelStorage.getSelf().getCurrentPixelMap().mode == ImageMode.RGB) {

            int it = 0;
            for (int pixel : PixelStorage.getSelf().getCurrentPixelMap().pix) {
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

                PixelStorage.getSelf().getCurrentPixelMap().pix[it] = colorConvert(c2);

                it++;
            }

        }

        PixelStorage.getSelf().getCurrentPixelMap().mode = ImageMode.GRAYSCALE;

        PixelStorage.getSelf().update();

    }

}