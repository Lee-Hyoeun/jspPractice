/**
 * Rentcar 프로젝트의 db속성 RentcarDAO
 * */

package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

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
		

	
	//최신순 3대의 자동차를 리턴하는 메소드
	public Vector<CarListBean> getSelectCar(){
		
		//리턴타입을 설정
		Vector<CarListBean> v = new Vector<>();
		getCon(); //커넥션이 연결되어야 쿼리를 실행 가능
		
		try {
			String sql = "select * from rentcar order by no desc";
			pstmt = con.prepareStatement(sql);
			//쿼리 실행 후 결과를 Result타입으로 리턴
			rs = pstmt.executeQuery();
			int count=0;
			while(rs.next()) {
				
				CarListBean bean = new CarListBean();
    			bean.setNo(rs.getInt(1));
    			bean.setName(rs.getString(2));
    			bean.setCategory(rs.getInt(3));
    			bean.setPrice(rs.getInt(4));
    			bean.setUsepeople(rs.getInt(5));
    			bean.setCompany(rs.getString(6));
    			bean.setImg(rs.getString(7));
    			bean.setInfo(rs.getString(8));

    			//벡터의 빈클래스를 저장
    			v.add(bean); //둘다 상관없는데 add가 더 최신 버전 v.addElement(bean);
				count++;
				if(count > 2) break; //반복문 빠져나가기
				//3개만 벡터에 저장완료
				
			}
			con.close(); //연결 닫기
		} catch (Exception e) {
			e.printStackTrace();
		}
		return v;
	}
	
	
	
}
