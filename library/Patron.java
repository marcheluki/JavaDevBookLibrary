package library;

public class Patron {
    private String name;
    private int id;
    private String contact;

    public Patron(String name, int id, String contact) {
        this.name = name;
        this.id = id;
        this.contact = contact;
    }

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public int getId() {
        return id;
    }

    public void setId(int newId) {
        this.id = newId;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String newContact) {
        this.contact = newContact;
    }

    public void display() {
        System.out.println("ID: " + id 
            + ", Nombre: " + name 
            + ", Contacto: " + contact);
    }
}
