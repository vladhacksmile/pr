import java.io.Serializable;

public class Command implements Serializable {

    private User user;
    private String name;
    private String[] args;
    private Organization organization = null;
    private RemoveByElement removeByElement;
    private Update update;

    public Command(User user, String[] args, Object object) {
        if(object instanceof Organization) organization = (Organization) object;
        if(object instanceof Update) update = (Update) object;
        if(object instanceof RemoveByElement) removeByElement = (RemoveByElement) object;
        this.args = args;
        this.name = args[0];
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public String toString() {
        return String.join(", ", args);
    }

    public Update getUpdate() {
        return update;
    }

    public RemoveByElement getRemoveByElement() {
        return removeByElement;
    }

    public User getUser() {
        return user;
    }
}