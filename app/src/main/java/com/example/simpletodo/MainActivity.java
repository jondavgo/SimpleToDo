package com.example.simpletodo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import org.apache.commons.io.FileUtils;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_POSITION = "item_pos";
    public static final int EDIT_TEXT_CODE = 0;

    List<String> items;
    Button btnAdd;
    EditText etItem;
    RecyclerView rvItems;
    ItemsAdapter.OnLongClickListener listener;
    ItemsAdapter.OnClickListener clickListener;
    ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.buttonAdd);
        etItem = findViewById(R.id.etItem);
        rvItems = findViewById(R.id.rvitems);

        loadItems();

        clickListener = new ItemsAdapter.OnClickListener() {
            @Override
            public void onItemClick(int pos) {
                Log.d("MainActivity", "Tapped at position " + pos);
                // create new activity
                Intent i = new Intent(MainActivity.this, EditActivity.class);
                // pass data being edited
                i.putExtra(KEY_ITEM_TEXT, items.get(pos));
                i.putExtra(KEY_ITEM_POSITION, pos);
                // display the activity
                startActivityForResult(i, EDIT_TEXT_CODE);
            }
        };

        listener = new ItemsAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClick(int pos) {
                //Delete Item from model
                items.remove(pos);
                //notify adapter
                itemsAdapter.notifyItemRemoved(pos);
                saveItems();
                Toast.makeText(getApplicationContext(), "Item was removed!", Toast.LENGTH_SHORT).show();
            }
        };
        itemsAdapter = new ItemsAdapter(items, listener, clickListener);
        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String todoItem = etItem.getText().toString();
                //add item to model
                items.add(todoItem);
                //notify adapter that item was added
                itemsAdapter.notifyItemInserted(items.size()-1);
                etItem.setText("");
                saveItems();
                Toast.makeText(getApplicationContext(), "Item was added!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private File getDataFile(){
        return new File(getFilesDir(), "data.txt");
    }

    // Load items by reading lines of data.txt
    private void loadItems(){
        try {
            items = FileUtils.readLines(getDataFile());
        } catch(IOException e){
            Log.e("MainActivity", "Error reading items", e);
            items = new ArrayList<>();
        }
    }
    // Writes items by saving them to data file
    private void saveItems(){
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing items", e);
        }
    }
}