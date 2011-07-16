package com.hexcore.cas.ui.test;

import com.hexcore.cas.math.Vector2i;
import com.hexcore.cas.ui.Button;
import com.hexcore.cas.ui.CheckBox;
import com.hexcore.cas.ui.Colour;
import com.hexcore.cas.ui.Container;
import com.hexcore.cas.ui.Event;
import com.hexcore.cas.ui.Fill;
import com.hexcore.cas.ui.ImageWidget;
import com.hexcore.cas.ui.LinearLayout;
import com.hexcore.cas.ui.Panel;
import com.hexcore.cas.ui.ScrollableContainer;
import com.hexcore.cas.ui.ShapeWidget;
import com.hexcore.cas.ui.Text;
import com.hexcore.cas.ui.TextBox;
import com.hexcore.cas.ui.TextWidget;
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
	public ShapeWidget	testShape;
	public ImageWidget	headingImage;
	public Container	headingContainer;
	public TextWidget	headingLabel;
	
	public Button		createWorldButton;
	public Button		loadWorldButton;
	public Button		optionsButton;
	public Button		helpButton;
	public Button		quitButton;
	
	public Panel		mainPanel;
	
	UITestApplication()
	{
		window = new Window("GUI Test", 800, 600);
		window.addListener(this);
		window.show();
	}
	
	public void initialise()
	{
		window.loadTheme("data/default.thm");
		
		windowLayout = new LinearLayout(window, LinearLayout.Direction.VERTICAL);
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
		
		ScrollableContainer	sc = new ScrollableContainer(new Vector2i(10, 10));
		sc.setMargin(new Vector2i(0, 0));
		sc.setFlag(Widget.FILL);
		mainPanel.setContents(sc);
		
		innerLayout = new LinearLayout(sc, LinearLayout.Direction.VERTICAL);
		innerLayout.setFlag(Widget.FILL);
		sc.setContents(innerLayout);
		
		nameTextBox = new TextBox(new Vector2i(100, 20));
		nameTextBox.setFlag(Widget.FILL_HORIZONTAL);
		nameTextBox.setText("Benny");
		innerLayout.add(nameTextBox);
		
		checkBox = new CheckBox(new Vector2i(100, 20), "Can fly");
		checkBox.setFlag(Widget.FILL_HORIZONTAL);
		innerLayout.add(checkBox);
		
		testShape = new ShapeWidget(new Vector2i(250, 250), new Fill(new Colour(1.0f, 0.0f, 0.0f), new Colour(0.0f, 1.0f, 0.0f)));
		testShape.setFlag(Widget.CENTER);
		innerLayout.add(testShape);
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
			if (event.target == loadWorldButton)
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
			else if (event.target == checkBox)
			{
				if (checkBox.isChecked())
					nameTextBox.setText("Flying Dutchmen");
				else
					nameTextBox.setText("Ostrich");
			}
		}
	}
}
