package sample;

import Algorithms.BreadthFirstSearch;
import Algorithms.DepthFirstSearch;
import Algorithms.DijkstraSP;
import Graph.*;
import Graph.Vertex;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.concurrent.TimeUnit;

import java.util.ArrayList;
import java.util.Optional;

public class Graph_Canvas_Controller {



    @FXML
    private Button bAddOne;
    @FXML
    private Button bAddEdges;
    @FXML
    private Button clear;
    @FXML
    private Button bRunAlg;
    @FXML
    private Label messageBox;
    @FXML
    private ChoiceBox algSelect;
    @FXML
    private Button deleteNode;
    @FXML
    private Button deleteEdge;
    @FXML
    private Button stepBack;
    @FXML
    private Button stepForward;
    @FXML
    private Button pause;
    @FXML
    private Button play;
    @FXML
    private CheckBox directed;
    @FXML
    private CheckBox weighted;

    private Graph graph;
    final String[] algorithms = new String[]{"dfs", "bfs", "kruskals", "dijkstras"};
    private String currentAlgorithm;
    private Stage stage;
    private Pane root;

    private boolean addVertMode = false;
    private boolean addEdgeMode = false;

    private boolean selectNode1 = false;
    private boolean selectNode2 = false;

    private int selectedNodeID1 = -1;
    private int selectedNodeID2 = -1;

    private int nodeNum = 1;

    private boolean firstNodeSelected = false;
    private String firstNode = "";

    private ArrayList<ColorMatrix> states;
    private int statesSize;
    private int currentState;

    private Timeline timeline;

    @FXML
    public void initialize () {
        this.graph = new Graph();
        currentAlgorithm = "";

        timeline = new Timeline(new KeyFrame(
                Duration.millis(1000),
                ae -> processState()));
        timeline.setCycleCount(Animation.INDEFINITE);
        //timeline.play();

        algSelect = new ChoiceBox(FXCollections.observableArrayList(
                "Depth First Search", "Breadth First Search", "Kruskal's MST", "Dijkstra's SP")
        );
    }

    public void setParent(Pane root){
        this.root = root;
    }


