<%-- 
    Document   : globals.jsp
    Created on : Jan 24, 2011, 1:09:16 AM
    Author     : kzheng
--%>
<!DOCTYPE html>
<%@ page contentType="text/html" %>
<%@ page pageEncoding="UTF-8" %>
<%@ page session="false" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<c:choose>
  <c:when test='${fn:startsWith(applicationScope.appHost, "https")}'>
    <c:set var="requestProtocol" value="https://" />
  </c:when>
  <c:otherwise>
    <c:set var="requestProtocol" value="http://" />
  </c:otherwise>
</c:choose>

