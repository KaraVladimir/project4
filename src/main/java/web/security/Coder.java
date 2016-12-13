package web.security;

import model.dao.exception.DaoException;
import org.apache.log4j.Logger;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author kara.vladimir2@gmail.com.
 */
public enum  Coder {

    INSTANCE;

    public static final String CODER_ERROR = "coder error";
    private static final Logger LOG = Logger.getLogger(Coder.class);


    public String getHash(String pwd) throws DaoException {
        MessageDigest messageDigest = null;
        byte[] digest;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(pwd.getBytes());
            digest = messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new DaoException(LOG,CODER_ERROR,e);
        }
        BigInteger bigInt = new BigInteger(1, digest);
        String md5Hex = bigInt.toString(16);
        while( md5Hex.length() < 32 ){
            md5Hex = "0" + md5Hex;
        }
        return md5Hex;
    }
}
