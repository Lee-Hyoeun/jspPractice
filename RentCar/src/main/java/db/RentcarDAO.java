/**
 * Rentcar 프로젝트의 db속성 RentcarDAO
 * */

package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class RentcarDAO {
	
	Connection con; //sql커넥션 개체 만들기
	PreparedStatement pstmt; //
	ResultSet rs; //
	
	//커넥션 풀을 이용한 데이터베이스 연결 메소드
	public void getCon() {
		
		try {
			//server.xml에 등록한 name="jdbc/pool"을 가져와야해서 javax.naming.Context
			//jdcb/pool에 naming이 없을 수도 있기 때문에 NamingException도 해줘야 됨
			Context initctx = new InitialContext();
			//java:comp를 찾아 읽어들임
			Context envctx = (Context) initctx.lookup("java:comp/env");
			//server.xml에 데이터타입이 type="javax.sql.DataSource" CDATA이기때문에 DataSource로 다시 casting
			DataSource ds = (DataSource) envctx.lookup("jdbc/pool"); //즉 서버가 데이터베이스를 관리할 수 있도록 해줌
			con = ds.getConnection();
		
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
		//원래 아래가 오리지널 문법인데 위로 함축
//		} catch(NamingException e) {
//			e.printStackTrace();
//		} catch(SQLException e) {
//			e.printStackTrace();
//		}
		

}
