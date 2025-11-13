package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Clase principal de la aplicación Spring Boot.
 * Contiene el modelo (Tarea), el Repositorio, el Servicio y el Controlador.
 * Para ejecutar:
 * 1. Asegúrate de tener las dependencias de Spring Web en tu proyecto.
 * 2. Ejecuta el método main.
 * 3. Los endpoints estarán disponibles en http://localhost:8080/api/tareas
 */
@SpringBootApplication
public class TodoApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodoApplication.class, args);
    }

    // =================================================================
    // 1. MODELO: Tarea (El Recurso)
    // =================================================================

    /**
     * Representa una tarea pendiente (To-Do).
     * Los frameworks modernos de Java (como Lombok) generarían estos métodos
     * automáticamente, pero los incluimos aquí para que sea un solo archivo.
     */
    public static class Tarea {
        private Long id;
        private String descripcion;
        private boolean completada;

        public Tarea(Long id, String descripcion, boolean completada) {
            this.id = id;
            this.descripcion = descripcion;
            this.completada = completada;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getDescripcion() { return descripcion; }
        public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
        public boolean isCompletada() { return completada; }
        public void setCompletada(boolean completada) { this.completada = completada; }

        @Override
        public String toString() {
            return "Tarea{" + "id=" + id + ", descripcion='" + descripcion + '\'' + ", completada=" + completada + '}';
        }
    }

    // =================================================================
    // 2. REPOSITORIO: TareaRepository (Simulación de BD)
    // =================================================================

    /**
     * @Repository: Capa de persistencia (acceso a datos).
     * En una aplicación real, esto interactuaría con JPA/Hibernate para hablar
     * con una base de datos real (MySQL, PostgreSQL, etc.).
     */
    @Repository
    public static class TareaRepository {
        // Simulación de la base de datos: lista estática
        private final List<Tarea> tareas = new ArrayList<>();
        // Generador de IDs automático
        private final AtomicLong nextId = new AtomicLong(1);

        public List<Tarea> findAll() {
            return tareas;
        }

        public Optional<Tarea> findById(Long id) {
            return tareas.stream()
                         .filter(t -> t.getId().equals(id))
                         .findFirst();
        }

        public Tarea save(Tarea tarea) {
            if (tarea.getId() == null) {
                // Crear (POST)
                tarea.setId(nextId.getAndIncrement());
                tareas.add(tarea);
            } else {
                // Actualizar (PUT) - Buscamos y reemplazamos
                findById(tarea.getId()).ifPresent(existing -> {
                    tareas.remove(existing);
                    tareas.add(tarea);
                });
            }
            return tarea;
        }

        public boolean deleteById(Long id) {
            return tareas.removeIf(t -> t.getId().equals(id));
        }
    }

    // =================================================================
    // 3. SERVICIO: TareaService (Lógica de Negocio)
    // =================================================================

    /**
     * @Service: Capa de lógica de negocio.
     * Aquí podríamos añadir validaciones o lógica compleja antes de acceder al Repositorio.
     */
    @Service
    public static class TareaService {
        private final TareaRepository repository;

        // Inyección de Dependencia del Repositorio
        public TareaService(TareaRepository repository) {
            this.repository = repository;
        }

        public List<Tarea> getAllTareas() {
            return repository.findAll();
        }

        public Optional<Tarea> getTareaById(Long id) {
            return repository.findById(id);
        }

        public Tarea createTarea(Tarea tarea) {
            // Lógica de validación: asegurarse de que la descripción no esté vacía
            if (tarea.getDescripcion() == null || tarea.getDescripcion().trim().isEmpty()) {
                throw new IllegalArgumentException("La descripción de la tarea no puede estar vacía.");
            }
            // El ID es generado por el repositorio/BD
            tarea.setId(null);
            return repository.save(tarea);
        }

        public Tarea updateTarea(Long id, Tarea nuevaTarea) {
            return repository.findById(id)
                .map(tareaExistente -> {
                    // PUT: Reemplazo completo de campos
                    tareaExistente.setDescripcion(nuevaTarea.getDescripcion());
                    tareaExistente.setCompletada(nuevaTarea.isCompletada());
                    return repository.save(tareaExistente);
                })
                .orElse(null); // Si no se encuentra la tarea, devolvemos null
        }

        public Optional<Tarea> patchCompletada(Long id, boolean estado) {
             return repository.findById(id)
                .map(tareaExistente -> {
                    // PATCH: Actualización parcial (solo el campo 'completada')
                    tareaExistente.setCompletada(estado);
                    return repository.save(tareaExistente);
                });
        }

        public boolean deleteTarea(Long id) {
            return repository.deleteById(id);
        }
    }

    // =================================================================
    // 4. CONTROLADOR: TareaController (Los Endpoints REST)
    // =================================================================

    /**
     * @RestController: Marca esta clase para manejar peticiones HTTP.
     * @RequestMapping("/api/tareas"): Define la ruta base para todos los métodos.
     */
    @RestController
    @RequestMapping("/api/tareas")
    public static class TareaController {
        private final TareaService service;

        public TareaController(TareaService service) {
            this.service = service;
        }

        // -------------------------------------------------------------------
        // 1. GET (READ) - Obtener todos los recursos
        // Petición: GET http://localhost:8080/api/tareas
        // -------------------------------------------------------------------
        @GetMapping
        public List<Tarea> getAll() {
            return service.getAllTareas();
        }

        // -------------------------------------------------------------------
        // 2. GET (READ) - Obtener recurso por ID
        // Petición: GET http://localhost:8080/api/tareas/1
        // -------------------------------------------------------------------
        @GetMapping("/{id}")
        public ResponseEntity<Tarea> getById(@PathVariable Long id) {
            return service.getTareaById(id)
                .map(ResponseEntity::ok)        // Si existe, devuelve 200 OK con la tarea
                .orElse(ResponseEntity.notFound().build()); // Si no existe, devuelve 404 NOT FOUND
        }

        // -------------------------------------------------------------------
        // 3. POST (CREATE) - Crear un nuevo recurso
        // Petición: POST http://localhost:8080/api/tareas
        // Cuerpo: JSON de la Tarea (sin ID)
        // -------------------------------------------------------------------
        @PostMapping
        public ResponseEntity<Tarea> create(@RequestBody Tarea tarea) {
            try {
                Tarea createdTarea = service.createTarea(tarea);
                // 201 Created es el código ideal para una creación exitosa
                return new ResponseEntity<>(createdTarea, HttpStatus.CREATED);
            } catch (IllegalArgumentException e) {
                // En caso de error de validación (ej. descripción vacía)
                return ResponseEntity.badRequest().build(); // 400 Bad Request
            }
        }

        // -------------------------------------------------------------------
        // 4. PUT (FULL UPDATE) - Reemplazar/Actualizar recurso completo
        // Petición: PUT http://localhost:8080/api/tareas/1
        // Cuerpo: JSON de la Tarea (con ID)
        // -------------------------------------------------------------------
        @PutMapping("/{id}")
        public ResponseEntity<Tarea> update(@PathVariable Long id, @RequestBody Tarea tareaDetails) {
            Tarea updatedTarea = service.updateTarea(id, tareaDetails);

            if (updatedTarea != null) {
                // 200 OK es el código estándar para una actualización exitosa
                return ResponseEntity.ok(updatedTarea);
            } else {
                // Si el ID no existe, devolvemos 404 NOT FOUND
                return ResponseEntity.notFound().build();
            }
        }

        // -------------------------------------------------------------------
        // 5. PATCH (PARTIAL UPDATE) - Actualización parcial (solo el estado)
        // Petición: PATCH http://localhost:8080/api/tareas/1/completada?estado=true
        // -------------------------------------------------------------------
        @PatchMapping("/{id}/completada")
        public ResponseEntity<Tarea> patchCompletada(@PathVariable Long id, @RequestParam boolean estado) {
            return service.patchCompletada(id, estado)
                .map(ResponseEntity::ok)        // Si existe, devuelve 200 OK con la tarea actualizada
                .orElse(ResponseEntity.notFound().build()); // Si no existe, devuelve 404 NOT FOUND
        }


        // -------------------------------------------------------------------
        // 6. DELETE (DELETE) - Eliminar un recurso
        // Petición: DELETE http://localhost:8080/api/tareas/1
        // -------------------------------------------------------------------
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> delete(@PathVariable Long id) {
            boolean wasDeleted = service.deleteTarea(id);

            if (wasDeleted) {
                // 204 No Content es el código estándar para un borrado exitoso sin cuerpo
                return ResponseEntity.noContent().build();
            } else {
                // Si no se pudo borrar (porque el ID no existía)
                return ResponseEntity.notFound().build();
            }
        }
    }
}