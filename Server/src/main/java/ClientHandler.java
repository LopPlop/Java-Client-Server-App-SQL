import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ClientHandler extends Thread{
    private int id;
    public static ArrayList<Result> arrayList = new ArrayList<>();
    Socket socket;

    ClientHandler(Socket client){
        socket = client;
        start();
    }
    public void handle(String name, int time, int obj_c){
        String insert = "insert into business.acuascores (username, fish_count, time) values (\""+name+"\", " +Integer.toString(obj_c) + ", "+Integer.toString(time)+")";
        System.out.println(insert);
        try {
            Server.statement.execute(insert);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void DbHandler(){
        arrayList.clear();
        try {
            PreparedStatement pstmt = Server.connection.prepareStatement("select id, username, fish_count, time from business.acuascores");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                Result res = new Result();
                res.setId(rs.getInt("id"));
                res.setUsername(rs.getString("username"));
                res.setFish_count(rs.getInt("fish_count"));
                res.setTime(rs.getInt("time"));
                res.print();
                arrayList.add(res);
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void checkOnline(){
        try {
            ObjectOutputStream ous = new ObjectOutputStream(socket.getOutputStream());
            ous.writeInt(Server.arrayList.size());
            ous.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try{
            while (true) {
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                String name = (String) ois.readUTF();
                switch (name){
                    case "results": {
                        DbHandler();
                        ObjectOutputStream ous = new ObjectOutputStream(socket.getOutputStream());
                        ous.writeObject(arrayList);
                        System.out.println("Отправлено!");
                        ous.flush();
                        break;
                    }
                    case "online": {
                        checkOnline();
                        break;
                    }
                    case "end": {
                        stop();
                        break;
                    }
                    default: {
                        int time = ois.readInt();
                        int obj_c = ois.readInt();
                        handle(name, time, obj_c);
                        break;
                    }
                }
            }
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }
}
