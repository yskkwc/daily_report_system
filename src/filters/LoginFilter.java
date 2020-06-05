package filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import models.Employee;

/**
 * Servlet Filter implementation class LoginFilter
 */
@WebFilter("/***")
public class LoginFilter implements Filter {

    /**
     * Default constructor.
     */
    public LoginFilter() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @see Filter#destroy()
     */
    public void destroy() {
        // TODO Auto-generated method stub
    }

    /**
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String context_path = ((HttpServletRequest) request).getContextPath();
        String servlet_path = ((HttpServletRequest) request).getServletPath();

        // CSSフォルダ内は認証処理から除外する
        if (!servlet_path.matches("/css.*")) {
            HttpSession session = ((HttpServletRequest) request).getSession();

            // LoginServletでセッションスコープに保存された"login_employee"を取得、eにしまう
            Employee e = (Employee) session.getAttribute("login_employee");

            // servlet_pathの中身が"/login"でないとき〜
            if (!servlet_path.equals("/login")) {
                // 〜にeがnullのとき
                if (e == null) {

                    //LoginServletに返す
                    ((HttpServletResponse) response).sendRedirect(context_path + "/login");
                    return;
                }

                // 従業員管理の機能は管理者のみが閲覧できるようにする
                if (servlet_path.matches("/employees.*") && e.getAdmin_flag() == 0) {
                    // 0=一般従業員
                    ((HttpServletResponse) response).sendRedirect(context_path + "/");
                    return;
                }
            } else { // ログイン画面について
                // ログインしているのにログイン画面を表示させようとした場合は
                // システムのトップページにリダイレクト
                if (e != null) {
                    ((HttpServletResponse) response).sendRedirect(context_path + "/");
                    return;
                }
            }
        }

        chain.doFilter(request, response);
    }

    /**
     * @see Filter#init(FilterConfig)
     */
    public void init(FilterConfig fConfig) throws ServletException {
        // TODO Auto-generated method stub
    }

}