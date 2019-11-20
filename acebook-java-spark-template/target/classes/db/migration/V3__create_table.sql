create table dates(
 id VARCHAR REFERENCES posts (post_id),
 datecreated VARCHAR
)