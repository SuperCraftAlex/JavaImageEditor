package at.alex_s168.imageeditor.ui.right;

import at.alex_s168.imageeditor.ui.Editor;
import at.alex_s168.imageeditor.ui.EditorArea;
import at.alex_s168.imageeditor.ui.UIPreviewWindow;
import at.alex_s168.imageeditor.util.Translator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.internal.C;
import org.eclipse.swt.layout.BorderData;
import org.eclipse.swt.layout.BorderLayout;
import org.eclipse.swt.widgets.*;

public class UIRight {

    public static void genUIRightRight(Shell s, Display d, Composite groupRight) {
        //todo: translate

        final Composite groupRightRight = new Composite(groupRight, SWT.NONE);
        groupRightRight.setLayoutData(new BorderData(SWT.RIGHT, SWT.DEFAULT, SWT.DEFAULT));
        BorderLayout groupRightRightLayout = new BorderLayout();
        groupRightRightLayout.type = SWT.VERTICAL;
        groupRightRight.setLayout(groupRightRightLayout);

        final Group groupNavigate = new Group(groupRightRight, SWT.NONE);
        groupNavigate.setLayoutData(new BorderData(SWT.TOP));
        groupNavigate.setLayout(new BorderLayout());
        groupNavigate.setText("Navigate");

        final Group groupHistory = new Group(groupRightRight, SWT.NONE);
        groupHistory.setLayoutData(new BorderData(SWT.CENTER));
        groupHistory.setLayout(new BorderLayout());
        groupHistory.setText("History");

        final Group groupChannels = new Group(groupRightRight, SWT.NONE);
        groupChannels.setLayoutData(new BorderData(SWT.CENTER));
        groupChannels.setLayout(new BorderLayout());
        groupChannels.setText("Channels");

        final Group groupLayers = new Group(groupRightRight, SWT.NONE);
        groupLayers.setLayoutData(new BorderData(SWT.CENTER));
        groupLayers.setLayout(new BorderLayout());
        groupLayers.setText("Layers");

        ScrolledComposite layers = new ScrolledComposite( groupLayers, SWT.V_SCROLL );

        final Composite layer = new Composite(layers, SWT.NONE);
        BorderLayout layerLayout = new BorderLayout();
        layer.setLayout(layerLayout);
        layer.setSize(110, 100);

        //Label label = new Label( layer, SWT.LEFT );
        //label.setBackground( new Color(50,50,250,128));
        //label.setSize(layer.getSize());
        //label.setText("Layer 1");

        UIPreviewWindow previewArea = new UIPreviewWindow(layer);
        previewArea.setSize(300, 300);
        previewArea.setLocation(50, 20);
        previewArea.setBackground(new Color(128, 128, 0));
        previewArea.setSize(layer.getSize());

        layers.setContent( layer );

    }

}
