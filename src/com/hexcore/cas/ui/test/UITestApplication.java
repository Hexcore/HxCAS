package com.hexcore.cas.ui.test;

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

public class UITestApplication implements WindowEventListener
{
	public Window		window;
	
	public LinearLayout	windowLayout;
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
	public TabbedView			tabbedView;
	
	public ColourRuleSet		colourRules;
	
	public ScrollableContainer	rectGridViewerContainer;
	public GameOfLife			rectGameOfLife;
	public RectangleGridWidget	rectGridViewer; 
	
	public ScrollableContainer	gridViewerContainer;
	public GameOfLife			gameOfLife;
	public HexagonGridWidget	gridViewer;
	
	public ScrollableContainer	triGridViewerContainer;
	public GameOfLife			triGameOfLife;
	public TriangleGridWidget	triGridViewer;
	
	public RectangleGrid3DWidget	rectGrid3DViewer;
	public HexagonGrid3DWidget		hexGrid3DViewer;
	public TriangleGrid3DWidget		triGrid3DViewer;
	
	public WaterFlow				waterFlow;
	public RectangleGrid3DWidget	waterGrid3DViewer;
	
	public Button				nextIterationButton;
	
	UITestApplication()
	{
		HexagonGrid grid = new HexagonGrid(new Vector2i(12, 12));
		grid.getCell(6, 5).setValue(0, 1);
		grid.getCell(6, 6).setValue(0, 1);
		grid.getCell(6, 7).setValue(0, 1);		
		gameOfLife = new GameOfLife(grid);
		
		RectangleGrid rectGrid = new RectangleGrid(new Vector2i(12, 12));
		rectGrid.getCell(2, 4).setValue(0, 1);
		rectGrid.getCell(3, 4).setValue(0, 1);
		rectGrid.getCell(4, 4).setValue(0, 1);
		rectGrid.getCell(4, 3).setValue(0, 1);
		rectGrid.getCell(3, 2).setValue(0, 1);
		rectGameOfLife = new GameOfLife(rectGrid);
		
		TriangleGrid triGrid = new TriangleGrid(new Vector2i(14, 14));
		triGrid.getCell(7, 6).setValue(0, 1);
		triGrid.getCell(7, 7).setValue(0, 1);
		triGrid.getCell(7, 8).setValue(0, 1);
		triGrid.getCell(6, 6).setValue(0, 1);
		triGrid.getCell(6, 7).setValue(0, 1);
		triGrid.getCell(6, 8).setValue(0, 1);
		triGameOfLife = new GameOfLife(triGrid);		
		
		waterFlow = new WaterFlow();
		
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
		colourRule.addRange(new ColourRule.Range(0.0, 5.0, new Colour(0.5f, 0.25f, 0.0f), new Colour(0.0f, 0.8f, 0.5f)));
		colourRule.addRange(new ColourRule.Range(5.0, 10.1, new Colour(0.0f, 0.8f, 0.5f), new Colour(0.4f, 1.0f, 0.8f)));
		colourRules.setColourRule(2, colourRule);	
		
		window = new Window("GUI Test", 800, 600);
		window.addListener(this);
		window.show();
	}
	
