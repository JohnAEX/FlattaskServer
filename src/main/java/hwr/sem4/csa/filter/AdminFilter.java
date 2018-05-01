package hwr.sem4.csa.filter;

import hwr.sem4.csa.managedBeans.LoginManagedBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AdminFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse responseNow = (HttpServletResponse) res;
        HttpSession session = ((HttpServletRequest) req).getSession(false);
        LoginManagedBean loginManagedBean = (session != null) ? (LoginManagedBean) session.getAttribute("LoginManagedBean") : null;
        if (loginManagedBean != null && loginManagedBean.isLoggedIn() && loginManagedBean.loggedInUser.getRole().equalsIgnoreCase("admin")) {
            // Logged in.
            chain.doFilter(req, res);
        }else{
            //Not logged in
            responseNow.sendRedirect("/login.xhtml");
        }



    }

    @Override
    public void destroy() {

    }
}
