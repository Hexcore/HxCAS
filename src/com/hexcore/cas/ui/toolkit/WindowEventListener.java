package com.hexcore.cas.ui.toolkit;

public interface WindowEventListener
{
	public void initialise();
	public void render();
	public void update(float delta);
	public boolean close();
	public void handleWindowEvent(Event event);
}
