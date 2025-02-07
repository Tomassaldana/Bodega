package View;

import ModelView.Bodega;
import ModelView.Item;
import Model.FirebaseService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

public class BodegaGUI {
    private JFrame frame;
    private Bodega bodega;
    private FirebaseService firebaseService;
    private JTabbedPane tabbedPane;

    public BodegaGUI() {
        try {
            bodega = new Bodega();
            firebaseService = new FirebaseService("src/main/resources/firebasebodega.json");
            initializeGUI();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error conectando a Firebase: " + e.getMessage());
        }
    }

    private void initializeGUI() {
        frame = new JFrame("Sistema de Bodega");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        
        // Panel de bienvenida
        JPanel welcomePanel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("¡Bienvenido al Sistema de Gestión de Bodega!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomePanel.add(welcomeLabel, BorderLayout.CENTER);
        
        // Panel de operaciones
        JPanel operationsPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        operationsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JButton addButton = createStyledButton("Agregar Ítem");
        JButton updateButton = createStyledButton("Actualizar Cantidad");
        JButton deleteButton = createStyledButton("Eliminar Ítem");
        JButton showButton = createStyledButton("Mostrar Inventario");
        
        addButton.addActionListener(e -> showCategorySelection("Agregar"));
        updateButton.addActionListener(e -> showCategorySelection("Actualizar"));
        deleteButton.addActionListener(e -> showCategorySelection("Eliminar"));
        showButton.addActionListener(e -> mostrarInventario());
        
        operationsPanel.add(addButton);
        operationsPanel.add(updateButton);
        operationsPanel.add(deleteButton);
        operationsPanel.add(showButton);
        
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Inicio", welcomePanel);
        tabbedPane.addTab("Operaciones", operationsPanel);
        
        frame.add(tabbedPane);
        frame.setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 18));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        return button;
    }

    private void showCategorySelection(String operation) {
        JDialog dialog = new JDialog(frame, "Seleccionar Categoría", true);
        dialog.setLayout(new GridLayout(Bodega.Categoria.values().length + 1, 1));
        
        for (Bodega.Categoria categoria : Bodega.Categoria.values()) {
            JButton btn = new JButton(categoria.name());
            btn.addActionListener(e -> {
                dialog.dispose();
                handleCategorySelection(operation, categoria);
            });
            dialog.add(btn);
        }
        
        dialog.setSize(300, 400);
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    private void handleCategorySelection(String operation, Bodega.Categoria categoria) {
        switch (operation) {
            case "Agregar":
                showAddItemDialog(categoria);
                break;
            case "Eliminar":
                showDeleteItemDialog(categoria);
                break;
            case "Actualizar":
                showUpdateItemDialog(categoria);
                break;
        }
    }

    private void showAddItemDialog(Bodega.Categoria categoria) {
        JDialog dialog = new JDialog(frame, "Agregar Ítem", true);
        dialog.setLayout(new GridLayout(3, 2));
        
        JTextField nombreField = new JTextField();
        JSpinner cantidadSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));
        
        dialog.add(new JLabel("Nombre del ítem:"));
        dialog.add(nombreField);
        dialog.add(new JLabel("Cantidad:"));
        dialog.add(cantidadSpinner);
        
        JButton confirmar = new JButton("Agregar");
        confirmar.addActionListener(e -> {
            String nombre = nombreField.getText();
            int cantidad = (Integer) cantidadSpinner.getValue();
            
            bodega.agregarItem(categoria, nombre, cantidad);
            firebaseService.guardarItem(categoria, new Item(nombre, cantidad));
            JOptionPane.showMessageDialog(dialog, "Ítem agregado con éxito!");
            dialog.dispose();
        });
        
        dialog.add(confirmar);
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    private void showDeleteItemDialog(Bodega.Categoria categoria) {
        JDialog dialog = new JDialog(frame, "Eliminar Ítem", true);
        dialog.setLayout(new BorderLayout());
        
        JPanel itemsPanel = new JPanel(new GridLayout(0, 1));
        JScrollPane scrollPane = new JScrollPane(itemsPanel);
        
        HashMap<String, Item> items = bodega.getItems(categoria);
        if (items.isEmpty()) {
            itemsPanel.add(new JLabel("No hay ítems en esta categoría"));
        } else {
            items.keySet().forEach(item -> {
                JButton btn = new JButton(item);
                btn.addActionListener(e -> {
                    bodega.eliminarItem(categoria, item);
                    firebaseService.eliminarItem(categoria, item);
                    dialog.dispose();
                    JOptionPane.showMessageDialog(dialog, "Ítem eliminado!");
                });
                itemsPanel.add(btn);
            });
        }
        
        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.setSize(300, 400);
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    private void showUpdateItemDialog(Bodega.Categoria categoria) {
        JDialog selectionDialog = new JDialog(frame, "Seleccionar Ítem", true);
        selectionDialog.setLayout(new BorderLayout());
        
        JPanel itemsPanel = new JPanel(new GridLayout(0, 1));
        JScrollPane scrollPane = new JScrollPane(itemsPanel);
        
        HashMap<String, Item> items = bodega.getItems(categoria);
        if (items.isEmpty()) {
            itemsPanel.add(new JLabel("No hay ítems en esta categoría"));
        } else {
            items.keySet().forEach(item -> {
                JButton btn = new JButton(item);
                btn.addActionListener(e -> {
                    selectionDialog.dispose();
                    showUpdateQuantityDialog(categoria, item);
                });
                itemsPanel.add(btn);
            });
        }
        
        selectionDialog.add(scrollPane, BorderLayout.CENTER);
        selectionDialog.setSize(300, 400);
        selectionDialog.setLocationRelativeTo(frame);
        selectionDialog.setVisible(true);
    }

    private void showUpdateQuantityDialog(Bodega.Categoria categoria, String item) {
        JDialog dialog = new JDialog(frame, "Actualizar Cantidad", true);
        dialog.setLayout(new GridLayout(2, 2));
        
        JSpinner cantidadSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));
        
        dialog.add(new JLabel("Nueva cantidad:"));
        dialog.add(cantidadSpinner);
        
        JButton confirmar = new JButton("Actualizar");
        confirmar.addActionListener(e -> {
            int nuevaCantidad = (Integer) cantidadSpinner.getValue();
            bodega.actualizarItem(categoria, item, nuevaCantidad);
            firebaseService.guardarItem(categoria, new Item(item, nuevaCantidad));
            JOptionPane.showMessageDialog(dialog, "Cantidad actualizada!");
            dialog.dispose();
        });
        
        dialog.add(confirmar);
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    private void mostrarInventario() {
        JDialog dialog = new JDialog(frame, "Inventario Completo", true);
        dialog.setLayout(new BorderLayout());
        
        JTabbedPane categoriasPane = new JTabbedPane();
        
        for (Bodega.Categoria categoria : Bodega.Categoria.values()) {
            JPanel categoriaPanel = new JPanel(new GridLayout(0, 1));
            JScrollPane scrollPane = new JScrollPane(categoriaPanel);
            
            HashMap<String, Item> items = bodega.getItems(categoria);
            if (items.isEmpty()) {
                categoriaPanel.add(new JLabel("No hay ítems en esta categoría"));
            } else {
                items.values().forEach(item -> {
                    JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    itemPanel.add(new JLabel(item.getNombre() + ": "));
                    itemPanel.add(new JLabel(String.valueOf(item.getCantidad()) + " unidades"));
                    categoriaPanel.add(itemPanel);
                });
            }
            
            categoriasPane.addTab(categoria.name(), scrollPane);
        }
        
        dialog.add(categoriasPane, BorderLayout.CENTER);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BodegaGUI());
    }
}
