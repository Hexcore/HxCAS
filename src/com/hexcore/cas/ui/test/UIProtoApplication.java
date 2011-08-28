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
import com.hexcore.cas.ui.DropDownBox;
import com.hexcore.cas.ui.Event;
import com.hexcore.cas.ui.Fill;
import com.hexcore.cas.ui.HexagonGrid3DWidget;
import com.hexcore.cas.ui.HexagonGridWidget;
import com.hexcore.cas.ui.ImageWidget;
import com.hexcore.cas.ui.LinearLayout;
import com.hexcore.cas.ui.Panel;
import com.hexcore.cas.ui.RectangleGrid3DWidget;
import com.hexcore.cas.ui.RectangleGridWidget;
import com.hexcore.cas.ui.ScrollableContainer;
import com.hexcore.cas.ui.TabbedView;
import com.hexcore.cas.ui.Text;
import com.hexcore.cas.ui.Text.Size;
import com.hexcore.cas.ui.TextBox;
import com.hexcore.cas.ui.TextWidget;
import com.hexcore.cas.ui.TriangleGrid3DWidget;
import com.hexcore.cas.ui.TriangleGridWidget;
import com.hexcore.cas.ui.View;
import com.hexcore.cas.ui.Widget;
import com.hexcore.cas.ui.Window;
import com.hexcore.cas.ui.WindowEventListener;

public class UIProtoApplication implements WindowEventListener
{
	public Window		window;
	
	
	public View masterView;
	
	
	public TabbedView tabbedWorldView;
	
	public LinearLayout	mainMenuLayout;
	public LinearLayout worldLayout;
	
	
	public TextWidget worldEditorLabel;
	
	//1st,2,3,4 ...tab
	
	
	public Container propertiesContainer;
		
		public LinearLayout masterPropertiesLayout;
		public Container widgetPreviewContainer;
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
		
		public RectangleGrid3DWidget	rectGrid3DViewer;
		public HexagonGrid3DWidget		hexGrid3DViewer;
		public TriangleGrid3DWidget		triGrid3DViewer;
		
		
	public Container rulesContainer;
		public LinearLayout rulesLayout;
		

	
	
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
	
	
	
	UIProtoApplication()
	{
		
		
		HexagonGrid grid = new HexagonGrid(new Vector2i(12, 12));
		grid.getCell(6, 5).setValue(0, 1);
		grid.getCell(6, 6).setValue(0, 1);
		grid.getCell(6, 7).setValue(0, 1);		
		hexGameOfLife = new GameOfLife(grid);
		
		RectangleGrid rectGrid = new RectangleGrid(new Vector2i(12, 12));
		rectGrid.getCell(2, 4).setValue(0, 1);
		rectGrid.getCell(3, 4).setValue(0, 1);
		rectGrid.getCell(4, 4).setValue(0, 1);
		rectGrid.getCell(4, 3).setValue(0, 1);
		rectGrid.getCell(3, 2).setValue(0, 1);
		rectGameOfLife = new GameOfLife(rectGrid);
		
		TriangleGrid triGrid = new TriangleGrid(new Vector2i(12, 12));
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
		
		
		window = new Window("Cellular Automata Simulator - v1.0", 1024, 700);
		
		window.addListener(this);
		window.show();
	}
	
