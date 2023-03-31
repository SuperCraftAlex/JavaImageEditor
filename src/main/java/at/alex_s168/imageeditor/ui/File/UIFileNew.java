package at.alex_s168.imageeditor.ui.File;

import at.alex_s168.imageeditor.ImageEditor;
import at.alex_s168.imageeditor.features.image.FeatureImageColor;
import at.alex_s168.imageeditor.ui.Editor;
import at.alex_s168.imageeditor.ui.EditorArea;
import at.alex_s168.imageeditor.ui.UIBottomButtons;
import at.alex_s168.imageeditor.util.Translator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.BorderLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

public class UIFileNew {


    public static void genFileNewButton(Menu fileMenu, Shell shell, Shell mainShell, Display display) {

        MenuItem newOpt = new MenuItem(fileMenu, SWT.PUSH);
        newOpt.setText(Translator.translate("editor.menu.file.new"));
        newOpt.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {

                Shell w = new Shell(display);
                w.setSize(mainShell.getSize().x / 3, mainShell.getSize().y / 3);
                w.setText(Translator.translate("editor.window.file.new"));
                w.setLayout(new GridLayout(2, true));

                final int[] size_x = {0};
                final int[] size_y = {0};

                Label labelWidth = new Label(w, SWT.NULL);
                labelWidth.setText("Width: ");

                final Text width = new Text(w, SWT.SINGLE | SWT.BORDER);

                Label labelHeight = new Label(w, SWT.NULL);
                labelHeight.setText("Height: ");

                final Text height = new Text(w, SWT.SINGLE | SWT.BORDER);

                final Button buttonOK = new Button(w, SWT.PUSH);
                buttonOK.setText("Ok");
                buttonOK.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
                Button buttonCancel = new Button(w, SWT.PUSH);
                buttonCancel.setText("Cancel");

                width.addListener(SWT.Modify, event -> {
                    try {
                        size_x[0] = Integer.parseInt(width.getText());
                        buttonOK.setEnabled(true);
                    } catch (Exception e1) {
                        buttonOK.setEnabled(false);
                    }
                });

                height.addListener(SWT.Modify, event -> {
                    try {
                        size_y[0] = Integer.parseInt(height.getText());
                        buttonOK.setEnabled(true);
                    } catch (Exception e1) {
                        buttonOK.setEnabled(false);
                    }
                });

                buttonOK.addListener(SWT.Selection, new Listener() {
                    public void handleEvent(Event event) {
                        ImageEditor.getInstance().getEditor().editorArea.newFile(size_x[0], size_y[0]);
                        w.dispose();
                    }
                });

                buttonCancel.addListener(SWT.Selection, event -> w.dispose());

                w.open();

            }
        });

    }
}
