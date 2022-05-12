package a7;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

/**
 * This class displays sample on an XY graph and draws lines to the nearest neighbors.
 * Samples are colored by their classification. 
 * The unknown shows up as yellow.
 * @author dejohnso
 *
 */
public class SampleDisplayPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private ClassifierGUI gui;
	
	public SampleDisplayPanel(ClassifierGUI initGUI) {
		super();
		gui = initGUI;
	}

	/**
	 * Positions the oval centered at x,y rather than at a corner.
	 * @param g2d
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	private void drawCenteredOval(Graphics2D g2d, int x, int y, int width, int height) {
		g2d.fillOval(x - width/2,  y- height/2,  width,  height);
	}
	
	/* 
	 * Draws the sample on a XY graph and connects to nearest neighbors.
	 */
	@Override 
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		int h = getHeight();
		int w = getWidth();
	    g2d.translate(0, h);
	    g2d.scale(1.25, -1.25);
		ArrayList<Neighbor> kClosest = gui.getkClosestSet();
		Sample2D unknown = gui.getUnknown();

		// Draw the nearest neighbor lines first show they appear "under" the samples.
		if (kClosest != null) {
			for (Neighbor neighbor : kClosest) {
				g2d.setColor(Color.BLACK);
				g2d.setStroke(new BasicStroke(2));
				Sample2D sample = neighbor.getSample();
				g2d.drawLine((int)(sample.getX()*w),  (int)(sample.getY()*h),  (int)(unknown.getX()*w), (int)(unknown.getY()*h));
			}
		}

		// Draw the samples as colored circles.
		for (Sample2D face : gui.getSamples()) {
			if (face.getClassification().equals("smiling"))
				g2d.setColor(Color.GREEN);
			if (face.getClassification().equals("neutral"))
				g2d.setColor(Color.GRAY);
			if (face.getClassification().equals("surprised"))
				g2d.setColor(Color.RED);
			drawCenteredOval(g2d, (int)(face.getX()*w),  (int)(face.getY()*h),  10,  10);
			// Draw the unknown as a larger yellow dot.
			if (gui.getUnknown() != null) {
				g2d.setColor(Color.YELLOW);
				drawCenteredOval(g2d, (int)(gui.getUnknown().getX()*w),  (int)(gui.getUnknown().getY()*h), 8, 8);
			}
		}
	}
}
