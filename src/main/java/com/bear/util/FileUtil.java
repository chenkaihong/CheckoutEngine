package com.bear.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

public class FileUtil {
	/**
	 * 创建一个新的文件,如果文件存在,则会被删除后重新进行建立
	 * @param address
	 * @return
	 * @throws IOException 
	 */
	public static File getNewFile(File newFile) throws IOException{
		if(newFile.isDirectory()){
			throw new RuntimeException("address must be a file! address : " + newFile);
		}
		if(newFile.exists()){
			newFile.delete();
		}else{
			newFile.getParentFile().mkdirs();
		}
		newFile.createNewFile();
		return newFile;
	}
	
	/**
	 * 将String写入文件
	 * @param content		需要写入文件的内容
	 * @param fileLocal		需要保存的文件路径
	 * @param isCover		是否需要覆盖原文件,如果不需要覆盖则继续在文件后方添加
	 * @param isMakeDir		是否创建父路径
	 * @return
	 * @throws IOException
	 */
	public static File fromStringToFile(String content, String fileLocal, boolean isCover, boolean isMakeDir) throws IOException{
		File file = new File(fileLocal);
		saveCheck(file, isMakeDir);
		
		StringReader sr = new StringReader(content);
		BufferedReader br = new BufferedReader(sr);
		FileWriter fw = new FileWriter(file, !isCover);
		BufferedWriter bw = new BufferedWriter(fw);
		
		try{
			String temp;
			while((temp = br.readLine()) != null){
				bw.write(temp+"\n");
			}
			bw.flush();
		} finally{
			CloseUtil.close(br, sr, bw, fw);
		}
		return file;
	}
	
	/**
	 * 将文件读成String
	 * @param fileLocal 需要读取的文件路径
	 * @return
	 * @throws IOException
	 */
	public static String fromFileToString(String fileLocal) throws IOException{
		File file = new File(fileLocal);
		if(!isCanRead(file)){
			throw new IOException("It's not a file! -> " + file);
		}
		
		FileReader fr = null;
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		try{
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			
			String temp;
			while((temp = br.readLine()) != null){
				sb.append(temp).append("\n");
			}
		}finally{
			CloseUtil.close(br, fr);
		}
		return sb.toString();
	}
	
	/**
	 * 将文件转化为字节流,逸出的资源句柄需要玩家自行管理关闭
	 * @return
	 * @throws IOException
	 */
	public static InputStream fromFileToStream(String fileLocal) throws IOException{
		File file = new File(fileLocal);
		if(!isCanRead(file)){
			throw new FileNotFoundException("It's not a file! -> " + file);
		}
		return new BufferedInputStream(new FileInputStream(file));
	}
	
	/**
	 * 将字节流写入文件,写完后会关闭字节流
	 * @param is			读入的字节流
	 * @param fileLocal		需要保存的文件路径
	 * @param isCover		是否需要覆盖原文件,如果不需要覆盖则继续在文件后方添加
	 * @param isMakeDir		是否创建父路径
	 * @return
	 * @throws IOException
	 */
	public static File fromStreamToFile(InputStream is,String fileLocal,boolean isCover,boolean isMakeDir) throws IOException{
		File file = new File(fileLocal);
		saveCheck(file, isMakeDir);
		BufferedInputStream bi = null;
		FileOutputStream os = null;
		BufferedOutputStream bo = null;
		try{
			bi = new BufferedInputStream(is);
			os = new FileOutputStream(file, !isCover);
			bo = new BufferedOutputStream(os);
			byte[] buf = new byte[1024];
			int len;
			while((len = is.read(buf)) != -1){
				bo.write(buf,0,len);
			}
			os.flush();
		}finally{
			CloseUtil.close(bo, is, os, bi);
		}
		return file;
	}
	
	/**
	 * 拷贝文件, 将 fileA 复制到 fileB 
	 * @param fileA
	 * @param fileB
	 * @param isCover 是否覆盖源文件
	 * @param isMakeDir 如果路径不合法,是否创建父路径
	 * @throws IOException
	 */
	public static File copyFile(String fileALocal,String fileBLocal,boolean isCover,boolean isMakeDir) throws IOException{
		return fromStreamToFile(fromFileToStream(fileALocal), fileBLocal, isCover, isMakeDir);
	}
	
	/**
	 * 在文件存档之前检查文件的路径是否可以访问,若不可以访问则抛出异常,若选择自动创建路径,则将isMakeDir置为true
	 * @param file
	 * @param isMakeDir true: 如果路径不合法,则创建父路径 - false: 直接抛出异常
	 * @param isCover 是否覆盖源文件
	 * @param isMakeDir 如果路径不合法,是否创建父路径
	 * @throws FileNotFoundException
	 */
	public static void saveCheck(File file, boolean isMakeDir) throws FileNotFoundException{
		if(file != null){
			if(file.isDirectory()){
				throw new FileNotFoundException("This path is a directory!You can't operate it! - " + file);
			}
			if(!file.exists()){
				File parentFile = file.getParentFile();
				if(!isExists(parentFile)){
					if(!isMakeDir){
						throw new FileNotFoundException("Can't find the parent's path! - " + file);
					}else{
						if(!parentFile.mkdirs()){
							throw new FileNotFoundException("Parent path create err! - " + file);
						}
					}
				}
			}
		}else{
			throw new FileNotFoundException("Can't process by a null file!");
		}
	}
	
	public static boolean isExists(File file){
		return file != null && file.exists();
	}
	public static boolean isCanRead(File file){
		return isExists(file) && file.isFile() && file.canRead();
	}
	public static boolean isCanWrite(File file){
		return isExists(file) && file.isFile() && file.canWrite();
	}
	public static boolean isCanReadAndWrite(File file){
		return isCanRead(file) && isCanWrite(file);
	}
}
