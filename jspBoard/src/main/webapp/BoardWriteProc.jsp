<%@page import="model.BoardDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<body>
<%

	request.setCharacterEncoding("utf-8"); //한글처리
%>

<!-- 게시글 작성한 데이터를 한번에 읽어들임 -->
<jsp:useBean id="boardbean" class="model.BoardBean">
	<jsp:setProperty name="boardbean" property="*" />
</jsp:useBean>
<!--참고) id는 UseBean빈클래스를 지칭하는 레퍼런스 변수명
name은 내가 어느 bin클래스를 현재 쓰고있느냐 그 사용하고 있는 bean클래스이름
property 일대일 맵핑 (bean에 없는 reg등은 *로인해 널로 들어오게 됨(레벨등 셋팅값은 미리 데이터베이스에서 세트함)
 -->
 
<%
	//데이터 베이스 쪽으로 빈클래스를 넘겨줌
	BoardDAO bdao = new BoardDAO();

	//데이터 저장 메소드를 호출
	bdao.insertBoard(boardbean);
	
	//게시글 저장후 전체게시글 보기
	response.sendRedirect("BoardList.jsp");
	
	
%>
 

</body>
</html>