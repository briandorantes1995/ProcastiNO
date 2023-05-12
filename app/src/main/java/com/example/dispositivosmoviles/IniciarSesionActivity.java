package com.example.dispositivosmoviles;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class IniciarSesionActivity extends AppCompatActivity {

    private EditText correo;
    private EditText contrasena;
    private FirebaseAuth mAuth;
    private android.widget.Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);


        correo = findViewById(R.id.Correo);
        contrasena = findViewById(R.id.Contrasena1);
        mAuth = FirebaseAuth.getInstance();
        button = findViewById(R.id.button);

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
                               Intent i = new Intent(getApplicationContext(), Logeado.class);
                               startActivity(i);

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

    public void Tranzaxd(View view){
        Intent i = new Intent (this, IniciarSesion2.class);
        startActivity(i);
    }


}

