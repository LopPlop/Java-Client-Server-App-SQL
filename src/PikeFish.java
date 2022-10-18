import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PikeFish implements TFish {

    private double x, y;
    public double speed = 1;
    public double speed_y = 0.25;
    private BufferedImage img;
    private BufferedImage r_img;
    private boolean isHungry = false;

    public PikeFish() {
        init();
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x){ x = x; }
    public void setY(double y){ y = y; }

    @Override
    public void Draw(Graphics g) {
        if (speed > 0)
            g.drawImage(img, (int) (x), (int) (y), 160, 50, null);
        else
            g.drawImage(r_img, (int) (x), (int) (y), 160, 50, null);
    }

    @Override
    public void init() {
        x = (int) (100 + Math.random() * Habitat.WIDTHS / 2);
        y = (int) (100 + Math.random() * (Habitat.HEIGHT - 300));


        double a = Math.random() * 100;
        if (a > 50)
            speed *= -1;

        try {

            img = ImageIO.read(new File("C:\\Users\\Dominator\\IdeaProjects\\Course_Work_Acuario\\Images\\pike.png"));
            r_img = ImageIO.read(new File("C:\\Users\\Dominator\\IdeaProjects\\Course_Work_Acuario\\Images\\r_pike.png"));
        } catch (IOException exc) {

        }
        Draw(Habitat.graphics);
    }

    @Override
    public boolean Look() {
        for (int i = 0 ; i < Habitat.bottomVegetation.size(); i++){
            if(Math.abs((int)(Habitat.bottomVegetation.get(i).getX() - x) ) < 50 && ((int)(Habitat.bottomVegetation.get(i).getY() - y) < 0)){
                speed*=-1;
                x += speed*6;
                y -= 4;
                speed_y*=-1;
            }
        }
        for (int i = 0; i < Habitat.carpArray.size(); i++) {
            if (Math.abs((int) (Habitat.carpArray.get(i).getX() - x)) < 120 && Math.abs((int) (Habitat.carpArray.get(i).getY() - y)) < 40) {
                isHungry = true;
                Attack(Habitat.carpArray.get(i), i);
                return true;
            }
        }
        return false;
    }

    @Override
    public void Run() {
        if (!isHungry) {
                x += speed;
            double a = Math.random() * 100;
            if (a < 1 && (x > 960 || x < 70)) {
                speed *= -1;
            }
            if((x > 960 || x < 70))
                speed*=-1;
            double b = Math.random() * 100;
            if (b < 1 && (y > 660 || y < 0)) {
                speed_y *= -1;
            }
            if(y > 660 || y < 160)
                speed_y*=-1;
            if(y < Habitat.HEIGHT-100 && y > 0 )
                y+=speed_y;
            Look();
        }
    }

    public void Attack(CarpFish fish, int i) {
        setX(fish.getX());
        setY(fish.getY());
        Habitat.carpArray.remove(i);
        isHungry = false;
    }
}
