package za.ac.richfield.smartpantry;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class RecipeDetailActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle b){super.onCreate(b);setContentView(R.layout.activity_recipe_detail);
        DatabaseHelper db=new DatabaseHelper(this);long id=getIntent().getLongExtra("recipeId",-1);Recipe r=db.getRecipe(id);
        TextView name=findViewById(R.id.tvRecipeName), ing=findViewById(R.id.tvIngredients), method=findViewById(R.id.tvMethod);
        if(r!=null){name.setText(r.getName());StringBuilder s=new StringBuilder("INGREDIENTS\n");for(String x:db.getRecipeIngredients(id))s.append("• ").append(x).append("\n");ing.setText(s.toString());method.setText("METHOD\n"+r.getMethod());}
        findViewById(R.id.btnBack).setOnClickListener(v->finish());
    }
}
