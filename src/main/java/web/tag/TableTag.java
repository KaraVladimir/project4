package web.tag;

import model.entities.Account;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author kara.vladimir2@gmail.com.
 */
public class TableTag extends BodyTagSupport {
    private List<Account> accounts;
    private int size;
//    JspWriter writer;

    public void setSize(int size) {
        this.size = size;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
        size = accounts.size();
    }


    @Override
    public int doStartTag() throws JspException {
//        writer = pageContext.getOut();
//        Locale locale = new Locale(pageContext.getSession().getAttribute("language").toString());
//        ResourceBundle bundle = ResourceBundle.getBundle("msg", locale);

        try {
            pageContext.getOut().write("start tag");
//            writer.write("<table>");
//            writer.write("<tr>");
//            writer.write("<td>" + bundle.getString("table.acc.id") + "</td>");
//            writer.write("<td>" + bundle.getString("table.acc.number") + "</td>");
//            writer.write("<td>" + bundle.getString("table.acc.balance") + "</td>");
//            writer.write("<td>" + bundle.getString("table.acc.status") + "</td>");
//            writer.write(" </tr>");
        } catch (IOException e) {
            throw new JspTagException(e.getMessage());
        }
        return EVAL_BODY_BUFFERED;
    }


    //    <c:forEach items="${requestScope[Attrs.BLOCKED_ACCOUNTS]}" var="account">
//    <tr>
//    <td><c:out value="${account.getID()}"/></td>
//    <td><c:out value="${account.getAccountNumber()}"/></td>
//    <td><c:out value="${account.getAccountBalance()}"/></td>
//    <td>
//    <button name="${Attrs.ACCOUNT_ID}" value="${account.getID()}">
//    <img src="${!account.isBlocked()?Pages.PATH_BTN_FAIL:Pages.PATH_BTN_OK}"
//    style="height: 20px;width: 20px"></button>
//    </tr>
//    </c:forEach>

    @Override
    public int doAfterBody() throws JspException {
        return super.doAfterBody();
    }


//    @Override
//    public int doAfterBody() throws JspException {
//        try {
//            while (size >1 )
//            {
//                writer.write("O");
//                size--;
//                return EVAL_BODY_AGAIN;
//
//            }
//            return SKIP_BODY;
//
//
////            for (Account account : accounts) {
////                writer.write("<tr>");
////                writer.write("<td>" + account.getID() + "</td>");
////                writer.write("<td>" + account.getAccountNumber() + "</td>");
////                writer.write("<td>" + account.getAccountBalance() + "</td>");
////                writer.write("<td>" + account.isBlocked() + "</td>");
////                writer.write("</tr>");
////                return EVAL_BODY_AGAIN;
////            }
//        } catch (IOException e) {
//            throw new JspTagException(e.getMessage());
//        }
////        return SKIP_BODY;
//    }

    @Override
    public int doEndTag() throws JspException {
        try {
//            writer.write("</table>");
            pageContext.getOut().write(""+size);
            pageContext.getOut().write("OK");
        } catch (IOException e) {
            throw new JspTagException(e.getMessage());
        }
        return EVAL_PAGE;
    }
}
