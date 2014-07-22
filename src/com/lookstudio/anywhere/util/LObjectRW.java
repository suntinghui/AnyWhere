package com.lookstudio.anywhere.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;

public class LObjectRW {

	private File file;
	public LObjectRW(File dir,String fileName)
	{
		file = new File(dir,fileName);
		LLog.info("打开文件:" + file.getAbsolutePath());
	}
	
	public void delete()
	{
		file.delete();
	}
	
	public void write(Serializable obj)
	{
		if(file.exists() == false)
		{
			try {
				file.createNewFile();
			} catch (IOException e) {
				LLog.error("write error",e);
			}
		}
		
		try {
			ObjectOutputStream output = new ObjectOutputStream(
					new BufferedOutputStream(
							new FileOutputStream(file)));
			output.writeObject(obj);
			output.flush();
			output.close();
		} catch (FileNotFoundException e) {
			LLog.error("write error:"+ file.getAbsolutePath() , e);
		
		} catch (IOException e) {
			LLog.error("write error:"+ file.getAbsolutePath() , e);
		}
	}
	
	public Serializable read()
	{
		if((false == file.exists()) || (false == file.canRead()))
		{
			return null;
		}
		try {
			ObjectInputStream input  = new ObjectInputStream(new FileInputStream(file));
			Serializable obj = (Serializable)input.readObject();
			input.close();
			return  obj;
		} 
		 catch (ClassNotFoundException e) {
			 LLog.error("read error:"+ file.getAbsolutePath() , e);
		}catch (StreamCorruptedException e) {
			LLog.error("read error:"+ file.getAbsolutePath() , e);
		} catch (FileNotFoundException e) {
			LLog.error("read error:"+ file.getAbsolutePath() , e);
		} catch (IOException e) {
			LLog.error("read error:"+ file.getAbsolutePath() , e);
		}
		
		return null;
	}
}
