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

	public void insert(GuestbookVo vo) {
		try(
				Connection conn = getConnection(); 
				PreparedStatement pstmt1 = conn.prepareStatement("insert into guestbook(name, password,contents,reg_date) values(?,?,?,now())");  
				PreparedStatement pstmt2 = conn.prepareStatement("select last_insert_id() from guestbook");  
					
			){
				pstmt1.setString(1, vo.getName());
				pstmt1.setString(2, vo.getPassword());
				pstmt1.setString(3, vo.getContents());
				pstmt1.executeUpdate();
				
				
				ResultSet rs = pstmt2.executeQuery();
				vo.setNo(rs.next() ? rs.getLong(1):null);
				rs.close();
				
			} catch (SQLException e) {
				System.out.println("error : " + e);
			}

		
	}
	
	public List<GuestbookVo> findAll(){
		List<GuestbookVo> result = new ArrayList<>();
		
		try(
				Connection conn = getConnection(); 
				PreparedStatement pstmt = conn.prepareStatement("select no,name,password,contents,reg_date from guestbook");  
					
			
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
