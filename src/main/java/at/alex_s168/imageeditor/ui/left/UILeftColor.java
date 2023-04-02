package at.alex_s168.imageeditor.ui.left;

import at.alex_s168.imageeditor.ToolStorage;
import at.alex_s168.imageeditor.util.Translator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.BorderData;
import org.eclipse.swt.layout.BorderLayout;
import org.eclipse.swt.widgets.*;

public class UILeftColor {

    public static void genColorSidebar(Shell shell, Composite groupLeft) {
        //todo: custom color selection menu

        final Group groupColor = new Group(groupLeft, SWT.NONE);
        groupColor.setLayoutData(new BorderData(SWT.BOTTOM));
        groupColor.setLayout(new BorderLayout());

        final Group groupColorPrimary = new Group(groupColor, SWT.NONE);
        groupColorPrimary.setLayoutData(new BorderData(SWT.TOP));
        groupColorPrimary.setLayout(new BorderLayout());
        groupColorPrimary.setText(Translator.translate("editor.menu.color.primary"));

        final Label colorPrimaryLabel = new Label(groupColorPrimary, SWT.NONE);
        colorPrimaryLabel.setText("                              ");
        colorPrimaryLabel.setBackground(ToolStorage.getSelf().colorPrimary);

        Button colorPrimaryButton = new Button(groupColorPrimary, SWT.PUSH);
        colorPrimaryButton.setText(Translator.translate("editor.menu.color.change"));
        colorPrimaryButton.setLayoutData(new BorderData(SWT.RIGHT));
        colorPrimaryButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                ColorDialog dlg = new ColorDialog(shell);
                dlg.setRGB(colorPrimaryLabel.getBackground().getRGB());
                dlg.setText(Translator.translate("editor.window.color.select"));
                RGB rgb = dlg.open();
                if (rgb != null) {
                    ToolStorage.getSelf().colorPrimary.dispose();
                    ToolStorage.getSelf().colorPrimary = new Color(shell.getDisplay(), rgb);
                    colorPrimaryLabel.setBackground(ToolStorage.getSelf().colorPrimary);
                }
            }
        });

        final Group groupColorSecondary = new Group(groupColor, SWT.NONE);
        groupColorSecondary.setLayoutData(new BorderData(SWT.BOTTOM));
        groupColorSecondary.setLayout(new BorderLayout());
        groupColorSecondary.setText(Translator.translate("editor.menu.color.secondary"));

        final Label colorSecondaryLabel = new Label(groupColorSecondary, SWT.NONE);
        colorSecondaryLabel.setText("                              ");
        colorSecondaryLabel.setBackground(ToolStorage.getSelf().colorSecondary);

        Button colorSecondaryButton = new Button(groupColorSecondary, SWT.PUSH);
        colorSecondaryButton.setText(Translator.translate("editor.menu.color.change"));
        colorSecondaryButton.setLayoutData(new BorderData(SWT.RIGHT));
        colorSecondaryButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                ColorDialog dlg = new ColorDialog(shell);
                dlg.setRGB(colorSecondaryLabel.getBackground().getRGB());
                dlg.setText(Translator.translate("editor.window.color.select"));
                RGB rgb = dlg.open();
                if (rgb != null) {
                    ToolStorage.getSelf().colorSecondary.dispose();
                    ToolStorage.getSelf().colorSecondary = new Color(shell.getDisplay(), rgb);
                    colorSecondaryLabel.setBackground(ToolStorage.getSelf().colorSecondary);
                }
            }
        });
    }

}
