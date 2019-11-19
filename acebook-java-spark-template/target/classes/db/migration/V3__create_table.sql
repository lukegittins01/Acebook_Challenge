create table dates(
 id VARCHAR REFERENCES users (user_id),
 times VARCHAR,
 am_or_pm VARCHAR,
 days VARCHAR
)