package edu.uw.tcss450team2client.ui.contacts;

import java.util.Arrays;
import java.util.List;

public class RequestGenerator {
    private static final FriendRequest[] REQUESTS;
    public static final int COUNT = 2;


    static {
        REQUESTS = new FriendRequest[COUNT];
//        for (int i = 5; i < CONTACTS.length; i++) {
//            CONTACTS[i] = new Contact("fname", "lname", "email", "username", i);
//        }
        REQUESTS[0] = new FriendRequest("Nathan",  0);
        REQUESTS[1] = new FriendRequest("Nam", 1);
//        CONTACTS[2] = new Contact("Caleb", "Chang", "nstick@fake.com", "caleb",2);
//        CONTACTS[3] = new Contact("Noah", "Kehm", "nstick@fake.com", "noah",3);
//        CONTACTS[4] = new Contact("Daniel", "Ty", "nstick@fake.com", "Daniel",4);

    }

    public static List<FriendRequest> getRequestList() {
        return Arrays.asList(REQUESTS);
    }

    public static FriendRequest[] getREQUESTS() {
        return Arrays.copyOf(REQUESTS, REQUESTS.length);
    }

    public RequestGenerator() { }
}
