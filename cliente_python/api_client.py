import requests

BASE_URL = "http://34.134.146.159:8080/api"

# ── Timeout global para todas las peticiones ─────────────────────────────────
TIMEOUT = 10


# ─────────────────────────────── PRODUCTOS ───────────────────────────────────

def listar_productos() -> list[dict]:
    """GET /api/productos → lista todos los productos."""
    r = requests.get(f"{BASE_URL}/productos", timeout=TIMEOUT)
    r.raise_for_status()
    return r.json()


def obtener_producto(id_producto: int) -> dict:
    """GET /api/productos/{id} → un producto por ID."""
    r = requests.get(f"{BASE_URL}/productos/{id_producto}", timeout=TIMEOUT)
    r.raise_for_status()
    return r.json()


def crear_producto(data: dict) -> dict:
    """POST /api/productos → crea un producto nuevo."""
    r = requests.post(f"{BASE_URL}/productos", json=data, timeout=TIMEOUT)
    r.raise_for_status()
    return r.json()


# ─────────────────────────────── PEDIDOS ─────────────────────────────────────

def registrar_pedido(data: dict) -> dict:
    """POST /api/pedidos → registra un pedido completo con detalles."""
    r = requests.post(f"{BASE_URL}/pedidos", json=data, timeout=TIMEOUT)
    r.raise_for_status()
    return r.json()


# ─────────────────────────────── HEALTH ──────────────────────────────────────

def ping() -> tuple[bool, str]:
    """Verifica si la API está disponible y devuelve su estado."""
    try:
        r = requests.get(f"{BASE_URL}/productos", timeout=3)
        if r.status_code == 200:
            return True, "🟢 API Online"
        elif r.status_code == 500:
            return False, "🟠 Servidor Online pero con Error Interno (500)"
        else:
            return False, f"🔴 Error del Servidor: {r.status_code}"
    except requests.exceptions.ConnectionError:
        return False, "🔴 No se pudo conectar al servidor (URL incorrecta o Down)"
    except requests.exceptions.Timeout:
        return False, "⌛ Tiempo de espera agotado"
    except Exception as e:
        return False, f"❌ Error: {str(e)}"
