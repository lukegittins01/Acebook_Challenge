package models;


import java.util.List;
import java.util.UUID;

public interface Model {
    UUID createPost(String title, String content);
    UUID createUser(String username, String full_name, String password);
    List getAllPosts();


    boolean UsernameExist(String example_username);

    List<Users> getUserId(String example_username, String example_password);
}





