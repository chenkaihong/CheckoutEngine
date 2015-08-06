package com.bear.servlet.newMarriageFix;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bear.connection.ConnectionManager;
import com.bear.connection.ServerNode;
import com.bear.connection.ServerNodeFactory;

/**
 * Copyright (c) 2011-2012 by 广州游爱 Inc.
 * 用于调用各服的修复文招问题的url
 * @Author Create by ckh
 * @Date 2014-11-12 下午4:34:09
 * @Description
 */
public class NewMarriageFix extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String url = "%s/TestFix";
		
		String msg = "";
		String view = req.getParameter("view");
		String update = req.getParameter("update");
		String playerID = req.getParameter("playerID");
		String serverID = req.getParameter("serverID");
		
		if(playerID == null || "".equals(playerID)){
			msg = "Please enter the playerID!";
			req.setAttribute("msg", msg);
			req.getRequestDispatcher("index.jsp").forward(req, resp);;
			return;
		}
		playerID = playerID.trim();
		if(!playerID.matches("^\\d+$")){
			msg = "PlayerID contend error chat!Please enter the right ID!";
			req.setAttribute("msg", msg);
			req.getRequestDispatcher("index.jsp").forward(req, resp);;
			return;
		}
		if(serverID == null || "".equals(serverID)){
			msg = "Please choose the server!";
			req.setAttribute("msg", msg);
			req.setAttribute("playerID", playerID);
			req.setAttribute("serverID", serverID);
			req.getRequestDispatcher("index.jsp").forward(req, resp);;
			return;
		}
		serverID = serverID.trim();
		if(!serverID.matches("^\\d+$")){
			msg = "ServerID contend error chat!Please enter the right ID!";
			req.setAttribute("msg", msg);
			req.setAttribute("playerID", playerID);
			req.setAttribute("serverID", serverID);
			req.getRequestDispatcher("index.jsp").forward(req, resp);;
			return;
		}
		
		Map<Integer,ServerNode> serverNodeMap = ConnectionManager.getDataDB(ServerNodeFactory.onlineServerNode);
		ServerNode serverNode = serverNodeMap.get(Integer.parseInt(serverID));
		if(serverNode == null){
			msg = "ServerNode is null!Please enter the right serverID!";
			req.setAttribute("msg", msg);
			req.setAttribute("playerID", playerID);
			req.setAttribute("serverID", serverID);
			req.getRequestDispatcher("index.jsp").forward(req, resp);;
			return;
		}
		url = String.format(url, serverNode.getGame_addr());
		
		if("view".equals(view)){
			msg = sendPost(url, String.format("action=NewMarriageFix&fengbird__updateCacheAndDB=false&playerID=%s",playerID));
		}else if("update".equals(update)){
			msg = sendPost(url, String.format("action=NewMarriageFix&fengbird__updateCacheAndDB=true&playerID=%s",playerID));
		}else{
			msg = "Commend is error!";
		}
		
		req.setAttribute("msg", msg);
		req.setAttribute("playerID", playerID);
		req.setAttribute("serverID", serverID);
		req.getRequestDispatcher("index.jsp").forward(req, resp);;
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
	
	/**
     * 向指定 URL 发送POST方法的请求
     * 
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }    
}