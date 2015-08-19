package com.mobile.syslogng.monitor.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class TestSyslogngMonitor extends TestSuite{
	
	public static Test suite(){
		TestSuite testSuite = new TestSuite();
		
		/*
		 * Most or All of the tests are based on previous results. 
		 * Please run this Test Suite instead of running individual testcase.
		 *
		 * CAUTION
		 * 
		 * Tests clear application data before starting and after ending.
		 * It will result in loss of data stored previously.
		 * 
		 * APP DATA CLEARING - USAGE
		 * 
		 * Added in TestFileManager - in First setUp()
		 * Added in TestGUI - in Last tearDown() - Note the last tearDown is 
		 * identified by number of testcases present. Please modify the value
		 * while adding or removing the testcases.   
		 * 
		 */
		
		testSuite.addTest(TestSuite.createTest(TestFileManager.class, "testGetCertificateDirectory"));
		testSuite.addTest(TestSuite.createTest(TestFileManager.class, "testGetCertificateFile"));
		testSuite.addTest(TestSuite.createTest(TestFileManager.class, "testGetCertificateFileFailedCase"));
		testSuite.addTest(TestSuite.createTest(TestFileManager.class, "testImportCertificate"));
		
		testSuite.addTest(TestSuite.createTest(TestSQLiteManager.class, "testInsertSyslogng"));
		testSuite.addTest(TestSuite.createTest(TestSQLiteManager.class, "testUpdateSyslogng"));
		testSuite.addTest(TestSuite.createTest(TestSQLiteManager.class, "testGetSyslogng"));
		testSuite.addTest(TestSuite.createTest(TestSQLiteManager.class, "testDeleteSyslogng"));
		
		
		testSuite.addTest(TestSuite.createTest(TestGUI.class, "testAppLoad"));
		testSuite.addTest(TestSuite.createTest(TestGUI.class, "testCertificateConfiguration"));
		testSuite.addTest(TestSuite.createTest(TestGUI.class, "testAddSyslogngFields"));
		testSuite.addTest(TestSuite.createTest(TestGUI.class, "testAddSyslogngs"));
		testSuite.addTest(TestSuite.createTest(TestGUI.class, "testEditSyslogng"));
		testSuite.addTest(TestSuite.createTest(TestGUI.class, "testDeleteSyslogng")); 
		testSuite.addTest(TestSuite.createTest(TestGUI.class, "testCommandExecutionWithoutCertificate"));
		testSuite.addTest(TestSuite.createTest(TestGUI.class, "testCommandExecutionWithCertificate"));
		testSuite.addTest(TestSuite.createTest(TestGUI.class, "testCommandExecutionWithWrongPassword"));
		testSuite.addTest(TestSuite.createTest(TestGUI.class, "testCommandExecutionWithInactiveSyslogng"));
		
		
		return testSuite;
	}

	
}
