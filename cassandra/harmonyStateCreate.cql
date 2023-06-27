CREATE KEYSPACE HarmonyState

CREATE KEYSPACE HarmonyState
    WITH REPLICATION = {
        'class' : 'SimpleStrategy',
        'replication_factor' : 1
    };

USE HarmonyState;

CREATE TABLE Report (
    id int,
    longitude double,
    latitude double,
    persons list<tuple<text, text>>,
    words list<text>,
    time date,
    PRIMARY KEY (id)
);

CREATE TABLE Person (
    id int,
    name text,
    surname text,
    harmonyScore int,
    PRIMARY KEY (id)
);

