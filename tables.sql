CREATE TABLE Adresse_Postale(
       NumAdr INTEGER check (NumAdr > 0),
       RueAdr VARCHAR(50), 
       CodeAdr INTEGER check (CodeAdr > 0), 
       VilleAdr VARCHAR(50),
       primary key (NumAdr,RueAdr,CodeAdr,VilleAdr)
)


CREATE TABLE Personne(
    CodePersonne INTEGER primary key,
    NomPersonne VARCHAR(10) Not Null,
    PrenomPersonne VARCHAR(10) Not Null,
    NaissancePersonne DATE Not Null,
    TelPersonne INTEGER Not Null,
    MailPersonne VARCHAR(30) Not Null,
    NumAdr foreign key (NumAdr) references AdressePostale(NumAdr),
    RueAdr foreign key (RueAdr) references AdressePostale(RueAdr),
    CodeAdr foreign key (CodeAdr) references AdressePostale(CodeAdr),
    VilleAdr foreign key (VilleAdr) references AdressePostale(VilleAdr)
)

CREATE TABLE Responsable (
    CodePersonne INT PRIMARY KEY FOREIGN KEY REFERENCES Personne,
)

CREATE TABLE Moniteur (
    CodePersonne INT PRIMARY KEY FOREIGN KEY REFERENCES Personne,
    CodeCentre INT FOREIGN KEY REFERENCES Centre
)


CREATE TABLE Centre (
    CodeCentre INTEGER primary key,
    NomCentre VARCHAR(50) Not Null,
    NumAdr INTEGER foreign key (NumAdr) references Adresse_Postale(NumAdr) ,
    RueAdr VARCHAR(50) foreign key (RueAdr) references Adresse_Postale(RueAdr),
    CodeAdr INTEGER foreign key (CodeAdr) references Adresse_Postale(CodeAdr) ,
    VilleAdr VARCHAR(50) foreign key (VilleAdr) references Adresse_Postale(VilleAdr),
    CodePersonne INTEGER foreign key  (CodePersonne) references Responsable(CodePersonnne)
)

ALTER TABLE Responsable
ADD CodeCentre INT FOREIGN KEY REFERENCES Centre

CREATE TYPE Niveau AS ENUM (‘debutant’, ‘confirme’, ‘expert’)

CREATE TABLE Groupe(
    CodeGroupe INTEGER primary key,
    CodeCentre foreign key (CodeCentre) references Centre(CodeCentre),
    DateDebutGroupe DATE Not Null,
    DateFinGroupe DATE Not Null,
    NomNiveau NIVEAU Not Null,
)



CREATE TABLE SeSitueA(
	NumAdr INTEGER, foreign key (NumAdr) references Adresse_Postale(NumAdr),
	RueAdr VARCHAR(50), foreign key (RueAdr) references Adresse_Postale(RueAdr),
	CodeAdr INTEGER,  foreign key (CodeAdr) references Adresse_Postale(CodeAdr),
	VilleAdr VARCHAR(50),  foreign key (VilleAdr) references Adresse_Postale(VilleAdr),
	CodeCentre Integer Not Null, foreign key (CodeCentre) references Centre(CodeCentre),
	primary key(NumAdr, RueAdr, CodeAdr, VilleAdr)
)




CREATE TABLE EstDansGroupe(
    CodePersonne Integer , foreign key (CodePersonne) references Personne(CodePersonne),
    CodeGroupe Integer , foreign key (CodePersonne) references Groupe(CodeGroupe),
    primary key(CodePersonne, CodeGroupe)
)


CREATE TYPE Categorie AS ENUM ('nautique', 'montagne', 'air')



CREATE TABLE Activite (
    CodeAct INTEGER primary key,
    NomAct VARCHAR(10) Not Null,
    CategorieAct CATEGORIE Not Null,
    DescrAct VARCHAR(1000) Not Null,
    NbMinStagGroupe INTEGER Not Null,
    NbMaxStagGroupe INTEGER Not Null check (NbMaxStagGroupe >= NbMinStagGroupre)
)


CREATE TABLE Gere (
    CodePersonne INT, foreign key (CodePersonne)  references Personne (CodePersonne),
    CodeAct INT, foreign key (CodeAct) references Activite (CodeAct),
    NbMaxStagMoniteur INTEGER Not Null,
    primary key (CodePersonne, CodeAct)
)

CREATE TABLE Necessite (
    CodeAct INT, foreign key (CodeAct)  references Activite (CodeAct),
    NumMateriel INT, foreign key (NumMateriel) references Materiel (NumMateriel),
    CodeCentre INT, foreign key (CodeCentre) references Centre (CodeCentre),
    QuantiteNecessaire INTEGER Not Null,
    primary key(CodeAct, NumMateriel, CodeCentre)
)


CREATE TABLE Materiel (
    CodeCentre INT, FOREIGN KEY CodeCentre REFERENCES Centre(CodeCentre),
    NumMateriel INT,
    TypeMateriel VARCHAR(255),
    MarqueMateriel VARCHAR(255),
    ModeleMateriel VARCHAR(255),
    QuantiteMateriel INT,
    NomNiveau NIVEAU Not Null,
    PRIMARY KEY (CodeCentre, NumMateriel)
)

CREATE TABLE Seance (
    CodeGroupe INT FOREIGN KEY REFERENCES Groupe,
    NumSeance INT,
    DateSeance DATE,
    HeureDebutSeance INT,
    Duree INT,
    PRIMARY KEY(CodeGroupe, NumSeance)
)

CREATE TABLE EstEncadreePar (
    CodePersonne INT FOREIGN KEY REFERENCES Personne,
    NumSeance INT FOREIGN KEY REFERENCES Seance,
    PRIMARY KEY (CodePersonne, NumSeance)
)

CREATE TABLE Stagiaire (
    CodePersonne INT PRIMARY KEY FOREIGN KEY REFERENCES Personne
)

CREATE TABLE EstInscritDansCentre (
    CodePersonne INT FOREIGN KEY REFERENCES Personne,
    CodeCentre INT FOREIGN KEY REFERENCES Centre,
    PRIMARY KEY (CodePersonne,CodeCentre)
)
;