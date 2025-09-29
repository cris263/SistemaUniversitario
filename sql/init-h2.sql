-- Script de inicialización para H2
-- Sistema Universitario - Base de datos
-- Basado en el esquema de DatabaseConnection.java

-- Crear tablas (siguiendo el esquema exacto de DatabaseConnection.java)

-- Tabla persona (base para estudiantes y profesores)
CREATE TABLE IF NOT EXISTS persona (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL
);

-- Facultad
CREATE TABLE IF NOT EXISTS facultad (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    decano BIGINT,
    FOREIGN KEY (decano) REFERENCES persona(id)
);

-- Programa
CREATE TABLE IF NOT EXISTS programa (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    duracion DOUBLE,
    registro DATE,
    facultad BIGINT,
    FOREIGN KEY (facultad) REFERENCES facultad(id)
);

-- Curso
CREATE TABLE IF NOT EXISTS curso (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    programa BIGINT,
    activo BOOLEAN,
    FOREIGN KEY (programa) REFERENCES programa(id)
);

-- Profesor (subclase de Persona)
CREATE TABLE IF NOT EXISTS profesor (
    id BIGINT PRIMARY KEY,
    tipo_contrato VARCHAR(50),
    FOREIGN KEY (id) REFERENCES persona(id)
);

-- Estudiante (subclase de Persona)
CREATE TABLE IF NOT EXISTS estudiante (
    id BIGINT PRIMARY KEY,
    codigo BIGINT,
    programa BIGINT,
    activo BOOLEAN,
    promedio DOUBLE,
    FOREIGN KEY (id) REFERENCES persona(id),
    FOREIGN KEY (programa) REFERENCES programa(id)
);

-- CursoProfesor (tabla de relación)
CREATE TABLE IF NOT EXISTS curso_profesor (
    profesor BIGINT,
    curso BIGINT,
    anio INT,
    semestre INT,
    PRIMARY KEY (profesor, curso, anio, semestre),
    FOREIGN KEY (profesor) REFERENCES profesor(id),
    FOREIGN KEY (curso) REFERENCES curso(id)
);

-- Inscripcion (tabla de relación)
CREATE TABLE IF NOT EXISTS inscripcion (
    curso BIGINT,
    estudiante BIGINT,
    anio INT,
    semestre INT,
    PRIMARY KEY (curso, estudiante, anio, semestre),
    FOREIGN KEY (curso) REFERENCES curso(id),
    FOREIGN KEY (estudiante) REFERENCES estudiante(id)
);

-- Datos de prueba (usando el esquema correcto de DatabaseConnection.java)

-- 1. Insertar Personas (base para profesores y estudiantes)
INSERT INTO persona (nombres, apellidos, email) VALUES 
    ('Carlos Alberto', 'González Pérez', 'carlos.gonzalez@universidad.edu'),
    ('María Elena', 'Rodríguez López', 'maria.rodriguez@universidad.edu'),
    ('José Luis', 'Martínez Silva', 'jose.martinez@universidad.edu'),
    ('Ana Patricia', 'Fernández Torres', 'ana.fernandez@universidad.edu'),
    ('Roberto', 'Jiménez Castro', 'roberto.jimenez@universidad.edu'),
    ('Laura Cristina', 'Vargas Herrera', 'laura.vargas@universidad.edu'),
    ('Andrés Felipe', 'Sánchez Ruiz', 'andres.sanchez@estudiante.edu'),
    ('Camila', 'Torres Medina', 'camila.torres@estudiante.edu'),
    ('Daniel', 'Ospina Ríos', 'daniel.ospina@estudiante.edu'),
    ('Valentina', 'Castro Mejía', 'valentina.castro@estudiante.edu');

-- 2. Insertar Facultades
INSERT INTO facultad (nombre, decano) VALUES 
    ('Facultad de Ingeniería', 1),
    ('Facultad de Ciencias Económicas', 2),
    ('Facultad de Medicina', 3),
    ('Facultad de Derecho', 4);

-- 3. Insertar Programas
INSERT INTO programa (nombre, duracion, registro, facultad) VALUES 
    ('Ingeniería de Sistemas', 5.0, '2020-01-15', 1),
    ('Ingeniería Civil', 5.0, '2019-08-20', 1),
    ('Administración de Empresas', 4.0, '2021-02-10', 2),
    ('Medicina', 6.0, '2018-03-12', 3);

-- 4. Insertar Cursos
INSERT INTO curso (nombre, programa, activo) VALUES 
    ('Programación I', 1, TRUE),
    ('Base de Datos', 1, TRUE),
    ('Estructuras de Datos', 1, TRUE),
    ('Cálculo I', 2, TRUE),
    ('Contabilidad General', 3, TRUE),
    ('Anatomía I', 4, TRUE);

-- 5. Insertar Profesores
INSERT INTO profesor (id, tipo_contrato) VALUES 
    (1, 'TIEMPO_COMPLETO'),
    (2, 'TIEMPO_COMPLETO'),
    (3, 'CATEDRA'),
    (4, 'TIEMPO_COMPLETO'),
    (5, 'CATEDRA'),
    (6, 'TIEMPO_COMPLETO');

-- 6. Insertar Estudiantes
INSERT INTO estudiante (id, codigo, programa, activo, promedio) VALUES 
    (7, 20231001, 1, TRUE, 4.2),
    (8, 20231002, 1, TRUE, 3.8),
    (9, 20231003, 2, TRUE, 4.0),
    (10, 20231004, 3, TRUE, 3.9);

-- 7. Insertar asignaciones Curso-Profesor
INSERT INTO curso_profesor (profesor, curso, anio, semestre) VALUES 
    (1, 1, 2024, 1),
    (1, 2, 2024, 1),
    (2, 3, 2024, 1),
    (3, 4, 2024, 1),
    (5, 5, 2024, 1),
    (6, 6, 2024, 1);

-- 8. Insertar Inscripciones
INSERT INTO inscripcion (curso, estudiante, anio, semestre) VALUES 
    (1, 7, 2024, 1),
    (2, 7, 2024, 1),
    (3, 8, 2024, 1),
    (1, 8, 2024, 1),
    (4, 9, 2024, 1),
    (5, 10, 2024, 1);