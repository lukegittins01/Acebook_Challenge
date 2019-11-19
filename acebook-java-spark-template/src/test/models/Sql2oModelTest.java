package models;

import org.apache.log4j.BasicConfigurator;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.converters.UUIDConverter;
import org.sql2o.quirks.PostgresQuirks;
import org.w3c.dom.ls.LSOutput;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class Sql2oModelTest {

    Sql2o sql2o = new Sql2o("jdbc:postgresql://localhost:5432/" + "acebook_test",
            null, null, new PostgresQuirks() {
        {
            // make sure we use default UUID converter.
            converters.put(UUID.class, new UUIDConverter());
        }
    });

    UUID id = UUID.fromString("49921d6e-e210-4f68-ad7a-afac266278cb");

    @BeforeAll
    static void setUpClass() {
        BasicConfigurator.configure();
        Flyway flyway = Flyway.configure().dataSource("jdbc:postgresql://localhost:5432/acebook_test", null, null).load();
        flyway.migrate();

    }
    @BeforeEach
    void setUp() {
        Connection conn = sql2o.beginTransaction();
        conn.createQuery("insert into posts(post_id, title, content) VALUES (:post_id, :title, :content)")
                .addParameter("post_id", id)
                .addParameter("title", "example title")
                .addParameter("content", "example content")
                .executeUpdate();

//        conn.createQuery("insert into users(user_id, username, full_name, password) VALUES (:user_id, :username, :full_name, :password)")
//                .addParameter("user_id", id)
//                .addParameter("username", "example username")
//                .addParameter("full_name", "example full name")
//                .addParameter("password", "example password")
//                .executeUpdate();
        conn.commit();
    }

    @AfterEach
    void tearDown() {
        Connection conn = sql2o.beginTransaction();
        conn.createQuery("TRUNCATE TABLE posts")
                .executeUpdate();
        conn.createQuery("TRUNCATE TABLE users")
                .executeUpdate();
        conn.commit();
    }

    @Test
    void createPost() {
        Connection conn = sql2o.beginTransaction();
        conn.createQuery("insert into posts(post_id, title, content) VALUES (:post_id, :title, :content)")
                .addParameter("post_id", id)
                .addParameter("title", "example title")
                .addParameter("content", "example content")
                .executeUpdate();

        conn.commit();
        Model model = new Sql2oModel(sql2o);
        List<Post> acebookItems =  new ArrayList<Post>();
        acebookItems.add(new Post(id, "example title", "example content"));
        acebookItems.add(new Post(id, "example title", "example content"));
        assertEquals(model.getAllPosts(), acebookItems);
    }

    @Test
    void getAllPosts() {
        Model model = new Sql2oModel(sql2o);
        List<Post> acebookItems =  new ArrayList<Post>();
        acebookItems.add(new Post(id, "example title", "example content"));
        assertEquals(model.getAllPosts(), acebookItems);
    }
    @Test
    void UsernameExist() {
        Connection conn = sql2o.beginTransaction();
        conn.createQuery("insert into users(user_id, username, full_name, password) VALUES (:user_id, :username, :full_name, :password)")
                .addParameter("user_id", id)
                .addParameter("username", "example username")
                .addParameter("full_name", "example full name")
                .addParameter("password", "example password")
                .executeUpdate();
        conn.commit();
        Model model = new Sql2oModel(sql2o);
        assertEquals(true, model.UsernameExist("example username"));
    }
    @Test
    void getUserId(){
        Connection conn = sql2o.beginTransaction();
        conn.createQuery("insert into posts(post_id, title, content) VALUES (:post_id, :title, :content)")
                .addParameter("post_id", id)
                .addParameter("title", "example title")
                .addParameter("content", "example content")
                .executeUpdate();

        conn.commit();
        Model model = new Sql2oModel(sql2o);

        List<Users> user = conn.createQuery("select user_id from users where username=:username and password=:password")
                    .addParameter("username", "example username")
                    .addParameter("password", "example password")
                    .executeAndFetch(Users.class);

        assertEquals(user, model.getUserId("example username", "example password"));

    }
}