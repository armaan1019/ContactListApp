package edu.lakeland.mycontactlist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ContactListActivity extends AppCompatActivity {
    public static final String TAG = "ContactListActivity";
    ArrayList<Contact> contacts;
    RecyclerView contactList;
    ContactAdapter contactAdapter;

    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();
            int contactId = contacts.get(position).getContactID();
            Log.d(TAG, "onClick: " + contactId);
            Intent intent = new Intent(ContactListActivity.this, MainActivity.class);
            intent.putExtra("contactId", contactId);
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_contact_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        this.setTitle(R.string.button_to_display_the_list_of_contacts);
        Navbar.initListButton(this);
        Navbar.initMapButton(this);
        initAddContactButton();
        initDeleteSwitch();
        initBatterySensor();
        Navbar.initSettingsButton(this);

        ContactDataSource ds = new ContactDataSource(this);
        try {
            ds.open();
            //ds.refreshData();
            contacts = ds.getContacts("City", "DESC");
            //contacts = ds.getContactName();
            ds.close();
            if (!contacts.isEmpty()) {
                contactList = findViewById(R.id.rvContacts);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
                contactList.setLayoutManager(layoutManager);
                contactAdapter = new ContactAdapter(contacts, this);
                contactAdapter.setOnItemClickListener(onItemClickListener);
                contactList.setAdapter(contactAdapter);
            }
            else {
                Intent intent = new Intent(ContactListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }
        catch (Exception e) {
            Toast.makeText(this, "Error retrieving contacts", Toast.LENGTH_LONG).show();
        }
    }

    private void initBatterySensor() {
        BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                double batteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL,0);
                double levelScale = intent.getIntExtra(BatteryManager.EXTRA_SCALE,0);
                int batteryPercent = (int) Math.floor(batteryLevel / levelScale * 100);
                TextView textBatteryState = findViewById(R.id.textBatteryLevel);
                textBatteryState.setText(batteryPercent + "%");
            }
        };

        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryReceiver, filter);
    }

    private void initAddContactButton() {
        Button newContact = findViewById(R.id.buttonAddContact);
        newContact.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ContactListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initDeleteSwitch() {
        Switch s = findViewById(R.id.switchDelete);
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                boolean status = compoundButton.isChecked();
                contactAdapter.setDelete(status);
                contactAdapter.notifyDataSetChanged();
            }
        });
    }
}