	public void initialise()
	{
		window.loadTheme("data/default.thm");
		
		windowLayout = new LinearLayout(LinearLayout.Direction.VERTICAL);
		windowLayout.setFlag(Widget.FILL);
		windowLayout.setMargin(new Vector2i(0, 0));
		window.add(windowLayout);
		
		headerLayout = new LinearLayout(new Vector2i(100, 100), LinearLayout.Direction.HORIZONTAL);
		headerLayout.setFlag(Widget.FILL_HORIZONTAL);
		headerLayout.setMargin(new Vector2i(0, 0));
		headerLayout.setBackground(new Fill(Colour.WHITE, new Colour(1.0f, 1.0f, 1.0f, 0.0f)));
		windowLayout.add(headerLayout);
						
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
		windowLayout.add(mainLayout);
		
		buttonBarLayout = new LinearLayout(new Vector2i(250, 50), LinearLayout.Direction.VERTICAL);
		buttonBarLayout.setMargin(new Vector2i(0, 0));
		buttonBarLayout.setFlag(Widget.FILL_VERTICAL);
		mainLayout.add(buttonBarLayout);
		
		createWorldButton = new Button(new Vector2i(100, 50), "Create New World", "Specify parameters from scratch");
		createWorldButton.setFlag(Widget.FILL_HORIZONTAL);
		buttonBarLayout.add(createWorldButton);
		
		loadWorldButton = new Button(new Vector2i(100, 50), "Load World", "Load a world from a file");
		loadWorldButton.setFlag(Widget.FILL_HORIZONTAL);
		buttonBarLayout.add(loadWorldButton);
		
		optionsButton = new Button(new Vector2i(100, 50), "Options", "Change preferences and settings");
		optionsButton.setFlag(Widget.FILL_HORIZONTAL);
		buttonBarLayout.add(optionsButton);
		
		helpButton = new Button(new Vector2i(100, 50), "Help");
		helpButton.setFlag(Widget.FILL_HORIZONTAL);
		buttonBarLayout.add(helpButton);
		
		quitButton = new Button(new Vector2i(100, 50), "Quit");
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
		mainView.add(innerLayout);
		
		nameTextBox = new TextBox(new Vector2i(100, 20));
		nameTextBox.setFlag(Widget.FILL_HORIZONTAL);
		nameTextBox.setText("Benny");
		innerLayout.add(nameTextBox);
		
		checkBox = new CheckBox(new Vector2i(100, 20), "Can fly");
		checkBox.setFlag(Widget.FILL_HORIZONTAL);
		innerLayout.add(checkBox);
		
		dropDownBox = new DropDownBox(new Vector2i(200, 20));
		dropDownBox.addItem("Alpha");
		dropDownBox.addItem("Beta");
		dropDownBox.addItem("Delta");
		dropDownBox.addItem("Omega");
		dropDownBox.setSelected(1);
		innerLayout.add(dropDownBox);
		
		gridViewLayout = new LinearLayout(LinearLayout.Direction.VERTICAL);
		gridViewLayout.setFlag(Widget.FILL);
		mainView.add(gridViewLayout);
		
		tabbedView = new TabbedView(new Vector2i(30, 30));
		tabbedView.setFlag(Widget.FILL);
		gridViewLayout.add(tabbedView);
		
		// Hexagon Grid
		gridViewerContainer = new ScrollableContainer(new Vector2i(30, 30));
		gridViewerContainer.setFlag(Widget.FILL);
		gridViewerContainer.setBackground(new Fill(Colour.BLACK));
		tabbedView.add(gridViewerContainer, "Hexagon");
		
		gridViewer = new HexagonGridWidget((HexagonGrid)gameOfLife.getGrid(), 16);
		gridViewer.setColourRuleSet(colourRules);
		gridViewerContainer.setContents(gridViewer);
		
		// Rectangle Grid
		rectGridViewerContainer = new ScrollableContainer(new Vector2i(30, 30));
		rectGridViewerContainer.setFlag(Widget.FILL);
		rectGridViewerContainer.setBackground(new Fill(Colour.BLACK));
		tabbedView.add(rectGridViewerContainer, "Rectangle");
		
		rectGridViewer = new RectangleGridWidget((RectangleGrid)rectGameOfLife.getGrid(), 24);
		rectGridViewer.setColourRuleSet(colourRules);
		rectGridViewerContainer.setContents(rectGridViewer);
	
		// Triangle Grid
		triGridViewerContainer = new ScrollableContainer(new Vector2i(30, 30));
		triGridViewerContainer.setFlag(Widget.FILL);
		triGridViewerContainer.setBackground(new Fill(Colour.BLACK));
		tabbedView.add(triGridViewerContainer, "Triangle");
		
		triGridViewer = new TriangleGridWidget((TriangleGrid)triGameOfLife.getGrid(), 32);
		triGridViewer.setColourRuleSet(colourRules);
		triGridViewerContainer.setContents(triGridViewer);	
		
		nextIterationButton = new Button(new Vector2i(100, 50), "Next");
		gridViewLayout.add(nextIterationButton);
		
		// 3D Rectangle Grid
		rectGrid3DViewer = new RectangleGrid3DWidget(new Vector2i(400, 300), (RectangleGrid)rectGameOfLife.getGrid(), 24);
		rectGrid3DViewer.setFlag(Widget.FILL);
		rectGrid3DViewer.setColourRuleSet(colourRules);
		rectGrid3DViewer.addSlice(0, 16.0f);
		tabbedView.add(rectGrid3DViewer, "3D Rect");
		
		// 3D Hexagon Grid
		hexGrid3DViewer = new HexagonGrid3DWidget(new Vector2i(400, 300), (HexagonGrid)gameOfLife.getGrid(), 24);
		hexGrid3DViewer.setFlag(Widget.FILL);
		hexGrid3DViewer.setColourRuleSet(colourRules);
		hexGrid3DViewer.addSlice(0, 16.0f);
		tabbedView.add(hexGrid3DViewer, "3D Hex");
		
		// 3D Triangle Grid
		triGrid3DViewer = new TriangleGrid3DWidget(new Vector2i(400, 300), (TriangleGrid)triGameOfLife.getGrid(), 24);
		triGrid3DViewer.setFlag(Widget.FILL);
		triGrid3DViewer.setColourRuleSet(colourRules);
		triGrid3DViewer.addSlice(0, 16.0f);
		tabbedView.add(triGrid3DViewer, "3D Tri");
		
		// Water Flow
		waterGrid3DViewer = new RectangleGrid3DWidget(new Vector2i(400, 300), waterFlow.getGrid(), 24);
		waterGrid3DViewer.setFlag(Widget.FILL);
		waterGrid3DViewer.setColourRuleSet(colourRules);
		waterGrid3DViewer.addSlice(2, 2, 5.0f);
		waterGrid3DViewer.addSlice(1, 1, 5.0f);
		tabbedView.add(waterGrid3DViewer, "Water Flow");
		
		window.relayout();
	}
	
