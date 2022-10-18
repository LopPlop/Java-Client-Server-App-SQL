import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class Habitat extends JFrame {

    // Динамические структуры, содержащие стаи рыб
    public static ArrayList<PikeFish> pikeArray = new ArrayList<>();
    public static ArrayList<CarpFish> carpArray = new ArrayList<>();
    // Динамическая структура, содержащая окружение
    public static ArrayList<SeaPlant> bottomVegetation = new ArrayList<>();
    private BufferedImage bg;
    private static String name = "unknown";

    public static Graphics2D graphics;
    public static Socket socket;
    public static ArrayList<Result> results = new ArrayList<>();

    private Thread draw, move;
    private Timer timer;
    public static int Clock;

    public static int online = 0;

    public static int WIDTHS = 1024, HEIGHT = 768;

    public void init() throws IOException {
        connect();
        getOnline();
        name = JOptionPane.showInputDialog(new Frame(),
                "Enter user name: ", name);
        setTitle("Acuario / Current User: " + name + " Online: " + online);
        setSize(WIDTHS, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        JButton ADD = new JButton("Add fish");
        JButton START = new JButton("Start");
        JButton STOP = new JButton("Stop");
        JButton SAVE = new JButton("Save data");
        JPanel CONTROL = new JPanel();

        JMenuBar menuBar = new JMenuBar();
        menuBar.setVisible(true);
        setJMenuBar(menuBar);
       // menuBar.add(createMenu());
        menuBar.add(createInfoMenu());


        CONTROL.setPreferredSize(new Dimension(250, 500));
        ADD.setPreferredSize(new Dimension(100, 50));
        START.setPreferredSize(new Dimension(100, 50));
        STOP.setPreferredSize(new Dimension(100, 50));
        SAVE.setPreferredSize(new Dimension(100, 50));

        START.setForeground(Color.GREEN);
        STOP.setForeground(Color.RED);

        add(CONTROL, BorderLayout.EAST);

//        CONTROL.add(START);
//        CONTROL.add(STOP);
//        CONTROL.add(ADD);
//        CONTROL.add(SAVE);




        graphics = (Graphics2D) getGraphics();

        bg = ImageIO.read(new File("C:\\Users\\Dominator\\IdeaProjects\\Course_Work_Acuario\\Images\\bg.jpg"));




        SeaPlant cp_o = new SeaPlant(100,460);
        SeaPlant cp_t = new SeaPlant(480,460);

        bottomVegetation.add(cp_o);
        bottomVegetation.add(cp_t);


        for(int i = 0; i < 6; i++){
            carpArray.add(new CarpFish());
            pikeArray.add(new PikeFish());
        }

        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Clock++;
                getOnline();
                setTitle("Acuario / Current User: " + name + " Online: " + online);
            }
        });

        draw = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    Habitat.super.paintComponents(graphics);
                    graphics.drawImage(bg, 0, 50, WIDTHS, HEIGHT, null);
                    for (int i = 0; i < pikeArray.size(); i++) {
                        pikeArray.get(i).Draw(graphics);
                        pikeArray.get(i).Run();
                       // repaint((int)pikeArray.get(i).getX(), (int)pikeArray.get(i).getY(),160, 50 );
                    }
                    for (int i = 0; i < carpArray.size(); i++) {
                        carpArray.get(i).Draw(graphics);
                        carpArray.get(i).Run();
                        //repaint((int)carpArray.get(i).get_X(), (int)carpArray.get(i).get_Y(),160, 50 );
                    }
                    for (int i = 0; i < bottomVegetation.size(); i++) {
                        bottomVegetation.get(i).Draw(graphics);
                        //repaint(bottomVegetation.get(i).getX(), bottomVegetation.get(i).getY(), 200, 400);
                    }
                    try {
                        Thread.sleep(35);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }

                }
            }
        });

        ADD.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double rand = Math.random();
                if(rand > 0.5) carpArray.add(new CarpFish());
                else pikeArray.add(new PikeFish());
            }
        });


        ActionListener save = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //getDbScore();
                sendPacket();
            }
        };
        ActionListener stop = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                draw.stop();
                timer.stop();
            }
        };
        SAVE.addActionListener(save);
        STOP.addActionListener(stop);
        draw.start();
        timer.start();
    }

    private JMenu createInfoMenu() {
        // создадим выпадающее меню
        JMenu viewMenu = new JMenu("Tools");

        JMenuItem BUTTON_INFO = new JMenuItem("Save Score");
        JMenuItem BUTTON_GET_SCORE = new JMenuItem("Get Scores");
        JMenuItem BUTTON_ADD = new JMenuItem("Add fish");


        viewMenu.add(BUTTON_INFO);
        viewMenu.addSeparator();
        viewMenu.add(BUTTON_GET_SCORE);
        viewMenu.addSeparator();
        viewMenu.add(BUTTON_ADD);

        BUTTON_INFO.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendPacket();
                JOptionPane.showMessageDialog(new Frame(),
                        "Score has been saved", "Dialog Window", JOptionPane.OK_OPTION,
                        new ImageIcon("C:\\Users\\Dominator\\IdeaProjects\\Course_Work_Acuario\\Images\\RoflanEbalo.jpg"));
            }
        });
        BUTTON_ADD.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double a = Math.random();
                if(a > 0.5) pikeArray.add(new PikeFish());
                else carpArray.add(new CarpFish());
            }
        });
        BUTTON_GET_SCORE.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getDbScore();
                ScoreFrame fr = new ScoreFrame();
            }
        });


        return viewMenu;
    }

    private JMenu createMenu() {
        return null;
    }

    public static void connect(){
        try{
           socket = new Socket("localhost", 1111);
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public static void sendPacket(){
        try{
            ObjectOutputStream dos = new ObjectOutputStream(socket.getOutputStream());
            dos.writeUTF(name);
            dos.writeInt(Clock);
            dos.writeInt(carpArray.size() + pikeArray.size());
            dos.flush();
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }
    public static void getDbScore(){
        try{
            ObjectOutputStream dos = new ObjectOutputStream(socket.getOutputStream());
            dos.writeUTF("results");
            dos.flush();
            while(true){
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                results = (ArrayList<Result>) ois.readObject();
                System.out.println("ЭТО УСПЕХ!!!! " + results.size());
                return;
            }
        }catch (IOException | ClassNotFoundException ex){
            ex.printStackTrace();
        }
    }
    public static void getOnline(){
        try {
            if(socket != null) {
                ObjectOutputStream ous = new ObjectOutputStream(socket.getOutputStream());
                ous.writeUTF("online");
                ous.flush();
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                online = ois.readInt();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void end(){
        try{
            socket.close();
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }
}
