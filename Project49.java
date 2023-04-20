import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.util.Scanner;
import java.util.Properties;

import java.io.*;
import java.sql.*;

public class Project49 {
        static Connection connection;

        public static void main(String[] args) throws Exception {

                // startup sequence
                MyDatabase db = new MyDatabase();
                runConsole(db);

                System.out.println("Exiting...");
        }

        public static void runConsole(MyDatabase db) {

                Scanner console = new Scanner(System.in);
                System.out.print("Welcome! Type h for help. ");
                System.out.print("db > ");
                String line = console.nextLine();
                String[] parts;
                String arg = "";

                while (line != null && !line.equals("q")) {
                        parts = line.split("\\s+");
                        if (line.indexOf(" ") > 0)
                                arg = line.substring(line.indexOf(" ")).trim();


                        if(parts.length>0 && parts[0].equals("i"))
                                db.intializeDatabase();

                        else if (parts.length>0 && parts[0].equals("h"))
                                printHelp();

                        else if (parts.length>0 && parts[0].equals("hcu")) {
                                if (parts.length >= 2)
                                        db.happyCountryUnique(arg);
                                else
                                        System.out.println("Require an argument for this command");
                        }

                        else if (parts.length > 0 && parts[0].equals("hc")) {
                                db.happyContinent();
                        } 

			else if (parts.length > 0 && parts[0].equals("mc")) {
                                db.commonCuisine();
                        }

			else if (parts.length > 0 && parts[0].equals("nzc")) {
                                db.nonZomatoCountries();
                        }


			else if (parts.length > 0 && parts[0].equals("total")) {
                                db.totalRestaurant();
                        }

			else if (parts.length > 0 && parts[0].equals("br")) {
                                db.bestRating();
                        }

                        else if (parts.length > 0 && parts[0].equals("numc")) {
                                db.numberOfCountries();
                        }

                        else if (parts.length > 0 && parts[0].equals("multiple")) {
                                db.multiCurrency();
                        }

                        else if (parts.length > 0 && parts[0].equals("mr")) {
                                db.mostRestaurants();
                        }
                        
                        else
                                System.out.println("Read the help with h, or find help somewhere else.");

                        System.out.print("db > ");
                        line = console.nextLine();
                }

                console.close();
        }

        private static void printHelp() {
                System.out.println("Zomato database");
                System.out.println("Commands:");
                System.out.println("i - intialize the database");
                System.out.println("h - Get help");
                System.out.println("br - Top 5 countries with the best average rating for restaurants in zomato");
                System.out.println("hcu <continent> - TOP 5 happiest countries in a given continent that do not use \nthe most popular currency in that continent");
                System.out.println("hc - continent with the highest averge happiness score of the countries");
                System.out.println("multiple - Currencies that are used in more than one country");
                System.out.println("total - total number of restaurants available in zomato in different countries");
                System.out.println("mr - Top 5 restaurants with most branches in zomato");
                System.out.println("nzc - Countries that do not currently have zomato deliveries");
                System.out.println("mc - Top 5 most common cuisines of restaurants in zomato");
                System.out.println("numc - Number of countries in all the continents");
                System.out.println("");

                System.out.println("q - Exit the program");

                System.out.println("---- end help ----- ");
        }

}


class MyDatabase{

