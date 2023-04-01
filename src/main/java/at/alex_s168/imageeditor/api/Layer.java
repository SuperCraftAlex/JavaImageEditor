package at.alex_s168.imageeditor.api;

public class Layer {

    public PixelMap pixelMap;
    public boolean visible = true;

    public Layer() {
        pixelMap = new PixelMap();
    }

    public Layer(PixelMap m) {
        pixelMap = m;
    }

}
