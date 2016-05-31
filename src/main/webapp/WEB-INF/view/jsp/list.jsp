<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%-- <%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%> --%>
<!DOCTYPE html>
<head>
<!-- <link rel="stylesheet" type="text/css" href="https://js.ui-portal.de/energie/cs/1.0/strom.css" /> -->
<script type="text/javascript" src="<c:url value="/js/jquery-1.7.2.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/main.js" />"></script>
</head>
<body>
	<h1>SmartHome Index Page</h1>
	
	<h3>Room Temperatures: ${room}</h3>
	<h3>All Rooms: ${allRooms}</h3>	
</body>
</html>