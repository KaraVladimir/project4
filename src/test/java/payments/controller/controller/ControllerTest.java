package payments.controller.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;
import payments.controller.commands.Command;
import payments.controller.commands.CommandKeeper;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.mockito.Mockito.*;

import org.mockito.Mock;

import java.util.List;

/**
 * @author kara.vladimir2@gmail.com.
 */
public class ControllerTest {
    @Mock
    HttpServletRequest mockedRequest;

    @Mock
    HttpServletResponse mockedResponse;

    @Mock
    HttpSession mockedSession;

    @Mock
    CommandKeeper mockedCommandKeeper;

    @Mock
    Command command;

    @Mock
    RequestDispatcher mockedDispatcher;

    @Captor
    ArgumentCaptor<String> dispatcherArgCaptor;

    private MainController testedController;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        testedController = new MainController();
        testedController.commandKeeper = mockedCommandKeeper;
        when(mockedRequest.getSession()).thenReturn(mockedSession);
        when(mockedSession.getAttribute(anyString())).thenReturn(1);
        when(mockedRequest.getRequestDispatcher(dispatcherArgCaptor.capture())).thenReturn(mockedDispatcher);
    }

    @Test
    public void testPostMethod() throws Exception {
        when(mockedRequest.getRequestURI()).thenReturn("path");
        when(mockedRequest.getMethod()).thenReturn("get");
        when(mockedCommandKeeper.get("path")).thenReturn(command);
        when(command.execute(mockedRequest, mockedResponse)).thenReturn("page");
        testedController.doPost(mockedRequest, mockedResponse);
        verify(mockedCommandKeeper, times(1)).get("path");
        verify(command, times(1)).execute(mockedRequest, mockedResponse);
        List<String> args = dispatcherArgCaptor.getAllValues();
        Assert.assertEquals("page", args.get(args.size() - 1));
    }

    @Test
    public void testForwardNull() throws Exception {
        when(mockedCommandKeeper.get(anyString())).thenReturn(command);
        when(command.execute(mockedRequest, mockedResponse)).thenReturn(null);
        testedController.process(mockedRequest, mockedResponse);
        verify(mockedResponse, times(1)).sendRedirect(mockedRequest.getContextPath());
        verify(mockedDispatcher, times(0)).forward(mockedRequest, mockedResponse);
    }
}
