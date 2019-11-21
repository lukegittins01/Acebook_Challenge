package models;


import java.util.List;
import java.util.UUID;

public interface Model {
    UUID createPost(String title, String content, String datecreated);
    List getAllPosts();
    boolean UsernameExist(String example_username);
    boolean CorrectPassword(String username, String example_password);
    void createUser(String example_username, String example_full_name, String example_password);

//    boolean SetDate(String id, String datecreated);
//
//    List getAllDates();
}





