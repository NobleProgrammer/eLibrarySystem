/*
    Authorized Access Only!
    Admin Panel Only.
    Name: Ammar Talat Joharji
    ID: 1742559
 */
package admin.panel;

import addbook.AddBookPopupController;
import bookinfo.BookinfoPopupController;
import categories.AddCategoryPopupController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.events.JFXDialogEvent;
import data.model.Book;
import data.model.LibraryEngine;
import data.model.User;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import removebook.RemoveBookPopupController;

/**
 *
 * @author Amar
 */
public class mainController implements Initializable {

    @FXML
    Tab logOut;
    @FXML
    Tab addBook;
    @FXML
    Tab removeBook;
    @FXML
    JFXTabPane tabPane;
    @FXML
    StackPane stackPane;
    @FXML
    Tab myLibrary;
    @FXML
    Tab store;
    @FXML
    ImageView addCat;
    @FXML
    ImageView searchButton;
    @FXML
    GridPane gridMenu;
    @FXML
    GridPane gridBooks;
    @FXML
    ScrollPane scrollPane;
    @FXML
    JFXTextField searchTF;
    @FXML
    JFXComboBox<String> filter;
    private static int lastSelectedTabIndex = 0;
    private static int catCounter = 0;
    private static int booksCounterRow;
    private static int booksCounterColumn;
    private static JFXButton lastPressedButton = new JFXButton();
    //------------------------------------------
    LibraryEngine engine;
    User currentUser;
    //--------------------------------------------------------------------Store Tab Data Fields start here------------------------------------------------------------
    @FXML
    JFXTabPane tabPane1;
    @FXML
    StackPane stackPane1;
    @FXML
    ImageView searchButton1;
    @FXML
    GridPane gridMenu1;
    @FXML
    GridPane gridBooks1;
    @FXML
    ScrollPane scrollPane1;
    @FXML
    JFXTextField searchTF1;
    @FXML
    JFXComboBox<String> filter1;
    private static int catCounter1 = 0;
    private static int booksCounterRow1;
    private static int booksCounterColumn1;
    private static JFXButton lastPressedButton1 = new JFXButton();
    @FXML
    void onTabChanged() {
        lastSelectedTabIndex = tabPane.getSelectionModel().getSelectedIndex();
        EventHandler<Event> handler = new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                if (myLibrary.isSelected()) {
                    System.out.println("My Library");
                    lastSelectedTabIndex = tabPane.getSelectionModel().getSelectedIndex();
                    System.out.println("Hi this is the number : " + lastSelectedTabIndex);
                } else if (addBook.isSelected()) {
                    tabPane.getSelectionModel().select(lastSelectedTabIndex);
                    loadAddBook();
                    resetScroll();
                    resetScrollLib();
                } else if (store.isSelected()) {
                    System.out.println("Store Tab");
                    lastSelectedTabIndex = tabPane.getSelectionModel().getSelectedIndex();
                    System.out.println("Hi this is the number : " + lastSelectedTabIndex);
                } else if (removeBook.isSelected()) {
                    tabPane.getSelectionModel().select(lastSelectedTabIndex);
                    loadRemoveBook();
                    resetScroll();
                    resetScrollLib();
                } else if (logOut.isSelected()) {
                    tabPane.getSelectionModel().select(lastSelectedTabIndex);
                    System.exit(0);
                    System.out.println("Logging out...");
                }
            }
        };
        if (myLibrary != null) {
            myLibrary.setOnSelectionChanged(handler);
        }
        if (store != null) {
            store.setOnSelectionChanged(handler);
        }
        if (addBook != null) {
            addBook.setOnSelectionChanged(handler);
        }
        if (logOut != null) {
            logOut.setOnSelectionChanged(handler);
        }
        if (removeBook != null) {
            removeBook.setOnSelectionChanged(handler);
        }
    }

    @FXML
    public void imageClickListener(MouseEvent event) throws FileNotFoundException {
        if (event.getSource() == addCat) {
            int size = engine.getCategories().size();
            loadAddCat();
            if (size < engine.getCategories().size()) {
                addToCatList(engine.getCategories().get(engine.getCategories().size() - 1));
            }
        }
        else if (event.getSource() == searchButton){
            if (searchTF.getText().equals("") && filter.getSelectionModel().isEmpty()) {
                filter.getSelectionModel().clearSelection();
                makeOkAlert("Please either search by book name, or order book by filter.");
            }
            else if (!(searchTF.getText().equals(""))){
                int index = engine.searchBook(searchTF.getText());
                if (index != -1) {
                    printSearchedBooks(engine.getStoreBooks().get(index));
                }
                else {
                    makeOkAlert("Book Not Found.");
                }
            }
            else {
                filter();
            }
        }
        else if (event.getSource() == searchButton1){
            if (searchTF1.getText().equals("") && filter1.getSelectionModel().isEmpty()) {
                System.out.println(currentUser.getBoughtBooks().size());
                filter1.getSelectionModel().clearSelection();
                makeOkAlert("Please either search by book name, or order book by filter.");
            }
            else if (!(searchTF1.getText().equals(""))){
                int index = currentUser.searchBoughtBooks(searchTF1.getText());
                int index2 = currentUser.searchRentedBooks(searchTF1.getText());
                if (index != -1) {
                    printSearchedBoughtBooksLib(currentUser.getBoughtBooks().get(index));
                }
                else if (index2 != -1){
                    printSearchedRentedBooksLib(currentUser.getRentedBooks().get(index2));
                }
                else {
                    makeOkAlert("Book Not Found.");
                }
            }
            else {
                filterBoughtLib();
                filterRentedLib();
            }
        }
    }

    public void setEngine(LibraryEngine engine, User user) {
        this.engine = engine;
        this.currentUser = user;
        this.updateCatList();
        updateCatListLibr();
        //Window window = (Stage) this.stackPane.getScene().getWindow();
        //window.setOnCloseRequest(e -> engine.closeFile());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.filter.getItems().addAll("Adding Date","Publish Year");
        this.filter1.getItems().addAll("Adding Date","Publish Year");
        onTabChanged();
    }
    //Not for My library
    public void loadAddCat() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/categories/AddCategoryPopup.fxml"));
            Parent root = loader.load();
            AddCategoryPopupController addCatController = loader.getController();
            loader.setController(addCatController);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Add Category");
            addCatController.setEngine(engine);
            stage.showAndWait();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void loadAddBook() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/addbook/AddBookPopup.fxml"));
            Parent root = loader.load();
            AddBookPopupController addBookController = loader.getController();
            loader.setController(addBookController);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Add Book");
            addBookController.setEngine(engine);
            stage.showAndWait();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    //Not for Library tab
    private void addToCatList(String cat) {
        JFXButton button = new JFXButton(cat);
        button.setPrefWidth(258);
        button.setPrefHeight(45);
        button.setAlignment(Pos.BASELINE_LEFT);
        button.setStyle("-fx-font-size:14pt;");//TODO:Change the colors !!! ----------------------------------------------------------
        setButtonListener(button);
        Separator sp = new Separator(Orientation.HORIZONTAL);
        sp.setPrefWidth(258);
        VBox vbox = new VBox();
        vbox.getChildren().addAll(button, sp);
        gridMenu.setConstraints(vbox, 0, catCounter++);
        gridMenu.getChildren().add(vbox);
    }

    private void updateCatList() {
        for (int i = 0; i < engine.getCategories().size(); i++) {
            JFXButton button = new JFXButton(engine.getCategories().get(i));
            button.setPrefWidth(258);
            button.setPrefHeight(45);
            button.setAlignment(Pos.BASELINE_LEFT);
            button.setStyle("-fx-font-size:14pt;");//TODO:Change the colors !!! ----------------------------------------------------------
            setButtonListener(button);
            Separator sp = new Separator(Orientation.HORIZONTAL);
            sp.setPrefWidth(258);
            VBox vbox = new VBox();
            vbox.getChildren().addAll(button, sp);
            gridMenu.setConstraints(vbox, 0, catCounter++);
            gridMenu.getChildren().add(vbox);
        }
    }

    private void setButtonListener(JFXButton button) {

        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    resetScroll();
                    for (int i = 0; i < engine.getStoreBooks().size(); i++) {
                        if (engine.getStoreBooks().get(i).getCategory().equals(button.getText())) {
                            VBox vbox = new VBox();
                            Image image = new Image(new FileInputStream(new File("src/icons/book.png")));
                            ImageView iv = new ImageView(image);
                            iv.setFitWidth(80);
                            iv.setFitHeight(100);
                            Label bookName = new Label(engine.getStoreBooks().get(i).getName());
                            Label bookAuthor = new Label(engine.getStoreBooks().get(i).getAuthor());
                            HBox hbox = new HBox();
                            Label bookBuyPrice = new Label("Buy: " + engine.getStoreBooks().get(i).getBuyPrice() + "");
                            Label bookRentPrice = new Label("Rent: " + engine.getStoreBooks().get(i).getRentPrice() + "");
                            Label booID = new Label("Book ID: " + engine.getStoreBooks().get(i).getBookID());
                            hbox.getChildren().add(bookBuyPrice);
                            hbox.getChildren().add(bookRentPrice);
                            hbox.setSpacing(10);
                            JFXButton buyRent = new JFXButton("Buy/Rent");
                            //Add Listener
                            setBuyRentListener(buyRent, i);
                            vbox.getChildren().add(iv);
                            vbox.getChildren().add(bookName);
                            vbox.getChildren().add(bookAuthor);
                            vbox.getChildren().add(booID);
                            vbox.getChildren().add(hbox);
                            vbox.getChildren().add(buyRent);
                            vbox.setAlignment(Pos.CENTER);
                            gridBooks.setHgap(10);
                            gridBooks.setPadding(new Insets(0, 0, 0, 10));
                            gridBooks.setConstraints(vbox, booksCounterColumn, booksCounterRow);
                            gridBooks.getChildren().add(vbox);
                            if (booksCounterColumn < 6) {
                                booksCounterColumn++;
                            } else {
                                booksCounterColumn = 0;
                                booksCounterRow++;
                            }
                        }
                    }
                    //scrollPane.setContent(gridBooks);
                    lastPressedButton = ((JFXButton) event.getSource());
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(mainController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    private void resetScroll() {
        gridBooks.getColumnConstraints().clear();
        gridBooks.getRowConstraints().clear();
        gridBooks.getChildren().clear();
        booksCounterColumn = 0;
        booksCounterRow = 0;
    }

    private void setBuyRentListener(JFXButton buyRent, int i) {
        buyRent.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loadBookInfo(engine.getStoreBooks().get(i));
                resetScroll();
            }
        });
    }

    public void loadBookInfo(Book book) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bookinfo/BookinfoPopup.fxml"));
            Parent root = loader.load();
            BookinfoPopupController bookInfoPopupController = loader.getController();
            loader.setController(bookInfoPopupController);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Add Book");
            bookInfoPopupController.setEngine(engine, currentUser, book);
            stage.showAndWait();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void loadRemoveBook() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/removebook/RemoveBookPopup.fxml"));
            Parent root = loader.load();
            RemoveBookPopupController removeBookController = loader.getController();
            loader.setController(removeBookController);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Remove Book");
            removeBookController.setEngine(engine);
            stage.showAndWait();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void filter() throws FileNotFoundException {
        resetScroll();
        ArrayList<Book> temp;
        if (filter.getSelectionModel().getSelectedItem().equals("Adding Date")) {
            temp = engine.sortBooksByDate();
        } else { // Means By Publish Year
            temp = engine.sortBooksByYear();
        }
        System.out.println(temp.size());
        for (int i = 0; i < temp.size(); i++) {
            VBox vbox = new VBox();
            Image image = new Image(new FileInputStream(new File("src/icons/book.png")));
            ImageView iv = new ImageView(image);
            iv.setFitWidth(80);
            iv.setFitHeight(100);
            Label bookName = new Label(engine.getStoreBooks().get(i).getName());
            Label bookAuthor = new Label(engine.getStoreBooks().get(i).getAuthor());
            HBox hbox = new HBox();
            Label bookBuyPrice = new Label("Buy: " + engine.getStoreBooks().get(i).getBuyPrice() + "");
            Label bookRentPrice = new Label("Rent: " + engine.getStoreBooks().get(i).getRentPrice() + "");
            Label booID = new Label("Book ID: " + engine.getStoreBooks().get(i).getBookID());
            JFXButton buyRent = new JFXButton("Buy/Rent");
            //Add Listener
            setBuyRentListener(buyRent, i);
            hbox.getChildren().add(bookBuyPrice);
            hbox.getChildren().add(bookRentPrice);
            hbox.setSpacing(10);
            vbox.getChildren().add(iv);
            vbox.getChildren().add(bookName);
            vbox.getChildren().add(bookAuthor);
            vbox.getChildren().add(booID);
            vbox.getChildren().add(hbox);
            vbox.getChildren().add(buyRent);
            vbox.setAlignment(Pos.CENTER);
            gridBooks.setHgap(10);
            gridBooks.setPadding(new Insets(0, 0, 0, 10));
            gridBooks.setConstraints(vbox, booksCounterColumn, booksCounterRow);
            gridBooks.getChildren().add(vbox);
            if (booksCounterColumn < 6) {
                booksCounterColumn++;
            } else {
                booksCounterColumn = 0;
                booksCounterRow++;
            }
        }
    }

    public void printSearchedBooks(Book book) throws FileNotFoundException {
        resetScroll();
        VBox vbox = new VBox();
        Image image = new Image(new FileInputStream(new File("src/icons/book.png")));
        ImageView iv = new ImageView(image);
        iv.setFitWidth(80);
        iv.setFitHeight(100);
        Label bookName = new Label(book.getName());
        Label bookAuthor = new Label(book.getAuthor());
        HBox hbox = new HBox();
        Label bookBuyPrice = new Label("Buy: " + book.getBuyPrice() + "");
        Label bookRentPrice = new Label("Rent: " + book.getRentPrice() + "");
        Label booID = new Label("Book ID: " + book.getBookID());
        JFXButton buyRent = new JFXButton("Buy/Rent");
        //Add Listener
        int index = engine.searchBook(book.getBookID());
        setBuyRentListener(buyRent, index);
        hbox.getChildren().add(bookBuyPrice);
        hbox.getChildren().add(bookRentPrice);
        hbox.setSpacing(10);
        vbox.getChildren().add(iv);
        vbox.getChildren().add(bookName);
        vbox.getChildren().add(bookAuthor);
        vbox.getChildren().add(booID);
        vbox.getChildren().add(hbox);
        vbox.getChildren().add(buyRent);
        vbox.setAlignment(Pos.CENTER);
        gridBooks.setHgap(10);
        gridBooks.setPadding(new Insets(0, 0, 0, 10));
        gridBooks.setConstraints(vbox, booksCounterColumn, booksCounterRow);
        gridBooks.getChildren().add(vbox);
        if (booksCounterColumn < 6) {
            booksCounterColumn++;
        } else {
            booksCounterColumn = 0;
            booksCounterRow++;
        }
    }
    //-------------------------------------------------------------------------My Library Tab-------------------------------------
    //-------------------------------------------------------------------------My Library Tab-------------------------------------
    //-------------------------------------------------------------------------My Library Tab-------------------------------------
    //-------------------------------------------------------------------------My Library Tab-------------------------------------
    //-------------------------------------------------------------------------My Library Tab-------------------------------------
    //-------------------------------------------------------------------------My Library Tab-------------------------------------
    //-------------------------------------------------------------------------My Library Tab-------------------------------------
    //-------------------------------------------------------------------------My Library Tab-------------------------------------
    
    private void updateCatListLibr() {
        for (int i = 0; i < engine.getCategories().size(); i++) {
            JFXButton button = new JFXButton(engine.getCategories().get(i));
            button.setPrefWidth(258);
            button.setPrefHeight(45);
            button.setAlignment(Pos.BASELINE_LEFT);
            button.setStyle("-fx-font-size:14pt;");//TODO:Change the colors !!! ----------------------------------------------------------
            setButtonListenerLib(button);
            Separator sp = new Separator(Orientation.HORIZONTAL);
            sp.setPrefWidth(258);
            VBox vbox = new VBox();
            vbox.getChildren().addAll(button, sp);
            gridMenu1.setConstraints(vbox, 0, catCounter1++);
            gridMenu1.getChildren().add(vbox);
        }
    }
    
    private void setButtonListenerLib(JFXButton button) {
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    resetScrollLib();
                    for (int i = 0; i < currentUser.getBoughtBooks().size(); i++) {
                        if (currentUser.getBoughtBooks().get(i).getCategory().equals(button.getText())) {
                            VBox vbox = new VBox();
                            Image image = new Image(new FileInputStream(new File("src/icons/book.png")));
                            ImageView iv = new ImageView(image);
                            iv.setFitWidth(80);
                            iv.setFitHeight(100);
                            Label bookName = new Label(currentUser.getBoughtBooks().get(i).getName());
                            Label bookAuthor = new Label(currentUser.getBoughtBooks().get(i).getAuthor());
                            HBox hbox = new HBox();
                            Label bookBuyPrice = new Label("Buy: " + currentUser.getBoughtBooks().get(i).getBuyPrice() + "");
                            Label bookRentPrice = new Label("Rent: " + currentUser.getBoughtBooks().get(i).getRentPrice() + "");
                            Label booID = new Label("Book ID: " + currentUser.getBoughtBooks().get(i).getBookID());
                            hbox.getChildren().add(bookBuyPrice);
                            hbox.getChildren().add(bookRentPrice);
                            hbox.setSpacing(10);
                            vbox.getChildren().add(iv);
                            vbox.getChildren().add(bookName);
                            vbox.getChildren().add(bookAuthor);
                            vbox.getChildren().add(booID);
                            vbox.getChildren().add(hbox);
                            vbox.setAlignment(Pos.CENTER);
                            gridBooks1.setHgap(10);
                            gridBooks1.setPadding(new Insets(0, 0, 0, 10));
                            gridBooks1.setConstraints(vbox, booksCounterColumn1, booksCounterRow1);
                            gridBooks1.getChildren().add(vbox);
                            if (booksCounterColumn1 < 6) {
                                booksCounterColumn1++;
                            } else {
                                booksCounterColumn1 = 0;
                                booksCounterRow1++;
                            }
                        }
                    }
                    //--------------------- for rent books
                    for (int i = 0; i < currentUser.getRentedBooks().size(); i++) {
                        if (currentUser.getRentedBooks().get(i).getCategory().equals(button.getText())) {
                            VBox vbox = new VBox();
                            Image image = new Image(new FileInputStream(new File("src/icons/book.png")));
                            ImageView iv = new ImageView(image);
                            iv.setFitWidth(80);
                            iv.setFitHeight(100);
                            Label bookName = new Label(currentUser.getRentedBooks().get(i).getName());
                            Label bookAuthor = new Label(currentUser.getRentedBooks().get(i).getAuthor());
                            HBox hbox = new HBox();
                            Label bookBuyPrice = new Label("Buy: " + currentUser.getRentedBooks().get(i).getBuyPrice() + "");
                            Label bookRentPrice = new Label("Rent: " + currentUser.getRentedBooks().get(i).getRentPrice() + "");
                            Label booID = new Label("Book ID: " + currentUser.getRentedBooks().get(i).getBookID());
                            hbox.getChildren().add(bookBuyPrice);
                            hbox.getChildren().add(bookRentPrice);
                            hbox.setSpacing(10);
                            vbox.getChildren().add(iv);
                            vbox.getChildren().add(bookName);
                            vbox.getChildren().add(bookAuthor);
                            vbox.getChildren().add(booID);
                            vbox.getChildren().add(hbox);
                            vbox.setAlignment(Pos.CENTER);
                            gridBooks1.setHgap(10);
                            gridBooks1.setPadding(new Insets(0, 0, 0, 10));
                            gridBooks1.setConstraints(vbox, booksCounterColumn1, booksCounterRow1);
                            gridBooks1.getChildren().add(vbox);
                            if (booksCounterColumn1 < 6) {
                                booksCounterColumn1++;
                            } else {
                                booksCounterColumn1 = 0;
                                booksCounterRow1++;
                            }
                        }
                    }
                    //scrollPane.setContent(gridBooks);
                    lastPressedButton1 = ((JFXButton) event.getSource());
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(mainController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    private void resetScrollLib() {
        gridBooks1.getColumnConstraints().clear();
        gridBooks1.getRowConstraints().clear();
        gridBooks1.getChildren().clear();
        booksCounterColumn1 = 0;
        booksCounterRow1 = 0;
    }

    private void setBuyRentListenerLib(JFXButton buyRent, int i) {
        buyRent.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loadBookInfo(engine.getStoreBooks().get(i));
                resetScrollLib();
            }
        });
    }    
    
    public void filterBoughtLib() throws FileNotFoundException {
        resetScrollLib();
        ArrayList<Book> temp;
        if (filter1.getSelectionModel().getSelectedItem().equals("Adding Date")) {
            temp = currentUser.sortBoughtBooksByDate();
            //System.out.println(" this from " + temp.size());
        } else { // Means By Publish Year
            temp = currentUser.sortBoughtBooksByYear();
        }
        for (int i = 0; i < temp.size(); i++) {
            VBox vbox = new VBox();
            Image image = new Image(new FileInputStream(new File("src/icons/book.png")));
            ImageView iv = new ImageView(image);
            iv.setFitWidth(80);
            iv.setFitHeight(100);
            Label bookName = new Label(currentUser.getBoughtBooks().get(i).getName());
            Label bookAuthor = new Label(currentUser.getBoughtBooks().get(i).getAuthor());
            HBox hbox = new HBox();
            Label bookBuyPrice = new Label("Buy: " + currentUser.getBoughtBooks().get(i).getBuyPrice() + "");
            Label bookRentPrice = new Label("Rent: " + currentUser.getBoughtBooks().get(i).getRentPrice() + "");
            Label booID = new Label("Book ID: " + currentUser.getBoughtBooks().get(i).getBookID());
            hbox.getChildren().add(bookBuyPrice);
            hbox.getChildren().add(bookRentPrice);
            hbox.setSpacing(10);
            vbox.getChildren().add(iv);
            vbox.getChildren().add(bookName);
            vbox.getChildren().add(bookAuthor);
            vbox.getChildren().add(booID);
            vbox.getChildren().add(hbox);
            vbox.setAlignment(Pos.CENTER);
            gridBooks1.setHgap(10);
            gridBooks1.setPadding(new Insets(0, 0, 0, 10));
            gridBooks1.setConstraints(vbox, booksCounterColumn1, booksCounterRow1);
            gridBooks1.getChildren().add(vbox);
            if (booksCounterColumn1 < 6) {
                booksCounterColumn1++;
            } else {
                booksCounterColumn1 = 0;
                booksCounterRow1++;
            }
        }
    }
    
    public void filterRentedLib() throws FileNotFoundException {
        resetScrollLib();
        
        ArrayList<Book> temp;
        if (filter1.getSelectionModel().getSelectedItem().equals("Adding Date")) {
            temp = currentUser.sortRentedBooksByDate();
        } else { // Means By Publish Year
            temp = currentUser.sortRentedBooksByYear();
        }
        System.out.println(temp.size());
        for (int i = 0; i < temp.size(); i++) {
            VBox vbox = new VBox();
            Image image = new Image(new FileInputStream(new File("src/icons/book.png")));
            ImageView iv = new ImageView(image);
            iv.setFitWidth(80);
            iv.setFitHeight(100);
            Label bookName = new Label(currentUser.getRentedBooks().get(i).getName());
            Label bookAuthor = new Label(currentUser.getRentedBooks().get(i).getAuthor());
            HBox hbox = new HBox();
            Label bookBuyPrice = new Label("Buy: " + currentUser.getRentedBooks().get(i).getBuyPrice() + "");
            Label bookRentPrice = new Label("Rent: " + currentUser.getRentedBooks().get(i).getRentPrice() + "");
            Label booID = new Label("Book ID: " + currentUser.getRentedBooks().get(i).getBookID());
            hbox.getChildren().add(bookBuyPrice);
            hbox.getChildren().add(bookRentPrice);
            hbox.setSpacing(10);
            vbox.getChildren().add(iv);
            vbox.getChildren().add(bookName);
            vbox.getChildren().add(bookAuthor);
            vbox.getChildren().add(booID);
            vbox.getChildren().add(hbox);
            vbox.setAlignment(Pos.CENTER);
            gridBooks1.setHgap(10);
            gridBooks1.setPadding(new Insets(0, 0, 0, 10));
            gridBooks1.setConstraints(vbox, booksCounterColumn1, booksCounterRow1);
            gridBooks1.getChildren().add(vbox);
            if (booksCounterColumn1 < 6) {
                booksCounterColumn1++;
            } else {
                booksCounterColumn1 = 0;
                booksCounterRow1++;
            }
        }
    }
    
    public void printSearchedBoughtBooksLib(Book book) throws FileNotFoundException {
        resetScrollLib();
        VBox vbox = new VBox();
        Image image = new Image(new FileInputStream(new File("src/icons/book.png")));
        ImageView iv = new ImageView(image);
        iv.setFitWidth(80);
        iv.setFitHeight(100);
        Label bookName = new Label(book.getName());
        Label bookAuthor = new Label(book.getAuthor());
        HBox hbox = new HBox();
        Label bookBuyPrice = new Label("Buy: " + book.getBuyPrice() + "");
        Label bookRentPrice = new Label("Rent: " + book.getRentPrice() + "");
        Label booID = new Label("Book ID: " + book.getBookID());
        hbox.getChildren().add(bookBuyPrice);
        hbox.getChildren().add(bookRentPrice);
        hbox.setSpacing(10);
        vbox.getChildren().add(iv);
        vbox.getChildren().add(bookName);
        vbox.getChildren().add(bookAuthor);
        vbox.getChildren().add(booID);
        vbox.getChildren().add(hbox);
        vbox.setAlignment(Pos.CENTER);
        gridBooks1.setHgap(10);
        gridBooks1.setPadding(new Insets(0, 0, 0, 10));
        gridBooks1.setConstraints(vbox, booksCounterColumn1, booksCounterRow1);
        gridBooks1.getChildren().add(vbox);
        if (booksCounterColumn1 < 6) {
            booksCounterColumn1++;
        } else {
            booksCounterColumn1 = 0;
            booksCounterRow1++;
        }
    }
    
    public void printSearchedRentedBooksLib(Book book) throws FileNotFoundException {
        resetScrollLib();
        VBox vbox = new VBox();
        Image image = new Image(new FileInputStream(new File("src/icons/book.png")));
        ImageView iv = new ImageView(image);
        iv.setFitWidth(80);
        iv.setFitHeight(100);
        Label bookName = new Label(book.getName());
        Label bookAuthor = new Label(book.getAuthor());
        HBox hbox = new HBox();
        Label bookBuyPrice = new Label("Buy: " + book.getBuyPrice() + "");
        Label bookRentPrice = new Label("Rent: " + book.getRentPrice() + "");
        Label booID = new Label("Book ID: " + book.getBookID());
        hbox.getChildren().add(bookBuyPrice);
        hbox.getChildren().add(bookRentPrice);
        hbox.setSpacing(10);
        vbox.getChildren().add(iv);
        vbox.getChildren().add(bookName);
        vbox.getChildren().add(bookAuthor);
        vbox.getChildren().add(booID);
        vbox.getChildren().add(hbox);
        vbox.setAlignment(Pos.CENTER);
        gridBooks1.setHgap(10);
        gridBooks1.setPadding(new Insets(0, 0, 0, 10));
        gridBooks1.setConstraints(vbox, booksCounterColumn1, booksCounterRow1);
        gridBooks1.getChildren().add(vbox);
        if (booksCounterColumn1 < 6) {
            booksCounterColumn1++;
        } else {
            booksCounterColumn1 = 0;
            booksCounterRow1++;
        }
    }
    
    //------------
    public void makeOkAlert(String text) {
        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        JFXButton button = new JFXButton("Ok");
        JFXDialog dialog = new JFXDialog(stackPane, dialogLayout, JFXDialog.DialogTransition.TOP);
        button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
            dialog.close();
        });
        dialogLayout.setHeading(new Label(text));
        dialogLayout.setActions(button);
        dialog.show();
        dialog.setOnDialogClosed(new EventHandler<JFXDialogEvent>() {
            @Override
            public void handle(JFXDialogEvent event) {
                stackPane.setEffect(null);
            }
        });
    }
}
        