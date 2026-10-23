<%@ include file="header.jsp" %>

<h2>Hello, World!</h2>
<%
int count = 10;
for (int i=0; i < count; i++) {
    out.println("Number: " + i + "<br>");
}
%>

<h3>Current Time: <%= new java.util.Date() %></h3>

<%!
private int counter = 0;
public int getCounter() {
    return ++counter;
}
%>
<p>Counter Value: <%= getCounter() %></p>

<%
String userName = "Jorge";
int age = 20;
%>
<p>User Name: <%= userName %></p>
<p>User Age: <%= age %></p>

<%
int num1 = 10;
int num2 = 20;
int sum = num1 + num2;
%>
<p>Sum of <%= num1 %> and <%= num2 %> is: <%= sum %></p>

<%
String role = "admin";
if ("admin".equals(role)) {
%>
<p>Welcome, Administrator!</p>
<%
} else {
%>
<p>Welcome, User!</p>
<%
}
%>

<ul>
<%
int i=1;
while ( i<=5 ) {
%>
<li>Item <%= i %></li>
<%
i++;
}
%>
</ul>

<h1>Number List:</h1>
<ul>
    <c:forEach var="i" begin="1" end="5">
        <li>Number: ${i}</li>
    </c:forEach>
</ul>

<%@ include file="footer.jsp" %>