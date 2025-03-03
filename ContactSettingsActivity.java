package edu.lakeland.mycontactlist;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ContactSettingsActivity extends AppCompatActivity {
    public static final String TAG = "ContactSettingsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_contact_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ScrollView background = findViewById(R.id.background);
        RadioButton lightblue = findViewById(R.id.colorLightBlue);
        RadioButton lightgreen = findViewById(R.id.colorLightGreen);
        RadioGroup colorGroup = findViewById(R.id.radioGroupColor);

        this.setTitle(R.string.button_to_display_the_settings);
        Navbar.initListButton(this);
        Navbar.initMapButton(this);
        Navbar.initSettingsButton(this);
        initSettings();
        initSortByClick();
        initSortOrderClick();
        initColor();

        if (lightblue.isChecked()) {
            background.setBackgroundResource(R.color.optionLightBlue);
        } else if (lightgreen.isChecked()) {
            background.setBackgroundResource(R.color.optionLightGreen);
        } else {
            background.setBackgroundResource(R.color.optionLightYellow);
        }

        colorGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (lightblue.isChecked()) {
                    background.setBackgroundResource(R.color.optionLightBlue);
                } else if (lightgreen.isChecked()) {
                    background.setBackgroundResource(R.color.optionLightGreen);
                } else {
                    background.setBackgroundResource(R.color.optionLightYellow);
                }
            }
        });
    }

    private void initSortOrderClick() {
        RadioGroup rgSortOrder = findViewById(R.id.radioGroupSortOrder);
        rgSortOrder.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                RadioButton rbAscending = findViewById(R.id.radioAscending);
                if (rbAscending.isChecked()) {
                    getSharedPreferences("MyContactListPreferences",
                            Context.MODE_PRIVATE).edit().putString("sortorder", "ASC").apply();
                }
                else {
                    getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE).edit().putString("sortorder", "DESC").apply();
                }
            }
        });
    }

    private void initSortByClick() {
        RadioGroup rgSortBy = findViewById(R.id.radioGroupSortBy);
        rgSortBy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                RadioButton rbName = findViewById(R.id.radioName);
                RadioButton rbCity = findViewById(R.id.radioCity);
                if (rbName.isChecked()) {
                    getSharedPreferences("MyContactListPreferences",
                            Context.MODE_PRIVATE).edit().putString("sortfield", "contactname").apply();
                }
                else if (rbCity.isChecked()) {
                    getSharedPreferences("MyContactListPreferences",
                            Context.MODE_PRIVATE).edit().putString("sortfield", "city").apply();
                }
                else {
                    getSharedPreferences("MyContactListPreferences",
                            Context.MODE_PRIVATE).edit().putString("sortfield", "birthday").apply();
                }
            }
        });
    }

    private void initColor() {
        RadioGroup rgColor = findViewById(R.id.radioGroupColor);
        rgColor.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                RadioButton lightBlue = findViewById(R.id.colorLightBlue);
                RadioButton lightGreen = findViewById(R.id.colorLightGreen);
                if (lightBlue.isChecked()) {
                    getSharedPreferences("MyContactListPreferences",
                            Context.MODE_PRIVATE).edit().putString("Color", "lightblue").apply();
                }
                else if (lightGreen.isChecked()) {
                    getSharedPreferences("MyContactListPreferences",
                            Context.MODE_PRIVATE).edit().putString("Color", "lightgreen").apply();
                }
                else {
                    getSharedPreferences("MyContactListPreferences",
                            Context.MODE_PRIVATE).edit().putString("Color", "lightyellow").apply();
                }
            }
        });
    }

    private void initSettings() {
        String sortBy = getSharedPreferences("MyContactListPreferences",
                Context.MODE_PRIVATE).getString("sortfield","contactname");
        String sortOrder = getSharedPreferences("MyContactListPreferences",
                Context.MODE_PRIVATE).getString("sortorder","ASC");
        String color = getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE).getString("Color", "lightblue");

        RadioButton rbName = findViewById(R.id.radioName);
        RadioButton rbCity = findViewById(R.id.radioCity);
        RadioButton rbBirthDay = findViewById(R.id.radioBirthday);
        if (sortBy.equalsIgnoreCase("contactname")) {
            Log.d(TAG, "initSettings: ContactName");
            rbName.setChecked(true);
        }
        else if (sortBy.equalsIgnoreCase("city")) {
            Log.d(TAG, "initSettings: City");
            rbCity.setChecked(true);
        }
        else {
            Log.d(TAG, "initSettings: Birthday");
            rbBirthDay.setChecked(true);
        }

        RadioButton rbAscending = findViewById(R.id.radioAscending);
        RadioButton rbDescending = findViewById(R.id.radioDescending);
        if (sortOrder.equalsIgnoreCase("ASC")) {
            rbAscending.setChecked(true);
        }
        else {
            rbDescending.setChecked(true);
        }

        RadioButton lightblue = findViewById(R.id.colorLightBlue);
        RadioButton lightgreen = findViewById(R.id.colorLightGreen);
        RadioButton lightyellow = findViewById(R.id.colorLightYellow);
        if (color.equalsIgnoreCase("lightblue")) {
            lightblue.setChecked(true);
        }
        else if (color.equalsIgnoreCase("lightgreen")) {
            lightgreen.setChecked(true);
        }
        else {
            lightyellow.setChecked(true);
        }
    }
}