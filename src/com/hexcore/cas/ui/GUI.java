//v1.0

package com.hexcore.cas.ui;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.hexcore.cas.Server;
import com.hexcore.cas.ServerEvent;
import com.hexcore.cas.control.discovery.LobbyListener;
import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.math.Vector3f;
import com.hexcore.cas.model.Cell;
import com.hexcore.cas.model.ColourRule;
import com.hexcore.cas.model.ColourRuleSet;
import com.hexcore.cas.model.Grid;
import com.hexcore.cas.model.GridType;
import com.hexcore.cas.model.HexagonGrid;
import com.hexcore.cas.model.RectangleGrid;
import com.hexcore.cas.model.TriangleGrid;
import com.hexcore.cas.model.World;
import com.hexcore.cas.rulesystems.CALCompiler;
import com.hexcore.cas.rulesystems.CodeGen;
import com.hexcore.cas.ui.toolkit.Button;
import com.hexcore.cas.ui.toolkit.CheckBox;
import com.hexcore.cas.ui.toolkit.Colour;
import com.hexcore.cas.ui.toolkit.Container;
import com.hexcore.cas.ui.toolkit.Dialog;
import com.hexcore.cas.ui.toolkit.DiscreteSliderWidget;
import com.hexcore.cas.ui.toolkit.DropDownBox;
import com.hexcore.cas.ui.toolkit.Event;
import com.hexcore.cas.ui.toolkit.Fill;
import com.hexcore.cas.ui.toolkit.Grid2DWidget;
import com.hexcore.cas.ui.toolkit.Grid3DWidget;
import com.hexcore.cas.ui.toolkit.GridWidget;
import com.hexcore.cas.ui.toolkit.HexagonGrid3DWidget;
import com.hexcore.cas.ui.toolkit.HexagonGridWidget;
import com.hexcore.cas.ui.toolkit.Image;
import com.hexcore.cas.ui.toolkit.ImageWidget;
import com.hexcore.cas.ui.toolkit.LinearLayout;
import com.hexcore.cas.ui.toolkit.ListWidget;
import com.hexcore.cas.ui.toolkit.NumberBox;
import com.hexcore.cas.ui.toolkit.Panel;
import com.hexcore.cas.ui.toolkit.RectangleGrid3DWidget;
import com.hexcore.cas.ui.toolkit.RectangleGridWidget;
import com.hexcore.cas.ui.toolkit.ScrollableContainer;
import com.hexcore.cas.ui.toolkit.SliderWidget;
import com.hexcore.cas.ui.toolkit.TabbedView;
import com.hexcore.cas.ui.toolkit.Text;
import com.hexcore.cas.ui.toolkit.Text.Size;
import com.hexcore.cas.ui.toolkit.TextArea;
import com.hexcore.cas.ui.toolkit.TextWidget;
import com.hexcore.cas.ui.toolkit.Theme;
import com.hexcore.cas.ui.toolkit.TriangleGrid3DWidget;
import com.hexcore.cas.ui.toolkit.TriangleGridWidget;
import com.hexcore.cas.ui.toolkit.View;
import com.hexcore.cas.ui.toolkit.Widget;
import com.hexcore.cas.ui.toolkit.Window;
import com.hexcore.cas.ui.toolkit.Window.FileSelectResult;
import com.hexcore.cas.ui.toolkit.WindowEventListener;
import com.hexcore.cas.utilities.Log;

public class GUI implements WindowEventListener, LobbyListener
{	
	public static class CellContainer
	{
		
	}
	
	
	public static class Viewport
	{
		enum Type {TWO_D, THREE_D};
		
		public Container	container;
		public GridWidget	gridWidget;
		public Type 		type;
		
		public Viewport(Container container, Type type)
		{
			this.container = container;
			this.type = type;
		}

		
		public void switchDimension(Grid grid, Window window)

		{			
			if (this.type == Viewport.Type.THREE_D)
				type =  Viewport.Type.TWO_D;
				
			else			
				type =  Viewport.Type.THREE_D;
			
			recreate(grid, window);
			
		}
		
		public void recreate(Grid grid, Window window)
		{
	    	switch (grid.getType())
			{
				case RECTANGLE:
					if (type == Viewport.Type.THREE_D)
					{
						Grid3DWidget temp3DWidget = new RectangleGrid3DWidget(new Vector2i(10, 10), (RectangleGrid)grid, 10);
						temp3DWidget.addSlice(1, 10.0f);
						if (gridWidget != null)
							if (gridWidget.hasFocus()) 
								window.requestFocus(temp3DWidget);
						gridWidget = temp3DWidget;
					}
					else
					{
						Grid2DWidget temp2DWidget = new RectangleGridWidget(new Vector2i(10, 10), (RectangleGrid)grid, 10);
						temp2DWidget.setColourProperty(1);
						if (gridWidget != null)
							if (gridWidget.hasFocus()) 
								window.requestFocus(temp2DWidget);
						gridWidget = temp2DWidget;
					}	
					break;
				case HEXAGON:
					if (type == Viewport.Type.THREE_D)
					{
						Grid3DWidget temp3DWidget = new HexagonGrid3DWidget(new Vector2i(10, 10), (HexagonGrid)grid, 10);
						temp3DWidget.addSlice(1, 10.0f);
						if (gridWidget != null)
							if (gridWidget.hasFocus()) 
								window.requestFocus(temp3DWidget);
						gridWidget = temp3DWidget;
					}
					else
					{
						Grid2DWidget temp2DWidget = new HexagonGridWidget(new Vector2i(10, 10), (HexagonGrid)grid, 10);
						temp2DWidget.setColourProperty(1);
						if (gridWidget != null)
							if (gridWidget.hasFocus()) 
								window.requestFocus(temp2DWidget);
						gridWidget = temp2DWidget;
					}	
					break;
				case TRIANGLE:
					if (type == Viewport.Type.THREE_D)
					{
						Grid3DWidget temp3DWidget = new TriangleGrid3DWidget(new Vector2i(10, 10), (TriangleGrid)grid, 10);
						temp3DWidget.addSlice(1, 10.0f);
						if (gridWidget != null)
							if (gridWidget.hasFocus()) 
								window.requestFocus(temp3DWidget);
						gridWidget = temp3DWidget;
					}
					else
					{
						Grid2DWidget temp2DWidget = new TriangleGridWidget(new Vector2i(10, 10), (TriangleGrid)grid, 10);
						temp2DWidget.setColourProperty(1);
						if (gridWidget != null)
							if (gridWidget.hasFocus()) 
								window.requestFocus(temp2DWidget);
						gridWidget = temp2DWidget;
					}	
					break;
    			
			}
	    	
	    	gridWidget.setFlag(Widget.FILL);
	    	container.setContents(gridWidget);
		}
	}

	static class ClientEntry implements Comparable<ClientEntry>
	{
		InetSocketAddress address;
		
		ClientEntry(InetSocketAddress address)
		{
			this.address = address;
		}
		
		@Override
		public int compareTo(ClientEntry other)
		{
			return address.getHostName().compareTo(other.address.getHostName());
		}
	}
	
	//OUR WORLD///
    public World world;
	//////////////
    
    
    //OUR VIEWPORTS//
    public ArrayList<Viewport> viewports;
    ////////////////

    public Set<ClientEntry> availableClients;   
    public Set<ClientEntry> usingClients;
	
	public static final String TAG = "GUI";
	
    public Theme    theme;
    public Window	window;
    public View		masterView;
    public TabbedView tabbedWorldView;
    
    public LinearLayout	mainMenuLayout;
    public LinearLayout	worldLayout;

    public Button 	backButton;
    
    // WORLD PROPERTIES TAB
    public Container propertiesContainer;
   	
    public LinearLayout	masterPropertiesLayout;
    public Container 	widgetPreviewContainer;
    public Container 	widget3DPreviewContainer;
    public LinearLayout widgetPreviewLayout;
    public LinearLayout cellShapeLayout;
    
    public LinearLayout propertiesLayout;
    public LinearLayout worldSizeLayout;

    public TextWidget	worldSizeLabel;
    public TextWidget	worldSizeXLabel;
    public TextWidget	cellShapeLabel;

    
    public NumberBox    worldSizeXNumberBox;
    public NumberBox    worldSizeYNumberBox;

    public CheckBox		wrapCheckBox;
    public DropDownBox	cellShapeDropDownBox;
    
    public Button		submitButton;
    
    public Grid3DWidget	grid3DViewer = null;
    public Grid2DWidget	gridViewer = null;

    // RULES TAB    
    public Container rulesContainer;
    public LinearLayout rulesLayout;
    
    public Button clearRulesButton;
    public Button submitRulesButton;
    public Button openCALFileButton;
    public Button saveCALFileButton;
    
    public TextArea CALTextArea;
    public ScrollableContainer outputContainer;
    public LinearLayout outputLayout;
    
    public File calFile;
    public FileSelectResult selectedFile;
    
    public Dialog		dialogCAL; 
    public LinearLayout dialogCALLayout;
    public TextWidget	dialogCALTitle;
    public TextWidget	dialogCALMessage;
    public Button		dialogCALOKButton;
 
    // DISTRIBUTION TAB
    public Container 	distributionContainer;

