package edu.uw.tcss450team2client.ui.contacts;

import java.util.Arrays;
import java.util.List;

/**
 * Simple class to generate fake contact requests
 *
 * @author Caleb Chang
 * @version 05/2021
 */
public class RequestGenerator {
    private static final FriendRequest[] REQUESTS;
    public static final int COUNT = 2;


    static {
        REQUESTS = new FriendRequest[COUNT];
        REQUESTS[0] = new FriendRequest("Nathan",  0);
        REQUESTS[1] = new FriendRequest("Nam", 1);
    }

    public static List<FriendRequest> getRequestList() {
        return Arrays.asList(REQUESTS);
    }

    public static FriendRequest[] getREQUESTS() {
        return Arrays.copyOf(REQUESTS, REQUESTS.length);
    }

    public RequestGenerator() { }
}
