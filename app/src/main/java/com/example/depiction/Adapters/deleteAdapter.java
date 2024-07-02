package com.example.depiction.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.depiction.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class deleteAdapter  extends RecyclerView.Adapter<deleteAdapter.deleteViewHolder>{

    public ArrayList<String> list;
    public ArrayList<String> name;
    StorageReference storageReference;
    private Context context;

    public  String username;

    public deleteAdapter(ArrayList<String> list,String username,ArrayList<String> name , Context context) {
        this.list = list;
        this.context = context;
        this.username=username;
        this.name=name;
    }

    @NonNull
    @Override
    public deleteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.custom_image_layout,parent,false);

        return new deleteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull deleteViewHolder holder, int position) {

        try{
            Glide.with(context).load(list.get(position)).into(holder.imageView);}
        catch (Exception e){e.printStackTrace();}

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Do you want to delete this wallpaper?");
                builder.setTitle("Alert !");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
                    int clickedPos = holder.getAdapterPosition();

//                    String listitem=list.get(clickedPos);
                    String listitemforfireStore=name.get(clickedPos);


                    FirebaseDatabase database = FirebaseDatabase.getInstance("https://depiction-d1b54-default-rtdb.asia-southeast1.firebasedatabase.app/");
                    DatabaseReference myRef = database.getReference().child("Users").child(username).child("imageslinks").child(listitemforfireStore);

                    myRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(context, "Image Deleted", Toast.LENGTH_SHORT).show();
                        }
                    });

                    username=username.replace('_','.');
                    storageReference= FirebaseStorage.getInstance().getReference().child(username).child(listitemforfireStore);
                    storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(context, "Item Deleted Permanenetly", Toast.LENGTH_SHORT).show();
                        }
                    });

                });

                // Set the Negative button with No name Lambda OnClickListener method is use of DialogInterface interface.
                builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
                    dialog.cancel();
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });



    }

    @Override
    public int getItemCount() {return list.size();}

    public class deleteViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public deleteViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView=itemView.findViewById(R.id.item_image);
        }
    }
}
