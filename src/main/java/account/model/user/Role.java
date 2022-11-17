package account.model.user;

public enum Role {
    ROLE_USER,
    ROLE_ACCOUNTANT,
    ROLE_ADMINISTRATOR,
    ROLE_AUDITOR;

    public String withoutROLE_() {
        return name().substring(5);
    }
}
