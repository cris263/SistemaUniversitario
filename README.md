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

