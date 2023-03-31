package at.alex_s168.imageeditor.ui.image.adjust;

import at.alex_s168.imageeditor.features.image.FeatureImageColor;
import at.alex_s168.imageeditor.ui.UIBottomButtons;
import at.alex_s168.imageeditor.util.Translator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.BorderData;
import org.eclipse.swt.layout.BorderLayout;
import org.eclipse.swt.widgets.*;

public class UIAdjustBrightnessAndContrast {

    public static void genImageBrightnesAndContrastButton(Menu imageAdjustMenu, Display display, Shell mainShell) {

        MenuItem imageBrightnessOpt = new MenuItem(imageAdjustMenu, SWT.PUSH);
        imageBrightnessOpt.setText(Translator.translate("editor.menu.image.adjust.brightness"));
        imageBrightnessOpt.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                Shell w = new Shell(display);
                w.setSize(mainShell.getSize().x / 3, mainShell.getSize().y / 3);
                w.setText(Translator.translate("editor.window.image.adjust.brightness"));
                w.setLayout(new BorderLayout());

                final int[] b = {1};
                final int[] c = {1};

                UIBottomButtons.generateBottomButtons(w, new SelectionListener() {
                    @Override
                    public void widgetSelected(SelectionEvent e) {
                        FeatureImageColor.colorBrightness((double) b[0] / 100);
                        //todo: contast (c)
                    }

                    @Override
                    public void widgetDefaultSelected(SelectionEvent e) {
                    }
                });

                Composite settings = new Composite(w, SWT.NONE);
                settings.setLayoutData(new BorderData(SWT.CENTER, SWT.DEFAULT, SWT.CENTER));
                settings.setLayout(new BorderLayout());

                final Label t = new Label(settings, SWT.BORDER);
                t.setLayoutData(new BorderData(SWT.TOP));
                t.setText(Translator.translate("brightness"));

                final Slider brightnessSlide = new Slider(settings, SWT.HORIZONTAL);
                brightnessSlide.setLayoutData(new BorderData(SWT.TOP, SWT.CENTER, SWT.CENTER));
                brightnessSlide.setMinimum(20);
                brightnessSlide.setMaximum(200);
                brightnessSlide.setIncrement(1);
                brightnessSlide.setSelection(100);

                brightnessSlide.addSelectionListener(new SelectionAdapter() {
                    public void widgetSelected(SelectionEvent e) {
                        b[0] = brightnessSlide.getSelection();
                    }
                });


                final Label tc = new Label(settings, SWT.BORDER);
                tc.setLayoutData(new BorderData(SWT.BOTTOM));
                tc.setText(Translator.translate("contrast"));

                final Slider contrastSlide = new Slider(settings, SWT.HORIZONTAL);
                contrastSlide.setLayoutData(new BorderData(SWT.BOTTOM, SWT.CENTER, SWT.CENTER));
                contrastSlide.setMinimum(20);
                contrastSlide.setMaximum(200);
                contrastSlide.setIncrement(1);
                contrastSlide.setSelection(100);

                contrastSlide.addSelectionListener(new SelectionAdapter() {
                    public void widgetSelected(SelectionEvent e) {
                        c[0] = contrastSlide.getSelection();
                    }
                });

                w.open();
            }
        });

    }

}
