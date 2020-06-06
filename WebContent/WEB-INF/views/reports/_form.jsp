<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:if test="${errors != null }">
  <div id="flush_error">
    入力内容にエラーがあります。<br />
    <c:forEach var="error" items="${errors}">
      ・<c:out value="${error}" />
      <br />
    </c:forEach>
  </div>
</c:if>
<!-- ここまで入力内容チェック -->

<label for="report_date">日付</label>
<br />
<input type="date" name="report_date" value="<fmt:formatDate
value='${report.report_date}' pattern= 'yyyy-MM-dd' />" />
<br />
<br />

<label for="name">氏名</label>
<br />
<c:out value="${sessionScope.login_employee.name}" />
<!-- これの"Remove"はLogoutServletなのでこの段階では保持しているから使える -->
<br />
<br />

<label for="title">タイトル</label>
<br />
<input type="text" name="title" value="${report.title}" />
<br />
<br />

<label for="content">内容</label>
<br />
<textarea name="content" rows="10" cols="50">${report.content}</textarea>
<br />
<br />

<p>公開範囲</p>
<select name="publish">
  <c:choose>
    <c:when test='${report.publish == "1"}'>
      <option selected value="1">全員に公開</option> />
    </c:when>
    <c:when test='${report.publish == "2"}'>
      <option selected value="2">自分と管理者にのみ公開</option> />
    </c:when>
    <c:when test='${report.publish == "3"}'>
      <option selected value="3">部署内にのみ公開</option> />
    </c:when>
    <c:otherwise>
      <option value="0">公開範囲を選択してください</option>
      <option value="1">全員に公開</option>
      <option value="2">自分と管理者にのみ公開</option>
      <option value="3">部署内にのみ公開</option>
    </c:otherwise>
  </c:choose>
</select>
<br/><br/>

<!-- "_token"の中身はgetId()したId番号 -->
<input type="hidden" name="_token" value="${_token}" />
<button type="submit">投稿</button>