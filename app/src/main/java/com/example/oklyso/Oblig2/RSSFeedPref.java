package com.example.oklyso.Oblig2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

public class RSSFeedPref extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.oklyso.MESSAGE";
    private Spinner time, element;
    public ArrayList<String> numb, list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        time = findViewById(R.id.timer);
  //Preferred time-choice.
        SharedPreferences settings = getSharedPreferences("Change", MODE_PRIVATE);
        numb = new ArrayList<String>();
        numb.add("1 hour");
        numb.add("12 hours");
        numb.add("24 hours");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, numb);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        time.setAdapter(adapter);
        int def = settings.getInt("Timer", 1);
        for(int i = 0; i < numb.size(); i++){
            if(def == i){
                time.setSelection(i);
            }

        }

        time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                SharedPreferences settings = getSharedPreferences("Change", MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("Timer", position);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });

        element = findViewById(R.id.listNr);

        list = new ArrayList<String>();
        list.add("5");
        list.add("10");
        list.add("20");
        list.add("50");
        list.add("100");

        ArrayAdapter<String> ad = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        element.setAdapter(ad);
        int nr = settings.getInt("Elements", 1);
        for(int j = 0; j < list.size(); j++) {
            if (nr == j) {
                element.setSelection(j);
            }
        }

            element.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    SharedPreferences settings = getSharedPreferences("Change", MODE_PRIVATE);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putInt("Elements", position);
                    editor.apply();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {

                }

            });

    }



    public void fetchFeed(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, MainActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        String url = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, url);

        SharedPreferences settings = getSharedPreferences("Change", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("Url", editText.toString());
        editor.apply();

        startActivity(intent);
    }

    public void goToActivity1(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
