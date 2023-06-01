package com.example.dispositivosmoviles;



import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import org.apache.commons.io.FileUtils;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class LogeadoUser extends AppCompatActivity {
    public static final String TAG = "YOUR-TAG-NAME";
    private FirebaseAuth mAuth;
    private String Correo;
    private Configuration config = new Configuration();
    public ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;
    TextView tvInfoUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logeado_user);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        tvInfoUser = findViewById(R.id.tv_info_user);
        lvItems = (ListView) findViewById(R.id.lvItems);
        initviews(user);
        // Lista de Tareas
        items = new ArrayList<String>();
        itemsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        receiveItems();
        setupListViewListener();
    }



    //Borra Tarea si se deja Presionado
    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter,
                                                   View item, int pos, long id) {
                        // Remove the item within array at position
                        items.remove(pos);
                        updatedatabase(items);
                        // Refresh the adapter
                        itemsAdapter.notifyDataSetChanged();
                        // Return true consumes the long click event (marks it handled)
                        return true;
                    }

                });
    }


 //Revisa Si existe el usuario y muestra el correo
    private void initviews(FirebaseUser user) {
        if (user!= null) {
            tvInfoUser.setText(user.getEmail());
            Correo = user.getEmail();
        }
        else{
            tvInfoUser.setText("--");
        }
    }


// cerrar cesion de usuario
    public void clickCerrarSesion2(View view) {
        mAuth.signOut();
        Intent i=new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        Toast.makeText(getApplicationContext(), "Sesion Cerrada Vuelve Pronto!", Toast.LENGTH_SHORT).show();
        finish();

    }


    private void receiveItems(){
        FirebaseFirestore.getInstance().collection("Users").whereEqualTo("correo",Correo)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                               items.addAll((Collection<? extends String>) document.get("actividades"));
                                itemsAdapter.notifyDataSetChanged();
                                Log.d(TAG, "Tareas" + items);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }//fin de items recibidos


    private void updatedatabase(ArrayList<String> tarea){
        FirebaseFirestore.getInstance().collection("Users").whereEqualTo("correo",Correo)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                document.getReference().update("actividades", tarea);
                                Log.d(TAG, "Error updating: " + tarea);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }








}//Fin Activity