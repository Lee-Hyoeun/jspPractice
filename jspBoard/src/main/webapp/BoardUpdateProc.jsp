<%@page import="model.BoardDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<body>
	
<!-- 수정은 글제목과 글내용만. 실제로 알아야 될것은 제목, 패스워드, 글내용, 넘버.
4개만 알면 되기 때문에 리퀘스트를 쓰는것보다 유즈빈을 쓰는게 편함.
우선. 데이터베이스에 접근해 해당 게시글의 비밀번호를 얻어와서 비교. -->

<!-- 한글처리 -->
<%
request.setCharacterEncoding("utf-8"); //한글처리

%>

<!-- 사용자데이터를 읽어들이는 빈클래스 설정 -->
<jsp:useBean id="boardbean" class="model.BoardBean">
	<jsp:setProperty name="boardbean" property="*" />
</jsp:useBean>
<%
	//1.데이터 베이스에 연결
	BoardDAO bdao = new BoardDAO();
	//2.해당 게시글의 패스워드 값을 얻어옴
	// (BoardUpdateForm에서 썼던 패스워드password 값을 읽어들임. db의 내용과 다름!)
	String pass = bdao.getPass(boardbean.getNum()); // getNumber를 기준으로 password값을 불러오겠다는 의미

	//기존 패스워드값과 update시 작성했던 password값이 같은지 비교
	if(pass.equals(boardbean.getPassword())){
		
		//데이터 수정하는 메소드 호출
		bdao.updateBoard(boardbean);
		//수정이 완료되면 전체 게시글 보기
		response.sendRedirect("BoardList.jsp");
	
	} else{//패스워드가 틀리다면
%>
	<script type="text/javascript">
		alert("패스워드가 일치하지 않습니다. 다시 확인 후 수정해주세요");
		history.go(-1);
	</script>
<%
	}
%>

</body>
</html>