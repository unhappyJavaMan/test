package com.lotto.model;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LottoJDBCDAO implements LottoInterface {
	String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	String url = "jdbc:sqlserver://127.0.0.1:1433;DatabaseName=lotto;trustServerCertificate=true";
	String userid = "sa";
	String passwd = "@1177tech";
	
	private static final String INSERT_STMT = 
			"INSERT INTO dbo.lottolist (winnumber, choosenumber, time, result) values (?, ?,  GETDATE(), ?) ";
	private static final String GET_ALL_STMT = 
			"SELECT winnumber, choosenumber, time, result FROM dbo.lottolist";
			

	@Override
	public void insert(LottoVO lottoVO) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(INSERT_STMT);

			pstmt.setString(1, lottoVO.getWinnumber());
			pstmt.setString(2, lottoVO.getChoosenumber());
			pstmt.setInt(3, lottoVO.getResult());

			pstmt.executeUpdate();

			// Handle any driver errors
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
			// Clean up JDBC resources
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
	}

	@Override
	public List<LottoVO> getAll() {
		List<LottoVO> list = new ArrayList<>();
		LottoVO lottoVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		

		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(GET_ALL_STMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				lottoVO = new LottoVO();
				lottoVO.setWinnumber(rs.getString("winnumber"));
				lottoVO.setChoosenumber(rs.getString("choosenumber"));
				lottoVO.setTime(rs.getDate("time"));
				lottoVO.setResult(rs.getInt("result"));
				list.add(lottoVO); // Store the row in the list
			}

			// Handle any driver errors
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
			// Clean up JDBC resources
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
		return list;
	}
	
	public static void main(String[] srg) {
		String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver"; // 載入JDBC驅動
		String dbURL = "jdbc:sqlserver://127.0.0.1:1433;DatabaseName=lotto;trustServerCertificate=true"; // 連線伺服器和資料庫test
		String userName = "sa"; // 預設使用者名稱
		String userPwd = "@1177tech"; // 密碼
		Connection dbConn;
		try {
			Class.forName(driverName);
			dbConn = DriverManager.getConnection(dbURL, userName, userPwd);
			System.out.println("Connection Successful!"); // 如果連線成功 控制檯輸出Connection Successful!
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
