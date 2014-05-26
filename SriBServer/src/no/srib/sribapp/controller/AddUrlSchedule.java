package no.srib.sribapp.controller;

import java.io.IOException;
import java.sql.Time;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import no.srib.sribapp.dao.exception.DAOException;
import no.srib.sribapp.dao.interfaces.StreamUrlScheduleDAO;
import no.srib.sribapp.dao.interfaces.StreamurlDAO;
import no.srib.sribapp.model.Streamurlschedule;

/**
 * Servlet implementation class AddUrlSchedule
 */
@WebServlet("/AddUrlSchedule")
public class AddUrlSchedule extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @EJB
    private StreamUrlScheduleDAO streamUrlScheduleDAO;
    @EJB
    private StreamurlDAO streUrlDAO;

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession ses = request.getSession();

        if (ses != null && ses.getAttribute("loggedIn") != null) {
            if (ses.getAttribute("loggedIn").equals("true")) {
                ses.setAttribute("errorNew", new Boolean(false));

                String fromTimeString = request.getParameter("fromTime");
                String toTimeString = request.getParameter("toTime");
                String dayString = request.getParameter("day");
                Time fromTime = null;
                Time toTime = null;
                int day = 0;

                try {
                    if (fromTimeString != null && toTimeString != null
                            && dayString != null) {
                        if (fromTimeString.length() == 5) {
                            fromTimeString += ":00";
                        }
                        if (toTimeString.length() == 5) {
                            toTimeString += ":00";
                        }
                        fromTime = Time.valueOf(fromTimeString);
                        toTime = Time.valueOf(toTimeString);
                        day = Integer.valueOf(dayString);
                        if (!fromTime.before(toTime)) {
                            throw new IllegalArgumentException();
                        }
                    } else {

                        throw new IllegalArgumentException();
                    }

                } catch (IllegalArgumentException e) {
                    ses.setAttribute("errorNew", new Boolean(true));
                    response.sendRedirect("/SriBServer/SetSource");
                    return;
                }
                Streamurlschedule streamurlschedule = new Streamurlschedule();
                streamurlschedule.setDay((byte) day);
                streamurlschedule.setFromtime(fromTime);
                streamurlschedule.setTotime(toTime);

                try {
                    streamUrlScheduleDAO.add(streamurlschedule);
                } catch (DAOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                response.sendRedirect("/SriBServer/SetSource");
                return;
            }
        } else {
            response.sendRedirect("index.html");
            return;

        }

    }

}
