CREATE TABLE departamento (
    id INT PRIMARY KEY,
    nombre TEXT NOT NULL,
    telefono VARCHAR(20),
    ubicacion TEXT
);

CREATE TABLE farmacia (
    id SERIAL PRIMARY KEY,
    nombre TEXT NOT NULL,
    estado VARCHAR(15) DEFAULT 'activa',
    ubicacion TEXT,
    telefono VARCHAR(20)
);

CREATE TABLE paciente (
    id SERIAL PRIMARY KEY,
    tipo_documento VARCHAR(10) NOT NULL,
    numero_documento VARCHAR(30) NOT NULL,
    nombres TEXT NOT NULL,
    apellidos TEXT NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    sexo VARCHAR(10),
    grupo_sanguineo VARCHAR(5),
    telefono VARCHAR(20),
    correo TEXT,
    direccion TEXT,
    eps TEXT,
    fecha_registro DATE DEFAULT CURRENT_DATE,
    estado VARCHAR(15) DEFAULT 'activo',
	UNIQUE(tipo_documento, numero_documento)
);

CREATE TABLE empleado (
    id SERIAL PRIMARY KEY,
    tipo_documento VARCHAR(10),
    numero_documento VARCHAR(30),
    nombres TEXT NOT NULL,
    apellidos TEXT NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    telefono VARCHAR(20),
    correo TEXT NOT NULL,
    direccion TEXT,
    estado VARCHAR(15) DEFAULT 'activo',
    id_departamento INT REFERENCES departamento(id),
	UNIQUE(tipo_documento, numero_documento)
);

CREATE TABLE farmaceutico (
    id_empleado INT PRIMARY KEY REFERENCES empleado(id),
    licencia_profesional TEXT NOT NULL,
    id_farmacia INT NOT NULL REFERENCES farmacia(id)
);

CREATE TABLE medico (
    id_empleado INT PRIMARY KEY REFERENCES empleado(id),
    especialidad TEXT,
    registro_medico TEXT
);

CREATE TABLE administrativo (
    id_empleado INT PRIMARY KEY REFERENCES empleado(id),
    area TEXT
);

CREATE TABLE medicamento (
    id SERIAL PRIMARY KEY,
    nombre TEXT NOT NULL,
    concentracion TEXT,
    via_administracion TEXT,
    forma_farmaceutica TEXT
);

CREATE TABLE historia_clinica (
    id SERIAL PRIMARY KEY,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estado VARCHAR(15),
    id_paciente INT NOT NULL REFERENCES paciente(id)
);

CREATE TABLE consulta_medica (
    id SERIAL PRIMARY KEY,
    motivo_consulta TEXT NOT NULL,
    tipo_consulta VARCHAR(20),
    id_medico INT NOT NULL REFERENCES medico(id_empleado),
    id_departamento INT NOT NULL REFERENCES departamento(id)
);

CREATE TABLE cita (
    id SERIAL PRIMARY KEY,
    fecha DATE NOT NULL,
    hora TIME NOT NULL,
    estado VARCHAR(20) DEFAULT 'programada',
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    id_paciente INT NOT NULL REFERENCES paciente(id),
    id_departamento INT NOT NULL REFERENCES departamento(id),
    id_medico INT NOT NULL REFERENCES medico(id_empleado),
    id_consultamedica INT REFERENCES consulta_medica (id)
);

CREATE TABLE evento (
    id SERIAL PRIMARY KEY,
    fecha DATE NOT NULL,
    hora TIME NOT NULL,
    tipo VARCHAR(30),
    descripcion TEXT,
    id_historiaclinica INT REFERENCES historia_clinica(id),
    id_departamento INT REFERENCES departamento(id),
    id_medico INT REFERENCES medico(id_empleado),
    id_consultamedica INT REFERENCES consulta_medica(id)
);

CREATE TABLE receta (
    id SERIAL PRIMARY KEY,
    fecha DATE DEFAULT CURRENT_DATE,
    hora TIME DEFAULT CURRENT_TIME,
    id_consultamedica INT NOT NULL REFERENCES consulta_medica(id)
);


CREATE TABLE detalle_receta (
    id SERIAL PRIMARY KEY,
    id_receta INT NOT NULL REFERENCES receta(id),
    id_medicamento INT NOT NULL REFERENCES medicamento(id),
    dosis TEXT NOT NULL,
    frecuencia TEXT NOT NULL,
    duracion TEXT NOT NULL
);

CREATE TABLE procedimiento (
    id SERIAL PRIMARY KEY,
    nombre TEXT,
    id_consultamedica INT REFERENCES consulta_medica(id)
);

CREATE TABLE tratamiento (
    id SERIAL PRIMARY KEY,
    descripcion TEXT,
    id_consultamedica INT REFERENCES consulta_medica(id)
);

CREATE TABLE equipamiento (
    id SERIAL PRIMARY KEY,
    nombre TEXT,
    estado VARCHAR(15),
    id_departamento INT REFERENCES departamento(id)
);

CREATE TABLE almacena (
    id_farmacia INT REFERENCES farmacia(id),
    id_medicamento INT REFERENCES medicamento(id),
    stock INTEGER DEFAULT 0,
    PRIMARY KEY (id_farmacia, id_medicamento)
);