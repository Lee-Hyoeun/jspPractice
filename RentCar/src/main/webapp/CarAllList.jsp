<%@page import="db.CarListBean"%>
<%@page import="java.util.Vector"%>
<%@page import="db.RentcarDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<body>

<center>
<table width="1000">
<tr heigth="100">
	<td align="center" colspan="3">
	<font size="6" color="gray"> 전체 렌트카 보기 </font></td>
	</tr>
<%
	
	RentcarDAO rdao = new RentcarDAO();
	//정확히 몇개 받아올지 몰라서 벡터로 받음 
	Vector<CarListBean> v = rdao.getAllCar();
	// 테이블에 반복문 3개씩 돌려서 받아오면 또 새로 받아올수 있게끔
	//즉, tr을 3개씩 보여주고 다시 tr을 실행할 수 있도록 하는 변수 선언
	int j=0;
	for(int i=0; i<v.size(); i++){
		
		//벡터에 저장되어있는 빈클래스를 
		CarListBean bean = v.get(i);
		if(j%3==0){
%>
	<tr height="220">			
<% } %>
		<td width="333" align="center">
		<a href="RentcarMain.jsp?center=CarCategoryList.jsp?no=<%=bean.getNo()%>"> 
		<img alt="" src="img/<%=bean.getImg() %>" width="300" height="200">
		</a><p>
		<font size="3" color="gray"> <b>차량명 : <%=bean.getName() %></b></font></td>
	
<% 
		j=j+1; //j값을 증가하여 하나의 행에 총 3개의 차량정보를 보여주기 위해서 증가
	}
	
%>

</table>
</center>


</body>
</html>