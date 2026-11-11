import java.util.Scanner;

public class Biblioteca {
    private static final int MAX_LIBROS = 100;
    private static final int CAMPOS = 5;

    private static final int CODIGO = 0;
    private static final int NOMBRE = 1;
    private static final int AUTOR = 2;
    private static final int MATERIA = 3;
    private static final int PAGINAS = 4;

    private static final String[] NOMBRES_CAMPOS = {
        "Código", "Nombre", "Autor", "Materia", "No. Páginas"
    };

    private static final String[] NOMBRES_METODOS_ORDENAMIENTO = {
        "Burbuja", "Selección", "Inserción"
    };

    private static final String[][] libros = new String[MAX_LIBROS][CAMPOS];
    private static int totalLibros = 0;
    private static final Scanner entrada = new Scanner(System.in);

    public static void main(String[] args) {
        int opcion;

        do {
            mostrarMenu();
            opcion = leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1:
                    registrarLibro();
                    break;
                case 2:
                    actualizarLibro();
                    break;
                case 3:
                    eliminarLibro();
                    break;
                case 4:
                    buscarLibro();
                    break;
                case 5:
                    ordenarLibros();
                    break;
                case 6:
                    listarLibros();
                    break;
                case 0:
                    System.out.println("Programa finalizado.");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    private static void mostrarMenu() {
        System.out.println("\n===== BIBLIOTECA COMPENSAR =====");
        System.out.println("1. Registrar nuevo libro");
        System.out.println("2. Actualizar libro");
        System.out.println("3. Eliminar libro");
        System.out.println("4. Buscar libro");
        System.out.println("5. Ordenar libros");
        System.out.println("6. Listar libros");
        System.out.println("0. Salir");
    }

    private static void registrarLibro() {
        if (totalLibros >= MAX_LIBROS) {
            System.out.println("No hay espacio disponible para registrar más libros.");
            return;
        }

        System.out.println("\n--- Registrar nuevo libro ---");
        String codigo = leerTextoObligatorio("Código: ");

        if (buscarIndicePorCodigo(codigo) != -1) {
            System.out.println("Ya existe un libro con ese código.");
            return;
        }

        String nombre = leerTextoObligatorio("Nombre: ");
        String autor = leerTextoObligatorio("Autor: ");
        String materia = leerTextoObligatorio("Materia: ");
        int paginas = leerEnteroPositivo("No. Páginas: ");

        libros[totalLibros] = new String[] {
            codigo, nombre, autor, materia, String.valueOf(paginas)
        };
        totalLibros++;

        System.out.println("Libro registrado correctamente.");
    }

    private static void actualizarLibro() {
        if (totalLibros == 0) {
            System.out.println("No hay libros registrados.");
            return;
        }

        System.out.println("\n--- Actualizar libro ---");
        String codigo = leerTextoObligatorio("Código del libro a actualizar: ");
        int indice = buscarIndicePorCodigo(codigo);

        if (indice == -1) {
            System.out.println("No se encontró un libro con ese código.");
            return;
        }

        mostrarLibro(libros[indice], indice + 1);
        System.out.println("Deje vacío un campo para conservar su valor actual.");

        String nuevoCodigo = leerTextoOpcional("Nuevo código (" + libros[indice][CODIGO] + "): ");
        String nuevoNombre = leerTextoOpcional("Nuevo nombre (" + libros[indice][NOMBRE] + "): ");
        String nuevoAutor = leerTextoOpcional("Nuevo autor (" + libros[indice][AUTOR] + "): ");
        String nuevaMateria = leerTextoOpcional("Nueva materia (" + libros[indice][MATERIA] + "): ");
        Integer nuevasPaginas = leerEnteroPositivoOpcional("Nuevo No. Páginas (" + libros[indice][PAGINAS] + "): ");

        if (!nuevoCodigo.isEmpty()) {
            int indiceDuplicado = buscarIndicePorCodigo(nuevoCodigo);
            if (indiceDuplicado != -1 && indiceDuplicado != indice) {
                System.out.println("Ya existe otro libro con ese código.");
                return;
            }
            libros[indice][CODIGO] = nuevoCodigo;
        }

        if (!nuevoNombre.isEmpty()) {
            libros[indice][NOMBRE] = nuevoNombre;
        }

        if (!nuevoAutor.isEmpty()) {
            libros[indice][AUTOR] = nuevoAutor;
        }

        if (!nuevaMateria.isEmpty()) {
            libros[indice][MATERIA] = nuevaMateria;
        }

        if (nuevasPaginas != null) {
            libros[indice][PAGINAS] = String.valueOf(nuevasPaginas);
        }

        System.out.println("Libro actualizado correctamente.");
    }

    private static void eliminarLibro() {
        if (totalLibros == 0) {
            System.out.println("No hay libros registrados.");
            return;
        }

        System.out.println("\n--- Eliminar libro ---");
        String codigo = leerTextoObligatorio("Código del libro a eliminar: ");
        int indice = buscarIndicePorCodigo(codigo);

        if (indice == -1) {
            System.out.println("No se encontró un libro con ese código.");
            return;
        }

        mostrarLibro(libros[indice], indice + 1);
        String confirmacion = leerTextoObligatorio("¿Confirma la eliminación? (s/n): ");

        if (!confirmacion.equalsIgnoreCase("s")) {
            System.out.println("Eliminación cancelada.");
            return;
        }

        for (int i = indice; i < totalLibros - 1; i++) {
            libros[i] = libros[i + 1];
        }

        libros[totalLibros - 1] = new String[CAMPOS];
        totalLibros--;

        System.out.println("Libro eliminado correctamente.");
    }

    private static void buscarLibro() {
        if (totalLibros == 0) {
            System.out.println("No hay libros registrados.");
            return;
        }

        System.out.println("\n--- Buscar libro ---");
        int campo = elegirCampo();
        int metodo = elegirMetodoBusqueda();
        String valor = leerTextoObligatorio("Valor exacto a buscar: ");
        int[] encontrados;

        if (metodo == 1) {
            encontrados = buscarSecuencial(campo, valor);
        } else {
            encontrados = buscarBinaria(campo, valor);
        }

        if (encontrados.length == 0) {
            System.out.println("No se encontraron libros con ese criterio.");
            return;
        }

        System.out.println("Se encontraron " + encontrados.length + " libro(s):");
        for (int indice : encontrados) {
            mostrarLibro(libros[indice], indice + 1);
        }
    }

    private static void ordenarLibros() {
        if (totalLibros < 2) {
            System.out.println("Se necesitan al menos dos libros para ordenar.");
            return;
        }

        System.out.println("\n--- Ordenar libros ---");
        int campo = elegirCampo();
        int metodo = elegirMetodoOrdenamiento();
        boolean ascendente = elegirDireccion();

        switch (metodo) {
            case 1:
                ordenarBurbuja(campo, ascendente);
                break;
            case 2:
                ordenarSeleccion(campo, ascendente);
                break;
            case 3:
                ordenarInsercion(campo, ascendente);
                break;
            default:
                System.out.println("Método no válido.");
                return;
        }

        System.out.println("Libros ordenados por " + NOMBRES_CAMPOS[campo]
                + " con el método " + NOMBRES_METODOS_ORDENAMIENTO[metodo - 1]
                + " en orden " + (ascendente ? "ascendente." : "descendente."));
        listarLibros();
    }

    private static void listarLibros() {
        if (totalLibros == 0) {
            System.out.println("No hay libros registrados.");
            return;
        }

        System.out.println("\n--- Listado de libros ---");
        System.out.printf("%-5s %-12s %-30s %-25s %-20s %-10s%n",
                "No.", "Código", "Nombre", "Autor", "Materia", "Páginas");

        for (int i = 0; i < totalLibros; i++) {
            System.out.printf("%-5d %-12s %-30s %-25s %-20s %-10s%n",
                    i + 1,
                    libros[i][CODIGO],
                    libros[i][NOMBRE],
                    libros[i][AUTOR],
                    libros[i][MATERIA],
                    libros[i][PAGINAS]);
        }
    }

    private static int[] buscarSecuencial(int campo, String valor) {
        int[] encontrados = new int[totalLibros];
        int cantidad = 0;

        for (int i = 0; i < totalLibros; i++) {
            if (compararValores(libros[i][campo], valor, campo) == 0) {
                encontrados[cantidad] = i;
                cantidad++;
            }
        }

        int[] resultado = new int[cantidad];
        System.arraycopy(encontrados, 0, resultado, 0, cantidad);
        return resultado;
    }

    private static int[] buscarBinaria(int campo, String valor) {
        int[] indices = new int[totalLibros];

        for (int i = 0; i < totalLibros; i++) {
            indices[i] = i;
        }

        ordenarIndices(indices, campo);

        int izquierda = 0;
        int derecha = totalLibros - 1;

        while (izquierda <= derecha) {
            int medio = (izquierda + derecha) / 2;
            int comparacion = compararValores(libros[indices[medio]][campo], valor, campo);

            if (comparacion < 0) {
                izquierda = medio + 1;
            } else if (comparacion > 0) {
                derecha = medio - 1;
            } else {
                int inicio = medio;
                int fin = medio;

                while (inicio > 0
                        && compararValores(libros[indices[inicio - 1]][campo], valor, campo) == 0) {
                    inicio--;
                }

                while (fin < totalLibros - 1
                        && compararValores(libros[indices[fin + 1]][campo], valor, campo) == 0) {
                    fin++;
                }

                int[] resultado = new int[fin - inicio + 1];
                for (int i = inicio; i <= fin; i++) {
                    resultado[i - inicio] = indices[i];
                }
                return resultado;
            }
        }

        return new int[0];
    }

    private static void ordenarIndices(int[] indices, int campo) {
        for (int i = 1; i < indices.length; i++) {
            int actual = indices[i];
            int j = i - 1;

            while (j >= 0 && compararFilas(libros[indices[j]], libros[actual], campo) > 0) {
                indices[j + 1] = indices[j];
                j--;
            }

            indices[j + 1] = actual;
        }
    }

    private static void ordenarBurbuja(int campo, boolean ascendente) {
        for (int i = 0; i < totalLibros - 1; i++) {
            for (int j = 0; j < totalLibros - i - 1; j++) {
                int comparacion = compararFilas(libros[j], libros[j + 1], campo);

                if (ascendente ? comparacion > 0 : comparacion < 0) {
                    intercambiar(j, j + 1);
                }
            }
        }
    }

    private static void ordenarSeleccion(int campo, boolean ascendente) {
        for (int i = 0; i < totalLibros - 1; i++) {
            int extremo = i;

            for (int j = i + 1; j < totalLibros; j++) {
                int comparacion = compararFilas(libros[j], libros[extremo], campo);

                if (ascendente ? comparacion < 0 : comparacion > 0) {
                    extremo = j;
                }
            }

            if (extremo != i) {
                intercambiar(i, extremo);
            }
        }
    }

    private static void ordenarInsercion(int campo, boolean ascendente) {
        for (int i = 1; i < totalLibros; i++) {
            String[] actual = libros[i];
            int j = i - 1;

            while (j >= 0) {
                int comparacion = compararFilas(libros[j], actual, campo);

                if (ascendente ? comparacion <= 0 : comparacion >= 0) {
                    break;
                }

                libros[j + 1] = libros[j];
                j--;
            }

            libros[j + 1] = actual;
        }
    }

    private static void intercambiar(int i, int j) {
        String[] temporal = libros[i];
        libros[i] = libros[j];
        libros[j] = temporal;
    }

    private static int compararFilas(String[] filaA, String[] filaB, int campo) {
        return compararValores(filaA[campo], filaB[campo], campo);
    }

    private static int compararValores(String valorA, String valorB, int campo) {
        if (campo == PAGINAS) {
            try {
                int paginasA = Integer.parseInt(valorA);
                int paginasB = Integer.parseInt(valorB);
                return Integer.compare(paginasA, paginasB);
            } catch (NumberFormatException e) {
                return valorA.compareToIgnoreCase(valorB);
            }
        }

        return valorA.compareToIgnoreCase(valorB);
    }

    private static int buscarIndicePorCodigo(String codigo) {
        for (int i = 0; i < totalLibros; i++) {
            if (libros[i][CODIGO].equalsIgnoreCase(codigo)) {
                return i;
            }
        }
        return -1;
    }

    private static int elegirCampo() {
        System.out.println("Campos disponibles:");

        for (int i = 0; i < NOMBRES_CAMPOS.length; i++) {
            System.out.println((i + 1) + ". " + NOMBRES_CAMPOS[i]);
        }

        return leerEnteroEnRango("Seleccione un campo: ", 1, NOMBRES_CAMPOS.length) - 1;
    }

    private static int elegirMetodoOrdenamiento() {
        System.out.println("Métodos de ordenamiento:");

        for (int i = 0; i < NOMBRES_METODOS_ORDENAMIENTO.length; i++) {
            System.out.println((i + 1) + ". " + NOMBRES_METODOS_ORDENAMIENTO[i]);
        }

        return leerEnteroEnRango("Seleccione un método: ", 1, NOMBRES_METODOS_ORDENAMIENTO.length);
    }

    private static int elegirMetodoBusqueda() {
        System.out.println("Métodos de búsqueda:");
        System.out.println("1. Secuencial");
        System.out.println("2. Binaria");
        return leerEnteroEnRango("Seleccione un método: ", 1, 2);
    }

    private static boolean elegirDireccion() {
        int opcion = leerEnteroEnRango("Dirección (1. Ascendente / 2. Descendente): ", 1, 2);
        return opcion == 1;
    }

    private static void mostrarLibro(String[] libro, int numero) {
        System.out.println("\nLibro No. " + numero);
        System.out.println("Código: " + libro[CODIGO]);
        System.out.println("Nombre: " + libro[NOMBRE]);
        System.out.println("Autor: " + libro[AUTOR]);
        System.out.println("Materia: " + libro[MATERIA]);
        System.out.println("No. Páginas: " + libro[PAGINAS]);
    }

    private static String leerTextoObligatorio(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String texto = entrada.nextLine().trim();

            if (!texto.isEmpty()) {
                return texto;
            }

            System.out.println("Este campo no puede quedar vacío.");
        }
    }

    private static String leerTextoOpcional(String mensaje) {
        System.out.print(mensaje);
        return entrada.nextLine().trim();
    }

    private static int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String texto = entrada.nextLine().trim();

            try {
                return Integer.parseInt(texto);
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un número entero válido.");
            }
        }
    }

    private static int leerEnteroEnRango(String mensaje, int minimo, int maximo) {
        while (true) {
            int valor = leerEntero(mensaje);

            if (valor >= minimo && valor <= maximo) {
                return valor;
            }

            System.out.println("Ingrese un valor entre " + minimo + " y " + maximo + ".");
        }
    }

    private static int leerEnteroPositivo(String mensaje) {
        while (true) {
            int valor = leerEntero(mensaje);

            if (valor > 0) {
                return valor;
            }

            System.out.println("El número de páginas debe ser mayor que cero.");
        }
    }

    private static Integer leerEnteroPositivoOpcional(String mensaje) {
        while (true) {
            String texto = leerTextoOpcional(mensaje);

            if (texto.isEmpty()) {
                return null;
            }

            try {
                int valor = Integer.parseInt(texto);

                if (valor > 0) {
                    return valor;
                }
            } catch (NumberFormatException e) {
            }

            System.out.println("Ingrese un número entero positivo o deje el campo vacío.");
        }
    }
}
