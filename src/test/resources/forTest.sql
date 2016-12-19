/*CREATE SCHEMA paymentsDB DEFAULT CHARACTER SET utf8 ;*/
DROP DATABASE IF EXISTS payments_test;
CREATE DATABASE IF NOT EXISTS payments_test;
USE payments;

CREATE TABLE IF NOT EXISTS account (
  idAcc        INT            NOT NULL AUTO_INCREMENT,
  numberAcc    VARCHAR(34)    NOT NULL,
  balanceAcc   DECIMAL(14, 2) NOT NULL,
  isBlockedAcc BIT            NOT NULL,
  UNIQUE (numberAcc),
  PRIMARY KEY (idAcc)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE IF NOT EXISTS client (
  idCl         INT         NOT NULL AUTO_INCREMENT,
  emailCl      VARCHAR(60) NOT NULL,
  familyNameCl VARCHAR(80) NOT NULL,
  nameCl       VARCHAR(80) NOT NULL,
  UNIQUE (emailCl),
  PRIMARY KEY (idCl)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE IF NOT EXISTS user (
  idUsr       INT         NOT NULL AUTO_INCREMENT,
  loginUsr    VARCHAR(20) NOT NULL,
  passUsr     VARCHAR(32),
  isAdminUsr  BIT         NOT NULL,
  FK_Usr_idCl INT         NULL,
  UNIQUE (loginUsr),
  PRIMARY KEY (idUsr),
  CONSTRAINT FK_Usr_idCl FOREIGN KEY (FK_Usr_idCl) REFERENCES client (idCl)
    ON UPDATE CASCADE
    ON DELETE CASCADE
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE IF NOT EXISTS creditCard (
  idCc        INT         NOT NULL AUTO_INCREMENT,
  numberCc    VARCHAR(16) NOT NULL,
  FK_Cc_idCl  INT         NOT NULL,
  FK_Cc_idAcc INT         NOT NULL,
  UNIQUE (numberCc),
  PRIMARY KEY (idCc),
  CONSTRAINT FK_Cc_idCl FOREIGN KEY (FK_Cc_idCl) REFERENCES client (idCl)
    ON UPDATE CASCADE
    ON DELETE RESTRICT,
  CONSTRAINT FK_Cc_idAcc FOREIGN KEY (FK_Cc_idAcc) REFERENCES account (idAcc)
    ON UPDATE CASCADE
    ON DELETE RESTRICT
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE IF NOT EXISTS payment (
  idPay                 INT       NOT NULL AUTO_INCREMENT,
  timeStampPay          TIMESTAMP NOT NULL,
  amountPay             DECIMAL(14, 2),
  typePay               TINYINT   NOT NULL,
  FK_Pay_idSenderCl     INT,
  FK_Pay_idSenderAcc    INT,
  FK_Pay_idSenderCc     INT,
  FK_Pay_idRecipientAcc INT,
  PRIMARY KEY (idPay),
  CONSTRAINT FK_Pay_idSenderCl FOREIGN KEY (FK_Pay_idSenderCl) REFERENCES client (idCl)
    ON UPDATE CASCADE
    ON DELETE RESTRICT,
  CONSTRAINT FK_Pay_idSenderAcc FOREIGN KEY (FK_Pay_idSenderAcc) REFERENCES account (idAcc)
    ON UPDATE CASCADE
    ON DELETE RESTRICT,
  CONSTRAINT FK_Pay_idSenderCc FOREIGN KEY (FK_Pay_idSenderCc) REFERENCES creditCard (idCc)
    ON UPDATE CASCADE
    ON DELETE RESTRICT,
  CONSTRAINT FK_Pay_idRecipientAcc FOREIGN KEY (FK_Pay_idRecipientAcc) REFERENCES account (idAcc)
    ON UPDATE CASCADE
    ON DELETE RESTRICT
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

# ===========================================================================================

USE payments;

DELETE FROM CreditCard;
DELETE FROM payment;
DELETE FROM account;
DELETE FROM client;
DELETE FROM user;

INSERT INTO account (idAcc, numberAcc, balanceAcc, isBlockedAcc)
VALUES
  (1, '1234', 1500.50, b'0'),
  (2, '565865265456855488', 7000.00, b'0'),
  (3, '545688323595325666', 440.80, b'0'),
  (4, '1111', 789.60, b'1');


INSERT INTO client (idCl, emailCl, familyNameCl, nameCl)
VALUES
  (1, 'asd@g.com', 'Ivanova', 'Elena'),
  (2, 'qwe@b.com', 'Petrenko', 'Ivan'),
  (3, 'zxc@f.com', 'Doe', 'John');


INSERT INTO CreditCard (idCc, numberCc, FK_Cc_idCl, FK_Cc_idAcc)
VALUES
  (1, '111111111111', 1, 1),
  (2, '222222222222', 1, 2),
  (3, '333333333333', 2, 3),
  (4, '444444444444', 2, 4),
  (5, '555555555555', 3, 4);


INSERT INTO user (idUsr, loginUsr, passUsr, isAdminUsr, FK_Usr_idCl)
VALUES
  (1, 'admin', '7694f4a66316e53c8cdd9d9954bd611d', TRUE, NULL),
  (2, 'user1', '7694f4a66316e53c8cdd9d9954bd611d', FALSE, 1),
  (3, 'user2', '7694f4a66316e53c8cdd9d9954bd611d', FALSE, 2)