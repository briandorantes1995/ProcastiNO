package com.example.dispositivosmoviles;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import android.util.Log;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.Locale;


public class Logeado extends AppCompatActivity {
    public static final String TAG = "YOUR-TAG-NAME";
    private FirebaseAuth mAuth;
    private Button button;
    private TextView textview;
    private Locale locale;
    private Configuration config = new Configuration();
    private ArrayList<String> Users;

    private String Tareas;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvUsers;
    TextView tvInfoUser;
    EditText sendText;
    EditText sendID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logeado);
        sendText = findViewById(R.id.etNewItem2);
        sendID = findViewById(R.id.userID);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        tvInfoUser = findViewById(R.id.tv_info_user);
        lvUsers = (ListView) findViewById(R.id.users);
        Users = new ArrayList<String>();
        itemsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, Users);
        lvUsers.setAdapter(itemsAdapter);
        getUsers();
        initviews(user);

    }//fin onCreate


    //Verifica si existe el usuario y muestra el mail
    private void initviews(FirebaseUser user) {

        if (user!= null) {
            tvInfoUser.setText(user.getEmail());
        }
        else{
            tvInfoUser.setText("--");
        }
    }

//destruye la sesion
    public void clickCerrarSesion2(View view) {
        mAuth.signOut();
        Intent i=new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        Toast.makeText(getApplicationContext(), "Sesion Cerrada Vuelve Pronto!", Toast.LENGTH_SHORT).show();
        finish();
    }//fin cerrar sesion


    //Inicia el chat
    public void chat(View view) {
        Intent i = new Intent(getApplicationContext(), Chat.class);
        startActivity(i);
    }//fin chat


    //Inicia la videollmada
    public void Videollamada(View view) {
        Intent i = new Intent(getApplicationContext(), Videollamada.class);
        startActivity(i);
    }//fin videollamada

//asignar tarea a usuario en firebase, para posterior visualizacion
    public void asignartarea(View view) {
      FirebaseFirestore.getInstance().collection("Users").whereEqualTo("correo",sendID.getText().toString())
              .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Tareas = sendText.getText().toString();
                        document.getReference().update("actividades", FieldValue.arrayUnion(Tareas));
                        Log.d(TAG, "Error updating: " + Tareas);
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });//fin oncomplete
    }//fin asignar tarea



    //obtener usuarios
    public void getUsers(){
        FirebaseFirestore.getInstance().collection("Users").whereNotEqualTo("nombre",null)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Users.add("Usuario: "+document.getString("nombre") +" - Correo: "+document.getString("correo"));
                                itemsAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }//fin getUsers

}//fin activity