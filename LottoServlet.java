package com.lotto.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.*;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

import com.lotto.model.LottoJDBCDAO;
import com.lotto.model.LottoVO;

@WebServlet("/LottoServlet")
public class LottoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
		String action = request.getParameter("action");
		
		
		if ("check".equals(action)) {
			
			List<String> errorMsgs = new LinkedList<String>();
			request.setAttribute("errorMsgs", errorMsgs);
			
			/*隨機開獎號碼*/
			HashSet<Integer> set = new HashSet<Integer>();
			while(set.size()<6) {
				set.add((int)(Math.random()*49+1));
			}
			Integer[] ans = set.toArray(new Integer[set.size()]);
			
			/*取出投注號碼*/
			int[] chs = new int[6];
			for(int i = 0;i<chs.length;i++) {
				Integer a = new Integer(request.getParameter("ch"+i).trim());
				chs[i] = a;
				if (a < 1 || a > 49) {
					errorMsgs.add("數字範圍錯誤，請選擇1~49");
				}
			}
			
			/*算出中獎總數*/
			int sum = 0;
			for(int i = 0; i<ans.length; i++) {
				for(int j = 0; j<chs.length; j++) {
					if (ans[i] == chs[j]) {
						sum++;
					}
				}
			}
			
			/*跳出錯誤*/
			if (!errorMsgs.isEmpty()) {
				RequestDispatcher failureView = request.getRequestDispatcher("/front-end/LottoClaim.jsp");
				failureView.forward(request, response);
				return; // 程式中斷
			}
			
			/*把兌獎紀錄寫入資料庫*/
			LottoVO lottoVO = new LottoVO();
			lottoVO.setWinnumber(set.toString());
			lottoVO.setChoosenumber(Arrays.toString(chs));
			lottoVO.setResult(sum);
			
			LottoJDBCDAO dao = new LottoJDBCDAO();
			dao.insert(lottoVO);
			
			/*將兌獎結果記錄到log4j2*/
			ConfigurationSource source;
			String relativePath = "D:/log4j2.xml";
			File log4jFile = new File(relativePath);
			try {
				if (log4jFile.exists()) {
					source = new ConfigurationSource(new FileInputStream(log4jFile), log4jFile);
					Configurator.initialize(null, source);
					Logger log = LogManager.getLogger("practice");
					log.info("開獎號碼:" + set.toString() + "投注號碼" + Arrays.toString(chs) + "中獎總數" + sum);
					
				} else {
					System.out.println("loginit failed");
					System.exit(1);
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			}
			
			
			/*存取資料及畫面轉換*/
			request.setAttribute("sum", sum); 
			request.setAttribute("set", set); 
			request.setAttribute("chs", chs);
			String url = "/front-end/LottoClaim.jsp";
			RequestDispatcher successView = request.getRequestDispatcher(url); 
			successView.forward(request, response);
			
			
		}
		

				
				





			
		
	}

}
