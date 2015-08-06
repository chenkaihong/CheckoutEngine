package com.bear.util.excelUtil;

import java.io.File;
import java.io.IOException;

public interface Excel {
	/**
	 * 在excel文档中获取新的sheet
	 */
	Excel getNewSheet(String sheetName);
	/**
	 * 将字符串写入文档的当前坐标,并将当前坐标右移一格
	 */
	Excel insertAndMove(String context);
	/**
	 * 将字符串写入文档的当前坐标,并将当前坐标右移一格
	 */
	Excel insertAndMove(int context);
	/**
	 * 设置excel文档中的当前x坐标
	 * @param x
	 * @return
	 */
	Excel setX(int x);
	/**
	 * 设置excel文档中的当前y坐标
	 * @param y
	 * @return
	 */
	Excel setY(int y);
	/**
	 * 换行
	 * @return
	 */
	Excel nextRow();
	/**
	 * 按照指定路径保存excel文档
	 * @throws IOException 
	 */
	File saveExcel(File file)  throws IOException;
	/**
	 * 按照固定路径保存excel文档(projectPath + "/DataFile/" + fileName)
	 * fileName 输入文件名
	 * @throws IOException 
	 */
	File saveExcel(String fileName)  throws IOException;
}
