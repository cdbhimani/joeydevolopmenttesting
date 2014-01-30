package restlet;

import org.restlet.resource.ServerResource;

public class RootServerResource extends ServerResource implements RootResource {

	public String represent() {
		return "Welcome to the " + getApplication().getName() + "!";
	}
}
