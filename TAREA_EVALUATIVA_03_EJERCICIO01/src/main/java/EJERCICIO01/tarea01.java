package EJERCICIO01;

import java.sql.*; // Importamos java.sql para poder trabajar con la base de datos

public class tarea01 {
	
	public static void main(String[] args) {
		
		 try {
			 
			 Connection conn = conexion();
			 Statement sentencia = conn.createStatement();
			 String sql = "SELECT \r\n"
			 		+ "    e.nombre_evento, \r\n"   /* Nombre del evento asociado a su tabla */
			 		+ "    COUNT(ae.dni) AS numero_asistentes,\r\n" /* Contamos los dni de asistentes y a esa cuenta le llamamos numero_asistentes */ 
			 		+ "    u.nombre AS nombre_ubicacion, \r\n" /* A la columna nombre de las ubicacion le llamamos nombre_ubicacion */
			 		+ "    u.direccion\r\n" /* Campo direccion de la tabla ubicaciones*/
			 		+ "FROM \r\n"
			 		+ "    eventos e\r\n" /* La relación se realiza desde la tabla eventos. */
			 		+ "INNER JOIN \r\n"
			 		+ "    ubicaciones u \r\n" /* Hacemos una inner join con la tabla ubicaciones a traves de su campo relacionado con la tabla eventos */ 
			 		+ "    ON e.id_ubicacion = u.id_ubicacion\r\n"
			 		+ "INNER JOIN \r\n"
			 		+ "    asistentes_eventos ae \r\n" /* Hacemos una inner join con la tabla asistentes_eventos a traves de su campo relacionado con la tabla eventos */ 
			 		+ "    ON e.id_evento = ae.id_evento\r\n"
			 		+ "GROUP BY \r\n"
			 		+ "    e.nombre_evento, u.nombre, u.direccion\r\n" /* Lo agrupamos en el siguiente orden para usar la funcion count de los asistentes */ 
			 		+ "ORDER BY \r\n"
			 		+ "	   e.nombre_evento DESC"; /* Lo ordenamos de forma descendente */
			 
			 ResultSet resul = sentencia.executeQuery(sql);
			 
			 String[] headers = {"Evento", "Asistentes", "Ubicacion", "Direccion"};
			 
			 System.out.printf("%-30s | %-10s | %-40s | %-30s%n", headers[0], headers[1], headers[2], headers[3]);
			 System.out.println("--------------------------------------------------------------------------------------------------------------------------");
			 
			 while (resul.next()) {
				 
				 String nombreEvento = resul.getString("nombre_evento");
				 int nAsistentes = resul.getInt("numero_asistentes");
				 String ubicacion = resul.getString("nombre_ubicacion");
				 String direccion = resul.getString("direccion");
				 
				 System.out.printf("%-30s | %-10s | %-40s | %-30s%n", nombreEvento, nAsistentes, ubicacion, direccion);
			 }
			 
		 }
		 catch(SQLException e) {
			 System.out.println("Fallo al establecer la conexion" + e.getMessage());
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
			 System.out.println("Fallo al establecer conexion " + e.getMessage());
		 }
		
		return conn;
	}
	

}
