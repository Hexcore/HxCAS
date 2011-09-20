package com.hexcore.cas.ui.toolkit;

public interface WindowEventListener
{
	public void initialise();
	public void render();
	public void update(float delta);
	public void handleWindowEvent(Event event);
}
