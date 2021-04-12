/*
    Authorized Access Only!
    Admin Panel Only.
    Name: Ammar Talat Joharji
    ID: 1742559
 */
package bookinfo;

import com.jfoenix.controls.JFXButton;
import data.model.Book;
import data.model.LibraryEngine;
import data.model.User;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;


public class BookinfoPopupController implements Initializable {
    @FXML Label bookName;
    @FXML Label bookAuthor;
    @FXML Label publishYear;
    @FXML Label bookCategory;
    @FXML Label bookBuyPrice;
    @FXML Label bookRentPrice;
    @FXML JFXButton buy;
    @FXML JFXButton rent;
    //-------------------
    LibraryEngine engine;
    Book book;
    User user;
    
    @FXML
    void buttonHandler(ActionEvent event) throws IOException, ClassNotFoundException{
        if (event.getSource() == buy) {
            engine.buyBook(book.getBookID(), user);
        }
        else { // if it's rent
            engine.rentBook(book.getBookID(), user);
        }
        Stage primaryStage = (Stage) this.bookAuthor.getScene().getWindow();
        primaryStage.close();
    }
    
    public void setEngine(LibraryEngine engine , User user , Book book){
       this.engine = engine; 
       this.book = book;
       this.user = user;
       setLabels();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    void setLabels(){
        this.bookName.setText(book.getName());
        this.bookAuthor.setText(book.getAuthor());
        this.bookCategory.setText(book.getCategory());
        this.bookBuyPrice.setText(book.getBuyPrice() + "");
        this.bookRentPrice.setText(book.getRentPrice() + "");
        this.publishYear.setText(book.getPublishYear() + "");
    }
    
}
