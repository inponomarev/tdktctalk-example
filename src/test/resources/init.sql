-- SCHEMA
create table speaker (
                         id INT not null primary key,
                         name VARCHAR(50)
);

create table conference (
                            id INT not null primary key,
                            name VARCHAR(50)
);

create table talk (
                      id INT not null primary key,
                      name VARCHAR(500) not null,
                      conferenceid INT not null,
                      status VARCHAR(100) not null,
                      feedback VARCHAR(1000),
                      foreign key (conferenceid) references conference(id)
);


create table talkspeakers (
                              talkid INT not null ,
                              speakerid INT not null,
                              primary key (talkid, speakerid),
                              foreign key (talkid) references talk(id),
                              foreign key (speakerid) references speaker(id)
);

-- DATA

insert into speaker (id, name) values (1001, 'Sergey Egorov');
insert into speaker (id, name) values (1002, 'Kirill Tolkachev');
insert into speaker (id, name) values (1003, 'Evgeny Borisov');
insert into speaker (id, name) values (1004, 'Tagir Valeev');

insert into conference (id, name) values (1001, 'JPoint');
insert into conference (id, name) values (1002, 'Joker');

insert into talk (id, name, conferenceid, status, feedback)
values (1001, 'Java 9-14: Small Optimizations', 1002, 'IN_REVIEW',
        'outdated material');

insert into talk (id, name, conferenceid, status)
values (1002, 'Reactive, or not reactive: that is the question', 1001, 'IN_REVIEW');

insert into talk (id, name, conferenceid, status)
values (1003, 'Don''t be Homer Simpson to your Reactor!', 1001, 'IN_REVIEW');

insert into talk (id, name, conferenceid, status)
values (1004, 'Testcontainers: Year later', 1002,  'IN_REVIEW');

insert into talkspeakers (talkid, speakerid) values (1001, 1004);
insert into talkspeakers (talkid, speakerid) values (1002, 1002);
insert into talkspeakers (talkid, speakerid) values (1002, 1003);
insert into talkspeakers (talkid, speakerid) values (1003, 1001);
insert into talkspeakers (talkid, speakerid) values (1004, 1001);
