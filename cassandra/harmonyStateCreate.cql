CREATE KEYSPACE IF NOT EXISTS HarmonyState
    WITH REPLICATION = {
        'class' : 'SimpleStrategy',
        'replication_factor' : 1
    };

USE HarmonyState;

CREATE TABLE IF NOT EXISTS Report (
    id int,
    droneId int,
    longitude double,
    latitude double,
    persons list<tuple<text, text>>,
    words list<text>,
    time date,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS Person (
    id int,
    name text,
    surname text,
    harmonyScore int,
    PRIMARY KEY (id)
);

//CREATE ROLE IF NOT EXISTS grafana WITH SUPERUSER = true AND LOGIN = true AND PASSWORD = 'bacon';