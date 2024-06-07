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
	


	public void deleteByNo(Long no, String pw) {
		try(
			Connection conn = getConnection(); 
			PreparedStatement pstmt = conn.prepareStatement("delete from guestbook where no=? and password=?");  
		){
			pstmt.setLong(1, no);
			pstmt.setString(2, pw);
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("error : "+e);
		}
		
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
			}finally {
				try {
					conn.rollback();
					
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
