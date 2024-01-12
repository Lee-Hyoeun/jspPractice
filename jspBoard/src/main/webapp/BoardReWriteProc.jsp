<%@page import="model.BoardBean"%>
<%@page import="model.BoardDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<body>
<%
request.setCharacterEncoding("utf-8"); //한글처리
%>
<!-- 데이터를 한번에 받아오는 빈클래스를 사용하도록 (ref~까지 전부다 넘어옴!)-->
<jsp:useBean id="boardbean" class="model.BoardBean">
	<jsp:setProperty name="boardbean" property="*" /> <!-- boardbean이라는 이름의 클래스를 property속성을 모두 맞추시오 -->
</jsp:useBean>

<!-- 데이터를 호출해서 저장 -->
<%
	//데이터베이스 객체 생성
	BoardDAO bdao = new BoardDAO(); 
	bdao.reWriteBoard(boardbean); //boardbean타입을 넘겨줌
	
	//답변데이터를 모두 저장후 전체 게시글 보기를 설정
	response.sendRedirect("BoardList.jsp");
	
%>


</body>
</html>