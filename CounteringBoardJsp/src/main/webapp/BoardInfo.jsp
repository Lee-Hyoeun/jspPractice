<%@page import="model.BoardBean"%>
<%@page import="model.BoardDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<body>

<%
	//String num = request.getParameter("num");
	int num = Integer.parseInt(request.getParameter("num").trim()); //trim으로 공백제거후 정수형으로 바뀜

	//데이터베이스 접근 (넘버만 넘겨서 데이터베이스에서 받기)
	BoardDAO bdao = new BoardDAO();
	//boardbean타입으로 하나의 게시글을 리턴 (데이터를 가방에 넣고 박스에 넣는다. 가방은 bean클래스.박스는 벡터(벡터는 가변(얼마나 많은 사용자가 이용할지 모르니깐)))
	BoardBean bean = bdao.getOneBoard(num);
	
%>

<center>
<h2> 게시글 보기 </h2>
<table width="600" border="1" bgcolor="skyblue">
	<tr height="40">
		<td align="center" width="100"> 글번호 </td>
		<td align="center" width="180"> <%=bean.getNum() %> </td>
		<td align="center" width="100"> 조회수 </td>
		<td align="center" width="180"> <%=bean.getReadcount() %> </td>
	</tr>
	<tr height="40">
		<td align="center" width="100"> 작성자 </td>
		<td align="center" width="180"> <%=bean.getWriter() %> </td>
		<td align="center" width="100"> 작성일 </td>
		<td align="center" width="180"> <%=bean.getReg_date() %> </td>
	</tr>
	<tr height="40">
		<td align="center" width="120"> 이메일 </td>
		<td align="center" colspan="3"> <%=bean.getEmail() %></td>
	</tr>
	<tr height="40">
		<td align="center" width="120"> 제목 </td>
		<td align="center" colspan="3"> <%=bean.getSubject() %></td>
	</tr>
	<tr height="80">
		<td align="center" width="120"> 글내용 </td>
		<td align="center" colspan="3"> <%=bean.getContent() %></td>
	</tr>
	<tr height="40">
		<td align="center" colspan="4"> 
			<!-- 넘버만 넘겨서 하나의 게시글을 받아가져와서 저쪽에서 처리해도 되는데 그냥 리스트를 한꺼번에 넘겨줌 -->
			<input type="button" value="답글쓰기" onclick="location.href='BoardReWriteForm.jsp?num=<%=bean.getNum()%>&ref=<%=bean.getRef()%>&re_step=<%=bean.getRe_step() %>&re_level=<%=bean.getRe_level()%>'"> 
			<!-- 넘버만 넘겨서 하는 방법 -->
			<input type="button" value="수정하기" onclick="location.href='BoardUpdateForm.jsp?num=<%=bean.getNum()%>'">
			<input type="button" value="삭제하기" onclick="location.href='BoardDeleteForm.jsp?num=<%=bean.getNum()%>'">		
			<input type="button" value="목록보기" onclick="location.href='BoardList.jsp'">
		
		</td>
	</tr>
	
	
</table>
</center>
</body>
</html>