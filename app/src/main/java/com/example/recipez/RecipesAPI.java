package com.example.recipez;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;



public class RecipesAPI {

    public final String APIKey = "";
    List<String> items;
    String id;

    public RecipesAPI(List<String> items){
        this.items = items;
    }

    public RecipesAPI(String id){
        this.id = id;
    }

}

class APICaller{

    String URL;

    public APICaller(String url){
        this.URL = url;
    }

    public String getAPIResponse(){

        StringBuilder sbResponse = new StringBuilder();

        try{
            URL url = new URL(URL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                sbResponse.append(line).append("\n");
            }
            br.close();
            urlConnection.disconnect();
            return sbResponse.toString();
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}


class CallAPIIngredients extends AsyncTask<RecipesAPI, Void, String>{

    @Override
    protected String doInBackground(RecipesAPI... recipesAPIS) {
        List<String> itemsSend = recipesAPIS[0].items;
        String APIKey = recipesAPIS[0].APIKey;
        StringBuilder sbUrl = new StringBuilder();

        for (String s : itemsSend) {
            sbUrl.append(s).append(",+");
        }
        String URL = "https://api.spoonacular.com/recipes/findByIngredients?apiKey=" + APIKey + "&ingredients=" + sbUrl.toString().substring(0, sbUrl.toString().length() - 2);

        APICaller call = new APICaller(URL);
        return call.getAPIResponse();
    }
}


class CallAPIInstructions extends AsyncTask<RecipesAPI, Void, String> {

    @Override
    protected String doInBackground(RecipesAPI... recipesAPIS) {
        String id = recipesAPIS[0].id;
        String APIKey = recipesAPIS[0].APIKey;
        String URL = "https://api.spoonacular.com/recipes/" + id + "/analyzedInstructions?apiKey=" + APIKey;
        APICaller call = new APICaller(URL);
        return call.getAPIResponse();
    }
}



@JsonIgnoreProperties(ignoreUnknown = true)
class Ingredients {

    @JsonProperty("title")
    private String title;

    @JsonProperty("id")
    private String id;

    @JsonProperty("missedIngredients")
    private ArrayList<Map> missedIngredients;

    @JsonProperty("usedIngredients")
    private ArrayList<Map> usedIngredients;

    public String getTitle(){
        return title;
    }

    public String getId(){
        return id;
    }

    public ArrayList<Map> getMissedIngredients(){
        return missedIngredients;
    }

    public ArrayList<Map> getUsedIngredients(){
        return usedIngredients;
    }
}


@JsonIgnoreProperties(ignoreUnknown = true)
class Instructions {

    @JsonProperty("steps")
    private ArrayList<Map> steps;

    public ArrayList<Map> getSteps(){
        return steps;
    }

}
