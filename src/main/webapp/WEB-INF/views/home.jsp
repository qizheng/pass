<%@include file="global.jsp" %>
<c:set var="pagetitle">Welcome to Coupon Center</c:set>
<%@include file="header.jsp" %>
<div id="main">
  <div class="content">
    <legend><span>Passes</span></legend>
    <table>
      <c:forEach items="${passes}" var="row">
        <c:if test="${empty row.serialNumber}">
        <tr>
          <td>
              <img alt="QA code" 
                   src="https://chart.googleapis.com/chart?cht=qr&chs=200x200&chl=${appHost}/issue/${row.id}"
                   width="100" height="100" title="QR code"/>
          </td>
          <td>${row.type}</td>
          <td>${row.description}</td>
          <td>${row.logoText}</td>
          <td>
            <c:forEach items="${row.fields.headerFields}" var="f">
              <div>${f.label}: ${f.value}</div>
            </c:forEach>
            <c:forEach items="${row.fields.primaryFields}" var="f">
              <div>${f.label}: ${f.value}</div>
            </c:forEach>
            <c:forEach items="${row.fields.secondaryFields}" var="f">
              <div>${f.label}: ${f.value}</div>
            </c:forEach>
            <c:forEach items="${row.fields.auxiliaryFields}" var="f">
              <div>${f.label}: ${f.value}</div>
            </c:forEach>
          </td>
          <td>${row.modifiedDate}</td>
          <td><span class="actions"><div><a href="<c:url value="/edit/${row.id}"/>">Edit</a></div>
              <div class="ptop10"><a href="<c:url value="/delete/${row.id}"/>">Delete</a></div></span></td>
        </tr>
        </c:if>
      </c:forEach>
    </table>  
    <p class="ptop10"><input type="button" value="Add New Pass" onclick="add();"/></p>
    <legend><span>Issued Pass</span></legend>
    <table>
      <c:forEach items="${passes}" var="row">
        <c:if test="${not empty row.serialNumber}">
        <tr>
          <td>
              <a href="<c:url value='/get/${row.id}'/>">${row.serialNumber}</a>
          </td>
          <td>${row.type}</td>
          <td>${row.logoText}</td>
          <td>
            <c:forEach items="${row.registrations}" var="reg" varStatus="varStatus">
              <c:if test="${varStatus.count > 1}"> | </c:if>
              ${reg.status}
            </c:forEach>
          </td>
          <td>${row.modifiedDate}</td>
          <td><span class="actions"><div><a href="<c:url value="/edit/${row.id}"/>">Edit</a></div>
              <div class="ptop10"><a href="<c:url value="/delete/${row.id}"/>">Delete</a></div></span></td>
        </tr>
        </c:if>
      </c:forEach>
    </table>  
  </div>
  <div class="push"></div>
</div>	
<script type="text/javascript">
  function add() {
    window.location.href='<c:url value="/add"/>';
  }
</script>
<%@include file="footer.jsp" %>