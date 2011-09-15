package com.hexcore.cas.ui.test;

import java.awt.Color;

import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.model.Cell;
import com.hexcore.cas.model.ColourRule;
import com.hexcore.cas.model.ColourRuleSet;
import com.hexcore.cas.model.RectangleGrid;
import com.hexcore.cas.model.HexagonGrid;
import com.hexcore.cas.model.TriangleGrid;
import com.hexcore.cas.test.GameOfLife;
import com.hexcore.cas.test.WaterFlow;
import com.hexcore.cas.ui.Button;
import com.hexcore.cas.ui.CheckBox;
import com.hexcore.cas.ui.Colour;
import com.hexcore.cas.ui.Container;
import com.hexcore.cas.ui.Dialog;
import com.hexcore.cas.ui.DropDownBox;
import com.hexcore.cas.ui.Event;
import com.hexcore.cas.ui.Fill;
import com.hexcore.cas.ui.HexagonGrid3DWidget;
import com.hexcore.cas.ui.HexagonGridWidget;
import com.hexcore.cas.ui.Image;
import com.hexcore.cas.ui.ImageWidget;
import com.hexcore.cas.ui.Layout;
import com.hexcore.cas.ui.LinearLayout;
import com.hexcore.cas.ui.Panel;
import com.hexcore.cas.ui.RectangleGrid3DWidget;
import com.hexcore.cas.ui.RectangleGridWidget;
import com.hexcore.cas.ui.ScrollableContainer;
import com.hexcore.cas.ui.SliderWidget;
import com.hexcore.cas.ui.TabbedView;
import com.hexcore.cas.ui.Text;
import com.hexcore.cas.ui.TextArea;
import com.hexcore.cas.ui.Text.Size;
import com.hexcore.cas.ui.TextBox;
import com.hexcore.cas.ui.TextWidget;
import com.hexcore.cas.ui.Theme;
import com.hexcore.cas.ui.TriangleGrid3DWidget;
import com.hexcore.cas.ui.TriangleGridWidget;
import com.hexcore.cas.ui.View;
import com.hexcore.cas.ui.Widget;
import com.hexcore.cas.ui.Window;
import com.hexcore.cas.ui.WindowEventListener;

public class UIProtoApplication implements WindowEventListener
{
	public Theme	theme;
	public Window	window;
	
	
	public View masterView;
	
	
	public TabbedView tabbedWorldView;
	
	public LinearLayout	mainMenuLayout;
	public LinearLayout worldLayout;
	
	
	public TextWidget worldEditorLabel;
	
	public Button simulateButton;
	
	//1st,2,3,4 ...tab
	
	public Button backButton;
	public Container propertiesContainer;
		
		public LinearLayout masterPropertiesLayout;
		public Container widgetPreviewContainer;
		public Container widget3DPreviewContainer;
		public LinearLayout widgetPreviewLayout;
		public LinearLayout cellShapeLayout;
		
		public GameOfLife			rectGameOfLife;
		public GameOfLife			triGameOfLife;
		public GameOfLife			hexGameOfLife;
		public LinearLayout propertiesLayout;
		public LinearLayout worldSizeLayout;
	
		public TextWidget worldSizeLabel;
		public TextWidget worldSizeXLabel;
		public TextWidget cellShapeLabel;
	
		
		public TextBox	worldSizeXTextBox;
		public TextBox	worldSizeYTextBox;
	
		
		public CheckBox wrapCheckBox;
		public DropDownBox cellShapeDropDownBox;
		
		public Button submitButton;
		
		public RectangleGrid3DWidget	rectGrid3DViewer;
		public HexagonGrid3DWidget		hexGrid3DViewer;
		public TriangleGrid3DWidget		triGrid3DViewer;
		
		public HexagonGridWidget gridViewer;
		public RectangleGridWidget rectGridViewer;
		public TriangleGridWidget triGridViewer;
		
		
	public Container rulesContainer;
		public LinearLayout rulesLayout;
		
