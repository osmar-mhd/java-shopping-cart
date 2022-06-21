package pkg3cv18_p1_vioratolozadaosmar;

//Librerias Utilizadas:
import java.io.*;//Librerias para input y output(entrada y salida) Para el Puerto
import java.net.*;//Librerias para los Sockets
import java.util.Scanner;////Librerias para input(Teclado) manejo de menu y entradas de Id product
import java.util.Vector;//Vector para info de los tickets
/*iText es una biblioteca para crear y manipular archivos PDF, RTF, y HTML en Java.*/
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfPTable;
//Formato iText:
import com.itextpdf.text.Chunk;//Orden de contenido
import com.itextpdf.text.Image;//Se declara instancia para agregar imagen al PDF
import com.itextpdf.text.Paragraph;//Se declara instancia para agregar texto al PDF
import com.itextpdf.text.FontFactory;//Formato de la letra: Arial, 12
//Fecha
import java.time.LocalDateTime;//Nos muestra una descripción de la fecha
import java.time.format.DateTimeFormatter;//Formato elegido: "dd/MM/yy HH:mm:ss"

public class Cliente {

    public static void main(String[] args) {
        try {
            //Globales
            char fin;//Char para controlar el Menu(Do While)
            Scanner teclado = new Scanner(System.in);//Lectura de teclado
            //Instancias para manipulacion de Base de Datos:
            Producto p = new Producto();// get & set
            ProductoDAO pd = new ProductoDAO();// Consultas a BD-create,update,delete,select

            Vector vector = new Vector();//Vector para Tickets
            Producto obj, data;//Manipulacion de vectores

            //////////////// Consola 
            System.out.println("===== Cliente Walmart ESCOM =====");

            /// Ingresamos el puerto: 3080
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Escriba el puerto:");
            int pto = Integer.parseInt(br.readLine());

            //Recibimos Imagenes del servidor, dependiendo la cantidad en la Base de Datos
            for (int i = 0; i < pd.readAll().size(); i++) {
                System.out.print(i);
                Imagen(pto);
            }

            do {
                //Guardamos en memoria
                int id[] = new int[pd.readAll().size()];
                String name[] = new String[pd.readAll().size()];
                String info[] = new String[pd.readAll().size()];
                int dispo[] = new int[pd.readAll().size()];
                float price[] = new float[pd.readAll().size()];

                for (int i = 0; i < pd.readAll().size(); i++) {
                    id[i] = pd.Consultar((Producto) pd.readAll().get(i)).getidProducto();
                    name[i] = pd.Consultar((Producto) pd.readAll().get(i)).getNombre();
                    info[i] = pd.Consultar((Producto) pd.readAll().get(i)).getInfo();
                    dispo[i] = pd.Consultar((Producto) pd.readAll().get(i)).getDisponibles();
                    price[i] = pd.Consultar((Producto) pd.readAll().get(i)).getPrecio();
                }

                //Entramos al menu
                ShowProduct(pd, id, name, info, dispo, price);//Mostramos datos de BD

                //Mostramos los datos del Vector(Carrito de compras)
                System.out.println("=== Carrito de compras: ===");
                float preciototal = 0;
                for (int i = 0; i < vector.size(); i++) {
                    obj = (Producto) vector.get(i);
                    System.out.println("NUM: " + i + ", Nombre: " + obj.getNombre() + ", Precio: " + obj.getPrecio() + ", Info:" + obj.getInfo());
                    preciototal += obj.getPrecio();
                }
                System.out.println("==");
                System.out.println("Precio Total:" + preciototal);

                //Opciones disponibles del Menu
                System.out.println("(1.- Agregar. || 2.- Modificar || 3.- Eliminar || 4- Comprar || 5- Salir)");
                System.out.print("Seleccione una opción:");
                int principal = teclado.nextInt();

                switch (principal) {
                    case 1: {
                        System.out.println("=== Agregar ===");
                        //Mostramos los productos que tenemos en la BD
                        ShowProduct(pd, id, name, info, dispo, price);
                        System.out.print("Seleccione el producto que desea(ID):");
                        int menu = teclado.nextInt();
                        //Agregamos el producto al Carrito(Vector)
                        try {
                            p.setidProductos(menu);
                            if (pd.Consultar(p).getNombre() != null) {
                                if (pd.Consultar(p).getDisponibles() > 0) {
                                    data = new Producto(pd.Consultar(p).getidProducto(), pd.Consultar(p).getNombre(), pd.Consultar(p).getDisponibles(), pd.Consultar(p).getPrecio(), pd.Consultar(p).getInfo());
                                    vector.add(data);
                                    fin = 'y';//Regresamos al menu principal
                                } else {
                                    //Si el producto no esta disponible por falta de existencias:
                                    System.out.println("Ya no hay " + pd.Consultar(p).getNombre() + " disponibles");
                                    fin = 'y';
                                }
                            } else {
                                //Si el ID proporcionado no existe en la BD
                                System.out.println("El ID del producto seleccionado no existe");
                                fin = 'y';
                            }
                        } catch (Exception e) {
                            //Exeption por si falla la BD
                            System.out.println("Error de Conexion con la BD");
                            fin = 'y';
                        }
                        break;
                    }
                    case 2: {
                        try {
                            //Update Data del carrito de compras
                            System.out.print("Seleccione el producto que desea Modificar(NUM):");
                            int modificar = teclado.nextInt();
                            vector.remove(modificar);
                            System.out.println("==");
                            //Mostramos productos disponibles
                            ShowProduct(pd, id, name, info, dispo, price);
                            System.out.print("Seleccione el nuevo Producto(ID):");
                            int menu = teclado.nextInt();
                            p.setidProductos(menu);
                            //Agregamos nuevo producto
                            if (pd.Consultar(p).getNombre() != null) {
                                if (pd.Consultar(p).getDisponibles() > 0) {
                                    data = new Producto(pd.Consultar(p).getidProducto(), pd.Consultar(p).getNombre(), pd.Consultar(p).getDisponibles(), pd.Consultar(p).getPrecio(), pd.Consultar(p).getInfo());
                                    vector.add(data);
                                    fin = 'y';
                                } else {
                                    //Si el ID proporcionado no existe en la BD
                                    System.out.println("Ya no hay " + pd.Consultar(p).getNombre() + " disponibles");
                                    fin = 'y';
                                }
                            } else {//Si el ID proporcionado no existe en la BD
                                System.out.println("El ID del producto seleccionado no existe");
                                fin = 'y';
                            }
                        } catch (Exception e) {//Exeption por si falla la BD
                            System.out.println("Error 402 no existe el num ingresado");
                            System.out.print("Desea volver al menú principal?(y/n):");
                            fin = 'y';
                        }
                        break;
                    }
                    case 3: {
                        try {////Eliminar producto de Ticket
                            System.out.print("Seleccione el producto que desea Eliminar(NUM):");
                            int eliminar = teclado.nextInt();
                            vector.remove(eliminar);
                            fin = 'y';
                        } catch (Exception e) {
                            System.out.println("Error 4012 no existe el num ingresado");
                            System.out.print("Desea volver al menú principal?(y/n):");
                            fin = 'y';
                        }
                        break;
                    }
                    case 4: {
                        System.out.println("=== Comprar ===");
                        System.out.println("Productos seleccionados:");
                        for (Object ob : vector) {
                            int i = Integer.valueOf(ob.toString());
                            p.setidProductos(i);
                            System.out.println(pd.Consultar(p).getNombre() + " - $" + pd.Consultar(p).getPrecio());
                            p.setDisponibles(p.getDisponibles() - 1);
                            pd.update(p);
                        }
                        Document PDF = new Document();
                        try {
                            String ruta = System.getProperty("user.home");
                            PdfWriter.getInstance(PDF, new FileOutputStream(ruta + "/Escritorio/Walmart.pdf"));

                            Image Walmart = Image.getInstance("src/images/super-venta-carrito-walmart.png");
                            Walmart.scaleToFit(300, 1000);
                            Walmart.setAlignment(Chunk.ALIGN_CENTER);

                            Paragraph calculus = new Paragraph();
                            calculus.setAlignment(Paragraph.ALIGN_CENTER);
                            calculus.setFont(FontFactory.getFont("Arial", 12));

                            Paragraph datos = new Paragraph();
                            datos.setAlignment(Paragraph.ALIGN_CENTER);
                            datos.setFont(FontFactory.getFont("Arial", 12));

                            PDF.open();
                            PDF.add(Walmart);
                            DateTimeFormatter fecha = DateTimeFormatter.ofPattern("yy/MM/dd HH:mm:ss");
                            datos.add(fecha.format(LocalDateTime.now()) + "\n");
                            datos.add("Cantidades(Productos comprados): " + vector.size() + "\n");
                            datos.add("\n");
                            PDF.add(datos);

                            PdfPTable tabla = new PdfPTable(2);
                            tabla.addCell("Nombre");
                            tabla.addCell("Costos Unitarios");
                            for (int i = 0; i < vector.size(); i++) {
                                obj = (Producto) vector.get(i);
                                String Precio = Float.toString(obj.getPrecio());
                                tabla.addCell(obj.getNombre());
                                tabla.addCell("$" + Precio);
                            }
                            calculus.add("Total a pagar: $" + preciototal + "\n");

                            PDF.add(tabla);
                            PDF.add(calculus);

                            vector.removeAllElements();
                            vector.clear();
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                        PDF.close();
                        System.out.println("El Ticket se generó en la siguiente ruta:/Escritorio/Walmart.pdf");
                        System.out.println("El Total a pagar es: $" + preciototal);
                        System.out.print("Desea volver al menú principal?(y/n):");
                        fin = teclado.next().charAt(0);
                        break;
                    }
                    case 5: {
                        //Salir
                        System.out.println("=== Salir ===");
                        System.out.print("Desea volver al menú principal?(y/n):");
                        fin = teclado.next().charAt(0);
                        break;
                    }
                    default: {
                        System.out.print("La opción seleccionada no existe.");
                        System.out.print("Desea volver al menú principal?(y/n):");
                        fin = teclado.next().charAt(0);
                    }
                }
            } while (fin == 'y' || fin == 'Y');//Regresar al menu principal
            System.out.println("Grácias por su preferenicia");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//Recibe: pd id name info dispo price y los muestra en la consola

    static void ShowProduct(ProductoDAO pd, int[] id, String[] name, String[] info, int[] dispo, float[] price) {
        System.out.println("Productos:");
        for (int i = 0; i < pd.readAll().size(); i++) {
            if (dispo[i] > 0) {
                System.out.print("ID: " + id[i]);
                System.out.print(", Nombre: " + name[i]);
                System.out.print(", Descripcion: " + info[i]);
                System.out.print(", Disponibles: " + dispo[i]);
                System.out.println(", Precio: " + price[i]);
            } else {
                System.out.print("El producto " + name[i] + " NO esta disponible ");
                System.out.print(", Cuenta con un Num de existencias: " + dispo[i] + "\n");
            }
        }
    }

    //Recibe puerto
    static void Imagen(int pto) {
        try {
            //Se define el socket
            Socket cl = new Socket("localhost", pto);//Creamos socket
            System.out.print("Data:" + cl.getInetAddress() + ":" + cl.getPort());//Get Datos
            DataInputStream dis = new DataInputStream(cl.getInputStream());//Conexion con Servidor
            byte[] b = new byte[1024];
            String nombre = dis.readUTF();//Guardamos nombre de readUFT nombre
            System.out.println(" Recibimos la imagen :" + nombre);//Mostramos nombre
            long tam = dis.readLong();//Guardamos tamaño
            DataOutputStream dos = new DataOutputStream(new FileOutputStream(nombre));//Retornamos
            long recibidos = 0;
            int n, porcentaje;
            while (recibidos < tam) {//Mandamos data al Servidor
                n = dis.read(b);
                dos.write(b, 0, n);
                dos.flush();
                recibidos = recibidos + n;
                porcentaje = (int) (recibidos * 100 / tam);
            }
            dos.close();//Cerramos conection
            dis.close();
            cl.close();//Cerramos Socket

        } catch (Exception e) {
            System.out.println("Posible Error:");
            System.out.println("El Servidor no está conectado");
        }
    }
}
