package com.mobile.syslogng.monitor.test;


import com.robotium.solo.Solo;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.ListView;


@SuppressWarnings("unchecked")
public class SyslogngTest extends ActivityInstrumentationTestCase2 {
	
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
	
	public SyslogngTest() throws ClassNotFoundException {
		super(launcherActivityClass);
		}
	
	private Solo solo;
	
	@Override
	protected void setUp() throws Exception {
		solo = new Solo(getInstrumentation(), getActivity());
	}
	
	public void testOne() {
		
			solo.waitForFragmentByTag("Monitored Syslog-ng(s)");
			solo.clickInList(1);
			assertTrue(solo.waitForDialogToOpen());
		
		}
	
	
	@Override
	public void tearDown() throws Exception {
		solo.finishOpenedActivities();
	}
}
