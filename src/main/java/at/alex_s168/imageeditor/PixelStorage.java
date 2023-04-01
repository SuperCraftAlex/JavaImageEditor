package at.alex_s168.imageeditor;

import at.alex_s168.imageeditor.api.PixelMap;
import at.alex_s168.imageeditor.ui.EditorArea;

import java.util.ArrayList;
import java.util.List;

public class PixelStorage {

    public List<PixelMap> layers = new ArrayList<>();
    private int currentLayer;

    public static PixelStorage getSelf() {
        return ImageEditor.getInstance().getPixelStorage();
    }

    public PixelMap getCurrentPixelMap() {
        return layers.get(currentLayer);
    }

    public void reset(PixelMap m) {
        //temp
        EditorArea.getSelf().rOut = m;

        layers.clear();
        layers.add(m);
        currentLayer = 0;
    }

}
