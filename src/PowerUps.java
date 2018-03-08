import java.awt.Color;
import java.awt.Dimension;
import java.awt.Polygon;
import java.util.Random;

public class PowerUps extends VectorSprite {
	public enum Type {
		INVINCIBILITY, LIFE, HEAL, LMGAmmo, ShotAmmo, HomingAmmo;
	}


	//For Homing AI
	VectorSprite target;
	double targetAngle;
	
	Type type;
	Random random;
	int lifeSpan = 50 * 3; //1 Second * 3

	public PowerUps(Dimension screenSize, double x, double y, VectorSprite target1) {
		super(screenSize);
		this.xPos = x;
		this.yPos = y;
		random = new Random();
		int typeNum = random.nextInt(100) + 1;
		if (typeNum <= 5)
			type = Type.LIFE;
		else if (typeNum <= 15)
			type = Type.INVINCIBILITY;
		else if (typeNum <= 25)
			type = Type.HEAL;
		else if (typeNum <= 45)
			type = Type.HomingAmmo;
		else if (typeNum <= 65)
			type = Type.ShotAmmo;
		else if (typeNum <= 100)
			type = Type.LMGAmmo;
		xSpeed = Math.cos((2 * Math.PI) * random.nextDouble()
				* random.nextDouble());
		xSpeed = Math.sin((2 * Math.PI) * random.nextDouble()
				* random.nextDouble());
		shape = new Polygon();
		drawShape = new Polygon();
		switch (type) {
		case LIFE:
			color = Color.pink;
			shape.addPoint(0, -5);
			shape.addPoint(-3, -8);
			shape.addPoint(-6, -5);
			shape.addPoint(0, 5);
			shape.addPoint(6, -5);
			shape.addPoint(3, -8);
			drawShape.addPoint(0, -5);
			drawShape.addPoint(-3, -8);
			drawShape.addPoint(-6, -5);
			drawShape.addPoint(0, 5);
			drawShape.addPoint(6, -5);
			drawShape.addPoint(3, -8);
			break;
		case HEAL:
			color = Color.red;
			shape.addPoint(2, 7);
			shape.addPoint(2, 2);
			shape.addPoint(7, 2);
			shape.addPoint(7, -2);
			shape.addPoint(2, -2);
			shape.addPoint(2, -7);
			shape.addPoint(-2, -7);
			shape.addPoint(-2, -2);
			shape.addPoint(-7, -2);
			shape.addPoint(-7, 2);
			shape.addPoint(-2, 2);
			shape.addPoint(-2, 7);
			drawShape.addPoint(2, 7);
			drawShape.addPoint(2, 2);
			drawShape.addPoint(7, 2);
			drawShape.addPoint(7, -2);
			drawShape.addPoint(2, -2);
			drawShape.addPoint(2, -7);
			drawShape.addPoint(-2, -7);
			drawShape.addPoint(-2, -2);
			drawShape.addPoint(-7, -2);
			drawShape.addPoint(-7, 2);
			drawShape.addPoint(-2, 2);
			drawShape.addPoint(-2, 7);
			break;
		case INVINCIBILITY:
			color = Color.magenta;
			shape.addPoint(0, -2);
			shape.addPoint(4, 8);
			shape.addPoint(12, 8);
			shape.addPoint(4, 12);
			shape.addPoint(8, 20);
			shape.addPoint(0, 16);
			shape.addPoint(-8, 20);
			shape.addPoint(-4, 12);
			shape.addPoint(-12, 8);
			shape.addPoint(-4, 8);
			drawShape.addPoint(0, -2);
			drawShape.addPoint(12, 8);
			drawShape.addPoint(12, 8);
			drawShape.addPoint(4, 12);
			drawShape.addPoint(8, 20);
			drawShape.addPoint(0, 16);
			drawShape.addPoint(-8, 20);
			drawShape.addPoint(-4, 12);
			drawShape.addPoint(-12, 8);
			drawShape.addPoint(-4, 8);
			break;
		case LMGAmmo:
			color = Color.orange;
			shape.addPoint(0, 0);
			shape.addPoint(10, 0);
			shape.addPoint(10, 10);
			shape.addPoint(0, 10);
			drawShape.addPoint(0, 0);
			drawShape.addPoint(10, 0);
			drawShape.addPoint(10, 10);
			drawShape.addPoint(0, 10);
			break;
		case ShotAmmo:
			color = Color.cyan;
			shape.addPoint(0, 0);
			shape.addPoint(10, 0);
			shape.addPoint(10, 10);
			shape.addPoint(0, 10);
			drawShape.addPoint(0, 0);
			drawShape.addPoint(10, 0);
			drawShape.addPoint(10, 10);
			drawShape.addPoint(0, 10);
			break;
		case HomingAmmo:
			color = Color.blue;
			shape.addPoint(0, 0);
			shape.addPoint(10, 0);
			shape.addPoint(10, 10);
			shape.addPoint(0, 10);
			drawShape.addPoint(0, 0);
			drawShape.addPoint(10, 0);
			drawShape.addPoint(10, 10);
			drawShape.addPoint(0, 10);
			break;
		}
		target = target1;
	}

	public void SpecialEffect(Spaceship ship) {
		counter = lifeSpan;
		switch (type) {
		case LIFE:
			ship.lives++;
			break;
		case HEAL:
			ship.health++;
			break;
		case INVINCIBILITY:
			ship.counter = 0;
			break;
		case LMGAmmo:
			ship.LMGAmmo += 20;
			break;
		case ShotAmmo:
			ship.ShotAmmo += 35;
			break;
		case HomingAmmo:
			ship.HomingAmmo += 10;
			break;
		}
	}
	
	public void updatePosition() {
//		//This code makes the powerups home in on the player (globally)
//		if (angle > Math.PI) {
//			angle -= Math.PI * 2;
//		} else if (angle < -Math.PI * 2) {
//			angle += Math.PI * 2;
//		}
//		targetAngle = Math.atan2(target.yPos - yPos, target.xPos - xPos);
//		targetAngle -= angle;
//		if (targetAngle > Math.PI) {
//			targetAngle -= Math.PI * 2;
//		} else if (targetAngle < -Math.PI * 2) {
//			targetAngle += Math.PI * 2;
//		}
//		xSpeed = Math.cos(angle) * speed;
//		ySpeed = Math.sin(angle) * speed;
//		angle += targetAngle * turnAmount;
//		if (!target.active)
//		this.active = false;
		super.updatePosition();
	}
}
