<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:if test="${errors != null}">
  <div id="flush_error">
    入力内容にエラーがあります。<br />
    <c:forEach var="error" items="${errors}">
      ・<c:out value="${error}" />
      <br />
    </c:forEach>
  </div>
</c:if>
<label for="code">社員番号</label>
<br />
<input type="text" name="code" value="${employee.code}" />
<br />
<br />

<label for="name">氏名</label>
<br />
<input type="text" name="name" value="${employee.name}" />
<br />
<br />

<p>所属</p>
<select name="department">
<c:choose>
    <c:when test='${employee.department == "general"}'>
        <option selected value="general">総務部</option> />
    </c:when>
    <c:when test='${emplopyee.department == "legal"}'>
        <option selected value="legal">法務部</option>
    </c:when>
    <c:when test='${emplopyee.department == "hr"}'>
        <option selected value="hr">人事部</option>
    </c:when>
    <c:when test='${emplopyee.department == "account"}'>
        <option selected value="account">経理部</option>
    </c:when>
    <c:when test='${emplopyee.department == "corpsales"}'>
        <option selected value="corpsales">法人営業部</option>
    </c:when>
    <c:when test='${emplopyee.department == "intsales"}'>
        <option selected value="intsales">国際営業部</option>
    </c:when>
    <c:when test='${emplopyee.department == "qm"}'>
        <option selected value="qm">品質管理部</option>
    </c:when>
    <c:otherwise>
      <option value="">所属を選択してください</option>
      <option value="general">総務部</option>
      <option value="legal">法務部</option>
      <option value="hr">人事部</option>
      <option value="account">経理部</option>
      <option value="corpsales">営業部</option>
    </c:otherwise>
</c:choose>
</select>

<br/>
<br/>

<label for="password">パスワード</label>
<br />
<input type="password" name="password" />
<br />
<br />

<label for="admin_flag">権限</label>
<br />
<select name="admin_flag">
  <option value="0"
    <c:if test="${employee.admin_flag == 0}"> selected</c:if>>一般</option>
  <option value="1"
    <c:if test="${employee.admin_flag == 1}"> selected</c:if>>管理者</option>
</select>
<br />
<br />

<input type="hidden" name="_token" value="${_token}" />
<button type="submit">投稿</button>