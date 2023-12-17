import com.google.gson.Gson;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.*;
import java.util.List;

public class CurrencyConverter extends JFrame {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/currency";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "12345";
    private static final String DB_NAME = "currency";

    private static JLabel statusLabel;
    private JTextArea coinInfoTextArea;
    private JComboBox<String> coin1ComboBox;
    private JTextField quantityTextField;
    private JComboBox<String> coin2ComboBox;
    private JButton calculateButton;
    private CurrencyConverter program;

    public CurrencyConverter() {
        setTitle("Coin Program");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        statusLabel = new JLabel("Status: ");
        coinInfoTextArea = new JTextArea();
        coinInfoTextArea.setEditable(false);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());

        coin1ComboBox = new JComboBox<>();
        coin1ComboBox.setPreferredSize(new Dimension(100, 25));
        quantityTextField = new JTextField();
        quantityTextField.setPreferredSize(new Dimension(100, 25));
        coin2ComboBox = new JComboBox<>();
        coin2ComboBox.setPreferredSize(new Dimension(100, 25));
        calculateButton = new JButton("Calculate");

        inputPanel.add(coin1ComboBox);
        inputPanel.add(quantityTextField);
        inputPanel.add(coin2ComboBox);
        inputPanel.add(calculateButton);

        add(statusLabel, BorderLayout.NORTH);
        add(new JScrollPane(coinInfoTextArea), BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);

        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateConversion();
            }
        });

        pack();
        setLocationRelativeTo(null);
    }

    private void calculateConversion() {
        String coin1Symbol = coin1ComboBox.getSelectedItem().toString();
        BigDecimal coin1Price = getCoinPrice(coin1Symbol);

        if (coin1Price == null) {
            JOptionPane.showMessageDialog(this, "Invalid coin symbol. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        BigDecimal quantity;
        try {
            quantity = new BigDecimal(quantityTextField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid quantity. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String coin2Symbol = coin2ComboBox.getSelectedItem().toString();
        BigDecimal coin2Price = getCoinPrice(coin2Symbol);

        if (coin2Price == null) {
            JOptionPane.showMessageDialog(this, "Invalid coin symbol. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        BigDecimal conversionRate = coin1Price.divide(coin2Price, 8, BigDecimal.ROUND_HALF_UP);
        BigDecimal convertedAmount = quantity.multiply(conversionRate);

        JOptionPane.showMessageDialog(this, "Conversion rate: 1 " + coin1Symbol + " = " + conversionRate + " " + coin2Symbol + "\nConverted amount: " + convertedAmount, "Conversion Result", JOptionPane.INFORMATION_MESSAGE);
    }

    private BigDecimal getCoinPrice(String symbol) {
        String query = "SELECT price FROM coins WHERE symbol = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, symbol);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getBigDecimal("price");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void updateCoinInfo(Connection connection) {
        StringBuilder coinInfoBuilder = new StringBuilder();

        String query = "SELECT symbol, name FROM coins";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            coinInfoBuilder.append("Available coins:\n");
            while (resultSet.next()) {
                String symbol = resultSet.getString("symbol");
                String fullName = resultSet.getString("name");
                coinInfoBuilder.append(String.format("%-20s (%s)", fullName, symbol));

                if (resultSet.isLast() || resultSet.getRow() % 2 == 0) {
                    coinInfoBuilder.append("\n");
                } else {
                    coinInfoBuilder.append("\t");
                }

                // Add coin symbols to the combo boxes
                coin1ComboBox.addItem(symbol);
                coin2ComboBox.addItem(symbol);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        coinInfoTextArea.setText(coinInfoBuilder.toString());
        pack();
    }

    public void start() {

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            createDatabase(connection);
            createTable(connection);
            getCoinData(connection);
            updateCoinInfo(connection);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to connect to the database.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        setVisible(true);
    }

    private static void createDatabase(Connection connection) throws SQLException {
        String createDatabaseQuery = "CREATE DATABASE IF NOT EXISTS " + DB_NAME;

        try (PreparedStatement statement = connection.prepareStatement(createDatabaseQuery)) {
            statement.executeUpdate();
        }
    }

    private static void createTable(Connection connection) throws SQLException {
        String useDatabaseQuery = "USE " + DB_NAME;
        String dropTableQuery = "DROP TABLE IF EXISTS coins";
        String createTableQuery = "CREATE TABLE coins (" +
                "coin_rank INT PRIMARY KEY," +
                "name VARCHAR(255)," +
                "symbol VARCHAR(10)," +
                "price DECIMAL(18, 8)," +
                "time_added DATETIME DEFAULT CURRENT_TIMESTAMP" +
                ")";

        try (PreparedStatement statement = connection.prepareStatement(useDatabaseQuery)) {
            statement.executeUpdate();
        }

        try (PreparedStatement statement = connection.prepareStatement(dropTableQuery)) {
            statement.executeUpdate();
        }

        try (PreparedStatement statement = connection.prepareStatement(createTableQuery)) {
            statement.executeUpdate();
        }
    }

    public static class CoinResponse {
        private String status;
        private CoinData data;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public CoinData getData() {
            return data;
        }

        public void setData(CoinData data) {
            this.data = data;
        }
    }

    public static class CoinData {
        private List<Coin> coins;

        public List<Coin> getCoins() {
            return coins;
        }

        public void setCoins(List<Coin> coins) {
            this.coins = coins;
        }
    }

    public static class Coin {
        private String name;
        private String symbol;
        private BigDecimal price;
        private int rank;

        public Coin(String symbol, String name) {
            this.symbol = symbol;
            this.name = name;
        }

        public Coin(String symbol, String name, BigDecimal price) {
            this.symbol = symbol;
            this.name = name;
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }
    }

    private static void getCoinData(Connection connection) {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://coinranking1.p.rapidapi.com/coins?referenceCurrencyUuid=yhjMzLPhuIDl&timePeriod=24h&tiers%5B0%5D=1&orderBy=marketCap&orderDirection=desc&limit=50&offset=0"))
                .header("X-RapidAPI-Key", "2f73569cefmsh1d3a485eccfb956p1ea800jsn66163630d8e2")
                .header("X-RapidAPI-Host", "coinranking1.p.rapidapi.com")
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();

            Gson gson = new Gson();
            CoinResponse coinResponse = gson.fromJson(responseBody, CoinResponse.class);

            if (coinResponse != null && coinResponse.getStatus().equals("success")) {
                try {
                    for (Coin coin : coinResponse.getData().getCoins()) {
                        insertData(connection, coin.getName(), coin.getSymbol(), coin.getPrice().toString(), coin.getRank());
                    }
                    CurrencyConverter.statusLabel.setText("Status: Data successfully added to the database.");
                } catch (SQLException e) {
                    e.printStackTrace();
                    CurrencyConverter.statusLabel.setText("Status: Failed to add data to the database.");
                }
            } else {
                CurrencyConverter.statusLabel.setText("Status: Failed to get coin data from the API.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            CurrencyConverter.statusLabel.setText("Status: Failed to get coin data from the API.");
        }
    }

    private static void insertData(Connection connection, String name, String symbol, String price, int rank) throws SQLException {
        String insertQuery = "INSERT INTO coins (name, symbol, price, coin_rank) VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
            statement.setString(1, name);
            statement.setString(2, symbol);
            statement.setString(3, price);
            statement.setInt(4, rank);

            statement.executeUpdate();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                CurrencyConverter program = new CurrencyConverter();
                program.start();
            }
        });
    }
}