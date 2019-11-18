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

    public Post(UUID post_id, String title, String content) {
        this.post_id = post_id;
        this.title = title;
        this.content = content;
    }

}
