package com.bear.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

public class PrintUtil {
	/**
	 * 以UTF-8编码输出字符串到浏览器上
	 * @param resp
	 * @param content
	 * @throws ServletException
	 * @throws IOException
	 */
	public static void outputStringResponse(HttpServletResponse resp, String content) throws ServletException, IOException {
		resp.setHeader("Cache-Control", "no-cache");
		resp.setContentType("text/plain;charset=UTF-8");
		resp.setCharacterEncoding("UTF-8");
		PrintWriter pw = resp.getWriter();
		pw.print(content);
		pw.flush();
	}
	
	/**
	 * 以UTF-8编码传送文件到浏览器中
	 * @param resp
	 * @param file
	 * @throws IOException 
	 */
	public static void outputFileResponse(HttpServletResponse resp, File file) throws IOException{
		if(file == null || !file.exists()){
			throw new RuntimeException("@@@@ when outputFileResponse , no find the file : " + file);
		}
        resp.reset();
        resp.setContentType("application/vnd.ms-excel");
		resp.addHeader("Content-Disposition", "attachment;filename=" + new String(file.getName().getBytes("utf-8"),"ISO-8859-1"));
		resp.addHeader("Content-Length", "" + file.length());
		resp.setContentType("text/plain;charset=UTF-8");
		resp.setCharacterEncoding("UTF-8");
		
		@SuppressWarnings("unused")
		int length = 0;
		byte[] buffer = new byte[1024];
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
		BufferedOutputStream out = new BufferedOutputStream(resp.getOutputStream());
		try{
			while((length=in.read(buffer, 0, buffer.length)) != -1){
				out.write(buffer);
				out.flush();
			}
		}finally{
			ClosedUtil.closedInputStream(in);
			ClosedUtil.closedOutputStream(out);
		}
	}
}
