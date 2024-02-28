package com.example.snakeladder;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

public class UserInterface {
    GridPane loginPage;
    HBox headBar;
    HBox footerBar;
    Button signInButton;
    Label welcomeLabel;
    VBox body;


    Customer loggedInCustomer;

    ProductList productList=new ProductList();

    VBox productPage;
    Button placeOrderButton=new Button("Place Order");

    ObservableList<Product> itemsInCart= FXCollections.observableArrayList();
    public BorderPane createContent()
    {
        BorderPane root=new BorderPane();
        root.setPrefSize(800,600);
        //root.getChildren().add(loginPage); //method to add nodes as children  to pane.
        root.setTop(headBar);
        //root.setCenter(loginPage);
        body=new VBox();
        body.setPadding(new Insets(10));
        body.setAlignment(Pos.CENTER);
        root.setCenter(body);
        productPage = productList.getDummyTable();
        body.getChildren().add(productPage);
        root.setBottom(footerBar);

        return root;
    }
    public UserInterface()
    {
         createLoginPage();
         createHeaderBar();
         createFooterBar();
    }
    private void createLoginPage()
    {
        Text userNameText=new Text("User Name");
        Text passwordText=new Text("Password");

        TextField userName=new TextField("angad@gmail.com");
        userName.setPromptText("Type your user name here");
        PasswordField password=new PasswordField();
        password.setText("abc123");
        password.setPromptText("Type your Password here");

        Label messageLabel=new Label("Hi");
        Button loginButton=new Button("LOGIN");

        loginPage=new GridPane();
        //loginPage.setStyle("-fx-background-color: grey");
        loginPage.setAlignment(Pos.CENTER);
        loginPage.setVgap(10);
        loginPage.setHgap(10);
        loginPage.add(userNameText,0,0);
        loginPage.add(userName, 1, 0);
        loginPage.add(passwordText,0,1);
        loginPage.add(password, 1, 1);
        loginPage.add(messageLabel, 0 ,2);
        loginPage.add(loginButton,1,2);

        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String name =userName.getText();
                String pass =password.getText();
                Login login=new Login();
                loggedInCustomer=login.customerLogin(name, pass);
                if(loggedInCustomer != null)
                {
                    messageLabel.setText("Welcome : " + loggedInCustomer.getName());
                    welcomeLabel.setText("Welcome" + loggedInCustomer.getName());
                    headBar.getChildren().add(welcomeLabel);
                }
                else {
                    messageLabel.setText("Login Failed ! Please provide correct credentials");
                }

            }
        });

    }

    private void createHeaderBar()
    {
        Button homeButton=new Button();
        Image image=new Image("C:\\Users\\manthan londhe\\IdeaProjects\\SnakeLadder\\src\\ecommercee.jpg");
        ImageView imageView=new ImageView();
        imageView.setImage(image);
        imageView.setFitHeight(80);
        imageView.setFitWidth(80);
        homeButton.setGraphic(imageView);

        TextField searchBar =new TextField();
        searchBar.setPromptText("Search Here");
        searchBar.setPrefWidth(180); 

        Button searchButton=new Button("Search");

        signInButton=new Button("Sign in");
        welcomeLabel =new Label();

        Button cartButton=new Button("Cart");
        Button orderButton=new Button("Orders");


        headBar =new HBox(20);
        //headBar.setStyle("-fx-background-color: grey");
        headBar.setPadding(new Insets(10));
        headBar.setAlignment(Pos.CENTER);
        headBar.getChildren().addAll(homeButton,searchBar, searchButton,signInButton,cartButton,orderButton);

        signInButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                body.getChildren().clear();
                body.getChildren().add(loginPage);
                headBar.getChildren().remove(signInButton);
//              body.getChildren().clear();
//              body.getChildren().add(productPage);
            }
        });
        cartButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                body.getChildren().clear();
                VBox prodPage=productList.getProductsInCar(itemsInCart);
                prodPage.setAlignment(Pos.CENTER);
                prodPage.setSpacing(10);
                prodPage.getChildren().add(placeOrderButton);
                body.getChildren().add(prodPage);
                footerBar.setVisible(false);
            }
        });

        placeOrderButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

            }
        });
        homeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                body.getChildren().clear();
                body.getChildren().add(productPage);
                footerBar.setVisible(true);
                if(loggedInCustomer==null && headBar.getChildren().indexOf(signInButton)==-1)
                {
                        headBar.getChildren().add(signInButton);
                }
            }
        });
    }

    private void createFooterBar()
    {
//        TextField searchBar =new TextField();
//        searchBar.setPromptText("Search Here");
//        searchBar.setPrefWidth(180);

        Button buyNowButton=new Button("BuyNow");
        Button addToCartButton=new Button();

//        signInButton=new Button("Sign in");
//        welcomeLabel =new Label();

        footerBar =new HBox(20);
        //headBar.setStyle("-fx-background-color: grey");
        footerBar.setPadding(new Insets(10));
        footerBar.setSpacing(10);
        footerBar.setAlignment(Pos.CENTER);
        footerBar.getChildren().addAll(buyNowButton, addToCartButton);

        buyNowButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Product product=productList.getSelectedProduct();
                if(itemsInCart==null)
                {
                    showDailog("Please add some products in the cart to place order");
                    return;
                }
                if(loggedInCustomer==null)
                {
                    showDailog("Please login first to place order");
                    return;
                }
                int count=Order.placeMultipleOrder(loggedInCustomer,itemsInCart);
                if(count!=0)
                {
                    showDailog("Order for "+count+" products  Placed Successfully");
                }
                else {
                    showDailog("Order Failed!");
                }

            }
        });

        addToCartButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Product product=productList.getSelectedProduct();
                if(product==null)
                {
                    showDailog("Please select a product first to add it to cart");
                    return;
                }
                itemsInCart.add(product);
                showDailog("Selected item has been added successfully");
            }
        });

    }
    private void showDailog(String message)
    {
        Alert alert=new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText("Message");
        alert.setContentText(message);
        alert.setTitle(null);
        alert.showAndWait();

    }
}