    // INITIAL STATE TAB
    public Container 	worldPreviewContainer;

    // MAIN MENU
    public LinearLayout    headerLayout;
    public LinearLayout    mainLayout;
    public LinearLayout    buttonBarLayout;
    public LinearLayout    innerLayout;
    
    public View		mainView;
    
    public Button	createWorldButton;
    public Button	loadWorldButton;
    public Button	optionsButton;
    public Button	helpButton;
    public Button	quitButton;
        
    public Panel	mainPanel;
    
   
    public ColourRuleSet        colourRules;
    
    public String    currentThemeName = "lightV2";
    public String    themeName = currentThemeName;
    
    public Dialog        dialog;
    public LinearLayout  dialogLayout;
    public TextWidget    dialogTitle;
    public TextWidget    dialogMessage;
    public Button        dialogOKButton;
    
    // SIMULATION SCREEN
    private Button 		simulateButton;
    
    private LinearLayout viewportsLayout;
    private  LinearLayout masterSimulationLayout;
    
    private Button 	playButton;
    private Button 	pauseButton;
    private Button 	resetButton;
    private Button	stepForwardButton;
    
    private Button addViewportButton;
    
    private TextWidget	iterationsText;
    private TextWidget	timeText;
    private TextWidget	numCellsText;
    
    private DiscreteSliderWidget 	generationSlider;
        
    private int		currentGeneration = 0;
    private Grid	currentGrid;
    
    private Server 	server;
	private Button zoomInButton;


	private Button zoomOutButton;


	private Button moveUpButton;


	private Button moveDownButton;


	private Button moveLeftButton;


	private Button moveRightButton;


	private Button yawLeftButton;


	private Button yawRightButton;


	private Button pitchUpButton;


	private Button pitchDownButton;


	private Button saveAsCALFileButton;


	private Button refreshServerButton;


	private Container coloursContainer;


	private LinearLayout masterColoursLayout;


	private Button toggleHideButton;


	private Button toggle3dButton;


	private Button toggleWireframeButton;


	private Button removeViewportButton;


	private ImageWidget viewSettingsHeader;


	private Button toggleShowButton;
	
	
	// Distribution tab
	public ListWidget 	clientsAvailableList;
	public ListWidget 	clientsUsingList;
	
	public Image	computerIcon;
	public Image	computerLinkIcon;
	
	private Button	addClientButton;
	private Button	addAllClientsButton;
	private Button	removeClientButton;
	private Button	removeAllClientsButton;


	private LinearLayout masterWorldPreviewLayout;


	private LinearLayout leftLayout;


	private LinearLayout rightLayout;
	private Container previewWindowContainer;
	private Viewport previewViewport;
	private ArrayList<LinearLayout> rightLayouts;


	private Button setCellValueButton;


	private ArrayList<NumberBox> numberboxList;
	private Cell c;
    
    public GUI(Server server)
    {
        this.server = server;
        
        availableClients = new TreeSet<ClientEntry>();
        usingClients = new TreeSet<ClientEntry>();
        
        colourRules = new ColourRuleSet(4);
        ColourRule    colourRule;

        colourRule = new ColourRule();
        colourRule.addRange(new ColourRule.Range(0.0, 1.0, new Colour(1.0f, 0.0f, 0.0f)));
        colourRules.setColourRule(0, colourRule);
        
        colourRule = new ColourRule();
        colourRule.addRange(new ColourRule.Range(0.0, 1.0, new Colour(0.0f, 0.25f, 0.5f)));
        colourRule.addRange(new ColourRule.Range(1.0, 2.0, new Colour(0.0f, 0.8f, 0.5f)));
        colourRules.setColourRule(1, colourRule);
        
        colourRule = new ColourRule();
        colourRule.addRange(new ColourRule.Range(0.0, 15.1, new Colour(0.0f, 0.5f, 0.8f), new Colour(0.0f, 0.25f, 0.5f)));
        colourRules.setColourRule(2, colourRule);    
        
        colourRule = new ColourRule();
        colourRule.addRange(new ColourRule.Range(0.0, 8.0, new Colour(0.5f, 0.25f, 0.0f), new Colour(0.0f, 0.8f, 0.5f)));
        colourRule.addRange(new ColourRule.Range(8.0, 16.0, new Colour(0.0f, 0.8f, 0.5f), new Colour(0.4f, 1.0f, 0.8f)));
        colourRules.setColourRule(3, colourRule);  

        theme = new Theme();
        window = new Window("Cellular Automata Simulator - v1.0", 1024, 700, theme);
        
        window.addListener(this);
        window.show();
    }
    
