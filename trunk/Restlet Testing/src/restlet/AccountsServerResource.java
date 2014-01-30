package restlet;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.restlet.resource.ServerResource;

public class AccountsServerResource extends ServerResource implements AccountsResource {

	private static final List<String> accounts = new CopyOnWriteArrayList<>();

	public static List<String> getAccounts() {
		return accounts;
	}

	public String represent() {
		StringBuilder result = new StringBuilder();

		for (String account : getAccounts()) {
			result.append(account == null ? "" : account).append("\n");
		}
		return result.toString();
	}

	public String add(String account) {
		getAccounts().add(account);
		return Integer.toString(getAccounts().indexOf(account));
	}

}
