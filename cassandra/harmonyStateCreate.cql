CREATE KEYSPACE IF NOT EXISTS HarmonyState
    WITH REPLICATION = {
        'class' : 'SimpleStrategy',
        'replication_factor' : 1
    };

USE HarmonyState;

CREATE TABLE IF NOT EXISTS Report (
    droneId int,
    longitude double,
    latitude double,
    persons list<tuple<text, text>>,
    words list<text>,
    time date,
    PRIMARY KEY (droneId, time)
);

CREATE TABLE IF NOT EXISTS Person (
    firstName text,
    lastName text,
    harmonyScore int,
    PRIMARY KEY (firstName, lastName)
);

//CREATE ROLE IF NOT EXISTS grafana WITH SUPERUSER = true AND LOGIN = true AND PASSWORD = 'bacon';