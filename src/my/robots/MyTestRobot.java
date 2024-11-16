package my.robots;

import robocode.AdvancedRobot;
import robocode.HitByBulletEvent;
import robocode.RobocodeFileOutputStream;
import robocode.ScannedRobotEvent;

import java.io.IOException;

public class MyTestRobot extends AdvancedRobot {

    private int attackCount = 0;
    private double energy = 100;
    private double radarTurnAmount; // P6863

    public void run() {
        radarTurnAmount = Double.POSITIVE_INFINITY; // Paaa4
        setTurnRadarRight(radarTurnAmount); // Paaa4
        while (true) {
            try {
                RobocodeFileOutputStream robocodeFileOutputStream =
                        new RobocodeFileOutputStream(getDataDirectory().toPath() + "/test.log", true);
                // X,Y,Energy,Attacks
                robocodeFileOutputStream.write(
                        String.format("%d\t%.2f\t%.2f\t%.2f\t%d\n",
                                getTime(),
                                getX(),
                                getY(),
                                getEnergy(),
                                attackCount
                        ).getBytes()
                );
                ahead(100); // Move ahead 100
                robocodeFileOutputStream.write(
                        String.format("%d\t%.2f\t%.2f\t%.2f\t%d\n",
                                getTime(),
                                getX(),
                                getY(),
                                getEnergy(),
                                attackCount
                        ).getBytes()
                );
                turnGunRight(360); // Spin gun around
                back(100); // Move back 100
                robocodeFileOutputStream.write(
                        String.format("%d\t%.2f\t%.2f\t%.2f\t%d\n",
                                getTime(),
                                getX(),
                                getY(),
                                getEnergy(),
                                attackCount
                        ).getBytes()
                );
                turnGunRight(360); // Spin gun around
                robocodeFileOutputStream.flush();
                robocodeFileOutputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Fire when we see a robot
     */
    public void onScannedRobot(ScannedRobotEvent e) {
        radarTurnAmount = getHeading() - getRadarHeading() + e.getBearing(); // Paba5
        setTurnRadarRight(radarTurnAmount); // Paba5
        fire(1);
        attackCount++;
    }

    /**
     * We were hit!  Turn perpendicular to the bullet,
     * so our seesaw might avoid a future shot.
     */
    public void onHitByBullet(HitByBulletEvent e) {
        energy -= e.getPower();
        turnLeft(90 - e.getBearing());
    }
}