    public void setHandlers () {
        this.directed.setOnMouseClicked(e->{
            //happens when directed box is clicked.
            this.getGraph().setDirected(!this.getGraph().isDirected());

        });
        this.weighted.setOnMouseClicked(e->{
            //happens when weighted box is clicked
            this.getGraph().setWeighted(!this.getGraph().isWeighted());
        });
        this.deleteNode.setOnMouseClicked(e -> {
            //delete node mode
            this.messageBox.setText("Click on a node to delete it.");

        });
        this.deleteEdge.setOnMouseClicked( e -> {
            //delete edge mode
            this.messageBox.setText("Click on an edge to delete it.");
        });
        this.stepBack.setOnMouseClicked(e->{
            //STEP BACK IN THE ANIMATION
            timeline.pause();
            if(currentState == 1){
                currentState--;
                processState();
            }
            else if(currentState > 1){
                currentState--;
                currentState--;
                processState();
            }
        });
        this.stepForward.setOnMouseClicked((e->{
            //STEP FORWARD IN THE ANIMATION

            timeline.pause();
            processState();

        }));
        this.pause.setOnMouseClicked(e->{
            //PAUSE THE ANIMATION UNTIL PLAY IS HIT. Perhaps do have a "isPlayingNow" boolean that is
            //set to false when pause is pressed and set to true when play is pressed. this
            //could dictate when to keep playing the animation
            timeline.pause();
        });
        this.play.setOnMouseClicked(e-> {
            //PLAY THE ANIMATION - maybe set the boolean play variable to true here

            timeline.play();
        });
        this.root.setOnKeyPressed(e->{
            if (e.getCode() == KeyCode.ESCAPE) {
                addVertMode = false;
                addEdgeMode = false;
                firstNodeSelected = false;
                this.selectNode1 = false;
                this.selectNode2 = false;
                this.messageBox.setText("No Mode Selected");
            }
        });
        this.bAddOne.setOnMouseClicked( e -> {
            //this.bAddOne.setText("Add Nodes");
            this.messageBox.setText("Add nodes by selecting a location on the graph and entering in a value.");
            this.addVertMode = true;
            this.addEdgeMode = false;
            this.selectNode1 = false;
            this.selectNode2 = false;
            /*
            else {
                this.bAddOne.setText("Not add Nodes Mode");
                this.addVertMode = !this.addVertMode;
            }
            */

        });
        this.clear.setOnMouseClicked( e-> {
            this.root.getChildren().removeAll(this.graph.getEdges());
            this.root.getChildren().removeAll(this.graph.getEdgeLabels());
            this.root.getChildren().removeAll(this.graph.getVertices());
            this.root.getChildren().removeAll(this.graph.getVertexLabels());
            this.graph = new Graph();
            this.messageBox.setText("Graph cleared. Create a new graph by adding new nodes!");
        });
        this.algSelect.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>(){
            public void changed(ObservableValue ov, Number value, Number new_value){
                currentAlgorithm = algorithms[new_value.intValue()];
            }
        });
        this.bRunAlg.setOnMouseClicked(e-> {
            //run the currentAlgorithm
            if(currentAlgorithm.equals("dfs")){
                this.messageBox.setText("Running Depth First Search! Select Start Node.");
                addVertMode = false;
                addEdgeMode = false;
                firstNodeSelected = false;
                this.selectNode2 = false;
                selectNode1 = true;
                //run dfs
                //ASK TO SPECIFY START NODE, then pass in that vertex to DFS
                //Vertex start =
                //DepthFirstSearch dfs = new DepthFirstSearch(this.graph, start);
                //for loop for showing the states stored in dfs as a list of graphs.
                //draw graph in the for loop for the length of the list
                //if they want to step, just wait for next mouse click on next arrow to show the step. do it in a while loop.
                //while not reached final step, show current graph and wait for a mouseclick on either "next" or "play to end" or something
            }
            else if(currentAlgorithm.equals("bfs")){
                this.messageBox.setText("Running Breadth First Search! Select Start Node.");
                addVertMode = false;
                addEdgeMode = false;
                firstNodeSelected = false;
                this.selectNode2 = false;
                selectNode1 = true;
                //run bfs
            }
            else if(currentAlgorithm.equals("kruskals")){
                this.messageBox.setText("Running Kruskal's Minimal Spanning Tree! Select Start Node.");
                addVertMode = false;
                addEdgeMode = false;
                firstNodeSelected = false;
                this.selectNode2 = false;
                selectNode1 = true;
                //run kruskals
            }
            else if(currentAlgorithm.equals("dijkstras")){
                this.messageBox.setText("Running Dijkstra's! Select Start Node.");
                addVertMode = false;
                addEdgeMode = false;
                firstNodeSelected = false;
                this.selectNode2 = false;
                selectNode1 = true;
                //run dijkstras
            }
        });
        this.root.setOnMouseClicked( (MouseEvent event) -> {
            if(addVertMode) {
                double xVal = event.getSceneX();
                double yVal = event.getSceneY();

                String nodeVal = getClosestNode(xVal,yVal);

                if(nodeVal == null) {
                    TextInputDialog dialog = new TextInputDialog("");
                    dialog.setTitle("Vertex Value");
                    //dialog.setHeaderText("Look, a Text Input Dialog");
                    dialog.setContentText("Please enter the vertex value:");

                    // Traditional way to get the response value.
                    String result = dialog.showAndWait().orElse("n/a");
                    if (result.matches("^[a-zA-Z0-9]*$") && this.graph.getVertex(result) == null && result.length() > 0) {
                        Vertex circ = this.graph.addVertex(xVal, yVal, 10);
                        circ.setValue(result);
                        /*circ.setOnMouseDragged(e -> {
                            circ.setCenterX(e.getSceneX());
                            circ.setCenterY(e.getSceneY());
                            for(Edge edge: graph.getEdges()){
                                if (edge.getFrom() == circ ) {
                                    edge.setStartX(circ.getCenterX());
                                    edge.setStartY(circ.getCenterY());
                                }
                                else if(edge.getTo() == circ) {
                                    edge.setEndX(circ.getCenterX());
                                    edge.setEndY(circ.getCenterY());
                                }
                            }
                        });*/

                        Text text = this.graph.addVertexText(circ, result);
                        this.root.getChildren().add(text);
                        this.root.getChildren().add(circ);
                        nodeNum++;

                        System.out.println(this.graph);
                    }
                }
            }
            else if(addEdgeMode) {
                double xVal = event.getSceneX();
                double yVal = event.getSceneY();

                String nodeVal = getClosestNode(xVal,yVal);

                if(nodeVal != null && nodeVal.length() > 0 && !firstNodeSelected){
                    firstNode = nodeVal;
                    firstNodeSelected = true;
                    this.messageBox.setText("Select an ending node for the edge.");
                }
                else if(nodeVal != null && nodeVal.length() > 0 && this.getGraph().isWeighted()){
                    TextInputDialog dialog = new TextInputDialog("1");
                    dialog.setTitle("Edge Weight");
                    //dialog.setHeaderText("Look, a Text Input Dialog");
                    dialog.setContentText("Please the edge weight:");

                    // Traditional way to get the response value.
                    String result = dialog.showAndWait().orElse("n/a");
                    if (result.matches("^[1-9]\\d*$") && result.length() > 0){
                        Edge edge = this.graph.addEdge(firstNode, nodeVal, Integer.parseInt(result), this.getGraph().isDirected() ? false : true);

                        if (edge != null) {

                            Text text = this.graph.addEdgeText(edge, result);
                            this.root.getChildren().add(text);
                            this.root.getChildren().add(edge);
                        }
                        firstNodeSelected = false;
                        firstNode = "";
                        this.messageBox.setText("Edge created! To add more edges, select another starting node.");
                        if (edge == null)
                            this.messageBox.setText("That edge already exists, select another starting node.");
                    }
                    else{
                        firstNodeSelected = false;
                        firstNode = "";
                        this.messageBox.setText("That edge was not created, select another starting node.");
                    }
                }
                else if(nodeVal != null && nodeVal.length() > 0 ){
                    Edge edge = this.graph.addEdge(firstNode, nodeVal, 1, this.getGraph().isDirected() ? false : true);

                    if (edge != null) {
                        Text text = this.graph.addEdgeText(edge, "");
                        this.root.getChildren().add(text);
                        this.root.getChildren().add(edge);
                    }
                    firstNodeSelected = false;
                    firstNode = "";
                    this.messageBox.setText("Edge created! To add more edges, select another starting node.");
                    if (edge == null)
                        this.messageBox.setText("That edge already exists, select another starting node.");

                }
                System.out.println(this.graph);
            }
            else if(selectNode1) {
                double xVal = event.getSceneX();
                double yVal = event.getSceneY();

                String nodeVal = getClosestNode(xVal,yVal);

                if(nodeVal != null && nodeVal.length() > 0){
                    this.selectedNodeID1 = Integer.valueOf(this.graph.getVertex(nodeVal).getId());

                    if(currentAlgorithm.equals("dfs") || currentAlgorithm.equals("bfs") || currentAlgorithm.equals("dijkstras")){
                        this.selectNode1 = false;
                        this.selectNode2 = true;
                        this.messageBox.setText("Select the node you want to find.");
                    }
                    else if(currentAlgorithm.equals("kruskals")){
                        //start the algorithm by passing selectedNodeID1 to the algorithm
                        this.selectNode1 = false;
                    }
                }
            }
            else if(selectNode2){
                double xVal = event.getSceneX();
                double yVal = event.getSceneY();

                String nodeVal = getClosestNode(xVal,yVal);

                if(nodeVal != null && nodeVal.length() > 0){
                    this.selectedNodeID2 = Integer.valueOf(this.graph.getVertex(nodeVal).getId());

                    if(currentAlgorithm.equals("dfs") || currentAlgorithm.equals("bfs") || currentAlgorithm.equals("dijkstras")){
                        this.selectNode1 = false;
                        this.selectNode2 = false;

                        //start the algorithm by passing both nodesIDs to the algorithm.
                        if(currentAlgorithm.equals("dfs")){
                            DepthFirstSearch dfs = new DepthFirstSearch
                                    (this.getGraph(), this.getGraph().getVertex(this.selectedNodeID1),
                                            this.getGraph().getVertex(this.selectedNodeID2));
                            //show visualization from arraylist returned
                            states = dfs.getStates();
                            this.messageBox.setText("Running Depth First Search!");
                            statesSize = states.size();
                            currentState = 0;

                            //timeline.play();
                            processState();

                            System.out.println(dfs.getVisited());

                            /*this.root.getChildren().removeAll(this.graph.getEdges());
                            this.root.getChildren().removeAll(this.graph.getVertices());

                            for(int j = 0; j < states.get(0).getNumVertices(); j++){
                                for(int k = 0; k < states.get(0).getNumVertices(); k++){
                                    this.root.getChildren().removeAll(this.graph.getEdges());
                                    this.root.getChildren().removeAll(this.graph.getVertices());
                                }
                            }*/
                        }
                        else if(currentAlgorithm.equals("bfs")){
                            BreadthFirstSearch bfs = new BreadthFirstSearch
                                    (this.getGraph(), this.getGraph().getVertex(this.selectedNodeID1),
                                            this.getGraph().getVertex(this.selectedNodeID2));
                            //show visualization from arraylist returned
                            states = bfs.getStates();
                            System.out.println(bfs.getVisitedNodes());
                            this.messageBox.setText("Running Breadth First Search!");
                            statesSize = states.size();
                            currentState = 0;

                            //timeline.play();
                            processState();

                            //System.out.println(bfs.getVisited());

                            /*this.root.getChildren().removeAll(this.graph.getEdges());
                            this.root.getChildren().removeAll(this.graph.getVertices());

                            for(int j = 0; j < states.get(0).getNumVertices(); j++){
                                for(int k = 0; k < states.get(0).getNumVertices(); k++){
                                    this.root.getChildren().removeAll(this.graph.getEdges());
                                    this.root.getChildren().removeAll(this.graph.getVertices());
                                }
                            }*/
                        }
                        else if(currentAlgorithm.equals("dijkstras")){
                            DijkstraSP dsp = new DijkstraSP
                                    (this.getGraph(), this.getGraph().getVertex(this.selectedNodeID1),
                                            this.getGraph().getVertex(this.selectedNodeID2));
                            //show visualization from arraylist returned
                            states = dsp.getStates();
                            //System.out.println(dsp.getVisited());
                            this.messageBox.setText("Running Dijkstra's Shortest Path Search!");
                            statesSize = states.size();
                            currentState = 0;

                            //timeline.play();
                            processState();

                            //System.out.println(bfs.getVisited());

                            /*this.root.getChildren().removeAll(this.graph.getEdges());
                            this.root.getChildren().removeAll(this.graph.getVertices());

                            for(int j = 0; j < states.get(0).getNumVertices(); j++){
                                for(int k = 0; k < states.get(0).getNumVertices(); k++){
                                    this.root.getChildren().removeAll(this.graph.getEdges());
                                    this.root.getChildren().removeAll(this.graph.getVertices());
                                }
                            }*/
                        }
                    }
                }
            }
        });
        this.bAddEdges.setOnMouseClicked( (MouseEvent event) -> {
            addEdgeMode = true;
            addVertMode = false;
            firstNodeSelected = false;
            firstNode = "";

            this.messageBox.setText("Select a starting node for the edge.");

            //addEdge();
        });
    }




    /*public void setbAddOne(JFXButton bAddOne) {
        this.bAddOne = bAddOne;
    }*/
    public void addEdge(){
        
        this.messageBox.setText("Select a starting node for the edge.");
        //wait for them to select a starting node
        this.messageBox.setText("Select an ending node for the edge.");
        //wait for them to select an ending node
        this.messageBox.setText("Edge created! To add more edges, select another starting node.");

    }
    public void setGraph (Graph graph) {
        this.graph = graph;
    }
    public Graph getGraph() {
        return this.graph;
    }
    public void setStage(Stage stage){
        this.stage = stage;
    }
    public Stage getStage() {
        return this.stage;
    }

    public void setupChoice() {
        algSelect.setLayoutX(14);
        algSelect.setLayoutY(406);
        algSelect.setMinWidth(108);
        algSelect.setPrefWidth(108);
        root.getChildren().add(algSelect);
    }

    private void processState(){

        if(currentState >= statesSize){
            timeline.pause();
            return;
        }

        this.root.getChildren().removeAll(this.graph.getEdges());
        this.root.getChildren().removeAll(this.graph.getVertices());

        for(int j = 0; j < states.get(currentState).getNumVertices(); j++){
            for(int k = 0; k < states.get(currentState).getNumVertices(); k++){
                System.out.print(states.get(currentState).getColorMatrix()[j][k] + "\t");

                if(j == k){
                    Vertex v = this.graph.getVertices().get(j);

                    if(v != null) {
                        if (states.get(currentState).getColorMatrix()[j][k] == 0)
                            v.setFill(Color.GRAY);
                        if (states.get(currentState).getColorMatrix()[j][k] == 1)
                            v.setFill(Color.BLACK);
                        if (states.get(currentState).getColorMatrix()[j][k] == 2)
                            v.setFill(Color.RED);
                        if (states.get(currentState).getColorMatrix()[j][k] == 3)
                            v.setFill(Color.GREEN);

                        this.root.getChildren().add(v);
                    }
                }
                else{
                    Vertex v1 = this.graph.getVertices().get(j);
                    Vertex v2 = this.graph.getVertices().get(k);
                    if(v1 != null && v2 != null) {
                        Edge e = this.graph.getEdge(v1, v2);
                        if(e != null) {
                            if (states.get(currentState).getColorMatrix()[j][k] == 0)
                                e.setStroke(Color.GRAY);
                            if (states.get(currentState).getColorMatrix()[j][k] == 1)
                                e.setStroke(Color.BLACK);
                            if (states.get(currentState).getColorMatrix()[j][k] == 2)
                                e.setStroke(Color.RED);
                            if (states.get(currentState).getColorMatrix()[j][k] == 3)
                                e.setStroke(Color.GREEN);
                            this.root.getChildren().add(e);
                        }
                    }
                }
            }
            System.out.println(states.size());
        }
        currentState++;
    }

    private String getClosestNode(double xVal, double yVal){
        Point2D clickPoint = new Point2D(xVal, yVal);

        String closestCircle = "";
        double closestDistance = -1;

        for(int i = 0; i < nodeNum; i++){
            Vertex v = this.graph.getVertex(i+1);

            if(v != null){
                Point2D circlePoint = new Point2D(v.getCenterX(), v.getCenterY());

                double distance = clickPoint.distance(circlePoint);
                if(distance < 17.0 && (distance < closestDistance || closestDistance == -1)){
                    closestCircle = v.getValue();
                    closestDistance = distance;
                }
            }

        }

        if(closestDistance > -1) return closestCircle;
        return null;
    }
}
