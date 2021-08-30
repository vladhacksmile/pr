public class Telegram extends Thing {

    private String content;
    private Person sender;

    public Telegram(String name, String content, Person sender) {
        super(name);
        this.content = content;
        this.sender = sender;
    }

    public void readTelegram(Person p) {
        System.out.println(p.getName() + " прочитал телеграмму от " + sender.getName() + ": \"" + getContent() + "\"");
    }

    public String getContent() {
        return content;
    }

    public Person getSender() {
        return sender;
    }
}
