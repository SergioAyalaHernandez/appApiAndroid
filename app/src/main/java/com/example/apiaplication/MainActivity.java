package com.example.apiaplication;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private static final String API_URL = "http://34.16.137.107:80/api/car/listar";

    private RequestQueue requestQueue;
    private TableLayout tableLayout;
    private Button updateButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boolean isNewInstance = getIntent().getBooleanExtra("isNewInstance", false);
        if (isNewInstance) {
            refreshActivity();
        }
        tableLayout = findViewById(R.id.tableLayout);
        requestQueue = Volley.newRequestQueue(this);
        updateButton = findViewById(R.id.updateButton);

        fetchCarData();
        Button updateButton = findViewById(R.id.updateButton);
        Button addButton = findViewById(R.id.addVehicleButton);
        Button deleteButton = findViewById(R.id.deleteVehicleButton);
        Button upButton = findViewById(R.id.updateVehiculo);

        upButton.setBackgroundTintList(ColorStateList.valueOf(Color.argb(255, 1, 19, 135)));
        upButton.setTextColor(Color.WHITE);
        updateButton.setBackgroundTintList(ColorStateList.valueOf(Color.argb(255, 1, 19, 135)));
        updateButton.setTextColor(Color.WHITE);
        deleteButton.setBackgroundTintList(ColorStateList.valueOf(Color.argb(255, 1, 19, 135)));
        deleteButton.setTextColor(Color.WHITE);
        addButton.setBackgroundTintList(ColorStateList.valueOf(Color.argb(255, 1, 19, 135)));
        addButton.setTextColor(Color.WHITE);

        TableRow headerRow = new TableRow(this);


        TextView nameHeaderTextView = createHeaderTextView("Name");
        headerRow.addView(nameHeaderTextView);
        TableRow.LayoutParams nameTextViewParams = new TableRow.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.6f);
        nameHeaderTextView.setLayoutParams(nameTextViewParams);

        TextView brandHeaderTextView = createHeaderTextView("Brand");
        headerRow.addView(brandHeaderTextView);
        TableRow.LayoutParams brandTextViewParams = new TableRow.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.6f);
        brandHeaderTextView.setLayoutParams(brandTextViewParams);


        TextView yearHeaderTextView = createHeaderTextView("Year");
        headerRow.addView(yearHeaderTextView);
        TableRow.LayoutParams yearTextViewParams = new TableRow.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.5f);
        yearHeaderTextView.setLayoutParams(yearTextViewParams);

        TextView descriptionHeaderTextView = createHeaderTextView("Description");
        headerRow.addView(descriptionHeaderTextView);
        TableRow.LayoutParams descriptionTextViewParams = new TableRow.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.7f);
        descriptionHeaderTextView.setLayoutParams(descriptionTextViewParams);

        TextView payDayHeaderTextView = createHeaderTextView("Pay Day");
        headerRow.addView(payDayHeaderTextView);
        TableRow.LayoutParams payTextViewParams = new TableRow.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.5f);
        payDayHeaderTextView.setLayoutParams(payTextViewParams);


        tableLayout.addView(headerRow, new TableLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));


        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchCarData();
                animateButtonClick();
                refreshActivity();
            }

        });

        Button addVehicleButton = findViewById(R.id.addVehicleButton);
        addVehicleButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View v){
                Intent intent = new Intent(MainActivity.this, AddVehicleActivity.class);
                startActivity(intent);
            }
        });

        Button deleteVehicleButton = findViewById(R.id.deleteVehicleButton);
        deleteVehicleButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View v){
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent);
            }
        });
    }

    private void fetchCarData() {
        requestQueue.cancelAll("");
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, API_URL, null,
                response -> {
                    try {
                        JSONArray carArray = response.getJSONArray("doc");
                        for (int i = 0; i < carArray.length(); i++) {
                            JSONObject carObject = carArray.getJSONObject(i);
                            String id = carObject.getString("_id");
                            String name = carObject.getString("name");
                            String brand = carObject.getString("brand");
                            String year = carObject.getString("year");
                            String description = carObject.getString("description");
                            String payDay = carObject.getString("payDay");
                            String link = carObject.getString("link");

                            // Verificar si la fila ya existe en la tabla antes de agregarla
                            if (!isRowExists(name, brand, year, description, payDay, link)) {
                                addRowToTable(name, brand, year, description, payDay, link);
                            }
                        }
                    } catch (JSONException e) {
                        Log.e("MainActivity", "JSON exception: " + e.getMessage());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("MainActivity", "API request error: " + error.getMessage());
                        showError();
                    }
                });

        requestQueue.add(request);
    }

    private void refreshActivity() {
        recreate();
    }

    private void addRowToTable(String name, String brand, String year, String description,
                               String payDay, String link) {
        TableRow row = new TableRow(this);
        row.setPadding(0, 8, 0, 8); // Agrega un espacio vertical de 8dp entre cada cuadro


        TextView nameTextView = createTextViewWithStyle(name);
        TableRow.LayoutParams nameTextViewParams = new TableRow.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.6f);
        nameTextView.setLayoutParams(nameTextViewParams);
        row.addView(nameTextView);

        TextView brandTextView = createTextViewWithStyle(brand);
        TableRow.LayoutParams brandTextViewParams = new TableRow.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.6f);
        brandTextView.setLayoutParams(brandTextViewParams);
        row.addView(brandTextView);

        TextView yearTextView = createTextViewWithStyle(year);
        TableRow.LayoutParams yearTextViewParams = new TableRow.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.5f);
        yearTextView.setLayoutParams(yearTextViewParams);
        row.addView(yearTextView);

        TextView descriptionTextView = createTextViewWithStyle(description);
        TableRow.LayoutParams descriptionTextViewParams = new TableRow.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.7f);
        descriptionTextView.setLayoutParams(descriptionTextViewParams);
        row.addView(descriptionTextView);

        TextView payDayTextView = createTextViewWithStyle(payDay);
        TableRow.LayoutParams payDayTextViewParams = new TableRow.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.5f);
        payDayTextView.setLayoutParams(payDayTextViewParams);
        row.addView(payDayTextView);

        tableLayout.addView(row, new TableLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }


    private TextView createTextViewWithStyle(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextColor(Color.BLACK);
        textView.setTextAppearance(R.style.TableRow);
        textView.setBackgroundResource(R.drawable.cell_background);
        textView.setPadding(16, 20, 16, 20);

        return textView;
    }

    private void showError() {
        // Agregar aquí la lógica para mostrar un mensaje de error al usuario
        // Puede ser un diálogo, una notificación o cualquier otro componente de la interfaz de usuario.
        // Por ejemplo, puedes mostrar un Toast con un mensaje de error.
        Toast.makeText(MainActivity.this, "Error al obtener los datos de la API", Toast.LENGTH_SHORT).show();
    }

    private boolean isRowExists(String name, String brand, String year, String description,
                                String payDay, String link) {
        int rowCount = tableLayout.getChildCount();

        for (int i = 0; i < rowCount; i++) {
            View view = tableLayout.getChildAt(i);
            if (view instanceof TableRow) {
                TableRow row = (TableRow) view;
                TextView nameTextView = (TextView) row.getChildAt(0);
                TextView brandTextView = (TextView) row.getChildAt(1);
                TextView yearTextView = (TextView) row.getChildAt(2);
                TextView descriptionTextView = (TextView) row.getChildAt(3);
                TextView payDayTextView = (TextView) row.getChildAt(4);

                // Verifica si alguno de los TextView es nulo antes de acceder a su contenido
                if (nameTextView == null || brandTextView == null || yearTextView == null
                        || descriptionTextView == null || payDayTextView == null) {
                    continue; // Salta esta iteración si alguno de los TextView es nulo
                }

                String existingName = nameTextView.getText().toString();
                String existingBrand = brandTextView.getText().toString();
                String existingYear = yearTextView.getText().toString();
                String existingDescription = descriptionTextView.getText().toString();
                String existingPayDay = payDayTextView.getText().toString();

                if (existingName.equals(name) && existingBrand.equals(brand)
                        && existingYear.equals(year) && existingDescription.equals(description)
                        && existingPayDay.equals(payDay)) {
                    return true; // La fila ya existe en la tabla
                }
            }
        }

        return false; // La fila no existe en la tabla
    }

    private TextView createHeaderTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextAppearance(R.style.TableHeader);
        textView.setBackgroundResource(R.drawable.header_background);
        textView.setPadding(16, 16, 16, 16);
        return textView;
    }

    private void animateButtonClick() {
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(updateButton, "scaleX", 1.0f, 0.9f, 1.0f);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(updateButton, "scaleY", 1.0f, 0.9f, 1.0f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleXAnimator, scaleYAnimator);
        animatorSet.setDuration(200);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.start();
    }



}
