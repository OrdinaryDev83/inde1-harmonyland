CREATE KEYSPACE IF NOT EXISTS harmonystate
    WITH REPLICATION = {
        'class' : 'SimpleStrategy',
        'replication_factor' : 1
    };

USE harmonystate;

CREATE TABLE IF NOT EXISTS report (
    droneid int,
    longitude double,
    latitude double,
    persons list<tuple<text, text>>,
    words list<text>,
    time date,
    PRIMARY KEY (droneid, time)
);

CREATE TABLE IF NOT EXISTS person (
    firstname text,
    lastname text,
    harmonyscore int,
    PRIMARY KEY (firstname, lastname)
);

//CREATE ROLE IF NOT EXISTS grafana WITH SUPERUSER = true AND LOGIN = true AND PASSWORD = 'bacon';