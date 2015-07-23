package com.mycompany.mail;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;

public class InboxReader {

	private Store store = null;

	public InboxReader() throws MessagingException, IOException {
		
		String host = null;
		String username = null;
		String password = null;
		String debug = null;

		Properties props = System.getProperties();
		Session session = Session.getDefaultInstance(props, null);

		// load a properties file
		Properties config = new Properties();
		InputStream input = InboxReader.class.getClassLoader().getResourceAsStream("config.properties");
		
		config.load(input);

		host = config.getProperty("host");
		username = config.getProperty("username");
		password = config.getProperty("password");
		debug = config.getProperty("debug");

		if (debug.compareToIgnoreCase("yes") == 0) {
			props.put("mail.debug", "true");
			session.setDebug(true);
		}

		store = session.getStore("imaps");
		store.connect(host, username, password);

	}

	public void processInbox() throws MessagingException, IOException, InterruptedException {

		Folder folder = store.getFolder("Inbox");
		folder.open(Folder.READ_WRITE);

		Message[] messages = folder.getMessages();

		for (int messageCount = 0; messageCount < messages.length; messageCount++) {
			Message message = messages[messageCount];

			getMessage(message);

			message.setFlag(Flags.Flag.DELETED, true);

		}
		folder.close(true);

	}

	private void getMessage(Message aMessage) throws IOException, MessagingException {

		System.out.println("Sender: " + aMessage.getFrom()[0].toString());
		System.out.println("Subject: " + aMessage.getSubject());

		String contentType = aMessage.getContentType();

		if (contentType.contains("TEXT/PLAIN")) {
			System.out.println("Content Type: " + aMessage.getContentType());
			System.out.println("Content: " + aMessage.getContent().toString());
		} else {
			Multipart multiPart = (Multipart) aMessage.getContent();
			int numberOfParts = multiPart.getCount();

			for (int partCount = 0; partCount < numberOfParts; partCount++) {
				BodyPart part = multiPart.getBodyPart(partCount);

				if (part.getContentType().contains("TEXT/PLAIN")) {
					System.out.println("Content Type: " + part.getContentType());
					System.out.println("Content: " + part.getContent().toString());
				}
			}
		}

	}

	public static void main(String args[]) throws IOException,
			MessagingException, InterruptedException {

		System.out.println("InboxReader: start");

		InboxReader inboxReader = new InboxReader();
		inboxReader.processInbox();

		System.out.println("InboxReader: end");

	}

}
