package com.example.apiaplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

public class activity_add_vehicle extends AppCompatActivity {
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
                String name = nameEditText.getText().toString();
                String brand = brandEditText.getText().toString();
                String year = yearEditText.getText().toString();
                String description = descriptionEditText.getText().toString();
                String payDay = payDayEditText.getText().toString();
                String link = linkEditText.getText().toString();
                sendVehicleData(name, brand, year, description, payDay, link);
            }
        });
    }

    private void sendVehicleData(String name, String brand, String year, String description, String payDay, String link) {
        String url = "http://34.16.137.107:80/api/car/save";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Intent intent = new Intent(activity_add_vehicle.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra("isNewInstance", true);
                        startActivity(intent);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
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

        Volley.newRequestQueue(this).add(request);
    }
}