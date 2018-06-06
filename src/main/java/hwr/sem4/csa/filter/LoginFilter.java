package hwr.sem4.csa.filter;

import hwr.sem4.csa.managedBeans.LoginManagedBean;
import hwr.sem4.csa.util.Participator;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter{


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse responseNow = (HttpServletResponse) res;
        HttpSession session = ((HttpServletRequest) req).getSession(false);
        //Session is used to check for LoginManagedBeans
        LoginManagedBean loginManagedBean = (session != null) ? (LoginManagedBean) session.getAttribute("LoginManagedBean") : null;
        System.out.println("\t> loginManagedBean: " + loginManagedBean + " - logged in: " + loginManagedBean.isLoggedIn());
        if (loginManagedBean != null && loginManagedBean.isLoggedIn()) {
            // Logged in, forward request
            chain.doFilter(req, res);
        }else{
            //Not logged in, redirect to login.xhtml
            responseNow.sendRedirect("/login.xhtml");

        }


    }

    @Override
    public void destroy() {

    }
}