		public Button clearRulesButton;
		public Button submitRulesButton;
		
		TextArea CALTextArea;
	
	
	public Container distributionContainer;
	public Container worldPreviewContainer;
	
	
	
	
	public LinearLayout	headerLayout;
	public LinearLayout	mainLayout;
	public LinearLayout	buttonBarLayout;
	public LinearLayout	innerLayout;
	
	public TextBox		nameTextBox;
	public CheckBox		checkBox;
	public DropDownBox	dropDownBox;
	public ImageWidget	headingImage;
	public Container	headingContainer;
	public TextWidget	headingLabel;
	public View			mainView;
	
	public Button		createWorldButton;
	public Button		loadWorldButton;
	public Button		optionsButton;
	public Button		helpButton;
	public Button		quitButton;
		
	public Panel		mainPanel;
	
	public LinearLayout			gridViewLayout;


	public Button				nextIterationButton;
	
	public ColourRuleSet		colourRules;
	
	public String	currentThemeName = "lightV2";
	public String	themeName = currentThemeName;
	
	public Dialog		dialog;
	public LinearLayout	dialogLayout;
	public TextWidget	dialogTitle;
	public TextWidget	dialogMessage;
	public Button		dialogOKButton;
	
	
	public HexagonGrid grid;
	public RectangleGrid rectGrid;
	public TriangleGrid triGrid;
	
	
	
	
	//Simulation Screen
	
	public Container simulationContainer;
	
	
	
	public HexagonGrid				waterFlowGrid;
	public WaterFlow				waterFlow;
	public HexagonGrid3DWidget		waterGrid3DViewer;
	
	UIProtoApplication()
	{
		
		waterFlowGrid = new HexagonGrid(new Vector2i(100, 100));
		waterFlowGrid.setWrappable(false);

		waterFlow = new WaterFlow(waterFlowGrid);
		
		
		grid = new HexagonGrid(new Vector2i(12, 12));
		grid.getCell(6, 5).setValue(0, 1);
		grid.getCell(6, 6).setValue(0, 1);
		grid.getCell(6, 7).setValue(0, 1);		
		hexGameOfLife = new GameOfLife(grid);
		
		rectGrid = new RectangleGrid(new Vector2i(12, 12));
		rectGrid.getCell(2, 4).setValue(0, 1);
		rectGrid.getCell(3, 4).setValue(0, 1);
		rectGrid.getCell(4, 4).setValue(0, 1);
		rectGrid.getCell(4, 3).setValue(0, 1);
		rectGrid.getCell(3, 2).setValue(0, 1);
		rectGameOfLife = new GameOfLife(rectGrid);
		
		triGrid = new TriangleGrid(new Vector2i(12, 12));
		triGrid.getCell(7, 6).setValue(0, 1);
		triGrid.getCell(7, 7).setValue(0, 1);
		triGrid.getCell(7, 8).setValue(0, 1);
		triGrid.getCell(6, 6).setValue(0, 1);
		triGrid.getCell(6, 7).setValue(0, 1);
		triGrid.getCell(6, 8).setValue(0, 1);
		triGameOfLife = new GameOfLife(triGrid);
		
		
		colourRules = new ColourRuleSet(3);
		ColourRule	colourRule;
		
		colourRule = new ColourRule();
		colourRule.addRange(new ColourRule.Range(0.0, 1.0, new Colour(0.0f, 0.25f, 0.5f)));
		colourRule.addRange(new ColourRule.Range(1.0, 2.0, new Colour(0.0f, 0.8f, 0.5f)));
		colourRules.setColourRule(0, colourRule);
		
		colourRule = new ColourRule();
		colourRule.addRange(new ColourRule.Range(0.0, 15.1, new Colour(0.0f, 0.5f, 0.8f), new Colour(0.0f, 0.25f, 0.5f)));
		colourRules.setColourRule(1, colourRule);	
		
		colourRule = new ColourRule();
		colourRule.addRange(new ColourRule.Range(0.0, 8.0, new Colour(0.5f, 0.25f, 0.0f), new Colour(0.0f, 0.8f, 0.5f)));
		colourRule.addRange(new ColourRule.Range(8.0, 16.0, new Colour(0.0f, 0.8f, 0.5f), new Colour(0.4f, 1.0f, 0.8f)));
		colourRules.setColourRule(2, colourRule);	
		
		theme = new Theme();
		window = new Window("Cellular Automata Simulator - v1.0", 1024, 700, theme);
		
		window.addListener(this);
		window.show();
	}
	
