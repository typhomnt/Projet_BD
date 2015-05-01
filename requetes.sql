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

--on verifie que le materiel est disponible avant de commiter

SELECT N.NumMateriel
FROM Necessite N, Seance S, Groupe G
WHERE S.NumSeance = NumSeance and S.CodeGroupe = CodeGroupe and
G.CodeGroupe = S.CodeGroupe and G.CodeAct = S.CodeAct and N.CodeCentre = G.CodeCentre;
--on stocke l'ID du materiel requis
SELECT Sum(N.QuantiteNecessaire) as Quantite
FROM Necessite N, Seance S, Groupe G
WHERE N.NumMateriel = ID and N.CodeAct = g.CodeAct and N.CodeCentre = g.CodeCentre
and ( (S.DateSeance >= DateSeance and S.DateSeance < DateSeance+HeureDebutSeance+Duree) 
      or 
      (S.DateSeance + S.HeureDebutSeance + S.Duree >= DateSeance and
      	S.DateSeance + S.HeureDebutSeance + S.Duree < DateSeance+HeureDebutSeance+Duree)
    );
--pb: ne gere pas si deux seance courtes se suivent mais sont toutes deux dans l'intervalle

SELECT M.QuantiteMateriel
FROM Materiel M, Groupe G
WHERE M.CodeCentre = G.CodeCentre and M.NumMateriel = ID;

--On verifie que la quantite necessaire pour les activités ne depasse pas celle contenue dans le centre

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