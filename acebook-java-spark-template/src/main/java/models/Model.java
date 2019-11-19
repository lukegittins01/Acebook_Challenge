package models;


import java.util.List;
import java.util.UUID;

public interface Model {
    UUID createPost(String title, String content);
    List getAllPosts();
    boolean UsernameExist(String example_username);
    List<Users> getUserId(String example_username, String example_password);
    boolean CorrectPassword(String user_id, String example_password);
    UUID createUser(String example_username, String example_full_name, String example_password);
}





