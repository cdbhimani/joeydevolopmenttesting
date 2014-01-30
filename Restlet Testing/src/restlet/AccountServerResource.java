package restlet;

import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

public class AccountServerResource extends ServerResource implements AccountResource {
	private int accountId;

	@Override
	protected void doInit() throws ResourceException {
		this.accountId = Integer.parseInt(getAttribute("accountId"));
	}

	public String represent() {
		return AccountsServerResource.getAccounts().get(this.accountId);
	}

	public void store(String account) {
		AccountsServerResource.getAccounts().set(this.accountId, account);
	}

	public void remove() {
		AccountsServerResource.getAccounts().remove(this.accountId);
	}

}
