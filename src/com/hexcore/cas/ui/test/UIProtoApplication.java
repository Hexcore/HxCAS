package com.hexcore.cas.ui.test;

import java.awt.Color;

import com.hexcore.cas.math.Vector2i;
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
	
	
	//1st,2,3,4 ...tab
	
	
	public Container propertiesContainer;
		
		public LinearLayout propertiesLayout;
		public LinearLayout worldSizeLayout;
	
		public TextWidget worldSizeLabel;
		public TextWidget worldSizeXLabel;
		public TextWidget cellShapeLabel;
		public TextWidget numCellPropertiesLabel;
		
		public TextBox	worldSizeXTextBox;
		public TextBox	worldSizeYTextBox;
		public TextBox	numCellPropertiesTextBox;
		
		public CheckBox wrapCheckBox;
		public DropDownBox cellShapeDropDownBox;
		
		
		
	public Container colorRulesContainer;
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
	
	UIProtoApplication()
	{
		window = new Window("Cellular Automata Simulator - v1.0", 800, 600);
		window.addListener(this);
		window.show();
	}
	
	public void initialise()
	{
		window.loadTheme("data/default.thm");
		
		
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
		headerLayout.setBackground(new Fill(Colour.WHITE, new Colour(1.0f, 1.0f, 1.0f, 0.0f)));
		mainMenuLayout.add(headerLayout);
						
		headingImage = new ImageWidget("data/logo.png");
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
		
		createWorldButton = new Button(new Vector2i(100, 50), "Create New World", "Blank world. Parameters must be specified");
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
		tabbedWorldView.setBackground(new Fill(new Colour(0.9f, 0.88f, 0.82f)));
		worldLayout.add(tabbedWorldView);
		
		// OUR WORLD EDITOR Containers
		
		propertiesContainer = new Container(new Vector2i(100, 100));
		propertiesContainer.setFlag(Widget.FILL);
		tabbedWorldView.add(propertiesContainer, "World Properties");
		
		
		propertiesLayout = new LinearLayout(LinearLayout.Direction.VERTICAL);
		propertiesContainer.setContents(propertiesLayout);
		
		worldSizeLayout = new LinearLayout(LinearLayout.Direction.HORIZONTAL);
		propertiesLayout.add(worldSizeLayout);
		
		worldSizeLabel = new TextWidget("World Size:");
		worldSizeLayout.add(worldSizeLabel);
		
		worldSizeXTextBox = new TextBox(new Vector2i(50,30));
		worldSizeLayout.add(worldSizeXTextBox);
		
		worldSizeXLabel = new TextWidget("X");
		worldSizeLayout.add(worldSizeXLabel);
		
		worldSizeYTextBox = new TextBox(new Vector2i(50,30));
		worldSizeLayout.add(worldSizeYTextBox);
		
	
		/*
		
		cellShapeLabel = new TextWidget("Cell Shape:");
		propertiesLayout.add(cellShapeLabel);
		
		cellShapeDropDownBox = new DropDownBox(new Vector2i(100,50));
		cellShapeDropDownBox.addItem("Square");
		cellShapeDropDownBox.addItem("Triangle");
		cellShapeDropDownBox.addItem("Hexagon");
		
		propertiesLayout.add(cellShapeDropDownBox);
				
		
		numCellPropertiesLabel = new TextWidget("Number of Cell Properties:");
		propertiesLayout.add(numCellPropertiesLabel);
		
		numCellPropertiesTextBox = new TextBox(new Vector2i(100,50));
		propertiesLayout.add(numCellPropertiesTextBox);
		
		wrapCheckBox = new CheckBox(new Vector2i(100,50), "Wrappable");
		
		*/
		
		
		
		
		
		colorRulesContainer = new Container(new Vector2i(100, 100));
		colorRulesContainer.setFlag(Widget.FILL);
		
		distributionContainer = new Container(new Vector2i(100, 100));
		distributionContainer.setFlag(Widget.FILL);
		
		worldPreviewContainer = new Container(new Vector2i(100, 100));
		worldPreviewContainer.setFlag(Widget.FILL);
		
		
		tabbedWorldView.add(colorRulesContainer, "Color Range Rules");
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
			else if (event.target == dropDownBox)
			{
				nameTextBox.setText(dropDownBox.getSelectedText());
			}
		}
	}
}
