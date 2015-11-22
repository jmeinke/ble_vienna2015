package com.devfest15.blevienna;


import java.util.List;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Class description
 */
public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.PersonViewHolder>{
    List<Person> persons;

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
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder holder, int position) {
        String name = "Unspecified";
        if (persons.get(position).getName() != null) name = persons.get(position).getName();
        holder.personName.setText(name);

        String accountName = "Unspecified";
        if (persons.get(position).getAccountName() != null) accountName = persons.get(position).getAccountName();
        holder.personAccount.setText(String.format("Account: %s", accountName));


        holder.personMacAddress.setText(String.format("Address: %s", persons.get(position).getMacAddress()));
        holder.personSignal.setText(String.format("Signal: %d", persons.get(position).getLastSignalStrength()));
        holder.personLastSeen.setText(String.format("Last seen: %s", persons.get(position).getLastSeen().toString()));
        // holder.personPhoto.setImageResource(persons.get(position).);
    }

    @Override
    public int getItemCount() {
        return persons.size();
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
        this.notifyDataSetChanged();
    }


    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView personName;
        TextView personAccount;
        TextView personMacAddress;
        TextView personSignal;
        TextView personLastSeen;
        ImageView personPhoto;

        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            personName = (TextView)itemView.findViewById(R.id.person_name);
            personAccount = (TextView)itemView.findViewById(R.id.person_account);
            personMacAddress = (TextView)itemView.findViewById(R.id.person_mac_address);
            personSignal = (TextView)itemView.findViewById(R.id.person_signal);
            personLastSeen = (TextView)itemView.findViewById(R.id.person_last_seen);
            personPhoto = (ImageView)itemView.findViewById(R.id.person_photo);
        }
    }

}