    public void initialise()
    {
        theme.loadTheme(themeName);
        
        computerIcon = window.getTheme().getImage("icons", "computer.png");
        computerLinkIcon = window.getTheme().getImage("icons", "computer_link.png");
        
        masterView = new View(new Vector2i(10, 10));
        masterView.setMargin(new Vector2i(0, 0));
        masterView.setFlag(Widget.FILL);
        window.add(masterView);
        
        mainMenuLayout = new LinearLayout(LinearLayout.Direction.VERTICAL);
        mainMenuLayout.setFlag(Widget.CENTER | Widget.WRAP);
        mainMenuLayout.setMargin(new Vector2i(0, 0));
        masterView.add(mainMenuLayout);
        
        mainLayout = new LinearLayout(new Vector2i(1000, 600), LinearLayout.Direction.HORIZONTAL);
        mainLayout.setFlag(Widget.CENTER);
        mainMenuLayout.add(mainLayout);
                
        ImageWidget mainLogo = new ImageWidget(theme.getImage("backgrounds", "main_bg.png"));
        mainLogo.setFlag(Widget.CENTER_VERTICAL);
        mainLayout.add(mainLogo);        
        
        buttonBarLayout = new LinearLayout(new Vector2i(340, 350), LinearLayout.Direction.VERTICAL);
        buttonBarLayout.setFlag(Widget.CENTER_VERTICAL | Widget.WRAP_VERTICAL | Widget.FILL_HORIZONTAL);

        mainLayout.add(buttonBarLayout);
        
        createWorldButton = new Button(new Vector2i(300, 50), "Create World");
        
        createWorldButton.setIcon(theme.getImage("icons", "create_icon.png"), theme.getImage("icons", "create_icon-white.png"));
        createWorldButton.setFlag(Widget.CENTER_HORIZONTAL);
        buttonBarLayout.add(createWorldButton);
        
        loadWorldButton = new Button(new Vector2i(300, 50), "Load World");
        
        loadWorldButton.setIcon(theme.getImage("icons", "load_icon.png"), theme.getImage("icons", "load_icon-white.png"));
        loadWorldButton.setFlag(Widget.CENTER_HORIZONTAL);
        buttonBarLayout.add(loadWorldButton);
        
        optionsButton = new Button(new Vector2i(300, 50), "Options");
        
        optionsButton.setIcon(theme.getImage("icons", "options_icon.png"), theme.getImage("icons", "options_icon-white.png"));
        optionsButton.setFlag(Widget.CENTER_HORIZONTAL);
        buttonBarLayout.add(optionsButton);
        
        helpButton = new Button(new Vector2i(300, 50), "Help");
        
        helpButton.setIcon(theme.getImage("icons", "help_icon.png"), theme.getImage("icons", "help_icon-white.png"));
        helpButton.setFlag(Widget.CENTER_HORIZONTAL);
        buttonBarLayout.add(helpButton);
        
        quitButton = new Button(new Vector2i(300, 50), "Quit");
        
        quitButton.setIcon(theme.getImage("icons", "on-off_icon.png"), theme.getImage("icons", "on-off_icon-white.png"));
        quitButton.setFlag(Widget.CENTER_HORIZONTAL);
        buttonBarLayout.add(quitButton);
        
        /// Main WORLD BUILDER
        
        
        worldLayout = new LinearLayout(LinearLayout.Direction.VERTICAL);
        worldLayout.setFlag(Widget.FILL);
        
        masterView.add(worldLayout);
        
        
        tabbedWorldView = new TabbedView(new Vector2i(30,30));
        tabbedWorldView.setFlag(Widget.FILL);
        
        
        
        LinearLayout worldHeaderLayout = new LinearLayout(new Vector2i(100, 30), LinearLayout.Direction.HORIZONTAL);
        worldHeaderLayout.setFlag(Widget.FILL_HORIZONTAL);
        worldLayout.add(worldHeaderLayout);

		worldLayout.add(tabbedWorldView);
		
		propertiesContainer = new Container(new Vector2i(100, 100));    
		propertiesContainer.setFlag(Widget.FILL);
		tabbedWorldView.add(propertiesContainer, "World Properties");
		
		masterPropertiesLayout = new LinearLayout(LinearLayout.Direction.VERTICAL);
		masterPropertiesLayout.setFlag(Widget.FILL);
		propertiesContainer.setContents(masterPropertiesLayout);

		LinearLayout instructionsLayout = new LinearLayout(LinearLayout.Direction.HORIZONTAL);
		instructionsLayout.setHeight(35);
		instructionsLayout.setFlag(Widget.CENTER_HORIZONTAL);
		instructionsLayout.setBorder(new Fill(new Colour(0.6f,0.6f,0.6f)));
		masterPropertiesLayout.add(instructionsLayout);
		   
		TextWidget propertiesInstructions = new TextWidget("Specify your world properties such as cell shape, world size and whether the world is wrappable.");
		instructionsLayout.add(propertiesInstructions);
		instructionsLayout.setWidth(propertiesInstructions.getWidth()+ 20);
	                                
        propertiesLayout = new LinearLayout(LinearLayout.Direction.VERTICAL);
        propertiesLayout.setFlag(Widget.FILL);
        propertiesLayout.setBorder(new Fill(new Colour(0.6f,0.6f,0.6f)));
        masterPropertiesLayout.add(propertiesLayout);
       
        worldSizeLayout = new LinearLayout(LinearLayout.Direction.HORIZONTAL);
        worldSizeLayout.setHeight(50);
        worldSizeLayout.setFlag(Widget.FILL_HORIZONTAL);
        
        propertiesLayout.add(worldSizeLayout);
        
        worldSizeLabel = new TextWidget("World Size:", Size.MEDIUM);
        worldSizeLabel.setFlag(Widget.CENTER_VERTICAL);
        worldSizeLayout.add(worldSizeLabel);
        
        worldSizeXNumberBox = new NumberBox(35);
        worldSizeXNumberBox.setWidth(50);  
        worldSizeXNumberBox.setValue(10);
        worldSizeXNumberBox.setFlag(Widget.CENTER_VERTICAL);
        worldSizeLayout.add(worldSizeXNumberBox);
        
        worldSizeXLabel = new TextWidget("X", Size.LARGE);
        worldSizeXLabel.setFlag(Widget.CENTER_VERTICAL);
        worldSizeLayout.add(worldSizeXLabel);
        
        worldSizeYNumberBox = new NumberBox(35);
        worldSizeYNumberBox.setWidth(50);  
        worldSizeYNumberBox.setValue(10);
        worldSizeYNumberBox.setFlag(Widget.CENTER_VERTICAL);
        worldSizeLayout.add(worldSizeYNumberBox);
        
        wrapCheckBox = new CheckBox(new Vector2i(100,50), "Wrappable");
        wrapCheckBox.setFlag(Widget.CENTER_VERTICAL);
        wrapCheckBox.setMargin(new Vector2i(50,0));
        worldSizeLayout.add(wrapCheckBox);
            
        cellShapeLayout = new LinearLayout(LinearLayout.Direction.HORIZONTAL);
        cellShapeLayout.setFlag(Widget.FILL_HORIZONTAL);
        cellShapeLayout.setHeight(65);

        propertiesLayout.add(cellShapeLayout);
        
        cellShapeLabel = new TextWidget("Cell Shape:",Size.MEDIUM);
        cellShapeLabel.setFlag(Widget.CENTER_VERTICAL);
        cellShapeLayout.add(cellShapeLabel);
        
        cellShapeDropDownBox = new DropDownBox(new Vector2i(100,20));
        cellShapeDropDownBox.setFlag(Widget.CENTER_VERTICAL);
        cellShapeDropDownBox.addItem("Square");
        cellShapeDropDownBox.addItem("Triangle");
        cellShapeDropDownBox.addItem("Hexagon");
        cellShapeDropDownBox.setSelected(0);
        
        cellShapeLayout.add(cellShapeDropDownBox);
                
        
        
        LinearLayout widgetPreviewLayout = new LinearLayout(LinearLayout.Direction.HORIZONTAL);
        widgetPreviewLayout.setBackground(new Fill(new Colour(0.0f,0.0f,0.0f)));
        widgetPreviewLayout.setFlag(Widget.FILL);
        propertiesLayout.add(widgetPreviewLayout);
      
        widgetPreviewContainer = new Container(new Vector2i(200,100));
        widgetPreviewContainer.setFlag(Widget.FILL);
        widgetPreviewLayout.add(widgetPreviewContainer);
        
        widget3DPreviewContainer = new Container(new Vector2i (300,100));
        widget3DPreviewContainer.setFlag(Widget.FILL);
        widgetPreviewLayout.add(widget3DPreviewContainer);
        
        widget3DPreviewContainer.setContents(grid3DViewer);
        widgetPreviewContainer.setContents(gridViewer);
        
        LinearLayout buttonHeaderLayout = new LinearLayout(new Vector2i(700, 50), LinearLayout.Direction.HORIZONTAL);
        buttonHeaderLayout.setBackground(new Fill(new Colour(0.7f, 0.7f, 0.7f)));
        buttonHeaderLayout.setBorder(new Fill(new Colour(0.7f, 0.7f, 0.7f)));
        buttonHeaderLayout.setFlag(Widget.CENTER_HORIZONTAL);
        worldLayout.add(buttonHeaderLayout);
            
            backButton = new Button(new Vector2i(100, 50), "Main Menu");
            backButton.setWidth(165);
            backButton.setHeight(35);
            buttonHeaderLayout.add(backButton);
            
            Button saveWorldButton = new Button(new Vector2i(100, 50), "Save World");
            saveWorldButton.setWidth(165);
            saveWorldButton.setHeight(35);
            buttonHeaderLayout.add(saveWorldButton);
            
            submitButton = new Button(new Vector2i(145,50), "Apply Changes");
            submitButton.setWidth(165);
            submitButton.setHeight(35);
            buttonHeaderLayout.add(submitButton);
            
            simulateButton = new Button(new Vector2i(100, 50), "Simulate");
            simulateButton.setWidth(165);
            simulateButton.setHeight(35);
            buttonHeaderLayout.add(simulateButton);

        rulesContainer = new Container(new Vector2i(100, 100));
        tabbedWorldView.add(rulesContainer, "CAL Rules");
        rulesContainer.setFlag(Widget.FILL);

        LinearLayout masterRulesLayout = new LinearLayout(LinearLayout.Direction.VERTICAL);
        masterRulesLayout.setFlag(Widget.FILL);
        rulesContainer.setContents(masterRulesLayout);
        
        LinearLayout instructionsCALLayout = new LinearLayout(LinearLayout.Direction.HORIZONTAL);
        instructionsCALLayout.setHeight(35);
        instructionsCALLayout.setFlag(Widget.CENTER_HORIZONTAL);
        instructionsCALLayout.setBorder(new Fill(new Colour(0.6f,0.6f,0.6f)));
        masterRulesLayout.add(instructionsCALLayout);
		   
		TextWidget CALInstructions = new TextWidget("Inform the compiler of the cells and their properties that your world will support.");
		instructionsCALLayout.add(CALInstructions);
		instructionsCALLayout.setWidth(CALInstructions.getWidth()+ 20);
        
        
        LinearLayout CALLayout = new LinearLayout(LinearLayout.Direction.VERTICAL);
        CALLayout.setFlag(Widget.FILL);
        masterRulesLayout.add(CALLayout);
        
        
        
        
        ImageWidget calEditorHeader = new ImageWidget(window.getTheme().getImage("headers","cal_editor_header.png"));
        calEditorHeader.setFlag(Widget.CENTER_HORIZONTAL);
        CALLayout.add(calEditorHeader);    
        
        CALTextArea = new TextArea(100, 20);
        CALTextArea.setMargin(new Vector2i(0,0));
        CALTextArea.setFlag(Widget.FILL);
        CALTextArea.setLineNumbers(true);
    
        
        ScrollableContainer textAreaContainer = new ScrollableContainer(new Vector2i(100,100));
        textAreaContainer.setFlag(Widget.FILL);
    
        textAreaContainer.setBorder(new Fill(new Colour(0.7f, 0.7f, 0.7f)));
        CALLayout.add(textAreaContainer);
        
        textAreaContainer.setContents(CALTextArea);
        
        
        ImageWidget compilerOutputHeader = new ImageWidget(window.getTheme().getImage("headers","compiler_output_header.png"));
        compilerOutputHeader.setFlag(Widget.CENTER_HORIZONTAL);
        CALLayout.add(compilerOutputHeader);    
        
        
        
        outputContainer = new ScrollableContainer(new Vector2i(250, 100));
        outputContainer.setFlag(Widget.FILL_HORIZONTAL);
        outputContainer.setFlag(Widget.CENTER_HORIZONTAL);
        outputContainer.setThemeClass("List");
        CALLayout.add(outputContainer);
        
        outputLayout = new LinearLayout(LinearLayout.Direction.VERTICAL);
        outputLayout.setMargin(new Vector2i(0, 0));
        outputLayout.setFlag(Widget.WRAP);
        
        outputContainer.setContents(outputLayout);
        

        LinearLayout buttonRulesLayout = new LinearLayout(new Vector2i(875, 50), LinearLayout.Direction.HORIZONTAL);
        buttonRulesLayout.setBorder(new Fill(new Colour(0.7f, 0.7f, 0.7f)));
        buttonRulesLayout.setFlag(Widget.CENTER_HORIZONTAL);
        masterRulesLayout.add(buttonRulesLayout);
            
            clearRulesButton = new Button(new Vector2i(100, 50), "Clear Rules");
            clearRulesButton.setWidth(165);
            clearRulesButton.setHeight(35);
            buttonRulesLayout.add(clearRulesButton);
            
            submitRulesButton = new Button(new Vector2i(100, 50), "Submit Rules");
            submitRulesButton.setWidth(165);
            submitRulesButton.setHeight(35);
            buttonRulesLayout.add(submitRulesButton);
            
            openCALFileButton = new Button(new Vector2i(100, 50), "Open File");
            openCALFileButton.setWidth(165);
            openCALFileButton.setHeight(35);
            buttonRulesLayout.add(openCALFileButton);
            
            saveCALFileButton = new Button(new Vector2i(100, 50), "Save File");
            saveCALFileButton.setWidth(165);
            saveCALFileButton.setHeight(35);
            buttonRulesLayout.add(saveCALFileButton);
            
           saveAsCALFileButton = new Button(new Vector2i(100, 50), "Save File As");
            saveAsCALFileButton.setWidth(165);
            saveAsCALFileButton.setHeight(35);
            buttonRulesLayout.add(saveAsCALFileButton);
            
            createDistributionTab();

        
        coloursContainer = new Container(new Vector2i(100, 100));
        coloursContainer.setFlag(Widget.FILL);
        tabbedWorldView.add(coloursContainer, "Colour Ranges");
        
        masterColoursLayout = new LinearLayout(LinearLayout.Direction.VERTICAL);
        masterColoursLayout.setFlag(Widget.FILL);
        coloursContainer.setContents(masterColoursLayout);
        
        
        LinearLayout colourInstructionsLayout = new LinearLayout(LinearLayout.Direction.HORIZONTAL);
        colourInstructionsLayout.setHeight(35);
        colourInstructionsLayout.setFlag(Widget.CENTER_HORIZONTAL);
        colourInstructionsLayout.setBorder(new Fill(new Colour(0.6f,0.6f,0.6f)));
        masterColoursLayout.add(colourInstructionsLayout);
		   
		TextWidget coloursInstructions = new TextWidget("Create the colour ranges for each cell property you have defined");
		colourInstructionsLayout.add(coloursInstructions);
		colourInstructionsLayout.setWidth(coloursInstructions.getWidth()+ 20);
        
        
        ///
        
        
        
        worldPreviewContainer = new Container(new Vector2i(100, 100));
        worldPreviewContainer.setFlag(Widget.FILL);
        tabbedWorldView.add(worldPreviewContainer, "World Preview");
        
        
        masterWorldPreviewLayout = new LinearLayout(LinearLayout.Direction.HORIZONTAL);
        masterWorldPreviewLayout.setFlag(Widget.FILL);
        worldPreviewContainer.setContents(masterWorldPreviewLayout);
        
        leftLayout = new LinearLayout(LinearLayout.Direction.VERTICAL);
        leftLayout.setWidth(500);
        leftLayout.setFlag(Widget.FILL_VERTICAL);
        masterWorldPreviewLayout.add(leftLayout);
        
        rightLayout = new LinearLayout(LinearLayout.Direction.VERTICAL);
        rightLayout.setFlag(Widget.FILL);
        masterWorldPreviewLayout.add(rightLayout);
       
        previewWindowContainer = new Container(new Vector2i(100,100));
        previewWindowContainer.setFlag(Widget.FILL);
        previewWindowContainer.setBackground(new Fill(new Colour(0f,0f,0f)));
        
        previewViewport = new Viewport(previewWindowContainer, Viewport.Type.TWO_D);    
        leftLayout.add(previewViewport.container);
        
        
        setCellValueButton = new Button(new Vector2i(70,43), "SET");
    	setCellValueButton.setFlag(Widget.CENTER_HORIZONTAL);
        rightLayout.add(setCellValueButton);
    	
        
        
        //
        
        
        //
        

        dialog = new Dialog(window, new Vector2i(400, 200));
        
        dialogLayout = new LinearLayout(LinearLayout.Direction.VERTICAL);
        dialogLayout.setFlag(Widget.FILL);
        dialog.setContents(dialogLayout);
        
        dialogTitle = new TextWidget("Invalid World Size", Text.Size.LARGE);
        dialogTitle.setFlag(Widget.CENTER_HORIZONTAL);
        dialogLayout.add(dialogTitle);
        
        dialogMessage = new TextWidget("World Sizes cannot be less than 5 cells.");
        dialogMessage.setFlag(Widget.FILL_HORIZONTAL);
        dialogMessage.setFlag(Widget.FILL_VERTICAL); // This pushes the OK button down because it fills the space in between
        dialogMessage.setFlowed(true);
        dialogMessage.setFlag(Widget.CENTER_HORIZONTAL);
        dialogLayout.add(dialogMessage);
        
        dialogOKButton = new Button(new Vector2i(120, 30), "OK");
        dialogOKButton.setFlag(Widget.CENTER_HORIZONTAL);
        dialogLayout.add(dialogOKButton);
        
        // Dialog CAL
        
        dialogCAL = new Dialog(window, new Vector2i(400, 200));
        
        dialogCALLayout = new LinearLayout(LinearLayout.Direction.VERTICAL);
        dialogCALLayout.setFlag(Widget.FILL);
        dialogCAL.setContents(dialogCALLayout);
        
        dialogCALTitle = new TextWidget("CAL Rules Error", Text.Size.LARGE);
        dialogCALTitle.setFlag(Widget.CENTER_HORIZONTAL);
        dialogCALLayout.add(dialogCALTitle);
        
        dialogCALMessage = new TextWidget("Invalid .cal File");
        dialogCALMessage.setFlag(Widget.FILL_HORIZONTAL);
        dialogCALMessage.setFlag(Widget.FILL_VERTICAL); // This pushes the OK button down because it fills the space in between
        dialogCALMessage.setFlowed(true);
        dialogCALMessage.setFlag(Widget.CENTER_HORIZONTAL);
        dialogCALLayout.add(dialogCALMessage);
        
        dialogCALOKButton = new Button(new Vector2i(120, 30), "OK");
        dialogCALOKButton.setFlag(Widget.CENTER_HORIZONTAL);
        dialogCALLayout.add(dialogCALOKButton);
        
        
        
        ////Simulation
        
        masterSimulationLayout = new LinearLayout(LinearLayout.Direction.VERTICAL);
        masterSimulationLayout.setFlag(Widget.FILL);
        masterSimulationLayout.setMargin(new Vector2i(0, 0));
        masterView.add(masterSimulationLayout);
        masterSimulationLayout.add(worldHeaderLayout);
        
        
        viewportsLayout = new LinearLayout(LinearLayout.Direction.HORIZONTAL);
        viewportsLayout.setFlag(Widget.FILL);
        masterSimulationLayout.add(viewportsLayout);
   
         
        
        Container simulationWindowContainer = new Container(new Vector2i(500,300));
        simulationWindowContainer.setFlag(Widget.FILL);
        simulationWindowContainer.setBackground(new Fill(new Colour(0f,0f,0f)));
        
        Viewport v = new Viewport(simulationWindowContainer, Viewport.Type.THREE_D);    
        viewportsLayout.add(v.container);
      
        viewports = new ArrayList<Viewport>();
        viewports.add(v);
       

        //SLIDER
        
        LinearLayout sliderLayout = new LinearLayout(new Vector2i(40, 55), LinearLayout.Direction.HORIZONTAL);
        sliderLayout.setBorder(new Fill(new Colour(0.7F, 0.7F, 0.7F)));
        sliderLayout.setFlag(Widget.FILL_HORIZONTAL | Widget.WRAP_VERTICAL);
        masterSimulationLayout.add(sliderLayout);
        
        generationSlider = new DiscreteSliderWidget(100);
        generationSlider.setMargin(new Vector2i(5, 5));
        generationSlider.setFlag(Widget.FILL_HORIZONTAL);
        generationSlider.setShowValue(true);
        sliderLayout.add(generationSlider);
        
        LinearLayout simulationControlsLayout = new LinearLayout(new Vector2i(100, 150), LinearLayout.Direction.HORIZONTAL);
        simulationControlsLayout.setFlag(Widget.FILL_HORIZONTAL | Widget.WRAP_VERTICAL);
        
        masterSimulationLayout.add(simulationControlsLayout);
        
        LinearLayout detailsLayout = new LinearLayout(new Vector2i(250, 150), LinearLayout.Direction.VERTICAL);
        detailsLayout.setBorder(new Fill(new Colour(0.7F, 0.7F, 0.7F)));
        simulationControlsLayout.add(detailsLayout);
        
        LinearLayout innerDetailsLayout = new LinearLayout(new Vector2i(160, 25), LinearLayout.Direction.HORIZONTAL);
        innerDetailsLayout.setFlag(Widget.CENTER_HORIZONTAL | Widget.WRAP_VERTICAL);
        detailsLayout.add(innerDetailsLayout);
        
        LinearLayout innerDetailsLayout2 = new LinearLayout(new Vector2i(205, 40), LinearLayout.Direction.VERTICAL);
        innerDetailsLayout2.setFlag(Widget.CENTER_HORIZONTAL | Widget.WRAP_VERTICAL);
        detailsLayout.add(innerDetailsLayout2);
        
        iterationsText = new TextWidget("Generations: 0");
        innerDetailsLayout2.add(iterationsText);
        
       // numPropertiesText = new TextWidget("Num Properties: ");
       // innerDetailsLayout2.add(numPropertiesText);
        
        numCellsText = new TextWidget("Num. Cells: ");
        innerDetailsLayout2.add(numCellsText);
        
        ImageWidget detailsImage = new ImageWidget(this.window.getTheme().getImage("headers", "details_header.png"));
        detailsImage.setFlag(Widget.CENTER_HORIZONTAL);
        innerDetailsLayout.add(detailsImage);
        
        LinearLayout playbackLayout = new LinearLayout(new Vector2i(250, 150), LinearLayout.Direction.VERTICAL);
        playbackLayout.setBorder(new Fill(new Colour(0.7F, 0.7F, 0.7F)));
        simulationControlsLayout.add(playbackLayout);
        
        LinearLayout innerPlaybackLayout = new LinearLayout(new Vector2i(205, 25), LinearLayout.Direction.HORIZONTAL);
        innerPlaybackLayout.setFlag(Widget.CENTER_HORIZONTAL);
        playbackLayout.add(innerPlaybackLayout);
        
        LinearLayout innerPlaybackLayout2 = new LinearLayout(new Vector2i(205, 40), LinearLayout.Direction.HORIZONTAL);
        innerPlaybackLayout2.setFlag(Widget.CENTER_HORIZONTAL);
        playbackLayout.add(innerPlaybackLayout2);
        
        LinearLayout innerPlaybackLayout3 = new LinearLayout(new Vector2i(225, 35), LinearLayout.Direction.HORIZONTAL);
        innerPlaybackLayout3.setFlag(Widget.CENTER_HORIZONTAL);
        playbackLayout.add(innerPlaybackLayout3);
        
        
        Button stepBackwardButton = new Button(this.window.getTheme().getImage("icons", "step_backward_icon.png"));
        stepBackwardButton.setMargin(new Vector2i(5, 0));
        innerPlaybackLayout2.add(stepBackwardButton);
        playButton = new Button(this.window.getTheme().getImage("icons", "play_icon.png"));
        playButton.setMargin(new Vector2i(5, 0));
        innerPlaybackLayout2.add(playButton);
        pauseButton = new Button(this.window.getTheme().getImage("icons", "pause_icon.png"));
        pauseButton.setMargin(new Vector2i(5, 0));
        innerPlaybackLayout2.add(pauseButton);
        resetButton = new Button(this.window.getTheme().getImage("icons", "reset_icon.png"));
        resetButton.setMargin(new Vector2i(5, 0));
        innerPlaybackLayout2.add(resetButton);
        stepForwardButton = new Button(window.getTheme().getImage("icons", "step_forward_icon.png"));
        stepForwardButton.setMargin(new Vector2i(5, 0));
        innerPlaybackLayout2.add(stepForwardButton);
        TextWidget playbackSpeedHeader = new TextWidget("Playback Speed:");
        innerPlaybackLayout3.add(playbackSpeedHeader);
        SliderWidget playbackSpeedSlider = new SliderWidget(1);
        playbackSpeedSlider.setFlag(Widget.CENTER);
        playbackSpeedSlider.setShowValue(true);
        innerPlaybackLayout3.add(playbackSpeedSlider);
        ImageWidget playbackImage = new ImageWidget(window.getTheme().getImage("headers", "playback_header.png"));
        playbackImage.setFlag(Widget.CENTER_HORIZONTAL);
        innerPlaybackLayout.add(playbackImage);
        
        
        
        LinearLayout cameraLayout = new LinearLayout(new Vector2i(270, 150), LinearLayout.Direction.VERTICAL);
        cameraLayout.setBorder(new Fill(new Colour(0.7F, 0.7F, 0.7F)));
        simulationControlsLayout.add(cameraLayout);
        LinearLayout innerCameraLayout = new LinearLayout(new Vector2i(190, 25), LinearLayout.Direction.HORIZONTAL);
        innerCameraLayout.setFlag(Widget.CENTER_HORIZONTAL);
        cameraLayout.add(innerCameraLayout);
        
        LinearLayout innerCameraLayout2 = new LinearLayout(new Vector2i(245, 40), LinearLayout.Direction.HORIZONTAL);
        innerCameraLayout2.setFlag(Widget.CENTER_HORIZONTAL);
        cameraLayout.add(innerCameraLayout2);
        
        LinearLayout innerCameraLayout3 = new LinearLayout(new Vector2i(160, 40), LinearLayout.Direction.HORIZONTAL);
        innerCameraLayout3.setFlag(Widget.CENTER_HORIZONTAL);
        cameraLayout.add(innerCameraLayout3);
        
        zoomInButton = new Button(window.getTheme().getImage("icons", "zoom_in_icon.png"));
        zoomInButton.setMargin(new Vector2i(5, 0));
        innerCameraLayout2.add(zoomInButton);
      
        zoomOutButton = new Button(window.getTheme().getImage("icons", "zoom_out_icon.png"));
        zoomOutButton.setMargin(new Vector2i(5, 0));
        innerCameraLayout2.add(zoomOutButton);
       
        moveUpButton = new Button(window.getTheme().getImage("icons", "up_icon.png"));
        moveUpButton.setMargin(new Vector2i(5, 0));
        innerCameraLayout2.add(moveUpButton);
       
        moveDownButton = new Button(window.getTheme().getImage("icons", "down_icon.png"));
        moveDownButton.setMargin(new Vector2i(5, 0));
        innerCameraLayout2.add(moveDownButton);
       
        moveLeftButton = new Button(window.getTheme().getImage("icons", "left_icon.png"));
        moveLeftButton.setMargin(new Vector2i(5, 0));
        innerCameraLayout2.add(moveLeftButton);
        
        moveRightButton = new Button(window.getTheme().getImage("icons", "right_icon.png"));
        moveRightButton.setMargin(new Vector2i(5, 0));
        innerCameraLayout2.add(moveRightButton);
        
        
        yawLeftButton = new Button(window.getTheme().getImage("icons", "yaw_left_icon.png"));
        yawLeftButton.setMargin(new Vector2i(5, 0));
        innerCameraLayout3.add(yawLeftButton);
        
        yawRightButton = new Button(window.getTheme().getImage("icons", "yaw_right_icon.png"));
        yawRightButton.setMargin(new Vector2i(5, 0));
        innerCameraLayout3.add(yawRightButton);
        
        pitchUpButton = new Button(window.getTheme().getImage("icons", "pitch_up_icon.png"));
        pitchUpButton.setMargin(new Vector2i(5, 0));
        innerCameraLayout3.add(pitchUpButton);
        
        pitchDownButton = new Button(window.getTheme().getImage("icons", "pitch_down_icon.png"));
        pitchDownButton.setMargin(new Vector2i(5, 0));
        
        innerCameraLayout3.add(pitchDownButton);
        
        
        ImageWidget cameraImage = new ImageWidget(window.getTheme().getImage("headers", "camera_header.png"));
        cameraImage.setFlag(Widget.CENTER_HORIZONTAL);
        innerCameraLayout.add(cameraImage);
        
        
        
        
        
        
        LinearLayout viewSettingsLayout = new LinearLayout(new Vector2i(250, 90), LinearLayout.Direction.VERTICAL);
        viewSettingsLayout.setBorder(new Fill(new Colour(0.7F, 0.7F, 0.7F)));
        simulationControlsLayout.add(viewSettingsLayout);
        
        LinearLayout innerViewSettingsLayout = new LinearLayout(new Vector2i(145, 25), LinearLayout.Direction.HORIZONTAL);
        innerViewSettingsLayout.setFlag(Widget.CENTER_HORIZONTAL);
        viewSettingsLayout.add(innerViewSettingsLayout);
        
        LinearLayout innerViewSettingsLayout2 = new LinearLayout(new Vector2i(220, 40), LinearLayout.Direction.HORIZONTAL);
        innerViewSettingsLayout2.setFlag(Widget.CENTER_HORIZONTAL);
        viewSettingsLayout.add(innerViewSettingsLayout2);
        
        toggleHideButton = new Button(window.getTheme().getImage("icons", "toggle_hide_icon.png"));
        toggleHideButton.setMargin(new Vector2i(5, 0));
        innerViewSettingsLayout2.add(toggleHideButton);
        
        toggle3dButton = new Button(this.window.getTheme().getImage("icons", "3d_icon.png"));
        toggle3dButton.setMargin(new Vector2i(5, 0));
        innerViewSettingsLayout2.add(toggle3dButton);
        
        toggleWireframeButton = new Button(this.window.getTheme().getImage("icons", "toggle_wireframe_icon.png"));
        toggleWireframeButton.setMargin(new Vector2i(5, 0));
        innerViewSettingsLayout2.add(toggleWireframeButton);
        
        addViewportButton = new Button(this.window.getTheme().getImage("icons", "add_viewport_icon.png"));
        addViewportButton.setMargin(new Vector2i(5, 0));
        innerViewSettingsLayout2.add(addViewportButton);
        
        removeViewportButton = new Button(this.window.getTheme().getImage("icons", "remove_viewport_icon.png"));
        removeViewportButton.setMargin(new Vector2i(5, 0));
        innerViewSettingsLayout2.add(removeViewportButton);
        
        viewSettingsHeader = new ImageWidget(this.window.getTheme().getImage("headers", "view_settings_header.png"));
        cameraImage.setFlag(Widget.CENTER_HORIZONTAL);
        innerViewSettingsLayout.add(viewSettingsHeader);
        
        toggleShowButton = new Button(new Vector2i(10, 15), "");
        toggleShowButton.setFlag(Widget.FILL_HORIZONTAL);
        toggleShowButton.setVisible(false);
        masterSimulationLayout.add(toggleShowButton);
        
       
        window.relayout();
    }
    
