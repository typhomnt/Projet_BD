Fichiers sur le Drive :
	- tableau de contraintes
	- schéma relationnel
	- requêtes sql : Brice et moi avons rédigé ce qu'on doit faire
	en SQL et en Java pour les 7 requêtes SQL dans la partie 2.2 du
	sujet. Il s'agit de brouillons en quelque sorte donc les requêtes
	SQL ne sont pas forcément justes (on ne les a pas exécuté). Donc
	il faudrait que vous vérifiez que ce qu'on a mis comme vérifications
	à faire sont justes. Ensuite, vous testez les requêtes SQL et enfin,
	vous les implémentez en JBDC en vous basant sur le code fourni sur
	le Kiosk (dans le dossier kiosk/), sur ce qu'on a déjà codé, etc. 
	On a décidé d'utiliser ma connexion (ghorreso/ghorreso) pour SQLPLUS.

Fichiers SQL sur le git :
	- tables SQL : on crée toutes les tables
	- drop.sql : on supprime toutes les tables
	- inserts.sql : on fait tous les inserts
	- display.sql : on affiche toutes les tables
	- requetes.sql : les requêtes demandés dans le sujet en SQL

Pour ajouter/modifier/supprimer des inserts, vous effectuez vos modifs
dans le fichier inserts.sql puis vous appliquez après connexion : @drop, 
@tables, @inserts. Le display ne sert pas trop, vaut mieux faire "select *
from nom_table;". 

Fichiers sources sur le git :
	- main.java : fichier principal où on lance la connexion
	- ClassementVilles.java : fichier java pour la requête : classement des villes

Attention : il faut changer le login et le mdp de la connexion dans Main.java sinon
vos requêtes ne vont rien afficher si vous êtes connectés en même temps sur le même
compte. Cela ne posera pas de soucis normalement au niveau du git vu qu'on ne pushera
pas très souvent le Main.java (il faut juste modifier le Main quand on veut ajouter les
nouvelles méthodes des requêtes)

ATTENTION : ne pas de mettre de POINT VIRGULE à la fin de votre requête en Java (voir
l'exemple de ClassementVilles.java)

Omid

