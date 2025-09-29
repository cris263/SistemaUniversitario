-- Script de inicialización para Oracle
-- Sistema Universitario - Base de datos
-- SIN TRIGGERS - Usando NEXTVAL directamente

-- Crear secuencias para IDs
CREATE SEQUENCE seq_persona START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_facultad START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_programa START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE seq_curso START WITH 1 INCREMENT BY 1;

-- Crear tablas
CREATE TABLE persona (
    id NUMBER PRIMARY KEY,
    nombres VARCHAR2(100) NOT NULL,
    apellidos VARCHAR2(100) NOT NULL,
    email VARCHAR2(100) NOT NULL UNIQUE
);

CREATE TABLE facultad (
    id NUMBER PRIMARY KEY,
    nombre VARCHAR2(100) NOT NULL,
    decano NUMBER,
    CONSTRAINT fk_facultad_decano FOREIGN KEY (decano) REFERENCES persona(id)
);

CREATE TABLE programa (
    id NUMBER PRIMARY KEY,
    nombre VARCHAR2(100) NOT NULL,
    duracion NUMBER,
    registro DATE,
    facultad NUMBER,
    CONSTRAINT fk_programa_facultad FOREIGN KEY (facultad) REFERENCES facultad(id)
);

CREATE TABLE curso (
    id NUMBER PRIMARY KEY,
    nombre VARCHAR2(100) NOT NULL,
    programa NUMBER,
    activo NUMBER(1) DEFAULT 1 CHECK (activo IN (0,1)),
    CONSTRAINT fk_curso_programa FOREIGN KEY (programa) REFERENCES programa(id)
);

CREATE TABLE profesor (
    id NUMBER PRIMARY KEY,
    tipo_contrato VARCHAR2(50),
    CONSTRAINT fk_profesor_persona FOREIGN KEY (id) REFERENCES persona(id)
);

CREATE TABLE estudiante (
    id NUMBER PRIMARY KEY,
    codigo NUMBER,
    programa NUMBER,
    activo NUMBER(1) DEFAULT 1 CHECK (activo IN (0,1)),
    promedio NUMBER,
    CONSTRAINT fk_estudiante_persona FOREIGN KEY (id) REFERENCES persona(id),
    CONSTRAINT fk_estudiante_programa FOREIGN KEY (programa) REFERENCES programa(id)
);

CREATE TABLE curso_profesor (
    profesor NUMBER,
    curso NUMBER,
    anio NUMBER,
    semestre NUMBER,
    CONSTRAINT pk_curso_profesor PRIMARY KEY (profesor, curso, anio, semestre),
    CONSTRAINT fk_cp_profesor FOREIGN KEY (profesor) REFERENCES profesor(id),
    CONSTRAINT fk_cp_curso FOREIGN KEY (curso) REFERENCES curso(id)
);

CREATE TABLE inscripcion (
    curso NUMBER,
    estudiante NUMBER,
    anio NUMBER,
    semestre NUMBER,
    CONSTRAINT pk_inscripcion PRIMARY KEY (curso, estudiante, anio, semestre),
    CONSTRAINT fk_inscripcion_curso FOREIGN KEY (curso) REFERENCES curso(id),
    CONSTRAINT fk_inscripcion_estudiante FOREIGN KEY (estudiante) REFERENCES estudiante(id)
);

-- Insertar datos usando NEXTVAL explícitamente

-- 1. Personas (USANDO NEXTVAL)
INSERT INTO persona (id, nombres, apellidos, email) VALUES (seq_persona.NEXTVAL, 'Carlos Alberto', 'González Pérez', 'carlos.gonzalez@universidad.edu');
INSERT INTO persona (id, nombres, apellidos, email) VALUES (seq_persona.NEXTVAL, 'María Elena', 'Rodríguez López', 'maria.rodriguez@universidad.edu');
INSERT INTO persona (id, nombres, apellidos, email) VALUES (seq_persona.NEXTVAL, 'José Luis', 'Martínez Silva', 'jose.martinez@universidad.edu');
INSERT INTO persona (id, nombres, apellidos, email) VALUES (seq_persona.NEXTVAL, 'Ana Patricia', 'Fernández Torres', 'ana.fernandez@universidad.edu');
INSERT INTO persona (id, nombres, apellidos, email) VALUES (seq_persona.NEXTVAL, 'Roberto', 'Jiménez Castro', 'roberto.jimenez@universidad.edu');
INSERT INTO persona (id, nombres, apellidos, email) VALUES (seq_persona.NEXTVAL, 'Laura Cristina', 'Vargas Herrera', 'laura.vargas@universidad.edu');
INSERT INTO persona (id, nombres, apellidos, email) VALUES (seq_persona.NEXTVAL, 'Andrés Felipe', 'Sánchez Ruiz', 'andres.sanchez@estudiante.edu');
INSERT INTO persona (id, nombres, apellidos, email) VALUES (seq_persona.NEXTVAL, 'Camila', 'Torres Medina', 'camila.torres@estudiante.edu');
INSERT INTO persona (id, nombres, apellidos, email) VALUES (seq_persona.NEXTVAL, 'Daniel', 'Ospina Ríos', 'daniel.ospina@estudiante.edu');
INSERT INTO persona (id, nombres, apellidos, email) VALUES (seq_persona.NEXTVAL, 'Valentina', 'Castro Mejía', 'valentina.castro@estudiante.edu');

