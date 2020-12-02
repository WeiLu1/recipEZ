package com.example.recipez;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

import io.reactivex.rxjava3.disposables.CompositeDisposable;


public class ShowRecipesActivity extends AppCompatActivity {

    String responseRecipe;
    String responseInstruct;
    ObjectMapper map = new ObjectMapper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_recipes);

        Intent intent = getIntent();
        TextView textViewShowRecipe = findViewById(R.id.textViewShowRecipes);
        TextView textViewShowInstructions = findViewById(R.id.textViewShowInstructions);
        TextView textViewShowIngredients = findViewById(R.id.textViewShowIngredients);
        textViewShowIngredients.setMovementMethod(new ScrollingMovementMethod());
        textViewShowInstructions.setMovementMethod(new ScrollingMovementMethod());

        String passed = intent.getStringExtra("input");
        List<String> items = Arrays.asList(passed.split("\\s*,\\s*"));
        RecipesAPI recipe = new RecipesAPI(items);

        try {
            CallAPIIngredients ingredients = new CallAPIIngredients();
            ingredients.execute(recipe);
            responseRecipe = ingredients.get();

            Ingredients[] recipeList = map.readValue(responseRecipe, Ingredients[].class);
            String recipeTitle = recipeList[0].getTitle();
            String recipeID = recipeList[0].getId();
            ArrayList<Map> usedIngr = recipeList[0].getUsedIngredients();
            ArrayList<Map> missedIngr = recipeList[0].getMissedIngredients();
            textViewShowRecipe.setText(recipeTitle);

            for (int i = 0; i < usedIngr.size(); i++){
                textViewShowIngredients.append("- ");
                textViewShowIngredients.append((String) usedIngr.get(i).get("original"));
                textViewShowIngredients.append("\n");
            }

            for (int i = 0; i < missedIngr.size(); i++){
                textViewShowIngredients.append("- ");
                textViewShowIngredients.append((String) missedIngr.get(i).get("original"));
                textViewShowIngredients.append("\n");
            }


            if (recipeID != null) {
                RecipesAPI recipeInstructs = new RecipesAPI(recipeID);
                CallAPIInstructions instruct = new CallAPIInstructions();
                instruct.execute(recipeInstructs);
                responseInstruct = instruct.get();
                Instructions[] instructionList = map.readValue(responseInstruct, Instructions[].class);
                ArrayList<Map> instruction = instructionList[0].getSteps();
//                Log.d("instruction", String.valueOf(instruction.get(0).get("step")));

                for (int i = 0; i < instruction.size(); i++) {
                    textViewShowInstructions.append("- ");
                    textViewShowInstructions.append((String) instruction.get(i).get("step"));
                    textViewShowInstructions.append("\n");
                    textViewShowInstructions.append("\n");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}