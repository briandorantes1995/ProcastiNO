package com.example.dispositivosmoviles;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.Locale;

public class Logeado extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button button;
    private TextView textview;
    private Locale locale;
    private Configuration config = new Configuration();
    TextView tvInfoUser;
    Button sendButton;
    EditText sendText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logeado);
        sendButton = findViewById(R.id.btnAddItem2);
        sendText = findViewById(R.id.etNewItem2);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        tvInfoUser = findViewById(R.id.tv_info_user);
        initviews(user);

        //Funcion Callback enviar info a la otra activity
        sendButton.setOnClickListener(v -> {
            String str = sendText.getText().toString();
            SharedPreferences sharedPref = getSharedPreferences("myKey", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("value", str);
            editor.apply();
            sendText.setText("");
        });//Fin callbackbutton
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
    }
    //Inicia el chat
    public void chat(View view) {
        Intent i = new Intent(getApplicationContext(), Chat.class);
        startActivity(i);
    }

    //Inicia la videollmada
    public void Videollamada(View view) {
        Intent i = new Intent(getApplicationContext(), Videollamada.class);
        startActivity(i);
    }
}