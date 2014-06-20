
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

public class Servidor extends Thread{
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private int maxClient=10;
    private int port;
    private InputStream in1;
    private OutputStream out1;
    private DataInputStream in;
    private DataOutputStream out;
    public ServidorGui sg;
    private hiloCliente[] hilos=new hiloCliente[maxClient];
    
    public Servidor(int port,ServidorGui ven){
        this.port=port;
        sg=ven;
        sg.setStartedButton("SERVICE STARTED");
    }
    
    public void run(){
        int i=0;
        try{
            serverSocket=new ServerSocket(this.port);
            System.out.println("Listening on port "+this.port);
        }catch(Exception e){}
        
        while(true){
            try{
            clientSocket=serverSocket.accept();
            for(i=0;i<maxClient;i++){
                if(hilos[i]==null){
                    (hilos[i]=new hiloCliente(clientSocket,hilos)).start();
                    System.out.println("Cliente "+i);
                    break;
                }
            }
            if(i==maxClient)
                clientSocket.close();
            }catch(Exception e){}
        }
    }
}

class hiloCliente extends Thread{
    private Socket client;
    private InputStream in1;
    private OutputStream out1;
    private DataInputStream in;
    private DataOutputStream out;
    private final hiloCliente[] hilos;
    private int max;
    
    public hiloCliente(Socket cliente,hiloCliente[] hilo){
        client=cliente;
        hilos=hilo;
        max=hilos.length;
    }
    
    public void run(){
        try{
            in1=client.getInputStream();
            out1=client.getOutputStream();
            in=new DataInputStream(in1);
            out=new DataOutputStream(out1);
            String str;
            
            SocketAddress ip=client.getRemoteSocketAddress();
            
            System.out.println(in.readUTF());
            out.writeUTF("Bienvenido al chat seras identificado por tu ip: "+ip);
            while(true){
                str=in.readUTF();
                for(int i=0;i<max;i++){
                    if(hilos[i]!=null){
                    hilos[i].out.writeUTF(str);
                    hilos[i].out.flush();
                    }
                }
            }
        }catch(Exception e){System.out.println("el error es "+e);}
    }
}
