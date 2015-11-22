package com.devfest15.blevienna;


import java.util.List;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Class description
 */
public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.PersonViewHolder>{
    List<Person> persons;
    ItemClickListener listener;

    PersonAdapter(List<Person> persons){
        this.persons = persons;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_beacon_info, parent, false);
        final PersonViewHolder pvh = new PersonViewHolder(v);
        if (listener != null) {
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(v, pvh.getAdapterPosition());
                }
            });
        }
        return pvh;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder holder, int position) {
        Person person = persons.get(position);

        String name = "Unspecified";
        if (person.getName() != null) name = person.getName();
        holder.personName.setText(name);

        String accountName = "Unspecified";
        if (person.getAccountName() != null) accountName = person.getAccountName();
        holder.personAccount.setText(String.format("Account: %s", accountName));
        holder.personMacAddress.setText(String.format("Address: %s", person.getMacAddress()));
        holder.personLastSeen.setText(String.format("Last seen: %s", person.getLastSeen().toString()));

        // grey out if unavailable
        if (person.getLastSignalStrength() == Integer.MIN_VALUE) {
            holder.personSignal.setText("Signal: gone");
            holder.gl.setBackgroundColor(Color.argb(160, 160, 160, 160));
        } else {
            holder.personSignal.setText(String.format("Signal: %d", person.getLastSignalStrength()));
        }
    }

    @Override
    public int getItemCount() {
        return persons.size();
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
        this.notifyDataSetChanged();
    }

    public void setListener(ItemClickListener listener) {
        this.listener = listener;
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        FrameLayout gl;
        TextView personName;
        TextView personAccount;
        TextView personMacAddress;
        TextView personSignal;
        TextView personLastSeen;
        ImageView personPhoto;

        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            gl = (FrameLayout)itemView.findViewById(R.id.gl);
            personName = (TextView)itemView.findViewById(R.id.person_name);
            personAccount = (TextView)itemView.findViewById(R.id.person_account);
            personMacAddress = (TextView)itemView.findViewById(R.id.person_mac_address);
            personSignal = (TextView)itemView.findViewById(R.id.person_signal);
            personLastSeen = (TextView)itemView.findViewById(R.id.person_last_seen);
            personPhoto = (ImageView)itemView.findViewById(R.id.person_photo);
        }
    }

}