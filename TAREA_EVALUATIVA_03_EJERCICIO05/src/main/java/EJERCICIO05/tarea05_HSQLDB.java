package EJERCICIO05;

import java.sql.*;

public class tarea05_HSQLDB {
	
	
	    public static void main(String[] args) {
	        String url = "jdbc:hsqldb:file:mydatabase;shutdown=true"; 
	        String user = "SA"; 
	        String password = ""; 

	        try (Connection conn = DriverManager.getConnection(url, user, password)) {
	            System.out.println("Conexión a la base de datos HSQLDB establecida.");
	            
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	

}
