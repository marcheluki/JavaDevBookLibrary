package library;

public class Book {
    private String title;
    private String author;
    private String isbn;
    private int copies;
    private int year;

    public Book(String title, String author, String isbn, int copies, int year) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.copies = copies;
        this.year = year;
    }

    public Book(String title, String author, String isbn, int copies) {
        this(title, author, isbn, copies, 0);
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

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void display() {
        System.out.println("Título: " + title
                + ", Autor: " + author
                + ", ISBN: " + isbn
                + ", Año: " + year
                + ", Copias disponibles: " + copies);
    }

    @Override
    public String toString() {
        return "Title: " + title
                + ", Author: " + author
                + ", ISBN: " + isbn
                + ", Year: " + year
                + ", Available copies: " + copies;
    }
}
