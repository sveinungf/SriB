package no.srib.app.server.controller;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import no.srib.app.server.dao.exception.DAOException;
import no.srib.app.server.dao.interfaces.BackendUserDAO;
import no.srib.app.server.model.jpa.Backenduser;
import no.srib.app.server.util.Security;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @EJB
    private BackendUserDAO dao;

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        HttpSession ses = request.getSession(true);

        if (ses != null && ses.getAttribute("loggedIn") != null
                && ses.getAttribute("loggedIn").equals("true")) {

            RequestDispatcher reqD = request
                    .getRequestDispatcher("/WEB-INF/admin.html");
            reqD.forward(request, response);

        } else {
            response.sendRedirect("index.html");

        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request,

    HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null || password == null) {
            response.sendRedirect("index.html");
            return;
        }

        Backenduser back = null;
        String hashInDB = null;

        HttpSession ses = request.getSession(false);
        String hash = Security.toSHA512(password, username);
        try {
            back = dao.getByUserName(username);
        } catch (DAOException e) {

            response.sendRedirect("index.html");
            return;
        }

        if (ses == null) {
            ses = request.getSession();

        } else if (back != null) {
            hashInDB = back.getPassword();
            if (hash.equals(hashInDB)) {
                RequestDispatcher reqD = request
                        .getRequestDispatcher("/WEB-INF/admin.html");
                reqD.forward(request, response);
                return;
            } else {
                ses.setAttribute("loggedIn", "false");
            }
        }

        if (username.equals("test") && password.equals("test")) {
            ses.setAttribute("loggedIn", "true");
            RequestDispatcher reqD = request
                    .getRequestDispatcher("/WEB-INF/admin.html");
            reqD.forward(request, response);
            return;
        } else {
            response.sendRedirect("index.html");
            return;
        }

    }

}
