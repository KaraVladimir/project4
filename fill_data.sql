USE payments;

DELETE FROM account;
INSERT INTO account (idAcc,numberAcc,balanceAcc,isBlockedAcc)
	VALUES
	(1,'1234123412341234',1500.50,b'0'),
	(2,'565865265456855488',7000.00,b'0'),
	(3,'545688323595325666',440.80,b'0'),
	(4,'887896546896559887',789.60,b'1');

DELETE FROM client;
INSERT INTO client (idCl,emailCl,familyNameCl,nameCl)
	VALUES
    (1,'asd','Ivanova','Elena'),
    (2,'asd','Petrenko','Ivan'),
    (3,'asd','Doe','John');


DELETE FROM CreditCard;
INSERT INTO CreditCard (idCc,numberCc,FK_Cc_idCl,FK_Cc_idAcc)
	VALUES
    (1,'111111111111',1,1),
    (2,'222222222222',1,2),
    (3,'333333333333',2,3),
    (4,'444444444444',2,4),
    (5,'555555555555',3,4);