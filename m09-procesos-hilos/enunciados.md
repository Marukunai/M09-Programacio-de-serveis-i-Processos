# 📝 Enunciados de Actividades de Procesos e Hilos

## 🖥️ Actividad 1: Procesos

Genera 2 procesos, uno que ejecute el `notepad.exe` y otro que haga un `echo` en el `cmd` de "Hello world desde proceso", de manera paralela (no secuencial). Una vez han terminado los dos procesos, imprime por consola "Los dos primeros procesos han terminado" y crea un tercer proceso que vuelva a abrir de nuevo el `notepad.exe` y, una vez termine, imprima por consola "Todos los procesos han terminado".

---

## 🧵 Actividad 2: Hilos

Crea un programa que cree 5 hilos (`Threads`) distintos. Cada uno de ellos hará un bucle de 10 pero con un `sleep` distinto: (1, 2, 3, 4 y 5 segundos). En cada vuelta debe imprimir por consola el nombre del `thread` que es y la iteración por la que va.

Crea clases personalizadas para cada uno de los `Threads`, mínimo una con `Runnable` y otra con `Thread`.