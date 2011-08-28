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
import com.hexcore.cas.ui.Dialog;
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
import com.hexcore.cas.ui.TextArea;
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
	public LinearLayout	outerLayout;
	public LinearLayout	innerLayout;
	
	public TextBox		nameTextBox;
	public TextBox		nameTextBox2;
	public CheckBox		checkBox;
	public DropDownBox	dropDownBox;
	public TextWidget	themeLabel;
	public TextWidget	paragraph;
	public TextArea		description;
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
	
	public HexagonGrid				waterFlowGrid;
	public WaterFlow				waterFlow;
	public HexagonGrid3DWidget		waterGrid3DViewer;
	
	public Button	nextIterationButton;

	public Dialog		dialog;
	public LinearLayout	dialogLayout;
	public TextWidget	dialogTitle;
	public TextWidget	dialogMessage;
	public Button		dialogOKButton;
	
	public String	currentThemeName = "light";
	public String	themeName = currentThemeName;
	
	UITestApplication()
	{


		waterFlowGrid = new HexagonGrid(new Vector2i(128, 128));
		waterFlowGrid.setWrappable(false);

		waterFlow = new WaterFlow(waterFlowGrid);
		
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
		
		window = new Window("GUI Test", 1024, 768);
		window.addListener(this);
		window.show();
	}
	
	public void initialise()
	{
		window.loadTheme("data/"+themeName+".thm");
		
		windowLayout = new LinearLayout(LinearLayout.Direction.VERTICAL);
		windowLayout.setFlag(Widget.FILL);
		windowLayout.setMargin(new Vector2i(0, 0));
		window.add(windowLayout);
		
		headerLayout = new LinearLayout(new Vector2i(100, 100), LinearLayout.Direction.HORIZONTAL);
		headerLayout.setFlag(Widget.FILL_HORIZONTAL);
		headerLayout.setMargin(new Vector2i(0, 0));
		headerLayout.setThemeClass("Header");
		windowLayout.add(headerLayout);
						
		headingImage = new ImageWidget("data/logo.png");
		headingImage.setFlag(Widget.CENTER);
		headerLayout.add(headingImage);		
		
		headingContainer = new Container(new Vector2i(100, 100));
		headingContainer.setFlag(Widget.FILL);
		headerLayout.add(headingContainer);
		
		headingLabel = new TextWidget("Cellular Automata Simulator", Text.Size.LARGE, Colour.BLACK);
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
		
		outerLayout = new LinearLayout(LinearLayout.Direction.HORIZONTAL);
		outerLayout.setMargin(new Vector2i(0, 0));
		outerLayout.setFlag(Widget.FILL);
		outerLayout.setFlag(Widget.WRAP);
		mainView.add(outerLayout);
		
		innerLayout = new LinearLayout(LinearLayout.Direction.VERTICAL);
		innerLayout.setFlag(Widget.FILL);
		innerLayout.setFlag(Widget.WRAP);
		outerLayout.add(innerLayout);
		
		nameTextBox = new TextBox(100);
		nameTextBox.setFlag(Widget.FILL_HORIZONTAL);
		nameTextBox.setText("Benny");
		innerLayout.add(nameTextBox);
		
		
		nameTextBox2 = new TextBox(100);
		nameTextBox2.setFlag(Widget.FILL_HORIZONTAL);
		nameTextBox2.setText("Benny2");
		innerLayout.add(nameTextBox2);
		
		checkBox = new CheckBox(new Vector2i(100, 20), "Can fly");
		checkBox.setFlag(Widget.FILL_HORIZONTAL);
		innerLayout.add(checkBox);
		
		themeLabel = new TextWidget("Choose a theme:");
		themeLabel.setFlag(Widget.FILL_HORIZONTAL);
		innerLayout.add(themeLabel);	
		
		dropDownBox = new DropDownBox(new Vector2i(200, 20));
		dropDownBox.addItem("light");
		dropDownBox.addItem("default");
		dropDownBox.addItem("blue");
		dropDownBox.setSelected(1);
		innerLayout.add(dropDownBox);
		
		paragraph = new TextWidget("This is a lot of text.\nIt is going to fill the whole width of the screen and then start overflowing to the next line.\nIf it hasn't already then this extra sentence should help force it over the edge. Actually this is now wrapped.\nIt works!");
		paragraph.setFlag(Widget.FILL_HORIZONTAL);
		paragraph.setFlowed(true);
		innerLayout.add(paragraph);
		
		description = new TextArea(100, 20);
		description.setFlag(Widget.FILL);
		description.setLineNumbers(true);
		outerLayout.add(description);
		
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
		rectGrid3DViewer.setBackgroundColour(Colour.WHITE);
		rectGrid3DViewer.setColourRuleSet(colourRules);
		rectGrid3DViewer.addSlice(0, 16.0f);
		tabbedView.add(rectGrid3DViewer, "3D Rect");
		
		// 3D Hexagon Grid
		hexGrid3DViewer = new HexagonGrid3DWidget(new Vector2i(400, 300), (HexagonGrid)gameOfLife.getGrid(), 24);
		hexGrid3DViewer.setFlag(Widget.FILL);
		hexGrid3DViewer.setBackgroundColour(Colour.WHITE);
		hexGrid3DViewer.setColourRuleSet(colourRules);
		hexGrid3DViewer.addSlice(0, 16.0f);
		tabbedView.add(hexGrid3DViewer, "3D Hex");
		
		// 3D Triangle Grid
		triGrid3DViewer = new TriangleGrid3DWidget(new Vector2i(400, 300), (TriangleGrid)triGameOfLife.getGrid(), 24);
		triGrid3DViewer.setFlag(Widget.FILL);
		triGrid3DViewer.setBackgroundColour(Colour.WHITE);
		triGrid3DViewer.setColourRuleSet(colourRules);
		triGrid3DViewer.addSlice(0, 16.0f);
		tabbedView.add(triGrid3DViewer, "3D Tri");
		
		// Water Flow
		waterGrid3DViewer = new HexagonGrid3DWidget(new Vector2i(400, 300), (HexagonGrid)waterFlow.getGrid(), 24);
		waterGrid3DViewer.setFlag(Widget.FILL);
		waterGrid3DViewer.setBackgroundColour(new Colour(0.6f, 0.85f, 1.0f));
		waterGrid3DViewer.setColourRuleSet(colourRules);
		waterGrid3DViewer.addSlice(2, 2, 15.0f);
		waterGrid3DViewer.addSlice(1, 1, 15.0f);
		tabbedView.add(waterGrid3DViewer, "Water Flow");
		
		window.relayout();
		
		// Dialog
		dialog = new Dialog(window, new Vector2i(400, 200));
		
		dialogLayout = new LinearLayout(LinearLayout.Direction.VERTICAL);
		dialogLayout.setFlag(Widget.FILL);
		dialog.setContents(dialogLayout);
		
		dialogTitle = new TextWidget("Message Title", Text.Size.LARGE);
		dialogTitle.setFlag(Widget.CENTER_HORIZONTAL);
		dialogLayout.add(dialogTitle);
		
		dialogMessage = new TextWidget("The message itself is much more interesting to read, but most people skip it.");
		dialogMessage.setFlag(Widget.FILL_HORIZONTAL);
		dialogMessage.setFlag(Widget.FILL_VERTICAL); // This pushes the OK button down because it fills the space in between
		dialogMessage.setFlowed(true);
		dialogLayout.add(dialogMessage);
		
		dialogOKButton = new Button(new Vector2i(120, 30), "OK");
		dialogOKButton.setFlag(Widget.CENTER_HORIZONTAL);
		dialogLayout.add(dialogOKButton);
	}
	
	static public void main(String args[])
	{
		new UITestApplication();
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
				mainView.setIndex(1 - mainView.getIndex());
				
				window.setFullscreen(mainView.getIndex() == 1);
			}
			else if (event.target == loadWorldButton)
			{
				System.out.println(window.askUserForFile("Load a world"));
			}
			else if (event.target == optionsButton)
			{
				window.showModalDialog(dialog);
			}
			else if (event.target == dialogOKButton)
			{
				window.closeModalDialog();
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
						waterGrid3DViewer.setGrid((HexagonGrid)waterFlow.getGrid());
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
				themeName = dropDownBox.getSelectedText();
				nameTextBox.setText(dropDownBox.getSelectedText());
			}
		}
	}
}
