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
            List<Post> acebookItems = conn.createQuery("select * from posts")
                    .executeAndFetch(Post.class);
            return acebookItems;
        }
        //TODO - implement this
    }

}