package guestbook.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import guestbook.vo.GuestbookVo;

public class GuestbookDao {
	private Connection getConnection() throws SQLException {
		Connection conn = null;
		try {
		Class.forName("org.mariadb.jdbc.Driver");
		
		String url = "jdbc:mariadb://192.168.64.3:3306/webdb?charset=utf-8";
		
		conn = DriverManager.getConnection(url, "webdb", "webdb");
		}catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패: "+e);
		} 
		return conn;
	}
	

	public int deleteByNo(Long no, String password) {
	    int result = 0;
	    Connection conn = null;
	    PreparedStatement pstmt1 = null;
	    PreparedStatement pstmt2 = null;

	    try {
	      conn = getConnection();

	      // String date =
	      // conn.prepareStatement("select date_format('%yyyy-%MM-%dd', reg_date) from guestbook where
	      // no = ?");

	      // 1. Parameter Binding
	      pstmt1 = conn.prepareStatement(
	          "update guestbook_log set count = count - 1 where date = (select date(reg_date) from guestbook where no = ?)");
	      pstmt1.setLong(1, no);

	      // 2. Parameter Binding
	      pstmt2 = conn.prepareStatement("delete from guestbook where no = ? and password = ?");
	      pstmt2.setLong(1, no);
	      pstmt2.setString(2, password);

	      // TX:BEGIN // - 오직 statement들 실행만
	      conn.setAutoCommit(false);

	      // DML2 실행
	      result = pstmt2.executeUpdate();

	      // DML1 실행
	      // 비밀번호를 잘못 입력했을 경우도 생각
	      if (result == 1) {
	        pstmt1.executeUpdate();
	      }

	      // TX: END(SUCCESS) //
	      conn.commit();

	    } catch (SQLException e) {
	      System.out.println("Error:" + e);
	      // TX: END(FAIL) //
	      // rollback이 비즈니스쪽에 있으면 안됨.
	      // 예외를 논리로 쓰면 안됨.
	      // 이 자리에 있어야한다.
	      try {
	        if (conn != null) {
	          conn.rollback();
	        }
	      } catch (SQLException ignored) {

	      }
	    } finally {
	      try {
	        if (pstmt1 != null) {
	          pstmt1.close();
	        }
	        if (pstmt2 != null) {
	          pstmt2.close();
	        }
	        if (conn != null) {
	          conn.close();
	        }
	      } catch (SQLException e) {
	        System.out.println("error: " + e);
	      }
	    }

	    return result;
	  }

	public int insert(GuestbookVo vo) {
		int result = 0;
		Connection conn = null;
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		PreparedStatement pstmt3 = null;
		ResultSet rs = null;
		try{
			conn = getConnection(); 
			
			
			
			
			// guestbook_log update
			pstmt1 = conn.prepareStatement("update guestbook_log set count = count + 1 where date = current_date()");
			pstmt2 = conn.prepareStatement("insert into guestbook_log values(current_date(),1)");

			// TX:BEGIN // 
			conn.setAutoCommit(false);
			pstmt3 = conn.prepareStatement("insert into guestbook(name, password,contents,reg_date) values(?,?,?,now())");  
				
			pstmt3.setString(1, vo.getName());
			pstmt3.setString(2, vo.getPassword());
			pstmt3.setString(3, vo.getContents());
			//DML1
			int rowCount = pstmt1.executeUpdate();
			
			//DML2
			if(rowCount == 0) {
				pstmt2.executeUpdate();
			}
			
			//DML3
			result = pstmt3.executeUpdate();
			
			
			//TX:END(Succeess)//
			conn.commit();
			
		
				
			} catch (SQLException e) {				
				System.out.println("error : " + e);
				//TX:END(FAIL)//
				try {
					conn.rollback();
				} catch (SQLException ignored) {

				}
			}finally {
				try {
					
					if(pstmt1!=null) {
					pstmt1.close();}
					if(pstmt2!=null) {
						pstmt2.close();}
					if(pstmt3!=null) {
						pstmt3.close();}
					if(conn!=null) {
					conn.close();}
					if(rs!=null) {
						rs.close();
					}
				}catch(SQLException ignored){
					
				}
			}
		return result;
		
	}
	
	public List<GuestbookVo> findAll(){
		List<GuestbookVo> result = new ArrayList<>();
		
		try(
				Connection conn = getConnection(); 
				PreparedStatement pstmt = conn.prepareStatement("select no,name,password,contents,reg_date from guestbook order by no desc");  
					
			
			){
			
				ResultSet rs = pstmt.executeQuery();
				
				while(rs.next()) {
					GuestbookVo vo = new GuestbookVo();
					
					Long no = rs.getLong(1);
					String name = rs.getString(2);
					String pw = rs.getString(3);
					String contents = rs.getString(4);
					String regDate = rs.getString(5);
					
					vo.setNo(no);
					vo.setName(name);
					vo.setPassword(contents);
					vo.setContents(contents);
					vo.setRegDate(regDate);
					
					result.add(vo);
				}
			} catch (SQLException e) {
				System.out.println("error : "+e);
			}

		
		return result;
	}
}
