<%@ include file="header.jsp" %>
<h1>Jorge Saldaña -
    <c:choose>
        <c:when test="${province == null}"><fmt:message key="msg.province-form.add" /></c:when>
        <c:otherwise><fmt:message key="msg.province-form.edit" /></c:otherwise>
    </c:choose>
</h1>

<c:if test="${not empty errorMessage}">
    <div class="error-message" style="color:red; font-weight:bold;">${errorMessage}</div>
</c:if>

<form action="provinces" method="post">
    <input type="hidden" name="id" value="${province != null ? province.id : ''}" />
    <input type="hidden" name="action" value="${province == null ? 'insert' : 'update'}" />

    <label for="code"><fmt:message key='msg.region-form.code'/>:</label>
    <input type="text" name="code" id="code" value="${province != null ? province.code : ''}" required />
    <br><br>
    <label for="name"><fmt:message key='msg.region-form.name'/>:</label>
    <input type="text" name="name" id="name" value="${province != null ? province.name : ''}" required />
    <br><br>

    <label for="id_region"><fmt:message key='msg.province.region'/>:</label>
    <select name="id_region" id="id_region" required>
        <option value=""><fmt:message key="msg.province-form.select-region"/></option>
        <c:forEach var="region" items="${listRegions}">
            <option value="${region.id}" ${province != null && province.region.id == region.id ? 'selected' : ''}>
                ${region.name}
            </option>
        </c:forEach>
    </select>
    <br><br>

    <input type="submit" value="${province == null ? 'Guardar' : 'Actualizar'}" />
</form>
<a href="provinces"><fmt:message key="msg.region-form.returnback" /></a>
<%@ include file="footer.jsp" %>