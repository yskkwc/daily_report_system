<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:import url="/WEB-INF/views/layout/app.jsp">
  <c:param name="content">
    <c:choose>
      <c:when test="${report != null}">
        <h2>日報 詳細ページ</h2>
        <table>
          <c:choose>
            <c:when test="${report.publish == 1}">
              <c:import url="detail.jsp" />
            </c:when>
            <c:when
              test="${report.publish == 2 && sessionScope.login_employee.id
            == report.employee.id || sessionScope.login_employee.admin_flag == 1}">
              <c:import url="detail.jsp" />
            </c:when>
            <c:when
              test="${report.publish == 3 && sessionScope.login_employee.id
            == report.employee.id || sessionScope.login_employee.department == report.employee.department
            || sessionScope.login_employee.admin_flag == 1}">
              <c:import url="detail.jsp" />
            </c:when>
            <c:otherwise>
              <h3 class="etsuran">
                <c:out value="※この日報の閲覧権限がありません" />
              </h3>
        <tr>
          <th>公開範囲</th>
          <c:if test="${report.publish == 1}">
            <td><c:out value="全員に公開" /></td>
          </c:if>
          <c:if test="${report.publish == 2}">
            <td><c:out value="自分と管理者にのみ公開" /></td>
          </c:if>
          <c:if test="${report.publish == 3}">
            <td><c:out value="部署内にのみ公開" /></td>
          </c:if>
        </tr>
        <tr>


            </c:otherwise>
          </c:choose>
        </table>
        <!-- 書いた人と参照してる人のidが一致しないと編集できない！ -->
        <c:if test="${sessionScope.login_employee.id == report.employee.id}">
          <p>
            <a href="<c:url value="/reports/edit?id=${report.id}" />">この日報を編集する</a>
          </p>
        </c:if>
        <!--<c:if test="${sessionScope.login_employee.id == report.employee.id}">
          <p >
            <a href="#" onclick="confirmDestroy();">この履歴を削除する</a>
          </p>
        <form method="POST"
          action="${pageContext.request.contextPath}/reports/destroy">
          <input type="hidden" name="_token" value="${_token}" />
        </form>
        <script>
          function confirmDestroy() {
              if(confirm("本当に削除しますか？")) {
                  document.forms[0].submit();
              }
          }
          </script>
        <p>
        </c:if>-->
      </c:when>
      <c:otherwise>
        <h2>お探しのデータは見つかりませんでした。</h2>
      </c:otherwise>
    </c:choose>

    <p>
      <a href="<c:url value="/reports/index" />">一覧に戻る</a>
    </p>
  </c:param>
</c:import>