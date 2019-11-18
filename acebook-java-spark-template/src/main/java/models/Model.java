package models;


import java.sql.Date;
import java.util.List;
import java.util.UUID;

public interface Model {
    UUID createPost(String title, String content);
    UUID createUser(String username, String full_name, String password);
    List getAllPosts();


    boolean UsernameExist(String example_username);
}





