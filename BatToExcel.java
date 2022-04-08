package com.poi.example;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDate;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class BatToExcel {
	static String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	static String url = "jdbc:sqlserver://127.0.0.1:1433;DatabaseName=lotto;trustServerCertificate=true";
	static String userid = "sa";
	static String passwd = "@1177tech";
	
	public static String writeDbtoExcel(){
		LocalDate todaysDate = LocalDate.now();
		String path="D:lotto"+todaysDate+".xls";

		HSSFWorkbook book=new HSSFWorkbook();
		HSSFSheet sheet=book.createSheet("兌獎表");
		Connection con = null;
		PreparedStatement st = null;
		try {
			Class.forName(driver);
			
			con = DriverManager.getConnection(url, userid, passwd);
			st=con.prepareStatement("SELECT winnumber, choosenumber, time, result FROM dbo.lottolist");
			ResultSet rs=st.executeQuery();
			ResultSetMetaData rsmd=rs.getMetaData();//得到結果集的欄位名
			int c=rsmd.getColumnCount();//得到資料表的結果集的欄位的數量
			//生成表單的第一行，即表頭
			HSSFRow row0=sheet.createRow(0);//建立第一行
			sheet.setColumnWidth(0, 100 * 100);
			sheet.setColumnWidth(1, 100 * 100);
			sheet.setColumnWidth(2, 100 * 100);
			
			for(int i=0;i<c;i++){
				HSSFCell cel=row0.createCell(i);//建立第一行的第i列
				cel.setCellValue(rsmd.getColumnName(i+1));
//				cel.setCellStyle(style);
			}
			
			//將資料表中的資料按行匯入進Excel表中
			int r=1;
			while(rs.next()){
				HSSFRow row=sheet.createRow(r++);//建立非第一行的其他行
				for(int i=0;i<c;i++){//仍然是c列，匯入第r行的第i列
					HSSFCell cel=row.createCell(i);
					//以下兩種寫法均可
//					cel.setCellValue(rs.getString(rsmd.getColumnName(i+1)));
					cel.setCellValue(rs.getString(i+1));
				}
			}
			//用檔案輸出流類建立名為table的Excel表格
			FileOutputStream out=new FileOutputStream(path);
			book.write(out);//將HSSFWorkBook中的表寫入輸出流中
			book.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (st != null) {
				try {
					st.close();
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
		return path;
	}
     
    public static void main(String[] args) {
    	writeDbtoExcel();
    	System.out.println("完成");
    }
}
