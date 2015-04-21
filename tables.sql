
CREATE TABLE Adresse_Postale {
    NumAdr INTEGER primary key check (NumAdr > 0),
	RueAdr VARCHAR(50) primary key, 
	CodeAdr INTEGER primary key check (CodeAdr > 0), 
	VilleAdr VARCHAR(50) primary key
}


CREATE TABLE Personne{
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
}

CREATE TABLE Responsable {
    CodePersonne INT PRIMARY KEY FOREIGN KEY REFERENCES Personne,
    CodeCentre INT FOREIGN KEY REFERENCES Centre
}

CREATE TABLE Moniteur {
    CodePersonne INT PRIMARY KEY FOREIGN KEY REFERENCES Personne,
    CodeCentre INT FOREIGN KEY REFERENCES Centre
}


CREATE TABLE Centre {
    CodeCentre INTEGER primary key,
    NomCentre VARCHAR(50) Not Null,
	NumAdr INTEGER foreign key (NumAdr) references Adresse_Postale(NumAdr) ,
	RueAdr VARCHAR(50) foreign key (RueAdr) references Adresse_Postale(RueAdr),
	CodeAdr INTEGER foreign key (CodeAdr) references Adresse_Postale(CodeAdr) ,
	VilleAdr VARCHAR(50) foreign key (VilleAdr) references Adresse_Postale(VilleAdr),
	CodePersonne INTEGER foreign key  (CodePersonne) references Responsable(CodePersonnne)
}



CREATE TABLE Groupe{
    CodeGroupe INTEGER primary key,
    CodeCentre foreign key (CodeCentre) references Centre(CodeCentre),
    DateDebutGroupe DATE Not Null,
    DateFinGroupe DATE Not Null,
    NomNiveau NIVEAU Not Null,
}



CREATE TABLE SeSitueA{
	NumAdr primary key, foreign key (NumAdr) references Adresse_Postale(NumAdr),
	RueAdr primary key, foreign key (RueAdr) references Adresse_Postale(RueAdr),
	CodeAdr primary key,  foreign key (CodeAdr) references Adresse_Postale(CodeAdr),
	VilleAdr primary key,  foreign key (VilleAdr) references Adresse_Postale(VilleAdr),
	CodeCentre Integer Not Null, foreign key (CodeCentre) references Centre(CodeCentre)
}


CREATE TYPE Niveau AS ENUM (‘debutant’, ‘confirme’, ‘expert’)

CREATE TABLE EstDansGroupe{
    CodePersonne Integer primary key, foreign key (CodePersonne) references Personne(CodePersonne),
    CodeGroupe Integer primary key, foreign key (CodePersonne) references Groupe(CodeGroupe)
}




CREATE TABLE Activite {
    CodeAct INTEGER primary key,
    NomAct VARCHAR(10) Not Null,
    CategorieAct CATEGORIE Not Null,
    DescrAct VARCHAR(1000) Not Null,
    NbMinStagGroupe INTEGER Not Null,
    NbMaxStagGroupe INTEGER Not Null check (NbMaxStagGroupe >= NbMinStagGroupre)
}

CREATE TYPE Categorie AS ENUM ('nautique', 'montagne', 'air')

CREATE TABLE Gere {
    CodePersonne primary key, foreign key (CodePersonne)  references Personne (CodePersonne),
    CodeAct primary key, foreign key (CodeAct) references Activite (CodeAct),
    NbMaxStagMoniteur INTEGER Not Null
}

CREATE TABLE Necessite {
    CodeAct primary key, foreign key (CodeAct)  references Activite (CodeAct),
    NumMateriel primary key, foreign key (NumMateriel) references Materiel (NumMateriel),
    CodeCentre primary key, foreign key (CodeCentre) references Centre (CodeCentre),
    QuantiteNecessaire INTEGER Not Null
}


CREATE TABLE Materiel {
    CodeCentre INT PRIMARY KEY, FOREIGN KEY CodeCentre REFERENCES Centre(CodeCentre),
    NumMateriel INT PRIMARY KEY,
    TypeMateriel VARCHAR(255),
    MarqueMateriel VARCHAR(255),
    ModeleMateriel VARCHAR(255),
    QuantiteMateriel INT,
    NomNiveau VARCHAR(255) FOREIGN KEY REFERENCES Niveau
}

CREATE TABLE Seance {
    CodeGroupe INT PRIMARY KEY FOREIGN KEY REFERENCES Groupe,
    NumSeance INT PRIMARY KEY,
    DateSeance DATE,
    HeureDebutSeance INT,
    Duree INT
}

CREATE TABLE EstEncadreePar {
    CodePersonne INT PRIMARY KEY FOREIGN KEY REFERENCES Personne,
    NumSeance INT PRIMARY KEY FOREIGN KEY REFERENCES Seance
}

CREATE TABLE Stagiaire {
    CodePersonne INT PRIMARY KEY FOREIGN KEY REFERENCES Personne
}

CREATE TABLE EstInscritDansCentre {
    CodePersonne INT PRIMARY KEY FOREIGN KEY REFERENCES Personne,
    CodeCentre INT PRIMARY KEY FOREIGN KEY REFERENCES Centre
}



	
