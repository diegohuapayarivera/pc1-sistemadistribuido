import streamlit as st
import pandas as pd
import api_client

# Configuración de página
st.set_page_config(
    page_title="Neptuno API Client",
    page_icon="🌊",
    layout="wide",
    initial_sidebar_state="expanded"
)

# Estilo personalizado mínimo para pulir pequeños detalles
st.markdown("""
    <style>
    .stAlert { margin-top: 1rem; }
    </style>
""", unsafe_allow_html=True)

# ─── SIDEBAR (Menú y Estado) ──────────────────────────────────────────────────
st.sidebar.title("🌊 Neptuno API")
st.sidebar.markdown("Cliente para conectar con Lab5 (Spring Boot).")

# Verificar si la API está accesible
api_online, status_msg = api_client.ping()

if api_online:
    st.sidebar.success(status_msg)
else:
    st.sidebar.error(status_msg)
    if "500" in status_msg:
        st.error(f"El servidor respondió: {status_msg}. Esto suele significar un error en la base de datos del VPS.")
    else:
        st.error(f"No se puede conectar al servidor en http://34.134.146.159:8080. {status_msg}")
    st.stop()

menu = st.sidebar.radio(
    "Navegación",
    ["📋 Listar Productos", "🔍 Buscar por ID", "➕ Crear Producto", "🛒 Registrar Pedido"]
)

# ─── FUNCIONES AUXILIARES ─────────────────────────────────────────────────────
def format_moneda(valor):
    return f"S/. {valor:.2f}" if pd.notnull(valor) else "—"


# ─── PANTALLAS ────────────────────────────────────────────────────────────────

if menu == "📋 Listar Productos":
    st.header("📋 Todos los Productos")
    
    col1, col2 = st.columns([8, 1])
    if col2.button("↻ Refrescar"):
        st.rerun()

    with st.spinner("Cargando productos..."):
        try:
            productos = api_client.listar_productos()
            if productos:
                df = pd.DataFrame(productos)
                
                # Reordenar y renombrar columnas para mejor visualización
                df = df[['idProducto', 'nombreProducto', 'idProveedor', 'idCategoria', 'cantidadPorUnidad', 'precioUnidad', 'unidadesEnExistencia', 'unidadesEnPedido', 'nivelNuevoPedido', 'suspendido']]
                
                # Mostrar booleanos de manera amigable
                df['suspendido'] = df['suspendido'].apply(lambda x: "🟢 No" if x == "0" else "🔴 Sí")
                
                # Formatear precio
                df_visual = df.copy()
                df_visual['precioUnidad'] = df_visual['precioUnidad'].apply(lambda x: f"S/. {x:.2f}")

                st.dataframe(
                    df_visual,
                    use_container_width=True,
                    hide_index=True 
                )
                st.caption(f"Total: {len(productos)} productos.")
            else:
                st.info("No hay productos registrados en la base de datos.")
        except Exception as e:
            st.error(f"Error al cargar los productos: {e}")


elif menu == "🔍 Buscar por ID":
    st.header("🔍 Buscar Producto por ID")
    
    col1, col2 = st.columns([1, 2])
    with col1:
        search_id = st.text_input("Ingresa el ID del Producto", placeholder="Ej: 1")
        buscar_btn = st.button("Buscar Producto", type="primary")

    if buscar_btn:
        if not search_id.isdigit():
            st.warning("⚠️ El ID debe ser un número entero.")
        else:
            with st.spinner(f"Buscando el producto {search_id}..."):
                try:
                    producto = api_client.obtener_producto(int(search_id))
                    
                    st.success(f"✅ Producto #{producto['idProducto']} encontrado")
                    
                    # Mostrar detalles en columnas tipo tarjeta
                    c1, c2 = st.columns(2)
                    with c1:
                        st.metric("Nombre", producto.get("nombreProducto", "—"))
                        st.metric("Precio Unitario", format_moneda(producto.get("precioUnidad")))
                        st.metric("Categoría (ID)", producto.get("idCategoria", "—"))
                        st.metric("Proveedor (ID)", producto.get("idProveedor", "—"))
                        
                    with c2:
                        st.metric("Existencias", producto.get("unidadesEnExistencia", 0))
                        st.metric("En Pedido", producto.get("unidadesEnPedido", 0))
                        st.metric("Presentación", producto.get("cantidadPorUnidad", "—"))
                        st.metric("Estado", "🔴 Suspendido" if producto.get("suspendido") == "1" else "🟢 Activo")

                except Exception as e:
                    st.error(f"Ocurrió un error o el producto no existe: {e}")


