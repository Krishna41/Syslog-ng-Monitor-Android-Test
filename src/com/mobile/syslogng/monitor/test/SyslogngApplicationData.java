package com.mobile.syslogng.monitor.test;

import java.io.File;

import android.content.Context;
import android.util.Log;

public class SyslogngApplicationData{

	private Context context;
	
	public SyslogngApplicationData(Context context){
		this.context = context;
	}
	
	public void remove(){
		
		File cacheDirectory = context.getCacheDir();
		File applicationDirectory = new File(cacheDirectory.getParent());
		if(applicationDirectory.exists()){
			String[] childDirectories = applicationDirectory.list();
			for(String directoryName : childDirectories){
				if(!directoryName.equals("lib") /*|| !directoryName.equals(cacheDirectory)*/){
					deleteDirectory(new File(applicationDirectory, directoryName));
				}
			}
		}
		
		
	}
	
	
	
	public static boolean deleteDirectory(File directory) {
	    if (directory != null && directory.isDirectory()) {
	        String[] children = directory.list();
	        //Recursively remove the files ** note - Do not delete the directories - will not be created automatically without an appload
	        for (Integer iterator = 0; iterator < children.length; iterator++) {
	            Boolean success = deleteDirectory(new File(directory, children[iterator]));
	            if (!success) {
	                return false;
	            }
	        }
	    }
	    if(directory.isFile()){
	    	Log.i("TAG",  (directory).getName() +" DELETED");
	    	return directory.delete();
	    }
	    else{
	    	return true;
	    }
	    
	}
}
