import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

class Stock {
    private String ticker;
    private double currentPrice;

    public Stock(String ticker, double currentPrice) {
        this.ticker = ticker;
        this.currentPrice = currentPrice;
    }

    public String getTicker() {
        return ticker;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }
}

class StockMarket {
    private HashMap<String, Stock> stockList;

    public StockMarket() {
        stockList = new HashMap<>();
        stockList.put("AAPL", new Stock("AAPL", 150.0));
        stockList.put("GOOGL", new Stock("GOOGL", 2800.0));
        stockList.put("AMZN", new Stock("AMZN", 3400.0));
        stockList.put("MSFT", new Stock("MSFT", 290.0));
    }

    public void refreshPrices() {
        Random random = new Random();
        for (Stock stock : stockList.values()) {
            double fluctuation = random.nextDouble() * 10 - 5;
            stock.setCurrentPrice(stock.getCurrentPrice() + fluctuation);
        }
    }

    public Stock getStockByTicker(String ticker) {
        return stockList.get(ticker);
    }

    public void addNewStock(String ticker, double price) {
        stockList.put(ticker, new Stock(ticker, price));
        System.out.println("Added new stock: " + ticker + " at $" + price);
    }

    public void showMarketData() {
        System.out.println("Current Market Data:");
        for (Stock stock : stockList.values()) {
            System.out.printf("%s: $%.2f%n", stock.getTicker(), stock.getCurrentPrice());
        }
    }
}

class Portfolio {
    private HashMap<String, Integer> stockHoldings;
    private double availableCash;

    public Portfolio(double initialCash) {
        stockHoldings = new HashMap<>();
        availableCash = initialCash;
    }

    public void purchaseStock(String ticker, int quantity, double price) {
        if (quantity <= 0) {
            System.out.println("Quantity must be positive.");
            return;
        }
        if (availableCash < price * quantity) {
            System.out.println("Not enough cash to buy " + quantity + " shares of " + ticker);
            return;
        }

        stockHoldings.put(ticker, stockHoldings.getOrDefault(ticker, 0) + quantity);
        availableCash -= price * quantity;
        System.out.println("Bought " + quantity + " shares of " + ticker);
    }

    public void sellStock(String ticker, int quantity, double price) {
        if (quantity <= 0) {
            System.out.println("Quantity must be positive.");
            return;
        }
        if (stockHoldings.getOrDefault(ticker, 0) < quantity) {
            System.out.println("Not enough shares to sell " + quantity + " shares of " + ticker);
            return;
        }

        stockHoldings.put(ticker, stockHoldings.get(ticker) - quantity);
        availableCash += price * quantity;
        System.out.println("Sold " + quantity + " shares of " + ticker);
    }

    public void displayPortfolio(StockMarket market) {
        System.out.println("Current Portfolio:");
        double totalValue = availableCash;
        for (String ticker : stockHoldings.keySet()) {
            int quantity = stockHoldings.get(ticker);
            double price = market.getStockByTicker(ticker).getCurrentPrice();
            double value = quantity * price;
            totalValue += value;
            System.out.printf("%s: %d shares @ $%.2f each = $%.2f%n", ticker, quantity, price, value);
        }
        System.out.printf("Cash: $%.2f%n", availableCash);
        System.out.printf("Total Portfolio Value: $%.2f%n", totalValue);
    }
}

public class TradingPlatform {
    public static void main(String[] args) {
        StockMarket stockMarket = new StockMarket();
        Portfolio userPortfolio = new Portfolio(10000.0);
        Scanner inputScanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n1. Show Market Data");
            System.out.println("2. Purchase Stock");
            System.out.println("3. Sell Stock");
            System.out.println("4. View Portfolio");
            System.out.println("5. Refresh Market Prices");
            System.out.println("6. Add Stock to Market");
            System.out.println("7. Exit");
            System.out.print("Select your choice: ");

            try {
                int userChoice = inputScanner.nextInt();

                switch (userChoice) {
                    case 1:
                        stockMarket.showMarketData();
                        break;
                    case 2:
                        System.out.print("Enter stock ticker to buy: ");
                        String buyTicker = inputScanner.next().toUpperCase();
                        System.out.print("Enter quantity to buy: ");
                        int buyQuantity = inputScanner.nextInt();
                        Stock buyStock = stockMarket.getStockByTicker(buyTicker);
                        if (buyStock != null) {
                            userPortfolio.purchaseStock(buyTicker, buyQuantity, buyStock.getCurrentPrice());
                        } else {
                            System.out.println("Stock ticker not found.");
                        }
                        break;
                    case 3:
                        System.out.print("Enter stock ticker to sell: ");
                        String sellTicker = inputScanner.next().toUpperCase();
                        System.out.print("Enter quantity to sell: ");
                        int sellQuantity = inputScanner.nextInt();
                        Stock sellStock = stockMarket.getStockByTicker(sellTicker);
                        if (sellStock != null) {
                            userPortfolio.sellStock(sellTicker, sellQuantity, sellStock.getCurrentPrice());
                        } else {
                            System.out.println("Stock ticker not found.");
                        }
                        break;
                    case 4:
                        userPortfolio.displayPortfolio(stockMarket);
                        break;
                    case 5:
                        stockMarket.refreshPrices();
                        System.out.println("Market prices updated.");
                        break;
                    case 6:
                        System.out.print("Enter new stock ticker: ");
                        String newTicker = inputScanner.next().toUpperCase();
                        System.out.print("Enter initial price: ");
                        double newPrice = inputScanner.nextDouble();
                        stockMarket.addNewStock(newTicker, newPrice);
                        break;
                    case 7:
                        inputScanner.close();
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                inputScanner.next();
            }
        }
    }
}
