package Model;

import ModelView.Item;
import ModelView.Bodega;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.io.FileInputStream;
import java.io.IOException;

public class FirebaseService {
    private DatabaseReference dbRef;

    public FirebaseService(String serviceAccountPath) throws IOException {
        FileInputStream serviceAccount = new FileInputStream("src/main/resources/firebasebodega.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://bodega-d035b-default-rtdb.firebaseio.com")
                .build();

        FirebaseApp.initializeApp(options);
        dbRef = FirebaseDatabase.getInstance().getReference("inventario");
    }

    public void guardarItem(Bodega.Categoria categoria, Item item) {
        dbRef.child(categoria.name())
             .child(item.getNombre())
             .setValueAsync(item.getCantidad());
    }

    public void eliminarItem(Bodega.Categoria categoria, String nombreItem) {
        dbRef.child(categoria.name())
             .child(nombreItem)
             .removeValueAsync();
    }
}
