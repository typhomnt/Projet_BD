-- 1) Enregistrement d'un stagiaire à un centre et à une activite

SAVEPOINT

SELECT s.CodePersonne FROM Stagiaire c WHERE s.CodePersonne = CodePersonne;
-- Si aucun, faire :
SELECT p.CodePersonne FROM Personne p WHERE p.CodePersonne = CodePersonne;
-- S’il y en a un → erreur.
-- Sinon, faire :
INSERT INTO Stagiaire VALUES (CodePersonne);

-- Puis :
INSERT INTO EstInscritDansCentre VALUES (CodePersonne, CodeCentre);
INSERT INTO EstDansGroupe VALUES (CodePersonne, CodeGroupe);

COMMIT

-- 2) Planification d'une séance pour un groupe
-- Voir si en java on peut recupérer + sauver plusieurs colonne en même temps dans differentes variables
SAVEPOINT

SELECT g.CodeGroupe FROM Groupe g WHERE g.CodeGroupe = CodeGroupe;
-- si pas de groupe → erreur
SELECT Count(distinct edg.CodePersonne) as Participants
FROM EstDansGroupe edg
WHERE edg.CodeGroupe = CodeGroupe;
--on enregistre dans total
SELECT g.NbMinStagGroupe FROM Groupe g WHERE g.CodeGroupe = CodeGroupe;
-- on compare ce resultat à total, s'il est plus grand --> erreur il n'y a pas assez de participant

-- on donne une date, une heure et une duree
SELECT g.DateDebutGroupe FROM Groupe g WHERE g.CodeGroupe = CodeGroupe; --> gDebut
SELECT g.DateFinGroupe FROM Groupe g WHERE g.CodeGroupe = CodeGroupe;   --> gFin
--on verifie date >= gDebut et date+heure+duree < gFin si ce n'est pas le cas --> erreur

INSERT INTO Seance
Values (CodeGroupe, NumSeance, DateSeance, HeureDebutSeance, Duree);

SELECT g.CodeCentre FROM Groupe g WHERE g.CodeGroupe = CodeGroupe; --on recupere CodeCentre

SELECT distinct N.Type --on recupere le type de materiel requis
FROM Necessite N, Seance S, Groupe G
WHERE S.NumSeance = NumSeance and S.CodeGroupe = CodeGroupe and
G.CodeGroupe = S.CodeGroupe and G.CodeAct = S.CodeAct and N.CodeCentre = G.CodeCentre;

--affichage des materiels disponible pour cette activité
SELECT *
FROM Materiel M, Groupe g
where M.CodeCentre = g.CodeCentre and M.Type = type;  
--> erreur si vide alors que type non null

--l'utilisateur entre la quantité de materiel requis et le NumMateriel=ID
INSERT INTO Utilise
Values (CodeGroupe, NumSeance, CodeCentre, NumMateriel, Quantitée);
--on verifie que le materiel est disponible avant de commiter

--on regarde quel sera le maximum utilisé à un moment entre le debut et la fin de notre activité
SELECT Max(Quantite)
FROM
(
	--on fait la somme de materiel utilisé a chaque commencement de seance
	SELECT Dates.MomentTest, Sum(U.QuantiteNecessaire) as Quantite
	FROM Utilise U, Seance S, Groupe G, 
	--on cherche a obtenir chaque nouveau commencement de seance entre le debut et la fin de notre seance
	(SELECT (S.DateSeance + HeureDebutSeance) as MomentTest
	    FROM Utilise U, Seance S, Groupe G
	        WHERE U.NumMateriel = ID and U.CodeCentre = g.CodeCentre and
		    U.CodeGroupe = G.CodeGroupe and U.NumSeance = S.NumSeance and
    		    S.DateSeance + S.HeureDebutSeance < DateSeance+HeureDebutSeance+Duree and
    		    S.DateSeance + S.HeureDebutSeance >= DateSeance+HeureDebutSeance
	       ) as Dates
	WHERE U.NumMateriel = ID and U.CodeCentre = g.CodeCentre
	and U.CodeGroupe = G.CodeGroupe and U.NumSeance = S.NumSeance
	and S.DateSeance+S.HeureDebutSeance IN Dates.MomentTest
	GROUP BY Dates.MomentTest
);
 --on cherche tout les horaires de debuts de seance dans l'intervalle de notre séance


SELECT M.QuantiteMateriel
FROM Materiel M, Groupe G
WHERE M.CodeCentre = G.CodeCentre and M.NumMateriel = ID;

--On verifie que la quantite necessaire pour les activités ne depasse pas celle contenue dans le centre

COMMIT

-- 4) Visualisation des séances planifiées

SELECT s.*
FROM groupe g, seance s
WHERE g.CodeGroupe = s.CodeGroupe;

-- 5) Gestion du materiel : inventaire, ajout et suppression

-- Inventaire du materiel dans chaque centre

select m.*  
from centre c, materiel m
where c.CodeCentre = m.CodeCentre;

-- 6) Pour chaque activite, classement des centres en fonction 
--    du nombre d'inscrits dans l'annee


SELECT DISTINCT a.NomAct, c.NomCentre as Centre, count(*) as Nb_Inscrits
FROM Activite a, EstDansGroupe edg, Centre c, Groupe g
WHERE edg.CodeGroupe = g.CodeGroupe AND g.CodeAct = a.CodeAct AND g.CodeCentre = c.CodeCentre
GROUP BY a.NomAct, c.NomCentre
ORDER BY a.NomAct, Nb_Inscrits;

-- 7) Classement des villes par nombre de stagiaires inscrits

SELECT DISTINCT ap.VilleAdr as Ville, count(i.CodePersonne) as Nb_Inscrits
FROM Centre c, EstInscritDansCentre i, Adresse_Postale ap
WHERE ap.VilleAdr = c.VilleAdr AND ap.NumAdr = c.NumAdr AND ap.RueAdr = c.RueAdr AND ap.CodeAdr = c.CodeAdr 
      AND i.CodeCentre = c.CodeCentre
GROUP BY ap.VilleAdr
UNION
SELECT DISTINCT ap.VilleAdr as Ville, 0
FROM Adresse_Postale ap, Centre c
WHERE ap.VilleAdr NOT IN (	SELECT c.VilleAdr
      		      	 	FROM Centre c, EstInscritDansCentre i
				WHERE c.CodeCentre = i.CodeCentre	)
ORDER BY Nb_Inscrits;