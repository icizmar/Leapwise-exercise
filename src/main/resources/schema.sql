CREATE SCHEMA leapwise AUTHORIZATION sa;

DROP TABLE IF EXISTS feed;
DROP TABLE IF EXISTS heading;

CREATE TABLE leapwise.feed(
	id int NOT NULL AUTO_INCREMENT,
    title VARCHAR(1000) NULL,
    link VARCHAR(1000) NULL,
    session_id VARCHAR(250) NOT NULL,
    PRIMARY KEY (id)
);

create table leapwise.tag(
    id int NOT NULL AUTO_INCREMENT,
    tag_name VARCHAR(250) NULL,
    feed_id bigint(20),
    session_id VARCHAR(250) NOT NULL,
    PRIMARY KEY (id),
    foreign key (feed_id) references leapwise.feed(id)
);

