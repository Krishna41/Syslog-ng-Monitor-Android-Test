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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;


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
		//t.addTest(TestSuite.createTest(TestGUI.class, "testAppLoad"));
		//t.addTest(TestSuite.createTest(TestGUI.class, "testCertificateConfiguration"));
		//t.addTest(TestSuite.createTest(TestGUI.class, "testAddSyslogngFields"));
		//t.addTest(TestSuite.createTest(TestGUI.class, "testAddSyslogngs"));
		t.addTest(TestSuite.createTest(TestGUI.class, "testEditSyslogng"));
		
		return t;
	}
	
	
	public void testAppLoad() {
		assertTrue(solo.waitForFragmentByTag("fragment_welcome_tag", 2000) || solo.waitForFragmentByTag("fragment_monitored_syslogng_tag", 2000));
	}
	
	public void testCertificateConfiguration(){
		if(solo.waitForFragmentByTag("fragment_welcome_tag")){
			solo.clickOnButton("Add Syslog-ng");
			if(solo.waitForFragmentByTag("fragment_addsyslogng_tag")){
				//Check whether the checkbox is disabled by performing a click.
				solo.clickOnCheckBox(0);
				if(solo.isCheckBoxChecked("Include Client Certificate")){
					assertTrue(true);
				}
				else{
					solo.clickOnButton("Import Certificate");
					if(!isEmulator()){
						assertTrue(isFileManagerAvailable() && importCertificate());
					}
				}
			}
			
		}
		else{
			solo.clickOnActionBarHomeButton();
			solo.clickOnMenuItem("Add/Update Syslog-ng to monitor");
			if(solo.waitForFragmentByTag("fragment_addsyslogng_tag")){
				//Check whether the checkbox is disabled by performing a click.
				solo.clickOnCheckBox(0);
				if(solo.isCheckBoxChecked("Include Client Certificate")){
					assertTrue(true);
				}
				else{
					solo.clickOnButton("Import Certificate");
					if(!isEmulator()){
						assertTrue(isFileManagerAvailable() && importCertificate());
					}
				}
			}
		}
	}
	
	public void testAddSyslogngFields(){
		solo.clickOnActionBarHomeButton();
		solo.clickOnMenuItem("Add/Update Syslog-ng to monitor");
		solo.clickOnButton("Add Syslog-ng");
		assertTrue(solo.waitForText("Enter valid host/port details"));
		solo.typeText((EditText)activity.findViewById(R.id.ai_et_syslogng_name), "Syslog-ng Name");
		solo.typeText((EditText)activity.findViewById(R.id.ai_et_syslogng_input), "Syslog-ng Hostname");
		solo.typeText((EditText)activity.findViewById(R.id.ai_et_port_input), "Syslog-ng Portnumber");
		solo.clickOnButton("Add Syslog-ng");
		assertTrue(solo.waitForText("Port entered is not a Number"));
		solo.clickOnCheckBox(0);
		assertTrue(solo.isCheckBoxChecked("Include Client Certificate"));
	}
	
	
	
	public void testAddSyslogngs(){
		solo.clickOnActionBarHomeButton();
		solo.clickOnMenuItem("Add/Update Syslog-ng to monitor");
		//Add a Sample Instance to test edit and delete
		solo.typeText((EditText)activity.findViewById(R.id.ai_et_syslogng_name), "Sample Syslogng");
		solo.typeText((EditText)activity.findViewById(R.id.ai_et_syslogng_input), "hostname");
		solo.typeText((EditText)activity.findViewById(R.id.ai_et_port_input), "1234");
		solo.sleep(500);
		solo.clickOnButton("Add Syslog-ng");
		assertTrue(solo.waitForText("Syslog-ng successfully added into Database"));
		
		//Add a Valid Syslog-ng without Certificate
		solo.typeText((EditText)activity.findViewById(R.id.ai_et_syslogng_name), "Active Syslogng/No Certificate");
		solo.typeText((EditText)activity.findViewById(R.id.ai_et_syslogng_input), "ec2-54-69-101-145.us-west-2.compute.amazonaws.com");
		solo.typeText((EditText)activity.findViewById(R.id.ai_et_port_input), "2121");
		solo.sleep(500);
		solo.clickOnButton("Add Syslog-ng");
		assertTrue(solo.waitForText("Syslog-ng successfully added into Database"));
		
		//Add a valid Syslog-ng with Certificate with correct password
		if(!isEmulator()){
			solo.typeText((EditText)activity.findViewById(R.id.ai_et_syslogng_name), "Active Syslogng/Certificate/Password");
			solo.typeText((EditText)activity.findViewById(R.id.ai_et_syslogng_input), "ec2-54-69-101-145.us-west-2.compute.amazonaws.com");
			solo.typeText((EditText)activity.findViewById(R.id.ai_et_port_input), "2121");
			
			if(!solo.isCheckBoxChecked("Include Client Certificate")){
				solo.clickOnCheckBox(0);
				Spinner spinnerView = solo.getView(Spinner.class, 0);
				solo.clickOnView(spinnerView);
				solo.clickOnView(solo.getText("sample.pfx"));
			}
			solo.typeText(solo.getEditText("Enter password"), "krrisss");
			solo.sleep(500);
			solo.clickOnButton("Add Syslog-ng");
			assertTrue(solo.waitForText("Syslog-ng successfully added into Database"));
			
		}
		
		//Add a valid Syslog-ng with Certificate and wrong password
		if(!isEmulator()){
			solo.typeText((EditText)activity.findViewById(R.id.ai_et_syslogng_name), "Valid Syslogng/Certificate/WrongPassword");
			solo.typeText((EditText)activity.findViewById(R.id.ai_et_syslogng_input), "ec2-54-69-101-145.us-west-2.compute.amazonaws.com");
			solo.typeText((EditText)activity.findViewById(R.id.ai_et_port_input), "2121");
			if(!solo.isCheckBoxChecked("Include Client Certificate")){
				solo.clickOnCheckBox(0);
				Spinner spinnerView = solo.getView(Spinner.class, 0);
				solo.clickOnView(spinnerView);
				solo.clickOnView(solo.getText("sample.pfx"));
			}
			solo.typeText(solo.getEditText("Enter password"), "wrong");
			solo.sleep(500);
			solo.clickOnButton("Add Syslog-ng");
			assertTrue(solo.waitForText("Syslog-ng successfully added into Database"));
			
		}
		
		//Add a Inactive Syslog-ng
		solo.typeText((EditText)activity.findViewById(R.id.ai_et_syslogng_name), "Inactive Syslogng");
		solo.typeText((EditText)activity.findViewById(R.id.ai_et_syslogng_input), "212");
		solo.typeText((EditText)activity.findViewById(R.id.ai_et_port_input), "ec2-54-69-101-145.us-west-2.compute.amazonaws.com");
		
		if(solo.isCheckBoxChecked("Include Client Certificate")){
			solo.clickOnCheckBox(0);
		}
		solo.sleep(500);
		solo.clickOnButton("Add Syslog-ng");
		assertTrue(solo.waitForText("Syslog-ng successfully added into Database"));
	}
	
	public void testEditSyslogng(){
		solo.waitForFragmentByTag("fragment_monitored_syslogng_tag", 1000);
		ListView listView = (ListView) activity.findViewById(R.id.listview_view_instance);
		assertTrue(listView.getCount() >= 5);
		
		assertTrue(solo.waitForText("Sample Syslogng"));
		solo.clickLongInList(1);
		solo.clickInList(2);
		solo.clickOnView(activity.findViewById(R.id.edit_list_item));
		assertTrue(solo.waitForText("Select any one Syslog-ng for editing"));
		solo.clickInList(2);
		solo.clickOnView(activity.findViewById(R.id.edit_list_item));
		assertTrue(solo.waitForFragmentByTag("fragment_addsyslogng_tag"));
		solo.clearEditText((EditText)activity.findViewById(R.id.ai_et_syslogng_name));
		solo.typeText((EditText)activity.findViewById(R.id.ai_et_syslogng_name), "Edited Syslogng ");
		solo.clickOnButton("Add Syslog-ng");
		assertTrue(solo.waitForText("Syslog-ng successfully added into Database"));
		solo.clickOnActionBarHomeButton();
		solo.clickOnMenuItem("Monitored Syslog-ng\\(s\\)");
		assertTrue(solo.waitForText("Edited Syslogng"));
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