-- 2. Facultades (USANDO NEXTVAL)
INSERT INTO facultad (id, nombre, decano) VALUES (seq_facultad.NEXTVAL, 'Facultad de Ingeniería', 1);
INSERT INTO facultad (id, nombre, decano) VALUES (seq_facultad.NEXTVAL, 'Facultad de Ciencias Económicas', 2);
INSERT INTO facultad (id, nombre, decano) VALUES (seq_facultad.NEXTVAL, 'Facultad de Medicina', 3);
INSERT INTO facultad (id, nombre, decano) VALUES (seq_facultad.NEXTVAL, 'Facultad de Derecho', 4);

-- 3. Programas (USANDO NEXTVAL)
INSERT INTO programa (id, nombre, duracion, registro, facultad) VALUES (seq_programa.NEXTVAL, 'Ingeniería de Sistemas', 5.0, DATE '2020-01-15', 1);
INSERT INTO programa (id, nombre, duracion, registro, facultad) VALUES (seq_programa.NEXTVAL, 'Ingeniería Civil', 5.0, DATE '2019-08-20', 1);
INSERT INTO programa (id, nombre, duracion, registro, facultad) VALUES (seq_programa.NEXTVAL, 'Administración de Empresas', 4.0, DATE '2021-02-10', 2);
INSERT INTO programa (id, nombre, duracion, registro, facultad) VALUES (seq_programa.NEXTVAL, 'Medicina', 6.0, DATE '2018-03-12', 3);

-- 4. Cursos (USANDO NEXTVAL)
INSERT INTO curso (id, nombre, programa, activo) VALUES (seq_curso.NEXTVAL, 'Programación I', 1, 1);
INSERT INTO curso (id, nombre, programa, activo) VALUES (seq_curso.NEXTVAL, 'Base de Datos', 1, 1);
INSERT INTO curso (id, nombre, programa, activo) VALUES (seq_curso.NEXTVAL, 'Estructuras de Datos', 1, 1);
INSERT INTO curso (id, nombre, programa, activo) VALUES (seq_curso.NEXTVAL, 'Cálculo I', 2, 1);
INSERT INTO curso (id, nombre, programa, activo) VALUES (seq_curso.NEXTVAL, 'Contabilidad General', 3, 1);
INSERT INTO curso (id, nombre, programa, activo) VALUES (seq_curso.NEXTVAL, 'Anatomía I', 4, 1);

-- 5. Profesores (usando IDs existentes de persona)
INSERT INTO profesor (id, tipo_contrato) VALUES (1, 'TIEMPO_COMPLETO');
INSERT INTO profesor (id, tipo_contrato) VALUES (2, 'TIEMPO_COMPLETO');
INSERT INTO profesor (id, tipo_contrato) VALUES (3, 'CATEDRA');
INSERT INTO profesor (id, tipo_contrato) VALUES (4, 'TIEMPO_COMPLETO');
INSERT INTO profesor (id, tipo_contrato) VALUES (5, 'CATEDRA');
INSERT INTO profesor (id, tipo_contrato) VALUES (6, 'TIEMPO_COMPLETO');

-- 6. Estudiantes (usando IDs existentes de persona)
INSERT INTO estudiante (id, codigo, programa, activo, promedio) VALUES (7, 20231001, 1, 1, 4.2);
INSERT INTO estudiante (id, codigo, programa, activo, promedio) VALUES (8, 20231002, 1, 1, 3.8);
INSERT INTO estudiante (id, codigo, programa, activo, promedio) VALUES (9, 20231003, 2, 1, 4.0);
INSERT INTO estudiante (id, codigo, programa, activo, promedio) VALUES (10, 20231004, 3, 1, 3.9);

-- 7. Curso-Profesor
INSERT INTO curso_profesor (profesor, curso, anio, semestre) VALUES (1, 1, 2024, 1);
INSERT INTO curso_profesor (profesor, curso, anio, semestre) VALUES (1, 2, 2024, 1);
INSERT INTO curso_profesor (profesor, curso, anio, semestre) VALUES (2, 3, 2024, 1);
INSERT INTO curso_profesor (profesor, curso, anio, semestre) VALUES (3, 4, 2024, 1);
INSERT INTO curso_profesor (profesor, curso, anio, semestre) VALUES (5, 5, 2024, 1);
INSERT INTO curso_profesor (profesor, curso, anio, semestre) VALUES (6, 6, 2024, 1);

-- 8. Inscripciones
INSERT INTO inscripcion (curso, estudiante, anio, semestre) VALUES (1, 7, 2024, 1);
INSERT INTO inscripcion (curso, estudiante, anio, semestre) VALUES (2, 7, 2024, 1);
INSERT INTO inscripcion (curso, estudiante, anio, semestre) VALUES (3, 8, 2024, 1);
INSERT INTO inscripcion (curso, estudiante, anio, semestre) VALUES (1, 8, 2024, 1);
INSERT INTO inscripcion (curso, estudiante, anio, semestre) VALUES (4, 9, 2024, 1);
INSERT INTO inscripcion (curso, estudiante, anio, semestre) VALUES (5, 10, 2024, 1);

COMMIT;