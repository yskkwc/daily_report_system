package controllers.login;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import utils.DBUtil;
import utils.EncryptUtil;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    //ログイン画面の表示
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // /login→login.jspへ送る
        request.setAttribute("_token", request.getSession().getId());
        request.setAttribute("hasError", false);

        // セッションスコープ(どこでsetAttribute?) でgetAttributeした"flush"がnullじゃなかったら
        if(request.getSession().getAttribute("flush") != null){

            // /login→login.jspへ送る
            request.setAttribute("flush", request.getSession().getAttribute("flush"));

            // セッションスコープ取り除く
            request.getSession().removeAttribute("flush");
        }

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/login/login.jsp");
        rd.forward(request,  response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    // ログイン処理
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 認証結果を格納する
        Boolean check_result = false;
        // ブラウザから送信された"code"をStringのままcodeにする
        String code = request.getParameter("code");
        // ブラウザから送信された"password"をStringのまま"plain_passにする
        String plain_pass = request.getParameter("password");


        Employee e = null;

        // codeがnullじゃないかつ、空欄じゃない(String)
        if(code != null && !code.equals("") && plain_pass != null && !plain_pass.equals("")){
            EntityManager em = DBUtil.createEntityManager();

            // パスワードを暗号化する
            String password = EncryptUtil.getPasswordEncrypt(
                    plain_pass,
                    (String)this.getServletContext().getAttribute("salt"));

            // 社員番号とパスワードが正しいかチェック
            try{
                e = em.createNamedQuery("checkLoginCodeAndPassword", Employee.class)
            //"SELECT e FROM Employee AS e WHERE e.delete_flag = 0
            //"AND e.code = :code AND e.password = :pass")
            //e.code = :code, e.password = :passの場合値はnullになる

                        .setParameter("code", code)
                        .setParameter("pass", password)
                        .getSingleResult();
            } catch(NoResultException ex){}

            em.close();

            if(e != null){
                check_result = true;

            }
        }

        // もし変数check_resultがtrueだったら,認証できないとなりログイン画面に戻る。
        if(!check_result){
            request.setAttribute("_token", request.getSession().getId());
            request.setAttribute("hasError", true);
            request.setAttribute("code", code);

            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/login/login.jsp");
            rd.forward(request, response);
        } else {
            // 認証できたらログイン状態にしてトップページに行く
            // セッションスコープで"logon_employee"(= e =login.jspから受けた
            //"code", "password"がDBのcode, passwordと == になったとき(false)
            request.getSession().setAttribute("login_employee", e);

            request.getSession().setAttribute("flush", "ログインしました。");
            response.sendRedirect(request.getContextPath() + "/");
        }
    }
}
