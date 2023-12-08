-- Premium League Teams
INSERT INTO team (team_name, league_id) values ("Manchester Devils", 1);
INSERT INTO team (team_name, league_id) values ("London Eagles", 1);
INSERT INTO team (team_name, league_id) values ("Bournemouth Warriors", 1);
INSERT INTO team (team_name, league_id) values ("Liverpool Kings", 1);
INSERT INTO team (team_name, league_id) values ("Leeds Kids", 1);
-- Si Liga Teams
INSERT INTO team (team_name, league_id) values ("Barcelona Dioses", 2);
INSERT INTO team (team_name, league_id) values ("Madrid Toros", 2);
INSERT INTO team (team_name, league_id, values ("Valencia Naranjas", 2);
INSERT INTO team (team_name, league_id) values ("Sevilla Toreros", 2);
INSERT INTO team (team_name, league_id) values ("Bilbao Vascos", 2);

-- Deutche Liga Teams
INSERT INTO team (team_name, league_id) values ("Munich Bieren", 3);
INSERT INTO team (team_name, league_id) values ("Berlin Wanden", 3);
INSERT INTO team (team_name, league_id) values ("Frankfurt Bankers", 3);
INSERT INTO team (team_name, league_id) values ("Hamburg Fischer", 3);
INSERT INTO team (team_name, league_id) values ("Dortmund BVB", 3);

-- Add captains to teams
UPDATE team SET captain_id = 1 WHERE team_id = 1;
UPDATE team SET captain_id = 4 WHERE team_id = 2;
UPDATE team SET captain_id = 7 WHERE team_id = 3;
UPDATE team SET captain_id = 10 WHERE team_id = 4;
UPDATE team SET captain_id = 13 WHERE team_id = 5;
UPDATE team SET captain_id = 16 WHERE team_id = 6;
UPDATE team SET captain_id = 19 WHERE team_id = 7;
UPDATE team SET captain_id = 22 WHERE team_id = 8;
UPDATE team SET captain_id = 25 WHERE team_id = 9;
UPDATE team SET captain_id = 28 WHERE team_id = 10;
UPDATE team SET captain_id = 31 WHERE team_id = 11;
UPDATE team SET captain_id = 34 WHERE team_id = 12;
UPDATE team SET captain_id = 37 WHERE team_id = 13;
UPDATE team SET captain_id = 40 WHERE team_id = 14;
UPDATE team SET captain_id = 43 WHERE team_id = 15;
