package EJERCICIO04;

import java.sql.*;
import java.util.*;

public class tarea04 {
	
	public static void main(String[] args) {
		
		Scanner teclado = new Scanner(System.in);
		
		try {
		System.out.println("Lista de eventos:");
		
		listaEventos();
		System.out.println();
		
		System.out.println("Introduce el ID del evento para consultar la cantidad de asistentes:");
		int idEvento = teclado.nextInt();
		
		callFuncion(idEvento);
		
		teclado.close();
		
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	// Realizamos una llamada a la funcion preparada en la vase de datos
	public static void callFuncion(int idEvento) {
		Connection conn = conexion();
		
		try {
			CallableStatement c = conn.prepareCall("{Call obtener_numero_asistentes(?,?)}");
			
			// Valor de entrada
			c.setInt(1, idEvento);
			
			  // Registrar el parámetro de salida
	        c.registerOutParameter(2, java.sql.Types.INTEGER);
	        
	       
	        c.execute();
	        
	        // Obtener el número de asistentes del parámetro de salida
	        int asistentes = c.getInt(2);
			
			
			System.out.println("El numero de asistentes para el evento seleccionado es: " + asistentes);
			
		}
		catch(SQLException e) {
			System.out.println("No se encuentra el ID introducido. " + e.getMessage());
		}
		
	}

	// Metodo para mostrar la lista de eventos.
	public static void listaEventos() {
		Connection conn = conexion();
		
		try {
			
			Statement stmt = conn.createStatement();
			
			String sql = "SELECT e.id_evento, e.nombre_evento \r\n"
					+ "FROM eventos e \r\n";
			
			ResultSet rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				int id = rs.getInt("id_evento");
				String evento = rs.getString("nombre_evento");
				System.out.println(id + ". " + evento);
			}
			
		}
		catch(SQLException e) {
			System.out.println("Error al consultar el listado de eventos. " + e.getMessage());
		}
		
	}
	
	// Metodo para establecer la conexión con la BBDD.
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
				System.out.println("Error al conectar con la BBDD " + e.getMessage());
			}
			
			
			return conn;
		}

}
