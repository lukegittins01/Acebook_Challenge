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
    public UUID createPost(String title, String content, String datecreated, String usercreated) {
        try (Connection conn = sql2o.beginTransaction()) {
            UUID postUuid = UUID.randomUUID();
            int numberoflikes = 0;
            conn.createQuery("insert into posts(post_id, title, content, datecreated, usercreated, numberoflikes) VALUES (:post_id, :title, :content, :datecreated, :usercreated, :numberoflikes)")
                    .addParameter("post_id", postUuid)
                    .addParameter("title", title)
                    .addParameter("content", content)
                    .addParameter("datecreated", datecreated)
                    .addParameter("usercreated", usercreated)
                    .addParameter("numberoflikes", numberoflikes)
                    .executeUpdate();
            conn.commit();
            return postUuid;
            //TODO - implement this
        }
    }

    @Override
    public List<Post> getAllPosts() {
        try (Connection conn = sql2o.open()) {
            List<Post> acebookItems = conn.createQuery("SELECT * FROM posts ORDER BY datecreated DESC LIMIT 300 OFFSET 0;")
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
            if (user.contains(example_username)) {
                does_username_exists = true;
            }
        }
        return does_username_exists;
    }


    public boolean CorrectPassword(String username, String password) {
        boolean correct_password = false;

        try (Connection conn = sql2o.open()) {
            List<Users> user = conn.createQuery("select password from users where username=:username")
                    .addParameter("username", username)
                    .executeAndFetch(Users.class);
            password = "[Users(username=null, full_name=null, password=" + password + ")]";
            if (user.toString().equals(password)) {
                correct_password = true;
            }
        }
        return correct_password;
    }

    @Override
    public void createUser(String username, String full_name, String password) {
        try (Connection conn = sql2o.beginTransaction()) {
            conn.createQuery("insert into users(username, full_name, password) VALUES (:username, :full_name, :password)")
                    .addParameter("username", username)
                    .addParameter("full_name", full_name)
                    .addParameter("password", password)
                    .executeUpdate();
            conn.commit();

        }
    }

    @Override
    public void likePost(UUID post_id) {
        try (Connection conn = sql2o.beginTransaction()) {
//            Select the post which you want to add the like to, using the post_id
            conn.createQuery("update posts set numberoflikes= numberoflikes+1 WHERE post_id=:post_id")
                    .addParameter("post_id", post_id.toString())
//                    .addParameter("numberoflikes")
                    .executeUpdate();
            conn.commit();
//            String increasedNumberOfLikes = "[Post(post_id=49921d6e-e210-4f68-ad7a-afac266278cb, title=example title, content=example content, datecreated=Wed Nov 20 10:37:43 GMT 2019, usercreated=example user, numberoflikes=" + 1 + ")]";
        }
    }
}

