package com.example.i5banauth;

import android.database.Cursor;
import android.os.Bundle;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    //EditText Name;
    Context context;
    private DatabaseHandler helper = new DatabaseHandler(this);

    String[] banks = { "SBI", "IOB", "HDFC", "TMB", "ICICI" };
    String Selectedbank;
    private ListView listView;

    private SimpleCursorAdapter dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
/*
code for spinner
 */
        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        Spinner spin = (Spinner) findViewById(R.id.bankspinner);
        spin.setOnItemSelectedListener(this);
        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,banks);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);
/*
 code to handle database
 */     displayListView();
        System.out.println("In Main");

    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position,long id) {
        Selectedbank = banks[position];
        Toast.makeText(getApplicationContext(), "Selected Bank: "+banks[position] ,Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO - Custom Code
    }

    public void addBank(View view)
    {

        try {
            Toast.makeText(this,"Running", Toast.LENGTH_LONG).show();
            long identity = helper.insertData(Selectedbank);
            if(identity<0)
            {
                Toast.makeText( this, "Unsuccessful", Toast.LENGTH_LONG).show();
            } else
            {
                displayListView();
                Toast.makeText( this, "Successful", Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void displayListView() {

        try {
            Cursor cursor = helper.fetchData();

            final String[] from = new String[]{DatabaseHelper.SNO, DatabaseHelper.BANK};
            final int to[] = new int[]{R.id.bankid, R.id.banknm};

// create the adapter using the cursor pointing to the desired data
//as well as the layout information
            dataAdapter = new SimpleCursorAdapter(
                    this, R.layout.bank, cursor, from, to, 0);
            ListView listView = (ListView) findViewById(R.id.list_view);
// Assign adapter to ListView
            listView.setAdapter(dataAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
// Get the cursor, positioned to the corresponding row in the result set
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);
// Get the state's capital from this row in the database.
                String bankidsel = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.SNO));
                Toast.makeText(getApplicationContext(),bankidsel, Toast.LENGTH_SHORT).show();
                delBank(bankidsel);

            }
        });

        }
    catch (Exception e) {
        e.printStackTrace();
    }

    }

    public void delBank(String id)
    {

        try {
            int bankid  = Integer.parseInt(id);
            long identity = helper.deleteData(bankid);
            if(identity<0)
            {
                Toast.makeText( this, "Unsuccessful Del", Toast.LENGTH_LONG).show();
            } else
            {
                displayListView();
                Toast.makeText( this, "Successful Del", Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
