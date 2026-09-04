package za.ac.richfield.smartpantry;

import android.content.*;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.*;
import java.util.*;

public class SuggestedRecipesActivity extends AppCompatActivity {
    DatabaseHelper db; RecipeAdapter adapter; RecyclerView recycler; TextView noMatches;
    @Override protected void onCreate(Bundle b){super.onCreate(b);setContentView(R.layout.activity_suggested);db=new DatabaseHelper(this);
        recycler=findViewById(R.id.recyclerRecipes);noMatches=findViewById(R.id.tvNoMatches);recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter=new RecipeAdapter(new ArrayList<>(),r->{Intent i=new Intent(this,RecipeDetailActivity.class);i.putExtra("recipeId",r.getId());startActivity(i);});recycler.setAdapter(adapter);setupNav();load();
    }
    @Override protected void onResume(){super.onResume();if(db!=null)load();}
    private void load(){ArrayList<Recipe> x=db.getStrictSuggestions();adapter=new RecipeAdapter(x,r->{Intent i=new Intent(this,RecipeDetailActivity.class);i.putExtra("recipeId",r.getId());startActivity(i);});recycler.setAdapter(adapter);noMatches.setVisibility(x.isEmpty()?View.VISIBLE:View.GONE);}
    private void setupNav(){findViewById(R.id.navPantry).setOnClickListener(v->startActivity(new Intent(this,PantryActivity.class)));findViewById(R.id.navRecipes).setOnClickListener(v->{});findViewById(R.id.navSettings).setOnClickListener(v->startActivity(new Intent(this,SettingsActivity.class)));}
}
