package za.ac.richfield.smartpantry;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class AddEditIngredientActivity extends AppCompatActivity {
    DatabaseHelper db; long id=-1; EditText name,qty,unit,expiry; TextView title;
    @Override protected void onCreate(Bundle b){super.onCreate(b);setContentView(R.layout.activity_add_edit);db=new DatabaseHelper(this);
        title=findViewById(R.id.tvFormTitle);name=findViewById(R.id.etName);qty=findViewById(R.id.etQuantity);unit=findViewById(R.id.etUnit);expiry=findViewById(R.id.etExpiry);
        id=getIntent().getLongExtra("id",-1);
        if(id!=-1){title.setText("Edit Ingredient");PantryItem x=db.getPantryItem(id);if(x!=null){name.setText(x.getName());qty.setText(String.valueOf(x.getQuantity()));unit.setText(x.getUnit());expiry.setText(x.getExpiryDate());}}
        findViewById(R.id.btnSave).setOnClickListener(v->save());
        findViewById(R.id.btnCancel).setOnClickListener(v->finish());
    }
    private void save(){
        String n=name.getText().toString().trim(), q=qty.getText().toString().trim(), u=unit.getText().toString().trim(), e=expiry.getText().toString().trim();
        if(n.isEmpty()){name.setError("Ingredient name is required");return;}
        if(q.isEmpty()){qty.setError("Quantity is required");return;}
        if(u.isEmpty()){unit.setError("Unit is required");return;}
        double number;
        try{number=Double.parseDouble(q);}catch(Exception ex){qty.setError("Enter a valid number");return;}
        if(number<=0){qty.setError("Quantity must be greater than 0");return;}
        if(id==-1)db.insertPantry(n,number,u,e);else db.updatePantry(id,n,number,u,e);
        Toast.makeText(this,"Ingredient saved",Toast.LENGTH_SHORT).show();finish();
    }
}
