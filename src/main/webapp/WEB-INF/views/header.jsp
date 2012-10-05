<%-- 
    Document   : header
    Created on : Jan 21, 2011, 9:45:45 PM
    Author     : kzheng
--%>
<html class="no-js">
  <head>
    <title>${pagetitle}</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=8"/>
    <meta http-equiv="expires" content="0"/>
    <link href="<c:url value='/resources/css/jquery-ui/jquery-ui-1.8.10.custom.css'/>" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="<c:url value='/resources/css/main.css'/>" type="text/css" media="screen" charset="utf-8">
    <script type="text/javascript" src="${requestProtocol}ajax.googleapis.com/ajax/libs/jquery/1.4.4/jquery.min.js"></script>
    <script type="text/javascript" src="<c:url value='/resources/js/jquery-ui-1.8.10.custom.min.js'/>"></script>
  </head>
  <c:choose>
    <c:when test="${showErrorPage}">
      <body id="errorpage">
      <div id="all" class="landing">
      <img title="404" id="error404" src="<c:url value='/resources/images/error.jpg'/>">
    </c:when>
    <c:otherwise>
    <body>
    <div id="all">
    </c:otherwise>
  </c:choose>
      <div class="wrapper"><header><div class="content">
            <a href="<c:url value='/'/>" id="logo" title="GLMX"></a>
            <div id="signin">
              <a href="<c:url value='/'/>"><img class="sub" src="<c:url value='/resources/images/${logoName}'/>"></a>
              <ul class="nav" id="si-nav"><li>
                </li></ul></div></div></header></div>