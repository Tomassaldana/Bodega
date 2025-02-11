import View.BodegaGUI;
import java.util.Scanner;
import javax.swing.SwingUtilities;

public class SimulacionBodega {
    public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> new BodegaGUI());
    }
}
        /*try {
            FirebaseService firebaseService = new FirebaseService(""src/main/resources/firebasebodega.json"");
            
            while (true) {
                System.out.println("\n=== SISTEMA DE BODEGA ===");
                System.out.println("1. Agregar nuevo ítem");
                System.out.println("2. Actualizar cantidad");
                System.out.println("3. Eliminar ítem");
                System.out.println("4. Mostrar inventario");
                System.out.println("5. Salir");
                System.out.print("Seleccione opción: ");

                int opcion = scanner.nextInt();
                scanner.nextLine(); // Limpiar buffer

                switch (opcion) {
                    case 1:
                        agregarItem(scanner, bodega, firebaseService);
                        break;
                        
                    case 2:
                        actualizarItem(scanner, bodega, firebaseService);
                        break;
                        
                    case 3:
                        eliminarItem(scanner, bodega, firebaseService);
                        break;
                        
                    case 4:
                        bodega.mostrarInventario();
                        break;
                        
                    case 5:
                        System.out.println("Saliendo del sistema...");
                        scanner.close();
                        System.exit(0);
                        
                    default:
                        System.out.println("Opción no válida");
                }
            }
        } catch (IOException e) {
            System.out.println("Error de conexión: " + e.getMessage());
        }
    }

    private static void agregarItem(Scanner scanner, Bodega bodega, FirebaseService firebase) {
        System.out.println("\nCategorías disponibles:");
        for (Bodega.Categoria cat : Bodega.Categoria.values()) {
            System.out.println("- " + cat);
        }
        
        System.out.print("Seleccione categoría: ");
        Bodega.Categoria categoria = Bodega.Categoria.valueOf(scanner.nextLine().toUpperCase());
        
        System.out.print("Nombre del ítem: ");
        String nombre = scanner.nextLine();
        
        System.out.print("Cantidad: ");
        int cantidad = scanner.nextInt();
        scanner.nextLine();  // Limpiar buffer
        
        bodega.agregarItem(categoria, nombre, cantidad);
        firebase.guardarItem(categoria, new Item(nombre, cantidad));
        System.out.println("Ítem agregado correctamente!");
    }

    private static void actualizarItem(Scanner scanner, Bodega bodega, FirebaseService firebase) {
        System.out.print("\nCategoría del ítem: ");
        Bodega.Categoria categoria = Bodega.Categoria.valueOf(scanner.nextLine().toUpperCase());
        
        System.out.print("Nombre del ítem a actualizar: ");
        String nombre = scanner.nextLine();
        
        System.out.print("Nueva cantidad: ");
        int nuevaCantidad = scanner.nextInt();
        scanner.nextLine();  // Limpiar buffer
        
        bodega.actualizarItem(categoria, nombre, nuevaCantidad);
        firebase.guardarItem(categoria, new Item(nombre, nuevaCantidad));
        System.out.println("Ítem actualizado!");
    }

    private static void eliminarItem(Scanner scanner, Bodega bodega, FirebaseService firebase) {
        System.out.print("\nCategoría del ítem: ");
        Bodega.Categoria categoria = Bodega.Categoria.valueOf(scanner.nextLine().toUpperCase());
        
        System.out.print("Nombre del ítem a eliminar: ");
        String nombre = scanner.nextLine();
        
        bodega.eliminarItem(categoria, nombre);
        firebase.eliminarItem(categoria, nombre);
        System.out.println("Ítem eliminado!");
    }
}
*/