	public void initialise()
	{
		theme.loadTheme(themeName);
		
		masterView = new View(new Vector2i(10, 10));
		masterView.setMargin(new Vector2i(0, 0));
		masterView.setFlag(Widget.FILL);
		window.add(masterView);
		
		mainMenuLayout = new LinearLayout(LinearLayout.Direction.VERTICAL);
		mainMenuLayout.setFlag(Widget.FILL);
		mainMenuLayout.setMargin(new Vector2i(0, 0));
		masterView.add(mainMenuLayout);
		
		headerLayout = new LinearLayout(new Vector2i(100, 100), LinearLayout.Direction.HORIZONTAL);
		headerLayout.setFlag(Widget.FILL_HORIZONTAL);
		headerLayout.setMargin(new Vector2i(0, 0));
		mainMenuLayout.add(headerLayout);
						
		headingImage = new ImageWidget("data/logo2.png");
		headingImage.setFlag(Widget.CENTER);
		headerLayout.add(headingImage);		
		
		headingContainer = new Container(new Vector2i(100, 100));
		headingContainer.setFlag(Widget.FILL);
		headerLayout.add(headingContainer);
		
		
		mainLayout = new LinearLayout(new Vector2i(100, 100), LinearLayout.Direction.HORIZONTAL);
		mainLayout.setFlag(Widget.FILL);
		mainMenuLayout.add(mainLayout);
		
		buttonBarLayout = new LinearLayout(new Vector2i(250, 50), LinearLayout.Direction.VERTICAL);
		buttonBarLayout.setMargin(new Vector2i(0, 0));
		buttonBarLayout.setFlag(Widget.FILL_VERTICAL);
		mainLayout.add(buttonBarLayout);
		
		createWorldButton = new Button(new Vector2i(100, 50), "Create New World", "Create a blank world");
		createWorldButton.setFlag(Widget.FILL_HORIZONTAL);
		buttonBarLayout.add(createWorldButton);
		
		loadWorldButton = new Button(new Vector2i(100, 50), "Load World", "Load a world from a file");
		loadWorldButton.setFlag(Widget.FILL_HORIZONTAL);
		buttonBarLayout.add(loadWorldButton);
		
		optionsButton = new Button(new Vector2i(100, 50), "Options", "Change preferences and settings");
		optionsButton.setFlag(Widget.FILL_HORIZONTAL);
		buttonBarLayout.add(optionsButton);
		
		helpButton = new Button(new Vector2i(100, 50), "Help", "Manual and About Us");
		helpButton.setFlag(Widget.FILL_HORIZONTAL);
		buttonBarLayout.add(helpButton);
		
		quitButton = new Button(new Vector2i(100, 50), "Quit", "All unsaved changed will be lost");
		quitButton.setFlag(Widget.FILL_HORIZONTAL);
		buttonBarLayout.add(quitButton);
		
		mainPanel = new Panel(new Vector2i(10, 10));
		mainPanel.setFlag(Widget.FILL);
		mainLayout.add(mainPanel);
		
		mainView = new View(new Vector2i(10, 10));
		mainView.setMargin(new Vector2i(0, 0));
		mainView.setFlag(Widget.FILL);
		mainPanel.setContents(mainView);
		
		innerLayout = new LinearLayout(LinearLayout.Direction.VERTICAL);
		innerLayout.setFlag(Widget.FILL);
		
		
		/// Main WORLD BUILDER
		
		
		worldLayout = new LinearLayout(LinearLayout.Direction.VERTICAL);
		worldLayout.setFlag(Widget.FILL);
		
		masterView.add(worldLayout);
		
		
		tabbedWorldView = new TabbedView(new Vector2i(30,30));
		tabbedWorldView.setFlag(Widget.FILL);
		
		
		
		LinearLayout worldHeaderLayout = new LinearLayout(new Vector2i(100, 30), LinearLayout.Direction.HORIZONTAL);
		worldHeaderLayout.setFlag(Widget.FILL_HORIZONTAL);
		worldLayout.add(worldHeaderLayout);
		
		
		
		headingImage = new ImageWidget("data/logo3.png");
		
		headingImage.setFlag(Widget.CENTER_HORIZONTAL);
	worldHeaderLayout.add(headingImage);

		
		
	
	//	worldHeaderLayout.add(minimizeImage);
	//	worldHeaderLayout.add(fullscreenImage);
	//	worldHeaderLayout.add(quitImage);
		
		
		
	
		
		
//		worldEditorLabel = new TextWidget("World Editor Menu", Text.Size.LARGE);
		
		
	//	worldEditorLabel.setFlag(Widget.CENTER_HORIZONTAL);
		//worldLayout.add(worldEditorLabel);
		worldLayout.add(tabbedWorldView);
		
		// OUR WORLD EDITOR Containers
		
		
		propertiesContainer = new Container(new Vector2i(100, 100));	
		propertiesContainer.setFlag(Widget.FILL);
		tabbedWorldView.add(propertiesContainer, "World Properties");
		
		masterPropertiesLayout = new LinearLayout(LinearLayout.Direction.VERTICAL);
		masterPropertiesLayout.setFlag(Widget.FILL);
		propertiesContainer.setContents(masterPropertiesLayout);
		
	
		
   	LinearLayout instructionsLayout = new LinearLayout(LinearLayout.Direction.HORIZONTAL);
   	//instructionsLayout.setBackground(new Fill(new Colour(0.4f, 0.63f, 0.91f)));

   	instructionsLayout.setHeight(35);

   	instructionsLayout.setFlag(Widget.CENTER_HORIZONTAL);
   	   	masterPropertiesLayout.add(instructionsLayout);
   	
	instructionsLayout.setBorder(new Fill(new Colour(0.6f,0.6f,0.6f)));
	
   
	
	TextWidget propertiesInstructions = new TextWidget("Specify your world properties such as cell shape, world size and whether the world is wrappable.");
	instructionsLayout.add(propertiesInstructions);
		
   	instructionsLayout.setWidth(propertiesInstructions.getWidth()+ 20);
		
   	
   	
   		//2d

	
	gridViewer = new HexagonGridWidget((HexagonGrid)hexGameOfLife.getGrid(), 16);
	gridViewer.setColourRuleSet(colourRules);

	

	
	rectGridViewer = new RectangleGridWidget((RectangleGrid)rectGameOfLife.getGrid(), 24);
	rectGridViewer.setColourRuleSet(colourRules);
	



	triGridViewer = new TriangleGridWidget((TriangleGrid)triGameOfLife.getGrid(), 32);
	triGridViewer.setColourRuleSet(colourRules);
	
   	
   		//
		
		rectGrid3DViewer = new RectangleGrid3DWidget(new Vector2i(400, 300), (RectangleGrid)rectGameOfLife.getGrid(), 24);
		rectGrid3DViewer.setFlag(Widget.FILL);
		rectGrid3DViewer.addSlice(0, 16.0f);
		
		triGrid3DViewer = new TriangleGrid3DWidget(new Vector2i(400, 300), (TriangleGrid)triGameOfLife.getGrid(), 24);
		triGrid3DViewer.setFlag(Widget.FILL);
		triGrid3DViewer.addSlice(0, 16.0f);
		
		hexGrid3DViewer = new HexagonGrid3DWidget(new Vector2i(400, 300), (HexagonGrid)hexGameOfLife.getGrid(), 24);
		hexGrid3DViewer.setFlag(Widget.FILL);
	    hexGrid3DViewer.addSlice(0, 16.0f);
		
	    
	 // Water Flow
		waterGrid3DViewer = new HexagonGrid3DWidget(new Vector2i(400, 300), (HexagonGrid)waterFlow.getGrid(), 24);
		waterGrid3DViewer.setFlag(Widget.FILL);
		waterGrid3DViewer.setBackgroundColour(new Colour(0.6f, 0.85f, 1.0f));
		waterGrid3DViewer.setColourRuleSet(colourRules);
		waterGrid3DViewer.addSlice(2, 2, 15.0f);
		waterGrid3DViewer.addSlice(1, 1, 15.0f);
		
		
		
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
		
		worldSizeXTextBox = new TextBox(35);
		worldSizeXTextBox.setFlag(Widget.CENTER_VERTICAL);
		worldSizeLayout.add(worldSizeXTextBox);
		
		worldSizeXLabel = new TextWidget("X", Size.LARGE);
		worldSizeXLabel.setFlag(Widget.CENTER_VERTICAL);
		worldSizeLayout.add(worldSizeXLabel);
		
		worldSizeYTextBox = new TextBox(35);
		worldSizeYTextBox.setFlag(Widget.CENTER_VERTICAL);
		worldSizeLayout.add(worldSizeYTextBox);
		
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
				
		submitButton = new Button(new Vector2i(120,35), "Preview");
		propertiesLayout.add(submitButton);
		
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
		
		widget3DPreviewContainer.setContents(rectGrid3DViewer);
		widgetPreviewContainer.setContents(rectGridViewer);
		

		LinearLayout buttonHeaderLayout = new LinearLayout(new Vector2i(525, 50), LinearLayout.Direction.HORIZONTAL);
		buttonHeaderLayout.setBackground(new Fill(new Colour(0.7f, 0.7f, 0.7f)));
		buttonHeaderLayout.setBorder(new Fill(new Colour(0.7f, 0.7f, 0.7f)));
		buttonHeaderLayout.setFlag(Widget.CENTER_HORIZONTAL);
		worldLayout.add(buttonHeaderLayout);
			
			backButton = new Button(new Vector2i(100, 50), "Back to Main Menu");
			backButton.setWidth(165);
			backButton.setHeight(35);
			buttonHeaderLayout.add(backButton);
			
			Button saveWorldButton = new Button(new Vector2i(100, 50), "Save World");
			saveWorldButton.setWidth(165);
			saveWorldButton.setHeight(35);
			buttonHeaderLayout.add(saveWorldButton);
			
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
		
		
		
		//
		LinearLayout buttonRulesLayout = new LinearLayout(new Vector2i(355, 50), LinearLayout.Direction.HORIZONTAL);
		//buttonRulesLayout.setBackground(new Fill(new Colour(0.7f, 0.7f, 0.7f)));
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
			
		
		
		//
		
		
		
		
		 CALTextArea = new TextArea(100, 20);
		 CALTextArea.setFlag(Widget.FILL);
		 CALTextArea.setLineNumbers(true);
		
		
		ScrollableContainer textAreaContainer = new ScrollableContainer(new Vector2i(100,100));
		textAreaContainer.setFlag(Widget.FILL);
		masterRulesLayout.add(textAreaContainer);
		
		textAreaContainer.setContents(CALTextArea);
		
		
		distributionContainer = new Container(new Vector2i(100, 100));
		distributionContainer.setFlag(Widget.FILL);
		
		worldPreviewContainer = new Container(new Vector2i(100, 100));
		worldPreviewContainer.setFlag(Widget.FILL);
		
		

		tabbedWorldView.add(distributionContainer, "Distribution Settings");
		tabbedWorldView.add(worldPreviewContainer, "World Preview");
		
		
		
		
		
		
		
	 
		headingLabel = new TextWidget("Cellular Automata Simulator", Text.Size.LARGE, Colour.WHITE);
		headingLabel.setFlag(Widget.CENTER);
		
		
		////
		
		
		// Dialog
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
		
		
		
		////Simulation
		
		simulationContainer = new Container(new Vector2i(100,100));
		simulationContainer.setFlag(Widget.FILL);
		masterView.add(simulationContainer);
		
		
		LinearLayout masterSimulationLayout = new LinearLayout(LinearLayout.Direction.VERTICAL);
		masterSimulationLayout.setFlag(Widget.FILL);
		simulationContainer.setContents(masterSimulationLayout);
		
		masterSimulationLayout.add(worldHeaderLayout);
		
		Container simulationWindowContainer = new Container(new Vector2i(100,400));
		simulationWindowContainer.setFlag(Widget.FILL);
		simulationWindowContainer.setBackground(new Fill(new Colour(0f,0f,0f)));
		masterSimulationLayout.add(simulationWindowContainer);
		simulationWindowContainer.setContents(waterGrid3DViewer);
		
		
		
		
		

		//SLIDER
		
		LinearLayout sliderLayout = new LinearLayout(new Vector2i(40, 60), LinearLayout.Direction.VERTICAL);
		sliderLayout.setFlag(Widget.FILL_HORIZONTAL);
		masterSimulationLayout.add(sliderLayout);
		
		SliderWidget slider = new SliderWidget(100);
		slider.setFlag(Widget.FILL_HORIZONTAL);
		sliderLayout.add(slider);
		
		
		
		
		
		LinearLayout simulationControlsLayout = new LinearLayout(new Vector2i(100,135), LinearLayout.Direction.HORIZONTAL);
		//simulationControlsLayout.setMargin(new Vector2i(0,50));
		simulationControlsLayout.setFlag(Widget.FILL_HORIZONTAL);
		
		
		masterSimulationLayout.add(simulationControlsLayout);
		
		
		
		
		
		
		// WORLD DETAILS
		LinearLayout detailsLayout = new LinearLayout(new Vector2i(250, 1), LinearLayout.Direction.VERTICAL);
		detailsLayout.setBorder(new Fill(new Colour(0.7f, 0.7f, 0.7f)));
		detailsLayout.setFlag(Widget.FILL_VERTICAL);
		simulationControlsLayout.add(detailsLayout);
		
		LinearLayout innerDetailsLayout = new LinearLayout(new Vector2i(205, 55), LinearLayout.Direction.HORIZONTAL);
		innerDetailsLayout.setFlag(Widget.CENTER_HORIZONTAL);
		detailsLayout.add(innerDetailsLayout);
		
		

		
		
		ImageWidget detailsImage = new ImageWidget("data/details_header.png");
		detailsImage.setFlag(Widget.CENTER_HORIZONTAL);
		innerDetailsLayout.add(detailsImage);
		
		
		// PLAY BACK CONTROLS
		LinearLayout playbackLayout = new LinearLayout(new Vector2i(250, 1), LinearLayout.Direction.VERTICAL);
		playbackLayout.setBorder(new Fill(new Colour(0.7f, 0.7f, 0.7f)));
		playbackLayout.setFlag(Widget.FILL_VERTICAL);
		simulationControlsLayout.add(playbackLayout);
		
		LinearLayout innerPlaybackLayout = new LinearLayout(new Vector2i(205, 35), LinearLayout.Direction.HORIZONTAL);
		innerPlaybackLayout.setFlag(Widget.CENTER_HORIZONTAL);
		playbackLayout.add(innerPlaybackLayout);
		
		LinearLayout innerPlaybackLayout2 = new LinearLayout(new Vector2i(205,35), LinearLayout.Direction.HORIZONTAL);
		innerPlaybackLayout2.setFlag(Widget.FILL_HORIZONTAL);
		playbackLayout.add(innerPlaybackLayout2);
		
		LinearLayout innerPlaybackLayout3 = new LinearLayout(new Vector2i(205,35), LinearLayout.Direction.VERTICAL);
		innerPlaybackLayout3.setFlag(Widget.FILL_HORIZONTAL);
		playbackLayout.add(innerPlaybackLayout3);
		
		
		Button playButton = new Button(new Image("data/play_icon.png"));
		innerPlaybackLayout2.add(playButton);
		
		Button pauseButton = new Button(new Image("data/pause_icon.png"));
		innerPlaybackLayout2.add(pauseButton);
		
		
		
		
		
		Button resetButton = new Button(new Vector2i(70,30), "Reset");
		innerPlaybackLayout3.add(resetButton);
		
		
		
		
		

		
		
		ImageWidget playbackImage = new ImageWidget("data/playback_header.png");
		playbackImage.setFlag(Widget.CENTER_HORIZONTAL);
		innerPlaybackLayout.add(playbackImage);
		
		
		// CAMERA CONTROLS
		LinearLayout cameraLayout = new LinearLayout(new Vector2i(250, 1), LinearLayout.Direction.VERTICAL);
		cameraLayout.setBorder(new Fill(new Colour(0.7f, 0.7f, 0.7f)));
		cameraLayout.setFlag(Widget.FILL_VERTICAL);
		simulationControlsLayout.add(cameraLayout);
		
		LinearLayout innerCameraLayout = new LinearLayout(new Vector2i(205, 55), LinearLayout.Direction.HORIZONTAL);
		innerCameraLayout.setFlag(Widget.CENTER_HORIZONTAL);
		cameraLayout.add(innerCameraLayout);
		
		

		
		
		ImageWidget cameraImage = new ImageWidget("data/camera_header.png");
		cameraImage.setFlag(Widget.CENTER_HORIZONTAL);
		innerCameraLayout.add(cameraImage);	
		
		
		
		
		
		///
		window.relayout();
	}
	
