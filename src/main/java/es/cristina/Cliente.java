package es.cristina;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        InetAddress direccion = null; // La IP o Dirección de conexió
        Socket socket = null; // Socket para conectarnos a un servidor u otra máquina
        String command = "";

        final int PUERTO = 50000;

        System.out.println("Soy el cliente e intento conectarme");

        try {
            direccion = InetAddress.getLocalHost();
            //Conectando con el servidor
            socket = new Socket(direccion, PUERTO);
            //CANALES
            //Canal de recibir
            InputStream is = socket.getInputStream();
            DataInputStream dis = new DataInputStream(is);
            //Canal de enviar
            OutputStream os = socket.getOutputStream();
            DataOutputStream dos = new DataOutputStream(os);

            while (!command.equals("quit")) {
                System.out.print("miniftp>");
                command = sc.nextLine();
                //Mandar el comando por el dataoutputstream
                dos.writeUTF(command);
                if (command.equals("ls")) {
                    String lista = dis.readUTF();
                    System.out.println(lista);
                } else if (command.contains("get")) {
                    String archivo = command.substring(4);
                    System.out.println(archivo);
                    //Tamaño
                    long t = dis.readLong();
                    if(t == -1){
                        System.out.println(dis.readUTF());
                    }else{
                        byte[] datos = new byte[(int) t];
                        dis.readFully(datos);
                        Path path = Paths.get(archivo);
                        Files.write(path, datos);
                        System.out.println("El archivo " + archivo + "ha sido creado.\n Tamaño: " + datos.length + " bytes");
                    }

                } else if (command.equals("quit")) {
                    String saludo = dis.readUTF();
                    System.out.println(saludo);
                }
            }
            //Una vez que recibe el número de bytes completo, lo salva en disco
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
        //cliente recibe con input en bucle, termina cuando recibe un -1, como no se cierra el Stream no llega a recibir el -1

}
