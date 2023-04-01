package at.alex_s168.imageeditor.ui.image.adjust;

import at.alex_s168.imageeditor.features.image.FeatureImageAdjust;
import at.alex_s168.imageeditor.ui.UIBottomButtons;
import at.alex_s168.imageeditor.util.Translator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.BorderData;
import org.eclipse.swt.layout.BorderLayout;
import org.eclipse.swt.widgets.*;

public class UIAdjustColorReplace {

    public static void genImageColorReplaceButton(Menu imageColorMenu, Shell shell, Display display) {

        MenuItem imageColorReplaceOpt = new MenuItem(imageColorMenu, SWT.PUSH);
        imageColorReplaceOpt.setText(Translator.translate("editor.menu.image.adjust.color_replace"));
        imageColorReplaceOpt.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                Shell w = new Shell(display);
                w.setSize(shell.getSize().x / 3, shell.getSize().y / 3);
                w.setText(Translator.translate("editor.window.image.adjust.color_replace"));
                w.setLayout(new BorderLayout());

                w.addFocusListener(new FocusListener() {
                    public void focusLost(FocusEvent e) {}
                    public void focusGained(FocusEvent e) {}
                });

                final Color[] c1 = {new Color(255, 255, 255)};
                final Color[] c2 = {new Color(255, 255, 255)};
                final int[] tolerance = {0};
                final boolean[] smartReplace = {false};

                UIBottomButtons.generateBottomButtons(w, new SelectionListener() {
                    @Override public void widgetSelected(SelectionEvent e) { FeatureImageAdjust.colorReplace(c1[0], c2[0], tolerance[0], smartReplace[0]); }
                    @Override public void widgetDefaultSelected(SelectionEvent e) {}
                });

                Composite colors = new Composite(w, SWT.NONE);
                colors.setLayoutData(new BorderData(SWT.UP, SWT.DEFAULT, SWT.DEFAULT));
                colors.setLayout(new BorderLayout());

                final Label color1Label = new Label(colors, SWT.TOP);
                color1Label.setLayoutData(new BorderData(SWT.TOP));
                color1Label.setText("                              ");
                color1Label.setBackground(c1[0]);

                Button color1Button = new Button(colors, SWT.PUSH);
                color1Button.setText(Translator.translate("color_change"));
                color1Button.setLayoutData(new BorderData(SWT.TOP));
                color1Button.addSelectionListener(new SelectionAdapter() {
                    public void widgetSelected(SelectionEvent event) {ColorDialog dlg = new ColorDialog(shell);dlg.setRGB(color1Label.getBackground().getRGB()); dlg.setText(Translator.translate("editor.window.color.select"));
                        RGB rgb = dlg.open(); if (rgb != null) {c1[0].dispose(); c1[0] = new Color(shell.getDisplay(), rgb);color1Label.setBackground(c1[0]);} w.setActive(); }
                });

                final Label color2Label = new Label(colors, SWT.BOTTOM);
                color2Label.setLayoutData(new BorderData(SWT.BOTTOM));
                color2Label.setText("                              ");
                color2Label.setBackground(c1[0]);

                Button color2Button = new Button(colors, SWT.PUSH);
                color2Button.setText(Translator.translate("color_change"));
                color2Button.setLayoutData(new BorderData(SWT.BOTTOM));
                color2Button.addSelectionListener(new SelectionAdapter() {
                    public void widgetSelected(SelectionEvent event) {ColorDialog dlg = new ColorDialog(shell);dlg.setRGB(color1Label.getBackground().getRGB()); dlg.setText(Translator.translate("editor.window.color.select"));RGB rgb = dlg.open(); if (rgb != null) {c2[0].dispose(); c2[0] = new Color(shell.getDisplay(), rgb);color2Label.setBackground(c2[0]);} w.setActive(); }
                });

                //

                Composite settings = new Composite(w, SWT.NONE);
                settings.setLayoutData(new BorderData(SWT.CENTER, SWT.DEFAULT, SWT.CENTER));
                settings.setLayout(new BorderLayout());

                final Label t = new Label(settings, SWT.BORDER);
                t.setText(Translator.translate("tolerance"));

                final Slider toleranceSlide = new Slider(settings, SWT.HORIZONTAL);
                toleranceSlide.setMinimum(0);
                toleranceSlide.setMaximum(780);
                toleranceSlide.setIncrement(1);

                toleranceSlide.addSelectionListener(new SelectionAdapter() {
                    public void widgetSelected(SelectionEvent e) {
                        tolerance[0] = toleranceSlide.getSelection();
                    }
                });

                Button smartReplaceCheckBox = new Button(settings, SWT.CHECK);
                smartReplaceCheckBox.setLayoutData(new BorderData(SWT.BOTTOM, SWT.DEFAULT, SWT.DEFAULT));
                smartReplaceCheckBox.setText(Translator.translate("editor.window.image.adjust.color_replace.smartreplace"));
                smartReplaceCheckBox.addSelectionListener(new SelectionAdapter() {
                    @Override
                    public void widgetSelected(SelectionEvent event) {
                        Button btn = (Button) event.getSource();
                        smartReplace[0] = btn.getSelection();
                    }
                });

                w.open();
            }
        });

    }

}
