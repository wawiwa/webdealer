webdealer
=========

"webdealer" (one word, no caps) is a JavaFX GUI application that simulates a coupon purchasing website.

Overview

  webdealer has 6 main views "MERCHANTS","LOCATIONS","DEALS","CUSTOMERS","PURCHASES" and "REVIEWS". Typically
  each view contains common CRUD (Create/Retrieve/Update/Delete) actions, with some slight variations depending
  on the business logic constraints of our assignment, and the ability to harmlessly "Clear" textfields.

    MERCHANTS: Provides a list of existing merchants, common CRUD, and the ability to "Show reviews & ratings" for
      a specifically selected merchant.
    LOCATIONS: ........
    DEALS: Provides a list of existing deals, common CRUD, and filtering by "Open/Available Deals". From this
      view you can also purchase or "Buy" a deal. To buy a deal, you must specify a quantity. If the quantity
      exceeds the number of available vouchers, the system will notify you in the right pane console.
    CUSTOMERS: Provides a list of existing customers, common CRUD, a button to "Edit/Add payment" methods, and
      "Show Purchase History" for a selected customer. The default is to show the logged in customer's 
      purchase history.
    PURCHASES: You can update your voucher status here. From say "current" to "used". A purchase is really
      intended as a rather immutable concept. Other than modifying status, one can only retrieve purchases.
      There's also a quick buy, where if you know the Deal ID you can buy it on the spot in the purchase view.
      Kind of left over from testing :)
    REVIEWS: ..........
      
Getting Started

  Installation: 

    This application can run as a deployable jar from Linux,Mac, or Windows.
  
  Login:

    To login, you must have an Oracle IT&E account at GMU. webdealer is designed to operate with any backend, however
    it requires the JDBC connection string be modified to a database that allows schema creation.

    To gain access to the webdealer, and act as a customer, you must also have a valid login. The login is your
    email address. If you're not already in the database, login with the default 'portiz@gmu.edu' and add your account.
    Accounts can be created under CUSTOMERS. Be sure to select "Edit Payments" and add at least one credit card.

    The quickest way to get started, is to simply click Login under "Oracle Username". The default creds
    should return "Confirmed!" on the login button.

    If you do login with different credentials and something goes wrong, the login button will report "Incorrect!" Let
    t/s ensue.

  Example run:

    After successfully logging into Oracle and the webdealer system, you will see your purchase history. From there
    it's recommended to try the following activities:

      1) Select DEALS, then select "Open Deals".
      2) Find a deal you like, then check them out under REVIEWS.
      3) If you're satisfied with the customer reviews, select DEALS, and that particular deal again.
      4) Enter how many vouchers you would like, by entering quantity next to "Buy this Deal".
      5) Select "Buy this deal"
      6) If there are enough vouchers, you will see the recently purchased deals.
      7) Then look at your total purchase history by selecting "Retrieve"
      8) ...

Design Goals

    This project was designed according to the Model-View-Controller format. There's a CRUD model for most all tables.
    The controller interfaces with the model code to manipulate the database. The view provides a (F)XML based GUI 
    interface to the controller. As buttons are selected from the view, events are fired that trigger the controller to
    do something with models that interface with the database.

    The model constructs a tuple in the database and also in the view, depending on the state of the ID passed. If the
    ID is null, the model will create a tuple in the database, whereas if the ID is valid, the constructor will only pass
    information to the observable list for the view.

    As for schema, we load it based on a preliminary check of existing tables. If all tables exist, then no schema is
    loaded. If some or even one is missing then we drop all tables, all sequences and triggers and reload. Our schema,
    inserts, triggers and some queries are stored in resources files (/resources/blabla.sql).

Weirdness

    Ultimately we decided to trigger vouchers based on quantity limit within deals. This is bad. We have vouchers
    unecessarily created in the db waiting to be purchased. This did make it much easier to deal with having
    multiple vouchers associate with a single purchase ie. one transaction ID for several vouchers AKA shopping cart.

    Join is unbelievably slow. The purchase table is a join of vouchers, customers, and transacions. It takes much
    longer to render purchases than a single table query like retrieving deals.

  
Tools

    The project was created using the e(FX)clipse plugin. We also used Oracle Scenebuilder to perform our theming and
    FXML generation.

Colophon

    Miriam Joy and Wade Ward
    Dr. Jessica Lin





