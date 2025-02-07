package ModelView;

import ModelView.Item;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class Bodega {
    public enum Categoria {
        ELECTRODOMESTICO, COMIDA, ROPA, LIBROS, BEBIDAS
    }

    private EnumMap<Categoria, Map<String, Item>> inventario;

    public Bodega() {
        inventario = new EnumMap<>(Categoria.class);
        for (Categoria categoria : Categoria.values()) {
            inventario.put(categoria, new HashMap<>());
        }
    }

    public void agregarItem(Categoria categoria, String nombre, int cantidad) {
        Map<String, Item> items = inventario.get(categoria);
        if (items.containsKey(nombre)) {
            Item existente = items.get(nombre);
            existente.setCantidad(existente.getCantidad() + cantidad);
        } else {
            items.put(nombre, new Item(nombre, cantidad));
        }
    }

    public void actualizarItem(Categoria categoria, String nombre, int nuevaCantidad) {
        Map<String, Item> items = inventario.get(categoria);
        if (items.containsKey(nombre)) {
            items.get(nombre).setCantidad(nuevaCantidad);
        }
    }

    public void eliminarItem(Categoria categoria, String nombre) {
        inventario.get(categoria).remove(nombre);
    }

    public void mostrarInventario() {
        System.out.println("\n=== INVENTARIO COMPLETO ===");
        for (Categoria categoria : Categoria.values()) {
            System.out.println("\nCategoría: " + categoria);
            Map<String, Item> items = inventario.get(categoria);
            if (items.isEmpty()) {
                System.out.println("  - Vacío");
            } else {
                items.values().forEach(item -> 
                    System.out.printf("  - %s: %d unidades%n", 
                    item.getNombre(), item.getCantidad()));
            }
        }
    }
    public HashMap<String, Item> getItems(Categoria categoria) {
        return (HashMap<String, Item>) inventario.get(categoria);
    }
}
