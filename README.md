# 🏥 Sistema de Gestión Hospitalaria Distribuida

Proyecto final de la materia **Bases de Datos Avanzadas**.  
Sistema de base de datos distribuida para la gestión integral de una red hospitalaria,
con una base de datos central conectada a bases de datos departamentales.

---

## 📋 Descripción

Este sistema resuelve el problema de la fragmentación de información clínica en hospitales
de mediana y gran escala. Mediante una arquitectura distribuida, cada departamento opera
con su propia base de datos local mientras que una base central garantiza la consistencia
e integridad de los datos en toda la red hospitalaria.

El backend está desarrollado en **Java con Spring Boot** y la base de datos en
**PostgreSQL (pgAdmin)**.

---

## 👥 Usuarios del Sistema

| Rol | Descripción |
|---|---|
| **Administrativo** | Registra pacientes y empleados, agenda, reprograma y cancela citas |
| **Médico** | Realiza consultas, accede a historias clínicas y emite prescripciones |
| **Farmacéutico** | Gestiona el inventario de medicamentos y dispensa prescripciones |

---

## ⚙️ Funcionalidades Principales

- ✅ Registro y consulta de pacientes desde cualquier departamento autorizado
- ✅ Historia clínica única por paciente
- ✅ Programación, reprogramación y cancelación de citas médicas
- ✅ Registro de consultas, diagnósticos y tratamientos
- ✅ Gestión de prescripciones y dispensación de medicamentos
- ✅ Registro de empleados del hospital
- ✅ Control de acceso por roles

---

## 🗄️ Diagrama Entidad-Relación

![Diagrama ER](./docs/diagrama_er.png)

---

## 🏗️ Arquitectura del Sistema

- **Base de datos central (PostgreSQL):** Consolida la información de todos los
  departamentos y garantiza consistencia global.
- **Bases de datos departamentales:** Cada departamento opera de forma autónoma
  con su propia base local.
- **Backend (Spring Boot):** Expone la lógica de negocio mediante controladores REST,
  con acceso a datos a través de repositorios JPA.

---

## 📁 Estructura del Repositorio
Sistema_Hospitalario/
│
├── sql/
│   ├── tablas.sql                    # Creación de todas las tablas
│   ├── inserts.sql                   # Datos de prueba
│   ├── procedimientosalmacenados.sql # Procedimientos almacenados
│   ├── triggers.sql                  # Triggers del sistema
│   └── views.sql                     # Vistas definidas
│
├── src/
│   └── main/
│       ├── java/com/hospital/
│       │   ├── controller/           # Controladores REST
│       │   ├── model/                # Entidades del dominio
│       │   ├── repository/           # Acceso a datos (JPA)
│       │   └── HospitalApplication.java
│       └── resources/                # Configuración (application.properties)
│
├── pom.xml                           # Dependencias Maven
└── README.md
## 📦 Requisitos Previos

- Java 17+
- Maven
- PostgreSQL 14+ / pgAdmin
- Spring Boot
- Postman(para ejecutar los endpoints)

---

## 🚀 Instalación y Configuración

### 1. Clonar el repositorio
```bash
git clone https://github.com/DavidS127/Sistema_Hospitalario.git
cd Sistema_Hospitalario
```

### 2. Configurar la base de datos en pgAdmin
Abrir pgAdmin, crear una base de datos llamada `sistema_hospitalario` y ejecutar
los scripts en este orden:

```bash
# En la consola de pgAdmin o psql:
\i sql/tablas.sql
\i sql/inserts.sql
\i sql/views.sql
\i sql/procedimientosalmacenados.sql
\i sql/triggers.sql
```

### 3. Configurar la conexión en el proyecto
spring.datasource.url=jdbc:postgresql://localhost:5432/hospital_db
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
spring.jpa.hibernate.ddl-auto=none
server.port=8081

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/hospital_db
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
spring.jpa.hibernate.ddl-auto=none
```

### 4. Ejecutar el proyecto
```bash
./mvnw spring-boot:run
```

---

## 🛠️ Tecnologías Utilizadas

| Capa | Tecnología |
|---|---|
| Backend | Java + Spring Boot |
| Base de datos | PostgreSQL (pgAdmin) |
| ORM | Spring Data JPA |
| Build | Maven |
| Pruebas API | Postman |

---

## 👨‍💻 Autor

**David Sanchez Torres.** 
**Dilan Triana Jiménez.** 
**Luis Carlos Moralez.** 
— Proyecto Final, Bases de Datos Avanzadas


