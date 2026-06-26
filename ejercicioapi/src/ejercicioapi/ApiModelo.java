package ejercicioapi;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import org.json.JSONObject;
import org.json.JSONException;

public class ApiModelo {
    private final String api_url;
    public HttpResponse<String> respuesta_api;

    public ApiModelo () {
        // Corrección de la URL base apuntando al recurso posts
        this.api_url = "https://jsonplaceholder.typicode.com/posts";
        this.respuesta_api = null;
    }
    
    // Operación 1: Buscar una publicación específica por ID (GET)
    public HttpResponse<String> hacer_peticion_get(int idPublicacion) {
        try {
            HttpClient cliente = HttpClient.newHttpClient();
            // Concatenamos el ID a la URL base (Ej: .../posts/15)
            HttpRequest peticion = HttpRequest.newBuilder()
                    .uri(URI.create(this.api_url + "/" + idPublicacion))
                    .header("Accept", "application/json")
                    .GET()
                    .build();
            this.respuesta_api = cliente.send(peticion, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException ex) {
            System.out.println("Error en GET: " + ex.getMessage());
        }
        return this.respuesta_api;
    }
    
    // Operación 2: Transformar una respuesta JSON cruda a un objeto ModeloInfo
    public InfoModelo convertir_info_respuesta(String jsonCrudo) {
        try {
            // Como consultamos un ID específico, la respuesta es un Objeto {}, no un Array []
            JSONObject obj = new JSONObject(jsonCrudo);
            InfoModelo info = new InfoModelo();
            info.setId(obj.getInt("id"));
            info.setUserId(obj.getInt("userId"));
            info.setTitle(obj.getString("title"));
            info.setBody(obj.getString("body"));
            return info;
        } catch (JSONException ex) {
            System.out.println("Error al parsear JSON: " + ex.getMessage());
            return null;
        }
    }
    
    // Operación 3: Empaquetar un objeto validado y simular el envío (POST)
    public void hacer_peticion_post(InfoModelo infoNueva) {
        // Estructuramos dinámicamente el JSON con los datos del objeto real
        String texto_json = """
                            {
                                "userId": %d,
                                "title": "%s",
                                "body": "%s"
                            }
                            """.formatted(infoNueva.getUserId(), infoNueva.getTitle(), infoNueva.getBody());
        try {
            HttpClient cliente = HttpClient.newHttpClient();
            HttpRequest peticion = HttpRequest.newBuilder()
                    .uri(URI.create(this.api_url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(texto_json))
                    .build();
            this.respuesta_api = cliente.send(peticion, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException ex) {
            System.out.println("Error en POST: " + ex.getMessage());
        }
    }
    
    // Operación 4: Imprimir el objeto con un formato elegante
    public void imprimir_info(InfoModelo info) {
        if (info != null) {
            System.out.println("=========================================");
            System.out.println("       PUBLICACIÓN ENCONTRADA            ");
            System.out.println("=========================================");
            System.out.println("ID: " + info.getId());
            System.out.println("User ID: " + info.getUserId());
            System.out.println("Título: " + info.getTitle());
            System.out.println("Contenido: " + info.getBody());
            System.out.println("=========================================");
        } else {
            System.out.println("No hay datos disponibles para mostrar.");
        }
    }
    
    // Operación 5: El cerebro del negocio. Sincronizar y evaluar exclusividad
    public void gestionar_sincronizacion(InfoModelo infoLocal, int idAVerificar) {
        System.out.println("\n[Sincronizador] Verificando existencia en el servidor remoto...");
        hacer_peticion_get(idAVerificar);
        
        // Si el servidor responde 200 significa que la publicación con ese ID ya existe
        if (this.respuesta_api != null && this.respuesta_api.statusCode() == 200) {
            System.out.println("[ALERTA] La publicación con ID (" + idAVerificar + ") ya existe en el servidor.");
            
            // Convertimos la respuesta para comprobar el contenido
            InfoModelo infoRemota = convertir_info_respuesta(this.respuesta_api.body());
            
            // Si coincide en el título, es una copia exacta. Se descarta.
            if (infoRemota != null && infoRemota.getTitle().equalsIgnoreCase(infoLocal.getTitle())) {
                System.out.println("[RECHAZADO] Trabajo duplicado detectado. La idea ha sido descartada.");
            } else {
                System.out.println("[INFO] El ID está ocupado pero el título es diferente. Intente con otro ID.");
            }
        } else {
            // Si no existe (da 404), procedemos a subir la "idea fresca" con el POST
            System.out.println("[ÉXITO] Idea fresca detectada. Procediendo a almacenar en el servidor...");
            hacer_peticion_post(infoLocal);
            
            if (this.respuesta_api != null && (this.respuesta_api.statusCode() == 201 || this.respuesta_api.statusCode() == 200)) {
                System.out.println("[SISTEMA] Publicación almacenada con éxito en la nube.");
                System.out.println("Respuesta del servidor: " + this.respuesta_api.body());
            }
        }
    }
}