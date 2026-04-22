# 🌊 Cliente Python — Neptuno API (lab5)

Interfaz visual para consumir la API REST del proyecto lab5 (Spring Boot con base de datos Neptuno).

---

## Requisitos

- Python 3.10 o superior
- `tkinter` (viene incluido con Python en Windows y macOS)
- Librería `requests`

---

## Instalación

```bash
pip install -r requirements.txt
```

---

## Uso

1. **Asegúrate de que la API esté corriendo:**
   ```bash
   # En la carpeta del proyecto lab5:
   ./mvnw spring-boot:run
   ```

2. **Ejecuta el cliente:**
   ```bash
   python main.py
   ```

---

## Funciones disponibles

| Pestaña | Endpoint | Método |
|---|---|---|
| 📋 Listar Productos | `/api/productos` | GET |
| 🔍 Buscar por ID | `/api/productos/{id}` | GET |
| ➕ Crear Producto | `/api/productos` | POST |
| 🛒 Registrar Pedido | `/api/pedidos` | POST |

---

## Configuración de la API

Por defecto el cliente se conecta a `http://localhost:8080`.
Si necesitas cambiar la URL, edita la primera línea de `api_client.py`:

```python
BASE_URL = "http://localhost:8080/api"
```

Para conectarte a la VPS del profesor:
```python
BASE_URL = "http://IP-DEL-SERVIDOR:8080/api"
```
