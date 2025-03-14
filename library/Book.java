package library;

public class Book {
    private String title;
    private String author;
    private String isbn;
    private int copies;

    public Book(String title, String author, String isbn, int copies) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.copies = copies;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String newTitle) {
        this.title = newTitle;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String newAuthor) {
        this.author = newAuthor;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String newIsbn) {
        this.isbn = newIsbn;
    }

    public int getCopies() {
        return copies;
    }

    public void setCopies(int newCopies) {
        this.copies = newCopies;
    }

    public void display() {
        System.out.println("Título: " + title 
            + ", Autor: " + author 
            + ", ISBN: " + isbn 
            + ", Copias disponibles: " + copies);
    }
}
