<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="ja">
<c:choose>
                    <c:when test="${employee.department == 'general'}">
                        <c:out value= "総務部"/>
                    </c:when>
                    <c:when test="${employee.department == 'legal'}">
                        <c:out value= "法務部"/>
                    </c:when>
                    <c:when test="${employee.department == 'hr'}">
                        <c:out value= "人事部"/>
                    </c:when>
                    <c:when test="${employee.department == 'account'}">
                        <c:out value= "経理部"/>
                    </c:when>
                    <c:when test="${employee.department == 'corpsales'}">
                        <c:out value= "法人営業部"/>
                    </c:when>
                    <c:when test="${employee.department == 'intsales'}">
                        <c:out value= "国際営業部"/>
                    </c:when>
                    <c:when test="${employee.department == 'qm'}">
                        <c:out value= "品質管理部"/>
                    </c:when>
</c:choose>