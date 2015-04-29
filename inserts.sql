
INSERT INTO Adresse_Postale
Values (18,'rue des marseillais',13000,'Marseille');

INSERT INTO Adresse_Postale
Values (51,'rue des marseillaises',13000,'Marseille');

INSERT INTO Adresse_Postale
Values (30,'rue des marseillaises',13000,'Marseille');

SELECT *
FROM Adresse_Postale;

INSERT INTO Categorie
values ('nautique');

INSERT INTO Categorie
values ('montagne');

INSERT INTO Categorie
values ('air');

SELECT *
FROM Categorie;

INSERT INTO Activite
values (15,'Natation','nautique', 'on nage',7,15); 

SELECT * 
FROM Activite

INSERT INTO Niveau
values ('debutant');

INSERT INTO Niveau
values ('confirme');

INSERT INTO Niveau
values ('expert');


SELECT *
FROM Niveau;

INSERT INTO Personne
values (1,'martin','paul',to_date('2000-02-01', 'yyyy-mm-dd'),0665859575,'mp@sql.fr',18,'rue des marseillais',13000,'Marseille');

INSERT INTO Personne
values (2,'martin','michel',to_date('2000-02-01', 'yyyy-mm-dd'),0665854475,'mm@sql.fr',51,'rue des marseillaises',13000,'Marseille');

SELECT *
FROM Personne

INSERT INTO Centre
values(1,'le petit marseillais',30,'rue des marseillaises',13000,'Marseille');

SELECT *
FROM Personne

INSERT INTO Responsable
values (1,1);

SELECT *
FROM Responsable

INSERT INTO Moniteur
Values (1,1);

INSERT INTO Groupe
Values (1,15,1,to_date('2015-07-01', 'yyyy-mm-dd'),to_date('2015-07-08', 'yyyy-mm-dd'),'debutant')

SELECT *
FROM Groupe

INSERT INTO EstDansGroupe
Values (2,1);

SELECT *
FROM EstDansGroupe