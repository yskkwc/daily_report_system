package controllers.reports;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Report;
import utils.DBUtil;

/**
 * Servlet implementation class ReportsDestroyServlet
 */
@WebServlet("/reports/destroy")
public class ReportsDestroyServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportsDestroyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        String _token = (String) request.getParameter("_token");

        if (_token != null && _token.equals(request.getSession().getId())) {
            EntityManager em = DBUtil.createEntityManager();

            Report r = em.find(Report.class, (Integer) (request.getSession().getAttribute("report_id")));

            //DB操作
            em.getTransaction().begin();
            em.remove(r);// データ削除
            em.getTransaction().commit();
            request.getSession().setAttribute("flush", "削除が完了しました。");
            em.close();

            // セッションスコープ上の不要になったデータを削除
            request.getSession().removeAttribute("report_id");

            // indexページへリダイレクト
            response.sendRedirect(request.getContextPath() + "/reports/index");
        }
    }
}