create table dates(
 id VARCHAR REFERENCES posts (post_id),
 times VARCHAR,
 am_or_pm VARCHAR,
 days VARCHAR
)