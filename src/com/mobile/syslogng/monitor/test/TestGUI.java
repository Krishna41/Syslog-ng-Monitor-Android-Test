package com.mobile.syslogng.monitor.test;


import java.util.HashMap;
import java.util.List;

import com.robotium.solo.Solo;
import com.mobile.syslogng.monitor.R;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.test.ActivityInstrumentationTestCase2;
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
	private static Activity activity;
	private static SyslogngApplicationData data;
	private static Integer testCount = 0;
	
	@Override
	protected void setUp() throws Exception {
		solo = new Solo(getInstrumentation(), getActivity());
		activity = getActivity();
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
						assertTrue(isFileManagerAvailable());
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
						assertTrue(isFileManagerAvailable());
					}
				}
			}
		}
	}
	
	public void testAddSyslogngFields(){
		solo.clickOnActionBarHomeButton();
		solo.clickOnMenuItem("Add/Update Syslog-ng to monitor");
		solo.waitForFragmentByTag("fragment_addsyslogng_tag");
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
		solo.waitForFragmentByTag("fragment_addsyslogng_tag");
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
				solo.clickOnView(solo.getText("SampleCertificate.pfx"));
			}
			solo.typeText(solo.getEditText("Enter password"), "samplepassword");
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
				solo.clickOnView(solo.getText("SampleCertificate.pfx"));
			}
			solo.typeText(solo.getEditText("Enter password"), "wrong");
			solo.sleep(500);
			solo.clickOnButton("Add Syslog-ng");
			assertTrue(solo.waitForText("Syslog-ng successfully added into Database"));
			
		}
		
		//Add a Inactive Syslog-ng
		solo.typeText((EditText)activity.findViewById(R.id.ai_et_syslogng_name), "Inactive Syslogng");
		solo.typeText((EditText)activity.findViewById(R.id.ai_et_syslogng_input), "ec2-54-69-101-145.us-west-2.compute.amazonaws.com");
		solo.typeText((EditText)activity.findViewById(R.id.ai_et_port_input), "212");
		
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
		Integer sampleSyslogngPosition = findPositionInList("Sample Syslogng");
		Integer activeSyslogngPosition = findPositionInList("Active Syslogng/No Certificate");
		solo.clickLongInList(sampleSyslogngPosition);
		solo.clickInList(activeSyslogngPosition);
		solo.clickOnView(activity.findViewById(R.id.edit_list_item));
		assertTrue(solo.waitForText("Select any one Syslog-ng for editing"));
		solo.clickInList(activeSyslogngPosition);
		solo.clickOnView(activity.findViewById(R.id.edit_list_item));
		assertTrue(solo.waitForFragmentByTag("fragment_addsyslogng_tag"));
		solo.clearEditText((EditText)activity.findViewById(R.id.ai_et_syslogng_name));
		solo.typeText((EditText)activity.findViewById(R.id.ai_et_syslogng_name), "Edited Syslogng");
		solo.clickOnButton("Add Syslog-ng");
		assertTrue(solo.waitForText("Syslog-ng successfully added into Database"));
		solo.clickOnActionBarHomeButton();
		solo.clickOnMenuItem("Monitored Syslog-ng\\(s\\)");
		assertTrue(solo.waitForText("Edited Syslogng"));
	}
	
	public void testDeleteSyslogng(){
		solo.waitForFragmentByTag("fragment_monitored_syslogng_tag", 1000);
		ListView listView = (ListView)activity.findViewById(R.id.listview_view_instance);
		Integer syslogngCount = listView.getCount();
		Integer editedSyslogngPosition = findPositionInList("Edited Syslogng");
		solo.clickLongInList(editedSyslogngPosition);
		solo.clickOnView(activity.findViewById(R.id.delete_list_item));
		assertTrue(solo.waitForText("Deleted Successfully"));
		assertTrue(syslogngCount > listView.getCount());
	}
	
	public void testCommandExecutionWithoutCertificate(){
		Integer position = findPositionInList("Active Syslogng/No Certificate");

		//Test for command - stats
		
		solo.clickInList(position);
		solo.waitForDialogToOpen(500);
		solo.clickOnText("stats");
		solo.clickOnButton("OK");
		solo.waitForDialogToOpen();
		assertTrue(solo.searchText("Results"));
		solo.clickOnButton("OK");
		
		//Test for command - is_alive
		
		solo.clickInList(position);
		solo.waitForDialogToOpen(500);
		solo.clickOnText("is_alive");
		solo.clickOnButton("OK");
		solo.waitForDialogToOpen();
		assertTrue(solo.searchText("Results"));
		solo.clickOnButton("OK");
		
		//Test for command - show_config
		
		solo.clickInList(position);
		solo.waitForDialogToOpen(500);
		solo.clickOnText("show_config");
		solo.clickOnButton("OK");
		solo.waitForDialogToOpen();
		assertTrue(solo.searchText("Results"));
		solo.clickOnButton("OK");
		
		
	}
	
	public void testCommandExecutionWithCertificate(){
		Integer position = findPositionInList("Active Syslogng/Certificate/Password");

		//Test for command - stats
		
		solo.clickInList(position);
		solo.waitForDialogToOpen(500);
		solo.clickOnText("stats");
		solo.clickOnButton("OK");
		solo.waitForDialogToOpen();
		assertTrue(solo.searchText("Results"));
		solo.clickOnButton("OK");
		
		//Test for command - is_alive
		
		solo.clickInList(position);
		solo.waitForDialogToOpen(500);
		solo.clickOnText("is_alive");
		solo.clickOnButton("OK");
		solo.waitForDialogToOpen();
		assertTrue(solo.searchText("Results"));
		solo.clickOnButton("OK");
		
		//Test for command - show_config
		
		solo.clickInList(position);
		solo.waitForDialogToOpen(500);
		solo.clickOnText("show_config");
		solo.clickOnButton("OK");
		solo.waitForDialogToOpen();
		assertTrue(solo.searchText("Results"));
		solo.clickOnButton("OK");
		
		
	}
	
	public void testCommandExecutionWithWrongPassword(){
		Integer position = findPositionInList("Valid Syslogng/Certificate/WrongPassword");

		//Test for command - stats
		
		solo.clickInList(position);
		solo.waitForDialogToOpen(500);
		solo.clickOnText("stats");
		solo.clickOnButton("OK");
		solo.waitForDialogToOpen();
		assertTrue(solo.searchText("Error"));
		assertTrue(solo.searchText("PKCS12 key store mac invalid - wrong password or corrupted file."));
		solo.clickOnButton("OK");
		
		//Test for command - is_alive
		
		solo.clickInList(position);
		solo.waitForDialogToOpen(500);
		solo.clickOnText("is_alive");
		solo.clickOnButton("OK");
		solo.waitForDialogToOpen();
		assertTrue(solo.searchText("Error"));
		assertTrue(solo.searchText("PKCS12 key store mac invalid - wrong password or corrupted file."));
		solo.clickOnButton("OK");
		
		//Test for command - show_config
		
		solo.clickInList(position);
		solo.waitForDialogToOpen(500);
		solo.clickOnText("show_config");
		solo.clickOnButton("OK");
		solo.waitForDialogToOpen();
		assertTrue(solo.searchText("Error"));
		assertTrue(solo.searchText("PKCS12 key store mac invalid - wrong password or corrupted file."));
		solo.clickOnButton("OK");
		
		
	}
	
	public void testCommandExecutionWithInactiveSyslogng(){
		Integer position = findPositionInList("Inactive Syslogng");

		//Test for command - stats
		
		solo.clickInList(position);
		solo.waitForDialogToOpen(500);
		solo.clickOnText("stats");
		solo.clickOnButton("OK");
		solo.waitForDialogToOpen();
		assertTrue(solo.searchText("Error"));
		assertTrue(solo.searchText("ECONNREFUSED"));
		solo.clickOnButton("OK");
		
		//Test for command - is_alive
		
		solo.clickInList(position);
		solo.waitForDialogToOpen(500);
		solo.clickOnText("is_alive");
		solo.clickOnButton("OK");
		solo.waitForDialogToOpen();
		assertTrue(solo.searchText("Error"));
		assertTrue(solo.searchText("ECONNREFUSED"));
		solo.clickOnButton("OK");
		
		//Test for command - show_config
		
		solo.clickInList(position);
		solo.waitForDialogToOpen(500);
		solo.clickOnText("show_config");
		solo.clickOnButton("OK");
		solo.waitForDialogToOpen();
		assertTrue(solo.searchText("Error"));
		assertTrue(solo.searchText("ECONNREFUSED"));
		solo.clickOnButton("OK");
		
		
	}
	
	private Integer findPositionInList(String syslogngName){
		Integer position = -1;
		ListView listView = (ListView)activity.findViewById(R.id.listview_view_instance);
		for(Integer i = 0; i<= listView.getCount(); i++){
			HashMap<String,Object> listItem = (HashMap<String,Object>) listView.getItemAtPosition(i);
			if(listItem.get("SyslogngName").toString().equals(syslogngName)){
				position = i+1;
				break;
			}
			
		}
		return position;
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
	
	
	
	@Override
	public void tearDown() throws Exception {
		solo.finishOpenedActivities();
		testCount++;
		if(testCount.equals(10)){
			data = new SyslogngApplicationData(activity.getApplicationContext());
			data.remove();
		}
	}
}
