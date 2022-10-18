import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Stack;

public class Server {
    private ServerSocket serverSocket;
    public static ArrayList<ClientHandler> arrayList = new ArrayList<ClientHandler>();

    public static final String url = "jdbc:mysql://localhost:3306/mysql", username = "admin", password = "admin";
    public static Connection connection = null;
    public static Statement statement = null;




    Server(){
        connect();
        try {
            connection = DriverManager.getConnection(url,username,password);
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        handle();
        end();
    }
    public void connect(){
        try {
            serverSocket = new ServerSocket(1111);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void handle(){
        while (true){
            try{
                Socket client = serverSocket.accept();
                arrayList.add(new ClientHandler(client));
                System.out.println("Online Users: " + arrayList.size());
            }catch (IOException ex){
                ex.printStackTrace();
            }
        }
    }
    public void end(){
        try{
            serverSocket.close();
        }catch (IOException exc){
            exc.printStackTrace();
        }
    }

}
