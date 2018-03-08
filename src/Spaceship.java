import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Random;

public class Spaceship extends VectorSprite {
	int invincibleTime = 50 * 5;
	int respawnDelay = 50 * 3;
	int fireDelay = 0;
	int fireCounter = 0;
	int lives;
	int weapon, lastweapon;
	int shotgunBullets = 7, homingBullets = 1;
	int LMGAmmo, ShotAmmo, HomingAmmo;
	int laserHeat, MaxHeat;
	double health;
	boolean randomHoming;
	boolean cooldown;
	Random random;

	public Spaceship(Dimension screenSize) {
		super(screenSize);
		speed = 5;
		MaxHeat = 1000;
		cooldown = false;
		randomHoming = true;
		xPos = screenSize.getWidth() / 2;
		yPos = screenSize.getHeight() / 2;
		shape = new Polygon();
		shape.addPoint(0, -20);
		shape.addPoint(5, -10);
		shape.addPoint(10, -6);
		shape.addPoint(10, -15);
		shape.addPoint(12, -15);
		shape.addPoint(12, -5);
		shape.addPoint(17, 0);
		shape.addPoint(20, 5);
		shape.addPoint(15, 3);
		shape.addPoint(5, 3);
		shape.addPoint(2, 8);
		shape.addPoint(0, 10);
		shape.addPoint(-2, 8);
		shape.addPoint(-5, 3);
		shape.addPoint(-15, 3);
		shape.addPoint(-20, 5);
		shape.addPoint(-17, 0);
		shape.addPoint(-12, -5);
		shape.addPoint(-12, -15);
		shape.addPoint(-10, -15);
		shape.addPoint(-10, -6);
		shape.addPoint(-5, -10);
		drawShape = new Polygon();
		drawShape.addPoint(0, -20);
		drawShape.addPoint(5, -10);
		drawShape.addPoint(10, -6);
		drawShape.addPoint(10, -15);
		drawShape.addPoint(12, -15);
		drawShape.addPoint(12, -5);
		drawShape.addPoint(17, 0);
		drawShape.addPoint(20, 5);
		drawShape.addPoint(15, 3);
		drawShape.addPoint(5, 3);
		drawShape.addPoint(2, 8);
		drawShape.addPoint(0, 10);
		drawShape.addPoint(-2, 8);
		drawShape.addPoint(-5, 3);
		drawShape.addPoint(-15, 3);
		drawShape.addPoint(-20, 5);
		drawShape.addPoint(-17, 0);
		drawShape.addPoint(-12, -5);
		drawShape.addPoint(-12, -15);
		drawShape.addPoint(-10, -15);
		drawShape.addPoint(-10, -6);
		drawShape.addPoint(-5, -10);
		color = new Color(255, 215, 0);
		lives = 3;
		health = 2;
		weapon = 1;
	}

	public void accelerate() {
		xSpeed = Math.cos(angle - Math.PI / 2) * speed;
		ySpeed = Math.sin(angle - Math.PI / 2) * speed;
	}

	public void deaccelerate() {
		xSpeed *= .97;
		ySpeed *= .97;
	}

	public void turnLeft() {
		angle -= turnAmount;
	}

	public void turnRight() {
		angle += turnAmount;
	}

	public void hit() {
		if (this.active && counter > invincibleTime) {
			health -= 1;
			counter = 0;
			if (health <= 0) {
				this.active = false;
				lives -= 1;
			}
		}
	}

	public void respawn() {
		xPos = screenSize.getWidth() / 2;
		yPos = screenSize.getHeight() / 2;
		active = true;
		counter = 0;
		health = 2;
	}

	public void updatePosition() {
		super.updatePosition();
		if (!active && counter > respawnDelay) {
			respawn();
		}
		fireCounter++;
		atMaxHeat();
		if (!cooldown) {
			if (laserHeat - 4 >= 0)
				laserHeat -= 4;
		} else
			laserHeat -= 3;

		if (laserHeat <= 0)
			cooldown = false;
	}

	public void paint(Graphics g) {
		if (counter > invincibleTime || counter % 20 < 10) {
			g.setColor(color);
		} else {
			g.setColor(Color.magenta);
		}
		g.fillPolygon(drawShape);
	}

	public void fireGun(ArrayList<Bullet> bulletList) {
		fireDelay = 20;
		if (fireCounter > fireDelay && active) {
			bulletList.add(new Bullet(screenSize, this.xPos, this.yPos,
					this.angle));
			fireCounter = 0;
		}
	}

	public void fireLMG(ArrayList<Bullet> bulletList) {
		if (LMGAmmo > 0) {
			fireDelay = 5;
			if (fireCounter > fireDelay && active) {
				bulletList.add(new Bullet(screenSize, this.xPos, this.yPos,
						this.angle));
				fireCounter = 0;
				LMGAmmo -= 1; // Just comment out
			}
		}
	}

	public void fireShotgun(ArrayList<Bullet> bulletList) {
		if (ShotAmmo > shotgunBullets) {
			fireDelay = 50; // 50
			if (fireCounter > fireDelay && active) {
				double degdiff = (Math.PI / 180) * 3;
				double startingAngle = angle - ((shotgunBullets - 1) * degdiff)
						/ 2;
				for (int i = 0; i < shotgunBullets; i++) {
					bulletList.add(new Bullet(screenSize, this.xPos, this.yPos,
							startingAngle + degdiff * i));
				}
				fireCounter = 0;
				ShotAmmo -= shotgunBullets; // Just comment out
			}
		}
	}

	public void fireRailgun(ArrayList<Bullet> bulletList) {
		fireDelay = 50; //50
		if (!cooldown) {
			if (fireCounter > fireDelay && active) {
				bulletList.add(new Railgun(screenSize, this.xPos, this.yPos,
						this.angle));
				fireCounter = 0;
				laserHeat += 500; // Just comment out
			}
		}
	}

	public void fireHomingBullet(ArrayList<Bullet> bulletList,
			ArrayList<Asteroid> asteroidList) {
		if (HomingAmmo > homingBullets) {
			fireDelay = 20; // 20
			if (fireCounter > fireDelay && active) {
				double degdiff = (Math.PI / 180) * 10;
				double startingAngle = angle - ((homingBullets - 1) * degdiff)
						/ .25;
				for (int n = 0; n < homingBullets; n++) {
					if (!asteroidList.isEmpty()) {
						Asteroid closestAsteroid;
						if (!randomHoming) {
							closestAsteroid = asteroidList.get(0);
							double x = asteroidList.get(0).xPos - xPos;
							double y = asteroidList.get(0).yPos - yPos;
							double shortestDist = Math.sqrt(x * x + y * y);
							for (int i = 1; i < asteroidList.size(); i++) {
								x = asteroidList.get(i).xPos - xPos;
								y = asteroidList.get(i).yPos - yPos;
								double dist = Math.sqrt(x * x + y * y);
								if (dist < shortestDist) {
									shortestDist = dist;
									closestAsteroid = asteroidList.get(i);
								}

							}
						} else {
							closestAsteroid = asteroidList
									.get((int) (asteroidList.size() * Math
											.random()));
						}
						bulletList.add(new HomingBullet(screenSize, this.xPos,
								this.yPos, startingAngle + degdiff * n,
								closestAsteroid));
						fireCounter = 0;
					}
				}
				HomingAmmo -= homingBullets; // Just Comment Out
			}
		}
	}

	public boolean isInvincible() {
		return counter < invincibleTime;

	}

	public void atMaxHeat() {
		if (laserHeat >= MaxHeat)
			cooldown = true;
	}
}
