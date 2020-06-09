<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:import url="/WEB-INF/views/layout/app.jsp">
  <c:param name="content">
    <c:if test="${flush != null}">
      <div id="flush_success">
        <c:out value="${flush}"></c:out>
      </div>
    </c:if>
    <h2>日報一覧</h2>
    <table id="report_list">
      <tr>
        <th class="report_name">氏名</th>
        <th class="report_department">所属</th>
        <th class="report_date">日付</th>
        <th class="report_title">タイトル</th>
        <th class="report_action">操作</th>
      </tr>
      <c:forEach var="report" items="${reports}" varStatus="status">
        <tr class="row${status.count % 2}">
          <td class="report_name"><c:out value="${report.employee.name}" />
          </td>
          <td class="department">
          <c:choose>
              <c:when test="${report.employee.department == 'general'}">
                <c:out value="総務部" />
              </c:when>
              <c:when test="${report.employee.department == 'legal'}">
                <c:out value="法務部" />
              </c:when>
              <c:when test="${report.employee.department == 'hr'}">
                <c:out value="人事部" />
              </c:when>
              <c:when test="${report.employee.department == 'account'}">
                <c:out value="経理部" />
              </c:when>
              <c:when test="${report.employee.department == 'corpsales'}">
                <c:out value="営業部" />
              </c:when>
            </c:choose></td>
          <td class="report_date"><fmt:formatDate
              value='${report.report_date}' pattern='yyyy-MM-dd' /></td>
          <c:choose>
            <c:when test="${report.publish == 1}">
              <td class="report_title">${report.title}</td>
            </c:when>
            <c:when
              test="${report.publish == 2 && sessionScope.login_employee.id
            == report.employee.id || sessionScope.login_employee.admin_flag == 1}">
              <td class="report_title">${report.title}</td>
            </c:when>
            <c:when
              test="${report.publish == 3 && sessionScope.login_employee.id
            == report.employee.id || sessionScope.login_employee.department == report.employee.department
            || sessionScope.login_employee.admin_flag == 1}">
              <td class="report_title">${report.title}</td>
            </c:when>
            <c:otherwise>
              <td class="report_title">※閲覧権限がありません</td>
            </c:otherwise>
          </c:choose>
          <td class="report_action"><a
            href="<c:url value='/reports/show?id=${report.id}' />">詳細を見る</a></td>

        </tr>

      </c:forEach>

    </table>

    <div id="pagination">
      (全 ${reports_count} 件)<br />
      <c:forEach var="i" begin="1" end="${((reports_count - 1) / 15) + 1}"
        step="1">
        <c:choose>
          <c:when test="${i == page}">
            <c:out value="${i}" />&nbsp;
          </c:when>
          <c:otherwise>
            <a href="<c:url value='/reports/index?page=${i}' />"><c:out
                value="${i}" /></a>&nbsp;
          </c:otherwise>
        </c:choose>
      </c:forEach>
    </div>
    <p>
      <a href="<c:url value='/reports/new' />">新規日報の登録</a>
  </c:param>
</c:import>