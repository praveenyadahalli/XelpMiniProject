package com.geometric.xelp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText item1,item2,item3;
    Button add;
    ListView listView;
    ArrayList<String>itemlist=new ArrayList<>();
    ArrayAdapter<String>arrayAdapter;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        item1=findViewById(R.id.editTextName);
        item2=findViewById(R.id.editTextMobile);
        item3=findViewById(R.id.editTextEmail);

        add=findViewById(R.id.button);
        listView=findViewById(R.id.list);

        mAuth = FirebaseAuth.getInstance();

        itemlist = FileHelper.readData(this);

        arrayAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1,itemlist);
        listView.setAdapter(arrayAdapter);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              String itemName="";
                int num1=Integer.parseInt(item1.getText().toString());
                int num2=Integer.parseInt(item2.getText().toString());
                int num3=Integer.parseInt(item3.getText().toString());
                if(checkPrime(num1) && checkPrime(num2) && checkPrime(num3) ){
                    itemName=item1.getText().toString()+" "+item2.getText().toString()+" "+item3.getText().toString()+" prime numbers";
                }else{

                    if(!checkPrime((num1))){
                        item1.setError("Not a prime number");
                        itemName+=item1.getText().toString()+" ";
                    }
                    if(!checkPrime(num2)){
                        item2.setError("Not a prime Number");
                        itemName+=item2.getText().toString()+" ";
                    }
                    if(!checkPrime(num3)){
                        item3.setError("Not a prime Number");
                        itemName+=item3.getText().toString()+" ";
                    }
                    itemName=itemName+"not prime number";
                }


                itemlist.add(itemName);

                item1.setText("");
                item2.setText("");
                item3.setText("");
                FileHelper.writeData(itemlist,getApplicationContext());
                arrayAdapter.notifyDataSetChanged();


            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,final int position, long id) {
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("Delete");
                alert.setMessage("Do you want to delete");
                alert.setCancelable(false);
                alert.setNegativeButton("No", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        dialog.cancel();
                    }
                });
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        itemlist.remove(position);
                        arrayAdapter.notifyDataSetChanged();
                        FileHelper.writeData(itemlist,getApplicationContext());
                    }
                });
                AlertDialog alertDialog=alert.create();
                alertDialog.show();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        int id = item.getItemId();
        switch (id){
            case R.id.idLogOut:
                Toast.makeText(this,"User Logged Out..",Toast.LENGTH_SHORT).show();
                mAuth.signOut();
                Intent i = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(i);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private static boolean checkPrime(int n){
        for(int i=2;i<=n/2;i++){
            if(n%i==0){
                return false;
            }
        }
        return true;
    }
}