	static public void main(String args[])
	{
		new UITestApplication();
	}

	@Override
	public void handleWindowEvent(Event event)
	{
		if (event.type == Event.Type.ACTION)
		{
			if (event.target == createWorldButton)
			{
				mainView.setIndex(1 - mainView.getIndex());
			}
			else if (event.target == loadWorldButton)
			{
				System.out.println(window.askUserForFile("Load a world"));
			}
			else if (event.target == helpButton)
			{
				mainPanel.toggleVisibility();
			}
			else if (event.target == quitButton)
			{
				window.exit();
			}
			else if (event.target == nextIterationButton)
			{
				switch (tabbedView.getIndex())
				{
					case 4:
					case 0:
						gameOfLife.generateNextGeneration();
						gridViewer.setGrid((HexagonGrid)gameOfLife.getGrid());
						hexGrid3DViewer.setGrid((HexagonGrid)gameOfLife.getGrid());
						break;
					case 1:
					case 3:
						rectGameOfLife.generateNextGeneration();
						rectGridViewer.setGrid((RectangleGrid)rectGameOfLife.getGrid());
						rectGrid3DViewer.setGrid((RectangleGrid)rectGameOfLife.getGrid());
						break;
					case 5:
					case 2:
						triGameOfLife.generateNextGeneration();
						triGridViewer.setGrid((TriangleGrid)triGameOfLife.getGrid());
						triGrid3DViewer.setGrid((TriangleGrid)triGameOfLife.getGrid());
						break;		
					case 6:
						waterFlow.generateNextGeneration();
						waterGrid3DViewer.setGrid(waterFlow.getGrid());
						break;				
				}
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
