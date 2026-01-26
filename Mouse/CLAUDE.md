# Mouse Project

Proyecto Java que automatiza movimientos de mouse y teclado usando Robot.

## Reglas del Proyecto

### Dependencias
- **NO instalar librerías sin permiso explícito del usuario**
- Mantener el proyecto lo más "plain Java" posible
- Las únicas dependencias permitidas actualmente son:
  - JUnit 5 (para testing)
  - JaCoCo (para code coverage)

### Testing
- **Todos los cambios de código deben incluir tests**
- **El code coverage de líneas debe ser siempre mayor a 85%**
- Ejecutar `./test.sh` antes de commitear para verificar coverage
- Usar el patrón Executor/Recorder para testear código que depende de Robot

### Estructura
```
Mouse/
├── src/Mouse.java       # Código principal
├── test/MouseTest.java  # Tests unitarios
├── test.sh              # Ejecutar tests con coverage
├── run.sh               # Ejecutar el programa
├── lib/                 # JARs (ignorado en git, se descargan automáticamente)
├── bin/                 # Clases compiladas (ignorado en git)
└── coverage/            # Reportes de coverage (ignorado en git)
```

## Comandos

```bash
./test.sh    # Compila, ejecuta tests, muestra code coverage
./run.sh     # Compila y ejecuta el programa
```

## Convenciones Técnicas

- Java 21+ (compilado con `--release 21` para compatibilidad con JaCoCo)
- JUnit 5 para tests
- Patrón Executor para abstraer Robot y permitir testing sin UI
