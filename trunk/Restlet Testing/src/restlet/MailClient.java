package restlet;

import java.io.IOException;

import org.restlet.Client;
import org.restlet.Context;
import org.restlet.data.Protocol;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

public class MailClient {
	public static void main(String input[]) throws ResourceException, IOException{
		
		System.out.println("\n1) Set up the service client resource\n");
		Client client = new Client(new Context(),  Protocol.HTTP);
		ClientResource service = new ClientResource("http://localhost:8111");
		service.setNext(client);
		
		System.out.println("\n2) display the root resource\n");
		RootResource mailRoot = service.getChild("/", RootResource.class);
		System.out.println(mailRoot.represent());
		
		System.out.println("\n3) Display the initial list of accounts\n");
		AccountsResource mailAccounts = service.getChild("/accounts/", AccountsResource.class);
		String list = mailAccounts.represent();
		System.out.println(list==null ? "<empty>\n" : list);
		
		System.out.println("\n4) Add new accounts\n");
		mailAccounts.add("Homer Simpson");
		mailAccounts.add("Marjorine Simpson");
		mailAccounts.add("Bard Simpson");
		System.out.println("Three accounts added");
		
		System.out.println("\n5) Display the list of accounts\n");
		list = mailAccounts.represent();
		System.out.println(list==null ? "<empty>\n" : list);
		
		System.out.println("\n6) Display the second account\n");
		AccountResource mailAccount = service.getChild("/accounts/1", AccountResource.class);
		System.out.println(mailAccount.represent());
		
		System.out.println("\n7) Update the individual account and display again\n");
		mailAccount.store("Homer Jay Simpson");
		System.out.println(mailAccount.represent());
		
		System.out.println("\n8) Display the list of accounts\n");
		list = mailAccounts.represent();
		System.out.println(list==null ? "<empty>\n" : list);
		
		System.out.println("\n9) Delete the first account and display the list again\n");
		mailAccount = service.getChild("/accounts/0", AccountResource.class);
		mailAccount.remove();
		System.out.println(mailAccounts.represent());
		
		
		
	}
}
