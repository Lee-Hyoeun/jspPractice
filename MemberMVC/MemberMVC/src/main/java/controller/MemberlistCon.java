package controller;

import java.io.IOException;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.MemberBean;
import model.MemberDAO;

@WebServlet("/MemberlistCon.do")
public class MemberlistCon extends HttpServlet {
  
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		reqPro(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		reqPro(request, response);
	}

	
	protected void reqPro(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//데이터베이스에 연결하여 회원의 모든 정보를 리턴
		MemberDAO mdao = new MemberDAO();
		Vector<MemberBean> v = mdao.getAllMember();
		
		// 데이터를 가지고 jsp로 넘겨주기
		request.setAttribute("v", v); //벡터를 jsp쪽으로 넘겨줌
		
		RequestDispatcher dis = request.getRequestDispatcher("MemberList.jsp");
		dis.forward(request, response);
	}

}
