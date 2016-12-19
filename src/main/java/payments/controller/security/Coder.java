package payments.controller.security;

import payments.model.dao.exception.DaoException;
import org.apache.log4j.Logger;
import payments.helper.Msgs;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Helper class for security
 * @author kara.vladimir2@gmail.com.
 */
public enum Coder {

    INSTANCE;

    private static final Logger LOG = Logger.getLogger(Coder.class);


    public String getHash(String pwd) throws DaoException {
        MessageDigest messageDigest;
        byte[] digest;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(pwd.getBytes());
            digest = messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new DaoException(LOG, Msgs.CODER_ERROR, e);
        }
        BigInteger bigInt = new BigInteger(1, digest);
        String md5Hex = bigInt.toString(16);
        while (md5Hex.length() < 32) {
            md5Hex = "0" + md5Hex;
        }
        return md5Hex;
    }
}
