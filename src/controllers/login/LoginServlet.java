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
    //ログイン画面を表示
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("_token", request.getSession().getId());
        request.setAttribute("hasError", false);
        if (request.getSession().getAttribute("flush") != null) {
            request.setAttribute("flush", request.getSession().getAttribute("flush"));
            request.getSession().removeAttribute("flush");
        }

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/login/login.jsp");
        rd.forward(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    // ログイン処理を実行
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 認証結果を格納する変数
        Boolean check_result = false;

        String code = request.getParameter("code");
        String plain_pass = request.getParameter("password");

        Employee e = null;

        // まず、社員番号とパスワードは空欄じゃないか確認
        if (code != null && !code.equals("") && plain_pass != null && !plain_pass.equals("")) {
            // login.jspから送られた"code"と"password"をDBへ送る
            EntityManager em = DBUtil.createEntityManager();

            // そのうちplain_pass("password")についてはimport utils.EncryptUtilで暗号化
            String password = EncryptUtil.getPasswordEncrypt(
                    plain_pass,
                    (String) this.getServletContext().getAttribute("salt"));

            // Employee.javaで同じidのcodeカラムとpasswordカラムから値を変数eで受ける
            try {
                e = em.createNamedQuery("checkLoginCodeAndPassword", Employee.class)
                        // ここでも受ける
                        .setParameter("code", code)
                        .setParameter("pass", password)
                        .getSingleResult();

                /*クエリーのQuery.getSingleResult()やTypedQuery.getSingleResult()が実行され、
                 * 結果が見つからなかった場合に永続化プロバイダによって投げられます。*/
            } catch (NoResultException ex) {
            }

            em.close();

            // eがnullじゃなかったらtrue(エラーがある状態)
            if (e != null) {
                check_result = true;
            }
        }
        // 「check_result」でない場合、
        if (!check_result) {

            // 認証できなかったらログイン画面に戻る
            request.setAttribute("_token", request.getSession().getId());
            request.setAttribute("hasError", true);
            request.setAttribute("code", code);

            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/login/login.jsp");
            rd.forward(request, response);
        } else {
            // 認証できたらログイン状態にしてトップページへリダイレクト
            // eは『セッションスコープ』で"login_employee"にして渡す
            // 次どこでremoveするか注意
            request.getSession().setAttribute("login_employee", e);

            // "ログインしました。"は『セッションスコープ』で"flush"にして渡す
            // 次どこでremoveするか注意
            request.getSession().setAttribute("flush", "ログインしました。");
            response.sendRedirect(request.getContextPath() + "/"); // /topPage/index.jsp
        }
    }

}