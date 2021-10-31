insert into player values (1, datetime('now'), 'marko.hujo@gmail.com', 'Marko', 'Hujo');
insert into player values (2, datetime('now'), 'someone@gmail.com', 'Some', 'One');
insert into player values (3, datetime('now'), 'somebody@gmail.com', 'Some', 'Body');
insert into player values (4, datetime('now'), 'someone.else@gmail.com', 'Someone', 'Else');

insert into team values (1, 'Team A');
insert into team values (2, 'Team B');
insert into team values (3, 'Team C');

insert into match values (1, datetime('now'));
insert into match values (2, datetime('now'));
insert into match values (3, datetime('now'));
insert into match values (4, datetime('now'));

insert into team_players values (1, 1);
insert into team_players values (1, 4);
insert into team_players values (2, 1);
insert into team_players values (2, 2);
insert into team_players values (2, 3);

insert into team_matches values (1, 1);
insert into team_matches values (1, 3);
insert into team_matches values (3, 2);
insert into team_matches values (2, 4);
