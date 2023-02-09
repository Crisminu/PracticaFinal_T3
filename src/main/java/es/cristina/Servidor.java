package es.cristina;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Servidor {


    public static void main(String[] args) throws IOException {
       //Se abre canal de comunicación
       ServerSocket servidor = null;
       Socket socket;
       int cliente = 1;
       int PUERTO = 50000;
        //Se publica en la red la dir (PUERTO)
        System.out.println("(Servidor) Escuchando peticiones por el puerto + " + PUERTO);
        try{
            servidor = new ServerSocket(PUERTO);
            servidor.setReuseAddress(true);
            //Espera recibir solicitudes con while
            while(true){
                socket = servidor.accept();
                System.out.println("\t Llega el cliente: " + cliente);
                System.out.println("(Servidor) Conexión establecida" + socket.getInetAddress().getHostAddress());

                Gestor gestor = new Gestor(socket);
                new Thread(gestor).start();

            }
        } catch (IOException e) {
           e.printStackTrace();
        }finally{
            if(servidor != null){
                try{
                    servidor.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }
 //Servidor manda el número de bytes del archivo al Cliente

    //bucle que manda con output
}
