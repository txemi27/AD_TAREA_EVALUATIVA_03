package EJERCICIO03;

import java.sql.*;
import java.util.*;
import java.util.regex.*;

public class tarea03 {
	
	public static void main(String[] args) {
		Scanner teclado = new Scanner(System.in);
		
		try {
			String dniNewAsistente = null;
			do {System.out.println("Introduce el DNI del asistente: ");
			dniNewAsistente = teclado.nextLine();
			}
			// Validar el formato del DNI
			while(!isValidDNI(dniNewAsistente)); 
					
			
			// Realizamos la consulta del DNI
			Connection conn = conexion();
			
			String sqlDNI = "SELECT \r\n"
					+ "a.nombre \r\n"
					+ "FROM \r\n"
					+ "asistentes a \r\n"
					+ "WHERE \r\n"
					+ "a.dni \r\n"
					+ "LIKE \r\n"
					+ "?";
			
			PreparedStatement statDNI = conn.prepareStatement(sqlDNI);
			
			statDNI.setString(1, dniNewAsistente);
			ResultSet rsDNI = statDNI.executeQuery();
			
			String resultadoDNI = null;
			
			while(rsDNI.next()) {
				resultadoDNI = rsDNI.getString("nombre");
			}
			
			
			if(resultadoDNI != null) {
				System.out.println("Estas realizando la reserva para: " + resultadoDNI);
			}
			else {
				System.out.println("No se encontro un asistente con el DNI proporcionado.");
				System.out.println("Introduce el nombre del asistente:");
				resultadoDNI = teclado.nextLine();
				
				String slqNuevoAsistente = "INSERT INTO \r\n"
						+ "asistentes(dni, nombre) \r\n"
						+ "values (?,?)";
				
				PreparedStatement statNuevoAsistente = conn.prepareStatement(slqNuevoAsistente);
				
				statNuevoAsistente.setString(1, dniNewAsistente);
				statNuevoAsistente.setString(2, resultadoDNI);
				
				int filasInsertadas = statNuevoAsistente.executeUpdate();
				
				if (filasInsertadas > 0) {
					System.out.println("Estas realizando la reserva para: " + resultadoDNI);
				} else {
					System.out.println("No se pudo agregar el nuevo asistente.");
				}
				
				
			}
			
			System.out.println("Lista de eventos:");
			
			// Traermos la lista de eventos con la capacidad actual.
			listaEventos();
			
			System.out.println("Elige el numero del evento al que quiere asistir: ");
			int numEvento = teclado.nextInt();
			
			// Comprobamos el numero de plazas restantes del evento seleccionado.
			boolean capacidadRestante = capacidadRestanteSeleccionado(numEvento);
					
			do { /* Comprobamos que aun quedan plazas disponibles*/
				asistirEvento(dniNewAsistente, numEvento, resultadoDNI);
			}
			while(!capacidadRestante);
			
		}
		catch(SQLException e) {
			System.out.println("Error al realizar la consulta con la BBDD " + e.getMessage());
		}
		finally {
			teclado.close();
		}
		
	}
	
	
	// Comprobamos que el DNI tenga el formato valido
	public static boolean isValidDNI(String dni) {
		
		String formato = "^[0-9]{8}[A-Za-z]$"; // Que el numero sea del 0-9 y tenga 8 digitos. Una letra que sea en mayuscula o minuscula.
		Pattern pattern = Pattern.compile(formato);
		Matcher matcher = pattern.matcher(dni);
		  if (!matcher.matches()) { // Si el DNI no es válido
		        System.out.println("El DNI no es valido. Debe tener 8 digitos seguidos de una letra.");
		        return false; // Retornar false si el formato no es válido
		    }
		    return true; // Retornar true si el formato es válido
	
		}
	
