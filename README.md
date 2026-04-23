# Sistema de Gestión de Ventas - Proyecto Neptuno (Lab 5)

Este proyecto es una aplicación distribuida que consiste en un backend robusto desarrollado en Java y un cliente interactivo en Python. El sistema permite gestionar productos y procesar pedidos utilizando la base de datos relacional "NEPTUNO".

## 🚀 Propósito del Proyecto
El objetivo principal es demostrar la integración de múltiples tecnologías (Java, Spring Boot, Python, MySQL) en un entorno distribuido. El sistema maneja operaciones CRUD de productos y la lógica de negocio para la creación de pedidos, asegurando la integridad de los datos y la sincronización con un esquema de base de datos específico que incluye identificadores con caracteres especiales (tildes y eñes).

---

## 🛠️ Tecnologías Usadas

### Backend (Java)
- **Lenguaje:** Java 17
- **Framework:** Spring Boot 3.x
- **Acceso a Datos:** Spring JDBC (JdbcTemplate)
- **Base de Datos:** MySQL 8.x
- **Gestión de Dependencias:** Maven
- **Librerías Adicionales:** 
  - Lombok (para reducir código repetitivo)
  - Spring Web (para la API REST)

### Frontend / Cliente (Python)
- **Lenguaje:** Python 3.8+
- **Interfaz de Usuario:** Streamlit (Framework para aplicaciones de datos)
- **Comunicación HTTP:** Requests
- **Procesamiento de Datos:** Pandas

---

## 📋 Requerimientos del Sistema

### Generales
- Una instancia de **MySQL** ejecutándose con la base de datos `neptuno` cargada (ver archivo `NEPTUNO.sql`).
- Acceso a internet o red local para la comunicación entre el cliente y el servidor.

### Backend
- **JDK 17** instalado.
- Configuración de base de datos en `src/main/resources/application-mysql.properties`.

### Cliente Python
- Python instalado.
- Dependencias instaladas: `pip install -r requirements.txt`.

---

## ⚙️ Configuración y Ejecución

### 1. Preparación de la Base de Datos
Importar el archivo `NEPTUNO.sql` en su servidor MySQL:
```sql
SOURCE path/to/NEPTUNO.sql;
```

### 2. Ejecución del Backend (Java)
Desde la raíz del proyecto, compilar y ejecutar:
```bash
./mvnw clean package -DskipTests
java -jar target/lab5-0.0.1-SNAPSHOT.jar
```

### 3. Ejecución del Cliente (Python)
Navegar a la carpeta del cliente e iniciar Streamlit:
```bash
cd cliente_python
pip install -r requirements.txt
streamlit run app.py
```

---

## 🔍 Detalles de Implementación (Consideraciones Especiales)

### Manejo de Caracteres Especiales
Una característica crítica de este proyecto es la sincronización con el esquema de base de datos "NEPTUNO", el cual utiliza identificadores con tildes (ej. `IdCategoría`, `FechaEnvío`, `RegiónDestinatario`). 
### Sincronización de Identificadores (Tildes y Ñ)
El proyecto maneja una integración estricta con el esquema `NEPTUNO`, respetando los nombres de columnas originales:

| Tabla | Columnas con Acentos / Caracteres Especiales |
| :--- | :--- |
| `productos` | `IdCategoría` |
| `pedidos` | `FechaEnvío`, `FormaEnvío`, `DirecciónDestinatario`, `RegiónDestinatario`, `CódPostalDestinatario`, `PaísDestinatario` |
| `envios` | `IdCompañiaEnvios` |

### Arquitectura de la API
El servidor expone los siguientes endpoints principales:
- `GET /api/productos`: Lista todos los productos disponibles.
- `GET /api/productos/{id}`: Obtiene detalles de un producto específico.
- `POST /api/productos`: Registra un nuevo producto.
- `POST /api/pedidos`: Procesa un pedido completo con múltiples detalles y actualización automática de stock.

### Cliente Inteligente
El cliente Python incluye una lógica de verificación de estado (Ping) que permite distinguir entre:
- **Online:** El servidor responde correctamente.
- **Error 500:** El servidor está vivo pero hay un problema con la base de datos.
- **Offline:** No hay conexión con el servidor.

---

## 🛡️ Principios ACID y Transaccionalidad

El sistema garantiza la integridad de los datos mediante el cumplimiento de las propiedades ACID, especialmente en el proceso de **Registro de Pedidos**:

1.  **Atomicidad (Atomicity):** Gracias a la anotación `@Transactional` en `PedidoService`, el registro de la cabecera del pedido y todos sus detalles se trata como una única unidad. Si falla la validación de stock de un solo producto, se revierte toda la operación (Rollback).
2.  **Consistencia (Consistency):** Se aplican reglas de negocio estrictas antes de cualquier inserción. El sistema asegura que los clientes y productos existan, y que el stock nunca sea negativo, manteniendo la base de datos en un estado válido.
3.  **Aislamiento (Isolation):** Se implementa un mecanismo de **bloqueo selectivo** (`FOR UPDATE`) al consultar el stock. Esto evita que transacciones concurrentes intenten vender el mismo inventario simultáneamente.
4.  **Durabilidad (Durability):** Una vez confirmada la transacción, los cambios se persisten de forma permanente en el almacenamiento físico de la base de datos MySQL en el VPS.

---

## 👨‍💻 Autor
Desarrollado para el Laboratorio 5 de Sistemas Distribuidos (UTP).
