package ejercicioapi;

public class InfoModelo {
    private int id;
    private int userId;
    private String title;
    private String body;

    // Constructor vacío
    public InfoModelo() {}

    // Constructor completo
    public InfoModelo(int userId, String title, String body) {
        setUserId(userId);
        setTitle(title);
        setBody(body);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    
    // Regla 1: El userId no puede ser menor o igual a cero
    public void setUserId(int userId) {
        if (userId <= 0) {
            throw new IllegalArgumentException("Regla 1: El userId debe ser mayor a cero.");
        }
        this.userId = userId;
    }

    public String getTitle() { return title; }
    
    // Regla 2 y 4: No vacío, máx 50 caracteres y trim() automático
    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Regla 2: El título no puede estar vacío.");
        }
        String textoLimpio = title.trim(); // Regla 4
        if (textoLimpio.length() > 50) {
            throw new IllegalArgumentException("Regla 2: El título supera el límite de 50 caracteres.");
        }
        this.title = textoLimpio;
    }

    public String getBody() { return body; }
    
    // Regla 3 y 4: Mínimo 15 caracteres y trim() automático
    public void setBody(String body) {
        if (body == null) {
            throw new IllegalArgumentException("Regla 3: El cuerpo no puede ser nulo.");
        }
        String textoLimpio = body.trim(); // Regla 4
        if (textoLimpio.length() < 15) {
            throw new IllegalArgumentException("Regla 3: El contenido debe tener al menos 15 caracteres.");
        }
        this.body = textoLimpio;
    }
}