        private Connection connection;
        boolean proceed;


public void happyCountryUnique(String name)
        {
                if(proceed)
                {
                        try{
                                String sql = " SELECT top 5 country, happinessScore, currency from country as outerCountry where currency not in (Select Top 1 currency from country where country.continent = outerCountry.continent group by currency having count(currency) > 1 order by count(currency)desc) and continent  =  ? order by happinessScore desc; ";
                                PreparedStatement statement = connection.prepareStatement(sql);
				statement.setString(1, name);
                                ResultSet resultSet = statement.executeQuery();

				System.out.println("Projecting the Country, Happiness Score and Currency ");
                                while(resultSet.next())
                                {	
				System.out.println(resultSet.getString("country") + " " + resultSet.getFloat("happinessScore") + " " + resultSet.getString("currency"));
                                }

                                resultSet.close();
                                statement.close();

                          }

                         catch (SQLException e)
                        {
                                e.printStackTrace(System.out);
                        }

                }
                else
                {
                        System.out.println("ERROR: Database is not Initialized. Enter 'i' to intialize database");
                }

        }


public void commonCuisine()
        {
                if(proceed)
                {
                        try{
                                String sql = "select TOP 5 cuisineName, count(restaurantID) as counter from cuisine group by cuisineName order by counter DESC;";
                                PreparedStatement statement = connection.prepareStatement(sql);

                                ResultSet resultSet = statement.executeQuery();

				System.out.println("Projecting the Cuisine name and the number of restaurants ");
                                while(resultSet.next())
                                {
					System.out.println(resultSet.getString("cuisineName") + " " + resultSet.getInt("counter"));
                                }

                                resultSet.close();
                                statement.close();

                          }

                         catch (SQLException e)
                        {
                                e.printStackTrace(System.out);
                        }

                }
                else
                {
                        System.out.println("ERROR: Database is not Initialized. Enter 'i' to intialize database");
                }

        }

public void mostRestaurants()
        {
                if(proceed)
                {
                        try{
                                String sql = "select  TOP 5 restaurantName , count(restaurantID) as number from restaurant group by restaurantName order by number DESC;";
                                PreparedStatement statement = connection.prepareStatement(sql);

                                ResultSet resultSet = statement.executeQuery();

				System.out.println("Projecting the Restaurant name and number of restaurants ");
                                while(resultSet.next())
                                {
					System.out.println(resultSet.getString("restaurantName") + " " + resultSet.getInt("number"));
                                }

                                resultSet.close();
                                statement.close();

                          }

                         catch (SQLException e)
                        {
                                e.printStackTrace(System.out);
                        }

                }
                else
                {
                        System.out.println("ERROR: Database is not Initialized. Enter 'i' to intialize database");
                }

        }

public void multiCurrency()
        {
                if(proceed)
                {
                        try{
                                String sql = "select  currency , count(country) as counter from country group by  currency  having count(country) > 1;";
                                PreparedStatement statement = connection.prepareStatement(sql);

                                ResultSet resultSet = statement.executeQuery();

				System.out.println("Projecting the Currency and Number of Countries ");
                                while(resultSet.next())
                                {
					System.out.println(resultSet.getString("currency") + " " + resultSet.getInt("counter"));
                                }

                                resultSet.close();
                                statement.close();

                          }

                         catch (SQLException e)
                        {
                                e.printStackTrace(System.out);
                        }

                }
                else
                {
                        System.out.println("ERROR: Database is not Initialized. Enter 'i' to intialize database");
                }

        }


public void numberOfCountries()
        {
                if(proceed)
                {
                        try{
                                String sql = "select continent.continent , count(country) as counter from continent left join country on continent.continent = country.continent group by continent.continent;";
                                PreparedStatement statement = connection.prepareStatement(sql);

                                ResultSet resultSet = statement.executeQuery();

				System.out.println("Projecting the Continent and Number of Countries ");
                                while(resultSet.next())
                                {
					System.out.println(resultSet.getString("continent") + " " + resultSet.getInt("counter"));
                                }

                                resultSet.close();
                                statement.close();

                          }

                         catch (SQLException e)
                        {
                                e.printStackTrace(System.out);
                        }

                }
                else
                {
                        System.out.println("ERROR: Database is not Initialized. Enter 'i' to intialize database");
                }

        }

public void happyContinent()
        {
                if(proceed)
                {
                        try{
                                String sql = "select continent.continent , avg(happinessScore) as average from continent join country on continent.continent = country.continent group by continent.continent order by average desc;";
                                PreparedStatement statement = connection.prepareStatement(sql);

                                ResultSet resultSet = statement.executeQuery();

				System.out.println("Projecting the Continent and the Average Happiness Score ");
                                while(resultSet.next())
                                {
					System.out.println(resultSet.getString("continent") + " " + resultSet.getFloat("average"));
                                }

                                resultSet.close();
                                statement.close();

                          }

                         catch (SQLException e)
                        {
                                e.printStackTrace(System.out);
                        }

                }
                else
                {
                        System.out.println("ERROR: Database is not Initialized. Enter 'i' to intialize database");
                }

        }

public void bestRating()
        {
                if(proceed)
                {
                        try{
                                String sql = "select TOP 5 countrycode.countryCode, country , avg(aggregateRating) as average from restaurant join address on restaurant.address = address.address join countrycode on address.countryCode = countrycode.countryCode group by countrycode.countryCode , country order by average desc;";
                                PreparedStatement statement = connection.prepareStatement(sql);

                                ResultSet resultSet = statement.executeQuery();

				System.out.println("Projecting the Country Code in Zomato, Country and the Average Aggregate Rating ");
                                while(resultSet.next())
                                {
					System.out.println(resultSet.getString("countryCode") + " " + resultSet.getString("country") + " " + resultSet.getFloat("average"));
                                }

                                resultSet.close();
                                statement.close();

                          }

                         catch (SQLException e)
                        {
                                e.printStackTrace(System.out);
                        }

                }
                else
                {
                        System.out.println("ERROR: Database is not Initialized. Enter 'i' to intialize database");
                }

        }

public void totalRestaurant()
        {
                if(proceed)
                {
                        try{
                                String sql = "select countrycode.countryCode, country , count(restaurantID) as total from countrycode  join address on countrycode.countryCode = address.countryCode join restaurant on address.address = restaurant.address group by countrycode.countryCode, country order by total desc;";
                                PreparedStatement statement = connection.prepareStatement(sql);

                                ResultSet resultSet = statement.executeQuery();

				System.out.println("Projecting the Country Code, Country and the Total Number of Restaurants ");
                                while(resultSet.next())
                                {
					System.out.println(resultSet.getString("countryCode") + " " + resultSet.getString("country") + " " + resultSet.getInt("total"));
                                }

                                resultSet.close();
                                statement.close();

                          }

                         catch (SQLException e)
                        {
                                e.printStackTrace(System.out);
                        }

                }
                else
                {
                        System.out.println("ERROR: Database is not Initialized. Enter 'i' to intialize database");
                }

        }

public void nonZomatoCountries()
        {
                if(proceed)
                {
                        try{
                                String sql = "SELECT country from country where country not in (select country from countrycode left join address on countryCode.countrycode = address.countrycode);";
                                PreparedStatement statement = connection.prepareStatement(sql);

                                ResultSet resultSet = statement.executeQuery();
				int count = 0;

				System.out.println("Projecting the Countries");
                                while(resultSet.next())
                                {
					System.out.println(++count +  " - " + resultSet.getString("country"));
                                }

                                resultSet.close();
                                statement.close();

                          }

                         catch (SQLException e)
                        {
                                e.printStackTrace(System.out);
                        }

                }
                else
                {
                        System.out.println("ERROR: Database is not Initialized. Enter 'i' to intialize database");
                }

        }


public MyDatabase() {

        Properties prop = new Properties();
        String fileName = "auth.cfg";
        try {
            FileInputStream configFile = new FileInputStream(fileName);
            prop.load(configFile);
            configFile.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Could not find config file.");
            System.exit(1);
        } catch (IOException ex) {
            System.out.println("Error reading config file.");
            System.exit(1);
        }
        String username = (prop.getProperty("username"));
        String password = (prop.getProperty("password"));

        if (username == null || password == null){
            System.out.println("Username or password not provided.");
            System.exit(1);
        }

        String connectionUrl =
                "jdbc:sqlserver://uranium.cs.umanitoba.ca:1433;"
                + "database=cs3380;"
                + "user=" + username + ";"
                + "password="+ password +";"
                + "encrypt=false;"
                + "trustServerCertificate=false;"
                + "loginTimeout=30;";


                try {
                        // create a connection to the database
                        connection = DriverManager.getConnection(connectionUrl);
                } catch (SQLException e) {
                        e.printStackTrace(System.out);
                }

                proceed = false;

        }









