package za.ac.richfield.smartpantry;

import android.content.*;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.*;
import java.util.*;

public class PantryActivity extends AppCompatActivity {
    DatabaseHelper db; PantryAdapter adapter; RecyclerView recycler; TextView empty;
    @Override protected void onCreate(Bundle b){super.onCreate(b);setContentView(R.layout.activity_pantry);db=new DatabaseHelper(this);
        recycler=findViewById(R.id.recyclerPantry);empty=findViewById(R.id.tvEmpty);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter=new PantryAdapter(new ArrayList<>(),new PantryAdapter.Listener(){
            public void edit(PantryItem x){Intent i=new Intent(PantryActivity.this,AddEditIngredientActivity.class);i.putExtra("id",x.getId());startActivity(i);}
            public void delete(PantryItem x){new AlertDialog.Builder(PantryActivity.this).setTitle("Delete ingredient?").setMessage(x.getName()).setPositiveButton("Delete",(d, w)->{db.deletePantry(x.getId());load();}).setNegativeButton("Cancel",null).show();}
        }); recycler.setAdapter(adapter);
        findViewById(R.id.btnAdd).setOnClickListener(v->startActivity(new Intent(this,AddEditIngredientActivity.class)));
        setupNav(); load();
    }
    @Override protected void onResume(){super.onResume();if(db!=null)load();}
    private void load(){ArrayList<PantryItem> x=db.getPantry();adapter.setData(x);empty.setVisibility(x.isEmpty()?View.VISIBLE:View.GONE);}
    private void setupNav(){
        findViewById(R.id.navPantry).setOnClickListener(v->{});
        findViewById(R.id.navRecipes).setOnClickListener(v->startActivity(new Intent(this,SuggestedRecipesActivity.class)));
        findViewById(R.id.navSettings).setOnClickListener(v->startActivity(new Intent(this,SettingsActivity.class)));
    }
}
