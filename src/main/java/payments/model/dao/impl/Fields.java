package payments.model.dao.impl;

/**
 * @author kara.vladimir2@gmail.com.
 */
final class Fields {
    public static final String T_ACC = "account";
    public static final String ACC_ID = "idAcc";
    public static final String ACC_NUMBER = "numberAcc";
    public static final String ACC_BALANCE = "balanceAcc";
    public static final String ACC_IS_BLOCKED = "isBlockedAcc";

    public static final String T_CL = "client";
    public static final String CL_ID = "idCl";
    public static final String CL_EMAIL = "emailCl";
    public static final String CL_FNAME = "familyNameCl";
    public static final String CL_NAME = "nameCl";

    public static final String T_USR = "user";
    public static final String USR_ID = "idUsr";
    public static final String USR_LOGIN = "loginUsr";
    public static final String USR_PASS = "passUsr";
    public static final String USR_IS_ADMIN = "isAdminUsr";
    public static final String USR_ID_CLIENT = "FK_Usr_idCl";

    public static final String T_PAY = "payment";
    public static final String PAY_ID = "idPay";
    public static final String PAY_TIME = "timeStampPay";
    public static final String PAY_AMOUNT = "amountPay";
    public static final String PAY_TYPE = "typePay";
    public static final String PAY_ID_SENDER_ACCOUNT = "FK_Pay_idSenderAcc";
    public static final String PAY_ID_RECIP_ACCOUNT = "FK_Pay_idRecipientAcc";

    public static final String T_CARD = "creditCard";
    public static final String CARD_ID = "idCc";
    public static final String CARD_NUMBER = "numberCc";
    public static final String CARD_ID_CLIENT = "FK_Cc_idCl";
    public static final String CARD_ID_ACCOUNT = "FK_Cc_idAcc";


}
