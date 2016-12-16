USE payments;

DELETE FROM CreditCard;
delete from payment;
DELETE FROM account;
DELETE FROM client;
delete from user;

INSERT INTO account (idAcc,numberAcc,balanceAcc,isBlockedAcc)
	VALUES
	(1,'1234',1500.50,b'0'),
	(2,'565865265456855488',7000.00,b'0'),
	(3,'545688323595325666',440.80,b'0'),
	(4,'1111',789.60,b'1');


INSERT INTO client (idCl,emailCl,familyNameCl,nameCl)
	VALUES
    (1,'asd@g.com','Ivanova','Elena'),
    (2,'qwe@b.com','Petrenko','Ivan'),
    (3,'zxc@f.com','Doe','John');



INSERT INTO CreditCard (idCc,numberCc,FK_Cc_idCl,FK_Cc_idAcc)
	VALUES
    (1,'111111111111',1,1),
    (2,'222222222222',1,2),
    (3,'333333333333',2,3),
    (4,'444444444444',2,4),
    (5,'555555555555',3,4);
    

INSERT INTO user (idUsr,loginUsr,passUsr,isAdminUsr,FK_Usr_idCl)
	values
    (1,'admin','7694f4a66316e53c8cdd9d9954bd611d',true,null),
    (2,'user1','7694f4a66316e53c8cdd9d9954bd611d',false,1),
    (3,'user2','7694f4a66316e53c8cdd9d9954bd611d',false,2)