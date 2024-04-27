package com.example.assignment3;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
public class RecycleBinAdapter extends RecyclerView.Adapter<RecycleBinAdapter.ViewHolder> {

    EditContact parentActivity;

    public interface EditContact
    {
        public void onRestoreUser(int index);

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.single_user_recycle, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleBinAdapter.ViewHolder holder, int position) {
        holder.tvName.setText(RecordsList.get(position).getUsername());
        holder.tvPass.setText(RecordsList.get(position).getPassword());
        holder.ivRecycle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(holder.itemView.getContext());
                alertDialog.setMessage("Do you really want to Restore?");
                alertDialog.setTitle("Restore Notification");

                alertDialog.setPositiveButton("Restore", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RecycleBin database = new RecycleBin(context);
                        database.open();
                        database.deleteContact(RecordsList.get(holder.getAdapterPosition()).getId());
                        database.close();

                        UserRecordsDB UserDB = new UserRecordsDB(context);
                        UserDB.open();
                        UserDB.insert(RecordsList.get(holder.getAdapterPosition()).getUsername(),RecordsList.get(holder.getAdapterPosition()).getPassword());
                        UserDB.close();
                        parentActivity.onRestoreUser(holder.getAdapterPosition());
                    }
                });

                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                alertDialog.show();
                return false;
            }
        });


    }

    @Override
    public int getItemCount() {
        return RecordsList.size();
    }

    ArrayList<UserRecord> RecordsList;
    Context context;
    public RecycleBinAdapter(Context c, ArrayList<UserRecord> Records)
    {
        context = c;
        RecordsList = Records;
        parentActivity = (EditContact) c;
    }
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvName, tvPass;
        ImageView ivRecycle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvNameRecycle);
            tvPass = itemView.findViewById(R.id.tvPassRecycle);
            ivRecycle = itemView.findViewById(R.id.ivRestoreRecycle);
        }
    }


}