	public void initialise()
	{
		window.loadTheme("data/"+themeName+".thm");
		
		
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
		
		headingLabel = new TextWidget("Cellular Automata Simulator", Text.Size.LARGE, Colour.WHITE);
		headingLabel.setFlag(Widget.CENTER);
		headingContainer.setContents(headingLabel);
		
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
		
		
		LinearLayout gutterLayout = new LinearLayout(LinearLayout.Direction.HORIZONTAL);
		gutterLayout.setHeight(55);
		gutterLayout.setFlag(Widget.FILL_HORIZONTAL);
		gutterLayout.setBackground(new Fill(new Colour(0.5f,0.5f,0.4f)));
		
		worldLayout.add(gutterLayout);
		
		
	
		
		
		worldEditorLabel = new TextWidget("World Editor Menu", Text.Size.LARGE);
		
		
		worldEditorLabel.setFlag(Widget.CENTER_HORIZONTAL);
		worldLayout.add(worldEditorLabel);
		worldLayout.add(tabbedWorldView);
		
		// OUR WORLD EDITOR Containers
		
		
		propertiesContainer = new Container(new Vector2i(100, 100));	
		propertiesContainer.setFlag(Widget.FILL);
		tabbedWorldView.add(propertiesContainer, "World Properties");
		
		masterPropertiesLayout = new LinearLayout(LinearLayout.Direction.VERTICAL);
		masterPropertiesLayout.setFlag(Widget.FILL);
		propertiesContainer.setContents(masterPropertiesLayout);
		
	
		
   	LinearLayout instructionsLayout = new LinearLayout(LinearLayout.Direction.HORIZONTAL);
   	instructionsLayout.setBackground(new Fill(new Colour(0.4f, 0.63f, 0.91f)));

   	instructionsLayout.setHeight(35);

   	instructionsLayout.setFlag(Widget.CENTER_HORIZONTAL);
   	   	masterPropertiesLayout.add(instructionsLayout);
   	
	instructionsLayout.setBorder(new Fill(new Colour(0.6f,0.6f,0.6f)));
	
   
	
	TextWidget propertiesInstructions = new TextWidget("Specify your world properties such as cell shape, world size and whether the world is wrappable.");
	instructionsLayout.add(propertiesInstructions);
		
   	instructionsLayout.setWidth(propertiesInstructions.getWidth()+ 20);
		
		
		rectGrid3DViewer = new RectangleGrid3DWidget(new Vector2i(400, 300), (RectangleGrid)rectGameOfLife.getGrid(), 24);
		rectGrid3DViewer.setFlag(Widget.FILL);
		rectGrid3DViewer.addSlice(0, 16.0f);
		
		triGrid3DViewer = new TriangleGrid3DWidget(new Vector2i(400, 300), (TriangleGrid)triGameOfLife.getGrid(), 24);
		triGrid3DViewer.setFlag(Widget.FILL);
		triGrid3DViewer.addSlice(0, 16.0f);
		
		hexGrid3DViewer = new HexagonGrid3DWidget(new Vector2i(400, 300), (HexagonGrid)hexGameOfLife.getGrid(), 24);
		hexGrid3DViewer.setFlag(Widget.FILL);
	    hexGrid3DViewer.addSlice(0, 16.0f);
		
		
		
		propertiesLayout = new LinearLayout(LinearLayout.Direction.VERTICAL);
		propertiesLayout.setFlag(Widget.FILL);
		propertiesLayout.setBorder(new Fill(new Colour(0.6f,0.6f,0.6f)));
		masterPropertiesLayout.add(propertiesLayout);
		
		
		widgetPreviewContainer = new Container(new Vector2i (100,100));
		widgetPreviewContainer.setFlag(Widget.FILL);
		masterPropertiesLayout.add(widgetPreviewContainer);
		
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
		widgetPreviewContainer.setContents(rectGrid3DViewer);
		
		
		cellShapeLayout.add(cellShapeDropDownBox);
				
		
		
		
		wrapCheckBox = new CheckBox(new Vector2i(100,50), "Wrappable");
		propertiesLayout.add(wrapCheckBox);
		
		
		
		
		
		
		rulesContainer = new Container(new Vector2i(100, 100));
		rulesContainer.setFlag(Widget.FILL);
		
		distributionContainer = new Container(new Vector2i(100, 100));
		distributionContainer.setFlag(Widget.FILL);
		
		worldPreviewContainer = new Container(new Vector2i(100, 100));
		worldPreviewContainer.setFlag(Widget.FILL);
		
		
		tabbedWorldView.add(rulesContainer, "CAL Rules");
		tabbedWorldView.add(distributionContainer, "Distribution Settings");
		tabbedWorldView.add(worldPreviewContainer, "World Preview");
		
		
		
		
	 
		headingLabel = new TextWidget("Cellular Automata Simulator", Text.Size.LARGE, Colour.WHITE);
		headingLabel.setFlag(Widget.CENTER);
		
		
		
		
		////
			
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
			
			window.loadTheme("data/"+themeName+".thm");
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
				String shape = cellShapeDropDownBox.getSelectedText();
				
				if (shape == "Square")
				{
					// 3D Rectangle Grid
					
					widgetPreviewContainer.setContents(rectGrid3DViewer);
				}
				else if (shape == "Triangle")
				{
					// 3D Triangle Grid
					
					widgetPreviewContainer.setContents(triGrid3DViewer);
					
				}
				else
				{
					
					widgetPreviewContainer.setContents(hexGrid3DViewer);
				}
				
				
			}
		}
	}
}
