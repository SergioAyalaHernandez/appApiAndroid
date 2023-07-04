package com.example.apiaplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class updateCar extends AppCompatActivity {

    private static final String API_URL = "http://34.125.204.221:80/api/car/mostrar/";
    private static final String API_URL_UPDATE = "http://34.125.204.221:80/api/car/update/";

    private RequestQueue requestQueue;
    private EditText nameEditText;
    private EditText brandEditText;
    private EditText yearEditText;
    private EditText descriptionEditText;
    private EditText payDayEditText;
    private EditText linkEditText;
    private Button updateButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_car);

        String vehicleId = getIntent().getStringExtra("vehicleId");

        nameEditText = findViewById(R.id.nameEditTextUpdate);
        brandEditText = findViewById(R.id.brandEditTextUpdate);
        yearEditText = findViewById(R.id.yearEditTextUpdate);
        descriptionEditText = findViewById(R.id.descriptionEditTextUpdate);
        payDayEditText = findViewById(R.id.payDayEditTextUpdate);
        linkEditText = findViewById(R.id.linkEditTextUpdate);
        updateButton = findViewById(R.id.updateButtonUpdate);

        requestQueue = Volley.newRequestQueue(this);

        fetchVehicleData(vehicleId);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener los valores ingresados por el usuario
                String name = nameEditText.getText().toString();
                String brand = brandEditText.getText().toString();
                String year = yearEditText.getText().toString();
                String description = descriptionEditText.getText().toString();
                String payDay = payDayEditText.getText().toString();
                String link = linkEditText.getText().toString();

                // Crear una cadena con los datos actualizados en formato x-www-form-urlencoded
                String requestBody = "name=" + name +
                        "&brand=" + brand +
                        "&year=" + year +
                        "&description=" + description +
                        "&payDay=" + payDay +
                        "&link=" + link;

                // Enviar la solicitud PUT con los datos actualizados
                String url = API_URL_UPDATE + vehicleId;

                StringRequest request = new StringRequest(Request.Method.PUT, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Actualización exitosa
                                Toast.makeText(updateCar.this, "Vehículo actualizado", Toast.LENGTH_SHORT).show();
                                finish(); // Cerrar la actividad después de la actualización
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("UpdateVehicleActivity", "API request error: " + error.getMessage());
                                showError();
                            }
                        }) {
                    @Override
                    public String getBodyContentType() {
                        return "application/x-www-form-urlencoded";
                    }

                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        try {
                            return requestBody.getBytes("utf-8");
                        } catch (UnsupportedEncodingException e) {
                            Log.e("UpdateVehicleActivity", "Encoding exception: " + e.getMessage());
                            return null;
                        }
                    }
                };

                requestQueue.add(request);
            }
        });

    }

    private void fetchVehicleData(String vehicleId) {
        String url = API_URL + vehicleId;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Obtener el objeto "usuario" del objeto JSON de respuesta
                            JSONObject userObject = response.getJSONObject("usuario");

                            // Obtener los datos del vehículo del objeto "usuario"
                            String name = userObject.getString("name");
                            String brand = userObject.getString("brand");
                            String year = userObject.getString("year");
                            String description = userObject.getString("description");
                            String payDay = userObject.getString("payDay");
                            String link = userObject.getString("link");

                            // Establecer los datos en los campos correspondientes
                            nameEditText.setText(name);
                            brandEditText.setText(brand);
                            yearEditText.setText(year);
                            descriptionEditText.setText(description);
                            payDayEditText.setText(payDay);
                            linkEditText.setText(link);

                        } catch (JSONException e) {
                            Log.e("UpdateVehicleActivity", "JSON exception: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("UpdateVehicleActivity", "API request error: " + error.getMessage());
                        showError();
                    }
                });

        requestQueue.add(request);
    }

    private void showError() {
        Toast.makeText(updateCar.this, "Error al obtener los datos de la API", Toast.LENGTH_SHORT).show();
    }
}