/*
Manage the objects and functions of the BART Sytem graph
 */
package bart.sis.api;

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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Random;
import java.util.Set;
import java.util.stream.IntStream;
import javax.swing.JFrame;


class GraphData {
    private final Hashtable<String, StationObject> mStations;//Collection of station objects
    private final Hashtable<String, RouteObject> mRoutes;//Collection of route objects
    private final Graph<String, String> graph = new SparseMultigraph<>();//Graph object
    private final Random mRandomFactor = new Random();//Random number generator to determine whether station will heal/infect
    private final Set<String> mRouteKeys;//Create keychain to build the graph
    private final Set<String> mStationKeys;//Create keychain for infection and healing functions
    private VisualizationViewer<String, String> vv;
    private final ArrayList<String> mInfectKeys= new ArrayList<>();
    private final ArrayList<String> mHealKeys= new ArrayList<>();
    private final ArrayList<String> mMaintainKeys= new ArrayList<>();
    
    //Main Function 
    public GraphData(Hashtable stations, Hashtable routes) 
            throws SQLException{
        mStations = stations;
        mRoutes = routes;
        mRouteKeys = mRoutes.keySet();//Create a key chain for all the routes
        mStationKeys = mStations.keySet();//Create a key chain for all the stations
        populateGraph();//Initialize the graph
    }

    //Called by main function: Populates the graph from SQL data
    private void populateGraph(){
        mRouteKeys.stream().forEach((key)->{
            //int id = mRoutes.get(key).getLineID();
            //String RouteID = String.format("Route%s", Integer.toString(id));
            String RouteID = mRoutes.get(key).getAbbr();
            String stationOne = mRoutes.get(key).getOrig();
            String stationTwo = mRoutes.get(key).getDest();
            graph.addEdge(RouteID, stationOne, stationTwo);
        });
   
        
    }
    
    //Called by Main Project Function to run the SIS model
    public void runSIS(InfectionData infection, int iterations) 
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
        Object [] mStationKeyArray = mStationKeys.toArray();
        IntStream patientsRand = mRandomFactor.ints(patientZeroes, 1, mStations.size());
        for(int patient: patientsRand.toArray()){
            key = mStationKeyArray[patient].toString();
            mStations.get(key).infectStation();
        }

    }

    //Function called by runSIS to determine which stations will be infected
    private void infectGraph(InfectionData infection){
        //Use every key to unlock each station
        mStationKeys.stream().forEach((key) -> {
            //Get this station's neighbors
            Collection <String> neighbors = graph.getNeighbors(key);
            //Iterate through each neighbor of a station
            int infectedNeighbors = 0;
            for(String neighbor: neighbors){                
                if(mStations.get(neighbor).getState().equals("Infected"))
                    infectedNeighbors++;
            }
            if(getChanceChanged(infection.getInfectionRate(), infectedNeighbors) > mRandomFactor.nextFloat())
                this.mInfectKeys.add(key);
        });

    }
    
    //Function called by infectGraph to execute infection of selected stations
    private void infectKeys(){
        this.mInfectKeys.stream().forEach((key) -> {
            mStations.get(key).infectStation();
        });
        this.mInfectKeys.clear();
    }
    
    //Function called by runSIS to determine which stations will be healed
    private void healGraph(InfectionData infection){                
        mStationKeys.stream().forEach((key) ->  {
            if (mStations.get(key).getState().equals("Infected")){
                if(getChanceChanged(infection.getHealRate(), mStations.get(key).getDaysInfected())> mRandomFactor.nextFloat()){
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
            mStations.get(key).healStation();
        });
        this.mHealKeys.clear();
        
    }
    
    //Function called by healGraph to add a day infected to unhealed stations
    private void maintainKeys(){
        this.mMaintainKeys.stream().forEach((key) -> {
            mStations.get(key).addDayInfected();
        });
        this.mMaintainKeys.clear();
    }
    
    //Called by infect/healStations functions: Handle the odds of a station changing states;
    private double getChanceChanged(double changeRate, int numTrials){
        double chanceChanged = 1-Math.pow(1-changeRate, numTrials);
        return(chanceChanged);
    }    

    //Called by runSIS to visualize progress of infection
    private void visualizeGraph(){
        //Make a layout and visualizer where the stations can have a fixed position
        StaticLayout<String, String> layout = new StaticLayout<>(
        graph, new LocationTransformer(mStations));
        Dimension dimension = new Dimension(700, 700);
        layout.setSize(dimension);     
        this.vv = new VisualizationViewer<>(layout);

        
        
        //Organize and Format the graph data
        vv.getRenderContext().setVertexFillPaintTransformer(new StationPainter(mStations));
        vv.getRenderContext().setEdgeFillPaintTransformer(new RoutePainter(mRoutes));
        System.out.println("1");
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<>());
        System.out.println("2");
        //vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller<>());
        vv.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.E);
        System.out.println("3");
        vv.setPreferredSize(dimension);
        System.out.println("4");
        
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
