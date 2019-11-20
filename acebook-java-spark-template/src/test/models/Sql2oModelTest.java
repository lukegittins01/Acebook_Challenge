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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Sql2oModelTest {

    Sql2o sql2o = new Sql2o("jdbc:postgresql://localhost:5432/" + "acebook_test",
            null, null, new PostgresQuirks() {
        {
            // make sure we use default UUID converter.
            converters.put(UUID.class, new UUIDConverter());
        }
    });

    UUID id = UUID.fromString("49921d6e-e210-4f68-ad7a-afac266278cb");
    UUID id1 = UUID.fromString("49921d6e-e210-4f68-ad7a-afac266278c1");

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

        conn.createQuery("insert into users(username, full_name, password) VALUES (:username, :full_name, :password)")
                .addParameter("username", "example username")
                .addParameter("full_name", "example full name")
                .addParameter("password", "example password")
                .executeUpdate();

        conn.createQuery("insert into dates(id, datecreated) VALUES (:id, :datecreated)")
                .addParameter("id", id.toString())
                .addParameter("datecreated", "Wed Nov 20 10:37:43 GMT 2019")
                .executeUpdate();
        conn.commit();
    }

    @AfterEach
    void tearDown() {
        Connection conn = sql2o.beginTransaction();
        conn.createQuery("TRUNCATE TABLE posts, users, dates")
                .executeUpdate();
        conn.commit();
    }

    @Test
    void createPost() {
        Model model = new Sql2oModel(sql2o);
        model.createPost( "example title", "example content");
        boolean result = false;
        System.out.println(model.getAllPosts().toString());
        String test = "title=example title, content=example content";
        if(model.getAllPosts().toString().contains(test)){
            result = true;
        }
        assertEquals(true, result);
    }

    @Test
    void createUser() {
        Connection conn = sql2o.open();
        Model model = new Sql2oModel(sql2o);
        boolean result = false;
        model.createUser("example username2", "example full name2", "example password2");
        List<Users> list_of_users;
        list_of_users = (conn.createQuery("select * from users").executeAndFetch(Users.class));
        String test = "Users(username=example username2, full_name=example full name2, password=example password2)";

        if(list_of_users.toString().contains(test)){
            result = true;
        } else {
            result = false;
        }
        assertTrue(result);
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
        Model model = new Sql2oModel(sql2o);
        assertEquals(true, model.UsernameExist("example username"));
    }

    @Test
    void CorrectPassword(){
        Model model = new Sql2oModel(sql2o);
        assertEquals(true, model.CorrectPassword("example username","example password"));
    }

    @Test
    void AddTimeStamp(){
        Model model = new Sql2oModel(sql2o);
        model.SetDate(id.toString(), "Wed Nov 20 10:37:43 GMT 2019");
        assertEquals(true, model.SetDate(id.toString(), "Wed Nov 20 10:37:43 GMT 2019"));
    }
}