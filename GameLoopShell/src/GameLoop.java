import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

//test git WC
//test git TF
public class GameLoop extends JFrame {

	final public static int FRAMES_PER_SECOND = 60;
	final public static int SCREEN_HEIGHT = 600;
	final public static int SCREEN_WIDTH = 700;
	final public static Color BARRIER_COLOR = Color.BLACK;
	public static boolean CENTER_ON_PLAYER = true;

	private JPanel panel = null;
	private JButton btnPauseRun;
	private JLabel lblTimeLabel;
	private JLabel lblTime;

	private static Thread loop;
	private CastleBackground background = new CastleBackground();    
	private GrassBackground grass = new GrassBackground();
	private KeyboardInput keyboard = new KeyboardInput();

	long current_time = 0;								//MILLISECONDS
	long next_refresh_time = 0;							//MILLISECONDS
	long last_refresh_time = 0;
	long minimum_delta_time = 1000 / FRAMES_PER_SECOND;	//MILLISECONDS
	long actual_delta_time = 0;							//MILLISECONDS
	long elapsed_time = 0;
	private boolean isPaused = false;

	int xOffset = 0;
	int yOffset = 0;

	ArrayList<Rectangle> barriers = new ArrayList<Rectangle>();
	ArrayList<Sprite> sprites = new ArrayList<Sprite>();
	ArrayList<Sprite> spritesToDispose = new ArrayList<Sprite>();
	Sprite me = null;

