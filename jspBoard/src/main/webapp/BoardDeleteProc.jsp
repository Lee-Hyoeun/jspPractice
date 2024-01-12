<%@page import="model.BoardDAO"%>
<%@page import="model.BoardBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<body>
<!-- BoardDeleteForm에서 password와 bean값만 넘어옴.
 usebean써도 되긴하는데 간단하게 string타입에 pass로 받아버림 -->
<%
request.setCharacterEncoding("utf-8"); //한글처리
%>

<%
	String pass = request.getParameter("password");
	int num = Integer.parseInt(request.getParameter("num"));
	
	//데이터베이스 연결
	BoardDAO bdao = new BoardDAO();
	String password = bdao.getPass(num);
	
	//기존 패스워드값과 delete Form에서 작성한 패스워드를 비교
	if(pass.equals(password)){
		
		//패스워드가 둘다 값다면
		bdao.deleteBoard(num); 
		
		response.sendRedirect("BoardList.jsp");
	}else {//다르다면
%>
	<script>
		alert("패스워드가 틀려서 삭제 할 수 없습니다. 패스워드를 확인해주세요");
		history.go(-1);
	</script>
<%
	}
	
%>

</body>
</html>