elif menu == "➕ Crear Producto":
    st.header("➕ Crear Nuevo Producto")
    
    with st.form("form_crear_producto", clear_on_submit=False):
        c1, c2 = st.columns(2)
        
        with c1:
            nombre = st.text_input("Nombre del Producto *")
            id_prov = st.number_input("ID Proveedor *", min_value=1, value=1)
            id_cat = st.number_input("ID Categoría *", min_value=1, value=1)
            cantidad_un = st.text_input("Cantidad por Unidad", placeholder="Ej: 10 cajas x 20 bolsas")
            precio = st.number_input("Precio Unidad *", min_value=0.0, value=0.00, step=0.50)
            
        with c2:
            existencias = st.number_input("Unidades en Existencia", min_value=0, value=0)
            en_pedido = st.number_input("Unidades en Pedido", min_value=0, value=0)
            nivel_repo = st.number_input("Nivel Nuevo Pedido", min_value=0, value=0)
            suspendido = st.selectbox("Suspendido", options=["0", "1"], format_func=lambda x: "No (0)" if x == "0" else "Sí (1)")

        st.markdown("*(Los campos marcados con * son requeridos a nivel lógico)*")
        submitted = st.form_submit_button("✓ Guardar Producto", type="primary")

        if submitted:
            if not nombre:
                st.error("El nombre del producto es obligatorio.")
            else:
                payload = {
                    "nombreProducto": nombre,
                    "idProveedor": id_prov,
                    "idCategoria": id_cat,
                    "cantidadPorUnidad": cantidad_un,
                    "precioUnidad": precio,
                    "unidadesEnExistencia": existencias,
                    "unidadesEnPedido": en_pedido,
                    "nivelNuevoPedido": nivel_repo,
                    "suspendido": suspendido
                }
                
                with st.spinner("Creando producto..."):
                    try:
                        res = api_client.crear_producto(payload)
                        st.success(f"✅ ¡Producto creado con éxito!\n\nID Asignado: **{res['idProducto']}**")
                    except Exception as e:
                        st.error(f"Error al crear el producto: {e}")


elif menu == "🛒 Registrar Pedido":
    st.header("🛒 Registrar Pedido con Detalles")
    
    # Manejar estado en memoria (Session State) para la tabla de detalles
    if "detalles_pedido" not in st.session_state:
        st.session_state.detalles_pedido = []

    # Seccion Cabecera del Pedido
    st.subheader("1. Datos del Pedido (Cabecera)")
    c1, c2 = st.columns(2)
    
    with c1:
        id_cliente = st.text_input("ID Cliente *", value="ALFKI", max_chars=5)
        id_empleado = st.number_input("ID Empleado *", min_value=1, value=1)
        fecha_pedido = st.text_input("Fecha Pedido", value="21/04/2026 00:00")
        fecha_entrega = st.text_input("Fecha Entrega", value="28/04/2026 00:00")
        fecha_envio = st.text_input("Fecha Envío", value="22/04/2026 00:00")
        forma_envio = st.number_input("Forma Envío (1-3) *", min_value=1, max_value=3, value=1)
        
    with c2:
        cargo = st.number_input("Cargo de Envío (S/.)", min_value=0.0, value=0.0)
        destinatario = st.text_input("Destinatario")
        direccion = st.text_input("Dirección de destino")
        ciudad = st.text_input("Ciudad de destino")
        region = st.text_input("Región de destino")
        cod_postal = st.text_input("Código Postal")
        pais = st.text_input("País de destino", value="Perú")

    st.divider()

    # Sección Agregar Detalles
    st.subheader("2. Añadir Líneas de Producto")
    with st.form("form_add_detalle", clear_on_submit=True):
        d1, d2, d3, d4 = st.columns(4)
        with d1:
            det_id_prod = st.number_input("ID Producto *", min_value=1, key="det_id_prod")
        with d2:
            det_precio = st.number_input("Precio Unit. *", min_value=0.01, step=0.5, key="det_precio")
        with d3:
            det_cant = st.number_input("Cantidad *", min_value=1, value=1, key="det_cant")
        with d4:
            det_desc = st.number_input("Descuento", min_value=0, value=0, key="det_desc")
            
        add_btn = st.form_submit_button("➕ Añadir a la lista")
        if add_btn:
            st.session_state.detalles_pedido.append({
                "idProducto": det_id_prod,
                "precioUnidad": det_precio,
                "cantidad": det_cant,
                "descuento": det_desc
            })

    # Mostrar tabla de detalles agregados
    if st.session_state.detalles_pedido:
        st.write("### Líneas agregadas:")
        det_df = pd.DataFrame(st.session_state.detalles_pedido)
        st.dataframe(det_df, use_container_width=True, hide_index=True)
        
        # Opcion de quitar el ultimo
        if st.button("✕ Eliminar última línea"):
            st.session_state.detalles_pedido.pop()
            st.rerun()

    st.divider()

    # Boton de registrar todo
    st.subheader("3. Confirmar")
    if st.button("✓ ENVIAR PEDIDO", type="primary", use_container_width=True):
        if not id_cliente:
            st.error("⚠️ El ID de cliente es obligatorio.")
        elif not st.session_state.detalles_pedido:
            st.error("⚠️ Debes agregar al menos una línea de producto al pedido.")
        else:
            payload = {
                "idCliente": id_cliente,
                "idEmpleado": id_empleado,
                "fechaPedido": fecha_pedido,
                "fechaEntrega": fecha_entrega,
                "fechaEnvio": fecha_envio,
                "formaEnvio": forma_envio,
                "cargo": cargo,
                "destinatario": destinatario,
                "direccionDestinatario": direccion,
                "ciudadDestinatario": ciudad,
                "regionDestinatario": region,
                "codPostalDestinatario": cod_postal,
                "paisDestinatario": pais,
                "detalles": st.session_state.detalles_pedido
            }
            
            with st.spinner("Registrando pedido en la base de datos..."):
                try:
                    res = api_client.registrar_pedido(payload)
                    st.success(f"✅ {res.get('mensaje', 'Pedido creado con éxito.')}\n\n**ID de Pedido Generado: {res.get('idPedido')}**")
                    # Limpiar sesion de detalles
                    st.session_state.detalles_pedido = []
                except Exception as e:
                    st.error(f"Error al registrar el pedido: {e}")
