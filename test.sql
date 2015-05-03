SELECT Max(Q.Quantite)
FROM ( 	--on fait la somme de materiel utilisé a chaque commencement de seance
     SELECT Dates.MomentTest, Sum(U.QuantiteNecessaire) as Quantite
     FROM Utilise U, Seance S, Groupe G, 
     --on cherche a obtenir chaque nouveau commencement de seance entre le debut et la fin de notre seance
          (
	  SELECT (S.DateSeance + S.HeureDebutSeance/24) as MomentTest
     	  FROM Utilise U, Seance S, Groupe G
     	  WHERE U.NumMateriel = 4 and U.CodeCentre = g.CodeCentre and 
     	  g.CodeCentre = 2 and
     	  U.CodeGroupe = G.CodeGroupe and U.NumSeance = S.NumSeance and 
     	  (S.DateSeance + S.HeureDebutSeance/24) < (TO_DATE('20/07/2015:12:00:00','DD/MM/YYYY:HH24:MI:SS')) and 
     	  (S.DateSeance + S.HeureDebutSeance/24) >= (TO_DATE('04/07/2015:12:00:00','DD/MM/YYYY:HH24:MI:SS'))
     	  ) Dates
     WHERE U.NumMateriel = 4 and U.CodeCentre = g.CodeCentre and g.CodeCentre = 2
     and U.CodeGroupe = G.CodeGroupe and U.NumSeance = S.NumSeance 
     and (S.DateSeance + S.HeureDebutSeance/24) IN Dates.MomentTest
     GROUP BY Dates.MomentTest
     ) Q ;
