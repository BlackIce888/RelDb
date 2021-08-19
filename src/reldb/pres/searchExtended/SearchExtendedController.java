/*
 * @author Andreas Diesendorf (2343841) <andreas_d@hotmail.de>, Björn Burzlaff (2874851) <bjoern.b96@web.de>
 */

package reldb.pres.searchExtended;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import reldb.app.SearchApp;
import reldb.bdo.CharacterFilter;
import reldb.bdo.ExtendedSearch;
import reldb.bdo.InfoFilter;
import reldb.bdo.PersonFilter;
import reldb.pres.ImdbSearch;
import reldb.pres.mainmenu.MainMenuController;
import reldb.pres.searchResults.SearchResultsController;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class SearchExtendedController implements Initializable {

    @FXML
    private TextField titleKeywordTF;
    @FXML
    private RadioButton matchAllRB;
    @FXML
    private RadioButton matchAnyRB;
    @FXML
    private RadioButton matchExactRB;
    @FXML
    public ToggleGroup matchGroup;
    @FXML
    public CheckBox movieCB;
    @FXML
    public CheckBox videoCB;
    @FXML
    public CheckBox tvMovieCB;
    @FXML
    public CheckBox tvSeriesCB;
    @FXML
    public CheckBox tvMiniSeriesCB;
    @FXML
    public CheckBox videoGameCB;
    @FXML
    public CheckBox episodeCB;
    @FXML
    private CheckBox selectAllCB;
    @FXML
    public GridPane kindGrid;
    @FXML
    public GridPane infoFilterGrid;
    @FXML
    public GridPane personGrid;
    @FXML
    public GridPane charNameGrid;
    @FXML
    public VBox genreBox;
    @FXML
    public CheckBox pyearCB;
    @FXML
    public CheckBox ratingCB;
    @FXML
    public CheckBox votesCB;
    @FXML
    public CheckBox budgetCB;
    @FXML
    public CheckBox runtimeCB;
    @FXML
    public TextField pYearFromTF;
    @FXML
    public TextField pYearToTF;
    @FXML
    public TextField ratingFromTF;
    @FXML
    public TextField ratingToTF;
    @FXML
    public TextField votesFromTF;
    @FXML
    public TextField votesToTF;
    @FXML
    public TextField budgetFromTF;
    @FXML
    public TextField budgetToTF;
    @FXML
    public TextField runtimeFromTF;
    @FXML
    public TextField runtimeToTF;

    private static SearchApp searches;
    private ImdbSearch application;
    private MainMenuController mainController;
    private ObservableList<String> genreList;
    private ObservableList<String> roleList;

    private Integer genreRowCounter = 0;
    private Integer roleRowCounter = 0;
    private Integer charRowCounter = 0;

    private Map<String, Boolean> validationMap = new HashMap<>();
    private Boolean isValidInput = true;

    /**
     * Initialization, generates and sets lists with genres and roles for choice boxes, defines event handler that
     * un-checks the select all checkbox when another checkbox is unset.
     *
     * @param url URL
     * @param rb ResourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        addTextFieldValidation(titleKeywordTF);
        initGenreList();
        initRolesList();
        EventHandler<ActionEvent> handler = event -> {
            if (selectAllCB.isSelected()) {
                    selectAllCB.setSelected(false);
            }
        };

        movieCB.setOnAction(handler);
        videoCB.setOnAction(handler);
        tvMovieCB.setOnAction(handler);
        tvSeriesCB.setOnAction(handler);
        tvMiniSeriesCB.setOnAction(handler);
        videoGameCB.setOnAction(handler);
        episodeCB.setOnAction(handler);
    }

    private void addTextFieldValidation(TextField keywordTF) {
        keywordTF.focusedProperty().addListener((listener, unselected, selected) -> {
            if (unselected) {
                if (keywordTF.getText().isEmpty()) {
                    keywordTF.setStyle("-fx-text-box-border: red; -fx-focus-color: red;");
                    if (!keywordTF.getId().isEmpty()) {
                        validationMap.put(keywordTF.getId(), false);
                    }
                } else {
                    keywordTF.setStyle("-fx-text-box-border: green; -fx-focus-color: green;");
                    if (!keywordTF.getId().isEmpty()) {
                        validationMap.put(keywordTF.getId(), true);
                    }
                }
            }
        });
    }

    public void setApp(ImdbSearch application, MainMenuController mainController) {
        this.application = application;
        this.mainController = mainController;
    }

    public void selectAll(ActionEvent event) {
        if(selectAllCB.isSelected()){
            movieCB.setSelected(true);
            videoCB.setSelected(true);
            tvMovieCB.setSelected(true);
            tvSeriesCB.setSelected(true);
            tvMiniSeriesCB.setSelected(true);
            videoGameCB.setSelected(true);
            episodeCB.setSelected(true);
        }else{
            movieCB.setSelected(false);
            videoCB.setSelected(false);
            tvMovieCB.setSelected(false);
            tvSeriesCB.setSelected(false);
            tvMiniSeriesCB.setSelected(false);
            videoGameCB.setSelected(false);
            episodeCB.setSelected(false);
        }
    }

    public void addGenreSelectionRow() {
        HBox container = new HBox();
        container.setPadding(new Insets(5,0,0,0));
        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        choiceBox.setValue(genreList.get(genreRowCounter));
        choiceBox.setItems(genreList);
        Button removeRowButton = new Button("-");
        removeRowButton.setOnAction(this::removeGenreSelectionRow);
        container.setSpacing(10);
        choiceBox.prefHeight(25);
        choiceBox.prefWidth(180);
        container.getChildren().addAll(choiceBox, removeRowButton);
        genreBox.getChildren().add(container);
        genreRowCounter++;
    }

    public void addRoleSelectionRow(ActionEvent event) {
        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        ChoiceBox<String> negationBox = addNegationBox();
        choiceBox.setValue(roleList.get(roleRowCounter));
        choiceBox.setItems(roleList);
        choiceBox.setPrefWidth(180.0);

        TextField roleKeywordTF = new TextField();
        roleKeywordTF.setPromptText("enter keyword or phrase");
        roleKeywordTF.setId("roleKeyword_" + roleRowCounter.toString());
        addTextFieldValidation(roleKeywordTF);

        Button removeRowButton = new Button("-");
        removeRowButton.setOnAction(this::removeRoleSelectionRow);

        roleRowCounter++;

        if (!roleRowCounter.equals(1)) {
            Label andLabel = generateAndLabel();
            personGrid.add(andLabel, 0, roleRowCounter);
        }

        personGrid.add(negationBox, 1, roleRowCounter);
        personGrid.add(choiceBox, 2, roleRowCounter);
        personGrid.add(roleKeywordTF, 3, roleRowCounter);
        personGrid.add(removeRowButton, 4, roleRowCounter);
        roleKeywordTF.requestFocus();
        roleKeywordTF.selectAll();

        if (roleRowCounter.equals(12)) {
            Object source = event.getSource();
            Button addButton = (Button) source;
            addButton.setDisable(true);
        }
    }

    public void addCharacterSelectionRow(ActionEvent event) {
        ChoiceBox<String> negationBox = addNegationBox();
        TextField charKeywordTF = new TextField();
        charKeywordTF.setPromptText("enter keyword or phrase");
        charKeywordTF.setId("charKeyword_" + charRowCounter.toString());
        addTextFieldValidation(charKeywordTF);

        Button removeRowButton = new Button("-");
        removeRowButton.setOnAction(this::removeCharacterSelectionRow);

        charRowCounter++;

        if (!charRowCounter.equals(1)) {
            Label andLabel = generateAndLabel();
            charNameGrid.add(andLabel, 0, charRowCounter);
        }
        charNameGrid.add(negationBox, 1, charRowCounter);
        charNameGrid.add(charKeywordTF, 2, charRowCounter);
        charNameGrid.add(removeRowButton, 3, charRowCounter);
        charKeywordTF.requestFocus();
        charKeywordTF.selectAll();

        if (charRowCounter.equals(10)) {
            Object source = event.getSource();
            Button addButton = (Button) source;
            addButton.setDisable(true);
        }
    }

    private ChoiceBox<String> addNegationBox() {
        ChoiceBox<String> negationBox = new ChoiceBox<>();
        ObservableList<String> negationList = FXCollections.observableArrayList();
        negationList.addAll("-", "NOT");
        negationBox.setValue(negationList.get(0));
        negationBox.setItems(negationList);
        negationBox.setPrefWidth(125.0);
        return negationBox;
    }

    private Label generateAndLabel() {
        Label andLabel = new Label("AND");
        andLabel.setUnderline(true);
        andLabel.setFont(new Font("System bold", 14));
        return andLabel;
    }

    private void removeGenreSelectionRow(ActionEvent event) {
        ObservableList<Node> nodes = genreBox.getChildren();
        Object source = event.getSource();
        Button removeButton = (Button) source;

        for (Object container : nodes) {
            if (container instanceof HBox) {
                ObservableList childList = ((HBox) container).getChildren();
                if (childList.contains(removeButton)) {
                    nodes.remove(container);
                    genreRowCounter--;
                    break;
                }
            }
        }
    }

    private void removeCharacterSelectionRow(ActionEvent event) {
        initRowRemoval(charNameGrid.getChildren(), event, charNameGrid);
        charRowCounter--;
    }

    private void removeRoleSelectionRow(ActionEvent event) {
        initRowRemoval(personGrid.getChildren(), event, personGrid);
        roleRowCounter--;
    }


    /**
     * Initiates row removal, initializes variables, and removes all row elements.
     * If add row button was disabled, it is enabled again.
     *
     * @param nodes ObservableList<Node>
     * @param event ActionEvent
     * @param grid GridPane
     */
    private void initRowRemoval(ObservableList<Node> nodes, ActionEvent event, GridPane grid) {
        Object source = event.getSource();
        Button removeButton = (Button) source;
        Integer rowIndex = GridPane.getRowIndex(removeButton);
        Collection<Node> elementsToRemove = new ArrayList<>();
        for (Node node : nodes) {
            if (node instanceof Button) {
                if (node.isDisabled()) {
                    node.setDisable(false);
                }
            }
            prepareRowElementsRemoval(rowIndex, elementsToRemove, node);
        }
        grid.getChildren().removeAll(elementsToRemove);
    }

    /**
     * Collects elements in a collection for removal.
     *
     * @param rowIndex Integer
     * @param elementsToRemove Collection<Node>
     * @param node Node
     */
    private void prepareRowElementsRemoval(Integer rowIndex, Collection<Node> elementsToRemove, Node node) {
        if (GridPane.getRowIndex(node) != null) {
            if (GridPane.getRowIndex(node).equals(rowIndex)) {
                elementsToRemove.add(node);
                if (node instanceof TextField) {
                    if (!node.getId().isEmpty()) {
                        validationMap.remove(node.getId());
                    }
                }
            } else if (GridPane.getRowIndex(node) > rowIndex) {
                GridPane.setRowIndex(node, GridPane.getRowIndex(node) - 1);
            }
        }
    }

    public void validateAndSearch(ActionEvent event) {
        validationMap.forEach((key, val) -> {
            if (!val) {
                this.isValidInput = false;
            }
        });

        if (this.isValidInput) {
            processSearch();
        } else {
            this.isValidInput = true;
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Input!");
            alert.setHeaderText("Keyword fields must be filled out!");
            alert.setX((application.getMainStage().getWidth() / 2));
            alert.setY((application.getMainStage().getHeight() / 2));
            alert.showAndWait();
        }
    }

    /**
     * User input is extracted from the search mask elements.
     * Input is processed and prepared, queries are created and executed.
     */
    private void processSearch() {

        searches = new SearchApp();

        int match = 0;
        if (matchAllRB.isSelected()) {
            match = 1;
        } else if (matchAnyRB.isSelected()) {
            match = 2;
        } else if (matchExactRB.isSelected()) {
            match = 3;
        }

        ExtendedSearch searchInput = new ExtendedSearch(titleKeywordTF.getText(), match);

        List<String> kindList = new ArrayList<>();

        if (!selectAllCB.isSelected()) {
            kindList = fetchSelectedTitleKinds(kindGrid.getChildren());
        }

        searchInput.setKindList(kindList);
        searchInput.setGenreList(fetchSelectedGenres(genreBox.getChildren()));
        searchInput.setInfoFilters(fetchInfoFilters(infoFilterGrid.getChildren()));
        searchInput.setPersonFilters(fetchPersonFilters(personGrid.getChildren()));
        searchInput.setCharFilters(fetchCharacterFilters(charNameGrid.getChildren()));

        System.out.println("Folgende Titel Keywords angegeben");
        System.out.println(searchInput.getTitleKeywords());
        System.out.println("---------------------------------");
        System.out.println("Matche nach: 1 - All, 2 - Any, 3 - Exact");
        System.out.println(searchInput.getMatch());
        System.out.println("---------------------------------");
        System.out.println("Folgende Titel Typen ausgewählt:");
        searchInput.getKindList().forEach(System.out::println);
        System.out.println("---------------------------------");
        System.out.println("Folgende Genres ausgewählt:");
        searchInput.getGenreList().forEach(System.out::println);
        System.out.println("---------------------------------");
        System.out.println("Folgende Titel Info Filter ausgewählt:");
        searchInput.getInfoFilters().forEach(filter -> {
            System.out.println("Row ID: " + filter.getRowId());
            System.out.println("Filter: " + filter.getFilter());
            System.out.println("Filter gewählt: " + filter.isSelected());
            System.out.println("Weniger als/Vor...: " + filter.getBeforeValue());
            System.out.println("Mehr als/Nach...: " + filter.getAfterValue());
            System.out.println("---------------------------------");
        });
        System.out.println("Folgende Personen Info Filter ausgewählt");
        searchInput.getPersonFilters().forEach(filter -> {
            System.out.println("Rollen Typ: " + filter.getRoleType());
            System.out.println("NOT: " + filter.isExcluded());
            System.out.println("Keywords: " + filter.getKeywords());
            System.out.println("---------------------------------");
        });
        System.out.println("Folgende Character Info Filter ausgewählt");
        searchInput.getCharFilters().forEach(filter -> {
            System.out.println("NOT: " + filter.isExcluded());
            System.out.println("Keywords: " + filter.getKeywords());
        });
        System.out.println("---------------------------------");
        System.out.println("---------------------------------");


        /* TODO Input is extracted, search queries are created, prepared statement needed for query execution */

        searches.searchExtendedTitle(searchInput);

        try{
            showResultsView();
        } catch(IOException e){
            System.out.println(e);
        }
    }

    public static void showResultsView() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ImdbSearch.class.getResource("searchResults/searchResults.fxml"));
        Parent searchResultsController = loader.load();

        SearchResultsController controller = loader.getController();

        Scene scene = new Scene(searchResultsController);
        Stage searchResultStage = new Stage();
        //modal.initModality(Modality.APPLICATION_MODAL);
        searchResultStage.setTitle("Suchergebnisse");
        searchResultStage.setMinWidth(250);
        searchResultStage.setScene(scene);
        controller.setStage(searchResultStage);

        searchResultStage.show();

        controller.setResults(searches.titleList, searches.personList, searches.companyList ,searches.charNameList);

    }

    private List<String> fetchSelectedTitleKinds(ObservableList<Node> nodeList) {
        List<String> selectedTitleKinds = new ArrayList<>();
        for (Node node : nodeList) {
            if (node instanceof CheckBox) {
                if (((CheckBox) node).isSelected()) {
                    selectedTitleKinds.add(((CheckBox) node).getText());
                }
            }
        }
        return selectedTitleKinds;
    }

    private List<String> fetchSelectedGenres(ObservableList<Node> genreNodes) {
        List<String> selectedGenres = new ArrayList<>();
        for (Node container : genreNodes) {
            if (container instanceof HBox) {
                for (Node node : (((HBox) container).getChildren())) {
                    if (node instanceof ChoiceBox) {
                        selectedGenres.add(((ChoiceBox) node).getValue().toString());
                    }
                }
            }
        }
        return selectedGenres;
    }

    private List<InfoFilter> fetchInfoFilters(ObservableList<Node> infoFilterGrid) {
        List<InfoFilter> infoFilterList = new ArrayList<>();
        InfoFilter infoFilter = new InfoFilter();
        Integer currentRow = null;

        for (Node node : infoFilterGrid) {
            Integer rowIndex = GridPane.getRowIndex(node);
            if (rowIndex != null) {
                if (!rowIndex.equals(currentRow)) {
                    if (currentRow != null) {
                        infoFilterList.add(infoFilter);
                        currentRow = rowIndex;
                        infoFilter = new InfoFilter();
                        infoFilter.setRowId(currentRow);
                        infoFilter.setFilter();
                    } else {
                        currentRow = rowIndex;
                        infoFilter.setRowId(currentRow);
                        infoFilter.setFilter();
                    }
                }
                if (node instanceof CheckBox) {
                    infoFilter.setSelected(((CheckBox) node).isSelected());
                }
                if (node instanceof TextField) {
                    if (!((TextField) node).getText().isEmpty()) {
                        Integer columnIndex = GridPane.getColumnIndex(node);
                        if (columnIndex.equals(2)) {
                            infoFilter.setAfterValue(((TextField) node).getText());
                        } else if (columnIndex.equals(4)) {
                            infoFilter.setBeforeValue(((TextField) node).getText());
                        }
                    }
                }
            }
        }
        infoFilterList.add(infoFilter);
        return infoFilterList;
    }

    private List<PersonFilter> fetchPersonFilters(ObservableList<Node> personFilterGrid) {
        List<PersonFilter> personFilterList = new ArrayList<>();
        PersonFilter personFilter = new PersonFilter();
        Integer currentRow = null;

        for (Node node : personFilterGrid) {
            Integer rowIndex = GridPane.getRowIndex(node);
            if (rowIndex != null) {
                if (!rowIndex.equals(currentRow)) {
                    if (currentRow != null) {
                        if (personFilter.getKeywords() != null) {
                            personFilterList.add(personFilter);
                            personFilter = new PersonFilter();
                        }
                    }
                }
                if (node instanceof TextField) {
                    if (!((TextField) node).getText().isEmpty()) {
                        personFilter.setKeywords(((TextField) node).getText());
                        currentRow = rowIndex;
                    }
                }
                if (node instanceof ChoiceBox) {
                    String choiceBoxValue = ((ChoiceBox) node).getValue().toString();
                    currentRow = rowIndex;
                    switch (choiceBoxValue) {
                        case "NOT":
                            personFilter.setExcluded(true);
                            break;
                        case "-":
                            personFilter.setExcluded(false);
                            break;
                        default:
                            personFilter.setRoleType(choiceBoxValue);
                            break;
                    }
                }
            }
        }
        if (personFilter.getKeywords() != null) {
            personFilterList.add(personFilter);
        }
        return personFilterList;
    }

    private List<CharacterFilter> fetchCharacterFilters(ObservableList<Node> characterFilterGrid) {
        List<CharacterFilter> characterFilterList = new ArrayList<>();
        CharacterFilter characterFilter = new CharacterFilter();
        Integer currentRow = null;

        for (Node node : characterFilterGrid) {
            Integer rowIndex = GridPane.getRowIndex(node);
            if (rowIndex != null) {
                if (!rowIndex.equals(currentRow)) {
                    if (currentRow != null) {
                        if (characterFilter.getKeywords() != null) {
                            characterFilterList.add(characterFilter);
                            characterFilter = new CharacterFilter();
                        }
                    }
                }
                if (node instanceof TextField) {
                    if (!((TextField) node).getText().isEmpty()) {
                        characterFilter.setKeywords(((TextField) node).getText());
                        currentRow = rowIndex;
                    }
                }
                if (node instanceof ChoiceBox) {
                    String choiceBoxValue = ((ChoiceBox) node).getValue().toString();
                    currentRow = rowIndex;
                    if (choiceBoxValue.equals("NOT")) {
                        characterFilter.setExcluded(true);
                    } else if (choiceBoxValue.equals("-")) {
                        characterFilter.setExcluded(false);
                    }
                }
            }
        }
        if (characterFilter.getKeywords() != null) {
            characterFilterList.add(characterFilter);
        }
        return characterFilterList;
    }

    private void initRolesList() {
        ObservableList<String> list = FXCollections.observableArrayList();
        list.addAll(
                "Actor",
                "Actress",
                "Producer",
                "Writer",
                "Cinematographer",
                "Composer",
                "Costume Designer",
                "Director",
                "Editor",
                "Miscellaneous Crew",
                "Production Designer",
                "Guest"
        );
        this.roleList = list;
    }

    private void initGenreList() {
        ObservableList<String> list = FXCollections.observableArrayList();
        list.addAll(
                "Experimental",
                "Western",
                "Animation",
                "War",
                "Adult",
                "History",
                "Musical",
                "Reality-TV",
                "Documentary",
                "Sci-Fi",
                "Short",
                "Thriller",
                "Film-Noir",
                "Biography",
                "Horror",
                "Action",
                "Comedy",
                "Erotica",
                "News",
                "Commercial",
                "Family",
                "Adventure",
                "Talk-Show",
                "Lifestyle",
                "Romance",
                "Drama",
                "Fantasy",
                "Sport",
                "Mystery",
                "Crime",
                "Music",
                "Game-Show",
                "_//bbfc.co.uk/releases/import-export-2008-0_"
        );
        this.genreList = list;
    }

    public void processSearchKeyword(ActionEvent actionEvent) {
        this.mainController.processSearchKeyword(actionEvent);
    }

    public void processExtendedSearch(ActionEvent actionEvent) {
        this.mainController.processExtendedSearch(actionEvent);
    }

    public void processQuit(ActionEvent actionEvent) {
        application.quit();
    }

    public void processViewRentLists(ActionEvent actionEvent) {
        this.mainController.processViewRentLists(actionEvent);
    }
}
