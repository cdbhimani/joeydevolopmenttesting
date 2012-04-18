package chapter2.messageSystem;

public class MessageType {
    public static final int HiHoneyImHome = 0;
    public static final int StewReady = 1;

    public static String messageToString(int id) {
	switch (id) {
	    case HiHoneyImHome:
		return "Hi Honey I'm home!";
	    case StewReady:
		return "The Stew is ready!";
	}
	return "Invalid Message";

    }
}
