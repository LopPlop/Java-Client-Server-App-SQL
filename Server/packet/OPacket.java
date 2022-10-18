package Server.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;



public class OPacket {

    private int id, N, type, x, y;
    private Socket socket;


    public OPacket(){ id = 0; }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public Socket getSocket(){
        return socket;
    }

    public void setSocket(Socket socket){
        this.socket = socket;
    }

    public OPacket(int id,  int type, int x, int y) { this.id = id; this.x = x; this.type = type; this.y = y; }

    public OPacket(int id){
        this.id = id;
    }


    public void handle() {
        try {
            DataOutputStream dos = new DataOutputStream(new Socket("localhost",1111).getOutputStream());
            dos.writeInt(id);
            dos.writeInt(type);
            dos.writeInt(x);
            dos.writeInt(y);
            System.out.println("Sended id: " + id + "; type: " + type +  "; x: " + x + "; y:" + y );
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
