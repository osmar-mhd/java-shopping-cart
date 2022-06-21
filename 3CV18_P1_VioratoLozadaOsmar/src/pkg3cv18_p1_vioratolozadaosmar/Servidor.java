package pkg3cv18_p1_vioratolozadaosmar;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JFileChooser;

public class Servidor {

    public static void main(String[] args) {
        System.out.println("=== Servidor Walmart ESCOM ===");
        System.out.println("Esperando Conexión...");
        System.out.println("===");
        for (;;) {
            try {
                ServerSocket s = new ServerSocket(3080);
                Socket cl = s.accept();

                //Se usa JfileChooser() para elegir el archivo a enviar
                JFileChooser jf = new JFileChooser();
                int r = jf.showOpenDialog(null);

                //Una vez seleccionado, se obtienen sus datos principales
                if (r == JFileChooser.APPROVE_OPTION) {
                    File f = jf.getSelectedFile();  //Manejador
                    String archivo = f.getAbsolutePath(); //Dirección
                    String nombre = f.getName(); //Nombre
                    long tam = f.length();  //Tamaño

                    //Se definen dos flujos orientados a bytes, uno se usa para leer
                    //y el otro para enviar los datos por el socket
                    DataOutputStream dos = new DataOutputStream(cl.getOutputStream());
                    DataInputStream dis = new DataInputStream(new FileInputStream(archivo));

                    //Enviamos los datos generales del archivo por el socket
                    dos.writeUTF(nombre);
                    dos.flush();
                    dos.writeLong(tam);
                    dos.flush();

                    //Leemos los dato contenidos en el archivo en paquetes de 1024
                    //y los enviamos en el socket
                    byte[] b = new byte[1024];
                    long enviados = 0;
                    int porcentaje, n;

                    while (enviados < tam) {
                        n = dis.read(b);
                        dos.write(b, 0, n);
                        dos.flush();
                        enviados = enviados + n;
                        porcentaje = (int) (enviados * 100 / tam);
                        System.out.print("Enviado: " + porcentaje + "%\r");
                    }//While
                    //Cerramos los flujos, el socket, terminamos bloques y cerramos la clase
                    System.out.println("\nImagen del Producto Enviada a:" + cl.getInetAddress() + "-Puerto:" + cl.getLocalPort());
                    System.out.println("===");
                    dos.close();
                    dis.close();
                    cl.close();
                    s.close();
                }
            } catch (Exception e) {
                System.out.println("");
            }
        }
    }
}
