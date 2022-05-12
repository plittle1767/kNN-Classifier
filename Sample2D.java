package a7;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;

/**
 * A Sample2D represents a thing that a kNN classifier can
 * classify.
 * 
 * It has
 *  - an x and y value 
 *  - classification: the string saying what "thing" the sample is.
 *  
 * @author dejohnso
 *
 */
public class Sample2D {
	
	private double x;
	private double y;
	String classification;

	/**
	 * @param initX the x value or coordinate
	 * @param initY the y value or coordinate
	 * @param initClassification the known classification of the sample
	 */
	public Sample2D(double initX, double initY, String initClassification) {
		x = initX;
		y = initY;
		classification = initClassification;
	}
	
	/**
	 * @return the x coordinate
	 */
	public double getX() {
		return x;
	}

	/**
	 * @return the y coordinate
	 */
	public double getY() {
		return y;
	}
		
	/**
	 * @return the classification for the sample
	 */
	public String getClassification() {
		return classification;
	}

	/**
	 * Calculate the distance between two sample: this and other.
	 * @param other the other sample
	 * @return the distance
	 */
	public double calculateDistance(Sample2D other) {
		double deltaX = x - other.x;
		double deltaY = y - other.y;

		return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
	}
}
