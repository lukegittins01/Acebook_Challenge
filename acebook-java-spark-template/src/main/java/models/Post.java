package models;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

import lombok.Data;
@Data
public class Post {
    private UUID post_id;
    private String title;
    private String content;
    private String datecreated;
    private String usercreated;

    public Post(UUID post_id, String title, String content, String datecreated, String usercreated) {
        this.post_id = post_id;
        this.title = title;
        this.content = content;
        this.datecreated = datecreated;
        this.usercreated = usercreated;
    }

}
