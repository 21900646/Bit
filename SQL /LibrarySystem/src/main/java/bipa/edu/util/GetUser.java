package bipa.edu.util;

public class GetUser {
    private static GetUser getUser;
    private static String id;
    private GetUser() {}

    public static GetUser getInstance() {
        if(getUser == null){
            getUser = new GetUser();
        }
        return getUser;
    }

    public static String getId() {
        return id;
    }

    public static void setId(String id) {
        GetUser.id = id;
    }
}
