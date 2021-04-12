/*
This class contains the business logic for the users. (Admin or not Admin)
*/
package data.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class User implements Serializable{
    private String username;
    private String password;
    private boolean isAdmin;
    private ArrayList<Book> boughtBooks;
    private ArrayList<Book> rentedBooks;
    private static int userCount;
    private int userID;
    
    public User(String username, String password, boolean isAdmin) throws FileNotFoundException, IOException, ClassNotFoundException {
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
        this.boughtBooks = new ArrayList<>();
        this.rentedBooks = new ArrayList<>();
        userCount++;
        userID = userCount;
        fetchBoughtBook();
        fetchRentedBook();
    }
    //----------------------------------------------------------------------NO REMOVE BOOK FUNCTIONALITY UNTIL NOW!-----------------------------------------------------------------

    public static int getUserCount() {
        return userCount;
    }

    public int getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
   
    public void addBoughtBook(Book book) throws IOException, ClassNotFoundException{
        File file = new File(this.username + "_BoughtBooks.ser");
        this.boughtBooks.add(book);
        if (file.exists()) {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            ois.readObject();//Fetch it
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(boughtBooks); //Return it back again like nothing happened.
        }
        else {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(boughtBooks);
        }
    }
    
    public void addRentedBook(Book book) throws IOException, ClassNotFoundException {
        File file = new File(this.username + "_RentedBooks.ser");
        this.rentedBooks.add(book);
        if (file.exists()) {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            ois.readObject();//Fetch it
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(rentedBooks); //Return it back again like nothing happened.
        }
        else {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(rentedBooks);
        }
    }
    
    public ArrayList<Book> getBoughtBooks(){
        return this.boughtBooks;
    }
    
    public ArrayList<Book> getRentedBooks(){
        return this.rentedBooks;
    }

    public void setBoughtBooks(ArrayList<Book> boughtBooks) {
        this.boughtBooks = boughtBooks;
    }

    public void setRentedBooks(ArrayList<Book> rentedBooks) {
        this.rentedBooks = rentedBooks;
    }
    
    public void fetchBoughtBook() throws IOException, ClassNotFoundException{
        File file = new File(this.username + "_BoughtBooks.ser");
        System.out.println("It exists");
        if (file.exists()) {
            
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            ArrayList<Book> temp = (ArrayList<Book>)ois.readObject();//Fetch it
            boughtBooks.addAll(temp);//Add it 
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(temp); //Return it back again like nothing happened.
        } 
        
    }
    
    public void fetchRentedBook() throws IOException, ClassNotFoundException{
        File file = new File(this.username + "_RentedBooks.ser");
        if (file.exists()) {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            ArrayList<Book> temp = (ArrayList<Book>)ois.readObject();//Fetch it
            rentedBooks.addAll(temp);//Add it 
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(temp); //Return it back again like nothing happened.
        } 
    }
    
    public ArrayList<Book> sortBoughtBooksByDate(){
        ArrayList<Book> temp = boughtBooks;
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
    
    public ArrayList<Book> sortBoughtBooksByYear(){
        ArrayList<Book> temp = boughtBooks;
        Comparator<Book> compr = new Comparator<Book>() {
            @Override
            public int compare(Book t, Book t1) {
                return t.getPublishYear() - t1.getPublishYear();
            }
        };
        Collections.sort(temp, compr);
        return temp;
    }
    
    public ArrayList<Book> sortRentedBooksByDate(){
        ArrayList<Book> temp = rentedBooks;
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
    
    public ArrayList<Book> sortRentedBooksByYear(){
        ArrayList<Book> temp = rentedBooks;
        Comparator<Book> compr = new Comparator<Book>() {
            @Override
            public int compare(Book t, Book t1) {
                return t.getPublishYear() - t1.getPublishYear();
            }
        };
        Collections.sort(temp, compr);
        return temp;
    }
    public int searchBoughtBooks(String name){
        for (int i = 0; i < boughtBooks.size(); i++) {
            if (boughtBooks.get(i).getName().equals(name)) {
                return i;
            }
        }
        return -1;
    }
    
    public int searchRentedBooks(String name){
        for (int i = 0; i < rentedBooks.size(); i++) {
            if (rentedBooks.get(i).getName().equals(name)) {
                return i;
            }
        }
        return -1;
    }
}
