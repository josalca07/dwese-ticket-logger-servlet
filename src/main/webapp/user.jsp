<%@ include file="header.jsp" %>
<%
String userName = (String) request.getAttribute("userName");
if (userName != null) {
%>
    <h2>Hello, <%= userName %>!</h2>
<%
} else {
%>
    <h2>Hello, World!</h2>
<%
}
%>
<%@ include file="footer.jsp" %>