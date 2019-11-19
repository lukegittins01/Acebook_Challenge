import models.Model;
import models.Sql2oModel;
import models.Users;
import org.apache.log4j.BasicConfigurator;
import org.flywaydb.core.Flyway;
import org.sql2o.Sql2o;
import org.sql2o.converters.UUIDConverter;
import org.sql2o.quirks.PostgresQuirks;
import spark.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static spark.Spark.get;
import static spark.Spark.post;

public class Main {

    public static void main(String[] args) {
        BasicConfigurator.configure();

        Flyway flyway = Flyway.configure().dataSource("jdbc:postgresql://localhost:5432/acebook", null, null).load();
        flyway.migrate();

        Sql2o sql2o = new Sql2o("jdbc:postgresql://localhost:5432/" + "acebook", null, null, new PostgresQuirks() {
            {
                // make sure we use default UUID converter.
                converters.put(UUID.class, new UUIDConverter());
            }
        });

        Model model = new Sql2oModel(sql2o);


        get("/", (req, res) -> "Hello World");


        get("/posts", (req, res) -> {


            if(model.getAllPosts().size() == 0) {
                UUID id = model.createPost("hello", "world");
            }

            HashMap posts = new HashMap();
            posts.put("posts", model.getAllPosts());
            return new ModelAndView(posts, "templates/posts.vtl");
        }, new VelocityTemplateEngine());

        get("/create", (req, res) -> {
            HashMap create = new HashMap();
            return new ModelAndView(create, "templates/create.vtl");
        }, new VelocityTemplateEngine());

        post("/create", (req, res) -> {
            String title = req.queryParams("title");
            String content = req.queryParams("content");
            UUID id = model.createPost(title, content);
            res.redirect("/posts");
            return ":)";
        });

        get("/signup", (req, res) -> {
            HashMap signup = new HashMap();
            return new ModelAndView(signup, "templates/signup.vtl");
        }, new VelocityTemplateEngine());

        get("/signed", (req, res) -> {
            HashMap signed = new HashMap();
            return new ModelAndView(signed, "templates/signed.vtl");
        }, new VelocityTemplateEngine());

        post("/signed", (req, res) -> {
            res.redirect("/signin");
            return ":)";
        });

        post("/signedup", (req, res) -> {
            String username = req.queryParams("username");
            String fullname = req.queryParams("full_name");
            String password = req.queryParams("password");
            model.createUser(username, fullname, password);
            res.redirect("/signed");
            return null;
        });

        get("/signin", (req, res) -> {
            HashMap signin = new HashMap();
            return new ModelAndView(signin, "templates/signin.vtl");
        }, new VelocityTemplateEngine());

        post("/signedin", (req, res) -> {
            String username = req.queryParams("username");
            String password = req.queryParams("password");

            if(model.CorrectPassword(username, password)){
                    res.redirect("/signedin");
            } else {
                res.redirect("/signin");
            };

            return ":)";
        });

        get("/signedin", (req, res) -> {
            HashMap signedin = new HashMap();
            return new ModelAndView(signedin, "templates/signedin.vtl");
        }, new VelocityTemplateEngine());

        post("/posts", (req, res) -> {
            res.redirect("/posts");
            return ":)";
        });
    }
}
