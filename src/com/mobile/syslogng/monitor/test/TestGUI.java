package com.mobile.syslogng.monitor.test;


import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.robotium.solo.Solo;
import com.mobile.syslogng.monitor.FileManager;
import com.mobile.syslogng.monitor.R;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.view.View;
import android.widget.ListView;


@SuppressWarnings("unchecked")
public class TestGUI extends ActivityInstrumentationTestCase2{
	
	private static final String LAUNCHER_ACTIVITY_FULL_CLASSNAME = "com.mobile.syslogng.monitor.MainActivity";

	private static Class launcherActivityClass;
	static {
		try {
				launcherActivityClass = Class.forName(LAUNCHER_ACTIVITY_FULL_CLASSNAME);
		} 
		catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
		}
	}
	
	public TestGUI() throws ClassNotFoundException {
		super(launcherActivityClass);
		}
	
	public TestGUI(String name){
		super(launcherActivityClass);
		setName(name);
		}
	
	
	
	private Solo solo;
	private Activity activity;
	
	@Override
	protected void setUp() throws Exception {
		solo = new Solo(getInstrumentation(), getActivity());
		activity = getActivity();
	}
	
	public static TestSuite suite(){
		TestSuite t = new TestSuite();
		t.addTest(TestSuite.createTest(TestGUI.class, "testAppLoad"));
		t.addTest(TestSuite.createTest(TestGUI.class, "testCertificateConfiguration"));
		
		return t;
	}
	
	
	public void testAppLoad() {
			
		assertTrue(solo.waitForFragmentByTag("fragment_welcome_tag", 2000) || solo.waitForFragmentByTag("fragment_monitored_syslogng_tag", 2000));
		
		}
	
	public void testCertificateConfiguration(){
		if(solo.waitForFragmentByTag("fragment_welcome_tag")){
			solo.clickOnButton("Add Instance");
			if(solo.waitForFragmentByTag("fragment_addsyslogng_tag")){
				Log.e("FRAGMENT", "Add syslogng fragment");
				//Check whether the checkbox is disabled by performing a click.
				solo.clickOnCheckBox(0);
				if(solo.isCheckBoxChecked("Include Client Certificate")){
					assertTrue(true);
				}
				else{
					solo.clickOnButton("Import Certificate");
					if(!isEmulator()){
						assertTrue(isFileManagerAvailable() && importCertificate());
						activity.onBackPressed();
					}
				}
			}
			
		}
	}
	
	private boolean isEmulator() {
        return Build.FINGERPRINT.startsWith("generic");
    }
	
	private Boolean isFileManagerAvailable(){
		PackageManager packageManager = getActivity().getPackageManager();
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("file/*");
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.GET_ACTIVITIES);
        if(list.isEmpty()){
        	return false;
        }
        else{
        	return true;
        }
		
	}
	
	/*
	 * Please replace sourcePath with a actual path available in your device.
	 * 
	 */
	private Boolean importCertificate(){
		Boolean status;
		String sourcePath = "/storage/emulated/0/Download/certpkcs12.pfx";
		String certificateName = "sample.pfx";
		FileManager fManager = new FileManager(activity.getApplicationContext());
		fManager.createCertificateDirectory();
		status = fManager.copyFile(sourcePath, fManager.getCertificateFile(certificateName));
		return status;
	}
	
	
	@Override
	public void tearDown() throws Exception {
		solo.finishOpenedActivities();
	}
}
