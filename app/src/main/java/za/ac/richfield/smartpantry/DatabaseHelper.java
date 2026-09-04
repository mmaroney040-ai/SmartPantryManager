package za.ac.richfield.smartpantry;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.*;
import java.util.*;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "smart_pantry.db";
    private static final int DB_VERSION = 1;

    public DatabaseHelper(Context c) { super(c, DB_NAME, null, DB_VERSION); }

    @Override public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE pantry(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, quantity REAL NOT NULL, unit TEXT NOT NULL, expiry_date TEXT)");
        db.execSQL("CREATE TABLE recipes(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, method TEXT NOT NULL)");
        db.execSQL("CREATE TABLE recipe_ingredients(id INTEGER PRIMARY KEY AUTOINCREMENT, recipe_id INTEGER NOT NULL, ingredient_name TEXT NOT NULL, quantity REAL NOT NULL, unit TEXT NOT NULL, FOREIGN KEY(recipe_id) REFERENCES recipes(id))");
        seedRecipes(db);
    }

    @Override public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS recipe_ingredients");
        db.execSQL("DROP TABLE IF EXISTS recipes");
        db.execSQL("DROP TABLE IF EXISTS pantry");
        onCreate(db);
    }

    public long insertPantry(String name, double qty, String unit, String expiry) {
        ContentValues v=new ContentValues(); v.put("name",name); v.put("quantity",qty); v.put("unit",unit); v.put("expiry_date",expiry);
        return getWritableDatabase().insert("pantry",null,v);
    }
    public int updatePantry(long id,String name,double qty,String unit,String expiry) {
        ContentValues v=new ContentValues(); v.put("name",name); v.put("quantity",qty); v.put("unit",unit); v.put("expiry_date",expiry);
        return getWritableDatabase().update("pantry",v,"id=?",new String[]{String.valueOf(id)});
    }
    public int deletePantry(long id){ return getWritableDatabase().delete("pantry","id=?",new String[]{String.valueOf(id)}); }

    public ArrayList<PantryItem> getPantry() {
        ArrayList<PantryItem> list=new ArrayList<>();
        Cursor c=getReadableDatabase().query("pantry",null,null,null,null,null,"name ASC");
        while(c.moveToNext()) list.add(new PantryItem(c.getLong(c.getColumnIndexOrThrow("id")),c.getString(c.getColumnIndexOrThrow("name")),
            c.getDouble(c.getColumnIndexOrThrow("quantity")),c.getString(c.getColumnIndexOrThrow("unit")),
            c.getString(c.getColumnIndexOrThrow("expiry_date"))));
        c.close(); return list;
    }
    public PantryItem getPantryItem(long id){
        Cursor c=getReadableDatabase().query("pantry",null,"id=?",new String[]{String.valueOf(id)},null,null,null);
        PantryItem x=null;
        if(c.moveToFirst()) x=new PantryItem(c.getLong(c.getColumnIndexOrThrow("id")),c.getString(c.getColumnIndexOrThrow("name")),
            c.getDouble(c.getColumnIndexOrThrow("quantity")),c.getString(c.getColumnIndexOrThrow("unit")),
            c.getString(c.getColumnIndexOrThrow("expiry_date")));
        c.close(); return x;
    }

    public ArrayList<Recipe> getRecipes() {
        ArrayList<Recipe> list=new ArrayList<>();
        Cursor c=getReadableDatabase().query("recipes",null,null,null,null,null,"name ASC");
        while(c.moveToNext()) list.add(new Recipe(c.getLong(c.getColumnIndexOrThrow("id")),c.getString(c.getColumnIndexOrThrow("name")),
            c.getString(c.getColumnIndexOrThrow("method"))));
        c.close(); return list;
    }

    public ArrayList<String> getRecipeIngredients(long recipeId) {
        ArrayList<String> list=new ArrayList<>();
        Cursor c=getReadableDatabase().query("recipe_ingredients",new String[]{"ingredient_name","quantity","unit"},
            "recipe_id=?",new String[]{String.valueOf(recipeId)},null,null,"id ASC");
        while(c.moveToNext()) list.add(c.getString(0)+" - "+format(c.getDouble(1))+" "+c.getString(2));
        c.close(); return list;
    }

    private static String format(double n){ return n==Math.rint(n)?String.valueOf((int)n):String.valueOf(n); }

    // Strict matching: every required ingredient must exist and its available quantity,
    // after simple name/unit normalisation, must be at least the recipe requirement.
    public ArrayList<Recipe> getStrictSuggestions() {
        ArrayList<Recipe> result=new ArrayList<>();
        for(Recipe r:getRecipes()) if(canMake(r.getId())) result.add(r);
        return result;
    }

    private boolean canMake(long recipeId) {
        Cursor req=getReadableDatabase().query("recipe_ingredients",new String[]{"ingredient_name","quantity","unit"},
            "recipe_id=?",new String[]{String.valueOf(recipeId)},null,null,null);
        boolean ok=true;
        while(req.moveToNext()){
            String needed=req.getString(0); double neededQty=req.getDouble(1); String neededUnit=req.getString(2);
            double available=0;
            Cursor p=getReadableDatabase().query("pantry",new String[]{"name","quantity","unit"},null,null,null,null,null);
            while(p.moveToNext()){
                if(normalizeName(p.getString(0)).equals(normalizeName(needed))) {
                    double converted=convert(p.getDouble(1),p.getString(2),neededUnit);
                    if(converted>=0) available+=converted;
                }
            }
            p.close();
            if(available < neededQty){ ok=false; break; }
        }
        req.close(); return ok;
    }

    public Recipe getRecipe(long id){
        Cursor c=getReadableDatabase().query("recipes",null,"id=?",new String[]{String.valueOf(id)},null,null,null);
        Recipe r=null; if(c.moveToFirst()) r=new Recipe(c.getLong(0),c.getString(1),c.getString(2)); c.close(); return r;
    }

    public static String normalizeName(String s){
        String x=s.toLowerCase(Locale.ROOT).trim().replaceAll("[^a-z0-9 ]","");
        if(x.endsWith("ies")) x=x.substring(0,x.length()-3)+"y";
        else if(x.endsWith("es") && x.length()>3) x=x.substring(0,x.length()-2);
        else if(x.endsWith("s") && x.length()>2) x=x.substring(0,x.length()-1);
        return x;
    }

    // Returns amount in the recipe's unit. -1 means incompatible units.
    public static double convert(double qty,String from,String to){
        String f=from.toLowerCase(Locale.ROOT).trim(), t=to.toLowerCase(Locale.ROOT).trim();
        if(f.equals(t)) return qty;
        if((f.equals("g")||f.equals("gram")||f.equals("grams")) && (t.equals("kg")||t.equals("kilogram")||t.equals("kilograms"))) return qty/1000.0;
        if((f.equals("kg")||f.equals("kilogram")||f.equals("kilograms")) && (t.equals("g")||t.equals("gram")||t.equals("grams"))) return qty*1000.0;
        if((f.equals("ml")||f.equals("millilitre")||f.equals("millilitres")) && (t.equals("l")||t.equals("litre")||t.equals("litres"))) return qty/1000.0;
        if((f.equals("l")||f.equals("litre")||f.equals("litres")) && (t.equals("ml")||t.equals("millilitre")||t.equals("millilitres"))) return qty*1000.0;
        if((f.equals("pcs")||f.equals("piece")||f.equals("pieces")) && (t.equals("pcs")||t.equals("piece")||t.equals("pieces"))) return qty;
        return -1;
    }

    private void seedRecipes(SQLiteDatabase db){
        addRecipe(db,"Tomato Omelette","Beat eggs, mix with chopped tomato and onion, season, then cook in a pan.");
        addRecipe(db,"Cheese Omelette","Beat eggs, cook until nearly set, add cheese and fold.");
        addRecipe(db,"Egg Fried Rice","Stir-fry rice with egg, onion, carrot and soy sauce.");
        addRecipe(db,"Chicken Fried Rice","Stir-fry chicken, rice, egg, onion and soy sauce until hot.");
        addRecipe(db,"Pasta Pomodoro","Cook pasta and simmer tomato with garlic and olive oil; combine.");
        addRecipe(db,"Garlic Pasta","Cook pasta, sauté garlic in olive oil and toss together.");
        addRecipe(db,"Tuna Pasta","Mix cooked pasta with tuna, tomato and mayonnaise.");
        addRecipe(db,"Chicken Sandwich","Layer bread with cooked chicken, lettuce, tomato and mayonnaise.");
        addRecipe(db,"Cheese Toastie","Place cheese between bread slices and toast until golden.");
        addRecipe(db,"Vegetable Stir Fry","Stir-fry carrot, onion, pepper and soy sauce until crisp-tender.");
        addRecipe(db,"Chicken Stir Fry","Stir-fry chicken with pepper, onion and soy sauce.");
        addRecipe(db,"Tomato Soup","Cook tomato, onion and garlic, add stock, simmer and blend.");
        addRecipe(db,"Pancakes","Whisk flour, egg, milk and sugar; cook spoonfuls in a lightly oiled pan.");
        addRecipe(db,"French Toast","Dip bread in beaten egg and milk, then fry until golden.");
        addRecipe(db,"Rice and Beans","Heat cooked rice with beans, tomato and onion; season.");
        addRecipe(db,"Chicken Salad","Combine lettuce, cooked chicken, tomato and cucumber with dressing.");
        addRecipe(db,"Egg Salad Sandwich","Mix chopped boiled eggs with mayonnaise and onion; serve in bread.");
        addRecipe(db,"Loaded Baked Potato","Top cooked potato with cheese, beans and tomato.");
    }

    private void addRecipe(SQLiteDatabase db,String name,String method){
        ContentValues v=new ContentValues(); v.put("name",name); v.put("method",method);
        long id=db.insert("recipes",null,v);
        String[][] data=ingredientsFor(name);
        for(String[] a:data){
            ContentValues i=new ContentValues(); i.put("recipe_id",id); i.put("ingredient_name",a[0]); i.put("quantity",Double.parseDouble(a[1])); i.put("unit",a[2]);
            db.insert("recipe_ingredients",null,i);
        }
    }

    private String[][] ingredientsFor(String n){
        switch(n){
            case "Tomato Omelette": return new String[][]{{"eggs","2","pcs"},{"tomato","1","pcs"},{"onion","0.5","pcs"}};
            case "Cheese Omelette": return new String[][]{{"eggs","2","pcs"},{"cheese","50","g"}};
            case "Egg Fried Rice": return new String[][]{{"rice","200","g"},{"eggs","2","pcs"},{"onion","0.5","pcs"},{"carrot","1","pcs"},{"soy sauce","20","ml"}};
            case "Chicken Fried Rice": return new String[][]{{"chicken","150","g"},{"rice","200","g"},{"eggs","1","pcs"},{"onion","0.5","pcs"},{"soy sauce","20","ml"}};
            case "Pasta Pomodoro": return new String[][]{{"pasta","200","g"},{"tomato","2","pcs"},{"garlic","2","pcs"},{"olive oil","20","ml"}};
            case "Garlic Pasta": return new String[][]{{"pasta","200","g"},{"garlic","2","pcs"},{"olive oil","20","ml"}};
            case "Tuna Pasta": return new String[][]{{"pasta","200","g"},{"tuna","1","pcs"},{"tomato","1","pcs"},{"mayonnaise","30","ml"}};
            case "Chicken Sandwich": return new String[][]{{"bread","2","pcs"},{"chicken","100","g"},{"lettuce","2","pcs"},{"tomato","1","pcs"},{"mayonnaise","20","ml"}};
            case "Cheese Toastie": return new String[][]{{"bread","2","pcs"},{"cheese","50","g"}};
            case "Vegetable Stir Fry": return new String[][]{{"carrot","1","pcs"},{"onion","0.5","pcs"},{"pepper","1","pcs"},{"soy sauce","20","ml"}};
            case "Chicken Stir Fry": return new String[][]{{"chicken","150","g"},{"pepper","1","pcs"},{"onion","0.5","pcs"},{"soy sauce","20","ml"}};
            case "Tomato Soup": return new String[][]{{"tomato","3","pcs"},{"onion","1","pcs"},{"garlic","2","pcs"},{"stock","500","ml"}};
            case "Pancakes": return new String[][]{{"flour","200","g"},{"eggs","2","pcs"},{"milk","250","ml"},{"sugar","20","g"}};
            case "French Toast": return new String[][]{{"bread","2","pcs"},{"eggs","2","pcs"},{"milk","100","ml"}};
            case "Rice and Beans": return new String[][]{{"rice","200","g"},{"beans","200","g"},{"tomato","1","pcs"},{"onion","0.5","pcs"}};
            case "Chicken Salad": return new String[][]{{"lettuce","2","pcs"},{"chicken","100","g"},{"tomato","1","pcs"},{"cucumber","0.5","pcs"}};
            case "Egg Salad Sandwich": return new String[][]{{"eggs","2","pcs"},{"mayonnaise","30","ml"},{"onion","0.25","pcs"},{"bread","2","pcs"}};
            default: return new String[][]{{"potato","2","pcs"},{"cheese","50","g"},{"beans","100","g"},{"tomato","1","pcs"}};
        }
    }
}
