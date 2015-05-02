CREATE TABLE Adresse_Postale(
       NumAdr INTEGER check (NumAdr > 0),
       RueAdr VARCHAR(500), 
       CodeAdr INTEGER check (CodeAdr > 0), 
       VilleAdr VARCHAR(500),
       primary key (NumAdr,RueAdr,CodeAdr,VilleAdr)
);


CREATE TABLE Personne(
    CodePersonne INTEGER primary key,
    NomPersonne VARCHAR(100) Not Null,
    PrenomPersonne VARCHAR(100) Not Null,
    NaissancePersonne DATE Not Null,
    TelPersonne INTEGER Not Null,
    MailPersonne VARCHAR(300) Not Null,
    NumAdr INTEGER,
    RueAdr VARCHAR(500),
    CodeAdr INTEGER,
    VilleAdr VARCHAR(500), 
    foreign key (NumAdr, RueAdr, CodeAdr,VilleAdr) references Adresse_Postale(NumAdr, RueAdr, CodeAdr,VilleAdr)
);




CREATE TABLE Centre (
    CodeCentre INTEGER primary key,
    NomCentre VARCHAR(500) Not Null,
    NumAdr INTEGER,
    RueAdr VARCHAR(500),
    CodeAdr INTEGER,
    VilleAdr VARCHAR(500),
    foreign key (NumAdr, RueAdr, CodeAdr,VilleAdr) references Adresse_Postale(NumAdr, RueAdr, CodeAdr,VilleAdr)
);

CREATE TABLE Responsable (
    CodePersonne INTEGER PRIMARY KEY, FOREIGN KEY (CodePersonne) REFERENCES Personne(CodePersonne),
    CodeCentre INT, FOREIGN KEY (CodeCentre) REFERENCES Centre(CodeCentre)
);

CREATE TABLE Moniteur (
    CodePersonne INT PRIMARY KEY, FOREIGN KEY (CodePersonne) REFERENCES Personne(CodePersonne),
    CodeCentre INT, FOREIGN KEY (CodeCentre) REFERENCES Centre(CodeCentre)
);


--ALTER TABLE Responsable
--ADD CodeCentre INT;

--ALTER TABLE Responsable
--ADD FOREIGN KEY (CodeCentre) REFERENCES Centre(CodeCentre);

CREATE TABLE Niveau(
       niv VARCHAR(100) primary key check(niv IN ('debutant', 'confirme', 'expert') )
);


CREATE TABLE Categorie(
       cat VARCHAR(100) primary key check(cat IN ('nautique', 'montagne', 'air') )
);


CREATE TABLE Activite (
    CodeAct INTEGER primary key,
    NomAct VARCHAR(100) Not Null,
    CategorieAct VARCHAR(100) Not Null,foreign key (CategorieAct) references Categorie(cat),
    DescrAct VARCHAR(1000) Not Null
);

CREATE TABLE Groupe(
    CodeGroupe INTEGER primary key,
    CodeAct INTEGER Not Null, foreign key (CodeAct) references Activite(CodeAct),
    CodeCentre INTEGER, foreign key (CodeCentre) references Centre(CodeCentre),
    DateDebutGroupe DATE Not Null,
    DateFinGroupe DATE Not Null,
    NbMinStagGroupe INTEGER Not Null,
    NbMaxStagGroupe INTEGER Not Null, -- check (NbMaxStagGroupe >= NbMinStagGroupe) en java
    NomNiveau VARCHAR(100) Not Null,foreign key (NomNiveau) references Niveau(niv)
);

CREATE TABLE EstDansGroupe(
    CodePersonne Integer , foreign key (CodePersonne) references Personne(CodePersonne),
    CodeGroupe Integer , foreign key (CodeGroupe) references Groupe(CodeGroupe),
    primary key(CodePersonne, CodeGroupe)
);



CREATE TABLE Gere (
    CodePersonne INT, foreign key (CodePersonne)  references Personne (CodePersonne),
    CodeAct INT, foreign key (CodeAct) references Activite (CodeAct),
    NbMaxStagMoniteur INTEGER Not Null,
    primary key (CodePersonne, CodeAct)
);

CREATE TABLE TypeMateriel (
    type VARCHAR(255) primary key
);

CREATE TABLE Materiel (
    CodeCentre INT, FOREIGN KEY (CodeCentre) REFERENCES Centre(CodeCentre),
    NumMateriel INT,
    Type VARCHAR(255) Not Null, foreign key (Type) references TypeMateriel(type),
    MarqueMateriel VARCHAR(255) Not Null,
    ModeleMateriel VARCHAR(255) Not Null,
    QuantiteMateriel INT Not Null,
    NomNiveau VARCHAR(100) Not Null,foreign key (NomNiveau) references Niveau(niv),
    PRIMARY KEY (CodeCentre, NumMateriel)
);

CREATE TABLE Necessite (
    CodeAct INT, foreign key (CodeAct)  references Activite (CodeAct),
    Type VARCHAR(255) Not Null, foreign key (Type) references TypeMateriel(type),
    primary key(CodeAct, Type)
);

CREATE TABLE Seance (
    CodeGroupe INT, FOREIGN KEY (CodeGroupe) REFERENCES Groupe(CodeGroupe),
    NumSeance INT,
    DateSeance DATE Not Null,
    HeureDebutSeance INT Not Null,
    Duree INT Not Null,
    PRIMARY KEY(CodeGroupe, NumSeance)
);

CREATE TABLE Utilise(
    CodeGroupe INT,
    NumSeance INT,
    CodeCentre INT,
    NumMateriel INT,
    QuantiteNecessaire INT Not Null,
    primary key (CodeGroupe,NumSeance,CodeCentre,NumMateriel),
    foreign key (CodeGroupe,NumSeance) references Seance(CodeGroupe,NumSeance),
    foreign key (CodeCentre,NumMateriel) references Materiel(CodeCentre,NumMateriel)
);

CREATE TABLE EstEncadreePar (
    CodePersonne INT, FOREIGN KEY (CodePersonne) REFERENCES Personne(CodePersonne),
    CodeGroupe INT, FOREIGN KEY (CodeGroupe)  REFERENCES Groupe(CodeGroupe),
    NumSeance INT, FOREIGN KEY (NumSeance, CodeGroupe) REFERENCES Seance(NumSeance, CodeGroupe),
    PRIMARY KEY (CodePersonne, CodeGroupe, NumSeance)
);

CREATE TABLE Stagiaire (
    CodePersonne INT PRIMARY KEY, FOREIGN KEY (CodePersonne) REFERENCES Personne(CodePersonne)
);

CREATE TABLE EstInscritDansCentre (
    CodePersonne INT, FOREIGN KEY (CodePersonne) REFERENCES Personne(CodePersonne),
    CodeCentre INT, FOREIGN KEY (CodeCentre) REFERENCES Centre(CodeCentre),
    Datedeb DATE not null,
    Datefin DATE not null,
    PRIMARY KEY (CodePersonne,CodeCentre)
);
