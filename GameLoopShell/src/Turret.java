import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
public class Turret extends Sprite{
	private int range;
	private double damage;
	private double rateOfFire;
	private int health;
	private double shootingAngle;
	private double reloadTime;

	public Turret(double currentX, double currentY, int floor){
		this.currentX = currentX;
		this.currentY = currentY;
		try {
			this.defaultImage = ImageIO.read(new File("res/Turret.png"));
			this.IMAGE_HEIGHT = this.defaultImage.getHeight(null);
			this.IMAGE_WIDTH = this.defaultImage.getWidth(null);
		}
		catch (IOException e) {
			System.out.println(e.toString());
		}
		if (floor == 2){
			this.range = 500;
			this.damage = 1.5;
			this.rateOfFire = 1;
			this.health = 3;
		}
		else if (floor == 3){
			this.range = 550;
			this.damage = 1.5;
			this.rateOfFire = 2;
			this.health = 4;
		}
		else if (floor == 4){
			this.range = 550;
			this.damage = 2;
			this.rateOfFire = 2;
			this.health = 5;
			
		}
		
	}
	public double getHealth() {
		return health;
	}
	@Override
	public void update(KeyboardInput keyboard, long actual_delta_time) {
		shootingAngle = 0;
		for (Sprite other : sprites) {
			if (other instanceof SimpleSprite) {
				SimpleSprite player = (SimpleSprite)other;
				
				double deltaX = Math.abs(player.currentX - this.currentX);
				double deltaY = Math.abs(player.currentY - this.currentY);
				
				shootingAngle += Math.atan(deltaY/deltaX);
				if (player.currentX < this.currentX && player.currentY > this.currentY){
					shootingAngle = Math.PI - shootingAngle ;
				}
				else if(player.currentX < this.currentX && player.currentY < this.currentY){
					shootingAngle += Math.PI;
				}
				else if(player.currentX > this.currentX && player.currentY < this.currentY){
					shootingAngle = Math.PI *2 - shootingAngle;
				}
				
				
			}
		}
		reloadTime -= actual_delta_time;
		if (reloadTime < 0) {
			shoot();
		}
		
	}

	public void shoot() {

		double bulletVelocity = 500; // + currentVelocity;
//		double angleInRadians = Math.toRadians(shootingAngle);
		double bulletVelocityX = Math.cos(shootingAngle) * bulletVelocity;
		double bulletVelocityY = Math.sin(shootingAngle) * bulletVelocity;
		
		double bulletCurrentX = (this.currentX + (this.IMAGE_WIDTH / 2));
		double bulletCurrentY = (this.currentY + (this.IMAGE_HEIGHT / 2));

		BulletSprite bullet = new BulletSprite(bulletCurrentX, bulletCurrentY, bulletVelocityX, bulletVelocityY);
		sprites.add(bullet);
			
		reloadTime = 100;			
	}
	@Override
	public double getMinX() {
		// TODO Auto-generated method stub
		return currentX;
	}

	@Override
	public double getMaxX() {
		// TODO Auto-generated method stub
		return currentX + IMAGE_WIDTH;
	}

	@Override
	public double getMinY() {
		// TODO Auto-generated method stub
		return currentY;
	}

	@Override
	public double getMaxY() {
		// TODO Auto-generated method stub
		return currentY + IMAGE_HEIGHT;
	}

	@Override
	public long getHeight() {
		// TODO Auto-generated method stub
		return IMAGE_HEIGHT;
	}

	@Override
	public long getWidth() {
		// TODO Auto-generated method stub
		return IMAGE_HEIGHT;
	}

	@Override
	public Image getImage() {
		// TODO Auto-generated method stub
		return this.defaultImage;
	}

	@Override
	public void setMinX(double currentX) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMinY(double currentY) {
		// TODO Auto-generated method stub
		
	}
	public void setPlayers(ArrayList<Sprite> players) {
		this.sprites = players;
	}
	
	@Override
	public void setBarriers(ArrayList<Rectangle> barriers) {
		this.barriers = barriers;
		
	}

	@Override
	public void setSprites(ArrayList<Sprite> staticSprites) {
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

	@Override
	public boolean checkCollisionWithBarrier(double x, double y) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
