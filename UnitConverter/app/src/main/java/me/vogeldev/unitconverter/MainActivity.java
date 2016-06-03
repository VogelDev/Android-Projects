package me.vogeldev.unitconverter;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    private Spinner spinFrom, spinTo;
    private EditText etFrom, etTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayAdapter<String> spinAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.units));
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        spinFrom = (Spinner)findViewById(R.id.spinFrom);
        spinTo = (Spinner)findViewById(R.id.spinTo);

        spinFrom.setAdapter(spinAdapter);
        spinTo.setAdapter(spinAdapter);

        etFrom = (EditText)findViewById(R.id.etFrom);
        etTo = (EditText)findViewById(R.id.etTo);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setTitleTextColor(Color.WHITE);

    }

    public void convert(View v){

        Converter.Unit from = Converter.Unit.fromString((String)spinFrom.getSelectedItem());
        Converter.Unit to = Converter.Unit.fromString((String)spinTo.getSelectedItem());

        Converter converter = new Converter(from, to);

        etTo.setText(String.valueOf(converter.convert(Double.valueOf(etFrom.getText().toString()))));
    }
}
