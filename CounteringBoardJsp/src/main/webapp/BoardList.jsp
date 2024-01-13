<%@page import="model.BoardBean"%>
<%@page import="java.util.Vector"%>
<%@page import="model.BoardDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<body>

<center>
<h2> 전체 게시글 보기 </h2>
<!-- 카운터링: 게시글 보기에 카운터링을 설정하기 위한 변수들을 선언 -->
<!-- 이제 전체게시글을 가져오지 않고 10개씩 끊어 가지고 옴 -->
<% 
	//화면에 보여질 게시글의 개수를 지정
	int pageSize = 10;
	
	//현재 카운터를 클릭한 번호값을 읽어롬
	String pageNum = request.getParameter("pageNum");
	//만약 처음 boardList.jsp를 클릭하거나 수정,삭제 등 다른 게시글에서 이페이지로 넘어오면 pageNum값이 없기에 null처리 해줌
	// pageNum가 없다는 뜻은 카운터링을 눌러서 넘어온게 아니라 다른쪽에서 넘어왔다는 뜻. 즉 최초의 처음 글을 보여줘야함
	if(pageNum == null){
		pageNum="1";
	}
	
	int count = 0; //전체 글의 갯수를 저장하는 변수
	int number = 0;//페이지 넘버링 변수
	
	//현재 보고자 하는 페이지숫자를 저장
	int currentPage = Integer.parseInt(pageNum);
	
	
	///////////////////////////////////////
	//전체 게시글의 내용을 jsp쪽으로 가져와야함
	BoardDAO bdao = new BoardDAO();
	
	//+전체 게시글의 갯수를 읽어들이는 메소드(전체 개수를 알아야 이전과 다음 카운터링을 붙일 수 있음)
	count = bdao.getAllcount(); 
	
	//현재 페이지에 보여줄 시작 번호를 설정 = 데이터 베이스에서 불러올 시작번호
	int startRow = (currentPage-1)*pageSize+1;
	int endRow = currentPage * pageSize;
	
	//최신글 10개를 기준으로 게시글을 리턴 받아주는 메소드 호출
	Vector<BoardBean> vec = bdao.getAllBoard(startRow, endRow);

	//테이븗에 표시할 번호를 지정
	number = count - (currentPage-1) * pageSize;
	
	
	//전체게시글을 리턴 받아주는 소스
	//Vector<BoardBean> vec = bdao.getAllBoard();
	
%>

<table width="700" boarder="1" bgcolor="white">
	<tr height="40">
		<td align="right" colspan="5">
		<input type="button" value="글쓰기" onclick="location.href='BoardWriteForm.jsp'"></td>
	</tr>
	
	<tr height="40">
		<td width="50" align="center"> 번호 </td>
		<td width="320" align="center"> 제목 </td>
		<td width="100" align="center"> 작성자 </td>
		<td width="150" align="center"> 작성일 </td>
		<td width="80" align="center"> 조회수 </td>
	</tr>
	
<%
	for(int i=0; i<vec.size(); i++){
		BoardBean bean = vec.get(i); //벡터에 저장된 빈클래스를 하나씩 추출
%>	
	<tr height="40">
<%-- 		<td width="50" align="center"> <%=i+1 %> </td> <!-- bean.getNum()하면 순서가 뒤죽박죽. 답변형게시판이라서 sorting을 ref순으로하고 restab을 asc기준으로 했기때문에.pk는 글보기를 위한 순서 -->
 --%>		
 			<td width="50" align="center"> <%=number-- %> </td> <!-- 카운터링 -->
 			<td width="320" align="left"> <a href="BoardInfo.jsp?num=<%=bean.getNum()%>" style="text-decoration:none;"> 
			
			<%//답변제목 들여쓰기
				if(bean.getRe_step()>1){
					for(int j=0; j < (bean.getRe_step()-1)*5; j++){ //들여쓰기 5칸씩
			%>&nbsp; 		
			<%  	}
				}
			%>
			
			<%=bean.getSubject() %> </a></td> <!-- 제목을 눌렀을때 pk값으로 글보기 넘어가기 -->
		<td width="100" align="center"> <%=bean.getWriter()%> </td>
		<td width="150" align="center"> <%=bean.getReg_date()%> </td>
		<td width="80" align="center"> <%=bean.getReadcount() %> </td>
	</tr>
<% } %>
	
</table>

<p>
<!-- 알고리즘 안돼면 일단 외우세요! -->
<!-- 페이지 카운터링 소스를 작성 -->
<%
	if(count > 0){
		int pageCount = count / pageSize + (count%pageSize == 0?0:1); //카운터링 숫자를 얼마까지 보여줄건지 결정
		
		//시작 페이지 숫자를 설정(1부터 보여줄건지 10부터 보여줄건지)
		int startPage = 1;
		
		if(currentPage %10 != 0){
			startPage = (int)(currentPage/10)*10+1;
		} else{
			startPage = ((int)(currentPage/10-1))*10+1;
		} 
		
		int pageBlock = 10; //카운터링 처리숫자
		int endPage = startPage+pageBlock-1; //화면에 보여줄 페이지의 마지막 숫자
		
		if(endPage > pageCount) endPage = pageCount;
			
		//이전이라는 링크를 만들건지 파악
		if(startPage > 10){
%>
	<a href="BoardList.jsp?pageNum<%=startPage-10%>"> [이전] </a>

<%
		}
		//페이징 처리
		for(int i = startPage; i<=endPage; i++){
%>	
	<a href="BoardList.jsp?pageNum=<%=i %>" style="text-decoration:none;"> [<%=i %>] </a>
<%		
		}
		
		//다음이라는 링크를 만들건지 파악
		if(endPage < pageCount){
%>
			
	<a href="BoardList.jsp?pageNum=<%=startPage+10 %>"> [다음] </a>
<%
		}
		
	}

%>

</p>
</center>

</body>
</html>