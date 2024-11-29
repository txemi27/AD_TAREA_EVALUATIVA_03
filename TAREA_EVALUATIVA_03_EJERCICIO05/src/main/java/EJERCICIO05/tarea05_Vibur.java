package EJERCICIO05;

import org.vibur.dbcp.ViburDBCPDataSource;

import java.sql.*;

public class tarea05_Vibur {

	    public static void main(String[] args) {
	        // Configuración del DataSource
	        ViburDBCPDataSource dataSource = new ViburDBCPDataSource();
	        dataSource.setJdbcUrl("jdbc:hsqldb:file:mydatabase;shutdown=true"); // URL de la base de datos
	        dataSource.setUsername("SA"); 
	        dataSource.setPassword(""); 
	        dataSource.start(); 

	        try (Connection conn = dataSource.getConnection()) {
	            System.out.println("Conexión a la base de datos establecida usando Vibur.");
	            dataSource.close();
	            
	        } catch (SQLException e) {
	            e.printStackTrace();
	        } 
	    }
	
	

}
