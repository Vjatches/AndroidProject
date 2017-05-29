package pl.krakow.uek.project_android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


public class MainActivity extends AppCompatActivity {

    String[] data = {"USD", "EUR", "CZK", "GBP", "AUD", "CAD"};
    String[] dataSellBuy = {"SELL","BUY"};
    String currency;
    String direction;
    String amount;
    Button convert;
    Intent intent;
    EditText amountET;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        intent = new Intent(this, Kantors.class);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(adapter);
        spinner.setPrompt("Currency");
        spinner.setSelection(0);
        currency = spinner.getSelectedItem().toString();
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                currency = spinner.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dataSellBuy);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner2.setAdapter(adapter2);
        spinner2.setPrompt("Direction");
        spinner2.setSelection(0);
        direction = spinner2.getSelectedItem().toString();
        spinner2.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                direction = spinner2.getSelectedItem().toString();

            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        amountET = (EditText)findViewById(R.id.amount);

        convert = (Button)findViewById(R.id.convert);
        convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("currency", currency);
                intent.putExtra("direction", direction);
                amount = amountET.getText().toString();
                intent.putExtra("amount", amount);
                startActivity(intent);
            }
        });

    }
}
