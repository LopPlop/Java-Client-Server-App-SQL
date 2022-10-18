import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SeaPlant {
    private int x, y;
    private BufferedImage img;
    private BufferedImage r_img;
    SeaPlant(int x, int y) throws IOException {
        this.x = x;
        this.y = y;
        img = ImageIO.read(new File("C:\\Users\\Dominator\\IdeaProjects\\Course_Work_Acuario\\Images\\sea_plant.png"));
        r_img = ImageIO.read(new File("C:\\Users\\Dominator\\IdeaProjects\\Course_Work_Acuario\\Images\\r_sea_plant.png"));
    }

    public int getX(){return x;}
    public int getY(){return y;}

    public void Draw(Graphics2D g) {
        if(x > 200)
            g.drawImage(img, (int)(x), (int)(y), 200, 400, null);
        else
            g.drawImage(r_img, (int)(x), (int)(y), 200, 400, null);
    }
}
