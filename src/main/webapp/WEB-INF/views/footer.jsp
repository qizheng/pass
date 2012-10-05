<footer></footer>
<script type="text/javascript">
  function initialize() {
      <c:if test="${not empty mapInitialize}">${mapInitialize}</c:if>
    }

<c:if test="${not empty googleMap}">
    function loadScript() {
    var script = document.createElement("script");
    script.type = "text/javascript";
    script.src = "https://maps-api-ssl.google.com/maps/api/js?sensor=false&callback=initialize";
    document.body.appendChild(script);
  }

  $(document).ready(function(){
    loadScript();
  });
</c:if>

</script>
</body>
</html>
