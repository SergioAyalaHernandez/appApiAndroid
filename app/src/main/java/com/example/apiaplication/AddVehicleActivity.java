package com.example.apiaplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class AddVehicleActivity extends Activity {
    private EditText nameEditText;
    private EditText brandEditText;
    private EditText yearEditText;
    private EditText descriptionEditText;
    private EditText payDayEditText;
    private EditText linkEditText;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);

        // Obtener referencias a los elementos de la interfaz de usuario
        nameEditText = findViewById(R.id.nameEditText);
        brandEditText = findViewById(R.id.brandEditText);
        yearEditText = findViewById(R.id.yearEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        payDayEditText = findViewById(R.id.payDayEditText);
        linkEditText = findViewById(R.id.linkEditText);
        saveButton = findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener los valores ingresados por el usuario
                String name = nameEditText.getText().toString();
                String brand = brandEditText.getText().toString();
                String year = yearEditText.getText().toString();
                String description = descriptionEditText.getText().toString();
                String payDay = payDayEditText.getText().toString();
                String link = linkEditText.getText().toString();

                // Realizar la lógica para enviar los datos al servidor
                // Puedes utilizar una biblioteca como Volley o Retrofit para realizar la solicitud POST
                // Aquí tienes un ejemplo básico utilizando Volley:
                sendVehicleData(name, brand, year, description, payDay, link);
            }
        });
    }

    private void sendVehicleData(String name, String brand, String year, String description, String payDay, String link) {
        // Realizar la solicitud POST al servidor con los datos del vehículo
        // Puedes utilizar una biblioteca como Volley o Retrofit para realizar la solicitud POST
        // Aquí tienes un ejemplo básico utilizando Volley:

        String url = "http://34.16.137.107:80/api/car/save";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Intent intent = new Intent(AddVehicleActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra("isNewInstance", true);
                        startActivity(intent);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error en ", "API request error: " + error.getMessage());
                        showError();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Establecer los parámetros de la solicitud POST con los datos del vehículo
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("brand", brand);
                params.put("year", year);
                params.put("description", description);
                params.put("payDay", payDay);
                params.put("link", link);
                return params;
            }
        };

        // Agregar la solicitud a la cola de solicitudes de Volley
        Volley.newRequestQueue(this).add(request);
    }
    private void showError() {
        Toast.makeText(AddVehicleActivity.this, "Error al obtener los datos de la API", Toast.LENGTH_SHORT).show();
    }
}