    public void createDistributionTab()
    {
    	distributionContainer = new Container(new Vector2i(100, 100));
    	distributionContainer.setMargin(new Vector2i(0, 0));
        distributionContainer.setFlag(Widget.FILL);
        tabbedWorldView.add(distributionContainer, "Distribution Settings");
        
        LinearLayout masterDistributionLayout = new LinearLayout(LinearLayout.Direction.VERTICAL);
        masterDistributionLayout.setMargin(new Vector2i(0, 0));
        masterDistributionLayout.setFlag(Widget.FILL);
        distributionContainer.setContents(masterDistributionLayout);
        
        // List Layout
        LinearLayout listLayout = new LinearLayout(LinearLayout.Direction.HORIZONTAL);
        listLayout.setFlag(Widget.FILL);
        masterDistributionLayout.add(listLayout);
        
        	// Left List Layout
	        LinearLayout leftListLayout = new LinearLayout(LinearLayout.Direction.VERTICAL);
	        leftListLayout.setMargin(new Vector2i(0, 0));
	        leftListLayout.setFlag(Widget.FILL);
	        listLayout.add(leftListLayout);
	        
	        TextWidget leftListTitle = new TextWidget("Available clients");
	        leftListLayout.add(leftListTitle);
	        
	        clientsAvailableList = new ListWidget(new Vector2i(10, 10));
	        clientsAvailableList.setFlag(Widget.FILL);
	        leftListLayout.add(clientsAvailableList);
	        
	        // Center buttons
	        LinearLayout centerButtonLayout = new LinearLayout(LinearLayout.Direction.VERTICAL);
	        centerButtonLayout.setMargin(new Vector2i(0, 0));
	        centerButtonLayout.setFlag(Widget.WRAP | Widget.CENTER_VERTICAL);
	        listLayout.add(centerButtonLayout);
	        
	        addClientButton = new Button(new Vector2i(150, 40), "Add >");
	        centerButtonLayout.add(addClientButton);
	        
	        addAllClientsButton = new Button(new Vector2i(150, 40), "Add All >>");
	        centerButtonLayout.add(addAllClientsButton);
	        
	        removeClientButton = new Button(new Vector2i(150, 40), "< Remove");
	        centerButtonLayout.add(removeClientButton);
	        
	        removeAllClientsButton = new Button(new Vector2i(150, 40), "<< Remove All");
	        centerButtonLayout.add(removeAllClientsButton);
	        
	        // Right List Layout
	        LinearLayout rightListLayout = new LinearLayout(LinearLayout.Direction.VERTICAL);
	        rightListLayout.setMargin(new Vector2i(0, 0));
	        rightListLayout.setFlag(Widget.FILL);
	        listLayout.add(rightListLayout);
	        
	        TextWidget rightListTitle = new TextWidget("Using clients");
	        rightListLayout.add(rightListTitle);
	        
	        clientsUsingList = new ListWidget(new Vector2i(10, 10));
	        clientsUsingList.setFlag(Widget.FILL);   
	        rightListLayout.add(clientsUsingList);
        
        // Control Layout
        LinearLayout controlLayout = new LinearLayout(LinearLayout.Direction.HORIZONTAL);
        controlLayout.setFlag(Widget.FILL_HORIZONTAL | Widget.WRAP_VERTICAL);
        masterDistributionLayout.add(controlLayout);      
        
        refreshServerButton = new Button(new Vector2i(100, 50), "Refresh");
        controlLayout.add(refreshServerButton);
    }
    
