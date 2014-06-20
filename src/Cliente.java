
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import javax.swing.JFrame;

public class Cliente extends Thread{
    private Socket socket;
    private InputStream in1;
    private OutputStream out1;
    private DataInputStream in;
    private DataOutputStream out;
    private ClienteGui vent;
    private boolean cerrar=false;
    
    public Cliente(int port,ClienteGui ven){
        vent=ven;
        try{
            socket=new Socket("localhost",port);
            in1=socket.getInputStream();
            out1=socket.getOutputStream();
            in=new DataInputStream(in1);
            out=new DataOutputStream(out1);
            
            this.start();
            out.writeUTF("hola servidor");
            out.flush();
            
        }catch(Exception e){}
    }
    
    public void run(){
        String respuesta;
        try{
            while((respuesta=in.readUTF()) != null){
                if(cerrar)
                    break;
                vent.addText(respuesta);
            }    
        }catch(Exception e){}
        }
    
    public void close(){
        try{
        cerrar=true;
        vent.addText("Hasta luego");
        out.writeUTF("Log out");
        out.flush();
        
        in.close();
        out.close();
        socket.close();
        }catch(Exception e){}
    }
    
    public void send(String str){
        try{
        out.writeUTF(str);
        out.flush();
        }catch(Exception e){}
    }
    
    public void setList(String str){
        
    }
}
