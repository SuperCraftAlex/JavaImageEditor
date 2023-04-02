package at.alex_s168.imageeditor.ui;

import at.alex_s168.imageeditor.ui.file.UIFileNew;
import at.alex_s168.imageeditor.util.Translator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

public class UIWelcome {

    public static void genWelcomeScreen(Shell mainShell, Display display) {

        Shell w = new Shell(display);
        w.setSize(mainShell.getSize().x / 4, mainShell.getSize().y / 4);
        w.setText(Translator.translate("editor.window.welcome"));
        w.setLayout(new GridLayout(2, true));

        Button openImg = new Button(w, SWT.PUSH);
        openImg.setText("Open...");
        openImg.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if(Editor.getSelf().loadFile()) {
                    w.dispose();
                } else {
                    w.forceActive();
                }
            }
        });

        new Button(w, SWT.NONE).setVisible(false);

        Button newImg = new Button(w, SWT.PUSH);
        newImg.setText("New...");
        newImg.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                UIFileNew.newFileWindow(display, mainShell).addListener(SWT.Selection, event -> {
                    w.dispose();
                });

            }
        });

        Button buttonClose = new Button(w, SWT.PUSH);
        buttonClose.setText("Close");
        buttonClose.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));

        buttonClose.addListener(SWT.Selection, event -> w.dispose());

        w.open();

    }

}
