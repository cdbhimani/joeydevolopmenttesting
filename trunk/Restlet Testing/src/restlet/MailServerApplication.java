package restlet;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.Server;
import org.restlet.data.Protocol;
import org.restlet.routing.Router;

public class MailServerApplication extends Application {
	public static void main(String input[]) throws Exception {
		Server mailServer = new Server(Protocol.HTTP, 8111);
		mailServer.setNext(new MailServerApplication());
		mailServer.start();
	}
	
	public MailServerApplication(){
		setName("RESTful Mail Server");
		setDescription("Sample Description");
		setOwner("Testing Owner");
		setAuthor("Testing Author");
	}

	@Override
	public Restlet createInboundRoot() {
		Router router = new Router(getContext());
		router.attach("/", RootServerResource.class);
		router.attach("/accounts/", AccountsServerResource.class);
		router.attach("/accounts/{accountId}", AccountServerResource.class);
		return router;
	}

}
