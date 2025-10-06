# Sistema Universitario

## Requisitos
- Java 17 (JDK)
- IntelliJ IDEA (o similar)
- Driver H2 incluido en `lib/` 

## Cómo ejecutar
1. Abrir el proyecto en IntelliJ (carpeta `SistemaUniversitario/`).
2. **File → Project Structure**:
    - Project SDK: **17**
    - Modules → Dependencies: agregar `lib/h2-xxxx.jar` si no está.
    - Marcar `src/` como **Sources Root**.
3. Abrir `src/com/universidad/Main.java` y ejecutar **Run 'Main.main()'**.

## Como se implementa observer
1. CursoManager actúa como observable implementando la interfaz ObservableCurso, notificando a los observadores (CursoConsolaObserver) cuando se crean o eliminan cursos.

2. Curso sigue siendo solo la entidad de datos.
   Esto permite que distintas partes del sistema reaccionen a cambios sin que la entidad de datos conozca a los observadores.