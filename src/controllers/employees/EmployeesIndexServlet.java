package controllers.employees;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import utils.DBUtil;

/**
 * Servlet implementation class EmployeesIndexServlet
 */
@WebServlet("/employees/index")
public class EmployeesIndexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmployeesIndexServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //DBに接続する(EIS→DBUtil→DB)
        EntityManager em = DBUtil.createEntityManager();

        //開くページ数を取得
        int page = 1;
        try {
            //全件数のうち、1ページ目をviewで開く為のgetParameter
            page = Integer.parseInt(request.getParameter("page"));
            System.out.println("page");
        } catch (NumberFormatException e) {
        }

        //DBより"getAllEmployees"を取得(EIS→Employee.java→DBUtil.java→DB)
        List<Employee> employees = em.createNamedQuery("getAllEmployees", Employee.class)
                //全件数のうち、1ページにいくつ表示するかの設定
                .setFirstResult(15 * (page - 1))
                .setMaxResults(15)
                .getResultList();

        //DBより"getEmployeesCount"で全件数を取得(EIS→Employee.java→DBUtil.java→DB)
        long employees_count = (long) em.createNamedQuery("getEmployeesCount", Long.class)
                .getSingleResult();

        //DB閉じる
        em.close();

        //index.jspに上で取得したデータを送る
        request.setAttribute("employees", employees);
        request.setAttribute("employees_count", employees_count);
        request.setAttribute("page", page);

        //フラッシュメッセージ
        if (request.getSession().getAttribute("flush") != null) {
            request.setAttribute("flush", request.getSession().getAttribute("flush"));
            request.getSession().removeAttribute("flush");
        }

        //送り先の指定(リクエストスコープは受け/取る、一回まで)
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/employees/index.jsp");
        rd.forward(request, response);
    }
}