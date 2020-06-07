package controllers.employees;

import java.io.IOException;
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
import models.validators.EmployeeValidator;
import utils.DBUtil;
import utils.EncryptUtil;

/**
 * Servlet implementation class EmployeesUpdateServlet
 */
@WebServlet("/employees/update")
public class EmployeesUpdateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmployeesUpdateServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //POST送信だから、edit.jspから"_token"を受ける(Stringのまま)
        String _token = (String) request.getParameter("_token");

        //_tokenがnullじゃなかったら かつ、 _tokenとgetId()を比較して同じ文字列だったら
        if (_token != null && _token.equals(request.getSession().getId())) {

            //DBから変数emを取得
            EntityManager em = DBUtil.createEntityManager();

            //edit.jspからString "employee_id"を/updateで受ける。
            //em.findで"employee_id"に相当するintの数値を取得し変数eに入れる
            Employee e = em.find(Employee.class, (Integer) (request.getSession().getAttribute("employee_id")));

            //重複チェック
            Boolean code_duplicate_check = true;
            //edit.jspから"code"を受け取る
            //e.getCode()と"code"がtrue(重複してる)ならcode_duplicate_check = false(行わない)
            if (e.getCode().equals(request.getParameter("code"))) {
                code_duplicate_check = false;

            } else {
                e.setCode(request.getParameter("code"));
            }

            // パスワード欄に入力があったら
            // パスワードの入力値チェックを行う指定をする
            Boolean password_check_flag = true;

            //edit.jspから"password"を受け取る
            String password = request.getParameter("password");

            //passwordがnull or passwordが("")空欄なら、password_check_flag = false;(行わない)
            if (password == null || password.equals("")) {
                password_check_flag = false;

                //その他の場合、EncryptUtilの"salt"よりパスワード変換が行われる
            } else {
                e.setPassword(
                        EncryptUtil.getPasswordEncrypt(password,
                                (String) this.getServletContext().getAttribute("salt")));
            }

            // _checkを行わない他の要素
            //変数eで"name"を受ける
            e.setName(request.getParameter("name"));
            e.setDepartment(request.getParameter("department"));
            e.setAdmin_flag(Integer.parseInt(request.getParameter("admin_flag")));

            // 自動生成
            e.setUpdated_at(new Timestamp(System.currentTimeMillis()));
            e.setDelete_flag(0);

            //バリデーション
            List<String> errors = EmployeeValidator.validate(e, code_duplicate_check, password_check_flag);

            //errorが0より大きいならedit.jspに"_token", "employee", "errors"をDB更新せずそのまま返す
            if (errors.size() > 0) {
                em.close();

                request.setAttribute("_token", request.getSession().getId());
                request.setAttribute("employee", e);
                request.setAttribute("errors", errors);

                RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/employees/edit.jsp");
                rd.forward(request, response);

                //問題なければDBを更新して閉じる
            } else {
                em.getTransaction().begin();
                em.getTransaction().commit();
                em.close();

                //フラッシュメッセージ
                request.getSession().setAttribute("flush", "更新が完了しました。");

                //セッションスコープを除去
                request.getSession().removeAttribute("employee_id");

                //index.jspへ飛ぶ
                response.sendRedirect(request.getContextPath() + "/employees/index");
            }

        }
    }
}