	public GameLoop()
	{
		super("");

		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				keyboard.keyPressed(arg0);
			}
			@Override
			public void keyReleased(KeyEvent arg0) {
				keyboard.keyReleased(arg0);
			}
		});
		this.setFocusable(true);

		getContentPane().setBackground(Color.BLACK);
		Container cp = getContentPane();

		panel = new DrawPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		getContentPane().setLayout(null);

		btnPauseRun = new JButton("||");
		btnPauseRun.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				btnPauseRun_mouseClicked(arg0);
			}
		});

		btnPauseRun.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnPauseRun.setBounds(20, 20, 48, 32);
		getContentPane().add(btnPauseRun);

		lblTime = new JLabel("000");
		lblTime.setForeground(Color.WHITE);
		lblTime.setFont(new Font("Tahoma", Font.BOLD, 30));
		lblTime.setBounds(171, 22, 302, 30);
		getContentPane().add(lblTime);

		panel.setLayout(null);
		panel.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		setSize(SCREEN_WIDTH + 20, SCREEN_HEIGHT + 36);

		lblTimeLabel = new JLabel("Time: ");
		lblTimeLabel.setForeground(Color.WHITE);
		lblTimeLabel.setFont(new Font("Tahoma", Font.BOLD, 30));
		lblTimeLabel.setBounds(78, 22, 97, 30);
		getContentPane().add(lblTimeLabel);

		getContentPane().setComponentZOrder(lblTimeLabel, 0);
		getContentPane().setComponentZOrder(lblTime, 0);
		getContentPane().setComponentZOrder(btnPauseRun, 0);

		createBarriers();
		createSprites();

	}

	private void createBarriers() {
		this.barriers = background.getBarriers();
	}

	private void createSprites() {

		//   		me = new SateliteSprite(SCREEN_WIDTH/2,SCREEN_HEIGHT/2,0,0,1E15,false);
		//   		sprites.add(me);
		//   		
		//   		for (int i = 0; i < 5; i++){
		//			SateliteSprite sateliteSprite = new SateliteSprite(SCREEN_WIDTH/2 + 100 + 10*i,SCREEN_HEIGHT/2,0,50,1E10,false);
		//			sprites.add(sateliteSprite);
		//   		}
		me = new SimpleSprite(CastleBackground.START_X,CastleBackground.START_Y);
		sprites.add(me);
		Turret test2 = new Turret(CastleBackground.START_X + 10,CastleBackground.START_Y,2);
		sprites.add(test2);
		SateliteSprite test = new SateliteSprite(CastleBackground.START_X, CastleBackground.START_Y,0, 0, 0, false);
		sprites.add(test);

		for (Sprite sprite : sprites) {
			sprite.setBarriers(barriers);
			sprite.setSprites(sprites);
		}

	}

	public static void main(String[] args)
	{
		GameLoop m = new GameLoop();
		m.setVisible(true);

		loop = new Thread()
		{
			public void run()
			{
				m.gameLoop();
			}
		};
		loop.start();

	}

	private void gameLoop() {

		while (true) { // main game loop

			//adapted from http://www.java-gaming.org/index.php?topic=24220.0
			last_refresh_time = System.currentTimeMillis();
			next_refresh_time = current_time + minimum_delta_time;

			while (current_time < next_refresh_time)
			{
				Thread.yield();

				try {Thread.sleep(1);}
				catch(Exception e) {} 

				current_time = System.currentTimeMillis();
			}

			//read input
			keyboard.poll();
			handleKeyboardInput();

			//UPDATE STATE
			updateTime();
			updateSprites();
			disposeSprites();

			//REFRESH
			this.repaint();

		}
	}

	private void updateTime() {

		current_time = System.currentTimeMillis();
		actual_delta_time = (isPaused ? 0 : current_time - last_refresh_time);
		last_refresh_time = current_time;
		elapsed_time += actual_delta_time;

		this.lblTime.setText(Long.toString(elapsed_time));
	}

	private void updateSprites() {
		try {
			for (Sprite sprite : sprites) {
				sprite.update(keyboard, actual_delta_time);
			}
		} catch (Exception e) {
		}    	
	}

	private void disposeSprites() {
		for (Sprite sprite : this.sprites) {
			if (sprite.getDispose() == true) {
				spritesToDispose.add(sprite);
			}
		}
		for (Sprite sprite : this.spritesToDispose) {
			sprites.remove(sprite);
		}
		if (spritesToDispose.size() > 0) {
			spritesToDispose.clear();
		}
	}

	protected void btnPauseRun_mouseClicked(MouseEvent arg0) {
		if (isPaused) {
			isPaused = false;
			this.btnPauseRun.setText("||");
		}
		else {
			isPaused = true;
			this.btnPauseRun.setText(">");
		}
	}

	private void handleKeyboardInput() {
		//if the interface needs to respond to certain keyboard events
		if (keyboard.keyDown(80) && ! isPaused) {
			btnPauseRun_mouseClicked(null);	
		}
		if (keyboard.keyDown(79) && isPaused ) {
			btnPauseRun_mouseClicked(null);
		}
	}

	class DrawPanel extends JPanel {

		public void paintComponent(Graphics g)
		{			 
			xOffset = - 425;
			yOffset = - 582;
			if (CENTER_ON_PLAYER && me != null) {
				xOffset = - ((int) me.getMinX() - (SCREEN_WIDTH / 2));
				yOffset = - ((int) me.getMinY() - (SCREEN_HEIGHT / 2));	        
			}
			if (me.getMinY() < 10 * 50 ) {
				xOffset = - 435;
				yOffset =  122;
				CENTER_ON_PLAYER = false;
			}
			if(me.getMinX() > 1700 && me.getMinY() < 900){
				xOffset = - 1780;
				yOffset =  -300;

			}
			if(me.getMinX() > 1700 && me.getMinY() > 900){
				xOffset = - 1780;
				yOffset =  -975;

			}
			if(me.getMaxY() > 1270){
				xOffset = - 435;
				yOffset = - 1275;
			}
			if (me.getMinX() < 400){
				xOffset = 899;
				yOffset = -575;
			}



//			if (!buttonPressed){
//				barriers.add(new Rectangle(1,11*50,2000,50));
//				barriers.add(new Rectangle(35*50,1,50,2000));
//				barriers.add(new Rectangle(1,25*50,2000,50));
//				barriers.add(new Rectangle(8*50,1,50,2000));
//			}
//			if (buttonPressed){
//				barriers.removeAll(temporaryBarriers);
//
//			}


			paintBackground(g,grass);
			paintBackground(g, background);



			g.setColor(BARRIER_COLOR);
			for (Rectangle barrier : barriers) {
				g.fillRect((int)barrier.getX() + xOffset,(int) barrier.getY() + yOffset, (int)barrier.getWidth(), (int)barrier.getHeight());       	
			}

			for (Sprite staticSprite : sprites) {
				g.drawImage(staticSprite.getImage(), (int)staticSprite.getMinX() + xOffset, (int)staticSprite.getMinY() + yOffset, (int)staticSprite.getWidth(), (int)staticSprite.getHeight(), null);
			}

		}



	}

	private void paintBackground(Graphics g, Background background) {
		//what tile covers the top-left corner?
		int xTopLeft = - xOffset;
		int yTopLeft = - yOffset;
		int row = background.getRow(yTopLeft);
		int col = background.getCol(xTopLeft);
		Tile tile = null;

		boolean rowDrawn = false;
		boolean colDrawn = false;
		while (colDrawn == false) {
			while (rowDrawn == false) {
				tile = background.getTile(col, row);
				g.drawImage(tile.getImage(), tile.getX() + xOffset, tile.getY() + yOffset, tile.getWidth(), tile.getHeight(), null);
				//does the RHE of this tile extend past the RHE of the visible area?
				int rheTile = tile.getX() + xOffset + tile.getWidth();
				if (rheTile > SCREEN_WIDTH || tile.isOutOfBounds()) {
					rowDrawn = true;
				}
				else {
					col++;
				}
			}
			//does the bottom edge of this tile extend past the bottom edge of the visible area?
			int bottomEdgeTile = tile.getY() + yOffset + tile.getHeight();
			if (bottomEdgeTile > SCREEN_HEIGHT || tile.isOutOfBounds()) {
				colDrawn = true;
			}
			else {
				col = background.getCol(xTopLeft);
				row++;
				rowDrawn = false;
			}
		}
	}				
}

