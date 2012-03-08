package chapter2.messageSystem;

public class Telegram implements Comparable<Telegram> {
    public int sender;
    public int reciever;
    
    public int message;
    
    public Long dispatchTime;
    
    public Object ExtraInfo;

    public Telegram(){
	dispatchTime = System.currentTimeMillis();
    }
    
    public int getSender() {
        return sender;
    }

    public void setSender(int sender) {
        this.sender = sender;
    }

    public int getReciever() {
        return reciever;
    }

    public void setReciever(int reciever) {
        this.reciever = reciever;
    }

    public int getMessage() {
        return message;
    }

    public void setMessage(int message) {
        this.message = message;
    }

    public Long getDispatchTime() {
        return dispatchTime;
    }

    public void setDispatchTime(Long dispatchTime) {
        this.dispatchTime = dispatchTime;
    }

    public Object getExtraInfo() {
        return ExtraInfo;
    }

    public void setExtraInfo(Object extraInfo) {
        ExtraInfo = extraInfo;
    }

    @Override
    public int compareTo(Telegram o) {
	return dispatchTime.compareTo(o.dispatchTime);
    }
}
