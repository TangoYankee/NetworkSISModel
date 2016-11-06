/*
Manage the objects and functions of the BART Sytem graph
 */
package bart.sql;

import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.PluggableGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.TranslatingGraphMousePlugin;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Random;
import java.util.Set;
import java.util.stream.IntStream;
import javax.swing.JFrame;


class graphData {
    private final Hashtable<String, nodeData> mNodes;//Collection of node objects
    private final Hashtable<String, edgeData> mEdges;//Collection of edge objects
    private final Graph<String, String> graph = new SparseMultigraph<>();//Graph object
    private final Random mRandomFactor = new Random();//Random number generator to determine whether node will heal/infect
    private final Set<String> mNodeKeys;//Create keychain for infection and healing functions
    private final SQLDatabase mSQLdatabase;//Hold connection to SQL database
    private VisualizationViewer<String, String> vv;
    private final ArrayList<String> mInfectKeys= new ArrayList<>();
    private final ArrayList<String> mHealKeys= new ArrayList<>();
    private final ArrayList<String> mMaintainKeys= new ArrayList<>();
    
    //Main Function 
    public graphData(Hashtable nodes, Hashtable edges, SQLDatabase sqldatabase) 
            throws SQLException{
        mNodes = nodes;
        mEdges = edges;
        mNodeKeys = mNodes.keySet();//Create a key chain for all the nodes
        mSQLdatabase = sqldatabase;
        populateGraph();//Initialize the graph
    }

    //Called by main function: Populates the graph from SQL data
    private void populateGraph()
            throws SQLException{
        try ( 
                ResultSet edges = mSQLdatabase.getEdgeData("bartlines")) {
            while(edges.next()){
                int id = edges.getInt("LineId");
                String EdgeID = "Edge"+Integer.toString(id);
                String nodeOne = edges.getString("nodeOne");
                String nodeTwo = edges.getString("nodeTwo");
                graph.addEdge(EdgeID, nodeOne, nodeTwo);
            }
            edges.close();
        }
    }
    
    //Called by Main Project Function to run the SIS model
    public void runSIS(infectionData infection, int iterations) 
            throws InterruptedException{
        int interval = 500;
        int numPatientZeroes = 3;
        visualizeGraph();      
        startOutbreak(numPatientZeroes);
        vv.repaint();
        Thread.sleep(interval);
        
        for(int x = 0; x<iterations; x++){
            infectGraph(infection);
            infectKeys();
            vv.repaint();
            Thread.sleep(interval);
            healGraph(infection);
            healKeys();
            maintainKeys();
            vv.repaint();
            Thread.sleep(interval);
        }  
    }
    
    //Called by runSIS; start the outbreak with a user-defined number of inititial infections
    private void startOutbreak(int patientZeroes){       
        String key;
        Object [] mNodeKeyArray = mNodeKeys.toArray();
        IntStream patientsRand = mRandomFactor.ints(patientZeroes, 1, mNodes.size());
        for(int patient: patientsRand.toArray()){
            key = mNodeKeyArray[patient].toString();
            mNodes.get(key).infectNode();
        }

    }

    //Function called by runSIS to determine which nodes will be infected
    private void infectGraph(infectionData infection){
        //Use every key to unlock each node
        mNodeKeys.stream().forEach((key) -> {
            //Get this node's neighbors
            Collection <String> neighbors = graph.getNeighbors(key);
            //Iterate through each neighbor of a node
            int infectedNeighbors = 0;
            for(String neighbor: neighbors){                
                if(mNodes.get(neighbor).getState().equals("Infected"))
                    infectedNeighbors++;
            }
            if(getChanceChanged(infection.getInfectionRate(), infectedNeighbors) > mRandomFactor.nextFloat())
                this.mInfectKeys.add(key);
        });

    }
    
    //Function called by infectGraph to execute infection of selected stations
    private void infectKeys(){
        this.mInfectKeys.stream().forEach((key) -> {
            mNodes.get(key).infectNode();
        });
        this.mInfectKeys.clear();
    }
    
    //Function called by runSIS to determine which nodes will be healed
    private void healGraph(infectionData infection){                
        mNodeKeys.stream().forEach((key) ->  {
            if (mNodes.get(key).getState().equals("Infected")){
                if(getChanceChanged(infection.getHealRate(), mNodes.get(key).getDaysInfected())> mRandomFactor.nextFloat()){
                    this.mHealKeys.add(key);
                }else{
                    this.mMaintainKeys.add(key);
                }
            }
        });
    }
    
    //Function called by healGraph to execute healing of selected stations
    private void healKeys(){
        this.mHealKeys.stream().forEach((key) -> {
            mNodes.get(key).healNode();
        });
        this.mHealKeys.clear();
        
    }
    
    //Function called by healGraph to add a day infected to unhealed nodes
    private void maintainKeys(){
        this.mMaintainKeys.stream().forEach((key) -> {
            mNodes.get(key).addDayInfected();
        });
        this.mMaintainKeys.clear();
    }
    
    //Called by infect/healNodes functions: Handle the odds of a station changing states;
    private double getChanceChanged(double changeRate, int numTrials){
        double chanceChanged = 1-Math.pow(1-changeRate, numTrials);
        return(chanceChanged);
    }    

    //Called by runSIS to visualize progress of infection
    private void visualizeGraph(){
        //Make a layout and visualizer where the nodes can have a fixed position
        StaticLayout<String, String> layout = new StaticLayout<>(
        graph, new locationTransformer(mNodes));
        Dimension dimension = new Dimension(700, 700);
        layout.setSize(dimension);     
        this.vv = new VisualizationViewer<>(layout); 
        
        //Organize and Format the graph data
        vv.getRenderContext().setVertexFillPaintTransformer(new vertexPainter(mNodes));
        vv.getRenderContext().setEdgeFillPaintTransformer(new edgePainter(mEdges));
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<>());
        //vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller<>());
        vv.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.E);
        vv.setPreferredSize(dimension);
        
        //Make the map able to be manipulated by a mouse
        PluggableGraphMouse pgm = new PluggableGraphMouse();
        pgm.add(new PickingGraphMousePlugin());
        pgm.add(new TranslatingGraphMousePlugin(MouseEvent.BUTTON3_MASK));
        pgm.add(new ScalingGraphMousePlugin(new CrossoverScalingControl(), 0, 1 / 1.1f, 1.1f));
        vv.setGraphMouse(pgm);

        //Establish the JFRAME
        final JFrame frame = new JFrame();
        frame.setTitle("BART SIS Model");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(vv);
        frame.pack();
        frame.setVisible(true);
    }
    
    
}
