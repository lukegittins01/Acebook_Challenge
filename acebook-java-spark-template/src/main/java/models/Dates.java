package models;

import lombok.Data;

@Data
public class Dates {
    private String id;
    private String datecreated;


    public Dates(String id, String datecreated) {
        this.id = id;
        this.datecreated = datecreated;
    }
}
