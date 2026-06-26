package ejercicioapi;

import java.util.Scanner;

public class Ejercicioapi {

    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        ApiModelo objApiModelo = new ApiModelo();
        
        System.out.println("--- PROYECTO ECO-DATOS: FILTRO DE EXCLUSIVIDAD ---");
        
        try {
            // 1. Preguntar datos al usuario por consola
            System.out.print("Ingrese el ID del autor (User ID): ");
            int userId = Integer.parseInt(teclado.nextLine());
            
            System.out.print("Ingrese el título de la publicación (Máx 50 carac.): ");
            String title = teclado.nextLine();
            
            System.out.print("Ingrese el cuerpo del artículo (Mín 15 carac.): ");
            String body = teclado.nextLine();
            
            // 2. Intentar instanciar el molde (Aquí saltarán las excepciones si rompe alguna regla)
            InfoModelo nuevaPublicacion = new InfoModelo(userId, title, body);
            
            System.out.print("¿Qué ID de publicación desea verificar en el servidor?: ");
            int idVerificar = Integer.parseInt(teclado.nextLine());
            
            // 3. Ejecutar la lógica de sincronización completa
            objApiModelo.gestionar_sincronizacion(nuevaPublicacion, idVerificar);
            
        } catch (NumberFormatException e) {
            System.out.println("[ERROR DATO] Asegúrese de ingresar números válidos en los campos de ID.");
        } catch (IllegalArgumentException e) {
            // Aquí atrapamos los errores definidos en las Reglas de Negocio de InfoModelo
            System.out.println("[ERROR REGLA DE NEGOCIO] " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Ocurrió un error inesperado: " + e.getMessage());
        } finally {
            teclado.close();
        }
    }
}
