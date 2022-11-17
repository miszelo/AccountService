package account.model.user;

public enum Operation {
    GRANT,
    REMOVE,
    LOCK,
    UNLOCK;

    public String withSuffix() {
        return name().toLowerCase() + "ed";
    }
}
