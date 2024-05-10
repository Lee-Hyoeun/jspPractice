<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<body>
<center>
<%
	String center = request.getParameter("center");
	//처음 실행시에는 center값이 넘어오지 않아 아무것도 안들어오니깐 null처리
	if(center == null){
			center = "Center.jsp"; //디폴트 center값 부여
	}
%>
<table width="1000" border="1">
	<!-- Top부분 -->
	<tr height="120" align="center">
		<td align="center" width="1000"> <jsp:include page="Top.jsp"/>  </td>
	</tr>
	
	<!-- Center부분 -->
	<tr height="470" align="center">
		<td align="center" width="1000"> <jsp:include page="<%=center %>"/>  </td>
	</tr>	

	<!-- Bottom부분 -->
	<tr height="100" align="center">
		<td align="center" width="1000"> <jsp:include page="Bottom.jsp"/>  </td>
	</tr>

</table>

</center>
</body>
</html>