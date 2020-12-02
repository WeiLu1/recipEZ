package com.example.recipez;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.buttonFindRecipes);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ShowRecipesActivity.class);
                EditText editText = (EditText) findViewById(R.id.editTextTextMultiLineIngredientsList);
                String message = editText.getText().toString();
                intent.putExtra("input", message);
                view.getContext().startActivity(intent);}
        });

    }

}