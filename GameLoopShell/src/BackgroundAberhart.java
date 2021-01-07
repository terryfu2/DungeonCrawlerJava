import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class BackgroundAberhart extends Background {

    private Image aberhart;
    private Image blank;
    private int backgroundWidth = 0;
    private int backgroundHeight = 0;

    public BackgroundAberhart() {
    	try {
    		this.aberhart = ImageIO.read(new File("res/tileable-classic-nebula.jpg"));
    		this.blank = ImageIO.read(new File("res/blank.png"));
    		backgroundWidth = aberhart.getWidth(null);
    		backgroundHeight = blank.getHeight(null);
    		
    	}
    	catch (IOException e) {
    		//System.out.println(e.toString());
    	}		
    }
	
	public Tile getTile(int col, int row) {
		//row is an index of tiles, with 0 being the at the origin
		//col is an index of tiles, with 0 being the at the origin
		int x = (col * backgroundWidth);
		int y = (row * backgroundHeight);
		Tile newTile = null;
		
		if (((col + row) % 2) == 0 ) {
			newTile = new Tile(aberhart, x, y, backgroundWidth, backgroundHeight, false);
		} else {
			newTile = new Tile(blank, x, y, backgroundWidth, backgroundHeight, false);
		}
			
		
		
		return newTile;
	}
	
	public int getCol(int x) {
		//which col is x sitting at?
		int col = 0;
		if (backgroundWidth != 0) {
			col = (x / backgroundWidth);
			if (x < 0) {
				return col - 1;
			}
			else {
				return col;
			}
		}
		else {
			return 0;
		}
	}
	
	public int getRow(int y) {
		//which row is y sitting at?
		int row = 0;
		
		if (backgroundHeight != 0) {
			row = (y / backgroundHeight);
			if (y < 0) {
				return row - 1;
			}
			else {
				return row;
			}
		}
		else {
			return 0;
		}
	}
	
}


