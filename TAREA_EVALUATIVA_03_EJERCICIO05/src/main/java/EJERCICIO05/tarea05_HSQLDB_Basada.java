package EJERCICIO05;

import java.sql.*;

public class tarea05_HSQLDB_Basada {
	
	    public static void main(String[] args) {
	        // URL de la base de datos HSQLDB basada en fichero
	        String url = "jdbc:hsqldb:file:mydatabase;shutdown=true"; // Cambia "mydatabase" por el nombre que desees
	        String user = "SA"; 
	        String password = ""; 

	        Connection connection = null;

	        try {
	            // Establecer la conexi�n
	            connection = DriverManager.getConnection(url, user, password);
	            System.out.println("Conexi�n establecida con la base de datos HSQLDB.");

	            // Verificar si la conexi�n es v�lida
	            if (connection.isValid(0)) {
	                System.out.println("La conexi�n es v�lida.");
	            } else {
	                System.out.println("La conexi�n no es v�lida.");
	            }
	        } catch (SQLException e) {
	            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
	        } finally {
	            // Cerrar la conexi�n
	            try {
	                if (connection != null) {
	                    connection.close();
	                    System.out.println("Conexi�n cerrada.");
	                }
	            } catch (SQLException e) {
	                System.out.println("Error al cerrar la conexi�n: " + e.getMessage());
	            }
	        }
	    }
}
