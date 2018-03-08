import java.applet.Applet;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.*;

/*import javax.swing.Box;
 import javax.swing.JLabel;
 import javax.swing.JOptionPane;
 import javax.swing.JPanel;
 import javax.swing.JTextField;
 import javax.swing.Timer;*/

@SuppressWarnings("serial")
public class AsteroidsGame extends Applet implements KeyListener,
		ActionListener {
	Dimension screenSize;
	Spaceship ship;
	Graphics offg;
	ArrayList<Asteroid> asteroidList;
	ArrayList<Bullet> bulletList;
	ArrayList<FloatingPoints> pointsList;
	ArrayList<PowerUps> powerupsList;
	int level;
	int numAsteroids;
	int score;
	int tempweapon;
	Image offScreen;
	Image background;
	boolean upKey, leftKey, rightKey, spaceKey;
	boolean win, victory, gameOver, gameStarted, paused;
	Timer timer = new Timer(20, this);
	Color color1 = new Color(0, 0, 0);

	public void init() { //Startup
		background = getImage(getDocumentBase(), "spaceBackground.jpeg");
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize((int) screenSize.getWidth(), (int) screenSize.getHeight());
		this.addKeyListener(this);
		ship = new Spaceship(screenSize);
		offScreen = createImage(this.getWidth(), this.getHeight());
		offg = offScreen.getGraphics();
		level = 1;
		score = 0;
		gameOver = false;
		gameStarted = false;
		numAsteroids = 3;
		setLevel();
		pointsList = new ArrayList<FloatingPoints>();
	}

	public void paint(Graphics g) { //Paint
		//No Matter What
		offg.setColor(Color.black);
		offg.drawImage(background, 0, 0, screenSize.width, screenSize.height,
				this);
		//Before the game has started
		if (!gameStarted) {
			offg.setColor(Color.BLACK);
			offg.fillRect((int) screenSize.getWidth() / 2 - 175,
					(int) screenSize.getHeight() / 2 - 175, 375, 100);
			offg.setColor(Color.white);
			offg.drawRect((int) screenSize.getWidth() / 2 - 175,
					(int) screenSize.getHeight() / 2 - 175, 375, 100);
			offg.setFont(new Font("Impact", Font.BOLD, 75));
			offg.drawString("Asteroids", (int) screenSize.getWidth() / 2 - 150,
					(int) screenSize.getHeight() / 2 - 100);
			offg.setFont(new Font("Segoe Script   ", Font.BOLD, 25));
			offg.drawString("Begin? (Enter)",
					(int) screenSize.getWidth() / 2 - 90,
					(int) screenSize.getHeight() / 2 + 100);
		//During the game
		} else {
			offg.setColor(Color.black);
			offg.fillRect(40, 20, 150, 70);
			offg.fillRect((int) (screenSize.getWidth() - 310), 20, 250, 40);
			offg.setColor(Color.white);
			offg.drawRect(40, 20, 150, 70);
			offg.drawRect((int) (screenSize.getWidth() - 310), 20, 250, 40);
			offg.setFont(new Font("Segoe Script   ", Font.BOLD, 25));
			offg.drawString("Level: " + level + "/20", 50, 50);
			offg.drawString("Lives: " + ship.lives, 50, 75);
			offg.drawString("Score: " + score,
					(int) (screenSize.getWidth() - 300), 50);
			offg.drawString("Health: " + ship.health,
					(int) (screenSize.getWidth() - 300), 100);
			offg.drawString("Homing Ammo: " + ship.HomingAmmo,
					(int) (screenSize.getWidth() - 600),
					(int) screenSize.getHeight() - 100);
			offg.drawString("Shotgun Ammo: " + ship.ShotAmmo, (int) (400),
					(int) screenSize.getHeight() - 100);
			offg.drawString("Machine Gun Ammo: " + ship.LMGAmmo, (int) (50),
					(int) screenSize.getHeight() - 100);
			offg.drawString("Heat:", (int) (screenSize.getWidth() - 350),
					(int) screenSize.getHeight() - 100);
			offg.setColor(Color.BLACK);
			offg.fillRect((int) screenSize.getWidth() - 275,
					(int) screenSize.getHeight() - 125, 250, 30);
			offg.setColor(Color.WHITE);
			offg.drawRect((int) screenSize.getWidth() - 275,
					(int) screenSize.getHeight() - 125, 250, 30);
			if (ship.laserHeat < 1000) {
				color1 = new Color(ship.laserHeat / 4, 100, 10);
				offg.setColor(color1);
				if (ship.cooldown) {
					offg.setColor(Color.RED);
				}
				offg.fillRect((int) screenSize.getWidth() - 270,
						(int) screenSize.getHeight() - 120,
						(ship.laserHeat / 4) - 10, 20);
			} else {
				offg.setColor(Color.RED);
				offg.fillRect((int) screenSize.getWidth() - 270,
						(int) screenSize.getHeight() - 120, 240, 20);
			}

			for (int i = 0; i < asteroidList.size(); i++) {
				asteroidList.get(i).paint(offg);
			}
			for (int i = 0; i < bulletList.size(); i++) {
				bulletList.get(i).paint(offg);
			}
			if (level <= 20) { //After level 20 the points stop painting
				for (int i = 0; i < pointsList.size(); i++) {
					pointsList.get(i).paint(offg);
				}
			}
			for (int i = 0; i < powerupsList.size(); i++) {
				powerupsList.get(i).paint(offg);
			}
			if (ship.active)
				ship.paint(offg);
			//Level Complete
			if (win) {
				offg.setColor(Color.white);
				offg.setFont(new Font("Segoe Script   ", Font.BOLD, 50));
				offg.drawString("Level " + level + " " + "Complete!",
						(int) screenSize.getWidth() / 2 - 200,
						(int) screenSize.getHeight() / 2 - 100);
				offg.setFont(new Font("Segoe Script   ", Font.BOLD, 25));
				offg.drawString("Press Enter to Continue",
						(int) screenSize.getWidth() / 2 - 150,
						(int) screenSize.getHeight() / 2 + 100);
			}
			//Victory (Level 20 Complete)
			if (victory) {
				offg.setColor(Color.white);
				offg.setFont(new Font("Segoe Script   ", Font.BOLD, 50));
				offg.drawString("Congratulations You Win!",
						(int) screenSize.getWidth() / 2 - 300,
						(int) screenSize.getHeight() / 2 - 100);
				offg.setFont(new Font("Segoe Script   ", Font.BOLD, 25));
				offg.drawString("Keep Going (Enter)",
						(int) screenSize.getWidth() / 2 - 110,
						(int) screenSize.getHeight() / 2 + 100);
			}
			//Game Over
			if (gameOver) {
				offg.setColor(Color.white);
				offg.setFont(new Font("Segoe Script   ", Font.BOLD, 50));
				offg.drawString("Sorry you lose.",
						(int) screenSize.getWidth() / 2 - 175,
						(int) screenSize.getHeight() / 2 - 100);
				offg.setFont(new Font("Segoe Script   ", Font.BOLD, 25));
				offg.drawString("You made it to level " + level
						+ ". Restart? (Enter)",
						(int) screenSize.getWidth() / 2 - 250,
						(int) screenSize.getHeight() / 2 + 100);
			}
			//Pause Screen
			if (paused) {
				offg.setColor(Color.black);
				offg.fillRect(160, 90, (int)screenSize.getWidth() - 320, (int)screenSize.getHeight() - 180);
				offg.setColor(Color.white);
				offg.drawRect(160, 90, (int)screenSize.getWidth() - 320, (int)screenSize.getHeight() - 180);
				offg.setFont(new Font("Impact", Font.BOLD, 75));
				offg.drawString("Paused", (int) screenSize.getWidth() / 2 - 120, (int) screenSize.getHeight() / 2 - 375);
				offg.setFont(new Font("Impact", Font.BOLD, 60));
				offg.drawString("Press _ button for:", 200, (int) screenSize.getHeight() / 2 - 300);
				offg.drawString("1: Health", 200, (int) screenSize.getHeight() / 2 - 230);
				offg.drawString("2: Lives", 200, (int) screenSize.getHeight() / 2 - 160);
				offg.drawString("3: Level", 200, (int) screenSize.getHeight() / 2 - 90);
				offg.drawString("4: Ship Speed", 200, (int) screenSize.getHeight() / 2 - 20);
				offg.drawString("5: Random Homing Toggle", 200, (int) screenSize.getHeight() / 2 + 50);
				offg.drawString("6: Homing Bullets per Shot", 200, (int) screenSize.getHeight() / 2 + 120);
				offg.drawString("7: Shotgun Bullets per Shot", 200, (int) screenSize.getHeight() / 2 + 190);
			}
		}
		g.drawImage(offScreen, 0, 0, this);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) { //Main Loop
		KeyCheck();
		if (!gameStarted) {

		} else if (paused) {
		
		} else {
			ship.updatePosition();
			for (int i = 0; i < asteroidList.size(); i++) {
				asteroidList.get(i).updatePosition();
				if (asteroidList.get(i).active == false) {

				}
			}
			for (int n = 0; n < bulletList.size(); n++) {
				bulletList.get(n).updatePosition();
				if (bulletList.get(n).active == false) {
					bulletList.remove(n);
				}
			}
			
			if (level <= 20) { //After level 20 points don't move
				for (int z = 0; z < pointsList.size(); z++) {
					pointsList.get(z).updatePosition();
					if (pointsList.get(z).active == false)
						pointsList.remove(z);
				}
			}
			for (int m = 0; m < powerupsList.size(); m++) {
				powerupsList.get(m).updatePosition();
				if (powerupsList.get(m).active == false)
					powerupsList.remove(m);
			}
			checkAsteroids();
			checkCollisions();
			if (ship.lives == 0) {
				gameOver = true;
				ship.active = false;
			}
		}
		repaint();
	}

	@Override
	public void start() {
		timer.start();
	}

	public void stop() {
		timer.stop();
	}
	
	public void update(Graphics g) {
		paint(g);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP) {
			upKey = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN) {

		}
		if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT) {
			leftKey = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT) {
			rightKey = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			spaceKey = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_Q) {
			tempweapon = ship.weapon;
			ship.weapon = ship.lastweapon;
			ship.lastweapon = tempweapon;
		}
		if (e.getKeyCode() == KeyEvent.VK_1) {
			ship.lastweapon = ship.weapon;
			ship.weapon = 1;
		}
		if (e.getKeyCode() == KeyEvent.VK_2) {
			ship.lastweapon = ship.weapon;
			ship.weapon = 2;
		}
		if (e.getKeyCode() == KeyEvent.VK_3) {
			ship.lastweapon = ship.weapon;
			ship.weapon = 3;
		}
		if (e.getKeyCode() == KeyEvent.VK_4) {
			ship.lastweapon = ship.weapon;
			ship.weapon = 4;
		}
		if (e.getKeyCode() == KeyEvent.VK_5) {
			ship.lastweapon = ship.weapon;
			ship.weapon = 5;
		}
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE || e.getKeyCode() == KeyEvent.VK_P) {
			if (paused)
				paused = false;
			else
				paused = true;
		}
		if (paused) {
			if (e.getKeyCode() == KeyEvent.VK_1) {
				ship.health = Integer.parseInt(JOptionPane.showInputDialog("Health?"));
			}
			if (e.getKeyCode() == KeyEvent.VK_2) {
				ship.lives = Integer.parseInt(JOptionPane.showInputDialog("Lives?"));
			}
			if (e.getKeyCode() == KeyEvent.VK_3) {
				level = Integer.parseInt(JOptionPane.showInputDialog("Level?"));
			}
			if (e.getKeyCode() == KeyEvent.VK_4) {
				ship.speed = Integer.parseInt(JOptionPane.showInputDialog("Speed?"));
			}
			if (e.getKeyCode() == KeyEvent.VK_5) {
				Object[] params = { "Random Homing" };
				int n = JOptionPane.showConfirmDialog(null, params, "Random Homing?", JOptionPane.YES_NO_OPTION);
				if (n == JOptionPane.YES_OPTION) {
					ship.randomHoming = true;
				} else
					ship.randomHoming = false;
			}
			if (e.getKeyCode() == KeyEvent.VK_6) {
				ship.homingBullets = Integer.parseInt(JOptionPane.showInputDialog("# of Homing Bullets?"));
			}
			if (e.getKeyCode() == KeyEvent.VK_7) {
				ship.shotgunBullets = Integer.parseInt(JOptionPane.showInputDialog("# of Shotgun Bullets?"));
			}

			/* if (e.getKeyCode() == KeyEvent.VK_0) {
				JTextField xField = new JTextField(5);
				JTextField yField = new JTextField(5);

				JPanel myPanel = new JPanel();
				myPanel.add(new JLabel("x:"));
				myPanel.add(xField);
				myPanel.add(Box.createHorizontalStrut(15)); // a spacer
				myPanel.add(new JLabel("y:"));
				myPanel.add(yField);

				int result = JOptionPane.showConfirmDialog(null, myPanel, "Please Enter X and Y Values", JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.OK_OPTION) {
					System.out.println("x value: " + xField.getText());
					System.out.println("y value: " + yField.getText());
				}
			} */
		}
		if (e.getKeyCode() == KeyEvent.VK_ENTER && !gameStarted) {
			gameStarted = true;
		} else if (e.getKeyCode() == KeyEvent.VK_ENTER && win
				|| e.getKeyCode() == KeyEvent.VK_ENTER && victory) {
			level++;
			setLevel();
		} else if (e.getKeyCode() == KeyEvent.VK_ENTER && gameOver) {
			init();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_W) {
			upKey = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_S) {

		}
		if (e.getKeyCode() == KeyEvent.VK_A) {
			leftKey = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_D) {
			rightKey = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			spaceKey = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {

	}

	public void KeyCheck() {
		if (upKey) {
			ship.accelerate();
		} else {
			ship.deaccelerate();
		}
		if (leftKey) {
			ship.turnLeft();
		}
		if (rightKey) {
			ship.turnRight();
		}
		if (spaceKey) {
			if (ship.weapon == 1) {
				ship.fireGun(bulletList);
			} else if (ship.weapon == 2) {
				ship.fireLMG(bulletList);
			} else if (ship.weapon == 3) {
				ship.fireShotgun(bulletList);
			} else if (ship.weapon == 4) {
				ship.fireHomingBullet(bulletList, asteroidList);
			} else if (ship.weapon == 5) {
				ship.fireRailgun(bulletList);
			}
		}
	}

	public void checkCollisions() {
		for (int i = 0; i < asteroidList.size(); i++) {
			if (ship.collidingWith(asteroidList.get(i))) {
				if (ship.active && !ship.isInvincible()) {
					if (ship.health > 1) {
						score -= (int) 100 * level;
						if (level <= 20) {
							pointsList.add(new FloatingPoints(screenSize,
									asteroidList.get(i).xPos,
									asteroidList.get(i).yPos, (int) -100 * level));
						}
					} else {
						score -= (int) 1000 * level;
						if (level <= 20) {
							pointsList.add(new FloatingPoints(screenSize,
									asteroidList.get(i).xPos,
									asteroidList.get(i).yPos, (int) -1000 * level));
						}
					}
				}
				ship.hit();
			}
			for (int n = 0; n < bulletList.size(); n++) {
				if (bulletList.get(n).collidingWith(asteroidList.get(i))) {
					bulletList.get(n).active = bulletList.get(n) instanceof Railgun;
					asteroidList.get(i).active = false;
				}
			}
		}
		for (int i = 0; i < powerupsList.size(); i++) {
			if (ship.collidingWith(powerupsList.get(i))) {
				powerupsList.get(i).SpecialEffect(ship);
				powerupsList.get(i).active = false;
			}
		}
	}

	public void checkAsteroids() {
		for (int i = 0; i < asteroidList.size(); i++) {
			if (asteroidList.get(i).active == false) {
				score += (int) (10 * level);
				if (level <= 20) {
					pointsList.add(new FloatingPoints(screenSize, asteroidList
							.get(i).xPos, asteroidList.get(i).yPos, (int) 10
							* level));
				}
				if (100 * Math.random() < 100) {
					powerupsList.add(new PowerUps(screenSize, asteroidList
							.get(i).xPos, asteroidList.get(i).yPos, ship));
				}
				if (asteroidList.get(i).scale / 2 > 1) {
					asteroidList.add(new Asteroid(screenSize, asteroidList
							.get(i).xPos, asteroidList.get(i).yPos,
							asteroidList.get(i).scale / 2));
					asteroidList.add(new Asteroid(screenSize, asteroidList
							.get(i).xPos, asteroidList.get(i).yPos,
							asteroidList.get(i).scale / 2));
				}
				asteroidList.remove(i);
				if (asteroidList.isEmpty() && level == 20) {
					victory = true;
					score += (int) 500 * level;
				} else if (asteroidList.isEmpty() && level != 20) {
					win = true;
					score += (int) 500 * level;
					FloatingPoints fp = new FloatingPoints(screenSize,
							screenSize.getWidth() / 2,
							screenSize.getHeight() / 2, (int) 500 * level);
					fp.lifeSpan = 50 * 5;
					pointsList.add(fp);
				}
			}
		}
	}

	public void setLevel() {
		ship.counter = 0;
		win = false;
		if (victory)
			victory = false;
		asteroidList = new ArrayList<Asteroid>();
		bulletList = new ArrayList<Bullet>();
		powerupsList = new ArrayList<PowerUps>();
		numAsteroids = (int) (numAsteroids + (level - 2));
		for (int i = 0; i < numAsteroids; i++) {
			asteroidList.add(new Asteroid(screenSize));
		}
	}
}