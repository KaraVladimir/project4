package payments.controller.service;

import data.test.Accounts;
import data.test.Cards;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import payments.exception.AppException;
import payments.model.dao.*;
import payments.model.entities.Account;
import payments.model.entities.CreditCard;
import payments.model.entities.Payment;
import payments.service.AccountService;
import payments.service.exception.ServiceException;
import payments.service.impl.AccountServiceImpl;

import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author kara.vladimir2@gmail.com.
 */
public class AccountServiceImplTest {
    @Mock
    private DaoFactory daoFactory;
    @Mock
    private Connection connection;
    @Mock
    private DaoManager daoManager;
    @Mock
    private AccountDao mockedAccountDao;
    @Mock
    private PaymentDao mockedPaymentDao;
    @Mock
    private CreditCardDao mockedCardDao;
    @Mock
    DaoCommand daoCommand;
    @Captor
    private ArgumentCaptor<Account> accountArgumentCaptor;
    @Captor
    private ArgumentCaptor<Payment> paymentArgumentCaptor;
    @Captor
    private ArgumentCaptor<Integer> integerArgumentCaptor;

    @InjectMocks
    private AccountService service = AccountServiceImpl.getInstance();

    private Integer accIdS;
    private String numberR;
    private Integer accIdR;
    private static List<Account> accounts = initAccounts();

    private static List<Account> initAccounts() {
        List<Account> accounts = new ArrayList<>();
        for (Accounts accounts1 : Accounts.values()) {
            accounts.add(accounts1.account);
        }
        return accounts;
    }

    private static Account findAccById(Integer id) {
        return accounts.stream().filter(account -> account.getID().equals(id)).collect(Collectors.toList()).get(0);
    }

    private static Account findAccByNumber(String number) {
        List<Account> list = accounts.stream().filter(account -> account.getAccountNumber().equals(number)).collect(Collectors.toList());
        if (list.size() > 0) return list.get(0);
        else return null;
    }

    @Before
    public void init() throws AppException, SQLException {
        MockitoAnnotations.initMocks(this);
        when(daoFactory.getDaoManager()).thenReturn(daoManager);
        when(daoManager.getConnection()).thenReturn(connection);
        when(daoManager.getDao(Account.class)).thenReturn(mockedAccountDao);
        when(daoManager.getDao(Payment.class)).thenReturn(mockedPaymentDao);
        when(daoManager.getDao(CreditCard.class)).thenReturn(mockedCardDao);
//        when(daoManager.transaction(daoCommand)).thenReturn();
//        when(mockedUserDao.findUserByLogin(anyString())).thenReturn(Users.USER1.user);
    }

    @Test
    public void findAccountByNumberNormalTest() throws AppException {
        when(mockedAccountDao.findAccountByNumber(anyString())).thenReturn(Accounts.ACC1.account);
        Account account = service.findAccountByNumber("1234");
        verify(mockedAccountDao, times(1)).findAccountByNumber(Accounts.ACC1.account.getAccountNumber());
        Assert.assertEquals(account, Accounts.ACC1.account);
    }

    @Test(expected = ServiceException.class)
    public void findAccountByNumberNullTest() throws AppException {
        when(mockedAccountDao.findAccountByNumber(anyString())).thenReturn(null);
        service.findAccountByNumber(numberR);
    }

    @Test
    public void payTestNormal() throws AppException {
        accIdS = Accounts.ACC1.account.getID();
        numberR = Accounts.ACC2.account.getAccountNumber();
        accIdR = Accounts.ACC2.account.getID();
        BigDecimal amount = new BigDecimal("10");
        BigDecimal balanceS = Accounts.ACC1.account.getAccountBalance();
        BigDecimal balanceR = Accounts.ACC2.account.getAccountBalance();

        when(mockedAccountDao.findByPKForUpdate(accIdS)).thenReturn(findAccById(accIdS));
        when(mockedAccountDao.findAccountByNumber(numberR)).thenReturn(findAccByNumber(numberR));
        when(mockedAccountDao.findByPKForUpdate(accIdR)).thenReturn(findAccById(accIdR));
        when(mockedCardDao.findCreditCardByAccount(any())).thenReturn(Cards.CARD1.card);
        when(mockedPaymentDao.save(any())).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);
        service.pay(accIdS, numberR, amount);
        Assert.assertEquals(Accounts.ACC1.account.getAccountBalance(), balanceS.subtract(amount));
        Assert.assertEquals(Accounts.ACC2.account.getAccountBalance(), balanceR.add(amount));
    }

    @Test(expected = ServiceException.class)
    public void payTestNotEnoughMoney() throws AppException {
        accIdS = Accounts.ACC1.account.getID();
        numberR = Accounts.ACC2.account.getAccountNumber();
        accIdR = Accounts.ACC2.account.getID();
        BigDecimal amount = new BigDecimal("10000");

        when(mockedAccountDao.findByPKForUpdate(accIdS)).thenReturn(findAccById(accIdS));
        when(mockedAccountDao.findAccountByNumber(numberR)).thenReturn(findAccByNumber(numberR));
        when(mockedAccountDao.findByPKForUpdate(accIdR)).thenReturn(findAccById(accIdR));
        when(mockedCardDao.findCreditCardByAccount(any())).thenReturn(Cards.CARD1.card);
        when(mockedPaymentDao.save(any())).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);
        service.pay(accIdS, numberR, amount);
    }

    @Test(expected = ServiceException.class)
    public void payTestRecipientDoesNotExist() throws AppException {
        accIdS = Accounts.ACC1.account.getID();
        numberR = "9999";
        accIdR = Accounts.ACC2.account.getID();
        BigDecimal amount = new BigDecimal("10");

        when(mockedAccountDao.findByPKForUpdate(accIdS)).thenReturn(findAccById(accIdS));
        when(mockedAccountDao.findAccountByNumber(numberR)).thenReturn(findAccByNumber(numberR));
        when(mockedAccountDao.findByPKForUpdate(accIdR)).thenReturn(findAccById(accIdR));
        when(mockedCardDao.findCreditCardByAccount(any())).thenReturn(Cards.CARD1.card);
        when(mockedPaymentDao.save(any())).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);
        service.pay(accIdS, numberR, amount);
    }

}
