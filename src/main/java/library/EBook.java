package library;

/**
 * EBook class that extends Book with additional fileSize attribute
 */
public class EBook extends Book {

    private double fileSize; // in MB
    private String format; // e.g., PDF, EPUB, MOBI

    /**
     * Constructor for EBook with all attributes
     */
    public EBook(String title, String author, String isbn, int copies, int year,
            double fileSize, String format) {
        super(title, author, isbn, copies, year);
        this.fileSize = fileSize;
        this.format = format;
    }

    /**
     * Get the file size in MB
     * 
     * @return The file size in MB
     */
    public double getFileSize() {
        return fileSize;
    }

    /**
     * Set the file size in MB
     * 
     * @param fileSize The file size in MB
     */
    public void setFileSize(double fileSize) {
        this.fileSize = fileSize;
    }

    /**
     * Get the format of the ebook
     * 
     * @return The format (PDF, EPUB, etc.)
     */
    public String getFormat() {
        return format;
    }

    /**
     * Set the format of the ebook
     * 
     * @param format The format to set
     */
    public void setFormat(String format) {
        this.format = format;
    }

    @Override
    public void display() {
        System.out.println("Título: " + getTitle()
                + ", Autor: " + getAuthor()
                + ", ISBN: " + getIsbn()
                + ", Año: " + getYear()
                + ", Copias disponibles: " + getCopies()
                + ", Tamaño: " + fileSize + " MB"
                + ", Formato: " + format);
    }

    @Override
    public String toString() {
        return "EBook - " + super.toString()
                + ", File Size: " + fileSize + " MB"
                + ", Format: " + format;
    }
}