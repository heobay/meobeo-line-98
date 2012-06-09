package sunnet.meobeo.framework;

public abstract class Screen {
	protected final Game game;

	public Screen(Game game) {
		this.game = game;
	}
	
	public Screen(Game game, float scaleX, float scaleY) {
		this.game = game;
	}

	public abstract void update(long deltaTime);

	public abstract void present(long deltaTime);

	public abstract void pause();

	public abstract void resume();

	public abstract void dispose();
}
