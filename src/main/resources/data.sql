insert into player values (1, datetime('now'), 'marko.hujo@gmail.com', 'Marko', 'Hujo');
insert into player values (2, datetime('now'), 'someone@gmail.com', 'Some', 'One');
insert into player values (3, datetime('now'), 'somebody@gmail.com', 'Some', 'Body');
insert into player values (4, datetime('now'), 'someone.else@gmail.com', 'Someone', 'Else');
insert into player values (5, datetime('now'), 'idk@gmail.com', 'Idk', 'Idk');

insert into team values (2, 'Team A');
insert into team values (3, 'Team B');

insert into match values (1, datetime('now'), 1);
insert into match values (2, datetime('now'), 1);
insert into match values (3, datetime('now'), 1);
insert into match values (4, datetime('now'), 2);

insert into player_team values (1, 1);
insert into player_team values (1, 2);
insert into player_team values (1, 3);
insert into player_team values (1, 4);
insert into player_team values (2, 1);
insert into player_team values (2, 2);
