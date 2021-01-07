import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class SateliteSprite extends Sprite {

	private static final int VELOCITY = 25;

	public static double GRAVITATIONAL_CONSTANT = 1E-6;
	
	protected Image image_straight;
	
	protected double velocityX = 000;        	//PIXELS PER SECOND
	protected double velocityY = 0;          	//PIXELS PER SECOND
	protected double mass = 0;
	protected boolean anchored = false;
	protected double accelerationX = 0;          			//PIXELS PER SECOND PER SECOND 
	protected double accelerationY = 0;          		//PIXELS PER SECOND PER SECOND 
	
	public SateliteSprite(double currentX, double currentY, double velocityX, double velocityY, double mass, boolean anchored) {

		super();
		this.currentX = currentX;
		this.currentY = currentY;
		this.velocityX = velocityX;
		this.velocityY = velocityY;
		this.mass = mass;
		this.anchored = anchored;

		try {
			this.defaultImage = ImageIO.read(new File("res/simple-sprite.png"));
			this.IMAGE_HEIGHT = this.defaultImage.getHeight(null);
			this.IMAGE_WIDTH = this.defaultImage.getWidth(null);
		}
		catch (IOException e) {
			System.out.println(e.toString());
		}		
	}
	
	public double getVelocityX() {
		return velocityX;
	}

	public void setVelocityX(double velocityX) {
		this.velocityX = velocityX;
	}

	public double getVelocityY() {
		return velocityY;
	}

	public void setVelocityY(double velocityY) {
		this.velocityY = velocityY;
	}

	public double getMass() {
		return mass;
	}

	public void setMass(double mass) {
		this.mass = mass;
	}

	public boolean isAnchored() {
		return anchored;
	}

	@Override
	public void setSprites(ArrayList<Sprite> staticSprites) {
		// TODO Auto-generated method stub
		this.sprites = staticSprites;
		
	}

	@Override
	public void setKeyboard(KeyboardInput keyboard) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean checkCollisionWithSprites(double x, double y) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void setBarriers(ArrayList<Rectangle> barriers) {
		this.barriers = barriers;
	}
	
	public void setPlayers(ArrayList<Sprite> players) {
		this.sprites = players;
	}
	

	@Override
	public long getHeight() {
		return IMAGE_HEIGHT;
	}

	@Override
	public long getWidth() {
		return IMAGE_WIDTH;
	}	
	
	public double getMinX() {
		return currentX;
	}

	@Override
	public double getMaxX() {
		// TODO Auto-generated method stub
		return currentX + IMAGE_WIDTH;
	}

	public void setMinX(double currentX) {
		this.currentX = currentX;
	}

	public void setMinY(double currentY) {
		this.currentY = currentY;
	}

	public double getMinY() {
		return currentY;
	}

	@Override
	public double getMaxY() {
		// TODO Auto-generated method stub
		return currentY + IMAGE_HEIGHT;
	}
	

	@Override
	public Image getImage() {
		return defaultImage;
	}
		
	@Override
	public void update(KeyboardInput keyboard, long actual_delta_time) {

		double movement_x = (this.velocityX * actual_delta_time * 0.001);
	    double movement_y = (this.velocityY * actual_delta_time * 0.001);
	    this.currentX += movement_x;
	    this.currentY += movement_y;
		
		for (Sprite other : sprites) {
			if (other instanceof SimpleSprite) {
				SimpleSprite player = (SimpleSprite)other;
				
				double newX = currentX;
				double newY = currentY;
				
				
				//calculate the attraction vector
				double deltaX = player.currentX - this.currentX;
				double deltaY = player.currentY - this.currentY;
			
				
				//implement logic for moving towards sprite
				if (deltaX > 0) {
					this.velocityX = VELOCITY;
				}
				else if (deltaX < 0) {
					this.velocityX = -VELOCITY;
				}
				else {
					this.velocityX = 0;
				}
				
				if (deltaY > 0) {
					this.velocityY = VELOCITY;
				}
				else if (deltaY < 0) {
					this.velocityY = -VELOCITY;
				}
				else {
					this.velocityY = 0;
				}
				newX +=  actual_delta_time * 0.001 * velocityX;
				newY +=  actual_delta_time * 0.001 * velocityY;
				

				if (checkCollisionWithBarrier(newX, newY) == false) {
					
					this.currentX = newX;
					this.currentY = newY;
				}
			}
		}

				
	}			
	

	@Override
	public boolean checkCollisionWithBarrier(double x, double y) {
		boolean isColliding = false;
		
		for (Rectangle barrier : barriers) {			
			//colliding in x dimension?	
			if ( !( (x + this.IMAGE_WIDTH) < barrier.getMinX() || x > barrier.getMaxX())) {			
				//colliding in y dimension?	
				if ( !( (y + this.IMAGE_HEIGHT) < barrier.getMinY() || y > barrier.getMaxY())) {								
					isColliding = true;
					break;
				}
			}
		}
		
		return isColliding;		
	}
}
