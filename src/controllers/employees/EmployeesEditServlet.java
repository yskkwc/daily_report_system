package controllers.employees;

import java.io.IOException;

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
 * Servlet implementation class EmployeesEditServlet
 */
@WebServlet("/employees/edit")
public class EmployeesEditServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmployeesEditServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();

        //show.jspからString "id"を/Editで受ける。idなのでint型に変換した上、
        //em.findでidに相当する((例)5=DB上のid5のついたカラム),を変数eに入れる
        Employee e = em.find(Employee.class, Integer.parseInt(request.getParameter("id")));

        em.close();

        //変数eをStringの"employee"にしてedit.jspへ渡す
        request.setAttribute("employee", e);

        //Token要素のId属性を取得して、"_token"にしてedit.jspへ渡す
        //--request.getSession（）getId（）--はサーバーのセッションIDを返す。
        //（セッションが存在しない場合、request.getSession（）が作成)
        //セッションスコープで変数eのidを"employee_id"にしてedit.jspへ渡す
        request.setAttribute("_token", request.getSession().getId());

        request.getSession().setAttribute("employee_id", e.getId());

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/employees/edit.jsp");
        rd.forward(request, response);
    }
}
