package com.example.dispositivosmoviles;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import android.widget.Spinner;


public class Logeado extends AppCompatActivity {
    public static final String TAG = "YOUR-TAG-NAME";
    private FirebaseAuth mAuth;
    private Configuration config = new Configuration();
    private ArrayList<String> Users;


    private ArrayList<String> Correos;


    private String Tareas;

    private String Usuario;

    private String Correo;

    private String NombreUsuario;
    private ArrayAdapter<String> itemsAdapter;

    private ArrayAdapter<String> correosAdapter;
    private ListView lvUsers;
    TextView tvInfoUser;
    EditText sendText;
    Spinner sendID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logeado);
        sendText = findViewById(R.id.etNewItem2);
        sendID = findViewById(R.id.UserID);
        sendID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?>arg0, View view, int arg2, long arg3) {
               Usuario = sendID.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        tvInfoUser = findViewById(R.id.tv_info_user);
        lvUsers = (ListView) findViewById(R.id.users);
        Correos = new ArrayList<String>();
        correosAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, Correos);
        sendID.setAdapter(correosAdapter);
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
            Correo = user.getEmail();
            getUserName();
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
      FirebaseFirestore.getInstance().collection("Users").whereEqualTo("correo",Usuario)
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
                                Correos.add(document.getString("correo"));
                                itemsAdapter.notifyDataSetChanged();
                                correosAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }//fin getUsers


    public void getUserName(){
        FirebaseFirestore.getInstance().collection("Users").whereEqualTo("correo",Correo)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                NombreUsuario = document.getString("nombre");
                                tvInfoUser.setText(NombreUsuario);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }//Obtener datos del usuario actual


}//fin activity