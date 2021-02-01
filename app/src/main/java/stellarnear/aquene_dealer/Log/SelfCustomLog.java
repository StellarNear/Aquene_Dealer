package stellarnear.aquene_dealer.Log;

public abstract class SelfCustomLog {
    public transient CustomLog log = new CustomLog(this.getClass());
}
