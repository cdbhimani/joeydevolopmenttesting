package restlet;

import org.restlet.Component;
import org.restlet.data.Protocol;

public class MailServerCompoent extends Component {
	public static void main(String input[]) throws Exception{
		new MailServerCompoent().start();
	}
	
	public MailServerCompoent(){
		setName("Restful Mail Server Compoent");
		setDescription("Example Compoent");
		setOwner("Testing");
		setAuthor("Author");
		
		getServers().add(Protocol.HTTP, 8111);
		getDefaultHost().attachDefault(new MailServerApplication());
	}
}
