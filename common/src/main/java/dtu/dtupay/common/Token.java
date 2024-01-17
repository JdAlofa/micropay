package dtu.dtupay.common;

public class Token {
    private String id; // Token identifier
    private Boolean used;

    public Token() {
        this.used = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(Boolean used) {
        this.used = used;
    }
}