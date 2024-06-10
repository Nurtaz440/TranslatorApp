package com.nurtaz.dev.translater;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.NaturalLanguageTranslateRegistrar;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

public class MainActivity extends AppCompatActivity {

    private Spinner fromSpinner,toSpinner;
    private EditText sourceEdt;
    private Button btn;
    private TextView translatedTv;

    //souce array of strings - Spinners Data

    String[] fromLanguage = {
      "from", "English","Africaans","Arabic","TURKISH","KOREAN","RUSSIAN","Hindi","CHINESE"
    };
     String[] toLanguage = {
      "to", "English","Africans","Arabic","TURKISH","KOREAN","RUSSIAN","Hindi","CHINESE"
    };

     //permission
    private static final int REQUEST_CODE = 1;

    String languageCode,fromLanguageCode, toLanguageCode;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fromSpinner = findViewById(R.id.idFromSpinner);
        toSpinner = findViewById(R.id.idToSpinner);
        sourceEdt = findViewById(R.id.idEditSource);
        btn = findViewById(R.id.btn);
        translatedTv = findViewById(R.id.textViewResult);


        //spinner
        fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fromLanguageCode = String.valueOf(GetLanguageCode(fromLanguage[position]));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter fromAdapter = new ArrayAdapter(this,R.layout.item_spinner,fromLanguage);
        fromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromSpinner.setAdapter(fromAdapter);

         toSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                toLanguageCode = String.valueOf(GetLanguageCode(toLanguage[position]));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter toAdapter = new ArrayAdapter(this,R.layout.item_spinner,toLanguage);
        toAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toSpinner.setAdapter(toAdapter);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                translatedTv.setText("");
                if (sourceEdt.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this,"Please enter your text",Toast.LENGTH_SHORT).show();
                }else if (fromLanguageCode.isEmpty()){
                    Toast.makeText(MainActivity.this,"Please Select Source Language",Toast.LENGTH_SHORT).show();

                }else if (toLanguageCode.isEmpty()){
                    Toast.makeText(MainActivity.this,"Please Select The Target Language",Toast.LENGTH_SHORT).show();

                }else {
                    TranslateText(fromLanguageCode,toLanguageCode,sourceEdt.getText().toString());
                }
            }
        });

    }
    private void TranslateText(String fromLanguageCode, String toLanguageCode, String src){
        translatedTv.setText("Downloading Language Model");
        try {
            TranslatorOptions options = new TranslatorOptions.Builder()
                    .setSourceLanguage(fromLanguageCode)
                    .setTargetLanguage(toLanguageCode)
                    .build();

            Translator translator = Translation.getClient(options);

            DownloadConditions conditions = new DownloadConditions.Builder().build();

            translator.downloadModelIfNeeded(conditions)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            translatedTv.setText("Translating..");
                            translator.translate(src).addOnSuccessListener(new OnSuccessListener<String>() {
                                @Override
                                public void onSuccess(String s) {
                                    translatedTv.setText(s);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MainActivity.this,"Fail Translate",Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this,"Fail DownLoad Language",Toast.LENGTH_SHORT).show();
                        }
                    });
        }catch (Exception e){
            e.printStackTrace();
        }


    }
    private String GetLanguageCode(String language) {
        String LanguaeCode = "";
        switch (language){
            case "English" :
                languageCode = TranslateLanguage.ENGLISH;
                break;
                case "Africaans" :
                languageCode = TranslateLanguage.AFRIKAANS;
                break;
                 case "Arabic" :
                languageCode = TranslateLanguage.ARABIC;
                break;
                case "TURKISH" :
                languageCode = TranslateLanguage.TURKISH;
                break;
                case "KOREAN" :
                languageCode = TranslateLanguage.KOREAN;
                break;
                case "RUSSIAN" :
                languageCode = TranslateLanguage.RUSSIAN;
                break;
                 case "Hindi" :
                languageCode = TranslateLanguage.HINDI;
                break;
                case "CHINESE" :
                languageCode = TranslateLanguage.CHINESE;
                break;
            default:
                languageCode = "";

        }
        return languageCode;
    }
}