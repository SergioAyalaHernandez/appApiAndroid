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

public class MainActivity2 extends AppCompatActivity {

    private static final String API_URL = "http://34.125.204.221:80/api/car/listar";

    private RequestQueue requestQueue;
    private TableLayout tableLayout;
    private Button updateButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        tableLayout = findViewById(R.id.tableLayout);
        requestQueue = Volley.newRequestQueue(this);
        updateButton = findViewById(R.id.updateButton);


        fetchCarData();
        Button updateButton = findViewById(R.id.updateButton);
        Button addButton = findViewById(R.id.addVehicleButton);
        Button homeButton = findViewById(R.id.inicioButton);
        homeButton.setBackgroundTintList(ColorStateList.valueOf(Color.argb(255, 1, 19, 135)));
        homeButton.setTextColor(Color.WHITE);
        updateButton.setBackgroundTintList(ColorStateList.valueOf(Color.argb(255, 1, 19, 135)));
        updateButton.setTextColor(Color.WHITE);
        addButton.setBackgroundTintList(ColorStateList.valueOf(Color.argb(255, 1, 19, 135)));
        addButton.setTextColor(Color.WHITE);

        TableRow headerRow = new TableRow(this);

        TextView idHeaderTextView = createHeaderTextView("Id");
        headerRow.addView(idHeaderTextView);
        TableRow.LayoutParams idTextViewParams = new TableRow.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.4f);
        idHeaderTextView.setLayoutParams(idTextViewParams);

        TextView nameHeaderTextView = createHeaderTextView("Name");
        headerRow.addView(nameHeaderTextView);
        TableRow.LayoutParams nameTextViewParams = new TableRow.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.6f);
        nameHeaderTextView.setLayoutParams(nameTextViewParams);

        TextView yearHeaderTextView = createHeaderTextView("Year");
        headerRow.addView(yearHeaderTextView);
        TableRow.LayoutParams yearTextViewParams = new TableRow.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.5f);
        yearHeaderTextView.setLayoutParams(yearTextViewParams);

        TextView actionHeaderTextView = createHeaderTextView("Action");
        headerRow.addView(actionHeaderTextView);
        TableRow.LayoutParams actionTextViewParams = new TableRow.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.5f);
        actionHeaderTextView.setLayoutParams(actionTextViewParams);

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
        addVehicleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2.this, AddVehicleActivity.class);
                startActivity(intent);
            }
        });

        Button homeButtonhome = findViewById(R.id.inicioButton);
        homeButtonhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void refreshActivity() {
        recreate();
    }

    private void fetchCarData() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, API_URL, null,
                response -> {
                    try {
                        JSONArray carArray = response.getJSONArray("doc");
                        for (int i = 0; i < carArray.length(); i++) {
                            JSONObject carObject = carArray.getJSONObject(i);
                            String id = carObject.getString("_id");
                            String name = carObject.getString("name");
                            String year = carObject.getString("year");


                            // Verificar si la fila ya existe en la tabla antes de agregarla
                            if (!isRowExists(id, name, year)) {
                                addRowToTable(id, name, year);
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

    private void addRowToTable(String id, String name, String year) {
        TableRow row = new TableRow(this);
        row.setPadding(0, 8, 0, 8);

        TextView idTextView = createTextViewWithStyle(getShortenedId(id));
        TableRow.LayoutParams idTextViewParams = new TableRow.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.4f);
        idTextView.setLayoutParams(idTextViewParams);
        row.addView(idTextView);

        TextView nameTextView = createTextViewWithStyle(name);
        TableRow.LayoutParams nameTextViewParams = new TableRow.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.6f);
        nameTextView.setLayoutParams(nameTextViewParams);
        row.addView(nameTextView);

        TextView yearTextView = createTextViewWithStyle(year);
        TableRow.LayoutParams yearTextViewParams = new TableRow.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.5f);
        yearTextView.setLayoutParams(yearTextViewParams);
        row.addView(yearTextView);

        Button deleteButton = createDeleteButton(id);
        TableRow.LayoutParams deleteButtonParams = new TableRow.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.4f);
        deleteButton.setLayoutParams(deleteButtonParams);
        row.addView(deleteButton);

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

    private Button createDeleteButton(String id) {
        Button button = new Button(this);
        button.setText("Delete");
        button.setTextColor(Color.WHITE);
        button.setBackgroundResource(R.drawable.delete_button_background);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCarData(id);
            }
        });

        return button;
    }

    private void deleteCarData(String id) {
        String deleteUrl = "http://34.125.204.221:80/api/car/delete/" + id;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, deleteUrl, null,
                response -> {
                    // Eliminación exitosa, realizar cualquier acción adicional requerida
                    Toast.makeText(MainActivity2.this, "Data deleted successfully", Toast.LENGTH_SHORT).show();
                    fetchCarData(); // Actualizar la tabla después de eliminar los datos
                },
                error -> {
                    // Error en la eliminación, mostrar un mensaje de error
                    Toast.makeText(MainActivity2.this, "Failed to delete data", Toast.LENGTH_SHORT).show();
                    Log.e("MainActivity", "API request error: " + error.getMessage());
                });

        requestQueue.add(request);
        refreshActivity();
    }

    private void showError() {
        Toast.makeText(MainActivity2.this, "Error al obtener los datos de la API", Toast.LENGTH_SHORT).show();
    }

    private boolean isRowExists(String id, String name, String year) {
        int rowCount = tableLayout.getChildCount();

        for (int i = 0; i < rowCount; i++) {
            View view = tableLayout.getChildAt(i);
            if (view instanceof TableRow) {
                TableRow row = (TableRow) view;
                TextView idTextView = (TextView) row.getChildAt(0);
                TextView nameTextView = (TextView) row.getChildAt(1);
                TextView yearTextView = (TextView) row.getChildAt(2);

                if (idTextView == null || nameTextView == null || yearTextView == null) {
                    continue;
                }

                String existingId = idTextView.getText().toString();
                String existingName = nameTextView.getText().toString();
                String existingYear = yearTextView.getText().toString();

                if (existingId.equals(id) && existingName.equals(name) && existingYear.equals(year)) {
                    return true;
                }
            }
        }

        return false;
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

    private String getShortenedId(String id) {
        if (id.length() <= 3) {
            return id;
        } else {
            return id.substring(0, 3) + "...";
        }
    }

}
