<%@ include file="header.jsp" %>
<h1>
    <c:choose>
        <c:when test="${region == null}">
            <fmt:message key="msg.region-form.add" />
        </c:when>
        <c:otherwise>
            <fmt:message key="msg.region-form.edit" />
        </c:otherwise>
    </c:choose>
</h1>

<c:if test="${not empty errorMessage}">
    <div class="error-message">${errorMessage}</div>
</c:if>

<form action="regions" method="post">
    <input type="hidden" name="id" value="${region != null ? region.id : ''}" />
    <input type="hidden" name="action" value="${region == null ? 'insert' : 'update'}" />

    <label for="code"><fmt:message key='msg.region-form.code'/>:</label>
    <input type="text" name="code" id="code" value="${region != null ? region.code : ''}" required />

    <label for="name"><fmt:message key='msg.region-form.name'/>:</label>
    <input type="text" name="name" id="name" value="${region != null ? region.name : ''}" required />

    <c:choose>
        <c:when test="${region == null}">
            <input type="submit" value="<fmt:message key='msg.region-form.create'/>" />
        </c:when>
        <c:otherwise>
            <input type="submit" value="<fmt:message key='msg.region-form.update'/>" />
        </c:otherwise>
    </c:choose>
</form>
<a href="regions"><fmt:message key="msg.region-form.returnback" /></a>
<%@ include file="footer.jsp" %>