    public void startWorldEditor(World world)
    {
    	this.world = world;
    	loadPropertiesFromWorld();
    	
    	masterView.setIndex(1); 	
    	window.relayout();
    }
    
    public void loadPropertiesFromWorld()
    {
    	Grid grid = world.getInitialGeneration();
    	
    	worldSizeXNumberBox.setValue(grid.getWidth());
    	worldSizeYNumberBox.setValue(grid.getHeight());
    	
    	switch (grid.getType())
    	{
    		case RECTANGLE:
    			cellShapeDropDownBox.setSelected(0);
    			break;
    		case TRIANGLE:
    			cellShapeDropDownBox.setSelected(1);
    			break;
    		case HEXAGON:
    			cellShapeDropDownBox.setSelected(2);
    			break;
    	}
    	
    	wrapCheckBox.setChecked(grid.isWrappable());
    	
    	String ruleCode = world.getRuleCode();
    	if (ruleCode == null) ruleCode = "";
    	CALTextArea.setText(ruleCode);
    	
    	updatePreview();
    }
    
    public void savePropertiesToWorld()
    {
    	Grid grid = world.getInitialGeneration();
    	
        Vector2i size = new Vector2i(worldSizeXNumberBox.getValue(5), worldSizeYNumberBox.getValue(5));

        GridType type;
        if (cellShapeDropDownBox.getSelectedText() == "Triangle")
        	type = GridType.TRIANGLE;
        else if (cellShapeDropDownBox.getSelectedText() == "Hexagon")
        	type = GridType.HEXAGON;
        else
        	type = GridType.RECTANGLE;
        
        if (!grid.getSize().equals(size) || grid.getType() != type)
        {
        	Log.information(TAG, "Recreating grid, the current state will be lost");
        	grid = type.create(size, grid.getNumProperties());
        	
			grid.getCell(2, 4).setValue(1, 1);
			grid.getCell(3, 4).setValue(1, 1);
			grid.getCell(4, 4).setValue(1, 1);
			grid.getCell(4, 3).setValue(1, 1);
			grid.getCell(3, 2).setValue(1, 1);
        }
        
        grid.setWrappable(wrapCheckBox.isChecked());
        
        world.reset();
        world.setWorldGenerations(new Grid[] {grid});
    }
    
    
    public void createPreviewTab()
    {
    	
    	previewViewport.recreate(world.getInitialGeneration(), window);
    	
    	
    
    	
    	
    	
	        
    }
    
   
    
