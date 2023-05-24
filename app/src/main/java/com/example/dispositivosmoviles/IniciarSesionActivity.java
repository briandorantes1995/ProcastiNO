package com.example.dispositivosmoviles;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;



import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;


import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class IniciarSesionActivity extends AppCompatActivity {

    private EditText correo;
    private EditText contrasena;
    private FirebaseAuth mAuth;
    private String admin;



    public static final String TAG = "YOUR-TAG-NAME";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);


        correo = findViewById(R.id.Correo);
        contrasena = findViewById(R.id.Contrasena1);
        mAuth = FirebaseAuth.getInstance();
        android.widget.Button button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Iniciarsesion(correo.getText().toString(),contrasena.getText().toString());

            }
        });

    }

    public void onStart() {
        super.onStart();
    }

    public void Iniciarsesion(String correo,String contrasena) {
        mAuth.signInWithEmailAndPassword(correo, contrasena)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            assert currentUser != null;
                            if( currentUser.isEmailVerified()){
                               FirebaseUser user = mAuth.getCurrentUser();
                               Toast.makeText(getApplicationContext(), "Ha iniciado Sesion Correctamente :D", Toast.LENGTH_SHORT).show();
                               //Se devuelve a la pantalla principal
                                isAdmin();
                           }

                           else{

                               Toast.makeText(IniciarSesionActivity.this, "Primero Verifica Tu Correo >:/", Toast.LENGTH_SHORT).show();
                           }



                           //UpdateUi(user);
                        } else {
                            Toast.makeText(getApplicationContext(), "Autenticacion Fallida :(", Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });


    }

    public void isAdmin(){
        FirebaseFirestore.getInstance().collection("Users").whereEqualTo("correo", correo.getText().toString())
               .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        admin = String.valueOf(document.getBoolean("admin"));
                    }
                    if(Objects.equals(admin, "true")){
                        Intent i = new Intent(getApplicationContext(), Logeado.class);
                        startActivity(i);
                    }else{
                        Intent i = new Intent(getApplicationContext(), LogeadoUser.class);
                        startActivity(i);
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }



}

