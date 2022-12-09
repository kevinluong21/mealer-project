//code for searching and displaying search results from https://www.geeksforgeeks.org/how-to-implement-android-searchview-with-example/

package com.seg2105.mealer_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

public class Search extends AppCompatActivity {

    ArrayList<Meal> meals;
    ListView listSearchResults;
    SearchResultAdapter adapter;
    String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //pass the category name to filter the search results
        this.query = (String) getIntent().getSerializableExtra("query");

        //sets the toolbar for this activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.icons8_chevron_left_24);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        meals = UserWelcome.meals; //list of all meals in the system (by a non-suspended cook)
        listSearchResults = (ListView) findViewById(R.id.listSearchResults);
        adapter = new SearchResultAdapter(this, meals);
        listSearchResults.setAdapter(adapter);

        listSearchResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Search.this, MealPage.class);
                intent.putExtra("meal", meals.get(i));
                startActivity(intent);
            }
        });

        SearchView searchBar = (SearchView) findViewById(R.id.searchBar);

        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                adapter.getFilter().filter(query);
                return false;
            }
        });

        if (query != null) { //if a category was passed, filter the results using the category by default
            searchBar.setQuery(query, true);
        }
    }

    //create toolbar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //different toolbars based on the logged in user
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //toolbar menu select action
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}