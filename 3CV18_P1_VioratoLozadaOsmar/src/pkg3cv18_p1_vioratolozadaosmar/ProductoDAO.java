package pkg3cv18_p1_vioratolozadaosmar;

//Propias de la conexion a la BD
import java.sql.Connection;//Sirve para crear una conexion con la BD con el metodo forName
import java.sql.DriverManager;//Es el manejador de drivers nosotros usaremos el JDBC - Conexion
import java.sql.PreparedStatement;//Preparas sentencias SQL
import java.sql.ResultSet;
import java.sql.SQLException;
//Listas
import java.util.ArrayList;
import java.util.List;
//Manejo de Exeptions
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductoDAO {
    //Globales
    //Consultas a la BD
    private static final String SQL_UPDATE = "update Producto set disponibles = ? where idProducto = ? ";
    private static final String SQL_SELECT_ALL = "select * from Producto";
    private static final String SQL_SELECT = "select * from Producto where idProducto = ?";
    //Conexion
    private Connection conexion = null;
    
    //Funcion para realizar conexion con BD
    private void obtenerConexion() {
        String usr = "root";//Usuario
        String pwd = "root";//Password
        String driver = "com.mysql.jdbc.Driver";//Driver
        //Ubicaion - nombre de la BD - Eliminar los warnings de la consola
        String url = "jdbc:mysql://localhost:3306/walmart?autoReconnect=true&useSSL=false";

        try {//
            Class.forName(driver);//Driver jdbc
            conexion = DriverManager.getConnection(url, usr, pwd);//Realizamos conexion
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ProductoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void update(Producto e) {
        obtenerConexion();//Realizamos conexion
        PreparedStatement ps = null;//Eliminamos data buffer
        try {
            ps = conexion.prepareStatement(SQL_UPDATE);//Preparamos sentencia
            ps.setInt(1, e.getDisponibles());//Establecemos los parametros
            ps.setInt(2, e.getidProducto());
            ps.executeUpdate();//Ejecutamos sentencia
        } catch (SQLException ex) {//Exeptions
            Logger.getLogger(ProductoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List readAll() {
        obtenerConexion();//Realizamos conexion
        PreparedStatement ps = null;//Eliminamos data buffer
        ResultSet rs = null;//Eliminamos data buffer
        List resultados = null;//Eliminamos data buffer
        try {
            ps = conexion.prepareStatement(SQL_SELECT_ALL);//Preparamos sentencia
            rs = ps.executeQuery();//Ejec senten - nos da datos de la sentencia en una espec tabla  
            resultados = obtenerResultados(rs);//Los guardamos en una lista
        } catch (SQLException ex) {//Exeptions
            Logger.getLogger(ProductoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resultados;
    }

    public Producto Consultar(Producto e) {
        obtenerConexion();//Realizamos conexion
        PreparedStatement ps = null;//Eliminamos data buffer
        ResultSet rs = null;//Eliminamos data buffer
        try {
            ps = conexion.prepareStatement(SQL_SELECT);//Preparamos sentencia
            ps.setInt(1, e.getidProducto());//Establecemos los parametros
            rs = ps.executeQuery();//Ejec senten - nos da datos de la sentencia en una espec tabla
            while (rs.next()) {//Recorremos el ResulSet
                e.setidProductos(rs.getInt("idProducto"));
                e.setNombre(rs.getString("nombre"));
                e.setPrecio(rs.getFloat("precio"));
                e.setDisponibles(rs.getInt("disponibles"));
                e.setInfo(rs.getString("info"));
                return e;//Regresa el valor del Select 
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return e;
    }

    private List obtenerResultados(ResultSet rs) {//Recibimos los datos del ResultSet
        List resultados = new ArrayList();//Los guardamos en una lista
        try {
            while (rs.next()) {
                Producto e = new Producto();//Realizamos instancia Producto
                e.setidProductos(rs.getInt("idProducto"));//Leemos parametros
                e.setNombre(rs.getString("nombre"));
                e.setPrecio(rs.getFloat("precio"));
                e.setDisponibles(rs.getInt("disponibles"));
                e.setInfo(rs.getString("info"));
                resultados.add(e);//Agregamos a la lista
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resultados;//Retornmaos el valor de la lista para el readAll
    }
}