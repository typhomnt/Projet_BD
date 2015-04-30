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