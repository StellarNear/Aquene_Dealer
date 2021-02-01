package stellarnear.aquene_companion.Log;

public abstract class SelfCustomLog {
    public transient CustomLog log = new CustomLog(this.getClass());
}
