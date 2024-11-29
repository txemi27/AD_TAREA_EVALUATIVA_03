package EJERCICIO02;

import java.sql.*;
import java.util.*; // import para la funcion teclado

public class tarea02 {
	
	public static void main(String[] args) {
		
		Scanner teclado = new Scanner(System.in);
		
		try {
			Connection conn = conexion();
			
			System.out.print("Introduce el nombre de la ubicacion: ");
			String ubicacion = teclado.nextLine();
			
			// Query para obtener la capacidad de la ubicacion introducida por teclado.
			String sqlUbicacion = "SELECT\r\n "
					+ "u.capacidad\r\n"
					+ "FROM \r\n"
					+ "ubicaciones u \r\n"
					+ "WHERE \r\n"
					+ "u.nombre \r\n"
					+ "LIKE \r\n"
					+ "?";
			PreparedStatement statUbicacion = conn.prepareStatement(sqlUbicacion);
			
			statUbicacion.setString(1, ubicacion);
			ResultSet rsUbicacion = statUbicacion.executeQuery(); 
			
			int capacidadUbicacion = 0; 
			
			while(rsUbicacion.next()) {
				capacidadUbicacion = rsUbicacion.getInt("capacidad");	
			}
			
			// Si la capacidad de la ubicaciones es distinto a 0 procedemos a introducir una nueva capacidad maxima
			if(capacidadUbicacion != 0 ) {
				System.out.println("La capacidad actual de la ubicación '" + ubicacion + "' es: " + capacidadUbicacion);
				System.out.print("Introduce la nueva capacidad maxima: ");
				int nuevaCapacidad = teclado.nextInt();
	
				// Query para actualizar la capacidad de la ubicaciones
				String sqlCapacidad = "UPDATE\r\n"
						+"ubicaciones u \r\n" 
						+ "SET \r\n"
						+ "u.capacidad = ? \r\n"
						+ "WHERE \r\n"
						+ "u.nombre \r\n"
						+ "LIKE \r\n"
						+ "?";
				
				PreparedStatement statCapacidad = conn.prepareStatement(sqlCapacidad);
				
				statCapacidad.setInt(1, nuevaCapacidad);
				statCapacidad.setString(2, ubicacion);
				
				int capacidadActualizada = statCapacidad.executeUpdate();
				
				if(capacidadActualizada > 0) {
					System.out.println("Capacidad actualizada correctamente");
				}
				else {
					System.out.println("No se ha podido realizar la actualizacion de la capacidad");
				}
				
			}
			else {
				System.out.println("No se encontro la ubicacion");
			}
			
			
			conn.close();
			
		}
		catch(Exception e) {
			System.out.println("Fallo al establecer la conexion " + e.getMessage());
		}
		
		finally {
			teclado.close(); 
		}
		
	}
	
	
	// Metodo para la conexion con la base de datos MySQL.
	public static Connection conexion() {
		
		String basedatos = "dbeventos";
		String url = "jdbc:mysql://127.0.0.1:3306/" + basedatos;
		String user = "root";
		String pwd = "secret";
		
		Connection conn = null;
		
		try {
			conn = DriverManager.getConnection(url, user, pwd);
			
		}
		catch(SQLException e) {
			System.out.println("Fallo al conectar con la BBDD " + e.getMessage());
		}
		
		return conn;
		
	}

}
