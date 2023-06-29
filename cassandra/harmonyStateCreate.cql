CREATE KEYSPACE IF NOT EXISTS harmonystate
    WITH REPLICATION = {
        'class' : 'SimpleStrategy',
        'replication_factor' : 1
    };

USE harmonystate;

CREATE TYPE person (
    firstname text,
    lastname text,
    harmonyscore int,
);

CREATE TABLE IF NOT EXISTS report (
    droneid int,
    longitude double,
    latitude double,
    persons frozen<list<person>>,
    words list<text>,
    time timestamp,
    PRIMARY KEY (droneid, time)
);

CREATE TABLE IF NOT EXISTS harmonyscores (
    time timestamp,
    harmonyscore int,
    id uuid,
    PRIMARY KEY (time)
);

CREATE TABLE IF NOT EXISTS persons (
    time timestamp,
    count int,
    id uuid,
    PRIMARY KEY (time)
);

CREATE TABLE IF NOT EXISTS badpersons (
    time timestamp,
    count int,
    id uuid,
    PRIMARY KEY (time)
);

//CREATE ROLE IF NOT EXISTS grafana WITH SUPERUSER = true AND LOGIN = true AND PASSWORD = 'bacon';