        public void intializeDatabase()
        {
                System.out.println("\nIntializing the Database  :Estimated Time: 1 Minute");

                ResultSet resultSet = null;
                proceed = true;

        try {

        String sql = "drop table if EXISTS country;";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.execute();

        sql = "drop table if exists continent;";
        statement = connection.prepareStatement(sql);
        statement.execute();

        sql = "drop table if exists happinessInfo;";
        statement = connection.prepareStatement(sql);
        statement.execute();

        sql = "drop table if EXISTS cuisine;";
        statement = connection.prepareStatement(sql);
        statement.execute();

        sql = "drop table if EXISTS restaurant;";
        statement = connection.prepareStatement(sql);
        statement.execute();

        sql = "drop table if EXISTS address;";
        statement = connection.prepareStatement(sql);
        statement.execute();

        sql = "drop table if EXISTS rating;";
        statement = connection.prepareStatement(sql);
        statement.execute();

        sql = "drop table if EXISTS pricerange;";
        statement = connection.prepareStatement(sql);
        statement.execute();

        sql = "drop table if exists currency;";
        statement = connection.prepareStatement(sql);
        statement.execute();

        sql = "drop TABLE if EXISTS countrycode;";
        statement = connection.prepareStatement(sql);
        statement.execute();



        sql = "create table countrycode (countryCode integer primary key ,country varchar(300));";
        statement = connection.prepareStatement(sql);
statement.execute();

        sql = "create table currency(currency VARCHAR(300) unique,date VARCHAR(300),currencyValue REAL not NULL,primary key(currency , date));";
        statement = connection.prepareStatement(sql);
        statement.execute();

        sql = "create table pricerange(averageCostForTwo integer,currency varchar(300) FOREIGN key REFERENCES currency(currency) on delete cascade,priceRange integer primary key(averageCostForTwo , currency));";
        statement = connection.prepareStatement(sql);
        statement.execute();

        sql = "create table rating(aggregateRating Real primary key,ratingColor varchar(300),ratingText varchar(300));";
        statement = connection.prepareStatement(sql);
        statement.execute();

        sql = "create table address(address varchar(300) primary key,countryCode INTEGER FOREIGN key REFERENCES countrycode on delete cascade,city varchar(300),locality varchar(300),localityVerbose varchar(300));";
        statement = connection.prepareStatement(sql);
        statement.execute();

        sql = "create table restaurant (restaurantID integer PRIMARY KEY,restaurantName varchar(300),address varchar(300) foreign key references address on delete cascade,averageCostForTwo integer,hasTableBooking varchar(300), hasOnlineDelivery varchar(300),isDeliveringNow varchar(300),switchToOrderMenu varchar(300),aggregateRating Real foreign key REFERENCES rating on delete cascade,votes INTEGER);";
        statement = connection.prepareStatement(sql);
        statement.execute();

        sql = "create table cuisine(restaurantID INTEGER foreign KEY REFERENCES restaurant  on DELETE CASCADE,cuisineName VARCHAR(300),primary key(restaurantID , cuisineName));";
        statement = connection.prepareStatement(sql);
        statement.execute();

        sql = "create table happinessInfo(happinessScore Real PRIMARY key,happinessRank integer);";
        statement = connection.prepareStatement(sql);
        statement.execute();

        sql = "create table continent(continent VARCHAR(300) PRIMARY KEY,area INTEGER );";
        statement = connection.prepareStatement(sql);
        statement.execute();


         sql = "create table country( country VARCHAR(300) PRIMARY KEY, currency varchar(300), population integer, date varchar(300) , continent varchar(300) foreign key references continent on delete cascade, happinessScore Real FOREIGN key REFERENCES happinessInfo on delete cascade, foreign key (currency , date) REFERENCES currency (currency , date) on DELETE CASCADE ); ";
        statement = connection.prepareStatement(sql);
        statement.execute();

/*
        sql = "Delete from continent;";
        statement = connection.prepareStatement(sql);
        statement.execute();
        */

        sql = "INSERT INTO continent (continent , area ) VALUES (?, ?)";
        statement = connection.prepareStatement(sql);

        BufferedReader line = new BufferedReader(new FileReader("continent.csv"));
        String workLine = null;

        workLine=line.readLine();

        while((workLine= line.readLine()) != null)
        {

                String[] data = workLine.split(",");
                String continentName = data[0];
                String area = data[1];
                int areaInt = Integer.parseInt(area);

                statement.setString( 1 , continentName);
                statement.setInt( 2 , areaInt);
                statement.execute();

        }



        /*
        sql = "Delete from countrycode;";
        statement = connection.prepareStatement(sql);
         statement.execute();
*/

        sql = "INSERT INTO countrycode (countryCode , country ) VALUES (? , ?)";
        statement = connection.prepareStatement(sql);
        line = new BufferedReader(new FileReader("countrycode.csv"));
        workLine = null;

        line.readLine();

        while((workLine= line.readLine()) != null)
        {

                String[] data = workLine.split(",");
                String countryCode = data[0];
                String  country = data[1];
                int code = Integer.parseInt(countryCode);

                statement.setInt( 1 , code);
                statement.setString( 2 , country);
                statement.execute();

        }

/*
        sql = "Delete from happinessInfo;";
         statement = connection.prepareStatement(sql);
         statement.execute();
*/
        sql = "INSERT INTO happinessInfo (happinessScore, happinessRank) VALUES (? , ?)";
        statement = connection.prepareStatement(sql);
        line = new BufferedReader(new FileReader("happinessInfo.csv"));
        workLine = null;

        line.readLine();

        while((workLine= line.readLine()) != null)
        {

                String[] data = workLine.split(",");
                String happScore = data[0];
                String happRank = data[1];
                float happScoree = Float.parseFloat(happScore);
                int happRankk = Integer.parseInt(happRank);
                statement.setFloat( 1 , happScoree);
                statement.setInt( 2 , happRankk);
                statement.execute();

        }





/*
          sql = "Delete from currency;";
        statement = connection.prepareStatement(sql);
         statement.execute();
*/
sql = "INSERT INTO currency (currency , date, currencyValue ) VALUES (? , ? , ?)";
        statement = connection.prepareStatement(sql);
        line = new BufferedReader(new FileReader("currency.csv"));
        workLine = null;

        line.readLine();

        while((workLine= line.readLine()) != null)
        {

                String[] data = workLine.split(",");
                String currency = data[0];
                String  date = data[1];
                String value = data[2];
                float val = Float.parseFloat(value);

                statement.setString( 1 , currency);
                statement.setString( 2 , date);
                statement.setFloat( 3, val);
                statement.execute();

        }




/*
         sql = "Delete from pricerange;";
        statement = connection.prepareStatement(sql);
         statement.execute();
*/

        sql = "INSERT INTO pricerange ( averageCostForTwo , currency , priceRange ) VALUES (? , ? , ?)";
        statement = connection.prepareStatement(sql);
        line = new BufferedReader(new FileReader("pricerange.csv"));
        workLine = null;

        line.readLine();

        while((workLine= line.readLine()) != null)
        {

                String[] data = workLine.split(",");
                String avg = data[0];
                String  cur = data[1];
                String range = data[2];
                int avgg = Integer.parseInt(avg);
                int rangee = Integer.parseInt(range);

                statement.setInt( 1 , avgg);
                statement.setString( 2 , cur);
                statement.setInt( 3, rangee);
                statement.execute();

        }



/*
          sql = "Delete from rating;";
        statement = connection.prepareStatement(sql);
         statement.execute();
*/

        sql = "INSERT INTO rating (aggregateRating , ratingColor , ratingText ) VALUES (? , ? , ?)";
        statement = connection.prepareStatement(sql);
        line = new BufferedReader(new FileReader("rating.csv"));
        workLine = null;

        line.readLine();

        while((workLine= line.readLine()) != null)
        {

                String[] data = workLine.split(",");
                String ag = data[0];
                String  col = data[1];
                String tex = data[2];
                float agg = Float.parseFloat(ag);

                statement.setFloat( 1 , agg);
                statement.setString( 2 , col);
                statement.setString( 3, tex);
                statement.execute();

        }



/*
        sql = "Delete from country;";
        statement = connection.prepareStatement(sql);
         statement.execute();
*/

sql = "INSERT INTO country (country , currency , population, date, continent, happinessScore ) VALUES (? , ? , ?, ?, ? ,?)";
        statement = connection.prepareStatement(sql);
        line = new BufferedReader(new FileReader("country.csv"));
        workLine = null;

        line.readLine();

        while((workLine= line.readLine()) != null)
        {

                String[] data = workLine.split(",");
                String country = data[0];
                String  curr = data[1];
                String pop = data[2];
                String date = data[3];
                String cont = data[4];
                String hap = data[5];
                int popp = Integer.parseInt(pop);
                float happ = Float.parseFloat(hap);

                statement.setString( 1 , country);
                statement.setString( 2 , curr);
                statement.setInt( 3, popp);
                statement.setString(4, date);
                statement.setString(5 , cont);
                statement.setFloat(6 , happ);
                statement.execute();

        }



/*
        sql = "Delete from address;";
        statement = connection.prepareStatement(sql);
         statement.execute();
*/

        sql = "INSERT INTO address ( address, countryCode , city, locality, localityVerbose ) VALUES (? , ? , ?, ? , ?)";
        statement = connection.prepareStatement(sql);
        line = new BufferedReader(new FileReader("address.csv"));
        workLine = null;

        line.readLine();

        while((workLine= line.readLine()) != null)
        {

                String[] data = workLine.split(",");
                String add = data[0];
                String code = data[1];
                String city = data[2];
                String loca = data[3];
                String verb = data[4];
                int codee = Integer.parseInt(code);

                statement.setString( 1 , add);
                statement.setInt( 2 , codee);
                statement.setString( 3, city);
                statement.setString( 4, loca);
                 statement.setString(5, verb);
                statement.execute();

        }




/*
        sql = "Delete from restaurant;";
        statement = connection.prepareStatement(sql);
         statement.execute();
*/

 sql = "INSERT INTO restaurant (restaurantID , restaurantName , address, averageCostForTwo , hasTableBooking , hasOnlineDelivery , isDeliveringNow, switchToOrderMenu, aggregateRating, votes ) VALUES (? , ? , ?, ? ,? , ? ,? ,? ,? ,?)";
        statement = connection.prepareStatement(sql);
        line = new BufferedReader(new FileReader("restaurant.csv"));
        workLine = null;

        line.readLine();

        while((workLine= line.readLine()) != null)
        {

                String[] data = workLine.split(",");
                String ID = data[0];
                String rname= data[1];
                String radd = data[2];
                String cost = data[3];
                String table = data[4];
                String online = data[5];
                String now = data[6];
                String menu = data[7];
                String aggrat = data[8];
                String vote = data[9];
                float aggratt = Float.parseFloat(aggrat);
                int votee = Integer.parseInt(vote);
                int costt = Integer.parseInt(cost);
                int IDD = Integer.parseInt(ID);

                statement.setInt( 1 , IDD);
                statement.setString( 2 , rname);
                statement.setString( 3, radd);
                statement.setInt( 4, costt);
                statement.setString(5, table);
                statement.setString(6, online);
                statement.setString(7, now);
                statement.setString(8, menu);
                statement.setFloat(9, aggratt);
                statement.setInt(10, votee);

                statement.execute();

        }




/*
        sql = "Delete from cuisine;";
        statement = connection.prepareStatement(sql);
         statement.execute();
*/

        sql = "INSERT INTO cuisine (restaurantID , cuisineName ) VALUES (? , ?)";
        statement = connection.prepareStatement(sql);
        line = new BufferedReader(new FileReader("cuisine.csv"));
        workLine = null;

        line.readLine();

        while((workLine= line.readLine()) != null)
        {

                String[] data = workLine.split(",");
                String rID = data[0];
                String cName = data[1];
                int rIDD = Integer.parseInt(rID);

                statement.setInt( 1 , rIDD);
                statement.setString( 2 , cName);

                statement.execute();

        }



        }
        catch (IOException e)
        {
                System.err.println(e);
        }

        catch (SQLException e)
        {
                e.printStackTrace();
        }


        System.out.println("\nSuccessfully intialized database\n");

    }

}