	static public void main(String args[])
	{
		new UIProtoApplication();
	}
	
	@Override
	public void update(float delta)
	{
		if (!themeName.equals(currentThemeName))
		{
			System.out.println("Changing theme to "+themeName);
			
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
	public void handleWindowEvent(Event event)
	{
		if (event.type == Event.Type.ACTION)
		{
			if (event.target == createWorldButton)
			{
				
				masterView.setIndex(1 - masterView.getIndex());
				//buttonBarLayout.toggleVisibility();
				window.relayout();
				
				
				mainView.setIndex(1 - mainView.getIndex());
			}
			else if (event.target == loadWorldButton)
			{
				System.out.println(window.askUserForFile("Load a world"));
			}
			else if (event.target == helpButton)
			{
				mainView.setIndex(3 - mainView.getIndex());
			}
			else if (event.target == quitButton)
			{
				window.exit();
			}
			else if (event.target == nextIterationButton)
			{
				
					
			
			}
			else if (event.target == dialogOKButton)
			{
				window.closeModalDialog();
			}
			
			else if (event.target == backButton)
			{
				masterView.setIndex(0);
			}
			else if (event.target == submitButton)
			{
				
				
				if(( Integer.parseInt(worldSizeXTextBox.getText()) < 5) || ( Integer.parseInt(worldSizeYTextBox.getText()) < 5))
				{
					window.showModalDialog(dialog);
					return;
				}
			
			String shape = cellShapeDropDownBox.getSelectedText();
				
				
			
				if (shape == "Square")
				{
					// 3D Rectangle Grid
					
					rectGrid = new RectangleGrid(new Vector2i(Integer.parseInt(worldSizeXTextBox.getText()), Integer.parseInt(worldSizeYTextBox.getText())));
					rectGrid.getCell(2, 4).setValue(0, 1);
					rectGrid.getCell(3, 4).setValue(0, 1);
					rectGrid.getCell(4, 4).setValue(0, 1);
					rectGrid.getCell(4, 3).setValue(0, 1);
					rectGrid.getCell(3, 2).setValue(0, 1);
					rectGameOfLife = new GameOfLife(rectGrid);
					
					rectGrid3DViewer = new RectangleGrid3DWidget(new Vector2i(400, 300), (RectangleGrid)rectGameOfLife.getGrid(), 24);
					rectGrid3DViewer.setFlag(Widget.FILL);
					rectGrid3DViewer.addSlice(0, 16.0f);
					
					
					rectGridViewer = new RectangleGridWidget((RectangleGrid)rectGameOfLife.getGrid(), 16);
					rectGridViewer.setColourRuleSet(colourRules);
					
					widgetPreviewContainer.setContents(rectGridViewer);
					widget3DPreviewContainer.setContents(rectGrid3DViewer);
				}
				else if (shape == "Triangle")
				{
					// 3D Triangle Grid
					
					triGrid = new TriangleGrid(new Vector2i(Integer.parseInt(worldSizeXTextBox.getText()), Integer.parseInt(worldSizeYTextBox.getText())));
					triGrid.getCell(7, 6).setValue(0, 1);
					triGrid.getCell(7, 7).setValue(0, 1);
					triGrid.getCell(7, 8).setValue(0, 1);
					triGrid.getCell(6, 6).setValue(0, 1);
					triGrid.getCell(6, 7).setValue(0, 1);
					triGrid.getCell(6, 8).setValue(0, 1);
					triGameOfLife = new GameOfLife(triGrid);
		
					
					
					triGrid3DViewer = new TriangleGrid3DWidget(new Vector2i(400, 300), (TriangleGrid)triGameOfLife.getGrid(), 24);
					triGrid3DViewer.setFlag(Widget.FILL);
					triGrid3DViewer.addSlice(0, 16.0f);
					
					triGridViewer = new TriangleGridWidget((TriangleGrid)triGameOfLife.getGrid(), 32);
					triGridViewer.setColourRuleSet(colourRules);
					
					
					widgetPreviewContainer.setContents(triGridViewer);
					widget3DPreviewContainer.setContents(triGrid3DViewer);
					
				}
				else
				{
					grid = new HexagonGrid(new Vector2i(Integer.parseInt(worldSizeXTextBox.getText()), Integer.parseInt(worldSizeYTextBox.getText())));
					grid.getCell(6, 5).setValue(0, 1);
					grid.getCell(6, 6).setValue(0, 1);
					grid.getCell(6, 7).setValue(0, 1);		
					hexGameOfLife = new GameOfLife(grid);
					
					hexGrid3DViewer = new HexagonGrid3DWidget(new Vector2i(400, 300), (HexagonGrid)hexGameOfLife.getGrid(), 24);
					hexGrid3DViewer.setFlag(Widget.FILL);
				    hexGrid3DViewer.addSlice(0, 16.0f);
			
					gridViewer = new HexagonGridWidget((HexagonGrid)hexGameOfLife.getGrid(), 16);
					gridViewer.setColourRuleSet(colourRules);

				    widgetPreviewContainer.setContents(gridViewer);
					widget3DPreviewContainer.setContents(hexGrid3DViewer);
				}
			}
			else if (event.target == clearRulesButton)
			{
				CALTextArea.setText(" ");
	
			}
			
			else if (event.target == simulateButton)
			{
				masterView.setIndex(2);
			}
			
			
		}
		else if (event.type == Event.Type.CHANGE)
		{
			if (event.target == checkBox)
			{
				if (checkBox.isChecked())
					nameTextBox.setText("Flying Dutchmen");
				else
					nameTextBox.setText("Ostrich");
			}
			else if (event.target == cellShapeDropDownBox)
			{
	
				
				
			}
			
		}
	}
}
