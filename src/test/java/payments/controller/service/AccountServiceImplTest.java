package payments.controller.service;

import data.test.Accounts;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import payments.exception.AppException;
import payments.model.dao.*;
import payments.model.dao.exception.DaoException;
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
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    private Integer accId;
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
        return accounts.stream().filter(account -> account.getAccountNumber().equals(number)).collect(Collectors.toList()).get(0);
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
        Assert.assertEquals(account,Accounts.ACC1.account);
    }

    @Test(expected = ServiceException.class)
    public void findAccountByNumberNullTest() throws AppException {
        when(mockedAccountDao.findAccountByNumber(anyString())).thenReturn(null);
        Account account = service.findAccountByNumber("1234");
    }

    @Test
    public void payTest() throws AppException {
        accId = Accounts.ACC1.account.getID();
        when(mockedAccountDao.findByPKForUpdate(accId)).thenReturn(findAccById(accId));
        when(daoManager.transaction(Mockito.<DaoCommand>any())).thenAnswer(invocationOnMock -> {
            Object[] args = invocationOnMock.getArguments();
            DaoCommand arg = (DaoCommand) args[0];
            Object o = daoCommand.execute(daoManager);
            return o;
        });
//        when()

        service.pay(accId, Accounts.ACC2.account.getAccountNumber(), new BigDecimal("10"));
    }
}