	// Metodo para devolver la lista de eventos y comprobar la capacidad restante de todos los eventos.
	public static void listaEventos() {
		
		Connection conn = conexion();
		
		try {
		Statement stmt = conn.createStatement();
		
		String listaEventos = "SELECT \r\n"
				+ "e.id_evento, \r\n"
				+ "e.nombre_evento, \r\n"
		        + "(u.capacidad - COUNT(ae.dni)) AS plazas_disponibles \r\n" 
		        + "FROM \r\n"
		        + "eventos e \r\n"
		        + "INNER JOIN \r\n"
		        + "ubicaciones u \r\n" 
		        + "ON e.id_ubicacion = u.id_ubicacion \r\n"
		        + "LEFT JOIN \r\n" 
		        + "asistentes_eventos ae \r\n" 
		        + "ON e.id_evento = ae.id_evento \r\n"
		        + "GROUP BY e.id_evento, e.nombre_evento, u.capacidad";
		
		
		ResultSet rs = stmt.executeQuery(listaEventos);
		
		int capacidadRestante = 0;
		
		while(rs.next()) {
			
			String nombreEvento = rs.getString("nombre_evento");
			capacidadRestante = rs.getInt("plazas_disponibles");
			int id = rs.getInt("id_evento");
			
			System.out.println(id + ". " + nombreEvento + " - Espacios disponibles: " + capacidadRestante);
		}
		}
		catch(SQLException e) {
			System.out.println("Error al consultar la capacidad del evento." + e.getMessage());
		}
		
	}
	
	// Metodo para comprobar la capacidad restante de un evento.
	public static boolean capacidadRestanteSeleccionado(int numEvento) {
		
		Connection conn = conexion();
		
		boolean capacidadRestante = false;
		
		
		try {
		String sql = "SELECT \r\n"
				+ "e.id_evento, \r\n"
		        + "(u.capacidad - COUNT(ae.dni)) AS plazas_disponibles \r\n" 
		        + "FROM \r\n"
		        + "eventos e \r\n"
		        + "INNER JOIN \r\n"
		        + "ubicaciones u \r\n" 
		        + "ON e.id_ubicacion = u.id_ubicacion \r\n"
		        + "LEFT JOIN \r\n" 
		        + "asistentes_eventos ae \r\n" 
		        + "ON e.id_evento = ae.id_evento \r\n"
		        + "WHERE \r\n"
		        + "e.id_evento LIKE ? \r\n"
		        + "GROUP BY e.id_evento, e.nombre_evento, u.capacidad";
	
		
		PreparedStatement stat = conn.prepareStatement(sql);
		
		stat.setInt(1, numEvento);
		
		ResultSet resultado = stat.executeQuery();
		
		while(resultado.next()) {
			int capacidad = resultado.getInt("plazas_disponibles");
			if(capacidad > 0) {
				capacidadRestante = true;
			}
		}
		
		}
		catch(SQLException e) {
			System.out.println("Error al consultar la capacidad total del evento seleccionado. " + e.getMessage());
		}
		
		return capacidadRestante;
	}
	
	
	public static void asistirEvento(String dniNewAsistente, int numEvento, String resultadoDNI) {
		
		Connection conn = conexion();
		
		try {
			
			// Insertamos el asistente al evento
			String sqlEvento = "INSERT INTO \r\n"
				+ "asistentes_eventos(dni, id_evento) \r\n"
				+ "values (?,?)";
						
				PreparedStatement statEvento = conn.prepareStatement(sqlEvento);
						
				statEvento.setString(1, dniNewAsistente);
				statEvento.setInt(2, numEvento);
						
				int nuevaReserva = statEvento.executeUpdate();
						
				if(nuevaReserva > 0) {
					System.out.println(resultadoDNI + " ha sido registrado para el evento seleccionado.");
				}
			
		}
		catch(SQLException e) {
			System.out.println("Ya has reservado para el evento seleccionado. Por favor, ejecuta de nuevo la aplicación para realizar otra reserva.");
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
