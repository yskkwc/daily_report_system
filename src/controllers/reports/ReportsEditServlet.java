package controllers.reports;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.Report;
import utils.DBUtil;

/**
 * Servlet implementation class ReportsEditServlet
 */
@WebServlet("/reports/edit")
public class ReportsEditServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportsEditServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();

        // DBへ"id"をInt型にして送り、この"id"の情報をeditservletで受けて、rにしまう
        Report r = em.find(Report.class, Integer.parseInt(request.getParameter("id")));

        em.close();

        Employee login_employee = (Employee) request.getSession().getAttribute("login_employee");

        // 変数rがnullじゃないかつ、/loginから取得したlogin_employeeと変数rが受けた
        // getParameterの"id"が同じ（ログイン者と編集しようとしているひとが同じ)とき
        if (r != null && login_employee.getId() == r.getEmployee().getId()) {

            // 受けた情報をedit.jspへ流す
            request.setAttribute("report", r);
            request.setAttribute("_token", request.getSession().getId());
            request.getSession().setAttribute("report_id", r.getId());
        }

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/reports/edit.jsp");
        rd.forward(request, response);
    }

}