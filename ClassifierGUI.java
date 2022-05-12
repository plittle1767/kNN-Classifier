package a7;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 * This defines a program to visually explore the kNN classifier.
 * @author dejohnso
 *
 */
public class ClassifierGUI extends JPanel implements MouseListener, ChangeListener {

	// This is all user interface code
	private SampleDisplayPanel display; // The view of the samples and the k nearest
	private JScrollPane sampleDisplay; // The images that are the samples
	private JPanel samplePanel; // The panel that is scrolled
	private JPanel buttonPanel; // A place to hold the buttons
	private JSlider kSlider; // a slider to set the k value
	private JTextArea result; // text output from the classifier

	// This is the data the classifier needs
	private ArrayList<? extends Sample2D> fullSamples; // This means the ArrayList can be assigned an ArrayList of a subtype of Samples.
	private int k;
	private Sample2D unknown;
	
	private final static int INITIAL_K = 5;
	
	/**
	 * Create the interface for the classifier and load the samples
	 */
	public ClassifierGUI() {
		super();
		k = INITIAL_K;
		// Create a vertical layout
		LayoutManager layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		this.setLayout(layout);
				
		// Read the samples used by the classifier
		fullSamples = Face.readSamples(new File("src/a7/faces"));

		// The top of the program shows the samples in the sample space
		display = new SampleDisplayPanel(this);
		display.setPreferredSize(new Dimension(500,300));
		this.add(display);
		
		// Change the k value with a slider
		kSlider = new JSlider(JSlider.HORIZONTAL, 1, 11, 5);
		kSlider.setMajorTickSpacing(2);
		kSlider.setMinorTickSpacing(1);
		kSlider.setPaintTicks(true);
		kSlider.setPaintLabels(true);
		this.add(kSlider);
		kSlider.addChangeListener(this);
		
		// Show all the images
		samplePanel = new JPanel();
  		sampleDisplay = new JScrollPane(samplePanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
  		sampleDisplay.setPreferredSize(new Dimension(1500, 175));
		this.add(sampleDisplay);

		// Output classifier results here
		result = new JTextArea("");
		result.setPreferredSize(new Dimension(500, 40));
		this.add(result);
		
		// Add buttons to control the program
		JPanel buttonPanel = addButtons();
		this.add(buttonPanel);

		addFacePanels();
	}
	
	/**
	 * @return the list of samples.
	 */
	public ArrayList<? extends Sample2D> getSamples() {
		return fullSamples;
	}

	/**
	 * This computes the k nearest samples. It is really just repeating
	 * code so the GUI can draw them before the actual classification 
	 * is done.
	 * @return the k closest samples in the form of a Neighbor.
	 */
	public ArrayList<Neighbor> getkClosestSet() {
		if (unknown == null)
			return null;
		
		ArrayList<Sample2D> others = new ArrayList<>();
		for (Sample2D sample : fullSamples) {
			if (sample != unknown)
				others.add(sample);
		}
		KNNClassifier classifier = new KNNClassifier(k, others);
		ArrayList<Neighbor> neighbors = classifier.computeNeighbors(unknown);
		return classifier.findKClosest(neighbors);
	}

	/**
	 * @return the unknown sample
	 */
	public Sample2D getUnknown() {
		return unknown;
	}
	
	/**
	 * For every Face stored in the classifier, make an image panel to display the Face. 
	 */
	private void addFacePanels() {
		samplePanel.removeAll();
  		for (Sample2D sample : fullSamples) {
  			FacePanel facePanel = new FacePanel((Face)sample, 100, 140);
  			facePanel.addMouseListener(this); // These panels are clickable to set the unknown sample.
  			samplePanel.add(facePanel);
  		}
		samplePanel.revalidate();
	}
	
	/**
	 * A helper method to generate all the buttons and their actions.
	 * Use anonymous Listener classes to handle clicks.
	 * @return
	 */
	private JPanel addButtons() {
		buttonPanel = new JPanel();
  		buttonPanel.setPreferredSize(new Dimension(500, 50));

  		JButton classifyButton = new JButton("Classify");
  		classifyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (unknown != null) {
					ArrayList<Sample2D> others = new ArrayList<>();
					for (Sample2D sample : fullSamples) {
						if (sample != unknown)
							others.add(sample);
					}
					KNNClassifier classifier = new KNNClassifier(k, others);
					String classification = classifier.classifyUnknownSample(unknown);
					result.setText("  Unknown: " + unknown.getClassification() + "\n  Classified as: " + classification);
					repaint();					
				}
			}
  		});
  		buttonPanel.add(classifyButton);

  		JButton quitButton = new JButton("Quit");
  		quitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
  		});
  		buttonPanel.add(quitButton);

  		return buttonPanel;
	}
			
	private static final long serialVersionUID = 1L;

	/**
	 * Run the GUI program.
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame frame = new JFrame("kNN Explorer");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(500,600));
		ClassifierGUI gui = new ClassifierGUI();
		frame.setContentPane(gui);
		frame.pack();
		frame.setVisible(true);
	}

	/* 
	 * Only listens for clicks on the Face images. Sets the unknown and highlights the image.
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		for (Component c : samplePanel.getComponents()) {
			((JPanel)c).setBorder(null);
		}
		FacePanel clicked = (FacePanel)e.getSource();
		clicked.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
		unknown = clicked.getFace();
		result.setText("");
		repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		k = kSlider.getValue();
		repaint();
	}

}
