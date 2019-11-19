package models;

import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.List;
import java.util.UUID;

public class Sql2oModel implements Model {

    private Sql2o sql2o;

    public Sql2oModel(Sql2o sql2o) {
        this.sql2o = sql2o;

    }

    @Override
    public UUID createPost(String title, String content) {
        try (Connection conn = sql2o.beginTransaction()) {
            UUID postUuid = UUID.randomUUID();
            conn.createQuery("insert into posts(post_id, title, content) VALUES (:post_id, :title, :content)")
                    .addParameter("post_id", postUuid)
                    .addParameter("title", title)
                    .addParameter("content", content)
                    .executeUpdate();
            conn.commit();
            return postUuid;
            //TODO - implement this
        }
    }

    @Override
    public List<Post> getAllPosts() {
        try (Connection conn = sql2o.open()) {
            List<Post> acebookItems = conn.createQuery("select * from posts ORDER BY post_id")
                    .executeAndFetch(Post.class);
            return acebookItems;
        }
    }

    @Override
    public boolean UsernameExist(String example_username) {
        boolean does_username_exists = false;
        try (Connection conn = sql2o.open()) {
            List<Users> user1 = conn.createQuery("select username from users")
                    .executeAndFetch(Users.class);
            String user = user1.toString();
            if(user.contains(example_username)){

                does_username_exists = true;
            }
        }
        System.out.println(does_username_exists);
        return does_username_exists;
    }



    @Override
    public List<Users> getUserId(String username, String password) {
        List<Users> user;
        try (Connection conn = sql2o.open()) {
            user = conn.createQuery("select user_id from users where username=:username and password=:password")
                    .addParameter("username", username)
                    .addParameter("password", password)
                    .executeAndFetch(Users.class);


        }

        return user;
    }

//    public boolean CorrectPassword(String user_id) {
////        boolean correct_password = false;
////        try (Connection conn = sql2o.open()) {
////            List<Users> user = conn.createQuery("select password from users where user_id=:user_id")
////                    .addParameter("user_id", user_id)
////                    .executeAndFetch(Users.class);
////
////        }
////        return Users.check_;
////    }

    @Override
    public UUID createUser(String username, String full_name, String password) {
        try (Connection conn = sql2o.beginTransaction()) {
            UUID userUuid = UUID.randomUUID();
            conn.createQuery("insert into users(user_id, username, full_name, password) VALUES (:user_id, :username, :full_name, :password)")
                    .addParameter("user_id", userUuid)
                    .addParameter("username", username)
                    .addParameter("full_name", full_name)
                    .addParameter("password", password)
                    .executeUpdate();
            conn.commit();
            return userUuid;
        }
    }
}