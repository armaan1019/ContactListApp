package edu.lakeland.mycontactlist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

public class ContactDataSource {
    private static final String TAG = "ContactDataSource";
    private SQLiteDatabase database;
    private ContactDBHelper dbHelper;

    public ContactDataSource(Context context) {
        dbHelper = new ContactDBHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void refreshData()
    {
        Log.d(TAG, "refreshData: Start");
        ArrayList<Contact> contacts = new ArrayList<Contact>();

        database.delete("contact", null, null);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2002);
        calendar.set(Calendar.MONTH, Calendar.SEPTEMBER); // Note: Months are 0-based, so September is 8
        calendar.set(Calendar.DAY_OF_MONTH, 25);

        contacts.add(new Contact("Brian Foote",
                "123 Main St.",
                "Oshkosh",
                "WI",
                "54901",
                "9201112222",
                "9202223333",
                "footeb@lakeland.edu",
                calendar));

        calendar.set(Calendar.YEAR, 1996);
        calendar.set(Calendar.MONTH, Calendar.DECEMBER); // Note: Months are 0-based, so September is 8
        calendar.set(Calendar.DAY_OF_MONTH, 15);

        contacts.add(new Contact("Han Solo",
                "234 Jones St.",
                "Sheboygan",
                "WI",
                "53081",
                "9203334444",
                "9204445555",
                "soloh@lakeland.edu",
                calendar));

        calendar.set(Calendar.YEAR, 1941);
        calendar.set(Calendar.MONTH, Calendar.DECEMBER); // Note: Months are 0-based, so September is 8
        calendar.set(Calendar.DAY_OF_MONTH, 7);

        contacts.add(new Contact("Leia Organa",
                "234 Jones St.",
                "Sheboygan",
                "WI",
                "53081",
                "9203334545",
                "9204445656",
                "organal@lakeland.edu",
                calendar));

        // Delete and reinsert all the teams
        long results = 0;
        for(Contact contact : contacts){
            results += insertContact(contact);
        }
        Log.d(TAG, "refreshData: End: " + results + " rows...");
    }

    public ArrayList<String> getContactName() {
        ArrayList<String> contactNames = new ArrayList<>();
        try {
            String query = "Select contactname from contact";
            Cursor cursor = database.rawQuery(query, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                contactNames.add(cursor.getString(0));
                cursor.moveToNext();
            }
            cursor.close();
        }
        catch (Exception e) {
            contactNames = new ArrayList<String>();
        }
        return contactNames;
    }

    public long insertContact(Contact c) {
        long rowsAffected = 0;
        try {
            ContentValues initialValues = new ContentValues();

            initialValues.put("contactname", c.getContactName());
            initialValues.put("streetaddress", c.getStreetAddress());
            initialValues.put("city", c.getCity());
            initialValues.put("state", c.getState());
            initialValues.put("zipcode", c.getZipCode());
            initialValues.put("phonenumber", c.getPhoneNumber());
            initialValues.put("cellnumber", c.getCellNumber());
            initialValues.put("email", c.getEMail());
            initialValues.put("birthday",String.valueOf(c.getBirthday().getTimeInMillis()));

            // Take the bitmap photo and put it into blob storage
            if (c.getPicture() != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                c.getPicture().compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] photo = baos.toByteArray();
                initialValues.put("contactphoto", photo);
            }

            rowsAffected = database.insert("contact", null, initialValues);
        }
        catch (Exception e) {
            Log.e(TAG, "insertContact: " + e.getMessage());
            //Do nothing -will return false if there is an exception
        }
        return rowsAffected;
    }

    public long updateContact(Contact c) {
        long rowsAffected = 0;
        try {
            Long rowId = (long) c.getContactID();
            ContentValues updateValues = new ContentValues();

            updateValues.put("contactname", c.getContactName());
            updateValues.put("streetaddress", c.getStreetAddress());
            updateValues.put("city", c.getCity());
            updateValues.put("state", c.getState());
            updateValues.put("zipcode", c.getZipCode());
            updateValues.put("phonenumber", c.getPhoneNumber());
            updateValues.put("cellnumber", c.getCellNumber());
            updateValues.put("email", c.getEMail());
            updateValues.put("birthday",
                    String.valueOf(c.getBirthday().getTimeInMillis()));

            if (c.getPicture() != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                c.getPicture().compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] photo = baos.toByteArray();
                updateValues.put("contactphoto", photo);
            }

            rowsAffected = database.update("contact", updateValues, "_id=" + rowId, null);
        }
        catch (Exception e) {
            Log.e(TAG, "UpdateContact: " + e.getMessage());
            //Do nothing -will return false if there is an exception
        }
        return rowsAffected;
    }

    public int getLastContactId() {
        int lastId;
        try {
            String query = "Select MAX(_id) from contact";
            Cursor cursor = database.rawQuery(query, null);

            cursor.moveToFirst();
            lastId = cursor.getInt(0);
            cursor.close();
        }
        catch (Exception e) {
            lastId = -1;
        }
        return lastId;
    }

    public ArrayList<Contact> getContacts(String sortField, String sortOrder) {
        ArrayList<Contact> contacts = new ArrayList<Contact>();
        try {
            String query = "SELECT  * FROM contact ORDER BY " + sortField + " " + sortOrder;
            Cursor cursor = database.rawQuery(query, null);

            Contact newContact;
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                newContact = new Contact();
                newContact.setContactID(cursor.getInt(0));
                newContact.setContactName(cursor.getString(1));
                newContact.setStreetAddress(cursor.getString(2));
                newContact.setCity(cursor.getString(3));
                newContact.setState(cursor.getString(4));
                newContact.setZipCode(cursor.getString(5));
                newContact.setPhoneNumber(cursor.getString(6));
                newContact.setCellNumber(cursor.getString(7));
                newContact.setEMail(cursor.getString(8));
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(Long.valueOf(cursor.getString(9)));
                newContact.setBirthday(calendar);
                contacts.add(newContact);
                cursor.moveToNext();
            }
            cursor.close();
        }
        catch (Exception e) {
            contacts = new ArrayList<Contact>();
        }
        return contacts;
    }

    public Contact getSpecificContact(int contactId) {
        Contact contact = new Contact();
        String query = "SELECT  * FROM contact WHERE _id =" + contactId;
        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            contact.setContactID(cursor.getInt(0));
            contact.setContactName(cursor.getString(1));
            contact.setStreetAddress(cursor.getString(2));
            contact.setCity(cursor.getString(3));
            contact.setState(cursor.getString(4));
            contact.setZipCode(cursor.getString(5));
            contact.setPhoneNumber(cursor.getString(6));
            contact.setCellNumber(cursor.getString(7));
            contact.setEMail(cursor.getString(8));
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.valueOf(cursor.getString(9)));
            contact.setBirthday(calendar);

            byte[] photo = cursor.getBlob(10);
            if (photo != null) {
                ByteArrayInputStream imageStream = new ByteArrayInputStream(photo);
                Bitmap thePicture= BitmapFactory.decodeStream(imageStream);
                contact.setPicture(thePicture);
            }

            cursor.close();
        }
        return contact;
    }

    public boolean deleteContact(int contactId) {
        boolean didDelete = false;
        try {
            didDelete = database.delete("contact", "_id=" + contactId, null) > 0;
        }
        catch (Exception e) {
            //Do nothing -return value already set to false
        }
        return didDelete;
    }
}
