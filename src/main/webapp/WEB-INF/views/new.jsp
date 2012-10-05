<%-- 
    Document   : test
    Created on : Aug 27, 2012, 5:32:54 PM
    Author     : Kelvin Zheng <kelvin@glmx.com>
--%>
<%@include file="global.jsp" %>
<c:set var="pagetitle">new pass</c:set>
<%@include file="header.jsp" %>
<link rel="stylesheet" href="<c:url value='/resources/css/colorpicker/jquery.colorpicker.css'/>" type="text/css" media="screen" charset="utf-8">
<script type="text/javascript" src="<c:url value='/resources/js/jquery.colorpicker.js'/>"></script>
<script src="<c:url value='/resources/js/i18n/jquery.ui.colorpicker-nl.js'/>"></script>
<div id="main">
  <div class="content">
    <c:choose>
      <c:when test="${edit}"><h1>Edit pass</h1></c:when>
      <c:otherwise><h1>New pass</h1></c:otherwise>
    </c:choose>
    
    <form:form method="post" modelAttribute="pass">
      <fieldset>
        <legend><span>Appearance</span></legend>
        <p><label>Type:</label><form:select id="passType" path="typeEnum">
            <form:option value="coupon"/><form:option value="storeCard"/><form:option value="eventTicket"/>
            <form:option value="boardingPass"/><form:option value="generic">support card</form:option></form:select></p>
        <p><label>Business Name:</label><form:input path="organizationName"/></p>
        <p><label>Description:</label><form:input path="description"/></p>
        <p><label>Title:</label><form:input path="logoText"/></p>
        <p><label>Foreground Color:</label><form:input id="foregroundColor" path="foregroundColor"/></p>
        <p><label>Background Color:</label><form:input id="backgroundColor" path="backgroundColor"/></p>
        <span class="coupon">
          <p><label>Deal Name:</label><form:input path="fields.primaryFields[0].label" /></p> 
          <p><label>Deal:</label><form:input path="fields.primaryFields[0].value" />
            <span style="display:none"><form:input path="fields.primaryFields[0].key" value="offer"/>
              <form:input path="fields.primaryFields[0].changeMessage" value="%@"/>
            </span></p>
          <span style="display:none"><form:input path="fields.auxiliaryFields[0].label" value="EXPIRES"/>
            <form:input path="fields.auxiliaryFields[0].key" value="expires"/></span>
          <p><label>Expires:</label><form:input path="fields.auxiliaryFields[0].value"/></p>
        </span>
        <span class="storeCard">
          <p><label>Balance:</label><form:input path="fields.primaryFields[0].value" />
            <span style="display:none"><form:input path="fields.primaryFields[0].label" value="remaining balance" />
              <form:input path="fields.primaryFields[0].key" value="balance"/>
            </span></p>
          <span style="display:none"><form:input path="fields.auxiliaryFields[0].label" value="Level"/>
            <form:input path="fields.auxiliaryFields[0].key" value="level"/></span>
          <p><label>Level:</label><form:input path="fields.auxiliaryFields[0].value"/></p>
          <span style="display:none"><form:input path="fields.auxiliaryFields[1].label" value="THE USUAL"/>
            <form:input path="fields.auxiliaryFields[1].key" value="usual"/></span>
          <p><label>The Usual:</label><form:input path="fields.auxiliaryFields[1].value"/></p>
        </span>
        <span class="eventTicket">
          <p><label>Event Name:</label><form:input path="fields.primaryFields[0].value" /> 
            <span style="display:none"><form:input path="fields.primaryFields[0].label" value="EVENT" />
              <form:input path="fields.primaryFields[0].key" value="event"/>
              <form:input path="fields.primaryFields[0].changeMessage" value="%@"/>
            </span></p>
          <span style="display:none"><form:input path="fields.secondaryFields[0].label" value="LOCATION"/>
            <form:input path="fields.secondaryFields[0].key" value="loc"/></span>
          <p><label>Location:</label><form:input path="fields.secondaryFields[0].value"/></p>
        </span>
        <span class="boardingPass">
          <p><span style="display:none"><form:input path="fields.headerFields[0].label" value="GATE"/>
              <form:input path="fields.headerFields[0].key" value="gate"/>
              <form:input path="fields.headerFields[0].changeMessage" value="Gate has changed to %@"/></span>
          <p><label>Gate:</label><form:input path="fields.headerFields[0].value"/></p>
          <p><span style="display:none"><form:input path="fields.primaryFields[0].key" value="depart"/></span>
              <label>Depart City:</label><form:input path="fields.primaryFields[0].label"/></p>
          <p><label>Depart Airport:</label><form:input path="fields.primaryFields[0].value"/></p>
          <p><span style="display:none"><form:input path="fields.primaryFields[1].key" value="arrive"/></span>
              <label>Arrive City:</label><form:input path="fields.primaryFields[1].label"/></p>
          <p><label>Arrive Airport:</label><form:input path="fields.primaryFields[1].value"/></p>

          <p><span style="display:none"><form:input path="fields.secondaryFields[0].label" value="PASSENGER"/>
              <form:input path="fields.secondaryFields[0].key" value="passenger" /></span>
          <p><label>Passenger:</label><form:input path="fields.secondaryFields[0].value"/></p>
          <p><span style="display:none"><form:input path="fields.auxiliaryFields[0].label" value="DEPART"/>
              <form:input path="fields.auxiliaryFields[0].key" value="boardingTime"/>
              <form:input path="fields.auxiliaryFields[0].changeMessage" value="Boarding time changed %@"/></span>
            <label>Boarding Time:</label><form:input path="fields.auxiliaryFields[0].value"/></p>
          <p><span style="display:none"><form:input path="fields.auxiliaryFields[1].label" value="FLIGHT"/>
              <form:input path="fields.auxiliaryFields[1].key" value="flightNumber"/>
              <form:input path="fields.auxiliaryFields[1].changeMessage" value="Flight number changed to %@"/></span>
            <label>Flight Number:</label><form:input path="fields.auxiliaryFields[1].value"/></p>
          <p><span style="display:none"><form:input path="fields.auxiliaryFields[2].label" value="DESIG."/>
              <form:input path="fields.auxiliaryFields[2].key" value="class"/>
            </span>
            <label>Class:</label><form:input path="fields.auxiliaryFields[2].value"/></p>
          <p><span style="display:none"><form:input path="fields.auxiliaryFields[3].label" value="DATE"/>
              <form:input path="fields.auxiliaryFields[3].key" value="date"/>
            </span>
            <label>Date:</label><form:input path="fields.auxiliaryFields[3].value"/></p>
        </span>
        <span class="generic">
          <p><label>Problem:</label><form:input path="fields.primaryFields[0].value" />
            <span style="display:none"><form:input path="fields.primaryFields[0].label" value="Problem" />
              <form:input path="fields.primaryFields[0].key" value="problem"/>
            </span></p>
          <span style="display:none"><form:input path="fields.secondaryFields[0].label" value="Status"/>
            <form:input path="fields.secondaryFields[0].key" value="status"/></span>
          <p><label>Status:</label><form:input path="fields.secondaryFields[0].value"/></p>
          <span style="display:none"><form:input path="fields.secondaryFields[1].label" value="Offer"/>
            <form:input path="fields.secondaryFields[1].key" value="offer"/></span>
          <p><label>Offer:</label><form:input path="fields.secondaryFields[1].value"/></p>
        </span>
      </fieldset>
      <fieldset>
        <legend><span>Bar code</span></legend>
        <p><label>Format:</label><form:select path="barcode.format">
            <form:option value="PKBarcodeFormatQR"/><form:option value="PKBarcodeFormatPDF417"/>
            <form:option value="PKBarcodeFormatAzte"/>
          </form:select></p>
        <p><label>Message:</label><form:input path="barcode.message"/></p>
      </fieldset>
      <fieldset>
        <legend><span>Relevance</span></legend>
        <p><label>Location:</label>Longitude: <form:input path="locations[0].longitude" class="short" value="-122.163187"/>
          &nbsp;Latitude: <form:input path="locations[0].latitude" class="short" value="37.446755"/>
        </p>
        <p><label>Relevant Date:</label><form:input id="relevantDate" path="relevantDate"/></p>
        <span id="transitTypeSpan"<p><label>Transit Type:</label><form:select id="transitType" path="fields.transitType">
            <form:option value=""/>
            <form:option value="PKTransitTypeAir"/>
            <form:option value="PKTransitTypeTrain"/>
            <form:option value="PKTransitTypeBus"/>
            <form:option value="PKTransitTypeBoat"/>
            <form:option value="PKTransitTypeGeneric"/>
          </form:select></p></span>
      </fieldset>
      <fieldset>
        <legend><span>Back Page</span></legend>
        <span class="coupon_back">
          <span style="display:none"><form:input path="fields.backFields[0].label" value="TERMS AND CONDITIONS"/>
            <form:input path="fields.backFields[0].key" value="terms"/></span>
          <p><label>Terms and Conditions:</label><form:textarea path="fields.backFields[0].value" value="Generico offers this pass, including all information, software, products and services available from this pass or offered as part of or in conjunction with this pass (the \"pass\"), to you, the user, conditioned upon your acceptance of all of the terms, conditions, policies and notices stated here. Generico reserves the right to make changes to these Terms and Conditions immediately by posting the changed Terms and Conditions in this location.\n\nUse the pass at your own risk. This pass is provided to you \"as is,\" without warranty of any kind either express or implied. Neither Generico nor its employees, agents, third-party information providers, merchants, licensors or the like warrant that the pass or its operation will be accurate, reliable, uninterrupted or error-free. No agent or representative has the authority to create any warranty regarding the pass on behalf of Generico. 
                         Generico reserves the right to change or discontinue at any time any aspect or feature of the pass."/></p>
        </span>  
        <span class="eventTicket_back">
          <span style="display:none"><form:input path="fields.backFields[0].label" value="TERMS AND CONDITIONS"/>
            <form:input path="fields.backFields[0].key" value="terms"/></span>
          <p><label>Terms and Conditions:</label><form:textarea path="fields.backFields[0].value" value="Generico offers this pass, including all information, software, products and services available from this pass or offered as part of or in conjunction with this pass (the \"pass\"), to you, the user, conditioned upon your acceptance of all of the terms, conditions, policies and notices stated here. Generico reserves the right to make changes to these Terms and Conditions immediately by posting the changed Terms and Conditions in this location.\n\nUse the pass at your own risk. This pass is provided to you \"as is,\" without warranty of any kind either express or implied. Neither Generico nor its employees, agents, third-party information providers, merchants, licensors or the like warrant that the pass or its operation will be accurate, reliable, uninterrupted or error-free. No agent or representative has the authority to create any warranty regarding the pass on behalf of Generico. 
                         Generico reserves the right to change or discontinue at any time any aspect or feature of the pass."/></p>
        </span>  
        <span class="storeCard_back">
          <span style="display:none"><form:input path="fields.backFields[0].label" value="TERMS AND CONDITIONS"/>
            <form:input path="fields.backFields[0].key" value="terms"/></span>
          <p><label>Terms and Conditions:</label><form:textarea path="fields.backFields[0].value" value="Generico offers this pass, including all information, software, products and services available from this pass or offered as part of or in conjunction with this pass (the \"pass\"), to you, the user, conditioned upon your acceptance of all of the terms, conditions, policies and notices stated here. Generico reserves the right to make changes to these Terms and Conditions immediately by posting the changed Terms and Conditions in this location.\n\nUse the pass at your own risk. This pass is provided to you \"as is,\" without warranty of any kind either express or implied. Neither Generico nor its employees, agents, third-party information providers, merchants, licensors or the like warrant that the pass or its operation will be accurate, reliable, uninterrupted or error-free. No agent or representative has the authority to create any warranty regarding the pass on behalf of Generico. 
                         Generico reserves the right to change or discontinue at any time any aspect or feature of the pass."/></p>
        </span>  
        <span class="boardingPass_back">
          <span style="display:none"><form:input path="fields.backFields[0].label" value="PASSPORT"/>
            <form:input path="fields.backFields[0].key" value="passport"/></span>
          <p><label>Passport:</label><form:input path="fields.backFields[0].value" /></p>
          <span style="display:none"><form:input path="fields.backFields[1].label" value="RESIDENCE"/>
            <form:input path="fields.backFields[1].key" value="residence"/></span>
          <p><label>Residence:</label><form:input path="fields.backFields[1].value" /></p>
          <span style="display:none"><form:input path="fields.backFields[2].label" value="TERMS AND CONDITIONS"/>
            <form:input path="fields.backFields[2].key" value="terms"/></span>
          <p><label>Terms and Conditions:</label><form:textarea path="fields.backFields[2].value" value="Generico offers this pass, including all information, software, products and services available from this pass or offered as part of or in conjunction with this pass (the \"pass\"), to you, the user, conditioned upon your acceptance of all of the terms, conditions, policies and notices stated here. Generico reserves the right to make changes to these Terms and Conditions immediately by posting the changed Terms and Conditions in this location.\n\nUse the pass at your own risk. This pass is provided to you \"as is,\" without warranty of any kind either express or implied. Neither Generico nor its employees, agents, third-party information providers, merchants, licensors or the like warrant that the pass or its operation will be accurate, reliable, uninterrupted or error-free. No agent or representative has the authority to create any warranty regarding the pass on behalf of Generico. 
                         Generico reserves the right to change or discontinue at any time any aspect or feature of the pass."/></p>
        </span>
        <span class="generic_back">
          <span style="display:none"><form:input path="fields.backFields[0].label" value="Support Site"/>
            <form:input path="fields.backFields[0].key" value="website"/></span>
          <p><label>Support Site:</label><form:input path="fields.backFields[0].value" /></p>
          <span style="display:none"><form:input path="fields.backFields[1].label" value="Customer Phone"/>
            <form:input path="fields.backFields[1].key" value="customer-service"/></span>
          <p><label>Customer Phone:</label><form:input path="fields.backFields[1].value" /></p>
          <span style="display:none"><form:input path="fields.backFields[2].label" value="TERMS AND CONDITIONS"/>
            <form:input path="fields.backFields[2].key" value="terms"/></span>
          <p><label>Terms and Conditions:</label><form:textarea path="fields.backFields[2].value" value="Generico offers this pass, including all information, software, products and services available from this pass or offered as part of or in conjunction with this pass (the \"pass\"), to you, the user, conditioned upon your acceptance of all of the terms, conditions, policies and notices stated here. Generico reserves the right to make changes to these Terms and Conditions immediately by posting the changed Terms and Conditions in this location.\n\nUse the pass at your own risk. This pass is provided to you \"as is,\" without warranty of any kind either express or implied. Neither Generico nor its employees, agents, third-party information providers, merchants, licensors or the like warrant that the pass or its operation will be accurate, reliable, uninterrupted or error-free. No agent or representative has the authority to create any warranty regarding the pass on behalf of Generico. 
                         Generico reserves the right to change or discontinue at any time any aspect or feature of the pass."/></p>
        </span>         
      </fieldset>
      <p><label></label><input id="submitBtn" type="submit" value="Save"/></p>
      </form:form>
  </div>
  <div class="push"></div>
</div>	
<script>
  $(document).ready(
  function()
  {
    $( "#relevantDate" ).datepicker({ dateFormat: "yy-mm-dd" });
    $('#foregroundColor').colorpicker({
      colorFormat: ['RGB']
    });    
    $('#backgroundColor').colorpicker({
      colorFormat: ['RGB']
    });
    
    $("#passType").change(function () {
      showFields();
    });
    showFields();
  });
  

  function showFields() {
    $("#passType option").each(function () {
      var className = "."+$(this).val();
      if ($(this).attr("selected")) {
        if ("boardingPass" == $(this).val()) {
          $("#transitTypeSpan").show();
        } else {
          $("#transitTypeSpan").hide();
        }
        $(className).show();
        $("span"+className+" input").removeAttr("disabled");
        $(className+"_back").show();
        $("span"+className+"_back input").removeAttr("disabled");
        $("span"+className+"_back textarea").removeAttr("disabled");
      } else {
        $(className).hide();
        $("span"+className+" input").attr("disabled", true);
        $(className+"_back").hide();
        $("span"+className+"_back input").attr("disabled", true);
        $("span"+className+"_back textarea").attr("disabled", true);
      }
    });
  }
</script>
<%@include file="footer.jsp" %>
