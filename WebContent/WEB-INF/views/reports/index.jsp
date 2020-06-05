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
    <c:choose>
        <c:when test="${report.publish == 0}">
          <c:import url="range0.jsp"></c:import>
        </c:when>
        <c:when test="${report.publish == 1}">
          <c:import url="range1.jsp"></c:import>
        </c:when>
        <c:when test="${report.publish == 2}">
          <c:import url="range2.jsp"></c:import>
        </c:when>
        <c:otherwise>
            <c:import url="range0.jsp"></c:import>
        </c:otherwise>
    </c:choose>
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