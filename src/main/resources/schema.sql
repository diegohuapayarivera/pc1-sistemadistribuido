-- ============================================================
-- SCHEMA para H2 en modo MySQL (base de datos neptuno)
-- SOLO se ejecuta con el perfil "h2" (local).
--
-- Los nombres de columna son EXACTAMENTE los mismos que usa
-- el script del profesor en MySQL, para que el codigo Java
-- sea 100% compatible sin ninguna modificacion al cambiar
-- a MySQL (perfil "mysql" o "profesor").
-- ============================================================

CREATE TABLE IF NOT EXISTS `categorias` (
    `IdCategoria`    INT NOT NULL AUTO_INCREMENT,
    `NombreCategoria` TEXT,
    `Descripcion`    TEXT,
    PRIMARY KEY (`IdCategoria`)
);

CREATE TABLE IF NOT EXISTS `clientes` (
    `IdCliente`        VARCHAR(10) NOT NULL,
    `NombreCompañia`   TEXT,
    `NombreContacto`   TEXT,
    `CargoContacto`    TEXT,
    `Direccion`        TEXT,
    `Ciudad`           TEXT,
    `Region`           TEXT,
    `CodPostal`        TEXT,
    `Pais`             TEXT,
    `Telefono`         TEXT,
    `Fax`              TEXT,
    PRIMARY KEY (`IdCliente`)
);

CREATE TABLE IF NOT EXISTS `empleados` (
    `IdEmpleado`        INT,
    `Apellidos`         TEXT,
    `Nombre`            TEXT,
    `Cargo`             TEXT,
    `FechaNacimiento`   TEXT,
    `FechaContratacion` TEXT,
    `Ciudad`            TEXT,
    `Region`            TEXT,
    `Pais`              TEXT
);

CREATE TABLE IF NOT EXISTS `envios` (
    `IdCompañiaEnvios` INT,
    `NombreCompañia`   TEXT,
    `Telefono`         TEXT
);

CREATE TABLE IF NOT EXISTS `productos` (
    `IdProducto`           INT NOT NULL AUTO_INCREMENT,
    `NombreProducto`       TEXT,
    `IdProveedor`          INT,
    `IdCategoria`          INT,
    `CantidadPorUnidad`    TEXT,
    `PrecioUnidad`         DOUBLE,
    `UnidadesEnExistencia` INT,
    `UnidadesEnPedido`     INT,
    `NivelNuevoPedido`     INT,
    `Suspendido`           VARCHAR(5),
    PRIMARY KEY (`IdProducto`)
);

CREATE TABLE IF NOT EXISTS `pedidos` (
    `IdPedido`               INT NOT NULL AUTO_INCREMENT,
    `IdCliente`              VARCHAR(10),
    `IdEmpleado`             INT,
    `FechaPedido`            TEXT,
    `FechaEntrega`           TEXT,
    `FechaEnvío`             TEXT,
    `FormaEnvío`             INT,
    `Cargo`                  DOUBLE,
    `Destinatario`           TEXT,
    `DirecciónDestinatario`  TEXT,
    `CiudadDestinatario`     TEXT,
    `RegiónDestinatario`     TEXT,
    `CódPostalDestinatario`  TEXT,
    `PaísDestinatario`       TEXT,
    PRIMARY KEY (`IdPedido`)
);

CREATE TABLE IF NOT EXISTS `detalle` (
    `IdPedido`    INT NOT NULL,
    `IdProducto`  INT NOT NULL,
    `PrecioUnidad` DOUBLE,
    `Cantidad`    INT,
    `Descuento`   INT
);