    public void updatePreview()
    { 
    	Grid grid = world.getInitialGeneration();
    	if (grid == null)
    	{
    		Log.error(TAG, "World doesn't have an initial grid");
    		return;
    	}
    	
    	Log.debug(TAG, grid.toString());
    	
    	switch (grid.getType())
    	{
    		case RECTANGLE:
	            grid3DViewer = new RectangleGrid3DWidget(new Vector2i(400, 300), (RectangleGrid)grid, 24);
	            grid3DViewer.setFlag(Widget.FILL);
	            grid3DViewer.addSlice(1, 1, 16.0f);
	            widget3DPreviewContainer.setContents(grid3DViewer);
	
	            gridViewer = new RectangleGridWidget((RectangleGrid)grid, 16);
	            gridViewer.setColourProperty(1);
	            gridViewer.setColourRuleSet(colourRules);
	            widgetPreviewContainer.setContents(gridViewer);
	            break;
	            
    		case TRIANGLE:
	        	grid3DViewer = new TriangleGrid3DWidget(new Vector2i(400, 300), (TriangleGrid)grid, 24);
	            grid3DViewer.setFlag(Widget.FILL);
	            grid3DViewer.addSlice(1, 1, 16.0f);
	            widget3DPreviewContainer.setContents(grid3DViewer);
	            
	            gridViewer = new TriangleGridWidget((TriangleGrid)grid, 32);
	            gridViewer.setColourProperty(1);
	            gridViewer.setColourRuleSet(colourRules);
	            widgetPreviewContainer.setContents(gridViewer);      
	            break;
	            
            case HEXAGON:
	            grid3DViewer = new HexagonGrid3DWidget(new Vector2i(400, 300), (HexagonGrid)grid, 24);
	            grid3DViewer.setFlag(Widget.FILL);
	            grid3DViewer.addSlice(1, 1, 16.0f);
	            widget3DPreviewContainer.setContents(grid3DViewer);
	    
	            gridViewer = new HexagonGridWidget((HexagonGrid)grid, 16);
	            gridViewer.setColourProperty(1);
	            gridViewer.setColourRuleSet(colourRules);
	            widgetPreviewContainer.setContents(gridViewer);
	            break;
        }
    	
    	
    }
        
    public void updateSimulationScreen(boolean force)
    {
    	if (world != null)
    	{
    		// Update slider
    		int	generations = world.getNumGenerations() - 1;
			int origMaximum = generationSlider.getMaximum();
			
			generationSlider.setMaximum(world.getNumGenerations() - 1);
					
    		if (generationSlider.getValue() >= origMaximum)
    			generationSlider.setValue(world.getNumGenerations() - 1);
    		
    		// Update tex
    		iterationsText.setCaption("Generations: " + generations);

    		// Update viewports
    		int		generation = generationSlider.getValue();    		
    		Grid 	grid = world.getGeneration(generation);
    		
    		if (grid != null && (currentGeneration != generation || force))
    		{
    			currentGeneration = generation;
    			currentGrid = grid;
    			
    			if (currentGrid.getType() == grid.getType() && !force)
    			{
	    			for (Viewport viewport : viewports)
	    				viewport.gridWidget.setGrid(currentGrid);
    			}
    			else
    			{
	   				for (Viewport viewport : viewports) 
	   					viewport.recreate(grid, window);
    			}
    		}
    	}
    }
    
