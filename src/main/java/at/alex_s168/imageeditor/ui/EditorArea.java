package at.alex_s168.imageeditor.ui;

import at.alex_s168.imageeditor.ImageEditor;
import at.alex_s168.imageeditor.api.PixelMap;
import at.alex_s168.imageeditor.util.ColorHelper;
import at.alex_s168.imageeditor.util.ImagePixelHelper;
import static at.alex_s168.imageeditor.util.ColorHelper.colorConvert;
import de.m_marvin.logicsim.ui.TextRenderer;
import de.m_marvin.univec.impl.Vec2d;
import de.m_marvin.univec.impl.Vec2i;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL33;
import org.lwjgl.opengl.GLCapabilities;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class EditorArea extends Canvas implements MouseListener, MouseMoveListener, KeyListener {
	
	protected GLData glData;
	protected GLCanvas glCanvas;
	protected GLCapabilities glCapabilities;
	protected boolean resized;
	protected boolean initialized = false;
	protected int textureId;

	protected boolean mouseDown = false;
	protected Vec2d mousePosition;
	protected Vec2d realMousePosition;

	protected double scale = 1;

	public PixelMap rOut = new PixelMap();
	
	public EditorArea(Composite parent) {
		super(parent, SWT.NONE);
		this.setLayout(new FillLayout());
		this.glData = new GLData();
		this.glData.doubleBuffer = true;
		this.glCanvas = new GLCanvas(this, SWT.None, glData);
		this.glCanvas.addListener(SWT.Resize, (event) -> this.resized = true);
		this.glCanvas.addMouseListener(this);
		this.glCanvas.addMouseMoveListener(this);
		this.glCanvas.addKeyListener(this);
		initOpenGL();
	}

	public void updateMousePos(MouseEvent e) {
		mousePosition=new Vec2d(e.x, e.y);
	}

	@Override public void mouseUp(MouseEvent event) { mouseDown=false; updateMousePos(event); }
	@Override public void mouseDown(MouseEvent event) { mouseDown=true; updateMousePos(event); }
	@Override public void mouseMove(MouseEvent event) { updateMousePos(event); }

	@Override
	public void mouseDoubleClick(MouseEvent event) {}

	@Override
	public void keyPressed(KeyEvent event) {}

	@Override
	public void keyReleased(KeyEvent e) {
		String key = Character.toString(e.character);
		if(key.equals("+")) {
			scale += 0.1;
		}
		if(key.equals("-")) {
			scale -= 0.1;
		}

		if(((e.stateMask & SWT.CTRL) == SWT.CTRL) && (e.keyCode == 's')) {
			saveFile(ImageEditor.getInstance().getEditor().openFile);
		}
	}
	
	protected void initOpenGL() {
		this.glCanvas.setCurrent();
		this.glCapabilities = GL.createCapabilities();

		textureId = GL33.glGenTextures();
		GL33.glEnable(GL33.GL_TEXTURE_2D);
		GL33.glBindTexture(GL33.GL_TEXTURE_2D, textureId);
		GL33.glTexParameterf(GL33.GL_TEXTURE_2D, GL33.GL_TEXTURE_MIN_FILTER, GL33.GL_NEAREST);
		GL33.glTexParameterf(GL33.GL_TEXTURE_2D, GL33.GL_TEXTURE_MAG_FILTER, GL33.GL_NEAREST);
		GL33.glBindTexture(GL33.GL_TEXTURE_2D, 0);

		GL33.glMatrixMode(GL33.GL_PROJECTION);
		GL33.glLoadIdentity();
		GL33.glOrtho(0,1000,1000,0, 0, 1); // Hier einstellen was Links/Rechts/Oben/Unten ist (in deinen Bildschirmkoordinaten=
		GL33.glEnable(GL33.GL_BLEND);
		GL33.glBlendFunc(GL33.GL_SRC_ALPHA, GL33.GL_ONE_MINUS_SRC_ALPHA);
		GL33.glDisable(GL33.GL_DEPTH_TEST);
		GL33.glDisable(GL33.GL_CULL_FACE);

        this.glCanvas.addDisposeListener((e) -> TextRenderer.cleanUpOpenGL());
	    this.resized = true;
	    this.initialized = false;
	}

	public void updateImage(int[] pixels, int width, int height) {
		GL33.glBindTexture(GL33.GL_TEXTURE_2D, textureId);
		GL33.glTexImage2D(GL33.GL_TEXTURE_2D, 0, GL33.GL_RGBA8, width, height, 0,  GL33.GL_BGRA, GL33.GL_UNSIGNED_INT_8_8_8_8_REV, pixels);
		GL33.glBindTexture(GL33.GL_TEXTURE_2D, 0);
	}

	public void updateImage(PixelMap m) {
		updateImage(m.pix, m.getWidth(), m.getHeight());
	}

	public void render() {
		
		if (this.isDisposed() || this.glCanvas == null) return;
		
		this.glCanvas.setCurrent();

		if (!initialized) {
			initOpenGL();
		}

		if (resized) {
			GL33.glViewport(0, 0, this.getSize().x, this.getSize().y);
	        GL33.glLoadIdentity();
		    GL33.glOrtho(0.0, this.getSize().x, this.getSize().y, 0.0, 0.0, 1.0);
			GL33.glClearColor(0, 0, 0, 1);
		}

		// Vor jedem Rendering ausführen
		GL33.glClear(GL33.GL_COLOR_BUFFER_BIT);



		if(mouseDown) {
			int c = colorConvert(ImageEditor.getInstance().getEditor().colorPrimary);
			rOut.set(realMousePosition.add(1d,0d), c);
			rOut.set(realMousePosition.add(0d,1d), c);
			rOut.set(realMousePosition.add(-1d,0d), c);
			rOut.set(realMousePosition.add(0d,-1d), c);
			rOut.set(realMousePosition, c);
		}
		updateImage(rOut);




		// Ausführen um die Textur zu rendern
		int x = ImageEditor.getInstance().getEditor().scrollX;
		int y = ImageEditor.getInstance().getEditor().scrollY;

		int w = (int) (rOut.getWidth()*scale);
		int h = (int) (rOut.getHeight()*scale);
		GL33.glBindTexture(GL33.GL_TEXTURE_2D, textureId);
		GL33.glBegin(GL33.GL_QUADS);
		GL33.glTexCoord2f(0, 0);
		GL33.glVertex2f(x, y);
		GL33.glTexCoord2f(1, 0);
		GL33.glVertex2f(x + w, y);
		GL33.glTexCoord2f(1, 1);
		GL33.glVertex2f(x + w, y + h);
		GL33.glTexCoord2f(0, 1);
		GL33.glVertex2f(x, y + h);
		GL33.glEnd();

		glCanvas.swapBuffers();

		if(mousePosition != null) {
			realMousePosition = mousePosition.sub((double) x, (double) y).div(scale);
		}

		ImageEditor.getInstance().getEditor().sliderYMax = rOut.getHeight();
		ImageEditor.getInstance().getEditor().sliderXMax = rOut.getWidth();

	}

	public GLCanvas getGlCanvas() {
		return glCanvas;
	}

	public void openFile(File filePath) {
		this.rOut = ImagePixelHelper.getPixFromImage(filePath);
	}

	public void saveFile(File filePath) {
		int xLenght = rOut.getWidth();
		int yLength = rOut.getHeight();

		BufferedImage bi = new BufferedImage(xLenght, yLength, BufferedImage.TYPE_INT_BGR);

		for(int x = 0; x < xLenght; x++) {
			for(int y = 0; y < yLength; y++) {
				bi.setRGB(x, y, rOut.get(x,y));
			}
		}
		try {
			ImageIO.write(bi, filePath.getName().split("\\.")[filePath.getName().split("\\.").length-1], filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void replace(Color what, Color to, int tolerance, boolean smart) {

		int it = 0;
		for (int pixel : rOut.pix) {
			Color c = new Color(
					(pixel>>16)&0xFF,
					(pixel>>8)&0xFF,
					(pixel)&0xFF
			);

			Color diff = ColorHelper.sub(c, what);

			if (Math.abs(ColorHelper.sum(diff)) < tolerance) {
				if(smart) {
					rOut.pix[it] = ColorHelper.colorConvert( ColorHelper.add(to, diff) );
				}
				else {
					rOut.pix[it] = ColorHelper.colorConvert(to);
				}
			}
			it++;
		}

	}
}
