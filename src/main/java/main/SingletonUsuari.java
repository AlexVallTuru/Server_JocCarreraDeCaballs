package main;

public class SingletonUsuari {
    
    private String mail;
    private String nick;
    
    private static SingletonUsuari instance;
    
    private SingletonUsuari(String mail, String nick) {
        this.mail = mail;
        this.nick = nick;
    }
    
    public static SingletonUsuari getInstance(String mail, String nick) {
        if (instance == null) {
            instance = new SingletonUsuari(mail, nick);
        }
        return instance;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }
}