package edu.uw.tcss450team2client.ui.contacts;

import java.util.Arrays;
import java.util.List;

public class ContactGenerator {
    private static final Contact[] CONTACTS;
    public static final int COUNT = 20;


    static {
        CONTACTS = new Contact[COUNT];
        for (int i = 0; i < CONTACTS.length; i++) {
            CONTACTS[i] = new Contact("fname", "lname", "email", "username");
        }
    }

    public static List<Contact> getContactList() {
        return Arrays.asList(CONTACTS);
    }

    public static Contact[] getCONTACTS() {
        return Arrays.copyOf(CONTACTS, CONTACTS.length);
    }

    private ContactGenerator() { }
}
