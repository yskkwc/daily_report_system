<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<tbody>
  <tr>
    <th>氏名</th>
    <td><c:out value="${report.employee.name}" /></td>
  </tr>
  <tr>
    <th>日付</th>
    <td><fmt:formatDate value="${report.report_date}"
        pattern="yyyy-MM-dd" /></td>
  </tr>
  <tr>
    <th>タイトル</th>
    <td><c:out value="${report.title}" /></td>
  </tr>
  <tr>
    <th>内容</th>
    <td>
    <pre>
        <c:out value="${report.content}" />
    </pre>
    </td>
  </tr>
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
    <th>登録日時</th>
    <td><fmt:formatDate value="${report.created_at}"
        pattern="yyyy-MM-dd HH:mm:ss" /></td>
  </tr>
  <tr>
    <th>更新日時</th>
    <td><fmt:formatDate value="${report.updated_at}"
        pattern="yyyy-MM-dd HH:mm:ss" /></td>
  </tr>
</tbody>