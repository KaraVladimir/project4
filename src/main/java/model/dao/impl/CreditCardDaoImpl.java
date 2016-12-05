package model.dao.impl;

import model.dao.HaveUniqueField;
import model.dao.Identified;
import model.dao.exception.DaoException;
import model.entities.CreditCard;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Dao object for {@link CreditCard}
 * @author kara.vladimir2@gmail.com.
 */
public class CreditCardDaoImpl extends AbstractDaoImpl implements HaveUniqueField{
    public static final String QUERY_SAVE = "INSERT INTO creditCard("+Fields.CARD_NUMBER+
            ","+Fields.CARD_ID_CLIENT+","+Fields.CARD_ID_ACCOUNT+")" + " VALUES(?,?,?);";
    public static final String QUERY_SELECT_ALL = "SELECT * FROM creditCard " +
            "LEFT JOIN client ON("+Fields.CARD_ID_CLIENT+"="+Fields.CL_ID+")" +
            "LEFT JOIN account ON("+Fields.CARD_ID_ACCOUNT+"="+Fields.ACC_ID+")";
    public static final String QUERY_LAST_INSERT = QUERY_SELECT_ALL+
            "WHERE "+Fields.CARD_ID+" = last_insert_id();";
    public static final String QUERY_FIND_BY_PK = QUERY_SELECT_ALL+"WHERE "+Fields.CARD_ID+" = ?;";
    public static final String QUERY_FIND_BY_NUMBER = QUERY_SELECT_ALL+"WHERE "+Fields.CARD_NUMBER+" = ?;";
    public static final String QUERY_UPDATE = "UPDATE creditCard SET "+Fields.CARD_NUMBER+"= ? "
            +Fields.CARD_ID_CLIENT+" = ? "+Fields.CARD_ID_ACCOUNT+" = ? WHERE "+Fields.CARD_ID+"= ?;";
    public static final String QUERY_DELETE = "DELETE * FROM creditCard WHERE "+Fields.CARD_ID+" = ?;";

    public CreditCardDaoImpl(Connection connection) {
        super(connection);
    }

    @Override
    public String getSaveQuery() {
        return QUERY_SAVE;
    }

    @Override
    public String getLastInsertQuery() {
        return QUERY_LAST_INSERT;
    }

    @Override
    public String getByPKQuery() {
        return QUERY_FIND_BY_PK;
    }

    @Override
    public String getUpdateQuery() {
        return QUERY_UPDATE;
    }

    @Override
    public String getDeleteQuery() {
        return QUERY_DELETE;
    }

    @Override
    public String getSelectAllQuery() {
        return QUERY_SELECT_ALL;
    }

    @Override
    public PreparedStatement prepareStatementForUpdate(PreparedStatement preparedStatement, Identified identified) {
            CreditCard card = (CreditCard) identified;
            try {
                preparedStatement.setString(1, card.getCardNumber());
                preparedStatement.setInt(2, card.getClient().getID());
                preparedStatement.setInt(3, card.getAccount().getID());
            } catch (SQLException e) {
                throw new DaoException(ERR_UPDATE,e);
            }
            return preparedStatement;
    }

    @Override
    public PreparedStatement prepareStatementForSave(PreparedStatement preparedStatement, Identified identified) {
        CreditCard card = (CreditCard) identified;
        try {
            preparedStatement.setString(1, card.getCardNumber());
            preparedStatement.setInt(2, card.getClient().getID());
            preparedStatement.setInt(3, card.getAccount().getID());
            preparedStatement.setInt(4,card.getID());
        } catch (SQLException e) {
            throw new DaoException(ERR_SAVE,e);
        }
        return preparedStatement;
    }

    @Override
    public List<Identified> parseResultSetGen(ResultSet resultSet) {
        List<Identified> list = (List<Identified>) parseResultSet(resultSet);
        return list;
    }

    @Override
    public String getQueryFindByUniqueField() {
        return QUERY_FIND_BY_NUMBER;
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public List<? super CreditCard> parseResultSet(ResultSet rs) {
        ArrayList<CreditCard> listResult = new ArrayList<>();
        try {
            while (rs.next()) {
                listResult.add(parseResult(rs));
            }
        } catch (SQLException e) {
            throw new DaoException(ERR_PARSING, e);
        }
        return listResult;
    }

    @Override
    public CreditCard parseResult(ResultSet rs) {
        try {
            return new CreditCard(rs.getInt(Fields.CARD_ID),rs.getString(Fields.CARD_NUMBER));
        } catch (SQLException e) {
            throw new DaoException(ERR_PARSING, e);
        }
    }
}
