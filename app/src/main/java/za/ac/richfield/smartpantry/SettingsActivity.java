package za.ac.richfield.smartpantry;

import android.content.*;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle b){super.onCreate(b);setContentView(R.layout.activity_settings);
        Switch sw=findViewById(R.id.switchExpiry);sw.setChecked(getPreferences(0).getBoolean("expiry_alerts",false));
        sw.setOnCheckedChangeListener((button,checked)->getPreferences(0).edit().putBoolean("expiry_alerts",checked).apply());
        findViewById(R.id.navPantry).setOnClickListener(v->startActivity(new Intent(this,PantryActivity.class)));
        findViewById(R.id.navRecipes).setOnClickListener(v->startActivity(new Intent(this,SuggestedRecipesActivity.class)));
        findViewById(R.id.navSettings).setOnClickListener(v->{});
    }
}
