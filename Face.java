package a7;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;

/**
 * A Face has a 
 * - image (a drawable BufferedImage) 
 * 
 * @author dejohnso
 *
 */
public class Face extends Sample2D {

	private BufferedImage image;

	public Face(double initWidth, double initHeight, String initClassification, String initName, BufferedImage img) {
		super(initWidth, initHeight, initClassification);
		image = img;
	}

	public BufferedImage getImage() {
		return image;
	}

	public String getClassification() {
		return classification;
	}

	/**
	 * Reads files in a folder. The files are .gif files, and the name of the file
	 * encodes the expression and the face width in pixels, the mouth width in
	 * pixels, and mouth height in pixels.
	 * 
	 * @param folder
	 * @return
	 */
	public static ArrayList<Face> readSamples(File folder) {
		ArrayList<Face> faces = new ArrayList<Face>();
		File[] files = folder.listFiles();
		System.out.println(Arrays.deepToString(files));
		// If this pathname does not denote a directory, then listFiles() returns null.
		for (File file : files) {
			if (file.isFile()) {
				String origFilename = file.getName();
				boolean isGif = origFilename.contains("gif");
				if (isGif) {
					String filename = origFilename.replaceAll("\\.\\w+", "");

					String[] items = filename.split("_");
					String name = items[0];
					String classification = items[1];
					int faceWidth = Integer.parseInt(items[2]);
					int mouthWidth = Integer.parseInt(items[3]);
					int mouthHeight = Integer.parseInt(items[4]);

					BufferedImage img = null;
					try {
						img = ImageIO.read(new File(file.getAbsolutePath()));
					} catch (IOException e) {
						System.out.println("Face file " + origFilename + " not found.");
						System.exit(-1);
					}

					Face face = new Face(mouthWidth / (double) faceWidth, mouthHeight / (double) faceWidth,
							classification, name, img);
					faces.add(face);
				}
			}
		}
		return faces;
	}

}
