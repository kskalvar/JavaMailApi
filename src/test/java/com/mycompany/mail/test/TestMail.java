package com.mycompany.mail.test;

import com.mycompany.mail.InboxReader;
import junit.framework.TestCase;



public class TestMail extends TestCase {
	
	private InboxReader inboxReader = null;
	
	public void setUp() throws Exception {
		
		
		inboxReader = new InboxReader();

	}
	
	public void testProcessInbox() throws Exception {
		
		inboxReader.processInbox();
		assertEquals("processInbox", 0, 0);
	
	}

}
