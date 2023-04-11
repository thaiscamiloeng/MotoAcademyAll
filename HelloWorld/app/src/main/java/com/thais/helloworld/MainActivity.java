package com.thais.helloworld;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // add uma variavel do tipo botao. Buscar pelo ID
        Button botao = (Button) findViewById(R.id.cliqueme);

        // add o listener pra comportamento
        botao.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // acao
                Toast.makeText( MainActivity.this, "Clique no botao", Toast.LENGTH_SHORT).show();
            }
        });
        };
    }