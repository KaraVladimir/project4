package payments.view.tag;

import payments.model.entities.Account;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Class for custom tag
 * @author kara.vladimir2@gmail.com.
 */
public class TableTagExample extends TagSupport {
    private Locale locale;
    private JspWriter writer;
    private ResourceBundle bundle;
    private List<Account> accounts;
    private Iterator<Account> iterator;

    public void setAccounts(List<Account> accounts) {
        iterator = accounts.iterator();
        this.accounts = accounts;
    }

    @Override
    public int doStartTag() throws JspException {
        writer = pageContext.getOut();
        locale = new Locale(pageContext.getSession().getAttribute("language").toString());
        bundle = ResourceBundle.getBundle("msg", locale);
        try {
            writer.write("<table>");
            writer.write("<tr>");
            writer.write("<td>" + bundle.getString("table.acc.id") + "</td>");
            writer.write("<td>" + bundle.getString("table.acc.number") + "</td>");
            writer.write("<td>" + bundle.getString("table.acc.balance") + "</td>");
            writer.write("<td>" + bundle.getString("table.acc.status") + "</td>");
            writer.write(" </tr>");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return EVAL_BODY_INCLUDE;
    }


    @Override
    public int doAfterBody() throws JspException {
        if (iterator.hasNext()) {
            Account account = iterator.next();
            try {
                writer.write("<tr>");
                writer.write("<td>" + account.getID() + "</td>");
                writer.write("<td>" + account.getAccountNumber() + "</td>");
                writer.write("<td>" + account.getAccountBalance() + "</td>");
                String s = (account.isBlocked()) ? bundle.getString("table.acc.block") : bundle.getString("table.acc.unblock");
                writer.write("<td>" + s + "</td>");
                writer.write("</tr>");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return EVAL_BODY_AGAIN;
        } else {
            return SKIP_BODY;
        }
    }

    @Override
    public int doEndTag() throws JspException {
        try {
            writer.write("</table>");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return EVAL_PAGE;
    }
}
