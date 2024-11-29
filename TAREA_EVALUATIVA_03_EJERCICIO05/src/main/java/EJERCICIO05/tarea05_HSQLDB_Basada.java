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
	            // Establecer la conexión
	            connection = DriverManager.getConnection(url, user, password);
	            System.out.println("Conexión establecida con la base de datos HSQLDB.");

	            // Verificar si la conexión es válida
	            if (connection.isValid(0)) {
	                System.out.println("La conexión es válida.");
	            } else {
	                System.out.println("La conexión no es válida.");
	            }
	        } catch (SQLException e) {
	            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
	        } finally {
	            // Cerrar la conexión
	            try {
	                if (connection != null) {
	                    connection.close();
	                    System.out.println("Conexión cerrada.");
	                }
	            } catch (SQLException e) {
	                System.out.println("Error al cerrar la conexión: " + e.getMessage());
	            }
	        }
	    }
}
