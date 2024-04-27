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
public class UserRecordsAdapter extends RecyclerView.Adapter<UserRecordsAdapter.ViewHolder> {

    EditContact parentActivity;

    public interface EditContact
    {
        public void onDeleteUser(int index);
        public void onUpdateUser(int index, String []values);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.single_user, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UserRecordsAdapter.ViewHolder holder, int position) {
        holder.tvName.setText(RecordsList.get(position).getUsername());
        holder.tvPass.setText(RecordsList.get(position).getPassword());
        holder.ivDelete.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(holder.itemView.getContext());
                alertDialog.setMessage("Do you really want to delete?");
                alertDialog.setTitle("Delete Notification");

                alertDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UserRecordsDB database = new UserRecordsDB(context);
                        database.open();
                        database.deleteContact(RecordsList.get(holder.getAdapterPosition()).getId());
                        database.close();

                        RecycleBin RecycleDB = new RecycleBin(context);
                        RecycleDB.open();
                        RecycleDB.insert(RecordsList.get(holder.getAdapterPosition()).getUsername(),RecordsList.get(holder.getAdapterPosition()).getPassword());
                        RecycleDB.close();

                        parentActivity.onDeleteUser(holder.getAdapterPosition());
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
        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog editDialog = new AlertDialog.Builder(context).create();
                editDialog.setTitle("Edit Contact");
                View view = LayoutInflater.from(context).inflate(R.layout.edit_user_layout, null,false);
                editDialog.setView(view);
                editDialog.show();

                // hooks for dialog view
                EditText etName, etPhone;
                Button btnEditContact, btnCancel;
                etName = view.findViewById(R.id.etName);
                etPhone = view.findViewById(R.id.etPhone);
                btnEditContact = view.findViewById(R.id.btnUpdate);
                btnCancel = view.findViewById(R.id.btnCancel);

                etName.setText(RecordsList.get(holder.getAdapterPosition()).getUsername());
                etPhone.setText(RecordsList.get(holder.getAdapterPosition()).getPassword());

                btnEditContact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UserRecordsDB database = new UserRecordsDB(context);
                        database.open();
                        database.updateContact(RecordsList.get(holder.getAdapterPosition()).getId(),
                                etName.getText().toString().trim(),
                                etPhone.getText().toString().trim());
                        database.close();
                        String []updateContact = new String[]{etName.getText().toString().trim(),
                                etPhone.getText().toString().trim()};
                        parentActivity.onUpdateUser(holder.getAdapterPosition(), updateContact);
                        editDialog.dismiss();
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editDialog.dismiss();
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return RecordsList.size();
    }

    ArrayList<UserRecord> RecordsList;
    Context context;
    public UserRecordsAdapter(Context c, ArrayList<UserRecord> Records)
    {
        context = c;
        RecordsList = Records;
        parentActivity = (EditContact) c;
    }
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvName, tvPass;
        ImageView ivEdit;
        ImageView ivDelete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvPass = itemView.findViewById(R.id.tvPass);
            ivEdit = itemView.findViewById(R.id.ivEdit);
            ivDelete = itemView.findViewById(R.id.ivDelete);
        }
    }


}

