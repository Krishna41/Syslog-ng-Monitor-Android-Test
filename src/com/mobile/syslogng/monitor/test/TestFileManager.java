package com.mobile.syslogng.monitor.test;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;



import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.test.AndroidTestCase;

import com.mobile.syslogng.monitor.FileManager;


public class TestFileManager extends AndroidTestCase {

	public TestFileManager(){
		
	}
	
	public TestFileManager(String name){
		setName(name);
	}
	
	
	private static Context context;
	private FileManager fileManager;
	private static SyslogngApplicationData data;
	private static Boolean isFirstRun = true;
	
	private static String downloadPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
	
	@Override
	protected void setUp() throws Exception {
		context = getContext();
		fileManager	= new FileManager(context);
		if(isFirstRun){
			data = new SyslogngApplicationData(context);
			data.remove();
		}
	}
	
	
	
	
	
	public void testGetCertificateDirectory(){
		assertNotNull(fileManager.getCertificateDirectory());
		assertTrue(fileManager.getCertificateDirectory().equals(context.getFilesDir()+"/certificates"));
	}
	
	public void testGetCertificateFile(){
		assertTrue(fileManager.getCertificateFile("Sample").equals(context.getFilesDir()+"/certificates/Sample"));
	}
	
	public void testGetCertificateFileFailedCase(){
		assertTrue(!fileManager.getCertificateFile(null).equals(context.getFilesDir()+"/certificates/Sample"));
	}
	
	public void testImportCertificate(){
		assertTrue(copySampleCertificateToDownloads());
		assertTrue(importCertificateFromDownloads());
	}
	
	
	
	
	//Utilites
	
	private Boolean importCertificateFromDownloads(){
		Boolean status;
		String sourcePath = downloadPath+"/SampleCertificate.pfx";
		String certificateName = "SampleCertificate.pfx";
		fileManager.createCertificateDirectory();
		status = fileManager.copyFile(sourcePath, fileManager.getCertificateFile(certificateName));
		return status;
	}
	
	private Boolean copySampleCertificateToDownloads(){
			
		Boolean status = false;
		if(!isEmulator()){
			
				Integer read = 0;
				InputStream readFileInputStream = null;
				OutputStream writeFileOutputStream = null;
				
				String inputFile = "res/raw/samplecert.pfx";
				File outputFile = new File(downloadPath+"/SampleCertificate.pfx");
			
				try{
					readFileInputStream = this.getClass().getClassLoader().getResourceAsStream(inputFile);
					writeFileOutputStream = new FileOutputStream(outputFile); 
				
					byte[] bytes = new byte[1024];
					while ((read = readFileInputStream.read(bytes)) != -1){
						writeFileOutputStream.write(bytes, 0, read);
					}
					status = true;
				}
				catch(IOException e){
					e.printStackTrace();
				}
				finally{
					try {
						if(writeFileOutputStream != null){
							writeFileOutputStream.close();
							}
						if(readFileInputStream != null){
							readFileInputStream.close();
						}
						
					} catch (IOException e) {

						e.printStackTrace();
					}
					
				}
			
			}
		
		return status;
		
	}
		
		
	
	
	private boolean isEmulator() {
        return Build.FINGERPRINT.startsWith("generic");
    }
	
	@Override
	public void tearDown() throws Exception {
		
	}
	
}
