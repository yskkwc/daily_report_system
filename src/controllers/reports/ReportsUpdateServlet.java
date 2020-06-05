package controllers.reports;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Report;
import models.validators.ReportValidator;
import utils.DBUtil;

/**
 * Servlet implementation class ReportsUpdateServlet
 */
@WebServlet("/reports/update")
public class ReportsUpdateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportsUpdateServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // edit.jspで参照してる_form.jsp内の"_token"
        String _token = (String) request.getParameter("_token");

        // 空チェック
        if (_token != null && _token.equals(request.getSession().getId())) {
            EntityManager em = DBUtil.createEntityManager();

            // DBへ"id"をInt型にして送り、この"id"の情報をupdateservletで受けて、rにしまう
            Report r = em.find(Report.class, (Integer) (request.getSession().getAttribute("report_id")));

            // rのうちreport_dateは、新規の時間を取得
            Date report_date = new Date(System.currentTimeMillis());

            // new.jspから取った"report_date"をrd_strにする
            String rd_str = request.getParameter("report_date");
            if (rd_str != null && !rd_str.equals("")) {
                // チェックOKなら改めてreport_dateで受ける
                report_date = Date.valueOf(request.getParameter("report_date"));
            }

            r.setReport_date(report_date);
            r.setTitle(request.getParameter("title"));
            r.setContent(request.getParameter("content"));
            r.setPublish(Integer.parseInt(request.getParameter("publish")));

            r.setUpdated_at(new Timestamp(System.currentTimeMillis()));

            List<String> errors = ReportValidator.validate(r);
            if (errors.size() > 0) {
                em.close();

                request.setAttribute("_token", request.getSession().getId());
                request.setAttribute("report", r);
                request.setAttribute("errors", errors);

                RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/reports/edit.jsp");
                rd.forward(request, response);

            } else {
                em.getTransaction().begin();
                em.getTransaction().commit();
                em.close();
                request.getSession().setAttribute("flush", "更新が完了しました。");

                // セッションスコープ除外
                request.getSession().removeAttribute("report_id");

                response.sendRedirect(request.getContextPath() + "/reports/index");
            }

        }
    }
}