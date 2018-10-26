package webCrawler;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SpiderTest extends Application {
	public static int MAX_PAGES_TO_SEARCH = 0;
	public static Label results;
	public static String resultsString;
	
	private Group root;
	private Scene mainScene;
	private BorderPane outerBorderPane;
	private TabPane tabPane;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		root = new Group();
		mainScene = new Scene(root,650,425,Color.WHITE);
		outerBorderPane = new BorderPane();
		outerBorderPane.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
		tabPane = new TabPane();
		
		tabPane.prefHeightProperty().bind(mainScene.heightProperty());
		tabPane.prefWidthProperty().bind(mainScene.widthProperty());
		
		tabPane.getTabs().addAll(addWebCrawlerTab());//All tabs have to be added here to be displayed
		outerBorderPane.setCenter(tabPane);
		root.getChildren().add(outerBorderPane);
		
		primaryStage.setTitle("Web Crawler");
		primaryStage.setScene(mainScene);
		primaryStage.show();
	}

	private Tab addWebCrawlerTab() {
		Tab webCrawlerTab = new Tab();
		BorderPane border = new BorderPane();
		Scene scene = new Scene(border, 650, 400);
		
		HBox hbox = addHBox();
		border.setTop(hbox);
		border.setCenter(addGridPane());
		border.setBottom(addResults(border, scene));
		border.prefHeightProperty().bind(scene.heightProperty());
		border.prefWidthProperty().bind(scene.widthProperty());
		
		webCrawlerTab.setContent(border);
		webCrawlerTab.setText("Web Crawler");
		webCrawlerTab.setClosable(false);
		return webCrawlerTab;
	}
	
	private Node addResults(BorderPane border, Scene scene) {
		ScrollPane sp = new ScrollPane();
		sp.prefHeightProperty().bind(border.heightProperty());
		sp.prefWidthProperty().bind(border.widthProperty());
		sp.setMaxHeight(150);
		border.setBorder(new Border(
				new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		results = new Label();
		sp.setBackground(new Background(new BackgroundFill(Color.DARKCYAN, null, null)));
		sp.setStyle("-fx-background: darkcyan;" + "-fx-padding: 10;" + "-fx-border-style: solid inside;"
				+ "-fx-border-width: 2;" + "-fx-border-insets: 5;" + "-fx-border-radius: 5;"
				+ "-fx-border-color: black;");
		sp.setContent(results);
		return sp;
	}

	private Node addGridPane() {
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(0, 10, 0, 10));
		grid.setBackground(new Background(new BackgroundFill(Color.DARKCYAN, null, null)));

		Label website = new Label("Enter Website:");
		grid.add(website, 0, 1);

		TextField websiteTextField = new TextField();
		grid.add(websiteTextField, 1, 1);

		final Text websiteActionTarget = new Text();
		grid.add(websiteActionTarget, 1, 4);
		websiteActionTarget.setFill(Color.FIREBRICK);
		websiteTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (websiteTextField.getText().isEmpty())
				websiteActionTarget.setText("Please enter a website!");
			else
				websiteActionTarget.setText("");
		});

		Label maxNumberOfSitesToVisitLabel = new Label("Max number of websites");
		grid.add(maxNumberOfSitesToVisitLabel, 3, 1);

		TextField maxNumberOfSitesToVisit = new TextField();
		grid.add(maxNumberOfSitesToVisit, 4, 1);

		final Text maxWebsitesActionTarget = new Text();
		grid.add(maxWebsitesActionTarget, 1, 6);
		maxWebsitesActionTarget.setFill(Color.FIREBRICK);

		maxNumberOfSitesToVisit.textProperty().addListener((observable, oldValue, newValue) -> {
			if (maxNumberOfSitesToVisit.getText().isEmpty())
				maxWebsitesActionTarget.setText("Please enter the maximum number of websites!");
			else
				maxWebsitesActionTarget.setText("");
		});

		Label word = new Label("Enter word for search:");
		grid.add(word, 0, 2);

		TextField wordTextField = new TextField();
		grid.add(wordTextField, 1, 2);

		final Text wordActionTarget = new Text();
		grid.add(wordActionTarget, 1, 5);
		wordActionTarget.setFill(Color.FIREBRICK);
		wordTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (wordTextField.getText().isEmpty())
				wordActionTarget.setText("Please enter a word!");
			else
				wordActionTarget.setText("");
		});

		Button searchButton = new Button("Search");
		grid.add(searchButton, 1, 3);

		searchButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				resultsString = null;
				if (wordTextField.getText().isEmpty()) {
					wordActionTarget.setText("Please enter a word!");
				}
				if (websiteTextField.getText().isEmpty()) {
					websiteActionTarget.setText("Please enter a website!");
				}
				if (maxNumberOfSitesToVisit.getText().isEmpty()) {
					maxWebsitesActionTarget.setText("Please enter the maximum number of websites!");
				}
				if (!wordTextField.getText().isEmpty() && !websiteTextField.getText().isEmpty()
						&& !maxNumberOfSitesToVisit.getText().isEmpty()) {
					MAX_PAGES_TO_SEARCH = Integer.parseInt(maxNumberOfSitesToVisit.getText());
					Spider spider = new Spider();
					spider.search(websiteTextField.getText(), wordTextField.getText());
				}
			}
		});
		return grid;
	}

	private HBox addHBox() {
		HBox hbox = new HBox();
		hbox.setPadding(new Insets(15, 12, 15, 12));
		hbox.setSpacing(10);
		hbox.setStyle("-fx-background-color: lightgreen;");
		Text scenetitle = new Text("Welcome to Alex's Web Crawler");
		hbox.getChildren().addAll(scenetitle);
		return hbox;
	}

	public Label getResults() {
		return results;
	}

	public static void setResults(String string) {
		if (resultsString == null)
			resultsString = string;
		else
			resultsString += "\n" + string;
		results.setText(resultsString);
	}

	public static int getMaxPagesToSearch() {
		return MAX_PAGES_TO_SEARCH;
	}
}