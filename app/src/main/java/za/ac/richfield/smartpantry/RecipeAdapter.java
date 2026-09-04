package za.ac.richfield.smartpantry;

import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.*;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.VH> {
    public interface Listener { void click(Recipe r); }
    private ArrayList<Recipe> data; private Listener listener;
    public RecipeAdapter(ArrayList<Recipe> d,Listener l){data=d;listener=l;}
    @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup p,int v){return new VH(LayoutInflater.from(p.getContext()).inflate(R.layout.item_recipe,p,false));}
    @Override public void onBindViewHolder(@NonNull VH h,int pos){Recipe r=data.get(pos);h.name.setText(r.getName());h.info.setText("Tap to view ingredients and method");h.itemView.setOnClickListener(v->listener.click(r));}
    @Override public int getItemCount(){return data.size();}
    static class VH extends RecyclerView.ViewHolder{TextView name,info;VH(View v){super(v);name=v.findViewById(R.id.tvRecipeName);info=v.findViewById(R.id.tvRecipeInfo);}}
}
