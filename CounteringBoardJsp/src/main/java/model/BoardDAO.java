package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class BoardDAO {

	// 커넥션 연결
	Connection con;
	PreparedStatement pstmt;
	ResultSet rs;
	
	//데이터 베이스의 커넥션 풀을 사용하도록 설정하는 메소드
	public void getCon() {
		try {
			Context initctx = new InitialContext();
			Context envctx = (Context) initctx.lookup("java:comp/env");
			DataSource ds = (DataSource) envctx.lookup("jdbc/pool");
			
			//datasource
			con=ds.getConnection();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	//하나의 새로운 게시글이 넘어와서 저장되는 메소드
	public void insertBoard(BoardBean bean) {
		
		getCon(); // 데이터베이스 연결
		//빈클래스에 넘어오지 않았던 데이터들을 초기화 해주어야 함.
		int ref=0; //글그룹 의미 = 쿼리를 실행시켜서 가장 큰 ref값을 가져온 후 +1을 더해주면 됨
		int re_step=1; //새글이기에 = 부모글이기에
		int re_level=1;
		
		try {
			// 가장 큰 ref값을 읽어오는 쿼리 준비
			String refsql = "select max(ref) from board"; // 초기값은 0이 리턴
			//쿼리실행 객체
			pstmt = con.prepareStatement(refsql);
			//쿼리실행 후 결과를 리턴
			rs = pstmt.executeQuery();
			if(rs.next()) { //쿼리에 결과값이 있다면
				ref = rs.getInt(1)+1; //최대값에 +1해서 글그룹을 설정
				
			}
			//실제로 게시글 전체값을 테이블에 저장
			String sql = "insert into board values(board_seq.NEXTVAL,?,?,?,?,sysdate,?,?,?,0,?)";
			pstmt = con.prepareStatement(sql);
			//?에 값을 맵핑
			pstmt.setString(1, bean.getWriter());
			pstmt.setString(2, bean.getEmail());
			pstmt.setString(3, bean.getSubject());
			pstmt.setString(4, bean.getPassword());
			pstmt.setInt(5, ref);
			pstmt.setInt(6, re_step);
			pstmt.setInt(7, re_level);
			pstmt.setString(8, bean.getContent());
			//쿼리를 실행
			pstmt.executeUpdate();
			//자원 반납
			con.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	
	//모든 게시글을 리턴해주는 메소드 작성
	public Vector<BoardBean> getAllBoard(){
	
		//리턴할 객체 선언 
		Vector<BoardBean> v = new Vector<>();
		getCon();
		
		try {
			//쿼리준비
			String sql = "select * from board order by ref desc, re_step asc ";
			//쿼리를 실행할 객체 선언
			pstmt = con.prepareStatement(sql);
			//쿼리실행 후 결과 저장
			rs = pstmt.executeQuery();
			//데이터 개수가 몇개인지 모르기에 반복문을 이용하여 데이터를 추출
			while(rs.next()) {
				//데이터를 패키징(가방 = Boardbean클래스를 이용)해줌
				BoardBean bean = new BoardBean();
				bean.setNum(rs.getInt(1));
				bean.setWriter(rs.getString(2));
				bean.setEmail(rs.getString(3));
				bean.setSubject(rs.getString(4));
				bean.setPassword(rs.getString(5));
				bean.setReg_date(rs.getDate(6).toString());
				bean.setRef(rs.getInt(7));
				bean.setRe_step(rs.getInt(8));
				bean.setRe_level(rs.getInt(9));
				bean.setReadcount(rs.getInt(10));
				bean.setContent(rs.getString(11));
				
				//패키징한 데이터를 순서대로 벡터에 저장
				v.add(bean);
			}
			con.close(); //자원 반납
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return v;
	}
	
	
	//BoardInfo 하나의 게시글을 리턴하는 메소드
	public BoardBean getOneBoard(int num) {
		
		//리턴타입 선언 
		BoardBean bean = new BoardBean();
		getCon();
		
		try {
			
			//조회수 증가쿼리
			String readsql = "update board set readcount = readcount+1 where num=?";
			pstmt = con.prepareStatement(readsql);
			pstmt.setInt(1, num);
			pstmt.executeUpdate();
			
			//1.쿼리준비
			String sql="select * from board where num=?";
			//2.쿼리실행객체
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, num);
			//3.쿼리실행후 결과를 리턴
			rs = pstmt.executeQuery();
			if(rs.next()) {
				bean.setNum(rs.getInt(1));
				bean.setWriter(rs.getString(2));
				bean.setEmail(rs.getString(3));
				bean.setSubject(rs.getString(4));
				bean.setPassword(rs.getString(5));
				bean.setReg_date(rs.getDate(6).toString());
				bean.setRef(rs.getInt(7));
				bean.setRe_step(rs.getInt(8));
				bean.setRe_level(rs.getInt(9));
				bean.setReadcount(rs.getInt(10));
				bean.setContent(rs.getString(11));
			}
			//자원반납
			con.close();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return bean;
	}
	
	
	
	//답변글이 저장되는 메소드
	public void reWriteBoard(BoardBean bean) {
		// 답변글은 insert로 단순히 집어넣는게x
		// 답변형게시판의 가장 큰 특징: 글을 그룹으로 만드는것, re_step과 re_level의 단계를 먼저 수정
		
		//부모글그룹과 글레벨 글스텝을 읽어들임(증가시켜주기 위해)
		int ref = bean.getRef();
		int re_step = bean.getRe_step();
		int re_level = bean.getRe_level();
		
		getCon();
		
		try {
			/////// 핵심코드 ///////
			//부모글보다 큰 re_level의 값을 전부 1씩 증가시켜줌
			String levelsql = "update board set re_level=re_level+1 where ref=? and re_level>?";
			//쿼리실행객체 확인
			pstmt = con.prepareStatement(levelsql);
			pstmt.setInt(1, ref);
			pstmt.setInt(2, re_level);
			//쿼리실행
			pstmt.executeUpdate();
			//답변글 데이터를 저장
			String sql = "insert into board values(board_seq.NEXTVAL,?,?,?,?,sysdate,?,?,?,0,?)"; 
			pstmt = con.prepareStatement(sql);
			//?에 값을 대입
			pstmt.setString(1, bean.getWriter());
			pstmt.setString(2, bean.getEmail());
			pstmt.setString(3, bean.getSubject());
			pstmt.setString(4, bean.getPassword());
			pstmt.setInt(5, ref); //부모의 ref값을 넣어줌
			pstmt.setInt(6, re_step+1); //답글이기에 부모글 re_step에 1을 더해줌
			pstmt.setInt(7, re_level+1);
			pstmt.setString(8,  bean.getContent());
			pstmt.executeUpdate();
			
			con.close();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
		
		//boardUpdate용 하나의 게시글을 리턴. Delete시에도 마찬가지 메소드
		public BoardBean getOneUpdateBoard(int num) {
			
			//리턴타입 선언 
			BoardBean bean = new BoardBean();
			getCon();
			
			try {
				
				//1.쿼리준비
				String sql="select * from board where num=?";
				//2.쿼리실행객체
				pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, num);
				//3.쿼리실행후 결과를 리턴
				rs = pstmt.executeQuery();
				if(rs.next()) {
					bean.setNum(rs.getInt(1));
					bean.setWriter(rs.getString(2));
					bean.setEmail(rs.getString(3));
					bean.setSubject(rs.getString(4));
					bean.setPassword(rs.getString(5));
					bean.setReg_date(rs.getDate(6).toString());
					bean.setRef(rs.getInt(7));
					bean.setRe_step(rs.getInt(8));
					bean.setRe_level(rs.getInt(9));
					bean.setReadcount(rs.getInt(10));
					bean.setContent(rs.getString(11));
				}
				//자원반납
				con.close();
				
			} catch(Exception e) {
				e.printStackTrace();
			}
			
			return bean;
		}
	
		
		
    //update와 delete시 필요한 패스워드값을 리턴해주는 메소드
	public String getPass(int num) {
		//리턴할 변수 객체 선언 
		String pass="";
		
		getCon();
		try {
			//쿼리준비
			String sql="select password from board where num=?";
			//쿼리실행할 객체 선언
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, num);
			rs = pstmt.executeQuery();
			//패스워드 값을 저장
			if(rs.next()) {
				pass = rs.getString(1);
			}
			//자원반납
			con.close();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return pass;	
	}
	
	
	//하나의 게시글을 수정하는 메소드.
	public void updateBoard(BoardBean bean) {
		
		//리턴타입은 없으므로 바로 getConnection
		getCon();
		
		try {
			//쿼리준비
			String sql = "update board set subject=?, content=? where num=?";
			pstmt = con.prepareStatement(sql);
			
			//?값을 대입
			pstmt.setString(1,  bean.getSubject());
			pstmt.setString(2, bean.getContent());
			pstmt.setInt(3, bean.getNum());
			
			pstmt.executeUpdate();
			
			con.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	//하나의 게시글을 삭제하는 메소드
	public void deleteBoard(int num) {
		
		getCon();
		
		try {
			//쿼리준비
			String sql = "delete from board where num=?";
			pstmt = con.prepareStatement(sql);
			//?
			pstmt.setInt(1, num);
			//쿼리실행
			pstmt.executeUpdate();
			//자원반납
			con.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	///////////////////////////////////
	//카운터링
	//전체 글의 갯수를 리턴하는 메소드
	public int getAllcount() {
		
		getCon();
		
		//게시글 전체수를 저장하는 변수
		int count = 0;
		try {
			//쿼리준비(카운트를 한꺼번에 해주는 함수)
			String sql = "select count(*) from board";
			//쿼리를 실행할 객체 선언
			pstmt = con.prepareStatement(sql);
			//쿼리 실행 후 결과를 리턴
			rs = pstmt.executeQuery();
			if(rs.next()) {
				count = rs.getInt(1); //전체게시글 수
			}
			con.close();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return count;
		
	}
	
	
	//모든 게시글을 리턴해주는 메소드 작성
	public Vector<BoardBean> getAllBoard(int start, int end){
	
		//리턴할 객체 선언 
		Vector<BoardBean> v = new Vector<>();
		getCon();
		
		try {
			//쿼리준비 (A의 모든 결과를 정렬시키고 난 후 A의 모든 결과와 Rownum을 같이 셀렉트(검색)한 후 거기서 Rownum을 기준으로 우리가 원하는 데이터만큼 추출해서 그 결과를 셀렉트 하시오.라는 의미) 
			String sql = "select * from (select A.*, Rownum Rnum from (select * from board order by ref desc, re_step asc)A) "
					+ "where Rnum >= ? and Rnum <=? ";  
			//쿼리를 실행할 객체 선언
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, start);
			pstmt.setInt(2, end);
			
			//쿼리실행 후 결과 저장
			rs = pstmt.executeQuery();
			//데이터 개수가 몇개인지 모르기에 반복문을 이용하여 데이터를 추출
			while(rs.next()) {
				//데이터를 패키징(가방 = Boardbean클래스를 이용)해줌
				BoardBean bean = new BoardBean();
				bean.setNum(rs.getInt(1));
				bean.setWriter(rs.getString(2));
				bean.setEmail(rs.getString(3));
				bean.setSubject(rs.getString(4));
				bean.setPassword(rs.getString(5));
				bean.setReg_date(rs.getDate(6).toString());
				bean.setRef(rs.getInt(7));
				bean.setRe_step(rs.getInt(8));
				bean.setRe_level(rs.getInt(9));
				bean.setReadcount(rs.getInt(10));
				bean.setContent(rs.getString(11));
				
				//패키징한 데이터를 순서대로 벡터에 저장
				v.add(bean);
			}
			con.close(); //자원 반납
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return v;
	}
}
