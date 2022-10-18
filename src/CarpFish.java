import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CarpFish extends Canvas implements TFish{

    private double x,y;
    public double speed = -1;
    private double speed_y = -0.25;
    private BufferedImage img;
    private BufferedImage r_img;
    Graphics buf;
    Graphics buf1;


    public CarpFish(){
        init();
    }

    public double get_X(){
        return x;
    }
    public double get_Y(){
        return y;
    }



    public void setX(int x){ x = x; }
    public void setY(int y){ y = y; }

    @Override
    public void Draw(Graphics g) {
        if(speed < 0) {
            g.drawImage(img, (int) (x), (int) (y), 160, 50, null);
//            Draw(buf);
        }
        else{
            g.drawImage(r_img, (int) (x), (int) (y), 160, 50, null);
//            Draw(buf1);
        }
    }

    @Override
    public void init() {
        try {
            img = ImageIO.read(new File("C:\\Users\\Dominator\\IdeaProjects\\Course_Work_Acuario\\Images\\carp.png"));
            r_img = ImageIO.read(new File("C:\\Users\\Dominator\\IdeaProjects\\Course_Work_Acuario\\Images\\r_carp.png"));
        }catch (IOException exc){

        }
        buf = img.getGraphics();
        buf1 = r_img.getGraphics();
        double a = Math.random()*100;
        if(a > 50)
            speed*=-1;
        y = (int)(100 + Math.random()*(Habitat.HEIGHT-400));
        x = (int)(Habitat.WIDTHS/2 + Math.random()*(Habitat.WIDTHS/2-70));
        Draw(Habitat.graphics);
    }

    @Override
    public boolean Look() {
        for (int i = 0 ; i < Habitat.bottomVegetation.size(); i++){
            if(Math.abs((int)(Habitat.bottomVegetation.get(i).getX()- x) ) < 10){
                speed*=-1;
                return false;
            }
        }
        return false;
    }

    @Override
    public void Run() {
        x += speed;
        double a = Math.random() * 100;
        if (a < 1 && (x > 960 || x < 70)) {
            speed *= -1;
        }
        if((x > 960 || x < 70))
            speed*=-1;
        double b = Math.random() * 100;
        if (b < 1 && (y > 660 || y < 100)) {
            speed_y *= -1;
        }
        if(y > 660 || y < 100)
            speed_y*=-1;
        if(y < Habitat.HEIGHT-100 && y > 0 )
            y+=speed_y;
        Look();
    }
}

