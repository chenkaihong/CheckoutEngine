package com.bear.util.excelUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.bear.util.ClosedUtil;
import com.bear.util.FileUtil;
/**
 * 非线程安全的excel写入工具
 * @author Administrator
 *
 */
public class ExcelUtil implements Excel{
	
	private final int initX = 2;
	private final int initY = 2;
	
	private XSSFWorkbook book;
	private XSSFSheet sheet;
	private int x;
	private int y;
	
	private ExcelUtil(){
		this.book = new XSSFWorkbook();
		this.sheet = null;
		this.x = initX;
		this.y = initY;
	}
	
	public static ExcelUtil getInstance(){
		return new ExcelUtil();
	}
	
	/**
	 * 设置excel文档中的当前x坐标
	 * @param x
	 * @return
	 */
	public Excel setX(int x){
		this.x = x;
		return this;
	}
	/**
	 * 设置excel文档中的当前y坐标
	 * @param y
	 * @return
	 */
	public Excel setY(int y){
		this.y = y;
		return this;
	}
	/**
	 * 将字符串写入文档的当前坐标,并将当前坐标右移一格
	 */
	@Override
	public Excel insertAndMove(String context){
		XSSFCell cell = getCell(getRow(y), x);
		cell.setCellValue(new XSSFRichTextString(context));
		x++;
		return this;
	}
	@Override
	public Excel insertAndMove(int context){
		XSSFCell cell = getCell(getRow(y), x);
		cell.setCellValue(new XSSFRichTextString(context+""));
		x++;
		return this;
	}
	/**
	 * 换行
	 * @return
	 */
	public Excel nextRow(){
		this.x = initX;
		this.y++;
		return this;
	}
	/**
	 * 在excel文档中获取新的sheet
	 */
	@Override
	public Excel getNewSheet(String sheetName){
		this.sheet = book.createSheet(sheetName);
		this.x = initX;
		this.y = initY;
		return this;
	}
	/**
	 * 按照指定路径保存excel文档
	 * @throws IOException 
	 */
	@Override
	public File saveExcel(File file) throws IOException{
		OutputStream out = null;
		try {
			out = new FileOutputStream(FileUtil.getNewFile(file));
			book.write(out);
		    System.out.println("@@@@ Create a new excel at : " + file);
		} finally{
			ClosedUtil.closedOutputStream(out);
		}
		return file;
	}
	/**
	 * 按照固定路径保存excel文档(projectPath + "/DataFile/" + fileName)
	 * fileName 输入文件名
	 * @throws IOException 
	 */
	@Override
	public File saveExcel(String fileName) throws IOException{
		return saveExcel(new File(System.getProperty("user.dir") + "/DataFile/" + fileName));
	}
	// 获取表格中行操作元素
	private XSSFRow getRow(int index){
		XSSFRow row = sheet.getRow(index - 1);
		if(row == null){
			row = sheet.createRow(index - 1);
		}
		return row;
	}
	// 获取表格中单元格操作元素
	private XSSFCell getCell(XSSFRow row,int index){
		XSSFCell cell = row.getCell(index-1);
		if(cell == null){
			cell = row.createCell(index-1);
		}
		return cell;
	}
}