    public void showDialog(String caption, String message)
    {
    	dialogTitle.setCaption(caption);
    	dialogMessage.setCaption(message);
    	window.showModalDialog(dialog);
    }
    
    public void refreshClients()
    {
    	availableClients.clear();
    	usingClients.clear();
    	
		ServerEvent serverEvent = new ServerEvent(ServerEvent.Type.PING_CLIENTS);
		server.sendEvent(serverEvent);
    }
    
    public void updateAvailableClientsList()
    {
    	clientsAvailableList.clear();
    	clientsUsingList.clear();
    	
    	for (ClientEntry client : availableClients)
    		clientsAvailableList.addItem(computerIcon, client.address.getHostName());
			
		for (ClientEntry client : usingClients)
			clientsUsingList.addItem(computerLinkIcon, client.address.getHostName());    			
	}
   
    @Override
    public void update(float delta)
    {
    	updateSimulationScreen(false);
    	
        if (!themeName.equals(currentThemeName))
        {
            System.out.println("Changing theme to " + themeName);
            
            theme.loadTheme(themeName);
            currentThemeName = themeName;
            
            window.relayout();
        }    
    }
    
    @Override
    public void render()
    {
    	
    }
    
    @Override
    public boolean close()
    {
    	ServerEvent serverEvent = new ServerEvent(ServerEvent.Type.SHUTDOWN);  
    	server.sendEvent(serverEvent);
    	
    	return false;
    }

