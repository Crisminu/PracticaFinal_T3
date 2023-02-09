package es.cristina;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class Gestor extends Thread {

    private final Socket socket;
    private DataOutputStream dos;
    private DataInputStream dis;
    private OutputStream os;
    private InputStream is;

    String RUTA = "/home/minuri/IdeaProjects/PracticaFinal_T3/src/main/resources/";

    public Gestor(Socket socket) throws IOException {

        this.socket = socket;
        //Canal de recibir
        is = socket.getInputStream();
        dis = new DataInputStream(is);
        //Canal de enviar
        os = socket.getOutputStream();
        dos = new DataOutputStream(os);
    }

    @Override
    public void run() {
        try {
            String command = "";
            while (!command.equals("quit")) {
                command = dis.readUTF();
                System.out.println("Lectura realizada");
                System.out.println(command);
                manejarComandos(command);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (dos != null) {
                    dos.close();
                }
                if (dis != null) {
                    dis.close();
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void manejarComandos(String comando) throws IOException {
        if (comando.equals("ls")) {
            String archivos = null;
            File file = new File(RUTA);
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                archivos += files[i].getName() + "\n";
                System.out.println(archivos);
            }
            dos.writeUTF(archivos);
        } else if (comando.contains("get")) {
            String nombreArchivo = comando.substring(4);
            System.out.println("Nombre de archivo " + nombreArchivo);

            File file = new File(RUTA + nombreArchivo);
            if (file.exists()) {
                System.out.println(file.length());
                //Manda al cliente el tamaño del archivo
                dos.writeLong(file.length());
                //
                BufferedInputStream fileIn = new BufferedInputStream(new FileInputStream(file));
                byte[] datos;
                datos = fileIn.readAllBytes();
                dos.write(datos, 0, (int) file.length());
                dos.flush();
                fileIn.close();
            } else {
                System.out.println("ERR: Archivo no encontrado");
            }
        } else if (comando.equals("quit")) {
            //para simplificar el bucle del cliente
            dos.writeUTF("Adiós");
        }
    }

}

