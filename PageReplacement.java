import java.io.*;
import java.util.*;

public class PageReplacement {

    // Leer secuencia de páginas desde un archivo CSV
    public static List<Integer> readSequenceFromCSV(String filePath) throws IOException {
        List<Integer> pages = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists() || !file.isFile()) {
            throw new FileNotFoundException("Error: El archivo no fue encontrado en la ruta: '" + filePath + "'\n"
                    + "Por favor, asegúrate de cargar la ruta correcta en la variable 'filePath'.\n"
                    + "Ejemplo: filePath = \"C:/ruta/al/archivo.csv\" o \"src/resources/pages.csv\".\n\n"
                    + "Formato esperado del archivo CSV:\n"
                    + "1,3,2,1,5,3,4,1,5,2,6,7,5,7,2,5,3,5,3,1");
        }

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine();
            if (line == null || line.isEmpty()) {
                throw new IOException("Error: El archivo está vacío.\n\n"
                        + "Formato esperado:\n"
                        + "Ejemplo: 1,3,2,1,5,3,4,1,5,2,6,7,5,7,2,5,3,5,3,1");
            }
            String[] tokens = line.split(",");
            for (String token : tokens) {
                try {
                    pages.add(Integer.parseInt(token.trim()));
                } catch (NumberFormatException e) {
                    throw new IOException("Error: Formato incorrecto en el archivo CSV.\n"
                            + "Asegúrate de que el archivo contenga solo números separados por comas.\n"
                            + "Ejemplo válido: 1,3,2,1,5,3,4,1,5,2,6,7,5,7,2,5,3,5,3,1");
                }
            }
        }
        return pages;
    }

    // Algoritmo FIFO
    public static int fifoAlgorithm(List<Integer> pages, int frameSize) {
        Queue<Integer> frame = new LinkedList<>();
        int pageFaults = 0;

        for (int page : pages) {
            if (!frame.contains(page)) {
                if (frame.size() == frameSize) {
                    frame.poll(); // Elimina la página más antigua
                }
                frame.add(page);
                pageFaults++;
                System.out.println("Fallo de página (FIFO). Marco: " + frame);
            }
        }
        return pageFaults;
    }

    // Algoritmo LRU
    public static int lruAlgorithm(List<Integer> pages, int frameSize) {
        List<Integer> frame = new ArrayList<>();
        int pageFaults = 0;

        for (int page : pages) {
            if (!frame.contains(page)) {
                if (frame.size() == frameSize) {
                    frame.remove(0); // Elimina la página menos recientemente usada
                }
                pageFaults++;
            } else {
                frame.remove((Integer) page); // Actualiza el orden de uso
            }
            frame.add(page);
            System.out.println("Estado del marco (LRU): " + frame);
        }
        return pageFaults;
    }

    public static void main(String[] args) {
        try {
            // Variable que contiene la ruta del archivo CSV
            String filePath = "datos/paginas.csv"; // Ajustar la ruta del archivo aquí
            List<Integer> pages = readSequenceFromCSV(filePath);

            // Tamaño del marco de páginas
            int frameSize = 4;

            System.out.println("Secuencia de páginas: " + pages);

            // FIFO
            System.out.println("\n--- FIFO ---");
            int fifoFaults = fifoAlgorithm(pages, frameSize);
            System.out.println("Fallos de página (FIFO): " + fifoFaults);

            // LRU
            System.out.println("\n--- LRU ---");
            int lruFaults = lruAlgorithm(pages, frameSize);
            System.out.println("Fallos de página (LRU): " + lruFaults);

        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            System.err.println("Error de lectura: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
        }
    }
}
