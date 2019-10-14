<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<%--<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>--%>
<html>
<head>
    <title>Meal list</title>
    <style>
        .normal {
            color: green;
        }

        .excess {
            color: red;
        }
    </style>
</head>
<body>
<section>
    <h3><a href="index.html">Home</a></h3>
    <hr/>
    <h2>Meals</h2>
    <form action="meals" method="get">
        <label for="date-time-filter"><strong>Фильтр еды</strong></label>
        <input type="hidden" name="action" value="filter"/>
        <div id="date-time-filter" name="date-time-filter">
            <table>
                <thead>
                <tr>
                    <th>От даты</th>
                    <th>До даты</th>
                    <th>От времени</th>
                    <th>До времени</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>
                        <input type="date" id="date-from" name="date-from"
                               value="<%=request.getParameter("date-from")%>">
                    </td>
                    <td>
                        <input type="date" id="date-to" name="date-to"
                               value="<%=request.getParameter("date-to")%>">
                    </td>
                    <td align="center" width="40">
                        <input type="time" id="time-from" name="time-from"
                               value="<%=request.getParameter("time-from")%>">
                    </td>
                    <td align="center" width="40">
                        <input type="time" id="time-to" name="time-to"
                               value="<%=request.getParameter("time-to")%>">
                    </td>
                </tr>
                </tbody>
            </table>
            <br>
            <button type="submit">Отфильтровать</button>
            <button id="cancel-filter-button">
                <a href="meals?action=all">Сбросить</a></button>
        </div>
    </form>
    <a href="meals?action=create">Add Meal</a>
    <br><br><label>User id ${userId}</label><br><br>

    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <c:forEach items="${meals}" var="meal">
            <jsp:useBean id="meal" type="ru.javawebinar.topjava.to.MealTo"/>
            <tr class="${meal.excess ? 'excess' : 'normal'}">
                <td>
                        <%--${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}--%>
                        <%--<%=TimeUtil.toString(meal.getDateTime())%>--%>
                        <%--${fn:replace(meal.dateTime, 'T', ' ')}--%>
                        ${fn:formatDateTime(meal.dateTime)}
                </td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals?action=update&id=${meal.id}">Update</a></td>
                <td><a href="meals?action=delete&id=${meal.id}">Delete</a></td>
            </tr>
        </c:forEach>
    </table>
</section>
</body>
</html>