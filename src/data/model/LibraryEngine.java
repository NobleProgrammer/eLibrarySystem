/*
    In this class, all the business logic is implemented here.
*/
package data.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Scanner;

public class LibraryEngine {
    private ArrayList<Book> storeBooks;
    private ArrayList<User> users;
    private ArrayList<String> categories;
    private File catFile;
    private PrintWriter writeCat;
    //------------------------------------
    File usersFile;
    //------------------------------------
    File booksFile;
    //------------------------------------
        
    
    public LibraryEngine() throws FileNotFoundException, IOException, ClassNotFoundException{
        storeBooks = new ArrayList<>();
        users = new ArrayList<>();
        categories = new ArrayList<>();
        //--------------------------------------------------
        usersFile = new File("Users.ser");
        booksFile = new File("Books.ser");
        //--------------------------------------------------
        catFile = new File("Categories.txt");

        //Call Fetch methods
        fetchUser();
        fetchBook();
        fetchCat();
    }
    
    /**
     * Add Book to book list.
     * @param book
     * @return false if book is already exist.
     */
    
    public boolean addStoreBook(Book book) throws IOException, ClassNotFoundException{
        //No Duplicates.
        for (int i = 0; i < storeBooks.size(); i++) {
            if (storeBooks.get(i).getName().equals(book.getName()) && storeBooks.get(i).getAuthor().equals(book.getAuthor())) {
                return false;
            }
        }
        storeBooks.add(book);
        if (booksFile.exists()) {
            FileInputStream fis = new FileInputStream(booksFile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            ois.readObject();//Fetch it
            FileOutputStream fos = new FileOutputStream(booksFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(storeBooks); //Return it back again like nothing happened.
        }
        else {
            FileOutputStream fos = new FileOutputStream(booksFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(storeBooks);
        }
        return true;
    }
    /**
     * Remove book by ID.
     * @param id
     * @return null if the book doesn't exist.
     */
    public Book removeBook(int id) throws IOException, ClassNotFoundException{
        int index = -1;
        for (int i = 0; i < storeBooks.size(); i++) {
            if (storeBooks.get(i).getBookID() == id) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            Book book = storeBooks.remove(index);
            FileInputStream fis = new FileInputStream(booksFile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            ois.readObject();//Fetch it
            FileOutputStream fos = new FileOutputStream(booksFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(storeBooks); //Return it back again like nothing happened.
            return book;
        }
        else {
            //Book is not found.
            return null;
        }
    }
    /**
     * Remove book by Name.
     * @param name
     * @return null if the book doesn't exist.
     */
    public Book removeBook(String name) throws IOException, ClassNotFoundException{
        int index = -1;
        for (int i = 0; i < storeBooks.size(); i++) {
            if (storeBooks.get(i).getName().equals(name)) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            Book book = storeBooks.remove(index);
            FileInputStream fis = new FileInputStream(booksFile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            ois.readObject();//Fetch it
            FileOutputStream fos = new FileOutputStream(booksFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(storeBooks); //Return it back again like nothing happened.
            return book;
        }
        else {
            //Book is not found.
            return null;
        }
    }
    /**
     * add user to users list
     * @param user must be unique
     * @return false if username matches a username in the list.
     */
    private boolean addUser(User user) throws IOException, ClassNotFoundException{
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(user.getUsername())) {
                return false;
            }
        }
        users.add(user);
        if (usersFile.exists()) {
            FileInputStream fis = new FileInputStream(usersFile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            ois.readObject();//Fetch it
            FileOutputStream fos = new FileOutputStream(usersFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(users); //Return it back again like nothing happened.
        }
        else {
            FileOutputStream fos = new FileOutputStream(usersFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(users);
        }
        return true;
    }
    /**
     * Add Category to the category list.
     * @param Cat
     * @return false if the category already exists.
     */
    public boolean addCategory(String Cat) throws IOException{
        if (categories.contains(Cat)) {
            return false;
        }
        writeCat = new PrintWriter(new FileWriter(catFile , true));
        categories.add(Cat);
        
        writeCat.println(Cat);
        writeCat.close();
        return true;
    }
    
    /**
     * Sign up an user into the system
     * @param username
     * @param password
     * @param isAdmin
     * @return false if the user already exists.
     */
    public boolean signup(String username,String password,boolean isAdmin) throws IOException, ClassNotFoundException{
        return addUser(new User(username,password,isAdmin));
    }
    /**
     * Login in the system
     * @param username
     * @param password
     * @return the user object if login succeeds, else it will return null.
     */
    public User login(String username,String password) throws IOException, ClassNotFoundException{
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(username) && users.get(i).getPassword().equals(password)) {
                users.get(i).fetchBoughtBook();
                users.get(i).fetchRentedBook();
                return users.get(i);
            }
        }
        return null;
    }

    public ArrayList<Book> getStoreBooks() {
        return storeBooks;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }
    
    
    public Book buyBook(int id , User user) throws IOException, ClassNotFoundException{
        Book book = removeBook(id);
        if (book != null) {
            user.addBoughtBook(book);
            return book;
        }
        else 
            return null;
    }
    
    public Book rentBook(int id , User user) throws IOException, ClassNotFoundException{
        Book book = removeBook(id);
        if (book != null) {
            user.addRentedBook(book);
            return book;
        }
        else 
            return null;
    }
    
    private void fetchUser() throws IOException, ClassNotFoundException{
        if (usersFile.exists()) {
            FileInputStream fis = new FileInputStream(usersFile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            ArrayList<User> temp = (ArrayList<User>)ois.readObject();//Fetch it
            users.addAll(temp);//Add it 
            FileOutputStream fos = new FileOutputStream(usersFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(temp); //Return it back again like nothing happened.
        } 
    }
    
    private void fetchBook() throws IOException, ClassNotFoundException{
        if (booksFile.exists()) {
            FileInputStream fis = new FileInputStream(booksFile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            ArrayList<Book> temp = (ArrayList<Book>)ois.readObject();//Fetch it
            storeBooks.addAll(temp);//Add it 
            FileOutputStream fos = new FileOutputStream(booksFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(temp); //Return it back again like nothing happened.
        } 
    }
    
    private void fetchCat() throws FileNotFoundException{
        Scanner sc = new Scanner(catFile);
        while(sc.hasNext()){
            categories.add(sc.nextLine());
        }
        System.out.println(categories);
        sc.close();
    }
    
    public ArrayList<Book> sortBooksByDate(){
        ArrayList<Book> temp = storeBooks;
        Comparator<Book> compr = new Comparator<Book>() {
            @Override
            public int compare(Book t, Book t1) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
                return sdf.format(t.getAdditionDate()).compareTo(sdf.format(t1.getAdditionDate()));                
            }
        };
        Collections.sort(temp, compr);
        return temp;
    }
    
    public ArrayList<Book> sortBooksByYear(){
        ArrayList<Book> temp = storeBooks;
        Comparator<Book> compr = new Comparator<Book>() {
            @Override
            public int compare(Book t, Book t1) {
                return t.getPublishYear() - t1.getPublishYear();
            }
        };
        Collections.sort(temp, compr);
        return temp;
    }
    
    public int searchUser(String username){
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(username)) {
                return i;
            }
        }
        return -1;
    }
    
    public int searchBook(int id){
        for (int i = 0; i < storeBooks.size(); i++) {
            if (storeBooks.get(i).getBookID() == id) {
                return i;
            }
        }
        return -1;
    }
    public int searchBook(String name){
        for (int i = 0; i < storeBooks.size(); i++) {
            if (storeBooks.get(i).getName().equals(name)) {
                return i;
            }
        }
        return -1;
    }
    
    /*public void closeFile(){
        write.flush();
        write.close();
    }*/
}
