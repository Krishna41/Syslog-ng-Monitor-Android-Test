package com.mobile.syslogng.monitor.test;


import java.util.ArrayList;

import com.mobile.syslogng.monitor.SQLiteManager;
import com.mobile.syslogng.monitor.Syslogng;

import android.content.Context;
import android.test.AndroidTestCase;


public class TestSQLiteManager extends AndroidTestCase {
	
	public TestSQLiteManager(){
		
	}
	public TestSQLiteManager(String name){
		setName(name);
	}
	
	private Context context;
	private SQLiteManager sqliteManager;
	
	
	
	@Override
	protected void setUp() throws Exception {
		context = getContext();
		sqliteManager	= new SQLiteManager(context);
	}
	
	/*
	 * All Insertions are checked in GUI Testing
	 * Failure Cases are handled here
	 * 
	 */
	
	public void testInsertSyslogng(){
		assertTrue(!sqliteManager.insertSyslogng(null));
	}
	
	public void testUpdateSyslogng(){
		assertTrue(!sqliteManager.updateSyslogng(null));
	}
	
	public void testGetSyslogng(){
		ArrayList<Syslogng> list = sqliteManager.getSyslogngs();
		assertNotNull(list);
	}
	
	public void testDeleteSyslogng(){
		assertTrue(!sqliteManager.deleteSyslogngs(null));
	}
	
	@Override
	public void tearDown() throws Exception {
		
	}
}
