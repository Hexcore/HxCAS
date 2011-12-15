package com.hexcore.cas.ui.test;

import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.model.ColourRule;
import com.hexcore.cas.model.ColourRuleSet;
import com.hexcore.cas.model.HexagonGrid;
import com.hexcore.cas.model.RectangleGrid;
import com.hexcore.cas.model.TriangleGrid;
import com.hexcore.cas.test.GameOfLife;
import com.hexcore.cas.test.WaterFlow;
import com.hexcore.cas.ui.toolkit.Button;
import com.hexcore.cas.ui.toolkit.CheckBox;
import com.hexcore.cas.ui.toolkit.Colour;
import com.hexcore.cas.ui.toolkit.ColourPickerDialog;
import com.hexcore.cas.ui.toolkit.Container;
import com.hexcore.cas.ui.toolkit.Dialog;
import com.hexcore.cas.ui.toolkit.DropDownBox;
import com.hexcore.cas.ui.toolkit.Event;
import com.hexcore.cas.ui.toolkit.Fill;
import com.hexcore.cas.ui.toolkit.HexagonGrid3DWidget;
import com.hexcore.cas.ui.toolkit.HexagonGridWidget;
import com.hexcore.cas.ui.toolkit.ImageWidget;
import com.hexcore.cas.ui.toolkit.LayoutParser;
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
import com.hexcore.cas.ui.toolkit.TextArea;
import com.hexcore.cas.ui.toolkit.TextBox;
import com.hexcore.cas.ui.toolkit.TextWidget;
import com.hexcore.cas.ui.toolkit.Theme;
import com.hexcore.cas.ui.toolkit.TriangleGrid3DWidget;
import com.hexcore.cas.ui.toolkit.TriangleGridWidget;
import com.hexcore.cas.ui.toolkit.View;
import com.hexcore.cas.ui.toolkit.Widget;
import com.hexcore.cas.ui.toolkit.Window;
import com.hexcore.cas.ui.toolkit.WindowEventListener;

public class UITestApplication implements WindowEventListener
{
	public Theme		theme;
	public Window		window;
	
	public LinearLayout	windowLayout;
	public LinearLayout	headerLayout;
	public LinearLayout	mainLayout;
	public LinearLayout	buttonBarLayout;
	public LinearLayout	outerLayout;
	public LinearLayout	innerLayout;
	
	public TextBox		nameTextBox;
	public TextBox		nameTextBox2;
	public NumberBox	valueBox;
	public CheckBox		checkBox;
	public DropDownBox	dropDownBox;
	public TextWidget	themeLabel;
	public TextWidget	paragraph;
	public TextArea		description;
	public ImageWidget	headingImage;
	public Container	headingContainer;
	public TextWidget	headingLabel;
	public SliderWidget	slider;
	public ListWidget	listWidget;
	public View			mainView;
	
	public ScrollableContainer	listScroll;
	public LinearLayout			list;
	
	public Button		createWorldButton;
	public Button		loadWorldButton;
	public Button		optionsButton;
	public Button		wrenchButton;
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
	
	public ColourPickerDialog colourDialog;
	
	public String	currentThemeName = "light";
	public String	themeName = currentThemeName;
	
	private LayoutParser layoutParser = new LayoutParser();
	
	UITestApplication()
	{
		waterFlowGrid = new HexagonGrid(new Vector2i(128, 128), 3);
		waterFlowGrid.setWrappable(false);

		waterFlow = new WaterFlow(waterFlowGrid);
		
		HexagonGrid grid = new HexagonGrid(new Vector2i(22, 22), 1);
		grid.getCell(6, 5).setValue(0, 1);
		grid.getCell(6, 6).setValue(0, 1);
		grid.getCell(6, 7).setValue(0, 1);		
		gameOfLife = new GameOfLife(grid);
		
		RectangleGrid rectGrid = new RectangleGrid(new Vector2i(22, 22), 1);
		rectGrid.getCell(2, 4).setValue(0, 1);
		rectGrid.getCell(3, 4).setValue(0, 1);
		rectGrid.getCell(4, 4).setValue(0, 1);
		rectGrid.getCell(4, 3).setValue(0, 1);
		rectGrid.getCell(3, 2).setValue(0, 1);
		rectGameOfLife = new GameOfLife(rectGrid);
		
		TriangleGrid triGrid = new TriangleGrid(new Vector2i(24, 24), 1);
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
		window = new Window("GUI Test", 1024, 768, theme);
		window.addListener(this);
		window.show();
	}
	
	public void initialise()
	{
		theme.loadTheme(themeName);
			
		windowLayout = (LinearLayout)layoutParser.parse("UITest/window", window);
		
		mainView = (View)windowLayout.findByName("mainView");
		
		checkBox = (CheckBox)mainView.findByName("canFly");
		nameTextBox = (TextBox)mainView.findByName("name");
		dropDownBox = (DropDownBox)mainView.findByName("themeSelector");
		tabbedView = (TabbedView)mainView.findByName("tabbedView");
		nextIterationButton = (Button)mainView.findByName("nextIteration");

		// Hexagon Grid
		gridViewerContainer = (ScrollableContainer)mainView.findByName("hexGridViewerContainer");

		gridViewer = new HexagonGridWidget((HexagonGrid)gameOfLife.getGrid(), 16);
		gridViewer.setColourRuleSet(colourRules);
		gridViewerContainer.setContents(gridViewer);
		
		// Rectangle Grid
		rectGridViewerContainer = (ScrollableContainer)mainView.findByName("rectGridViewerContainer");

		rectGridViewer = new RectangleGridWidget((RectangleGrid)rectGameOfLife.getGrid(), 24);
		rectGridViewer.setColourRuleSet(colourRules);
		rectGridViewerContainer.setContents(rectGridViewer);
	
		// Triangle Grid
		triGridViewerContainer = (ScrollableContainer)mainView.findByName("triGridViewerContainer");

		triGridViewer = new TriangleGridWidget((TriangleGrid)triGameOfLife.getGrid(), 32);
		triGridViewer.setColourRuleSet(colourRules);
		triGridViewerContainer.setContents(triGridViewer);	
	
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
		
		// Colour picker dialog
		colourDialog = new ColourPickerDialog(window);
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
		return true;
	}
	
	@Override
	public void handleWindowEvent(Event event)
	{
		if (event.type == Event.Type.ACTION)
		{
			String name = event.target.getName();
			
			if (name.equals("createWorld"))
			{
				mainView.setIndex(1 - mainView.getIndex());
				
				window.setFullscreen(mainView.getIndex() == 1);
			}
			else if (name.equals("loadWorld"))
			{
				System.out.println(window.askUserForFileToSave("Load a world", "txt"));
			}
			else if (name.equals("options"))
			{
				window.showModalDialog(dialog);
			}
			else if (event.target == dialogOKButton)
			{
				window.closeModalDialog();
			}
			else if (name.equals("help"))
			{
				window.showModalDialog(colourDialog);
			}
			else if (name.equals("quit"))
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
			else if (colourDialog.isApplyButton(event.target))
			{
				window.closeModalDialog();
				windowLayout.setBackground(new Fill(colourDialog.getColour()));
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
