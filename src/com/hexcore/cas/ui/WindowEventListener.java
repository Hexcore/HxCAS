package com.hexcore.cas.ui;

public interface WindowEventListener
{
	public void initialise();
	public void render();
	public void update(float delta);
	public void handleWindowEvent(Event event);
}
