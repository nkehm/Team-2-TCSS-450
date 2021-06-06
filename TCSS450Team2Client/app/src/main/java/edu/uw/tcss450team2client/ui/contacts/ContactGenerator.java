package edu.uw.tcss450team2client.ui.contacts;

import java.util.Arrays;
import java.util.List;

/**
 * Simple class to construct a list of Contact object
 *
 * @author Caleb Chang
 * @version 05/2021
 */
public class ContactGenerator {
    private static final Contact[] CONTACTS;
    public static final int COUNT = 10;

    static {
        CONTACTS = new Contact[COUNT];
        for (int i = 5; i < CONTACTS.length; i++) {
            CONTACTS[i] = new Contact("fname", "lname", "email", "username", i);
        }
        CONTACTS[0] = new Contact("Nathan", "Stickler", "nstick@fake.com", "nathan", 0);
        CONTACTS[1] = new Contact("Nam", "Hong", "nstick@fake.com", "nam",1);
        CONTACTS[2] = new Contact("Caleb", "Chang", "nstick@fake.com", "caleb",2);
        CONTACTS[3] = new Contact("Noah", "Kehm", "nstick@fake.com", "noah",3);
        CONTACTS[4] = new Contact("Daniel", "Ty", "nstick@fake.com", "Daniel",4);
    }

    public static List<Contact> getContactList() {
        return Arrays.asList(CONTACTS);
    }

    public static Contact[] getCONTACTS() {
        return Arrays.copyOf(CONTACTS, CONTACTS.length);
    }

    public ContactGenerator() { }
}
