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

import models.Employee;
import models.Report;
import models.validators.ReportValidator;
import utils.DBUtil;

/**
 * Servlet implementation class ReportsCreateServlet
 */
@WebServlet("/reports/create")
public class ReportsCreateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportsCreateServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // new.jspから"_token"を呼んでString型 _tokenにする
        String _token = (String) request.getParameter("_token");
        if (_token != null && _token.equals(request.getSession().getId())) {
            EntityManager em = DBUtil.createEntityManager();

            Report r = new Report();
            // LoginServletで作った"login_employee"をset
            r.setEmployee((Employee) request.getSession().getAttribute("login_employee"));

            // rのうちreport_dateは、新規の時間を取得
            Date report_date = new Date(System.currentTimeMillis());

            // new.jspから取った"report_date"をrd_strにする
            String rd_str = request.getParameter("report_date");
            if (rd_str != null && !rd_str.equals("")) {
                // チェックOKなら改めてreport_dateで受ける
                report_date = Date.valueOf(request.getParameter("report_date"));
            }

            //変数rに各情報をセット
            r.setReport_date(report_date);
            r.setTitle(request.getParameter("title"));
            r.setContent(request.getParameter("content"));

            // 現在の時間(登録した時間)は自動でTimestamp currentTimeで取得
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            r.setCreated_at(currentTime);
            r.setUpdated_at(currentTime);

            //値をsetした変数rをバリデーション に通す
            List<String> errors = ReportValidator.validate(r);
            // エラーが検出されればDB閉じて値はr="report",エラー情報はerrors="errors"
            // getId()は"_token"にしてnew.jspに返す
            if (errors.size() > 0) {
                em.close();

                request.setAttribute("_token", request.getSession().getId());
                request.setAttribute("report", r);
                request.setAttribute("errors", errors);

                RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/reports/new.jsp");
                rd.forward(request, response);
            } else {
                // DBの処理を進める
                em.getTransaction().begin();
                em.persist(r);
                em.getTransaction().commit();
                em.close();
                request.getSession().setAttribute("flush", "登録が完了しました。");

                // リダイヤル index.jspへ
                response.sendRedirect(request.getContextPath() + "/reports/index");
            }
        }

    }
}