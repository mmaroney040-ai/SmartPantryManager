package za.ac.richfield.smartpantry;

import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.*;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.VH> {
    public interface Listener { void edit(PantryItem x); void delete(PantryItem x); }
    private ArrayList<PantryItem> data; private Listener listener;
    public PantryAdapter(ArrayList<PantryItem> data, Listener l){this.data=data;listener=l;}
    public void setData(ArrayList<PantryItem> d){data=d;notifyDataSetChanged();}
    @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup p,int v){return new VH(LayoutInflater.from(p.getContext()).inflate(R.layout.item_pantry,p,false));}
    @Override public void onBindViewHolder(@NonNull VH h,int pos){
        PantryItem x=data.get(pos); h.name.setText(x.getName()); h.qty.setText("Quantity: "+x.getQuantity()+" "+x.getUnit());
        h.expiry.setText(x.getExpiryDate()==null||x.getExpiryDate().isEmpty()?"No expiry date":"Expiry: "+x.getExpiryDate());
        h.edit.setOnClickListener(v->listener.edit(x)); h.del.setOnClickListener(v->listener.delete(x));
    }
    @Override public int getItemCount(){return data.size();}
    static class VH extends RecyclerView.ViewHolder{
        TextView name,qty,expiry; Button edit,del;
        VH(View v){super(v);name=v.findViewById(R.id.tvName);qty=v.findViewById(R.id.tvQty);expiry=v.findViewById(R.id.tvExpiry);edit=v.findViewById(R.id.btnEdit);del=v.findViewById(R.id.btnDelete);}
    }
}