    public void constructColoursTab()
    {
    	int numProperties = world.getGeneration(0).getNumProperties();
    	
    	for (int i = 0; i < numProperties; i++)
    	{
    		TextWidget t = new TextWidget("Property " + i + " goes here!");
    		masterColoursLayout.add(t);
    	}
    	
    }
    
    
    @Override
    public void handleWindowEvent(Event event)
    {
        if (event.type == Event.Type.ACTION)
        {
            if (event.target == createWorldButton)
            {            	
                ServerEvent serverEvent = new ServerEvent(ServerEvent.Type.CREATE_WORLD);
                serverEvent.size = new Vector2i(32, 32);
                serverEvent.gridType = GridType.RECTANGLE;
                serverEvent.wrappable = true;
                server.sendEvent(serverEvent);
                
                refreshClients();
            }
            else if (event.target == loadWorldButton)
            {
                FileSelectResult result = window.askUserForFileToLoad("Load a world");
                
                if (result.isValid())
                {
                    ServerEvent serverEvent = new ServerEvent(ServerEvent.Type.LOAD_WORLD);
                    serverEvent.filename = result.getFullPath();
                    server.sendEvent(serverEvent);
                }
                
                refreshClients();
            }
            else if (event.target == helpButton)
            {
            	
            }
            else if (event.target == quitButton)
            {
                window.exit();
            }
            else if ((event.target == dialogOKButton) || (event.target == dialogCALOKButton))
            {
                window.closeModalDialog();
            }
            else if (event.target == backButton)
            {
                masterView.setIndex(0);
            }
            else if (event.target == submitButton)
            {            	
                if(( worldSizeXNumberBox.getValue(5) < 5) || ( worldSizeYNumberBox.getValue(5) < 5))
                {
                    window.showModalDialog(dialog);
                    return;
                }
                
                savePropertiesToWorld();
                updatePreview();
                createPreviewTab();
            }
            else if (event.target == clearRulesButton)
            {
                CALTextArea.clear();
            }
            else if (event.target == submitRulesButton)
            {
            	String CALCode = CALTextArea.getText();
                CALCompiler compiler = new CALCompiler();
                compiler.compile(CALCode);
                
                TextWidget text = new TextWidget("Compiler Report:");
                outputLayout.add(text);
            
                outputLayout = new LinearLayout(LinearLayout.Direction.VERTICAL);
                outputLayout.setMargin(new Vector2i(0, 0));
                outputLayout.setFlag(Widget.WRAP);
                outputContainer.setContents(outputLayout);
 
                for (String result : compiler.getResult())
                {
                      outputLayout.add(new TextWidget(result));
                      window.relayout();
                }
                
                if (compiler.getErrorCount() == 0)
                {
                	Log.information(TAG, "Loading rule code into World");
                	world.setRuleCode(CALCode);
                	
                	constructColoursTab();
                	
                }
                else
                {
                	Log.information(TAG, "Rule code contains " + compiler.getErrorCount() + " errors");
                	world.setRuleCode("");
                }
            }
            
            else if (event.target == saveCALFileButton)
            {
            	if (selectedFile == null)
            	{
            		  selectedFile = window.askUserForFileToSave("Select a location to save", "cal");
                	  
                	  File f = new File(selectedFile.getFullPath() + ".cal");
                	 
                	  try {
    					f.createNewFile();
    					PrintWriter out = new PrintWriter(f);
    			        
    					out.write(CALTextArea.getText());
    					
    					out.close();
    					
    					
    					
    				} catch (IOException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
            	}
            	else
            	{
            	  File f = new File(selectedFile.getFullPath() + ".cal");
              	  try {
  					PrintWriter out = new PrintWriter(f);
  			        
  					out.write(CALTextArea.getText());
  					
  					out.close();
  					
  					
  					
  				} catch (IOException e) {
  					// TODO Auto-generated catch block
  					e.printStackTrace();
  				}
            	}
            }
            
            else if (event.target == saveAsCALFileButton)
            {
            	
            	  
            	  FileSelectResult calFile = window.askUserForFileToSave("Select a location to save", "cal");
            	  
            	  File f = new File(calFile.getFullPath() + ".cal");
            	  System.out.println(calFile.getFullPath() + "/" + calFile.filename);
            	  try {
					f.createNewFile();
					PrintWriter out = new PrintWriter(f);
			        out.write(CALTextArea.getText());
					out.close();
					
					
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	  
            }
            
            else if (event.target == openCALFileButton)
            {
                selectedFile = window.askUserForFileToLoad("Select CAL File", "cal");
                
                System.out.println(selectedFile.directory);
                System.out.println(selectedFile.filename);
                
                if (selectedFile.isValid())
                {
	                if (selectedFile.filename.contains(".cal"))
	                {
	                    try
	                    {
							FileInputStream fstream = new FileInputStream(selectedFile.directory + selectedFile.filename);
							DataInputStream in = new DataInputStream(fstream);
							BufferedReader br = new BufferedReader(new InputStreamReader(in));
							String strLine;
							String output = "";
							  
							while ((strLine = br.readLine()) != null)
								output += strLine + "\n";
							  
							in.close();
							 
							CALTextArea.setText(output);
	                    } 
	                    catch (Exception e) 
	                    { 
	                    	window.showModalDialog(dialogCAL);
	                    }
	                } 
                }
            }
            else if (event.target == simulateButton)
            {
            	String ruleCode = world.getRuleCode();
            	if (ruleCode == null || ruleCode.equals(""))
            	{
            		showDialog("Simulation", "Cell rules not set yet");
            		return;
            	}
            	
            	List<InetSocketAddress> clients = new ArrayList<InetSocketAddress>();
            	
            	for (ClientEntry clientEntry : usingClients)
            		clients.add(clientEntry.address);
            	
                ServerEvent serverEvent = new ServerEvent(ServerEvent.Type.READY_SIMULATION);
                serverEvent.clients = clients;
                server.sendEvent(serverEvent);
            	
            	masterView.setIndex(2);
            	Log.information(TAG, "Switched to simulation screen");

            	currentGeneration = 0;
            	updateSimulationScreen(true);
            }
            else if (event.target == playButton)
            {
                ServerEvent serverEvent = new ServerEvent(ServerEvent.Type.START_SIMULATION);
                server.sendEvent(serverEvent);
            }
            else if (event.target == pauseButton)
            {
                ServerEvent serverEvent = new ServerEvent(ServerEvent.Type.PAUSE_SIMULATION);
                server.sendEvent(serverEvent);
            }            
            else if (event.target == resetButton)
            {
                ServerEvent serverEvent = new ServerEvent(ServerEvent.Type.RESET_SIMULATION);
                server.sendEvent(serverEvent);
            }        
            else if (event.target == stepForwardButton)
            {
                ServerEvent serverEvent = new ServerEvent(ServerEvent.Type.STEP_SIMULATION);
                server.sendEvent(serverEvent);           	
            }
            else if (event.target == addViewportButton)
            {
            	Container container = new Container(new Vector2i(100,300));
            	container.setFlag(Widget.FILL);
            	container.setBackground(new Fill(new Colour(0f,0f,0f)));
            	viewportsLayout.add(container);
            	
            	Viewport viewport = new Viewport(container, Viewport.Type.THREE_D);
            	viewport.recreate(currentGrid, window);
            	
            	viewports.add(viewport);
            }
            
            //PREVIEW GRID
            
            // VIEWPORT CAMERA
            
            else if (event.target == zoomOutButton)
            {
            	for (Viewport viewport : viewports)
            	{
    				if (viewport.gridWidget.hasFocus())
    				{
    					if (viewport.type == Viewport.Type.THREE_D)
    					{
    						Grid3DWidget temp3DWidget = (Grid3DWidget) viewport.gridWidget;
    						temp3DWidget.move(new Vector3f(0, 0, 1));
    					}
    				}
            	}
            }
            
            else if (event.target == zoomInButton)
            {
            	for (Viewport viewport : viewports)
            	{
    				if (viewport.gridWidget.hasFocus())
    				{
    					if (viewport.type == Viewport.Type.THREE_D)
    					{
    						Grid3DWidget temp3DWidget = (Grid3DWidget) viewport.gridWidget;
    						temp3DWidget.move(0, 0, -1);
    					}
    				}
            	}
            }
            
            else if (event.target == zoomOutButton)
            {
            	for (Viewport viewport : viewports)
            	{
    				if (viewport.gridWidget.hasFocus())
    				{
    					if (viewport.type == Viewport.Type.THREE_D)
    					{
    						Grid3DWidget temp3DWidget = (Grid3DWidget) viewport.gridWidget;
    						temp3DWidget.move(0, 0, 1);
    					}
    				}
            	}
            }
			else if (event.target ==moveUpButton)
	        {
	        	for (Viewport viewport : viewports)
	        	{
					if (viewport.gridWidget.hasFocus())
					{
						if (viewport.type == Viewport.Type.THREE_D)
						{
							Grid3DWidget temp3DWidget = (Grid3DWidget) viewport.gridWidget;
							temp3DWidget.move(0, -1, 0);
						}
					}
	        	}
	        }
			else if (event.target ==moveDownButton)
			{
	        	for (Viewport viewport : viewports)
	        	{
					if (viewport.gridWidget.hasFocus())
					{
						if (viewport.type == Viewport.Type.THREE_D)
						{
    						Grid3DWidget temp3DWidget = (Grid3DWidget) viewport.gridWidget;
    						temp3DWidget.move(0, 1, 0);
    					}
    				}
            	}
            }
			else if (event.target ==moveLeftButton)
	        {
            	for (Viewport viewport : viewports)
            	{
    				if (viewport.gridWidget.hasFocus())
    				{
    					if (viewport.type == Viewport.Type.THREE_D)
    					{
    						Grid3DWidget temp3DWidget = (Grid3DWidget) viewport.gridWidget;
    						temp3DWidget.move(-1, 0, 0);
    					}
    				}
            	}
	        }
			else if (event.target ==moveRightButton)
	        {
            	for (Viewport viewport : viewports)
            	{
    				if (viewport.gridWidget.hasFocus())
    				{
    					if (viewport.type == Viewport.Type.THREE_D)
	    					{
	    						Grid3DWidget temp3DWidget = (Grid3DWidget) viewport.gridWidget;
	    						temp3DWidget.move(1, 0, 0);
	    					}
	    				}
	            	}
			}
			else if (event.target ==yawLeftButton)
			{
            	for (Viewport viewport : viewports)
            	{
    				if (viewport.gridWidget.hasFocus())
    				{
    					if (viewport.type == Viewport.Type.THREE_D)
    					{
    						Grid3DWidget temp3DWidget = (Grid3DWidget) viewport.gridWidget;
    						temp3DWidget.changeYaw(2);
    					}
	            	}
            	}
            }
            else if (event.target ==yawRightButton)
            {
            	for (Viewport viewport : viewports)
            	{
    				if (viewport.gridWidget.hasFocus())
    				{
    					if (viewport.type == Viewport.Type.THREE_D)
    					{
    						Grid3DWidget temp3DWidget = (Grid3DWidget) viewport.gridWidget;
    						temp3DWidget.changeYaw(-2);
    					}
    				}
	            }
			}
            else if (event.target == pitchUpButton)
	        {
            	for (Viewport viewport : viewports)
            	{
    				if (viewport.gridWidget.hasFocus())
    				{
    					if (viewport.type == Viewport.Type.THREE_D)
    					{
    						Grid3DWidget temp3DWidget = (Grid3DWidget) viewport.gridWidget;
    						temp3DWidget.changePitch(2);
    					}
    				}
            	}
            }
            else if (event.target == pitchDownButton)
            {
            	for (Viewport viewport : viewports)
            	{
    				if (viewport.gridWidget.hasFocus())
    				{
    					if (viewport.type == Viewport.Type.THREE_D)
    					{
    						Grid3DWidget temp3DWidget = (Grid3DWidget) viewport.gridWidget;
    						temp3DWidget.changePitch(-2);
    					}
    				}
            	}
            }

            //VIEWPORT SETTINGS BUTTONS
            

			else if (event.target == toggle3dButton)
			{
				for (Viewport viewport : viewports)
				{	
					if (viewport.gridWidget.hasFocus()) 
						viewport.switchDimension(currentGrid, window);
				} 
			}
			else if (event.target == toggleHideButton)
			{
				 				 
			}
			else if (event.target == toggleShowButton)
			{
				 				 
			}
			else if (event.target == toggleWireframeButton)
			{
				for (Viewport viewport : viewports)
				{	
					if (viewport.gridWidget.hasFocus()) 
						viewport.gridWidget.toggleDrawWireframe();
				} 	 				 
			}

            // DISTRIBUTION BUTTONS
			else if (event.target == refreshServerButton)
			{
				refreshClients();
			}
			else if (event.target == addAllClientsButton)
			{
				for (ClientEntry clientEntry : availableClients) usingClients.add(clientEntry);
				
				availableClients.clear();

				updateAvailableClientsList();
			}
			else if (event.target == removeAllClientsButton)
			{
				for (ClientEntry clientEntry : usingClients) availableClients.add(clientEntry);
				
				usingClients.clear();

				updateAvailableClientsList();
			} 
			else if (event.target == addClientButton)
			{
				String hostname = clientsAvailableList.getSelectedText();
				
				for (ClientEntry clientEntry : availableClients)
				{
					if (clientEntry.address.getHostName().equals(hostname))
					{
						availableClients.remove(clientEntry);
						usingClients.add(clientEntry);
					}
				}
				
				updateAvailableClientsList();
			}
			else if (event.target == removeClientButton)
			{
				String hostname = clientsUsingList.getSelectedText();
				
				for (ClientEntry clientEntry : usingClients)
				{
					if (clientEntry.address.getHostName().equals(hostname))
					{
						usingClients.remove(clientEntry);
						availableClients.add(clientEntry);
					}
				}
				
				updateAvailableClientsList();
			}
            //COLOUR RANGES
            
			///PREVIEW
                      
			 else if (event.target == setCellValueButton)
			 {
				 int numProperties = world.getInitialGeneration().getNumProperties();
				 
				 for (int i = 0; i < numProperties; i++)
				 {
					 int newValue = numberboxList.get(i).getValue(0);
					 
					 System.out.println("VALUE:" + newValue);
					 
					c.setValue(i, newValue);
					 
					 
					 
				 }
			 }
            
        }
        else if (event.type == Event.Type.CHANGE)
        {
            if (event.target == generationSlider)
            {
                ServerEvent serverEvent = new ServerEvent(ServerEvent.Type.PAUSE_SIMULATION);
                server.sendEvent(serverEvent);
            }
            
           
            //PREVIEW GRID
            else if (event.target == previewViewport.gridWidget)
            {
				Grid2DWidget temp2DWidget = (Grid2DWidget) previewViewport.gridWidget;
				
				c = world.getInitialGeneration().getCell(temp2DWidget.getSelectedCell());
				
            	int numProperties = world.getInitialGeneration().getNumProperties();
            	
            	System.out.println(numProperties);
            	
            	rightLayout.clear();
            	
            	 setCellValueButton = new Button(new Vector2i(70,43), "SET");
             	setCellValueButton.setFlag(Widget.CENTER_HORIZONTAL);
                 rightLayout.add(setCellValueButton);
            	
            	numberboxList = new ArrayList<NumberBox>();
            	
            	
            	for (int i = 0 ; i < numProperties; i++)
            	{
            	LinearLayout cellPropertyLayout = new LinearLayout(LinearLayout.Direction.HORIZONTAL);
            	cellPropertyLayout.setBorder(new Fill(new Colour(0.7F, 0.7F, 0.7F)));
            	
            	cellPropertyLayout.setHeight(40);
            	cellPropertyLayout.setWidth(250);
            	cellPropertyLayout.setFlag(Widget.CENTER_HORIZONTAL);
            	rightLayout.add(cellPropertyLayout);
            	
            
            	
            	TextWidget t = new TextWidget(CodeGen.getPropertyList().get(i));
            	cellPropertyLayout.add(t);
            	
            	NumberBox n = new NumberBox(40);
            	n.setValue((int) c.getValue(i));
            	cellPropertyLayout.add(n);
            	
            	numberboxList.add(n);
            	
            	
            	}
            	
            	
            	
			}
        }
    }

	@Override
	public void foundClient(InetSocketAddress address)
	{
		if (availableClients != null)
		{
			if (address.getHostName().equals("localhost")) return;
				
			ClientEntry clientEntry = new ClientEntry(address);
			availableClients.add(clientEntry);
			
			updateAvailableClientsList();
		}
	}
}
